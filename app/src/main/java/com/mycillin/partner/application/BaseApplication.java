package com.mycillin.partner.application;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        Timber.plant(new Timber.DebugTree());
        Stetho.initializeWithDefaults(this);
    }
}
