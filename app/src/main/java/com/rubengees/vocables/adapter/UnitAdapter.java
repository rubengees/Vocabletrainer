package com.rubengees.vocables.adapter;

import android.support.v7.util.SortedList;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.rubengees.vocables.R;
import com.rubengees.vocables.enumeration.SortMode;
import com.rubengees.vocables.pojo.Unit;

import java.util.List;

/**
 * Created by ruben on 02.05.15.
 */
public class UnitAdapter extends VocableListAdapter<Unit, UnitAdapter.ViewHolder> {

    private SortedList<Unit> list;
    private OnItemClickListener listener;

    public UnitAdapter(List<Unit> units, SortMode sortMode, OnItemClickListener listener) {
        super(sortMode);
        this.listener = listener;

        list = new SortedList<>(Unit.class, new SortedList.Callback<Unit>() {
            @Override
            public int compare(Unit o1, Unit o2) {
                switch (getSortMode()) {
                    case TITLE:
                        return o1.getTitle().compareToIgnoreCase(o2.getTitle());
                    case TIME:
                        return o1.getLastModificationTime().compareTo(o2.getLastModificationTime());
                    default:
                        return 0;
                }
            }

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position, count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position, count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition, toPosition);
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position, count);
            }

            @Override
            public boolean areContentsTheSame(Unit oldItem, Unit newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areItemsTheSame(Unit item1, Unit item2) {
                return item1.getId().equals(item2.getId());
            }
        }, units.size());

        addAll(units);
    }

    @Override
    public void remove(int pos) {
        list.removeItemAt(pos);
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
    public void add(Unit item) {
        list.add(item);
    }

    @Override
    public void addAll(List<Unit> items) {
        list.beginBatchedUpdates();
        for (Unit item : items) {
            list.add(item);
        }
        list.endBatchedUpdates();
    }

    @Override
    public void update(int pos, Unit item) {
        list.updateItemAt(pos, item);
    }

    @Override
    public Unit get(int pos) {
        return list.get(pos);
    }

    @Override
    public UnitAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(View.inflate(parent.getContext(), R.layout.list_item_unit, null));
    }

    @Override
    public void onBindViewHolder(UnitAdapter.ViewHolder holder, int position) {
        Unit unit = list.get(position);

        holder.title.setText(unit.getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface OnItemClickListener {
        void onItemClick(Unit unit);

        void onItemEdit(Unit unit);

        void onInfoClick(Unit unit);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        AppCompatTextView title;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (AppCompatTextView) itemView.findViewById(R.id.list_item_unit_title);
            ImageButton icon = (ImageButton) itemView.findViewById(R.id.list_item_unit_info);
            ImageButton edit = (ImageButton) itemView.findViewById(R.id.list_item_unit_edit);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(list.get(getLayoutPosition()));
                }
            });

            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onInfoClick(list.get(getLayoutPosition()));
                }
            });

            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemEdit(list.get(getLayoutPosition()));
                }
            });
        }
    }
}
