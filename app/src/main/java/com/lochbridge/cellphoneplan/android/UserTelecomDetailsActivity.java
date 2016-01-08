
package com.lochbridge.cellphoneplan.android;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.widget.LoginButton;
import com.lochbridge.cellphoneplan.model.CircleList;
import com.lochbridge.cellphoneplan.model.Circles;
import com.lochbridge.cellphoneplan.model.PlanDetails;
import com.lochbridge.cellphoneplan.model.PlanDetailsList;
import com.lochbridge.cellphoneplan.Utils.URLClass;

public class UserTelecomDetailsActivity extends AppCompatActivity {

    private String[] arrayCall;
    private String[] arrayMesg;
    private String[] arrayInternet;

    private ProgressDialog dialog;
    private CardView providerCV;
    private CardView circleCV;
    private CardView dataPlanCV;
    private CardView dateCV;
    private CardView multiSelectCV;
    private View mLayout;
    private TextView carrierName, circleName, dataUsage;
    private List<PlanDetails> listPlanDetails;
    private Spinner spinnerProvider, spinnerCircle, spinnerData;
    private List<String> listDataRange = new ArrayList<String>();
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private List<String> circleListNames;
    private ListView callLV, mesgLV, internetLV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /*
         * FacebookSdk.sdkInitialize(getApplicationContext()); callbackManager =
         * CallbackManager.Factory.create();
         */

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_usertelecom_details);

        mLayout = findViewById(R.id.relativeLayout);

        // declaration of the card view
        providerCV = (CardView) findViewById(R.id.cvProvider);
        circleCV = (CardView) findViewById(R.id.cvCircle);
        dataPlanCV = (CardView) findViewById(R.id.cvData);
        dateCV = (CardView) findViewById(R.id.cvDate);
        multiSelectCV = (CardView) findViewById(R.id.cvMultiSpinner);

        // declaration of the spinners
        spinnerCircle = (Spinner) findViewById(R.id.spinnerCircle);
        spinnerProvider = (Spinner) findViewById(R.id.spinnerProvider);
        spinnerData = (Spinner) findViewById(R.id.spinnerData);

        // declaration for the textviews
        carrierName = (TextView) findViewById(R.id.tvProvider);
        circleName = (TextView) findViewById(R.id.tvCircle);
        dataUsage = (TextView) findViewById(R.id.tvData);

        // carrierName.append(": " + ((ApplicationClass) getApplication()).getProviderName());

        listDataRange.add("Select your Data Usage");
        listDataRange.add("0 - 100 MB");
        listDataRange.add("100 - 300 MB");
        listDataRange.add("300MB - 500MB");
        listDataRange.add("500MB - 1GB");
        listDataRange.add("1GB - 3GB");
        listDataRange.add("3GB - 5GB");
        listDataRange.add("Above 5GB");

        ArrayList<String> providerList = getIntent().getStringArrayListExtra("ListProviders");

        providerCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinnerProvider.getCount() == 0) {
                    final Snackbar snackbar = Snackbar
                            .make(mLayout, "Select Provider First", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else {
                    spinnerProvider.performClick();
                }
            }
        });

        circleCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinnerCircle.getCount() == 0) {
                    final Snackbar snackbar = Snackbar
                            .make(mLayout, "Select Provider First", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else {
                    spinnerCircle.performClick();
                }
            }
        });

        dataPlanCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spinnerData.getCount() == 0) {
                    final Snackbar snackbar = Snackbar
                            .make(mLayout, "Select Circle First", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else {
                    spinnerData.performClick();
                }
            }
        });

        dateCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new FragmentDatePicker();
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }
        });

        multiSelectCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                Toast.makeText(getApplicationContext(), "dialog", Toast.LENGTH_SHORT).show();
                // custom dialog
                final Dialog dialog = new Dialog(UserTelecomDetailsActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.tab_layout);

                dialog.getWindow().setLayout((int) (displayMetrics.widthPixels * 0.85),
                        (int) (displayMetrics.heightPixels * 0.75));

                // declaration of multi select spinners
                callLV = (ListView) dialog.findViewById(R.id.listViewCall);
                mesgLV = (ListView) dialog.findViewById(R.id.listViewMesg);
                internetLV = (ListView) dialog.findViewById(R.id.listViewInternet);

                try {
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(),
                            R.layout.simple_multiselect_listview, arrayCall);
                    callLV.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    callLV.setAdapter(adapter);

                    ArrayAdapter<String> adapter2 = new ArrayAdapter<>(getApplicationContext(),
                            R.layout.simple_multiselect_listview, arrayMesg);
                    mesgLV.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    mesgLV.setAdapter(adapter2);

                    ArrayAdapter<String> adapter3 = new ArrayAdapter<>(getApplicationContext(),
                            R.layout.simple_multiselect_listview, arrayInternet);
                    internetLV.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    internetLV.setAdapter(adapter3);

                    TabHost tabs = (TabHost) dialog.findViewById(R.id.tabHost);

                    tabs.setup();

                    TabHost.TabSpec tabpage1 = tabs.newTabSpec("one");
                    tabpage1.setContent(R.id.listViewCall);
                    tabpage1.setIndicator("Call");

                    TabHost.TabSpec tabpage2 = tabs.newTabSpec("two");
                    tabpage2.setContent(R.id.listViewMesg);
                    tabpage2.setIndicator("Message");

                    TabHost.TabSpec tabpage3 = tabs.newTabSpec("two");
                    tabpage3.setContent(R.id.listViewInternet);
                    tabpage3.setIndicator("Internet");

                    tabs.addTab(tabpage1);
                    tabs.addTab(tabpage2);
                    tabs.addTab(tabpage3);

                    dialog.show();
                } catch (NullPointerException exception) {
                    final Snackbar snackBar = Snackbar.make(mLayout, "Select Data plan first",
                            Snackbar.LENGTH_LONG);

                    snackBar.setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            snackBar.dismiss();
                        }
                    });
                    snackBar.show();

                }

            }
        });

        /*
         * loginButton = (LoginButton) findViewById(R.id.login_button);
         * loginButton.setReadPermissions("public_profile"); setupTokenTracker();
         * setupProfileTracker(); mTokenTracker.startTracking(); mProfileTracker.startTracking();
         * loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
         * @Override public void onSuccess(LoginResult loginResult) { AccessToken accessToken =
         * loginResult.getAccessToken(); Profile profile = Profile.getCurrentProfile();
         * Toast.makeText(getApplicationContext(),"logged in", Toast.LENGTH_SHORT).show(); //
         * info.setText("User Name: " + profile.getFirstName() + " " + profile.getLastName()); }
         * @Override public void onCancel() { //info.setText("Login attempt canceled."); }
         * @Override public void onError(FacebookException e) {
         * //info.setText("Login attempt failed."); } });
         */

        spinnerProvider.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();

                if (item.equals("List of Providers")) {
                    Toast.makeText(getApplicationContext(), String.valueOf(position),
                            Toast.LENGTH_SHORT).show();
                    // spinnerDuration.setVisibility(View.VISIBLE);
                    // spinnerDuration.setVisibility(View.INVISIBLE);
                } else {
                    carrierName.setText(item);
                    dialog = new ProgressDialog(UserTelecomDetailsActivity.this);
                    dialog.setMessage("Loading data...");
                    dialog.show();
                    new BGAsyncTaskCircle().execute(item);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }

        });

        ArrayAdapter<String> dataAdapterProviderList = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, providerList);
        dataAdapterProviderList
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerProvider.setAdapter(dataAdapterProviderList);

        /*
         * Spinner for specifying the name of the circles for the current Carrier of the user. This
         * is being populated by the circleListNames - ArrayList
         */
        spinnerCircle.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();

                if (item.equals("List of Circles")) {
                } else {
                    circleName.setText(item);
                    ((ApplicationClass) getApplication()).setCircleName(item);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerData.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if (item.equals("Select your Data Usage")) {

                } else {
                    dataUsage.setText(item);
                    ((ApplicationClass) getApplication()).setDataUsage(item);
                    new BGAsyncTaskForPlans().execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void setupTokenTracker() {
        AccessTokenTracker mTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                    AccessToken currentAccessToken) {

            }
        };
    }

    private void setupProfileTracker() {
        ProfileTracker mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                // info.setText("Welcome" + currentProfile.getFirstName() + " " +
                // currentProfile.getLastName());
            }
        };
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Profile profile = Profile.getCurrentProfile();
        // info.setText("Welcome Back user !");

    }

    private class BGAsyncTaskForPlans extends
            AsyncTask<Void, Void, PlanDetailsList> {

        String response;

        @Override
        protected PlanDetailsList doInBackground(Void... params) {
            PlanDetailsList planDetailsList = null;
            try {

                String url = URLClass.baseURL + URLClass.dataproviderURL
                        + ((ApplicationClass) getApplication()).getProviderName() + "/" +
                        ((ApplicationClass) getApplication()).getCircleName();
                RestTemplate restTemplate = new RestTemplate(true);
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                planDetailsList = restTemplate
                        .getForObject(url, PlanDetailsList.class);
            } catch (Exception e) {
                Log.e("PlanDetails", e.getMessage(), e);
            }

            return planDetailsList;
        }

        @Override
        protected void onPostExecute(PlanDetailsList planDetailsList) {

            String[] patternDataPlans = {
                    "2G Pack", "3G Pack", "2G Internet pack",
                    "3G Internet pack"
            };

            super.onPostExecute(planDetailsList);
            listPlanDetails = planDetailsList.getListPlanDetails();
            ArrayList<String> listPlansCalls = new ArrayList<>();
            ArrayList<String> listPlansMesg = new ArrayList<>();
            ArrayList<String> listPlansInternet = new ArrayList<>();

            for (int i = 0; i < listPlanDetails.size(); i++) {
                String planDesc = null;

                try {
                    JSONObject jsonObject = new JSONObject(listPlanDetails.get(i)
                            .getPlanDetails());
                    planDesc = "Rs." + jsonObject.getString("recharge_value") + "\nDescription: "
                            + jsonObject.getString("recharge_description");

                    if (jsonObject.getString("recharge_short_description").equals("SMS Pack")) {
                        listPlansMesg.add(planDesc);
                    }
                    else if (Arrays.asList(patternDataPlans)
                            .contains(jsonObject.getString("recharge_short_description"))) {
                        listPlansInternet.add(planDesc);
                    }
                    else
                    {
                        listPlansCalls.add(planDesc);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            arrayCall = listPlansCalls.toArray(new String[listPlansCalls.size()]);
            arrayMesg = listPlansMesg.toArray(new String[listPlansMesg.size()]);
            arrayInternet = listPlansInternet.toArray(new String[listPlansInternet.size()]);

        }
    }

    private class BGAsyncTaskCircle extends AsyncTask<String, Void, CircleList> {

        @Override
        protected CircleList doInBackground(String... params) {
            try {
                String url = URLClass.baseURL + URLClass.dataproviderURL + params[0];
                ((ApplicationClass) getApplication()).setProviderName(params[0]);

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                return restTemplate.getForObject(url, CircleList.class);
            } catch (Exception e) {
                Log.e("UserTelecomDetailsActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(CircleList circleList) {

            try {
                dialog.dismiss();
                super.onPostExecute(circleList);
                List<Circles> listCircles = circleList.getListCircles();
                circleListNames = new ArrayList<String>();
                circleListNames.add("List of Providers");
                for (int k = 0; k < listCircles.size(); k++) {
                    circleListNames.add(listCircles.get(k).getCircleName());
                }

                ArrayAdapter<String> dataAdapterCircleList = new ArrayAdapter<String>(
                        getApplicationContext(),
                        android.R.layout.simple_spinner_item, circleListNames);
                dataAdapterCircleList
                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerCircle.setAdapter(dataAdapterCircleList);

                ArrayAdapter<String> dataAdapterDataPlans = new ArrayAdapter<String>(
                        getApplicationContext(),
                        android.R.layout.simple_spinner_item, listDataRange);
                dataAdapterDataPlans
                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerData.setAdapter(dataAdapterDataPlans);

            } catch (NullPointerException e) {
                Toast.makeText(getApplicationContext(), "Please Connect to internet",
                        Toast.LENGTH_SHORT).show();

            }
        }
    }
}
