package com.rubengees.vocables.core.mode;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * Created by ruben on 28.04.15.
 */
public class TimeMode extends Mode {

    protected TimeMode(final int played, final int correct, final int incorrect, final int bestTime, final int averageTime) {
        super(played, correct, incorrect, bestTime, averageTime);
    }

    @Override
    public int getColor(final Context context) {
        return 0;
    }

    @Override
    public int getDarkColor(final Context context) {
        return 0;
    }

    @Override
    public int getMinAmount() {
        return 0;
    }

    @Override
    public String getHelpText(final Context context) {
        return null;
    }

    @Override
    public String getTitle(final Context context) {
        return null;
    }

    @Override
    public String getShortTitle(final Context context) {
        return null;
    }

    @Override
    public Drawable getIcon(final Context context) {
        return null;
    }
}
