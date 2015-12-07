
package com.lochbridge.cellphoneplan.android;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.guna.libmultispinner.MultiSelectionSpinner;
import com.lochbridge.cellphoneplan.spring.CircleList;
import com.lochbridge.cellphoneplan.spring.Circles;
import com.lochbridge.cellphoneplan.spring.ListPlanDetailsByDuration;
import com.lochbridge.cellphoneplan.spring.ListPlanDetailsByDurationList;
import com.lochbridge.cellphoneplan.urls.URLClass;

public class MainActivity extends AppCompatActivity {

    Button buttonCircle, buttonDate, buttonDuration, buttonData;
    private TextView carrierName;
    private List<ListPlanDetailsByDuration> listPlanDetailsByDuration;
    private Spinner spinnerDuration, spinnerCircleList , spinnerData;
    private List<String> validityOfPlans = new ArrayList<String>();
    private List<String> listDataRange = new ArrayList<String>();
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private List<String> circleListNames;
    private MultiSelectionSpinner multiSelectionSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /*
         * FacebookSdk.sdkInitialize(getApplicationContext()); callbackManager =
         * CallbackManager.Factory.create();
         */

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_activity_main);

        buttonDate = (Button) findViewById(R.id.buttonDate);
        buttonData = (Button) findViewById(R.id.buttonData);
        buttonCircle = (Button) findViewById(R.id.buttonCircle);
        buttonDuration = (Button) findViewById(R.id.buttonDuration);

        spinnerCircleList = (Spinner) findViewById(R.id.listCircles);
        spinnerDuration = (Spinner) findViewById(R.id.listDuration);
        multiSelectionSpinner = (MultiSelectionSpinner) findViewById(R.id.mySpinner);
        spinnerData =(Spinner)findViewById(R.id.spinnerData);

        carrierName = (TextView) findViewById(R.id.carrierName);
        carrierName.append(": " + ((ApplicationClass) getApplication()).getProviderName());

        validityOfPlans.add("Days for plans");
        validityOfPlans.add("28 days");
        validityOfPlans.add("60 days");
        validityOfPlans.add("90 days");

        listDataRange.add("Select your Data Usage");
        listDataRange.add("0-100 MB");
        listDataRange.add("100-500 MB");
        listDataRange.add("500MB - 1GB");
        listDataRange.add("1GB - 2GB");
        listDataRange.add("2GB - 3GB");
        listDataRange.add("3GB - 4GB");
        listDataRange.add("4GB - 5GB");
        listDataRange.add("Above 5GB");

        buttonDate.setVisibility(View.INVISIBLE);
        buttonData.setVisibility(View.INVISIBLE);
        buttonDuration.setVisibility(View.INVISIBLE);

        CircleList circleListObj = (CircleList) getIntent().getSerializableExtra("classInfo");
        try {

            List<Circles> circleList = circleListObj.getListCircles();
            circleListNames = new ArrayList<String>();
            circleListNames.add("List of Circles");
            for (int k = 0; k < circleList.size(); k++) {
                circleListNames.add(circleList.get(k).getCircleName());
            }

        } catch (NullPointerException e) {
            Toast.makeText(getApplicationContext(), "exception caught", Toast.LENGTH_SHORT).show();
        }

        buttonCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerCircleList.performClick();
            }
        });

        buttonDuration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDuration.performClick();

            }
        });

        buttonData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerData.performClick();

            }
        });

        buttonDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePicker");
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


        /*
         * Spinner for specifying the name of the circles for the current Carrier of the user. This
         * is being populated by the circleListNames - ArrayList
         */
        spinnerCircleList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();

                if (item.equals("List of Circles")) {
                    Toast.makeText(getApplicationContext(), String.valueOf(position) , Toast.LENGTH_SHORT).show();
                    // spinnerDuration.setVisibility(View.VISIBLE);
                    // spinnerDuration.setVisibility(View.INVISIBLE);
                } else {
                    ArrayAdapter<String> dataAdapterDurationList = new ArrayAdapter<String>(
                            getApplicationContext(), android.R.layout.simple_spinner_item,
                            validityOfPlans);
                    dataAdapterDurationList
                            .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerDuration.setAdapter(dataAdapterDurationList);
                    ((ApplicationClass) getApplication()).setCircleName(item);
                    buttonDuration.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<String> dataAdapterCircleList = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, circleListNames);
        dataAdapterCircleList
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCircleList.setAdapter(dataAdapterCircleList);


        spinnerDuration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if (item.equals("Days for plans")) {

                } else {
                    ((ApplicationClass) getApplication()).setDays(item);
                    ArrayAdapter<String> dataAdapterCircleList = new ArrayAdapter<String>(getApplicationContext(),
                            android.R.layout.simple_spinner_item, listDataRange);
                    dataAdapterCircleList
                            .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerData.setAdapter(dataAdapterCircleList);
                    buttonData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerData.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if (item.equals("Select your Data Usage")) {

                } else {
                    ((ApplicationClass)getApplication()).setDataUsage(position);
                    buttonDate.setVisibility(View.VISIBLE);
                    new BgAsyncTaskForPlanByDuration().execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    /*
     * Button for selecting the Date. The call logs fetched would be filtered based on the value of
     * this Date
     */

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
    protected void onStop() {
        super.onStop();
        // mTokenTracker.stopTracking();
        // mProfileTracker.stopTracking();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Profile profile = Profile.getCurrentProfile();
        // info.setText("Welcome Back user !");

    }

    private class BgAsyncTaskForPlanByDuration extends
            AsyncTask<Void, Void, ListPlanDetailsByDurationList> {

        String response;

        @Override
        protected ListPlanDetailsByDurationList doInBackground(Void... params) {
            try {

                String url = URLClass.baseURL + URLClass.dataproviderURL
                        + ((ApplicationClass) getApplication()).getProviderName() + "/" +
                        ((ApplicationClass) getApplication()).getCircleName() + "/"
                        + ((ApplicationClass) getApplication()).getDays();
                RestTemplate restTemplate = new RestTemplate(true);
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                return restTemplate
                        .getForObject(url, ListPlanDetailsByDurationList.class);
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
        protected void onPostExecute(ListPlanDetailsByDurationList listPlanDetailsByDurationList) {
            super.onPostExecute(listPlanDetailsByDurationList);
            listPlanDetailsByDuration = listPlanDetailsByDurationList
                    .getListPlanDetailsByDuration();
            ArrayList<String> listPlans = new ArrayList<String>();
            for (int i = 0; i < listPlanDetailsByDuration.size(); i++) {
                try {
                    JSONObject jsonObject = new JSONObject(listPlanDetailsByDuration.get(i)
                            .getPlanDetails());
                    listPlans.add(
                            i,
                            jsonObject.getString("recharge_value") + "\t"
                                    + jsonObject.getString("recharge_description"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            String[] array = listPlans.toArray(new String[listPlans.size()]);
            multiSelectionSpinner.setItems(array);
             }
    }

}
