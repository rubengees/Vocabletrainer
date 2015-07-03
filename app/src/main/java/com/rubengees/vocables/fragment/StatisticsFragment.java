package com.rubengees.vocables.fragment;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.rubengees.vocables.R;
import com.rubengees.vocables.chart.ChartAdapter;
import com.rubengees.vocables.core.mode.Mode;
import com.rubengees.vocables.data.Database;
import com.rubengees.vocables.dialog.StatisticsResetDialog;

import java.util.ArrayList;
import java.util.List;

public class StatisticsFragment extends MainFragment implements StatisticsResetDialog.StatisticsResetDialogCallback {

    private static final String KEY_MODES = "modes";
    private static final String RESET_DIALOG = "reset_dialog";
    private List<Mode> modes;
    private RecyclerView recyclerView;

    public StatisticsFragment() {

    }

    public static StatisticsFragment newInstance(ArrayList<Mode> modes) {
        StatisticsFragment fragment = new StatisticsFragment();
        Bundle args = new Bundle();

        args.putParcelableArrayList(KEY_MODES, modes);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            this.modes = getArguments().getParcelableArrayList(KEY_MODES);
        }

        StatisticsResetDialog resetDialog = (StatisticsResetDialog) getActivity().getFragmentManager().findFragmentByTag(RESET_DIALOG);

        if (resetDialog != null) {
            resetDialog.setCallback(this);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_statistics, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.fragment_statistics_recycler);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new ChartAdapter(modes));

        getToolbarActivity().collapseToolbar();

        return root;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_statistics_reset:
                StatisticsResetDialog dialog = StatisticsResetDialog.newInstance();

                dialog.setCallback(this);
                dialog.show(getFragmentManager(), RESET_DIALOG);
                return true;
        }

        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_statistics, menu);
    }

    @Override
    public void onReset() {
        Database database = new Database(getActivity());

        for (Mode mode : modes) {
            mode.reset();
            database.update(mode);
        }

        recyclerView.setAdapter(new ChartAdapter(modes));
    }
}
