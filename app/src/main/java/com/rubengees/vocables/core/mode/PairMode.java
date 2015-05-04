package com.rubengees.vocables.core.mode;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

import com.rubengees.vocables.R;

/**
 * Created by ruben on 28.04.15.
 */
public class PairMode extends Mode {

    public PairMode(final int played, final int correct, final int incorrect, final int bestTime, final int averageTime) {
        super(played, correct, incorrect, bestTime, averageTime);
    }

    @Override
    public int getColor(final Context context) {
        return context.getResources().getColor(R.color.pair_mode);
    }

    @Override
    public int getDarkColor(final Context context) {
        return context.getResources().getColor(R.color.pair_mode_dark);
    }

    @Override
    public int getMinAmount() {
        return 5;
    }

    @Override
    public String getHelpText(final Context context) {
        return "Test";
    }

    @Override
    public String getTitle(final Context context) {
        return "Pair Mode";
    }

    @Override
    public String getShortTitle(final Context context) {
        return "Pair";
    }

    @Override
    public Drawable getIcon(final Context context) {
        return ContextCompat.getDrawable(context, R.drawable.ic_mode_pair);
    }
}