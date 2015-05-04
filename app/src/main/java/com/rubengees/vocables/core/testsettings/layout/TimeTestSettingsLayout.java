package com.rubengees.vocables.core.testsettings.layout;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.rubengees.vocables.core.testsettings.TimeTestSettings;

/**
 * Created by Ruben on 04.05.2015.
 */
public class TimeTestSettingsLayout extends TestSettingsLayout {

    public TimeTestSettingsLayout(Context context) {
        super(context);
    }

    @Override
    public Button getNext() {
        return null;
    }

    @Override
    public TimeTestSettings generateTestSettings() {
        return null;
    }

    @Override
    public View getLayout() {
        return null;
    }
}
