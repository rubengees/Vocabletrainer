package com.rubengees.vocables.core.testsettings.layout;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.rubengees.vocables.R;
import com.rubengees.vocables.core.testsettings.Direction;
import com.rubengees.vocables.core.testsettings.TestSettings;
import com.rubengees.vocables.core.testsettings.TimeTestSettings;

/**
 * Created by Ruben on 04.05.2015.
 */
public class TimeTestSettingsLayout extends TestSettingsLayout {

    private RadioGroup direction;

    public TimeTestSettingsLayout(Context context, OnTestSettingsListener listener) {
        super(context, listener);
    }

    @Override
    protected TestSettings generateSpecificTestSettings() {
        TimeTestSettings settings = new TimeTestSettings();

        settings.setDirection(getDirection());
        return settings;
    }

    protected Direction getDirection() {
        switch (direction.getCheckedRadioButtonId()) {
            case R.id.fragment_test_settings_direction_your_other:
                return Direction.FIRST;
            case R.id.fragment_test_settings_direction_other_your:
                return Direction.SECOND;
            case R.id.fragment_test_settings_direction_mix:
                return Direction.BOTH;
            default:
                return Direction.BOTH;
        }
    }

    @Override
    public void inflateSpecificLayout(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_test_settings_time, parent, true);
        direction = (RadioGroup) root.findViewById(R.id.fragment_test_settings_direction);

        for (int i = 0; i < direction.getChildCount(); i++) {
            View radioButton = direction.getChildAt(i);

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onChange(generateTestSettings());
                }
            });
        }
    }
}
