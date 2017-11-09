package com.mycillin.partner.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.mycillin.partner.activity.LoginActivity;
import com.mycillin.partner.activity.MainActivity;

public class SessionManager {
    private static final String KEY_PREF_NAME = "MYCILLIN_SESSION_MANAGER";

    private static final String KEY_IS_LOGGED_IN = "IS_LOGGED_IN";
    private static final String KEY_EMAIL = "EMAIL";
    private static final String KEY_FULLNAME = "FULLNAME";
    private static final String KEY_USER_ID = "USER_ID";
    private static final String KEY_TOKEN = "TOKEN";
    private static final String KEY_USER_PIC_URL = "USER_PIC_URL";
    private static final String KEY_USER_PASS = "USER_PASS";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public SessionManager(Context con) {
        this.context = con;
        int PRIVATE_MODE = 0;
        sharedPreferences = context.getSharedPreferences(KEY_PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void createLoginSession(String email, String fullName, String userId, String token, String url, String pass) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_EMAIL, email);
        editor.putString(KEY_FULLNAME, fullName);
        editor.putString(KEY_USER_ID, userId);
        editor.putString(KEY_TOKEN, token);
        editor.putString(KEY_USER_PIC_URL, url);
        editor.putString(KEY_USER_PASS, pass);

        editor.commit();
    }

    public void checkLogin() {
        if (!isLoggedIn()) {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public boolean isLoggedIn() {
        return sharedPreferences.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    public String getUserEmail() {
        return sharedPreferences.getString(KEY_EMAIL, null);
    }

    public String getUserFullName() {
        return sharedPreferences.getString(KEY_FULLNAME, null);
    }

    public String getUserId() {
        return sharedPreferences.getString(KEY_USER_ID, null);
    }

    public String getUserToken() {
        return sharedPreferences.getString(KEY_TOKEN, null);
    }

    public String getUserPicUrl() {
        return sharedPreferences.getString(KEY_USER_PIC_URL, null);
    }

    public String getUserPass() {
        return sharedPreferences.getString(KEY_USER_PASS, null);
    }
}
