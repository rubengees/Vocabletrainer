package com.rubengees.vocables.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.rubengees.vocables.R;

import java.io.File;

/**
 * A dialog to confirm to override a File in an export.
 *
 * @author Ruben Gees
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

        builder.title(getString(R.string.export_title))
                .content(getString(R.string.dialog_override_content))
                .positiveText(getString(R.string.dialog_override_ok))
                .negativeText(getString(R.string.dialog_cancel))
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog materialDialog,
                                        @NonNull DialogAction dialogAction) {
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

    /**
     * Callback for the override.
     */
    public interface OverrideDialogCallback {
        void onOverride(File file);
    }

}
