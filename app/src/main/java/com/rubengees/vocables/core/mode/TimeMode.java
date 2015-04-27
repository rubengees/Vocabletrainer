package com.rubengees.vocables.core.mode;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.rubengees.vocables.R;

/**
 * Created by ruben on 28.04.15.
 */
public class TimeMode extends Mode {

    public TimeMode(final int played, final int correct, final int incorrect, final int bestTime, final int averageTime) {
        super(played, correct, incorrect, bestTime, averageTime);
    }

    @Override
    public int getColor(final Context context) {
        return context.getResources().getColor(R.color.time_mode);
    }

    @Override
    public int getDarkColor(final Context context) {
        return context.getResources().getColor(R.color.time_mode_dark);
    }

    @Override
    public int getMinAmount() {
        return 10;
    }

    @Override
    public String getHelpText(final Context context) {
        return "Test";
    }

    @Override
    public String getTitle(final Context context) {
        return "Time Mode";
    }

    @Override
    public String getShortTitle(final Context context) {
        return "Time";
    }

    @Override
    public Drawable getIcon(final Context context) {
        return ContextCompat.getDrawable(context, R.drawable.ic_mode_time);
    }
}
