package com.rubengees.vocables.utils;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

/**
 * Created by Ruben on 24.04.2015.
 */
public class PreferenceUtils {

    private static final String PREF_START_GAMES = "start_games";
    private static final String PREF_CASE = "pref_case";
    private static final String PREF_REMINDER = "pref_reminder";
    private static final String PREF_REMINDER_TIME = "pref_reminder_time";
    private static final String PREF_ADS = "pref_ads";
    private static final String PREF_ANIMATION = "pref_animation";
    private static final String PREF_FIRST_START = "first_start";
    private static final String PREF_EVALUATION = "pref_evaluation";

    public static boolean shouldSignIn(@NonNull Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREF_START_GAMES, false);
    }

    public static void setSignIn(@NonNull Context context, boolean signIn) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(PREF_START_GAMES, signIn).apply();
    }

    public static boolean isCaseSensitive(@NonNull Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREF_CASE, false);
    }

    public static boolean isReminderEnabled(@NonNull Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREF_REMINDER, false);
    }

    public static void setReminder(@NonNull Context context, boolean shouldRemind) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(PREF_REMINDER, shouldRemind).apply();
    }

    public static int getReminderTime(@NonNull Context context) {
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_REMINDER_TIME, "20"));
    }

    public static void setAds(@NonNull Context context, boolean showAds) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(PREF_ADS, showAds).apply();
    }

    public static boolean shouldShowAds(@NonNull Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREF_ADS, false);
    }

    public static boolean areAnimationsEnabled(@NonNull Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREF_ANIMATION, true);
    }

    public static void setFirstStarted(@NonNull Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(PREF_FIRST_START, false).apply();
    }

    public static boolean isFirstStart(@NonNull Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREF_FIRST_START, true);
    }

    public static void setEvaluated(@NonNull Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(PREF_EVALUATION, true).apply();
    }

    public static boolean hasEvaluated(@NonNull Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREF_EVALUATION, false);
    }
}
