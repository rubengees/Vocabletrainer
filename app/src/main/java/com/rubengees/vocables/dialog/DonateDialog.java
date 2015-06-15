package com.rubengees.vocables.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rubengees.vocables.R;
import com.rubengees.vocables.core.Core;

/**
 * Created by ruben on 10.06.15.
 */
public class DonateDialog extends DialogFragment {

    public static DonateDialog newInstance() {
        return new DonateDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        builder.title(getString(R.string.dialog_donate_title)).content(getString(R.string.dialog_donate_content)).items(R.array.donation_titles).itemsCallback(new MaterialDialog.ListCallback() {
            @Override
            public void onSelection(MaterialDialog materialDialog, View view, int i, CharSequence charSequence) {
                Core core = Core.getInstance(getActivity());

                    switch (i) {
                        case 0:
                            core.donate(getString(R.string.donate_50_id));
                            break;
                        case 1:
                            core.donate(getString(R.string.donate_100_id));
                            break;
                        case 2:
                            core.donate(getString(R.string.donate_200_id));
                            break;
                    }
            }
        });

        return builder.build();
    }
}
