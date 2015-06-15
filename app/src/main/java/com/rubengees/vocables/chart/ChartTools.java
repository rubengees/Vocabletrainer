package com.rubengees.vocables.chart;

import android.content.Context;

import com.rubengees.vocables.R;
import com.rubengees.vocables.core.mode.Mode;

import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.PieChartView;

/**
 * Created by Ruben Gees on 12.02.2015.
 */
public class ChartTools {

    public static void generateAnswerChart(PieChartView chart, int correct, int incorrect) {
        Context context = chart.getContext();
        List<Integer> values = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();

        values.add(correct);
        values.add(incorrect);
        colors.add(context.getResources().getColor(R.color.green));
        colors.add(context.getResources().getColor(R.color.red));
        titles.add(context.getString(R.string.chart_answer_correct));
        titles.add(context.getString(R.string.chart_answer_incorrect));

        generatePieChart(chart, values, colors, titles);
    }

    public static void generateModeChart(PieChartView chart, List<Mode> modes) {
        Context context = chart.getContext();
        List<Integer> values = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();

        for (Mode mode : modes) {
            values.add(mode.getPlayed());
            colors.add(mode.getColor(context));
            titles.add(mode.getShortTitle(context));
        }

        generatePieChart(chart, values, colors, titles);
    }

    public static void generateTimeChart(ColumnChartView chart, int bestTime, int averageTime) {
        Context context = chart.getContext();
        List<Integer> values = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        ArrayList<String> titles = new ArrayList<>();

        values.add(bestTime);
        values.add(averageTime);
        colors.add(context.getResources().getColor(R.color.primary));
        colors.add(context.getResources().getColor(R.color.primary_dark));
        titles.add(context.getString(R.string.chart_time_best));
        titles.add(context.getString(R.string.chart_time_average));

        generateColumnChart(chart, values, colors, titles);
    }

    private static void generatePieChart(PieChartView chart, List<Integer> values, ArrayList<Integer> colors, ArrayList<String> titles) {
        ArrayList<SliceValue> sliceValues = new ArrayList<>();

        for (int i = 0; i < values.size(); i++) {
            int value = values.get(i);

            if (value > 0) {
                SliceValue sliceValue = new SliceValue(value, colors.get(i));

                sliceValue.setLabel(titles.get(i));
                sliceValues.add(sliceValue);
            }
        }

        PieChartData data = new PieChartData(sliceValues);
        if (data.getValues().size() > 0) {
            stylePieChart(chart, data);
            chart.setPieChartData(data);
        }
    }

    private static void generateColumnChart(ColumnChartView chart, List<Integer> values, ArrayList<Integer> colors, ArrayList<String> titles) {
        ArrayList<Column> columns = new ArrayList<>();

        for (int i = 0; i < values.size(); i++) {
            int value = values.get(i);

            if (value > 0 && value < Integer.MAX_VALUE) {
                Column column = new Column();
                column.setHasLabels(true);

                List<SubcolumnValue> subColumnValues = new ArrayList<>(1);
                SubcolumnValue subcolumnValue = new SubcolumnValue(value, colors.get(i));
                subcolumnValue.setLabel(titles.get(i));
                subColumnValues.add(subcolumnValue);

                column.setValues(subColumnValues);
                columns.add(column);
            }
        }

        ColumnChartData data = new ColumnChartData(columns);
        if (data.getColumns().size() > 0) {
            styleColumnChart(chart, data);
            chart.setColumnChartData(data);
        }
    }

    private static void stylePieChart(PieChartView chart, PieChartData data) {
        Context context = chart.getContext();

        data.setHasLabels(true);
        data.setValueLabelsTextColor(context.getResources().getColor(android.R.color.white));
        chart.setZoomEnabled(false);
    }

    private static void styleColumnChart(ColumnChartView chart, ColumnChartData data) {
        Context context = chart.getContext();

        data.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(4));
        data.setValueLabelsTextColor(context.getResources().getColor(android.R.color.white));
        chart.setZoomEnabled(false);
    }
}
