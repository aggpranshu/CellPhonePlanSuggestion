package com.lochbridge.cellphoneplan.android;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class BackgroundService extends Service {
    static int i = 0;
    String tag = "MyService";
    Thread t;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    while(Boolean.TRUE) {
                        Log.i("Value of i", String.valueOf(i++));
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        t.start();
        t.setName("Custom Thread");
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        t.stop();
        super.onDestroy();
    }

}