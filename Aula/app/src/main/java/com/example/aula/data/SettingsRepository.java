package com.example.aula.data;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingsRepository {

    private static final String PREFS_NAME = "settings";
    private static final String KEY_DARK_MODE = "dark_mode";

    private static final String KEY_NAME = "name";

    private final SharedPreferences prefs;

    public SettingsRepository(Context context) {
        prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void setDarkMode(boolean enabled) {
        prefs.edit().putBoolean(KEY_DARK_MODE, enabled).apply();
    }

    public void setName(String name){

        prefs.edit().putString(KEY_NAME,name).apply();
    }

    public String getName(){


            return prefs.getString(KEY_DARK_MODE, "");

    }

    public boolean isDarkModeEnabled() {
        return prefs.getBoolean(KEY_DARK_MODE, false);
    }
}
