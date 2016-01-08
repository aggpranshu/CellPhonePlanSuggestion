package com.lochbridge.cellphoneplan.android;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;

import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lochbridge.cellphoneplan.Utils.URLClass;
import com.lochbridge.cellphoneplan.spring.AggregatedLogStats;
import com.lochbridge.cellphoneplan.spring.BillPlansList;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class UserLogsTabs extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CALLLOGS = 0;
    private static final int PERMISSION_REQUEST_MESSAGES = 1;
    LinearLayout linearLayoutChart;
    private CallLogs object;
    private View mLayout;
    private Date d;
    private Bundle data;
    private TabLayout tabLayout;

    private AggregatedLogStats aggregatedLogStats;
    private int smsCount = 0;
    private String[] projectionCall = new String[]{
            CallLog.Calls.DATE,
            CallLog.Calls.NUMBER,
            CallLog.Calls.DURATION,
            CallLog.Calls.TYPE
    };

    private String[] projectionMesg = new String[]{
            "type", "date"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_adapter);
       /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
*/
        mLayout = findViewById(R.id.main_layout);

        d = (Date) getIntent().getSerializableExtra("date");
        String whenItHappened = getIntent().getStringExtra("whenItHappened");

        if (whenItHappened.equals("before")) {
            MesgRecords(d);
            CallRecords(d);

            // Toast.makeText(getApplicationContext(), ((ApplicationClass)
            // getApplication()).getDays(), Toast.LENGTH_SHORT).show();
        } else {
            // fetchCallRecordsFromPast(d,whenItHappened);
        }


        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Calls"));
        tabLayout.addTab(tabLayout.newTab().setText("Messages and Internet"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

    }

    private void MesgRecords(Date d) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionsCallLogs();
        } else {
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
    }


    private void CallRecords(Date d) {
        HashMap<String, CallLogs> callLogsDataMap = new LinkedHashMap<String, CallLogs>();
        String selection = "type = 2";
        ContentResolver resolver = getApplicationContext().getContentResolver();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionsMesgs();
        } else {
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
        }

        new BgAsyncTaskForLogAggregation().execute(callLogsDataMap);
    }

    private void requestPermissionsMesgs() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_SMS)) {
            Snackbar.make(mLayout, "Access to SMS is required to gather SMS data.",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Request the permission
                    ActivityCompat.requestPermissions(UserLogsTabs.this,
                            new String[]{Manifest.permission.READ_SMS},
                            PERMISSION_REQUEST_MESSAGES);
                }
            }).show();

        } else {

            Snackbar.make(mLayout,
                    "Permission is not available. Requesting SMS permission.",
                    Snackbar.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_SMS},
                    PERMISSION_REQUEST_MESSAGES);
        }
    }

    private void requestPermissionsCallLogs() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_CALL_LOG)) {
            Snackbar.make(mLayout, "Access to call logs is required to gather call data.",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Request the permission
                    ActivityCompat.requestPermissions(UserLogsTabs.this,
                            new String[]{Manifest.permission.READ_CALL_LOG},
                            PERMISSION_REQUEST_CALLLOGS);
                }
            }).show();

        } else {

            Snackbar.make(mLayout,
                    "Permission is not available. Requesting call logs permission.",
                    Snackbar.LENGTH_SHORT).show();
            // Request the permission. The result will be received in onRequestPermissionResult().
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALL_LOG},
                    PERMISSION_REQUEST_CALLLOGS);
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

            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return aggregatedLogStats;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(AggregatedLogStats aggregatedLogStats) {
            super.onPostExecute(aggregatedLogStats);

            Toast.makeText(getApplicationContext(), "hello", Toast.LENGTH_SHORT).show();
            data = new Bundle();
            data.putSerializable("logAggregationObj", aggregatedLogStats);

            final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
            final TabAdapter adapter = new TabAdapter
                    (getSupportFragmentManager(), data);
            viewPager.setAdapter(adapter);
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

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

            Intent i = new Intent(UserLogsTabs.this, BillList.class);
            i.putExtra("billObject", billPlansList);
            startActivity(i);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

        if (requestCode == PERMISSION_REQUEST_CALLLOGS) {
            // Request for call logs.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                Snackbar.make(mLayout, "Call Logs permission was granted. Starting preview.",
                        Snackbar.LENGTH_SHORT)
                        .show();
                CallRecords(d);
            } else {
                // Permission request was denied.
                Snackbar.make(mLayout, "Call Logs permission request was denied.",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }

        if (requestCode == PERMISSION_REQUEST_MESSAGES) {
            // Request for call logs.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                Snackbar.make(mLayout, "SMS permission was granted. Starting preview.",
                        Snackbar.LENGTH_SHORT)
                        .show();
                MesgRecords(d);
            } else {
                // Permission request was denied.
                Snackbar.make(mLayout, "SMS permission request was denied.",
                        Snackbar.LENGTH_SHORT)
                        .show();
            }
        }

    }
}



