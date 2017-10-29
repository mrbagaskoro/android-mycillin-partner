package com.mycillin.partner.util;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;

public class ContextHelper extends Application {
    private static ContextHelper instance;

    private ContextHelper(@NonNull Context context) {
        Context mContext = context.getApplicationContext();
    }

    public static ContextHelper getInstance(@NonNull Context context) {
        synchronized (ContextHelper.class) {
            if (instance == null) {
                instance = new ContextHelper(context);
            }
            return instance;
        }

    }

    /*public void onCreate() {
        super.onCreate();

        ContextHelper.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return ContextHelper.context;
    }*/
}
