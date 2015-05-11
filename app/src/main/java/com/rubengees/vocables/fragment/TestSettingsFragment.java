package com.rubengees.vocables.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.rubengees.vocables.R;
import com.rubengees.vocables.core.Core;
import com.rubengees.vocables.core.mode.Mode;
import com.rubengees.vocables.core.testsettings.TestSettings;
import com.rubengees.vocables.core.testsettings.layout.TestSettingsLayout;
import com.rubengees.vocables.data.VocableManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TestSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TestSettingsFragment extends MainFragment implements TestSettingsLayout.OnTestSettingsListener {

    private Mode mode;
    private TestSettings settings;
    private TestSettingsLayout layout;
    private VocableManager manager;

    private TextView status;
    private int vocableAmount;

    public TestSettingsFragment() {
        // Required empty public constructor
    }

    public static TestSettingsFragment newInstance(Mode mode) {
        TestSettingsFragment fragment = new TestSettingsFragment();
        Bundle args = new Bundle();
        args.putParcelable("mode", mode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode = getArguments().getParcelable("mode");
            layout = mode.getTestSettingsLayout(getActivity(), this);
        }

        if (savedInstanceState != null) {
            settings = savedInstanceState.getParcelable("test_settings");
        }

        manager = Core.getInstance(getActivity()).getVocableManager();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View header = inflater.inflate(R.layout.fragment_test_settings_header, container, false);
        status = (TextView) header.findViewById(R.id.fragment_test_settings_header_status);

        getMainActivity().setToolbarView(header, mode.getColor(getActivity()), true);
        FloatingActionButton fab = getMainActivity().getFAB();
        fab.setImageResource(R.drawable.ic_next);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vocableAmount >= mode.getMinAmount()) {
                    //TODO show TestFragment
                } else {
                    Toast.makeText(getActivity(), "You don't have enough Vocables selected. You need at least" + " " + mode.getMinAmount(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        // AnimationUtils.animate(fab, Techniques.Landing, 500, 0, null);

        View root = layout.inflateLayout(inflater, container, savedInstanceState);

        if (savedInstanceState == null) {
            settings = layout.generateTestSettings();
            updateStatus(calculateAmount(settings));
        } else {
            this.vocableAmount = savedInstanceState.getInt("vocable_amount");
            updateStatus(vocableAmount);
        }

        return root;
    }

    private void updateStatus(Integer amount) {
        this.status.setText("Vocable selected:" + " " + (amount == null ? calculateAmount(settings) : amount));
    }

    private int calculateAmount(TestSettings settings) {
        int result = 0;
        for (Integer integer : settings.getUnitIds()) {
            result += manager.getUnit(integer).getVocables(settings.getMaxRate()).size();
        }

        this.vocableAmount = result;

        return result;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("test_settings", settings);
        outState.putInt("vocable_amount", vocableAmount);
        layout.saveInstanceState(outState);
    }

    @Override
    public void onChange(TestSettings settings) {
        this.settings = settings;
        updateStatus(calculateAmount(settings));
    }
}
