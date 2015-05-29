package com.rubengees.vocables.adapter;

import android.support.annotation.NonNull;
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

    public abstract T remove(int pos);

    public abstract void clear();

    public abstract void add(T item);

    public abstract void addAll(List<T> items);

    public abstract void update(T item, int pos);

    public abstract T get(int pos);

    public boolean isEmpty(){
        return getItemCount() <= 0;
    }

    public void setMode(@NonNull SortMode mode) {
        this.sortMode = mode;
    }

    protected SortMode getSortMode(){
        return sortMode;
    }
}
