package com.rubengees.vocables.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by ruben on 05.05.15.
 */
public class EvaluationDialog extends DialogFragment {

    private EvaluationDialogCallback callback;

    public static EvaluationDialog newInstance() {
        return new EvaluationDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());

        builder.title("Evaluation").content("Do you like this App? Then rate it in the Play Store!").positiveText(android.R.string.ok).negativeText("No").neutralText("Later").callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                super.onPositive(dialog);

                if (callback != null) {
                    callback.onEvaluate();
                }
            }

            @Override
            public void onNegative(MaterialDialog dialog) {
                super.onNegative(dialog);

                if (callback != null) {
                    callback.onEvaluateNot();
                }
            }
        });

        return builder.build();
    }

    public void setCallback(EvaluationDialogCallback callback) {
        this.callback = callback;
    }

    public interface EvaluationDialogCallback {
        void onEvaluate();

        void onEvaluateNot();
    }
}
