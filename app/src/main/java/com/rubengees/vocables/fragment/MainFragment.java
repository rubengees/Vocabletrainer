package com.rubengees.vocables.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.rubengees.vocables.activity.MainActivity;

/**
 * Created by ruben on 01.05.15.
 */
public class MainFragment extends Fragment implements MainActivity.OnBackPressedListener {

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar ab = getMainActivity().getSupportActionBar();

        if (ab != null) {
            ab.setSubtitle(null);
        }
    }

    protected MainActivity getMainActivity() {
        return (MainActivity) getActivity();
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }
}
