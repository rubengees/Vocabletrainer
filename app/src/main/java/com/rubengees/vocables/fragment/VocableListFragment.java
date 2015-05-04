package com.rubengees.vocables.fragment;


import android.app.Fragment;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.melnykov.fab.FloatingActionButton;
import com.rubengees.vocables.R;
import com.rubengees.vocables.adapter.UnitAdapter;
import com.rubengees.vocables.adapter.VocableAdapter;
import com.rubengees.vocables.adapter.VocableListAdapter;
import com.rubengees.vocables.core.Core;
import com.rubengees.vocables.data.VocableManager;
import com.rubengees.vocables.enumeration.SortMode;
import com.rubengees.vocables.pojo.Unit;
import com.rubengees.vocables.pojo.Vocable;
import com.rubengees.vocables.utils.SwipeToDismissTouchListener;
import com.rubengees.vocables.utils.Utils;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VocableListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VocableListFragment extends MainFragment implements UnitAdapter.OnItemClickListener, VocableAdapter.OnItemClickListener {

    private static final String SORT_MODE = "sort_mode";
    private static final String CURRENT_UNIT = "current_unit";

    private RecyclerView recycler;
    private FloatingActionButton fab;
    private GridLayoutManager layoutManager;

    private VocableListAdapter adapter;
    private VocableManager manager;

    private SortMode mode;

    public VocableListFragment() {
        // Required empty public constructor
    }

    public static VocableListFragment newInstance() {
        return new VocableListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mode = (SortMode) savedInstanceState.getSerializable(SORT_MODE);
        } else {
            mode = SortMode.TITLE;
        }

        manager = Core.getInstance(getActivity()).getVocableManager();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putSerializable(SORT_MODE, mode);
        if (adapter instanceof VocableAdapter) {
            outState.putParcelable(CURRENT_UNIT, ((VocableAdapter) adapter).getUnit());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_vocable_list, container, false);

        recycler = (RecyclerView) root.findViewById(R.id.fragment_vocable_list_recycler);
        fab = (FloatingActionButton) root.findViewById(R.id.fragment_vocable_list_add);

        setupRecycler(savedInstanceState);
        setupFAB();

        return root;
    }

    private void setupRecycler(Bundle savedInstanceState) {
        layoutManager = new GridLayoutManager(getActivity(), Utils.getSpanCount(getActivity()));

        recycler.setLayoutManager(layoutManager);
        recycler.setHasFixedSize(true);

        SwipeToDismissTouchListener listener = new SwipeToDismissTouchListener(recycler, new SwipeToDismissTouchListener.DismissCallbacks() {
            @Override
            public SwipeToDismissTouchListener.SwipeDirection canDismiss(int position) {
                return SwipeToDismissTouchListener.SwipeDirection.BOTH;
            }

            @Override
            public void onDismiss(RecyclerView view, List<SwipeToDismissTouchListener.PendingDismissData> dismissData) {
                for (SwipeToDismissTouchListener.PendingDismissData pendingDismissData : dismissData) {
                    if (adapter instanceof VocableAdapter) {
                        Unit unit = ((VocableAdapter) adapter).getUnit();
                        Vocable vocable = ((VocableAdapter) adapter).get(pendingDismissData.position);

                        unit.remove(vocable);
                        manager.vocableRemoved(unit, vocable);
                    } else {
                        Unit unit = ((UnitAdapter) adapter).get(pendingDismissData.position);

                        manager.removeUnit(unit);
                    }

                    adapter.remove(pendingDismissData.position);
                }

                checkAdapter();
            }
        });

        recycler.addOnItemTouchListener(listener);

        if (savedInstanceState != null) {
            Unit current = savedInstanceState.getParcelable(CURRENT_UNIT);

            if (current == null) {
                setUnitAdapter();
            } else {
                setVocableAdapter(current);
            }
        } else {
            setUnitAdapter();
        }

        checkAdapter();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        layoutManager.setSpanCount(Utils.getSpanCount(getActivity()));
    }

    private void setupFAB() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO show Dialog
            }
        });
    }

    private void setUnitAdapter() {
        adapter = new UnitAdapter(manager.getUnitList(), mode, this);

        recycler.setAdapter(adapter);
        getMainActivity().setToolbarView(null, 0);
    }

    private void setVocableAdapter(Unit unit) {
        adapter = new VocableAdapter(unit, mode, this);

        recycler.setAdapter(adapter);
        View header = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_vocable_list_header, null);
        ImageButton back = (ImageButton) header.findViewById(R.id.fragment_vocable_list_header_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUnitAdapter();
            }
        });

        getMainActivity().setToolbarView(header, getResources().getColor(R.color.primary));
    }

    private void checkAdapter() {
        if (adapter.isEmpty()) {
            if (adapter instanceof VocableAdapter) {
                setUnitAdapter();
            } else if (adapter instanceof UnitAdapter) {
                //TODO Show View
            }
        }
    }

    @Override
    public void onItemClick(Unit unit) {
        setVocableAdapter(unit);
    }

    @Override
    public void onItemEdit(Unit unit) {
        //TODO Show Edit Dialog
    }

    @Override
    public void onInfoClick(Unit unit) {

    }

    @Override
    public void onItemClick(Unit unit, Vocable vocable) {
        //TODO Show Dialog
    }

    @Override
    public void onInfoClick(Vocable vocable) {

    }
}
