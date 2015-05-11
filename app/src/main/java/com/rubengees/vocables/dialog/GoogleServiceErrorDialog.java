package com.rubengees.vocables.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by ruben on 05.05.15.
 */
public class GoogleServiceErrorDialog extends DialogFragment {

    private Integer errorCode;
    private Integer requestCode;
    private String errorText;

    private GoogleServiceErrorDialogCallback callback;

    public static GoogleServiceErrorDialog newInstance(int errorCode, int requestCode) {
        GoogleServiceErrorDialog dialog = new GoogleServiceErrorDialog();
        Bundle bundle = new Bundle();

        bundle.putInt("error_code", errorCode);
        bundle.putInt("request_code", requestCode);
        dialog.setArguments(bundle);
        return dialog;
    }

    public static GoogleServiceErrorDialog newInstance(String errorText) {
        GoogleServiceErrorDialog dialog = new GoogleServiceErrorDialog();
        Bundle bundle = new Bundle();

        bundle.putString("error_text", errorText);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey("error_code")) {
            errorCode = getArguments().getInt("error_code");
        }

        if (getArguments().containsKey("request_code")) {
            requestCode = getArguments().getInt("request_code");
        }

        errorText = getArguments().getString("error_text");
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
