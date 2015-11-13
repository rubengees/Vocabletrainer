package com.rubengees.vocables.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.rubengees.vocables.R;
import com.rubengees.vocables.activity.TransferActivity;
import com.rubengees.vocables.adapter.UnitAdapter;
import com.rubengees.vocables.adapter.VocableAdapter;
import com.rubengees.vocables.adapter.VocableListAdapter;
import com.rubengees.vocables.core.Core;
import com.rubengees.vocables.core.GoogleServiceConnection;
import com.rubengees.vocables.data.UndoManager;
import com.rubengees.vocables.data.VocableManager;
import com.rubengees.vocables.dialog.DeleteDialog;
import com.rubengees.vocables.dialog.ExportDialog;
import com.rubengees.vocables.dialog.ImportDialog;
import com.rubengees.vocables.dialog.InfoDialog;
import com.rubengees.vocables.dialog.SortDialog;
import com.rubengees.vocables.dialog.UnitDialog;
import com.rubengees.vocables.dialog.VocableDialog;
import com.rubengees.vocables.enumeration.SortMode;
import com.rubengees.vocables.pojo.Unit;
import com.rubengees.vocables.pojo.Vocable;
import com.rubengees.vocables.utils.AnimationUtils;
import com.rubengees.vocables.utils.ImportTask;
import com.rubengees.vocables.utils.SnackbarManager;
import com.rubengees.vocables.utils.Utils;

import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VocableListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VocableListFragment extends MainFragment implements UnitAdapter.OnItemClickListener, VocableAdapter.OnItemClickListener,
        VocableDialog.VocableDialogCallback, UnitDialog.UnitDialogCallback, SortDialog.SortDialogCallback, DeleteDialog.DeleteDialogCallback, ImportTask.OnImportFinishedListener {

    private static final String VOCABLE_DIALOG = "vocable_dialog";
    private static final String UNIT_DIALOG = "unit_dialog";
    private static final String DELETE_DIALOG = "delete_dialog";
    private static final String SORT_DIALOG = "sort_dialog";
    private static final String EXPORT_DIALOG = "export_dialog";
    private static final String IMPORT_DIALOG = "import_dialog";
    private static final String SORT_MODE = "sort_mode";
    private static final String CURRENT_UNIT = "current_unit";
    private static final String INFO_DIALOG = "info_dialog";
    private static final String FILTER = "filter";

    private RecyclerView recycler;
    private FloatingActionButton fab;
    private View header;
    private TextView unitTitle;

    private VocableListAdapter adapter;
    private VocableManager vocableManager;

    private String filter = null;
    private SortMode mode;

    private ViewGroup root;

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
            filter = savedInstanceState.getString(FILTER);
            mode = (SortMode) savedInstanceState.getSerializable(SORT_MODE);

            VocableDialog vocableDialog = (VocableDialog) getActivity().getFragmentManager().findFragmentByTag(
                    VOCABLE_DIALOG);
            UnitDialog unitDialog = (UnitDialog) getActivity().getFragmentManager().findFragmentByTag(UNIT_DIALOG);
            SortDialog sortDialog = (SortDialog) getActivity().getFragmentManager().findFragmentByTag(SORT_DIALOG);
            DeleteDialog deleteDialog = (DeleteDialog) getActivity().getFragmentManager().findFragmentByTag(DELETE_DIALOG);

            if (vocableDialog != null) {
                vocableDialog.setCallback(this);
            }

            if (unitDialog != null) {
                unitDialog.setCallback(this);
            }

            if (sortDialog != null) {
                sortDialog.setCallback(this);
            }

            if (deleteDialog != null) {
                deleteDialog.setCallback(this);
            }
        } else {
            mode = SortMode.TITLE;
        }

        vocableManager = Core.getInstance(getActivity()).getVocableManager();

        setHasOptionsMenu(true);
    }

    @Override
    public void onStop() {
        SnackbarManager.dismiss();
        super.onStop();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_vocable_list, menu);

        final MenuItem searchItem = menu.findItem(R.id.action_vocable_list_search);
        final SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                setFilter(query);
                updateCount();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                setFilter(newText);
                updateCount();

                return false;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                setFilter(null);
                updateCount();

                return false;
            }
        });

        if (filter != null) {
            searchItem.expandActionView();
            searchView.setQuery(filter, true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_vocable_list_delete:
                DeleteDialog deleteDialog = DeleteDialog.newInstance();

                deleteDialog.setCallback(this);
                deleteDialog.show(getFragmentManager(), DELETE_DIALOG);
                return true;
            case R.id.action_vocable_list_export:
                Intent exportIntent = new Intent(getActivity(), TransferActivity.class);
                exportIntent.putExtra("isImport", false);
                startActivityForResult(exportIntent, TransferActivity.REQUEST_EXPORT);
                return true;
            case R.id.action_vocable_list_import:
                Intent importIntent = new Intent(getActivity(), TransferActivity.class);
                importIntent.putExtra("isImport", true);
                startActivityForResult(importIntent, TransferActivity.REQUEST_IMPORT);
                return true;
            case R.id.action_vocable_list_sort:
                SortDialog sortDialog = SortDialog.newInstance(mode);

                sortDialog.setCallback(this);
                sortDialog.show(getFragmentManager(), SORT_DIALOG);
                return true;
        }

        return false;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(FILTER, filter);
        outState.putSerializable(SORT_MODE, mode);
        if (adapter instanceof VocableAdapter) {
            outState.putParcelable(CURRENT_UNIT, ((VocableAdapter) adapter).getUnit());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.fragment_vocable_list, container, false);

        recycler = (RecyclerView) root.findViewById(R.id.fragment_vocable_list_recycler);
        fab = (FloatingActionButton) root.findViewById(R.id.fab);

        header = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_vocable_list_header, container, false);
        ImageButton back = (ImageButton) header.findViewById(R.id.fragment_vocable_list_header_back);
        unitTitle = (TextView) header.findViewById(R.id.fragment_vocable_list_header_title);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setUnitAdapter();
            }
        });

        setupRecycler(savedInstanceState);
        setupFAB();

        if (getUndoManager().size() > 0) {
            showSnackbar();
        }

        return root;
    }

    private void setupRecycler(Bundle savedInstanceState) {
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), Utils.getSpanCount(getActivity()));

        recycler.setLayoutManager(layoutManager);
        recycler.setHasFixedSize(true);

        ItemTouchHelper swipeToDismissTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                // callback for drag-n-drop, false to skip this feature
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (position < adapter.getItemCount() - 1) {
                    if (adapter instanceof VocableAdapter) {
                        Unit unit = ((VocableAdapter) adapter).getUnit();
                        Vocable vocable = ((VocableAdapter) adapter).get(position);

                        getUndoManager().add(unit, vocable);
                        unit.remove(vocable);
                        vocableManager.vocableRemoved(unit, vocable);
                    } else {
                        Unit unit = ((UnitAdapter) adapter).get(position);

                        getUndoManager().add(unit);
                        vocableManager.removeUnit(unit);
                    }

                    adapter.remove(position);
                    updateCount();

                    showSnackbar();

                    checkAdapter();
                }
            }
        });

        swipeToDismissTouchHelper.attachToRecyclerView(recycler);

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

    private void showSnackbar() {
        int amount = getUndoManager().size();
        String text = amount + " " + (amount == 1 ? getString(R.string.vocable) : getString(R.string.vocables))
                + " " + getString(R.string.fragment_vocable_list_deleted_message);

        if (SnackbarManager.hasSnackbar()) {
            SnackbarManager.update(text);
        } else {
            SnackbarManager.show(Snackbar.make(root, text, Snackbar.LENGTH_INDEFINITE), getString(R.string.fragment_vocable_list_undo), new SnackbarManager.SnackbarCallback() {
                @Override
                public void onDismiss(View v) {
                    getUndoManager().clear();
                }

                @Override
                public void onClick(View v) {
                    undo();
                }
            });
        }
    }

    private void setupFAB() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer unitId = null;

                if (adapter instanceof VocableAdapter) {
                    unitId = ((VocableAdapter) adapter).getUnit().getId();
                }

                showVocableDialog(unitId, null, null);
            }
        });
        fab.setImageResource(R.drawable.ic_add);

        AnimationUtils.scaleIn(fab, ANIMATION_DURATION, null, null);
    }

    private void setUnitAdapter() {
        getToolbarActivity().collapseToolbar();
        List<Unit> units = vocableManager.getUnitList();

        adapter = new UnitAdapter(units, mode, this);

        setAdapter(adapter);
    }

    private void setVocableAdapter(@NonNull Unit unit) {
        getToolbarActivity().expandToolbar();
        getToolbarActivity().setToolbarView(header);

        adapter = new VocableAdapter(unit, mode, this);

        unitTitle.setText(unit.getTitle());
        setAdapter(adapter);
    }

    private void setAdapter(VocableListAdapter adapter) {
        adapter.setFilter(filter);

        recycler.setAdapter(adapter);
        updateCount();
    }

    private void checkAdapter() {
        if (adapter.isEmpty()) {
            if (adapter instanceof VocableAdapter) {
                setUnitAdapter();
            } else if (adapter instanceof UnitAdapter) {
                //TODO Show Empty-View
            }
        }
    }

    private void updateCount() {
        int amount = adapter.getCount();
        String subTitle = amount + " ";
        if (adapter instanceof UnitAdapter) {
            subTitle += amount == 1 ? getString(R.string.unit) : getString(R.string.units);
        } else if (adapter instanceof VocableAdapter) {
            subTitle += amount == 1 ? getString(R.string.vocable) : getString(R.string.vocables);
        }

        getToolbarActivity().setSubtitle(subTitle);
    }

    @Override
    public boolean onBackPressed() {
        if (adapter instanceof VocableAdapter) {
            setUnitAdapter();
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onItemClick(Unit unit) {
        setVocableAdapter(unit);
    }

    @Override
    public void onItemEdit(Unit unit, int pos) {
        UnitDialog dialog = UnitDialog.newInstance(unit.getId(), pos);

        dialog.setCallback(this);
        dialog.show(getFragmentManager(), UNIT_DIALOG);
    }

    @Override
    public void onInfoClick(Unit unit) {
        InfoDialog.newInstance(unit).show(getFragmentManager(), INFO_DIALOG);
    }

    @Override
    public void onItemClick(Unit unit, Vocable vocable, int pos) {
        showVocableDialog(unit.getId(), vocable, pos);
    }

    @Override
    public void onInfoClick(Vocable vocable) {
        InfoDialog.newInstance(vocable).show(getFragmentManager(), INFO_DIALOG);
    }

    @Override
    public void onVocableAdded(Unit unit, Vocable vocable) {
        unit.add(vocable);

        boolean unitFound;
        if (vocableManager.getUnit(unit.getId()) == null) {
            unitFound = vocableManager.addUnit(unit);
        } else {
            vocableManager.vocableAdded(unit, vocable);
            unitFound = true;
        }

        if (adapter instanceof VocableAdapter) {
            if (((VocableAdapter) adapter).getUnit() == unit) {
                ((VocableAdapter) adapter).add(vocable);
            }
        } else if (adapter instanceof UnitAdapter) {
            if (!unitFound) {
                ((UnitAdapter) adapter).add(unit);
            }
        }

        updateCount();

        GoogleServiceConnection connection = Core.getInstance(getActivity()).getConnection();
        connection.incrementAchievement(getString(R.string.achievement_ready_to_learn));
        connection.incrementAchievement(getString(R.string.achievement_a_lot_to_do));
    }

    @Override
    public void onVocableChanged(Unit newUnit, Unit oldUnit, Vocable vocable, int pos) {
        vocableManager.updateVocable(oldUnit, newUnit, vocable);

        if (oldUnit == newUnit) {
            if (adapter instanceof VocableAdapter) {
                ((VocableAdapter) adapter).update(vocable, pos);
            }
        } else {
            if (adapter instanceof VocableAdapter) {
                ((VocableAdapter) adapter).remove(vocable);
            } else if (adapter instanceof UnitAdapter) {
                ((UnitAdapter) adapter).add(newUnit);
            }
        }

        updateCount();
        checkAdapter();
    }

    private void showVocableDialog(Integer unitId, Vocable vocable, Integer pos) {
        VocableDialog dialog = VocableDialog.newInstance(unitId, vocable, pos);

        dialog.setCallback(VocableListFragment.this);
        dialog.show(getFragmentManager(), VOCABLE_DIALOG);
    }

    private void setFilter(String filter) {
        this.filter = filter;
        adapter.setFilter(filter);
    }

    private void undo() {
        HashMap<Integer, Unit> units = getUndoManager().getUnits();

        if (adapter instanceof VocableAdapter) {
            Unit current = units.get(((VocableAdapter) adapter).getUnit().getId());

            if (current != null) {
                ((VocableAdapter) adapter).addAll(current.getVocables());
            }
        } else if (adapter instanceof UnitAdapter) {
            ((UnitAdapter) adapter).addAll(units.values());
        }

        updateCount();

        vocableManager.addUnits(units.values());
        getUndoManager().clear();
    }

    @Override
    public void onUnitChanged(Unit unit, int pos) {
        vocableManager.updateUnit(unit);

        if (adapter instanceof UnitAdapter) {
            ((UnitAdapter) adapter).update(unit, pos);
        }
    }

    @Override
    public void onSort(SortMode mode) {
        this.mode = mode;

        if (adapter instanceof UnitAdapter) {
            setUnitAdapter();
        } else if (adapter instanceof VocableAdapter) {
            setVocableAdapter(((VocableAdapter) adapter).getUnit());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == TransferActivity.REQUEST_EXPORT) {
                String path = data.getStringExtra("path");

                ExportDialog.newInstance(path).show(getFragmentManager(), EXPORT_DIALOG);
            } else if (requestCode == TransferActivity.REQUEST_IMPORT) {
                String path = data.getStringExtra("path");
                ImportDialog dialog = ImportDialog.newInstance(path);

                dialog.setListener(this);
                dialog.show(getFragmentManager(), IMPORT_DIALOG);
            }
        }
    }

    @Override
    public void onDelete() {
        vocableManager.clear();
        SnackbarManager.dismiss();
        getUndoManager().clear();
        adapter.clear();
        updateCount();

        checkAdapter();
        Core.getInstance(getActivity()).getConnection().unlockAchievement(getString(R.string.achievement_fresh_start));
    }

    @Override
    public void onImportFinished(String result) {
        if (adapter instanceof VocableAdapter) {
            setVocableAdapter(((VocableAdapter) adapter).getUnit());
        } else if (adapter instanceof UnitAdapter) {
            setUnitAdapter();
        }
    }

    private UndoManager getUndoManager() {
        return Core.getInstance(getActivity()).getUndoManager();
    }
}
