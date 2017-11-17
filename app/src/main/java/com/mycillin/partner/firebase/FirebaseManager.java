package com.mycillin.partner.firebase;

import android.content.Context;
import android.content.SharedPreferences;

public class FirebaseManager {
    public static final String DEFAULT_VALUE = "0";
    private static final String FIREBASE_KEY = "FIREBASE_KEY";
    private static final String PREF_NAME = "FirebaseManagerPreference";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public FirebaseManager(Context con) {
        int PRIVATE_MODE = 0;
        sharedPreferences = con.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    void setFirebaseToken(String firebaseToken) {
        editor.putString(FIREBASE_KEY, firebaseToken);
        editor.commit();
    }

    public String getFirebaseToken() {
        return sharedPreferences.getString(FIREBASE_KEY, DEFAULT_VALUE);
    }
}
