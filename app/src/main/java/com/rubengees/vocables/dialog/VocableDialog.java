package com.rubengees.vocables.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rubengees.vocables.R;
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

    private LinearLayout meaningContainer1;
    private LinearLayout meaningContainer2;
    private ImageButton addMeaning1;
    private ImageButton addMeaning2;
    private EditText hint;
    private Spinner units;
    private EditText inputUnit;
    private ImageButton toggleUnit;

    public VocableDialog newInstance(int unitId, Vocable vocable) {
        VocableDialog dialog = new VocableDialog();
        Bundle bundle = new Bundle();

        bundle.putInt("unit_id", unitId);
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
            unit = manager.getUnit(getArguments().getInt("unit_id"));
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());

        builder.title("Modify Vocable");

        if (vocable != null) {
            processVocable();
        }

        return builder.build();
    }

    private void processVocable() {
        for (String s : vocable.getFirstMeaning()) {
            meaningContainer1.addView(generateInput(s, "Meaning in your language"));
        }

        for (String s : vocable.getSecondMeaning()) {
            meaningContainer2.addView(generateInput(s, "Meaning in foreign language"));
        }
    }

    private View inflateView() {
        View content = View.inflate(getActivity(), R.layout.dialog_vocable, null);

        meaningContainer1 = (LinearLayout) content.findViewById(R.id.dialog_vocable_meanings1_container);
        meaningContainer2 = (LinearLayout) content.findViewById(R.id.dialog_vocable_meanings2_container);
        addMeaning1 = (ImageButton) content.findViewById(R.id.dialog_vocable_meanings1_add);
        addMeaning2 = (ImageButton) content.findViewById(R.id.dialog_vocable_meanings2_add);
        hint = (EditText) content.findViewById(R.id.dialog_vocable_hint);
        units = (Spinner) content.findViewById(R.id.dialog_vocable_units);
        inputUnit = (EditText) content.findViewById(R.id.dialog_vocable_unit_input);
        toggleUnit = (ImageButton) content.findViewById(R.id.dialog_vocable_toggle_unit_input);

        return content;
    }

    private EditText generateInput(String text, String hint) {
        EditText result = (EditText) View.inflate(getActivity(), R.layout.dialog_vocable_input, null);

        result.setText(text);
        result.setHint(hint);
        result.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        return result;
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
