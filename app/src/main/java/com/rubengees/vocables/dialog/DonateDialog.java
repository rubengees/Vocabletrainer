package com.rubengees.vocables.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rubengees.vocables.R;

/**
 * Created by ruben on 10.06.15.
 */
public class DonateDialog extends DialogFragment {

    private DonateDialogCallback callback;

    public static DonateDialog newInstance() {
        return new DonateDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        builder.title(getString(R.string.dialog_donate_title)).content(getString(R.string.dialog_donate_content)).items(R.array.donation_titles).itemsCallback(new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                if (callback != null) {
                    switch (i) {
                        case 0:
                            callback.onDonate(getString(R.string.donate_50));
                            break;
                        case 1:
                            callback.onDonate(getString(R.string.donate_100));
                            break;
                        case 2:
                            callback.onDonate(getString(R.string.donate_200));
                            break;
                    }
                }
            }
        });

        return builder.build();
    }

    public void setCallback(DonateDialogCallback callback) {
        this.callback = callback;
    }

    public interface DonateDialogCallback {
        void onDonate(String item);
    }
}
