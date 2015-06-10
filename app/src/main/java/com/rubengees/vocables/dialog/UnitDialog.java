package com.rubengees.vocables.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rubengees.vocables.R;
import com.rubengees.vocables.core.Core;
import com.rubengees.vocables.pojo.Unit;

/**
 * Created by ruben on 05.05.15.
 */
public class UnitDialog extends DialogFragment {

    private static final String KEY_UNIT_ID = "unit_id";
    private static final String KEY_UNIT_POS = "unit_pos";

    private Unit unit;
    private Integer unitPos;
    private UnitDialogCallback callback;

    public static UnitDialog newInstance(int unitId, Integer pos) {
        UnitDialog dialog = new UnitDialog();

        Bundle bundle = new Bundle();
        bundle.putInt(KEY_UNIT_ID, unitId);
        if (pos != null) {
            bundle.putInt(KEY_UNIT_POS, pos);
        }
        dialog.setArguments(bundle);

        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        unit = Core.getInstance(getActivity()).getVocableManager().getUnit(getArguments().getInt(KEY_UNIT_ID));
        if (getArguments().containsKey(KEY_UNIT_POS)) {
            unitPos = getArguments().getInt(KEY_UNIT_POS);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());

        builder.title(getString(R.string.dialog_unit_title))
                .input(getString(R.string.dialog_unit_input_hint), unit.getTitle(), false, new MaterialDialog.InputCallback() {
            @Override
            public void onInput(MaterialDialog materialDialog, CharSequence charSequence) {

                unit.setTitle(charSequence.toString());
                unit.setLastModificationTime(System.currentTimeMillis());

                if (callback != null) {
                    callback.onUnitChanged(unit, unitPos);
                }

            }
                }).negativeText(getString(R.string.dialog_cancel));

        return builder.build();
    }

    public void setCallback(UnitDialogCallback callback) {
        this.callback = callback;
    }

    public interface UnitDialogCallback {
        void onUnitChanged(Unit unit, int pos);
    }
}
