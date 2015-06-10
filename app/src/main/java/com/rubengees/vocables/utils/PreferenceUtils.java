package com.rubengees.vocables.utils;

import android.content.Context;
import android.preference.PreferenceManager;

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

    public static boolean shouldSignIn(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREF_START_GAMES, false);
    }

    public static void setSignIn(Context context, boolean signIn) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(PREF_START_GAMES, signIn).apply();
    }

    public static boolean isCaseSensitive(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREF_CASE, false);
    }

    public static boolean isReminderEnabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREF_REMINDER, false);
    }

    public static void setReminder(Context context, boolean shouldRemind) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(PREF_REMINDER, shouldRemind).apply();
    }

    public static int getReminderTime(Context context) {
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_REMINDER_TIME, "20"));
    }

    public static void setAds(Context context, boolean showAds) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(PREF_ADS, showAds).apply();
    }

    public static boolean shouldShowAds(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREF_ADS, false);
    }

    public static boolean areAnimationsEnabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREF_ANIMATION, true);
    }

    public static void setFirstStarted(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(PREF_FIRST_START, false).apply();
    }

    public static boolean isFirstStart(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREF_FIRST_START, true);
    }

    public static void setEvaluated(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(PREF_EVALUATION, true).apply();
    }

    public static boolean hasEvaluated(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREF_EVALUATION, false);
    }
}
