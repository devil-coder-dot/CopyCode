package com.cosafe.android.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class BreathePreferences {
    private static final String BREATHE_PREFS = "BreathePreferences";
    static final String SELECTED_EXHALE_DURATION_KEY = "selectedExhaleDuration";
    static final String SELECTED_HOLD_DURATION_KEY = "selectedHoldDuration";
    static final String SELECTED_INHALE_DURATION_KEY = "selectedInhaleDuration";
    static final String SELECTED_PRESET_KEY = "selectedPreset";
    private static BreathePreferences instance;
    private SharedPreferences prefs;

    private BreathePreferences(Context context) {
        this.prefs = context.getSharedPreferences(BREATHE_PREFS, 0);
    }

    public static void init(Context context) {
        if (instance == null) {
            instance = new BreathePreferences(context);
        }
    }

    public static BreathePreferences getInstance() {
        if (instance == null) {
            Log.e(BreathePreferences.class.getSimpleName(), "Call init() first");
        }
        return instance;
    }

    public void putString(String str, String str2) {
        this.prefs.edit().putString(str, str2).apply();
    }

    public void putLong(String str, long j) {
        this.prefs.edit().putLong(str, j).apply();
    }

    public void putInt(String str, int i) {
        this.prefs.edit().putInt(str, i).apply();
    }

    public void putFloat(String str, float f) {
        this.prefs.edit().putFloat(str, f).apply();
    }

    public String getString(String str) {
        return this.prefs.getString(str, null);
    }

    public long getLong(String str) {
        return this.prefs.getLong(str, -1);
    }

    public int getInt(String str) {
        return this.prefs.getInt(str, -1);
    }

    public float getFloat(String str) {
        return this.prefs.getFloat(str, -1.0f);
    }

    public void clearAll() {
        this.prefs.edit().clear().apply();
    }
}
