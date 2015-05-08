package com.rubengees.vocables.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.afollestad.materialdialogs.MaterialDialog;

/**
 * Created by Ruben on 07.05.2015.
 */
public class DeleteDialog extends DialogFragment {

    private DeleteDialogCallback callback;

    public static DeleteDialog newInstance() {
        return new DeleteDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());

        builder.title("Delete all Vocables").content("Do you want to delete all of your Vocables?").positiveText("Delete").negativeText("Cancel").callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                super.onPositive(dialog);

                if (callback != null) {
                    callback.onDelete();
                }
            }
        });

        return builder.build();
    }

    public void setCallback(DeleteDialogCallback callback) {
        this.callback = callback;
    }

    public interface DeleteDialogCallback {
        void onDelete();
    }
}
