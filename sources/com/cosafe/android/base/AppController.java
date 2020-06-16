package com.cosafe.android.base;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build.VERSION;
import com.cosafe.android.utils.PreferenceManager;
import com.cosafe.android.utils.retrofit.RetrofitClient;

public class AppController extends Application {
    public static final String CHANNEL_ID = "coSafeChannel";
    private static Context appContext;

    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        RetrofitClient.create();
        PreferenceManager.init(this);
        createNotificationChannel();
    }

    public static Context getAppContext() {
        return appContext;
    }

    private void createNotificationChannel() {
        if (VERSION.SDK_INT >= 26) {
            ((NotificationManager) getSystemService(NotificationManager.class)).createNotificationChannel(new NotificationChannel(CHANNEL_ID, "Example Service Channel", 3));
        }
    }
}
