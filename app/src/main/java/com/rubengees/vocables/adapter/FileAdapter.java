package com.rubengees.vocables.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.List;

/**
 * Created by ruben on 18.05.15.
 */
public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {

    private List<File> files;

    public FileAdapter(List<File> files) {
        this.files = files;
    }

    @Override
    public FileAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(FileAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }

    }
}
