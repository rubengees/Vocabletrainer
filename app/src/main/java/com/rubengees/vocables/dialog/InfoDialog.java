package com.rubengees.vocables.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rubengees.vocables.R;
import com.rubengees.vocables.chart.ChartTools;
import com.rubengees.vocables.pojo.TrainerItem;

import lecho.lib.hellocharts.view.PieChartView;

/**
 * Created by ruben on 09.06.15.
 */
public class InfoDialog extends DialogFragment {

    private static TrainerItem item;

    public static InfoDialog newInstance(TrainerItem item) {
        InfoDialog dialog = new InfoDialog();
        Bundle bundle = new Bundle();

        bundle.putParcelable("item", (Parcelable) item);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        item = getArguments().getParcelable("item");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        View view = View.inflate(getActivity(), R.layout.dialog_info, null);
        PieChartView chart = (PieChartView) view.findViewById(R.id.dialog_info_answers);

        ChartTools.generateAnswerChart(chart, item.getCorrect(), item.getIncorrect());

        return builder.title("Info").customView(view, false).build();
    }
}
