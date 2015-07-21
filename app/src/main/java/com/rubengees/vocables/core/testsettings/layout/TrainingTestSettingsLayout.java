package com.rubengees.vocables.core.testsettings.layout;

import android.content.Context;

import com.rubengees.vocables.core.testsettings.TestSettings;
import com.rubengees.vocables.core.testsettings.TrainingTestSettings;

/**
 * Created by Ruben on 20.07.2015.
 */
public class TrainingTestSettingsLayout extends TimeTestSettingsLayout {

    public TrainingTestSettingsLayout(Context context, OnTestSettingsListener listener) {
        super(context, listener);
    }

    @Override
    protected TestSettings generateSpecificTestSettings() {
        TrainingTestSettings settings = new TrainingTestSettings();

        settings.setDirection(getDirection());
        return settings;
    }
}
