package com.rubengees.vocables.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rubengees.vocables.core.Core;
import com.rubengees.vocables.data.VocableManager;
import com.rubengees.vocables.pojo.Unit;
import com.rubengees.vocables.pojo.Vocable;

/**
 * Created by Ruben on 04.05.2015.
 */
public class VocableDialog extends DialogFragment {

    private Vocable vocable;
    private Unit unit;
    private VocableDialogCallback callback;

    private VocableManager manager;

    public VocableDialog newInstance(int unitId, Vocable vocable) {
        VocableDialog dialog = new VocableDialog();
        Bundle bundle = new Bundle();

        bundle.putInt("unitId", unitId);
        bundle.putParcelable("vocable", vocable);
        dialog.setArguments(bundle);

        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        manager = Core.getInstance(getActivity()).getVocableManager();

        if (getArguments() != null) {
            vocable = getArguments().getParcelable("vocable");
            unit = manager.getU
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());

        builder.title("Modify Vocable")
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void setCallback(VocableDialogCallback callback) {
        this.callback = callback;
    }

    public interface VocableDialogCallback {
        void onVocableAdded(Unit unit, Vocable vocable);

        void onVocableChanged(Unit newUnit, Unit oldUnit, Vocable vocable);
    }
}
