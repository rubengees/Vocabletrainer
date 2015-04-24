package com.rubengees.vocables.utils;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by Ruben on 24.04.2015.
 */
public class PreferenceUtils {

    public static boolean shouldSignIn(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("start_games", false);
    }

    public static void setSignIn(Context context, boolean signIn) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("start_games", signIn).apply();
    }

    public static boolean isCaseSensitive(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("pref_case", false);
    }

    public static boolean isReminderEnabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("pref_reminder", false);
    }

    public static void setReminder(Context context, boolean shouldRemind) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("pref_reminder", shouldRemind).apply();
    }

    public static int getReminderTime(Context context) {
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("pref_reminder_time", "20"));
    }

    public static void setAds(Context context, boolean showAds) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("pref_ads", showAds).apply();
    }

    public static boolean shouldShowAds(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("pref_ads", false);
    }

    public static boolean areAnimationsEnabled(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("pref_animation", true);
    }

    public static void setFirstStarted(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("first_start", false).apply();
    }

    public static boolean isFirstStart(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("first_start", true);
    }

    public static void setEvaluated(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean("pref_evaluation", true).apply();
    }

    public static boolean hasEvaluated(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean("pref_evaluation", false);
    }
}
