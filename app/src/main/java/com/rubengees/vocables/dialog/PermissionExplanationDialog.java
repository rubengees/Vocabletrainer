package com.rubengees.vocables.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rubengees.vocables.R;

/**
 * Todo: Describe Class
 *
 * @author Ruben Gees
 */
public class PermissionExplanationDialog extends DialogFragment {

    public static final String KEY_MESSAGE = "message";
    private PermissionExplanationDialogCallback callback;
    private String message;

    public static PermissionExplanationDialog newInstance(String message) {
        PermissionExplanationDialog dialog = new PermissionExplanationDialog();
        Bundle bundle = new Bundle();

        bundle.putString(KEY_MESSAGE, message);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        message = getArguments().getString(KEY_MESSAGE);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getContext());

        builder.title(R.string.dialog_permission_title).content(message)
                .negativeText(R.string.dialog_close).callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onNegative(MaterialDialog dialog) {
                if (callback != null) {
                    callback.onDialogClosed();
                }
            }
        });
        return builder.build();
    }

    public void setCallback(PermissionExplanationDialogCallback callback) {
        this.callback = callback;
    }

    public interface PermissionExplanationDialogCallback {
        void onDialogClosed();
    }
}
