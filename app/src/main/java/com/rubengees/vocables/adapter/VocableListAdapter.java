package com.rubengees.vocables.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import com.rubengees.vocables.enumeration.SortMode;

import java.util.Collection;

/**
 * Created by Ruben on 30.04.2015.
 */
public abstract class VocableListAdapter<T, H extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<H> {

    private SortMode sortMode;

    public VocableListAdapter(@NonNull SortMode sortMode) {
        this.sortMode = sortMode;
    }

    public abstract T remove(int pos);

    public abstract void clear();

    public abstract void add(@NonNull T item);

    public abstract void addAll(@NonNull Collection<T> items);

    public abstract void update(@NonNull T item, int pos);

    public abstract T get(int pos);

    public final boolean isEmpty() {
        return getCount() <= 0;
    }

    @Override
    public final int getItemCount() {
        return getCount() + 1;
    }

    public abstract int getCount();

    public void setMode(@NonNull SortMode mode) {
        this.sortMode = mode;
    }

    protected abstract boolean isLastPosition(int position);

    @Override
    public int getItemViewType(int position) {
        if (!isLastPosition(position)) {
            return 0;
        } else {
            return 1;
        }
    }

    protected SortMode getSortMode() {
        return sortMode;
    }
}
