package com.rubengees.vocables.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rubengees.vocables.R;

import java.io.File;

/**
 * Created by Ruben on 31.05.2015.
 */
public class OverrideDialog extends DialogFragment {

    private static final String KEY_PATH = "path";
    private OverrideDialogCallback callback;

    private String path;

    public static OverrideDialog newInstance(String path) {
        OverrideDialog dialog = new OverrideDialog();
        Bundle bundle = new Bundle();

        bundle.putString(KEY_PATH, path);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        path = getArguments().getString(KEY_PATH);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());

        builder.title(getActivity().getString(R.string.export_title)).content(getActivity().getString(R.string.dialog_override_content))
                .positiveText(getActivity().getString(R.string.dialog_override_ok)).callback(new MaterialDialog.ButtonCallback() {
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
