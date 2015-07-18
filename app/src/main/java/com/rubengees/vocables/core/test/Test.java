package com.rubengees.vocables.core.test;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.rubengees.vocables.R;
import com.rubengees.vocables.activity.ExtendedToolbarActivity;
import com.rubengees.vocables.core.test.logic.TestLogic;
import com.rubengees.vocables.core.testsettings.TestSettings;
import com.rubengees.vocables.pojo.Vocable;
import com.rubengees.vocables.utils.PreferenceUtils;
import com.rubengees.vocables.utils.SnackbarManager;

import java.util.ArrayList;

/**
 * Created by ruben on 28.04.15.
 */
public abstract class Test {

    protected static final int ANIMATION_TIME = 500;
    protected static final int WAIT_TIME = 1000;

    private OnTestFinishedListener testFinishedListener;
    private OnHintVisibilityListener hintVisibilityListener;
    private Context context;
    private TestSettings settings;
    private int color;
    private int darkColor;
    private boolean animate;

    private Bundle savedInstanceState;
    private View root;

    public Test(Context context, TestSettings settings, OnTestFinishedListener testFinishedListener, int color, int darkColor, Bundle savedInstanceState) {
        this(context, settings, testFinishedListener, color, darkColor);

        this.savedInstanceState = savedInstanceState;
    }

    public Test(Context context, TestSettings settings, OnTestFinishedListener testFinishedListener, int color, int darkColor) {
        this.context = context;
        this.settings = settings;
        this.testFinishedListener = testFinishedListener;
        this.color = color;
        this.darkColor = darkColor;
        this.animate = PreferenceUtils.areAnimationsEnabled(context);
    }

    protected void restoreSavedInstanceState(Bundle savedInstanceState) {

    }

    public void saveInstanceState(Bundle outState) {

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
        getToolbarActivity().setSubtitle((pos + 1) + "/" + amount);
    }

    protected final ExtendedToolbarActivity getToolbarActivity() {
        return (ExtendedToolbarActivity) context;
    }

    public final View getLayout() {
        root = getSpecificLayout();

        if (savedInstanceState != null) {
            restoreSavedInstanceState(savedInstanceState);
            savedInstanceState = null;
        }

        return root;
    }

    public abstract View getSpecificLayout();

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

    protected void showError() {
        SnackbarManager.show(Snackbar.make(getToolbarActivity().findViewById(R.id.content), context.getString(com.rubengees.vocables.R.string.test_error), Snackbar.LENGTH_INDEFINITE), null, null);
        finishTest(getLogic().getResult(), getLogic().getVocables());
    }

    public boolean setHintVisibilityListener(OnHintVisibilityListener hintVisibilityListener) {
        this.hintVisibilityListener = hintVisibilityListener;

        return getLogic().getHint() != null;
    }

    protected void changeHintVisibility(boolean visible) {
        if (hintVisibilityListener != null) {
            hintVisibilityListener.onHintVisibilityChanged(visible);
        }
    }

    protected void finishTest(TestResult result, ArrayList<Vocable> vocables) {
        testFinishedListener.onTestFinished(result, settings, vocables);
    }

    public String getHint() {
        return getLogic().getHint();
    }

    public interface OnTestFinishedListener {
        void onTestFinished(TestResult result, TestSettings settings, ArrayList<Vocable> vocables);
    }

    public interface OnHintVisibilityListener {
        void onHintVisibilityChanged(boolean visible);
    }
}
