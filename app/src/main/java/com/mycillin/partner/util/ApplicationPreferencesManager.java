package com.mycillin.partner.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 16003041 on 04/10/2017.
 */

public class ApplicationPreferencesManager {

    public static final String KEY_USER_PREFERED_LANGUAGE = "user_preferred_language";
    private static final String IS_SELECT_LANGUAGE = "is_select_language";
    private static final String IS_INTRO_DONE = "is_intro_done";

    public static final String DEFAULT_VALUE = "-1";
    private static final String PREF_NAME = "ApplicationPreferencesManager";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;

    public ApplicationPreferencesManager(Context con) {
        this.context = con;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
    }

    public void selectLanguage() {
        editor.putBoolean(IS_SELECT_LANGUAGE, true);

        editor.commit();
    }

    public boolean isSelectLanguage() {
        return sharedPreferences.getBoolean(IS_SELECT_LANGUAGE, false);
    }

    public void introDone() {
        editor.putBoolean(IS_INTRO_DONE, true);

        editor.commit();
    }

    public boolean isIntroDone() {
        return sharedPreferences.getBoolean(IS_INTRO_DONE, false);
    }
}
