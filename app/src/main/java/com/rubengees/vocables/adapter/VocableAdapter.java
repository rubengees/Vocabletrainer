package com.rubengees.vocables.adapter;

import android.support.annotation.NonNull;
import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.rubengees.vocables.R;
import com.rubengees.vocables.enumeration.SortMode;
import com.rubengees.vocables.pojo.Unit;
import com.rubengees.vocables.pojo.Vocable;
import com.rubengees.vocables.utils.Utils;

import java.util.Collection;

/**
 * Created by Ruben on 30.04.2015.
 */
public class VocableAdapter extends VocableListAdapter<Vocable, RecyclerView.ViewHolder> {

    private static final int FAB_MARGIN = 56 + 32;  //FAB size + Margin

    private Unit unit;
    private SortedList<Vocable> list;
    private OnItemClickListener listener;

    public VocableAdapter(Unit unit, SortMode sortMode, OnItemClickListener listener) {
        super(unit.getVocables(), sortMode);
        this.unit = unit;
        this.listener = listener;
        list = new SortedList<>(Vocable.class, new SortedList.Callback<Vocable>() {
            @Override
            public int compare(Vocable vocable, Vocable other) {
                switch (getSortMode()) {
                    case TITLE:
                        int result = vocable.getFirstMeaningList().compareTo(other.getFirstMeaningList());

                        if (result == 0) {
                            return vocable.getSecondMeaningList().compareTo(other.getSecondMeaningList());
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
        getItems().remove(get(pos));

        return list.removeItemAt(pos);
    }

    public void remove(Vocable vocable) {
        getItems().remove(vocable);

        list.remove(vocable);
    }

    @Override
    public void clear() {
        getItems().clear();

        list.clear();
    }

    @Override
    public void add(@NonNull Vocable item) {
        super.add(item);

        if (matchesFilter(getFilter(), item)) {
            list.add(item);
        }
    }

    @Override
    public void addAll(@NonNull Collection<Vocable> items) {
        super.addAll(items);

        list.beginBatchedUpdates();

        for (Vocable item : items) {
            if (matchesFilter(getFilter(), item)) {
                list.add(item);
            }
        }

        list.endBatchedUpdates();
    }

    @Override
    public void update(@NonNull Vocable item, int pos) {
        if (matchesFilter(getFilter(), item)) {
            list.updateItemAt(pos, item);
        } else {
            list.removeItemAt(pos);
        }
    }

    @Override
    public Vocable get(int pos) {
        return list.get(pos);
    }

    public Unit getUnit() {
        return unit;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new ViewHolder(View.inflate(parent.getContext(), R.layout.list_item_vocable, null));
            case 1:
                View space = new View(parent.getContext());

                space.setMinimumHeight(Utils.dpToPx(parent.getContext(), FAB_MARGIN));
                return new ViewHolderSpace(space);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof VocableAdapter.ViewHolder) {
            Vocable vocable = list.get(i);

            ((ViewHolder) viewHolder).firstMeaning.setText(vocable.getFirstMeaningList().toString());
            ((ViewHolder) viewHolder).secondMeaning.setText(vocable.getSecondMeaningList().toString());
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    protected boolean isLastPosition(int position) {
        return position == list.size();
    }

    @Override
    public void setFilter(String filter) {
        super.setFilter(filter);

        list.beginBatchedUpdates();
        if (filter == null) {
            list.clear();
            list.addAll(getItems());
        } else {
            list.clear();

            for (Vocable vocable : getItems()) {
                if (matchesFilter(filter, vocable)) {
                    list.add(vocable);
                }
            }
        }
        list.endBatchedUpdates();
    }

    private boolean matchesFilter(String filter, Vocable item) {
        if (filter == null) {
            return true;
        }

        for (String meaning : item.getFirstMeaningList()) {
            if (meaning.toLowerCase().contains(filter.toLowerCase())) {
                return true;
            }
        }

        for (String meaning : item.getSecondMeaningList()) {
            if (meaning.toLowerCase().contains(filter.toLowerCase())) {
                return true;
            }
        }

        return false;
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
