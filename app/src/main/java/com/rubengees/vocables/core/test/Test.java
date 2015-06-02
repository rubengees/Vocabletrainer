package com.rubengees.vocables.core.test;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.rubengees.vocables.activity.ExtendedToolbarActivity;
import com.rubengees.vocables.core.test.logic.TestLogic;
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

    public Test(Context context, TestSettings settings, OnTestFinishedListener listener, int color, int darkColor, Bundle savedInstanceState) {
        this(context, settings, listener, color, darkColor);

        restoreSavedInstanceState(savedInstanceState);
    }

    public Test(Context context, TestSettings settings, OnTestFinishedListener listener, int color, int darkColor) {
        this.context = context;
        this.settings = settings;
        this.listener = listener;
        this.color = color;
        this.darkColor = darkColor;
        this.animate = PreferenceUtils.areAnimationsEnabled(context);
    }

    protected void restoreSavedInstanceState(Bundle savedInstanceState) {

    }

    public void saveInstanceState(Bundle outState) {
        getLogic().saveInstanceState(outState);
    }

    protected abstract TestLogic getLogic();

    public final void onResume() {
        getLogic().onResume();
    }

    public final void onPause() {
        getLogic().onPause();
    }

    public void show() {
        TestLogic logic = getLogic();

        updateCount(logic.getPosition(), logic.getAmount());
    }

    private void updateCount(int pos, int amount) {
        ActionBar actionBar = getToolbarActivity().getSupportActionBar();

        if (actionBar != null) {
            actionBar.setSubtitle((pos + 1) + "/" + amount);
        }
    }

    protected final ExtendedToolbarActivity getToolbarActivity() {
        return (ExtendedToolbarActivity) context;
    }

    public abstract View getLayout();

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

    protected void finishTest(TestResult result, List<Vocable> vocables) {
        listener.onTestFinished(result, settings, vocables);
    }

    public interface OnTestFinishedListener {
        void onTestFinished(TestResult result, TestSettings settings, List<Vocable> vocables);
    }
}
