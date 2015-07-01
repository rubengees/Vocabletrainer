package com.rubengees.vocables.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * A dialog showing all errors when trying to connect to PlayServices.
 *
 * @author Ruben Gees
 */
public class GoogleServiceErrorDialog extends DialogFragment {

    private static final String KEY_ERROR_CODE = "error_code";
    private static final String KEY_REQUEST_CODE = "request_code";
    private static final String KEY_ERROR_TEXT = "error_text";

    private Integer errorCode;
    private Integer requestCode;
    private String errorText;

    private GoogleServiceErrorDialogCallback callback;

    public static GoogleServiceErrorDialog newInstance(int errorCode, int requestCode) {
        GoogleServiceErrorDialog dialog = new GoogleServiceErrorDialog();
        Bundle bundle = new Bundle();

        bundle.putInt(KEY_ERROR_CODE, errorCode);
        bundle.putInt(KEY_REQUEST_CODE, requestCode);
        dialog.setArguments(bundle);
        return dialog;
    }

    public static GoogleServiceErrorDialog newInstance(String errorText) {
        GoogleServiceErrorDialog dialog = new GoogleServiceErrorDialog();
        Bundle bundle = new Bundle();

        bundle.putString(KEY_ERROR_TEXT, errorText);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(KEY_ERROR_CODE)) {
            errorCode = getArguments().getInt(KEY_ERROR_CODE);
        }

        if (getArguments().containsKey(KEY_REQUEST_CODE)) {
            requestCode = getArguments().getInt(KEY_REQUEST_CODE);
        }

        errorText = getArguments().getString(KEY_ERROR_TEXT);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if (errorText == null) {
            return GooglePlayServicesUtil.getErrorDialog(errorCode, getActivity(), requestCode);
        } else {
            return new MaterialDialog.Builder(getActivity())
                    .content(errorText).build();
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (callback != null) {
            callback.onDismiss();
        }
    }

    public void setCallback(GoogleServiceErrorDialogCallback callback) {
        this.callback = callback;
    }

    public interface GoogleServiceErrorDialogCallback {
        void onDismiss();
    }
}
