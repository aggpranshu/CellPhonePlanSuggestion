
package com.lochbridge.cellphoneplan.android;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.lochbridge.cellphoneplan.spring.CircleList;
import com.lochbridge.cellphoneplan.spring.ProviderList;
import com.lochbridge.cellphoneplan.spring.Providers;
import com.lochbridge.cellphoneplan.urls.URLClass;

public class SplashScreen extends AppCompatActivity {

    List<String> providerListNames;
    Spinner spinnerProvider;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        spinnerProvider = (Spinner) findViewById(R.id.spinnerProvider);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        new BackgroundSplashTaskForProviders().execute();

        spinnerProvider.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
                if (item.equals("List of Providers")) {

                } else {
                    ((ApplicationClass) getApplication()).setProviderName(item);
                    new BackgroundSplashTask().execute(item);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private class BackgroundSplashTask extends AsyncTask<String, Void, CircleList> {

        @Override
        protected CircleList doInBackground(String... params) {
            try {
                String url = URLClass.baseURL + URLClass.dataproviderURL + params[0];
                ((ApplicationClass) getApplication()).setProviderName(params[0]);

                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                return restTemplate.getForObject(url, CircleList.class);
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
        protected void onPostExecute(CircleList circleList) {

            try {
                super.onPostExecute(circleList);
                Intent i = new Intent(SplashScreen.this,
                        MainActivity.class);
                i.putExtra("classInfo", circleList);
                startActivity(i);
                finish();
            } catch (NullPointerException e) {
                Toast.makeText(getApplicationContext(), "Please Connect to internet",
                        Toast.LENGTH_SHORT).show();
                /*
                 * Intent i = new Intent(SplashScreen.this, MainActivity.class);
                 * i.putExtra("isEmpty",0);
                 */
            }
        }
    }

    private class BackgroundSplashTaskForProviders extends AsyncTask<Void, Void, ProviderList> {

        @Override
        protected ProviderList doInBackground(Void... params) {
            try {
                String url = URLClass.baseURL + URLClass.dataproviderURL + "/providers";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                return restTemplate.getForObject(url, ProviderList.class);
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
        protected void onPostExecute(ProviderList providerList) {

            progressBar.setVisibility(View.INVISIBLE);
            try {
                super.onPostExecute(providerList);
                List<Providers> listProviders = providerList.getListProviders();
                 providerListNames = new ArrayList<String>();
                providerListNames.add("List of Providers");
                for (int k = 0; k < listProviders.size(); k++) {
                    providerListNames.add(listProviders.get(k).getProvider());
                }

                ArrayAdapter<String> dataAdapterProviderList = new ArrayAdapter<String>(
                        getApplicationContext(),
                        android.R.layout.simple_spinner_item, providerListNames);
                dataAdapterProviderList
                        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerProvider.setAdapter(dataAdapterProviderList);

            } catch (NullPointerException e) {
                Toast.makeText(getApplicationContext(), "Please Connect to internet",
                        Toast.LENGTH_SHORT).show();
                /*
                 * Intent i = new Intent(SplashScreen.this, MainActivity.class);
                 * i.putExtra("isEmpty",0);
                 */
            }
        }
    }
}
