package com.rubengees.vocables.core.testsettings.layout;

import android.content.Context;
import android.view.View;

import com.rubengees.vocables.core.testsettings.TestSettings;

/**
 * Created by Ruben on 24.04.2015.
 */
public abstract class TestSettingsLayout {

    private Context context;
    private OnTestSettingsListener listener;

    protected TestSettingsLayout(Context context, OnTestSettingsListener listener) {
        this.context = context;
        this.listener = listener;
    }

    protected Context getContext() {
        return context;
    }

    public TestSettings generateTestSettings() {
        TestSettings settings = generateSpecificTestSettings();

        return settings;
    }

    protected abstract TestSettings generateSpecificTestSettings();

    public abstract View getLayout();

    protected void finish() {
        if (listener != null) {
            listener.onComplete();
        }
    }

    public interface OnTestSettingsListener {
        void onComplete();
    }
}
