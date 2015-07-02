package com.rubengees.vocables.core.testsettings.layout;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.rubengees.vocables.R;
import com.rubengees.vocables.core.Core;
import com.rubengees.vocables.core.testsettings.TestSettings;
import com.rubengees.vocables.data.VocableManager;
import com.rubengees.vocables.pojo.Unit;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Ruben on 24.04.2015.
 */
public abstract class TestSettingsLayout {

    private static final String STATE_CHECKED_UNIT_AMOUNT = "checked_unit_amount";
    private static final int RATE_ALL = 100;
    private static final int RATE_OKAY = 60;
    private static final int RATE_BAD = 40;

    protected OnTestSettingsListener listener;
    private Context context;
    private RadioGroup rate;
    private CheckBox allUnits;
    private FlowLayout units;

    private int checkedUnitAmount;

    protected TestSettingsLayout(Context context, OnTestSettingsListener listener) {
        this.context = context;
        this.listener = listener;
    }

    protected Context getContext() {
        return context;
    }

    public TestSettings generateTestSettings() {
        TestSettings result = generateSpecificTestSettings();

        result.setMaxRate(getRate());
        for (Integer integer : getUnitIds()) {
            result.addUnitId(integer);
        }

        return result;
    }

    private int getRate() {
        switch (rate.getCheckedRadioButtonId()) {
            case R.id.fragment_test_settings_rate_all:
                return RATE_ALL;
            case R.id.fragment_test_settings_rate_okay:
                return RATE_OKAY;
            case R.id.fragment_test_settings_rate_bad:
                return RATE_BAD;
            default:
                return RATE_ALL;
        }
    }

    private List<Integer> getUnitIds() {
        List<Integer> result = new ArrayList<>();

        for (int i = 0; i < units.getChildCount(); i++) {
            CheckBox unitBox = (CheckBox) units.getChildAt(i);

            if (unitBox.isChecked()) {
                result.add((Integer) unitBox.getTag());
            }
        }

        return result;
    }

    protected abstract TestSettings generateSpecificTestSettings();

    public View inflateLayout(LayoutInflater inflater, ViewGroup viewGroup, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_test_settings, viewGroup, false);

        FrameLayout specificContainer = (FrameLayout) root.findViewById(R.id.fragment_test_settings_specific_container);
        rate = (RadioGroup) root.findViewById(R.id.fragment_test_settings_rate);
        allUnits = (CheckBox) root.findViewById(R.id.fragment_test_settings_all_units);
        units = (FlowLayout) root.findViewById(R.id.fragment_test_settings_units);

        inflateSpecificLayout(inflater, specificContainer, savedInstanceState);

        for (int i = 0; i < rate.getChildCount(); i++) {
            View radioButton = rate.getChildAt(i);

            radioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onChange(generateTestSettings());
                }
            });
        }

        allUnits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (allUnits.isChecked()) {

                    for (int i = 0; i < units.getChildCount(); i++) {
                        CheckBox unitBox = (CheckBox) units.getChildAt(i);

                        unitBox.setChecked(true);
                    }

                    checkedUnitAmount = units.getChildCount();
                } else {

                    for (int i = 0; i < units.getChildCount(); i++) {
                        CheckBox unitBox = (CheckBox) units.getChildAt(i);

                        unitBox.setChecked(false);
                    }

                    checkedUnitAmount = 0;
                }

                listener.onChange(generateTestSettings());
            }
        });


        VocableManager manager = Core.getInstance((Activity) context).getVocableManager();

        List<Unit> unitList = manager.getUnitList();
        Collections.sort(unitList);

        for (Unit unit : unitList) {
            CheckBox unitBox = (CheckBox) inflater.inflate(R.layout.fragment_test_settings_unit, viewGroup, false);

            unitBox.setText(unit.getTitle());
            unitBox.setTag(unit.getId());
            unitBox.setChecked(true);
            unitBox.setId(unit.getId());

            units.addView(unitBox);
        }

        allUnits.setChecked(true);

        if (savedInstanceState == null) {
            checkedUnitAmount = units.getChildCount();

        } else {
            checkedUnitAmount = savedInstanceState.getInt(STATE_CHECKED_UNIT_AMOUNT);
        }

        setupUnits();

        return root;
    }

    private void setupUnits() {

        for (int i = 0; i < units.getChildCount(); i++) {
            CheckBox unitBox = (CheckBox) units.getChildAt(i);

            unitBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CheckBox box = (CheckBox) view;

                    if (box.isChecked()) {
                        checkedUnitAmount++;

                        if (checkedUnitAmount == units.getChildCount()) {
                            allUnits.setChecked(true);
                        }
                    } else {
                        checkedUnitAmount--;
                    }

                    if (checkedUnitAmount == units.getChildCount()) {
                        allUnits.setChecked(true);
                    } else {
                        allUnits.setChecked(false);
                    }

                    listener.onChange(generateTestSettings());
                }
            });
        }

    }

    public void saveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(STATE_CHECKED_UNIT_AMOUNT, checkedUnitAmount);
    }

    public abstract void inflateSpecificLayout(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState);

    public interface OnTestSettingsListener {
        void onChange(TestSettings settings);
    }
}
