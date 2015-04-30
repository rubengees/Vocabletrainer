package com.rubengees.vocables.adapter;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.rubengees.vocables.enumeration.SortMode;
import com.rubengees.vocables.pojo.Unit;
import com.rubengees.vocables.pojo.Vocable;

import java.util.List;

/**
 * Created by Ruben on 30.04.2015.
 */
public class VocableAdapter extends VocableListAdapter<Vocable, VocableAdapter.ViewHolder> {

    private Unit unit;
    private SortedList<Vocable> list;

    public VocableAdapter(Unit unit, SortMode sortMode) {
        super(sortMode);
        this.unit = unit;
        list = new SortedList<Vocable>(Vocable.class, new SortedList.Callback<Vocable>() {
            @Override
            public int compare(Vocable vocable, Vocable other) {
                switch(getSortMode()){
                    case TITLE:
                        int result = vocable.getFirstMeaning().compareTo(other.getFirstMeaning());

                        if(result == 0){
                            return vocable.getSecondMeaning().compareTo(other.getSecondMeaning());
                        }
                    case TIME:
                        return vocable.getLastModificationTime().compareTo(other.getLastModificationTime());
                    default:
                        return 0;
                }
            }

            @Override
            public void onInserted(int i, int i1) {
                notifyItemRangeInserted(i, i1);
            }

            @Override
            public void onRemoved(int i, int i1) {
                notifyItemRangeRemoved(i, i1);
            }

            @Override
            public void onMoved(int i, int i1) {
                notifyItemMoved(i, i1);
            }

            @Override
            public void onChanged(int i, int i1) {
                notifyItemRangeChanged(i, i1);
            }

            @Override
            public boolean areContentsTheSame(Vocable vocable, Vocable other) {
                return vocable.equals(other);
            }

            @Override
            public boolean areItemsTheSame(Vocable vocable, Vocable other) {
                return vocable.getId().equals(other.getId());
            }
        });
    }

    @Override
    public void remove(int pos) {

    }

    @Override
    public void clear() {

    }

    @Override
    public void add(Vocable item) {

    }

    @Override
    public void addAll(List<Vocable> items) {

    }

    @Override
    public void sort() {

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
