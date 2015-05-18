package com.rubengees.vocables.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rubengees.vocables.R;
import com.rubengees.vocables.adapter.HelpAdapter;
import com.rubengees.vocables.adapter.HelpItem;
import com.rubengees.vocables.core.mode.Mode;
import com.rubengees.vocables.utils.Utils;

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

        View root = inflater.inflate(R.layout.fragment_help, container, false);
        RecyclerView recycler = (RecyclerView) root.findViewById(R.id.fragment_help_recycler);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(Utils.getSpanCount(getActivity()), StaggeredGridLayoutManager.VERTICAL);
        HelpAdapter adapter = new HelpAdapter(generateHelpItems());

        recycler.setHasFixedSize(true);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(adapter);

        return root;
    }

    private List<HelpItem> generateHelpItems() {
        List<HelpItem> result = new ArrayList<>();

        for (Mode mode : modes) {
            result.add(new HelpItem(mode.getTitle(getActivity()), mode.getHelpText(getActivity())));
        }

        String[] titles = getResources().getStringArray(R.array.help_titles);
        String[] texts = getResources().getStringArray(R.array.help_texts);

        for (int i = 0; i < titles.length; i++) {
            result.add(new HelpItem(titles[i], texts[i]));
        }

        return result;
    }

}
