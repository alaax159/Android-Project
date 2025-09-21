package com.example.project;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreManager {
    private static final String SHARED_PREF_NAME = "SHAREDPREFERENCES";
    private static final int SHARED_PREF_PRIVATE = Context.MODE_PRIVATE;

    private static SharedPreManager ourInstance = null;
    private static SharedPreferences sharedPreferences = null;
    private SharedPreferences.Editor editor = null;

    // Constructor (no void!)
    SharedPreManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,
                SHARED_PREF_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static SharedPreManager getInstance(Context context) {
        if (ourInstance == null) {
            ourInstance = new SharedPreManager(context);
        }
        return ourInstance;
    }

    public boolean writeString(String key, String value) {
        editor.putString(key, value);
        return editor.commit();
    }

    public String readString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public void deleteFromSharedPref(String key) {
        editor.remove(key);
        editor.commit();
    }
}
