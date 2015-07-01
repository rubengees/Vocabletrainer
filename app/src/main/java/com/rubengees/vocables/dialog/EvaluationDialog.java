package com.rubengees.vocables.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rubengees.vocables.R;

/**
 * A dialog to trigger evaluation of this App.
 *
 * @author Ruben Gees
 */
public class EvaluationDialog extends DialogFragment {

    private EvaluationDialogCallback callback;

    public static EvaluationDialog newInstance() {
        return new EvaluationDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());

        builder.title(getString(R.string.dialog_evaluation_title))
                .content(getString(R.string.dialog_evaluation_content)).positiveText(getString(R.string.dialog_evaluation_ok))
                .negativeText(getString(R.string.dialog_evaluation_no)).neutralText(getString(R.string.dialog_evaluation_later)).callback(new MaterialDialog.ButtonCallback() {
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
