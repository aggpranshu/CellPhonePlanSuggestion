package com.lochbridge.cellphoneplan.android;

import android.content.Intent;
import android.os.AsyncTask;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.lochbridge.cellphoneplan.spring.ProviderList;
import com.lochbridge.cellphoneplan.spring.Providers;
import com.lochbridge.cellphoneplan.Utils.URLClass;
import com.viksaa.sssplash.lib.activity.AwesomeSplash;
import com.viksaa.sssplash.lib.cnst.Flags;
import com.viksaa.sssplash.lib.model.ConfigSplash;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by paggarwal1 on 12/23/2015.
 */
public class SplashScreen extends AwesomeSplash {

    ArrayList<String> providerListNames;

    @Override
    public void initSplash(ConfigSplash configSplash) {

            /* you don't have to override every property */

        //Customize Circular Reveal
        configSplash.setBackgroundColor(R.color.colorPrimary); //any color you want form colors.xml
        configSplash.setAnimCircularRevealDuration(2000); //int ms
        configSplash.setRevealFlagX(Flags.REVEAL_RIGHT);  //or Flags.REVEAL_LEFT
        configSplash.setRevealFlagY(Flags.REVEAL_BOTTOM); //or Flags.REVEAL_TOP

        //Choose LOGO OR PATH; if you don't provide String value for path it's logo by default

        //Customize Logo
        configSplash.setLogoSplash(R.mipmap.ic_launcher); //or any other drawable
        configSplash.setAnimLogoSplashDuration(1500); //int ms
        configSplash.setAnimLogoSplashTechnique(Techniques.SlideInDown); //choose one form Techniques (ref: https://github.com/daimajia/AndroidViewAnimations)

        //Customize Title
        configSplash.setTitleSplash("Cell Phone Plan Suggestion App");
        configSplash.setTitleTextColor(R.color.com_facebook_likeboxcountview_text_color);
        configSplash.setTitleTextSize(60f); //float value
        configSplash.setAnimTitleDuration(1000);
        configSplash.setAnimTitleTechnique(Techniques.SlideInUp);
        //  configSplash.setTitleFont("fonts/myfont.ttf"); //provide string to your font located in assets/fonts/

        new BackgroundSplashTaskForProviders().execute();

    }

    @Override
    public void animationsFinished() {

        Intent i = new Intent(SplashScreen.this,MainActivity.class);
        i.putStringArrayListExtra("ListProviders", providerListNames);
        startActivity(i);
        finish();
    }

    private class BackgroundSplashTaskForProviders extends AsyncTask<Void, Void, ProviderList> {

        @Override
        protected ProviderList doInBackground(Void... params) {
            try {
                String url = URLClass.baseURL + URLClass.dataproviderURL + "providers";
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
            try {
                super.onPostExecute(providerList);
                List<Providers> listProviders = providerList.getListProviders();
                providerListNames = new ArrayList<String>();
                providerListNames.add("List of Providers");
                for (int k = 0; k < listProviders.size(); k++) {
                    providerListNames.add(listProviders.get(k).getProvider());
                }




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

