package com.rubengees.vocables.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.gms.common.GooglePlayServicesUtil;

/**
 * Created by ruben on 05.05.15.
 */
public class GoogleServiceErrorDialog extends DialogFragment {

    private int errorCode;
    private int requestCode;

    private GoogleServiceErrorDialogCallback callback;

    public static GoogleServiceErrorDialog newInstance(int errorCode, int requestCode) {
        GoogleServiceErrorDialog dialog = new GoogleServiceErrorDialog();
        Bundle bundle = new Bundle();

        bundle.putInt("error_code", errorCode);
        bundle.putInt("request_code", requestCode);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        errorCode = getArguments().getInt("error_code");
        requestCode = getArguments().getInt("request_code");
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog result = GooglePlayServicesUtil.getErrorDialog(errorCode, getActivity(), requestCode);
        result.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (callback != null) {
                    callback.onDismiss();
                }
            }
        });

        return result;
    }

    public void setCallback(GoogleServiceErrorDialogCallback callback) {
        this.callback = callback;
    }

    public interface GoogleServiceErrorDialogCallback {
        void onDismiss();
    }
}
