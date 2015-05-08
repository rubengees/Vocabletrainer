package com.rubengees.vocables.core.testsettings.layout;

import android.content.Context;
import android.view.View;

import com.rubengees.vocables.core.testsettings.TestSettings;

/**
 * Created by Ruben on 04.05.2015.
 */
public class TimeTestSettingsLayout extends TestSettingsLayout {

    protected TimeTestSettingsLayout(Context context, OnTestSettingsListener listener) {
        super(context, listener);
    }

    @Override
    protected TestSettings generateSpecificTestSettings() {
        return null;
    }

    @Override
    public View getLayout() {
        return null;
    }
}
