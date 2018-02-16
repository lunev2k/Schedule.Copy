package com.lunev2k.schedule.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.prefs.Preferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by lunev on 16.02.2018.
 */

public class PrefUtils {

    private static PrefUtils _preferences;
    private Preferences preferences;
    private Context context;

    private PrefUtils(Context context) {
        this.context = context;
    }

    public static PrefUtils getInstance(Context context) {
        if (_preferences == null) {
            _preferences = new PrefUtils(context);
        }
        return _preferences;
    }

    public int getInt(String name) {
        SharedPreferences preferences = context.getSharedPreferences("settings", MODE_PRIVATE);
        return preferences.getInt(name, 0);
    }

    public void putInt(String name, int value) {
        SharedPreferences preferences = context.getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor ed = preferences.edit();
        ed.putInt(name, value);
        ed.apply();
    }

    public long getLong(String name) {
        SharedPreferences preferences = context.getSharedPreferences("settings", MODE_PRIVATE);
        return preferences.getLong(name, 0);
    }

    public void putLong(String name, long value) {
        SharedPreferences preferences = context.getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor ed = preferences.edit();
        ed.putLong(name, value);
        ed.apply();
    }
}
