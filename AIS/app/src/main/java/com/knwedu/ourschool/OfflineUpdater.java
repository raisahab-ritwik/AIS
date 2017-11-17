package com.knwedu.ourschool;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by ddasgupta on 3/15/2016.
 */
public class OfflineUpdater extends Service {
    static final String Tag="ServiceUpdater";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(Tag, "Service Created");
    }

    @Override
    public void onStart(Intent intent, int startId) {

        Log.d(Tag,"Service Started");
        super.onStart(intent, startId);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



}
