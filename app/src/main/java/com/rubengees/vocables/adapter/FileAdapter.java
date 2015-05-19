package com.rubengees.vocables.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rubengees.vocables.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruben on 18.05.15.
 */
public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> {

    private List<File> files;
    private OnItemClickListener listener;

    public FileAdapter(List<File> files, OnItemClickListener listener) {
        this.files = files;
        this.listener = listener;
    }

    public FileAdapter(OnItemClickListener listener) {
        this.files = new ArrayList<>();
        this.listener = listener;
    }

    @Override
    public FileAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.file_item, parent, false));
    }

    @Override
    public void onBindViewHolder(FileAdapter.ViewHolder holder, int position) {
        File item = files.get(position);

        if (item.isDirectory()) {
            holder.icon.setImageResource(R.drawable.ic_folder);
        } else {
            holder.icon.setImageResource(R.drawable.ic_file);
        }

        holder.title.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public void setFiles(List<File> files) {
        this.files = files;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(File file);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);

            this.icon = (ImageView) itemView.findViewById(R.id.file_item_icon);
            this.title = (TextView) itemView.findViewById(R.id.file_item_title);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(files.get(getLayoutPosition()));
                }
            });
        }

    }
}
