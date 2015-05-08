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
    private Integer unitPos;
    private UnitDialogCallback callback;

    public static UnitDialog newInstance(int unitId, Integer pos) {
        UnitDialog dialog = new UnitDialog();

        Bundle bundle = new Bundle();
        bundle.putInt("unit_id", unitId);
        if (pos != null) {
            bundle.putInt("unit_pos", pos);
        }
        dialog.setArguments(bundle);

        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        unit = Core.getInstance(getActivity()).getVocableManager().getUnit(getArguments().getInt("unit_id"));
        if (getArguments().containsKey("unit_pos")) {
            unitPos = getArguments().getInt("unit_pos");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());

        builder.title("Modify Unit").input("Unit title", unit.getTitle(), false, new MaterialDialog.InputCallback() {
            @Override
            public void onInput(MaterialDialog materialDialog, CharSequence charSequence) {

                unit.setTitle(charSequence.toString());
                unit.setLastModificationTime(System.currentTimeMillis());

                if (callback != null) {
                    callback.onUnitChanged(unit, unitPos);
                }

            }
        }).negativeText("Cancel");

        return builder.build();
    }

    public void setCallback(UnitDialogCallback callback) {
        this.callback = callback;
    }

    public interface UnitDialogCallback {
        void onUnitChanged(Unit unit, int pos);
    }
}
