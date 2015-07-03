package com.rubengees.vocables.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rubengees.vocables.R;

/**
 * Created by ruben on 29.06.15.
 */
public class StatisticsResetDialog extends DialogFragment {

    private StatisticsResetDialogCallback callback;

    public static StatisticsResetDialog newInstance() {
        return new StatisticsResetDialog();
    }

    public void setCallback(StatisticsResetDialogCallback callback) {
        this.callback = callback;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());

        builder.title(getString(R.string.dialog_reset_title)).content(getString(R.string.dialog_reset_content)).positiveText(getString(R.string.dialog_reset_ok))
                .negativeText(getString(R.string.dialog_cancel)).callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                super.onPositive(dialog);

                if (callback != null) {
                    callback.onReset();
                }
            }
        });

        return builder.build();
    }

    public interface StatisticsResetDialogCallback {
        void onReset();
    }
}
