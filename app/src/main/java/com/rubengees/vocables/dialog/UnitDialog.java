package com.rubengees.vocables.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rubengees.vocables.core.Core;
import com.rubengees.vocables.pojo.Unit;

/**
 * Created by ruben on 05.05.15.
 */
public class UnitDialog extends DialogFragment {

    private Unit unit;
    private UnitDialogCallback callback;

    public static UnitDialog newInstance(int unitId) {
        UnitDialog dialog = new UnitDialog();

        Bundle bundle = new Bundle();
        bundle.putInt("unit_id", unitId);
        dialog.setArguments(bundle);

        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        unit = Core.getInstance(getActivity()).getVocableManager().getUnit(getArguments().getInt("unit_id"));
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());

        builder.input("Unit title", unit.getTitle(), false, new MaterialDialog.InputCallback() {
            @Override
            public void onInput(MaterialDialog materialDialog, CharSequence charSequence) {

                unit.setTitle((String) charSequence);
                unit.setLastModificationTime(System.currentTimeMillis());

                if (callback != null) {
                    callback.onUnitChanged(unit);
                }

            }
        });

        return builder.build();
    }

    public void setCallback(UnitDialogCallback callback) {
        this.callback = callback;
    }

    public interface UnitDialogCallback {
        void onUnitChanged(Unit unit);
    }
}
