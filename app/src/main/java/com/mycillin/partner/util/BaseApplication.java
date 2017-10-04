package com.mycillin.partner.util;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by 16003041 on 04/10/2017.
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Stetho.initializeWithDefaults(this);
    }
}
