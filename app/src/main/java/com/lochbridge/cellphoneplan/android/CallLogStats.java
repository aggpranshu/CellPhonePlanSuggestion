
package com.lochbridge.cellphoneplan.android;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.lochbridge.cellphoneplan.spring.AggregatedLogStats;
import com.lochbridge.cellphoneplan.spring.BillPlansList;
import com.lochbridge.cellphoneplan.spring.ListPlanDetailsByDuration;
import com.lochbridge.cellphoneplan.urls.URLClass;

public class CallLogStats extends Activity {

    List<ListPlanDetailsByDuration> listPlanDetailsByDuration;
    LinearLayout linearLayoutChart;
    CallLogs object;
    private PieChart mChart;
    private String providerName;
    private Button buttonBill;

    private AggregatedLogStats aggregatedLogStats;
    private int smsCount = 0;
    private String[] projectionCall = new String[] {
            CallLog.Calls.DATE,
            CallLog.Calls.NUMBER,
            CallLog.Calls.DURATION,
            CallLog.Calls.TYPE
    };

    private String[] projectionMesg = new String[] {
            "type", "date"
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        linearLayoutChart = (LinearLayout) findViewById(R.id.aggregatedDataLayout);

        TextView circleNameTv = (TextView) findViewById(R.id.circleNameTextView);
        TextView validityTv = (TextView) findViewById(R.id.providerNameTextView);
        buttonBill = (Button) findViewById(R.id.billButton);

        Date d = (Date) getIntent().getSerializableExtra("date");
        String whenItHappened = getIntent().getStringExtra("whenItHappened");

        circleNameTv.setText(((ApplicationClass) getApplication()).getCircleName());
        validityTv.setText(((ApplicationClass) getApplication()).getDays());

        mChart = new PieChart(this);
        // add pie chart to main layout
        linearLayoutChart
                .addView(mChart, new LinearLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT));

        // setContentView(mChart);
        linearLayoutChart.setBackgroundColor(Color.parseColor("#55656C"));
        // setContentView(t);

        // configure pie chart
        mChart.setUsePercentValues(true);

        mChart.setDescription("User log Aggregated Stats");

        // enable hole and configure
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColorTransparent(true);
        mChart.setHoleRadius(5);
        mChart.setTransparentCircleRadius(7);

        // enable rotation of the chart by touch
        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);

        // customize legends
        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);

        if (whenItHappened.equals("before")) {
            MesgRecords(d);
            CallRecords(d);
            isRoaming();

            // Toast.makeText(getApplicationContext(), ((ApplicationClass)
            // getApplication()).getDays(), Toast.LENGTH_SHORT).show();
        } else {
            // fetchCallRecordsFromPast(d,whenItHappened);
        }
        // payPerSecond();
        buttonBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BgAsyncTaskForBillGeneration().execute();
            }
        });
    }

    private void MesgRecords(Date d) {
        Uri uri = Uri.parse("content://sms");
        Cursor curMesg = getContentResolver().query(uri, projectionMesg, "type=2", null, null);

        if (curMesg.moveToFirst()) {
            for (int i = 0; i < curMesg.getCount(); i++) {
                String date = curMesg.getString(curMesg.getColumnIndexOrThrow("date"));
                Date smsDayTime = new Date(Long.valueOf(date));
                if (smsDayTime.compareTo(d) > 0) {
                    smsCount++;
                }
                curMesg.moveToNext();
            }

            curMesg.close();
        }
    }

    private void CallRecords(Date d) {
        HashMap<String, CallLogs> callLogsDataMap = new LinkedHashMap<String, CallLogs>();
        String selection = "type = 2";
        ContentResolver resolver = getApplicationContext().getContentResolver();
        Cursor curCall = resolver.query(CallLog.Calls.CONTENT_URI, projectionCall, selection, null,
                null);

        while (curCall.moveToNext()) {
            String truncatedNumber;
            String number = curCall.getString(curCall.getColumnIndex(CallLog.Calls.NUMBER));
            String duration = curCall.getString(curCall.getColumnIndex(CallLog.Calls.DURATION));

            String date = curCall.getString(curCall.getColumnIndex(CallLog.Calls.DATE));
            Date callDate = new Date(Long.valueOf(date));

            Log.i("DATE", d.toString());

            if (callDate.compareTo(d) > 0 && Integer.valueOf(duration) > 0) {
                Log.i("duration", number + "    " + Long.valueOf(duration).toString());
                if (number.length() > 10) {
                    truncatedNumber = "91"
                            + number.substring((Math.abs(10 - number.length())), number.length());
                } else
                    truncatedNumber = number;

                if (callLogsDataMap.containsKey(truncatedNumber)) {
                    object = callLogsDataMap.get(truncatedNumber);
                    object.setDuration(Integer.valueOf(duration), callDate);

                } else {
                    object = new CallLogs(truncatedNumber);
                    object.setDuration(Integer.valueOf(duration), callDate);
                    object.setSmsCount(smsCount);
                }
                callLogsDataMap.put(truncatedNumber, object);
            }
        }
        curCall.close();
        new BgAsyncTaskForLogAggregation().execute(callLogsDataMap);
    }

    private void isRoaming() {

        NetworkInfo info = (NetworkInfo) ((ConnectivityManager) getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info.isRoaming()) {
            Toast.makeText(getApplicationContext(), "roaming", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Not roaming", Toast.LENGTH_SHORT).show();
        }

    }

    private class BgAsyncTaskForLogAggregation extends
            AsyncTask<HashMap<String, CallLogs>, Void, AggregatedLogStats> {

        String response;

        @Override
        protected AggregatedLogStats doInBackground(HashMap... params) {
            try {

                String url = URLClass.baseURL + URLClass.userDataURL
                        + ((ApplicationClass) getApplication()).getProviderName() + "/" +
                        ((ApplicationClass) getApplication()).getCircleName() + "/"
                        + URLClass.aggregationURL;

                RestTemplate restTemplate = new RestTemplate(true);
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                aggregatedLogStats = restTemplate.postForObject(
                        url, params[0], AggregatedLogStats.class);
                return aggregatedLogStats;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(AggregatedLogStats aggregatedLogStats) {
            super.onPostExecute(aggregatedLogStats);

            Integer[] arrayData = {
                    aggregatedLogStats.getLddInSeconds(),
                    aggregatedLogStats.getLdsInSeconds(), aggregatedLogStats.getLnsInSeconds(),
                    aggregatedLogStats.getLndInSeconds(), aggregatedLogStats.getSdsInSeconds(),
                    aggregatedLogStats.getSddInSeconds(), aggregatedLogStats.getSnsInSeconds(),
                    aggregatedLogStats.getSndInSeconds()
            };

            String[] descriptionArray = {"Local Day Different Network","Local Day Same Network",
                    "Local Day Same Network","Local Night Different Network",
                    "STD Day Same Network","STD Day Different Network","STD Night Same Network",
                    "STD Night Different Network"};

            ArrayList<Entry> yVals1 = new ArrayList<Entry>();

            for (int i = 0; i < arrayData.length; i++)
                yVals1.add(new Entry(arrayData[i], i));

            ArrayList<String> xVals = new ArrayList<String>();

            for (int i = 0; i < descriptionArray.length; i++)
                xVals.add(descriptionArray[i]);

            // create pie data set
            PieDataSet dataSet = new PieDataSet(yVals1, "Market Share");
            dataSet.setSliceSpace(3);
            dataSet.setSelectionShift(5);

            // add many colors
            ArrayList<Integer> colors = new ArrayList<Integer>();

            for (int c : ColorTemplate.VORDIPLOM_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.JOYFUL_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.COLORFUL_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.LIBERTY_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.PASTEL_COLORS)
                colors.add(c);

            colors.add(ColorTemplate.getHoloBlue());
            dataSet.setColors(colors);

            // instantiate pie data object now
            PieData data = new PieData(xVals, dataSet);
            data.setValueFormatter(new PercentFormatter());
            data.setValueTextSize(11f);
            data.setValueTextColor(Color.GRAY);

            mChart.setDrawSliceText(false);

            mChart.setData(data);

            // undo all highlights
            mChart.highlightValues(null);

            // update pie chart
            mChart.invalidate();

            /*
             * totalDayCallsTv.setText(String.valueOf(aggregatedLogStats
             * .getTotalCallDuringDayInSeconds()) + " sec");
             * totalNightCallsTv.setText(String.valueOf(aggregatedLogStats
             * .getTotalCallDuringNightInSeconds()) + " sec");
             * totalCallsTv.setText(String.valueOf(aggregatedLogStats
             * .getTotalCallDuringDayInSeconds() +
             * aggregatedLogStats.getTotalCallDuringNightInSeconds()) + " sec");
             */

            // Toast.makeText(getApplicationContext(), aggregatedLogStats.toString(),
            // Toast.LENGTH_SHORT).show();
            // setLayoutData(aggregatedLogStats);
        }
    }

    private class BgAsyncTaskForBillGeneration extends AsyncTask<Void, Void, BillPlansList> {

        String response;

        @Override
        protected BillPlansList doInBackground(Void... params) {
            try {

                String url = URLClass.baseURL + URLClass.dataproviderURL
                        + ((ApplicationClass) getApplication()).getProviderName() + "/" +
                        ((ApplicationClass) getApplication()).getCircleName() + "/"
                        + ((ApplicationClass) getApplication()).getDays() + "/" +
                        ((ApplicationClass) getApplication()).getDataUsage();
                RestTemplate restTemplate = new RestTemplate(true);
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                return restTemplate.postForObject(url + "/bill", aggregatedLogStats,
                        BillPlansList.class);
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(BillPlansList billPlansList) {
            super.onPostExecute(billPlansList);
            Gson gson = new Gson();
            Log.i("BILLS123", gson.toJson(billPlansList));
            Toast.makeText(getApplicationContext(), billPlansList.toString(), Toast.LENGTH_SHORT)
                    .show();

            Intent i = new Intent(CallLogStats.this, BillList.class);
            i.putExtra("billObject", billPlansList);
            startActivity(i);

        }
    }

}
