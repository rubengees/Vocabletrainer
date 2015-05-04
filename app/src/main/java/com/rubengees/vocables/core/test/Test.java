package com.rubengees.vocables.core.test;

import android.content.Context;

import com.rubengees.vocables.core.testsettings.TestSettings;
import com.rubengees.vocables.pojo.Vocable;
import com.rubengees.vocables.utils.PreferenceUtils;

import java.util.List;

/**
 * Created by ruben on 28.04.15.
 */
public abstract class Test {

    private OnTestFinishedListener listener;
    private Context context;
    private TestSettings settings;
    private int color;
    private int darkColor;
    private boolean animate;
    private boolean caseSensitive;

    public Test(final OnTestFinishedListener listener, TestSettings settings, final Context context, final int color, final int darkColor) {
        this.listener = listener;
        this.settings = settings;
        this.context = context;
        this.color = color;
        this.darkColor = darkColor;
        animate = PreferenceUtils.areAnimationsEnabled(context);
        caseSensitive = PreferenceUtils.isCaseSensitive(context);
    }

    protected Context getContext() {
        return context;
    }

    protected TestSettings getSettings() {
        return settings;
    }

    protected int getColor() {
        return color;
    }

    protected int getDarkColor() {
        return darkColor;
    }

    protected boolean shouldAnimate() {
        return animate;
    }

    protected boolean isCaseSensitive() {
        return caseSensitive;
    }

    protected void finishTest(TestResult result, List<Vocable> vocables) {
        listener.onTestFinished(result, settings, vocables);
    }

    public interface OnTestFinishedListener {
        void onTestFinished(TestResult result, TestSettings settings, List<Vocable> vocables);
    }
}
