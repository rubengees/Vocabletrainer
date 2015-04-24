package com.rubengees.vocables.core.testsettings.layout;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.rubengees.vocables.core.testsettings.TestSettings;

/**
 * Created by Ruben on 24.04.2015.
 */
public class ClassicTestSettingsLayout extends TestSettingsLayout {

    public ClassicTestSettingsLayout(Context context) {
        super(context);
    }

    @Override
    public Button getNext() {
        return null;
    }

    @Override
    public TestSettings generateTestSettings() {
        return null;
    }

    @Override
    public View getLayout() {
        return null;
    }
}
