
package com.lochbridge.cellphoneplan.android;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lochbridge.cellphoneplan.spring.CircleList;
import com.lochbridge.cellphoneplan.urls.URLClass;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        Button submitButton = (Button) findViewById(R.id.button);
        final EditText carrierName = (EditText) findViewById(R.id.editText);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BackgroundSplashTask().execute(carrierName.getText().toString());
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
}
