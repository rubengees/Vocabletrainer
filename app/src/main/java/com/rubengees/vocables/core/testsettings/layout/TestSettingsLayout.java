package com.rubengees.vocables.core.testsettings.layout;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.rubengees.vocables.core.testsettings.TestSettings;

/**
 * Created by Ruben on 24.04.2015.
 */
public abstract class TestSettingsLayout {

    private Context context;

    protected TestSettingsLayout(Context context) {
        this.context = context;
    }

    protected Context getContext() {
        return context;
    }

    public abstract Button getNext();

    public abstract TestSettings generateTestSettings();

    public abstract View getLayout();
}
