package com.rubengees.vocables.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rubengees.vocables.R;

import java.util.List;

/**
 * Created by ruben on 18.05.15.
 */
public class HelpAdapter extends RecyclerView.Adapter<HelpAdapter.ViewHolder> {

    private List<HelpItem> items;

    public HelpAdapter(List<HelpItem> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_help, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HelpItem current = items.get(position);

        holder.title.setText(current.getTitle());
        holder.text.setText(current.getText());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView text;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.help_item_title);
            text = (TextView) itemView.findViewById(R.id.help_item_text);
        }
    }
}
