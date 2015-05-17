package com.rubengees.vocables.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rubengees.vocables.R;
import com.rubengees.vocables.core.mode.Mode;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HelpFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HelpFragment extends MainFragment {

    private List<Mode> modes;

    public HelpFragment() {
        // Required empty public constructor
    }

    public static HelpFragment newInstance(ArrayList<Mode> modes) {
        HelpFragment fragment = new HelpFragment();
        Bundle args = new Bundle();

        args.putParcelableArrayList("modes", modes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.modes = getArguments().getParcelableArrayList("modes");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_help, container, false);
    }


}
