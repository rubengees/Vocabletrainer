package com.rubengees.vocables.core.testsettings.layout;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioGroup;

import com.rubengees.vocables.R;
import com.rubengees.vocables.core.testsettings.ClassicTestSettings;
import com.rubengees.vocables.core.testsettings.Direction;
import com.rubengees.vocables.core.testsettings.TestSettings;

/**
 * Created by Ruben on 24.04.2015.
 */
public class ClassicTestSettingsLayout extends TestSettingsLayout {

    private RadioGroup direction;
    private CheckBox caseSensitive;

    public ClassicTestSettingsLayout(Context context, OnTestSettingsListener listener) {
        super(context, listener);
    }

    @Override
    protected TestSettings generateSpecificTestSettings() {
        ClassicTestSettings settings = new ClassicTestSettings();

        settings.setCaseSensitive(caseSensitive.isChecked());
        settings.setDirection(getDirection());

        return settings;
    }

    private Direction getDirection() {
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
        View root = inflater.inflate(R.layout.fragment_test_settings_classic, parent, true);

        direction = (RadioGroup) root.findViewById(R.id.fragment_test_settings_direction);
        caseSensitive = (CheckBox) root.findViewById(R.id.fragment_test_settings_classic_case_sensitive);

        for (int i = 0; i < direction.getChildCount(); i++) {
            View radioButton = direction.getChildAt(i);

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onChange(generateTestSettings());
                }
            });
        }

        caseSensitive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onChange(ClassicTestSettingsLayout.super.generateTestSettings());
            }
        });
    }
}
