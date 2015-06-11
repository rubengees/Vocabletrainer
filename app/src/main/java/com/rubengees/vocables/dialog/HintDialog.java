package com.rubengees.vocables.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rubengees.vocables.R;

/**
 * Created by Ruben on 12.06.2015.
 */
public class HintDialog extends DialogFragment {

    private static final String KEY_HINT = "hint";

    private String hint;

    public static HintDialog newInstance(String hint) {
        HintDialog dialog = new HintDialog();
        Bundle bundle = new Bundle();

        bundle.putString(KEY_HINT, hint);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            hint = getArguments().getString(KEY_HINT);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());

        return builder.title(getString(R.string.dialog_hint_title)).content(hint).build();
    }
}
