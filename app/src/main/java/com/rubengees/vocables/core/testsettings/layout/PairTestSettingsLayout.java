package com.rubengees.vocables.core.testsettings.layout;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rubengees.vocables.core.testsettings.TestSettings;

/**
 * Created by Ruben on 24.04.2015.
 */
public class PairTestSettingsLayout extends TestSettingsLayout {

    public PairTestSettingsLayout(Context context, OnTestSettingsListener listener) {
        super(context, listener);
    }

    @Override
    protected TestSettings generateSpecificTestSettings() {
        return new TestSettings();
    }

    @Override
    public View inflateSpecificLayout(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        return null;
    }
}
