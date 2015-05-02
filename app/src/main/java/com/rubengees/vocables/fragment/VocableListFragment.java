package com.rubengees.vocables.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.melnykov.fab.FloatingActionButton;
import com.rubengees.vocables.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VocableListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VocableListFragment extends MainFragment {

    private RecyclerView recycler;
    private FloatingActionButton fab;

    public VocableListFragment() {
        // Required empty public constructor
    }

    public static VocableListFragment newInstance() {
        return new VocableListFragment();
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
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_vocable_list, container, false);

        recycler = (RecyclerView) root.findViewById(R.id.fragment_vocable_list_recycler);
        fab = (FloatingActionButton) root.findViewById(R.id.fragment_vocable_list_add);



        return root;
    }


}
