package com.rubengees.vocables.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rubengees.vocables.R;
import com.rubengees.vocables.core.mode.Mode;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TestSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TestSettingsFragment extends MainFragment {

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

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView textView = new TextView(getActivity());
        textView.setText(R.string.hello_blank_fragment);
        return textView;
    }


}
