package com.rubengees.vocables.chart;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.rubengees.vocables.R;
import com.rubengees.vocables.core.mode.Mode;

import java.util.List;

import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * Created by Ruben on 07.03.2015.
 */
public class ChartAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Mode> modes;

    public ChartAdapter(List<Mode> modes) {
        this.modes = modes;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case 0:
                return new ViewHolderGeneral(inflater.inflate(R.layout.list_item_stat_general, parent, false));
            case 1:
                return new ViewHolderMode(inflater.inflate(R.layout.list_item_stat_mode, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderGeneral) {
            ViewHolderGeneral current = (ViewHolderGeneral) holder;
            int correct = 0;
            int incorrect = 0;

            for (Mode mode : modes) {
                correct += mode.getCorrect();
                incorrect += mode.getIncorrect();
            }

            ChartTools.generateModeChart(current.modes, modes);
            ChartTools.generateAnswerChart(current.answers, correct, incorrect);
        } else if (holder instanceof ViewHolderMode) {
            ViewHolderMode current = (ViewHolderMode) holder;
            Mode mode = modes.get(position - 1);

            current.title.setText(mode.getShortTitle(current.title.getContext()));
            current.title.setTextColor(mode.getColor(current.title.getContext()));
            ChartTools.generateAnswerChart(current.answers, mode.getCorrect(), mode.getIncorrect());
            ChartTools.generateTimeChart(current.times, mode.getBestTime(), mode.getAverageTime());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }

    @Override
    public int getItemCount() {
        return modes.size() + 1;
    }

    class ViewHolderMode extends RecyclerView.ViewHolder {

        TextView title;
        PieChartView answers;
        ColumnChartView times;

        public ViewHolderMode(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.stat_item_mode_title);
            answers = (PieChartView) itemView.findViewById(R.id.stat_item_mode_answers);
            times = (ColumnChartView) itemView.findViewById(R.id.stat_item_mode_times);
        }
    }

    class ViewHolderGeneral extends RecyclerView.ViewHolder {

        PieChartView modes;
        PieChartView answers;

        public ViewHolderGeneral(View itemView) {
            super(itemView);

            modes = (PieChartView) itemView.findViewById(R.id.stat_item_general_modes);
            answers = (PieChartView) itemView.findViewById(R.id.stat_item_general_answers);
        }
    }
}
