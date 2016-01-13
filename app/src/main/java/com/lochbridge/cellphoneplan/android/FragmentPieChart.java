
package com.lochbridge.cellphoneplan.android;

/**
 * Created by rgupta1 on 1/6/2016.
 */

import java.util.ArrayList;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.lochbridge.cellphoneplan.Utils.URLClass;
import com.lochbridge.cellphoneplan.model.AggregatedLogStats;
import com.lochbridge.cellphoneplan.model.BillPlansList;

public class FragmentPieChart extends Fragment {

    private final String[] xData = {
            "Local Day Different Network", "Local Day Same Network",
            "Local Day Same Network", "Local Night Different Network",
            "STD Day Same Network", "STD Day Different Network", "STD Night Same Network",
            "STD Night Different Network"
    };
    private Button b;
    private Bundle data;
    private LinearLayout mainLayout;
    private PieChart mChart;
    // we're going to display pie chart for smartphones martket shares
    /*
     * private float[] yData = { 5, 10, 15, 30, 40 }; private String[] xData = { "Sony", "Huawei",
     * "LG", "Apple", "Samsung" };
     */
    private AggregatedLogStats aggregatedLogStats;
    private Integer[] yData;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        data = this.getArguments();

        aggregatedLogStats = (AggregatedLogStats) data.getSerializable("logAggregationObj");

        yData = new Integer[] {
                aggregatedLogStats.getLddInSeconds(),
                aggregatedLogStats.getLdsInSeconds(), aggregatedLogStats.getLnsInSeconds(),
                aggregatedLogStats.getLndInSeconds(), aggregatedLogStats.getSdsInSeconds(),
                aggregatedLogStats.getSddInSeconds(), aggregatedLogStats.getSnsInSeconds(),
                aggregatedLogStats.getSndInSeconds()
        };

        View v = inflater.inflate(R.layout.fragment_pie_chart,
                container, false);

        mainLayout = (LinearLayout) v.findViewById(R.id.layoutPieChart);

        b = (Button) v.findViewById(R.id.layoutIncluded).findViewById(R.id.buttonBill);

        mChart = new PieChart(getActivity());

        mChart.setDrawSliceText(false);

        // add pie chart to main layout
        mainLayout
                .addView(mChart, new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT));
        mainLayout.setBackgroundColor(Color.parseColor("#55656C"));

        // configure pie chart
        mChart.setUsePercentValues(true);
        mChart.setDescription("Aggregated Log stats data");

        // enable hole and configure
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.BLACK);
        // mChart.setHoleColorTransparent(true);
        mChart.setHoleRadius(7);
        mChart.setTransparentCircleRadius(10);

        // enable rotation of the chart by touch
        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);

        // set a chart value selected listener
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {

            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                // display msg when value selected
                if (e == null)
                    return;

                Toast.makeText(getActivity(),
                        xData[e.getXIndex()] + " = " + e.getVal() + "%", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected() {

            }
        });

        // add data
        addData();

        // customize legends
        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BgAsyncTaskForBillGeneration().execute();
                // Toast.makeText(getActivity(), "Hello Button !!", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    private void addData() {
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();
        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < yData.length; i++) {
            if (yData[i] != 0) {
                yVals1.add(new Entry(yData[i], i));
                xVals.add(xData[i]);
            }
        }

        // create pie data set
        PieDataSet dataSet = new PieDataSet(yVals1, "");
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
        // data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.GRAY);

        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        // update pie chart
        mChart.invalidate();

    }

    private class BgAsyncTaskForBillGeneration extends AsyncTask<Void, Void, BillPlansList> {

        String response;

        @Override
        protected BillPlansList doInBackground(Void... params) {
            try {

                String url = URLClass.baseURL + URLClass.dataproviderURL
                        + ((ApplicationClass) getActivity().getApplication()).getProviderName()
                        + "/" +
                        ((ApplicationClass) getActivity().getApplication()).getCircleName() + "/" +
                        ((ApplicationClass) getActivity().getApplication()).getDataUsage();
                RestTemplate restTemplate = new RestTemplate(true);
                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                return restTemplate.postForObject(url + "/bill", aggregatedLogStats,
                        BillPlansList.class);
            } catch (Exception e) {
                Log.e("UserTelecomDetailsActivity", e.getMessage(), e);
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
            /*
             * Toast.makeText(getApplicationContext(), billPlansList.toString(), Toast.LENGTH_SHORT)
             * .show();
             */

            Intent i = new Intent(getActivity(), BillList.class);
            i.putExtra("billObject", billPlansList);
            startActivity(i);

        }
    }

}
