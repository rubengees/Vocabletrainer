package com.rubengees.vocables.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.File;

/**
 * Created by Ruben on 31.05.2015.
 */
public class OverrideDialog extends DialogFragment {

    private OverrideDialogCallback callback;

    private String path;

    public static OverrideDialog newInstance(String path) {
        OverrideDialog dialog = new OverrideDialog();
        Bundle bundle = new Bundle();

        bundle.putString("path", path);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        path = getArguments().getString("path");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());

        builder.title("Export").content("The file already exists. Do you want to override it?").positiveText("Override").callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                super.onPositive(dialog);

                if (callback != null) {
                    callback.onOverride(new File(path));
                }
            }
        });

        return builder.build();
    }

    public void setCallback(OverrideDialogCallback callback) {
        this.callback = callback;
    }

    public interface OverrideDialogCallback {
        void onOverride(File file);
    }

}
