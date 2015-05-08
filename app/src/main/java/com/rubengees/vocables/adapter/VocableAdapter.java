package com.rubengees.vocables.adapter;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.rubengees.vocables.R;
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
    private OnItemClickListener listener;

    public VocableAdapter(Unit unit, SortMode sortMode, OnItemClickListener listener) {
        super(sortMode);
        this.unit = unit;
        this.listener = listener;
        list = new SortedList<>(Vocable.class, new SortedList.Callback<Vocable>() {
            @Override
            public int compare(Vocable vocable, Vocable other) {
                switch (getSortMode()) {
                    case TITLE:
                        int result = vocable.getFirstMeaning().compareTo(other.getFirstMeaning());

                        if (result == 0) {
                            return vocable.getSecondMeaning().compareTo(other.getSecondMeaning());
                        } else {
                            return result;
                        }

                    case TIME:
                        return other.getLastModificationTime().compareTo(vocable.getLastModificationTime());
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
        }, unit.size());

        addAll(unit.getVocables());
    }

    @Override
    public Vocable remove(int pos) {
        return list.removeItemAt(pos);
    }

    public void remove(Vocable vocable) {
        list.remove(vocable);
    }

    @Override
    public void clear() {
        list.beginBatchedUpdates();
        for (int i = list.size() - 1; i >= 0; i--) {
            list.removeItemAt(i);
        }
        list.endBatchedUpdates();
    }

    @Override
    public void add(Vocable item) {
        list.add(item);
    }

    @Override
    public void addAll(List<Vocable> items) {
        list.beginBatchedUpdates();
        for (Vocable item : items) {
            list.add(item);
        }
        list.endBatchedUpdates();
    }

    @Override
    public void update(Vocable item, int pos) {
        list.updateItemAt(pos, item);
    }

    @Override
    public Vocable get(int pos) {
        return list.get(pos);
    }

    @Override
    public void refresh() {

    }

    public Unit getUnit() {
        return unit;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_vocable, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Vocable vocable = list.get(i);

        viewHolder.firstMeaning.setText(vocable.getFirstMeaning().toString());
        viewHolder.secondMeaning.setText(vocable.getSecondMeaning().toString());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Unit unit, Vocable vocable, int pos);

        void onInfoClick(Vocable vocable);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView firstMeaning;
        TextView secondMeaning;

        public ViewHolder(View itemView) {
            super(itemView);

            firstMeaning = (TextView) itemView.findViewById(R.id.list_item_vocable_first_meaning);
            secondMeaning = (TextView) itemView.findViewById(R.id.list_item_vocable_second_meaning);
            ImageButton icon = (ImageButton) itemView.findViewById(R.id.list_item_vocable_info);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(unit, list.get(getLayoutPosition()), getLayoutPosition());
                }
            });

            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onInfoClick(list.get(getLayoutPosition()));
                }
            });
        }
    }
}
