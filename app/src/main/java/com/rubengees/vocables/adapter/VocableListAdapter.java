package com.rubengees.vocables.adapter;

import android.support.v7.widget.RecyclerView;

import com.rubengees.vocables.enumeration.SortMode;

import java.util.List;

/**
 * Created by Ruben on 30.04.2015.
 */
public abstract class VocableListAdapter<T, H extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<H> {

    private SortMode sortMode;

    public VocableListAdapter(SortMode sortMode) {
        this.sortMode = sortMode;
    }

    public abstract void remove(int pos);

    public abstract void clear();

    public abstract void add(T item);

    public abstract void addAll(List<T> items);

    public abstract void update(int pos, T item);

    public boolean isEmpty(){
        return getItemCount() <= 0;
    }

    protected SortMode getSortMode(){
        return sortMode;
    }
}
