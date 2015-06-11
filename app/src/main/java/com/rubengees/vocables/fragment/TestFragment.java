package com.rubengees.vocables.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.rubengees.vocables.R;
import com.rubengees.vocables.core.mode.Mode;
import com.rubengees.vocables.core.test.Test;
import com.rubengees.vocables.core.test.TestResult;
import com.rubengees.vocables.core.testsettings.TestSettings;
import com.rubengees.vocables.dialog.HintDialog;
import com.rubengees.vocables.pojo.Vocable;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TestFragment extends MainFragment implements Test.OnTestFinishedListener {

    public static final String HINT_DIALOG = "hint_dialog";
    private static final String MODE_KEY = "mode";
    private static final String SETTINGS_KEY = "settings";
    private Mode mode;
    private Test test;
    private TestSettings settings;

    private MenuItem hint;

    public TestFragment() {
        // Required empty public constructor
    }

    public static TestFragment newInstance(Mode mode, TestSettings settings) {
        TestFragment fragment = new TestFragment();
        Bundle args = new Bundle();

        args.putParcelable(MODE_KEY, mode);
        args.putParcelable(SETTINGS_KEY, settings);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mode = getArguments().getParcelable(MODE_KEY);
            settings = getArguments().getParcelable(SETTINGS_KEY);
        }

        if (savedInstanceState == null) {
            test = mode.getTest(getActivity(), settings, this);
        } else {
            test = mode.getTest(getActivity(), settings, this, savedInstanceState);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = test.getLayout();

        test.show();
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_test, menu);

        hint = menu.findItem(R.id.action_test_hint);
        hint.setVisible(test.setHintVisibilityListener(new Test.OnHintVisibilityListener() {
            @Override
            public void onHintVisibilityChanged(boolean visible) {
                hint.setVisible(visible);
            }
        }));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item == hint) {
            String hint = test.getHint();

            HintDialog.newInstance(hint).show(getFragmentManager(), HINT_DIALOG);
        }

        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        test.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        test.saveInstanceState(outState);
    }

    @Override
    public void onPause() {
        test.onPause();
        super.onPause();
    }

    @Override
    public void onTestFinished(TestResult result, TestSettings settings, ArrayList<Vocable> vocables) {
        getFragmentManager().beginTransaction().replace(R.id.content, TestResultFragment.newInstance(mode, result, settings, vocables)).commit();
    }
}
