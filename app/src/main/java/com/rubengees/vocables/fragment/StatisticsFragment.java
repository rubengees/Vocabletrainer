package com.rubengees.vocables.fragment;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rubengees.vocables.R;
import com.rubengees.vocables.chart.ChartAdapter;
import com.rubengees.vocables.core.mode.Mode;

import java.util.ArrayList;
import java.util.List;

public class StatisticsFragment extends MainFragment {

    private List<Mode> modes;

    public StatisticsFragment() {

    }

    public static StatisticsFragment newInstance(ArrayList<Mode> modes) {
        StatisticsFragment fragment = new StatisticsFragment();
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

        View root = inflater.inflate(R.layout.fragment_statistics, container, false);
        RecyclerView recyclerView = (RecyclerView) root.findViewById(R.id.fragment_statistics_recycler);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new ChartAdapter(modes));

        return root;
    }


}
