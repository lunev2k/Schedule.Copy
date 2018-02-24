package com.lunev2k.schedule.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.lunev2k.schedule.App;

import javax.inject.Inject;

import static android.content.Context.MODE_PRIVATE;

public class PrefsUtils {

    @Inject
    Context mContext;

    private SharedPreferences mPreferences;

    public PrefsUtils(Context context) {
        App.getComponent().inject(this);
        mContext = context;
        mPreferences = mContext.getSharedPreferences("settings", MODE_PRIVATE);
    }

    public int getInt(String name) {
        return mPreferences.getInt(name, 0);
    }

    public void putInt(String name, int value) {
        SharedPreferences.Editor ed = mPreferences.edit();
        ed.putInt(name, value);
        ed.apply();
    }

    public long getLong(String name) {
        return mPreferences.getLong(name, 0);
    }

    public void putLong(String name, long value) {
        SharedPreferences.Editor ed = mPreferences.edit();
        ed.putLong(name, value);
        ed.apply();
    }
}
