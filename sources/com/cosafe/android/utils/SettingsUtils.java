package com.cosafe.android.utils;

public class SettingsUtils {
    public static int getBackgroundByPresetPosition(int i) {
        return Preset.values()[i].getResId();
    }

    public static void saveSelectedPreset(int i) {
        BreathePreferences.getInstance().putInt("selectedPreset", i);
    }

    public static int getSelectedPreset() {
        int i = BreathePreferences.getInstance().getInt("selectedPreset");
        if (i != -1) {
            return i;
        }
        return 0;
    }

    public static void saveSelectedInhaleDuration(int i) {
        BreathePreferences.getInstance().putInt("selectedInhaleDuration", i);
    }

    public static int getSelectedInhaleDuration() {
        int i = BreathePreferences.getInstance().getInt("selectedInhaleDuration");
        return i != -1 ? i : Constants.DEFAULT_DURATION;
    }

    public static void saveSelectedExhaleDuration(int i) {
        BreathePreferences.getInstance().putInt("selectedExhaleDuration", i);
    }

    public static int getSelectedExhaleDuration() {
        int i = BreathePreferences.getInstance().getInt("selectedExhaleDuration");
        return i != -1 ? i : Constants.DEFAULT_DURATION;
    }

    public static void saveSelectedHoldDuration(int i) {
        BreathePreferences.getInstance().putInt("selectedHoldDuration", i);
    }

    public static int getSelectedHoldDuration() {
        int i = BreathePreferences.getInstance().getInt("selectedHoldDuration");
        return i != -1 ? i : Constants.DEFAULT_DURATION;
    }
}
