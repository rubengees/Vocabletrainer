package com.rubengees.vocables.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.rubengees.vocables.R;
import com.rubengees.vocables.chart.ChartTools;
import com.rubengees.vocables.core.test.TestAnswer;
import com.rubengees.vocables.core.test.TestResult;

import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * Created by ruben on 02.06.15.
 */
public class ResultAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private TestResult result;

    public ResultAdapter(TestResult result) {
        this.result = result;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case 0:
                return new ViewHolderResultHeader(inflater.inflate(R.layout.list_item_result_header, parent, false));
            case 1:
                return new ViewHolderResult(inflater.inflate(R.layout.list_item_result, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolderResultHeader) {
            ViewHolderResultHeader viewHolder = (ViewHolderResultHeader) holder;

            ChartTools.generateAnswerChart(viewHolder.answers, result.getCorrect(), result.getIncorrect());
            ChartTools.generateTimeChart(viewHolder.times, result.getBestTime(), result.getAverageTime());
        } else if (holder instanceof ViewHolderResult) {
            ViewHolderResult viewHolder = (ViewHolderResult) holder;
            position--;
            TestAnswer current = result.getAnswerAt(position);

            if (current.isCorrect()) {
                viewHolder.icon.setImageResource(R.drawable.ic_correct);
                viewHolder.correction.setText(null);
            } else {
                viewHolder.icon.setImageResource(R.drawable.ic_incorrect);
                viewHolder.correction.setText("Correct:" + " " + current.getAnswer());
            }

            viewHolder.answer.setText(current.getQuestion() + " - " + (current.getGiven() == null ? "No Answer" : current.getGiven()));
        }
    }

    @Override
    public int getItemCount() {
        return result.getCorrect() + result.getIncorrect() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position <= 0) {
            return 0;
        } else {
            return 1;
        }
    }

    class ViewHolderResultHeader extends RecyclerView.ViewHolder {

        PieChartView answers;
        ColumnChartView times;

        public ViewHolderResultHeader(View itemView) {
            super(itemView);

            answers = (PieChartView) itemView.findViewById(R.id.list_item_result_answers);
            times = (ColumnChartView) itemView.findViewById(R.id.list_item_result_times);
        }
    }

    class ViewHolderResult extends RecyclerView.ViewHolder {

        ImageView icon;
        TextView answer;
        TextView correction;

        public ViewHolderResult(View itemView) {
            super(itemView);

            icon = (ImageView) itemView.findViewById(R.id.list_item_result_icon);
            answer = (TextView) itemView.findViewById(R.id.list_item_result_answer);
            correction = (TextView) itemView.findViewById(R.id.list_item_result_correction);
        }
    }
}
