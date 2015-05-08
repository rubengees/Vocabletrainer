package com.rubengees.vocables.core.testsettings.layout;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;

import com.rubengees.vocables.core.testsettings.TestSettings;

/**
 * Created by Ruben on 24.04.2015.
 */
public class PairTestSettingsLayout extends TestSettingsLayout {

    private RadioGroup rate;
    private CheckBox all_units;

    protected PairTestSettingsLayout(Context context, OnTestSettingsListener listener) {
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
