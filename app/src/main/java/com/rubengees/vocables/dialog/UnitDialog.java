package com.rubengees.vocables.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

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

    private TextInputLayout inputLayout;
    private EditText input;
    private MaterialDialog dialog;

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
        inputLayout = (TextInputLayout) View.inflate(getActivity(), R.layout.input, null);
        input = (EditText) inputLayout.getChildAt(0);

        input.setHint(getString(R.string.dialog_unit_input_hint));
        input.setText(unit.getTitle());

        input.setImeOptions(EditorInfo.IME_ACTION_GO);
        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    processInput();
                    return true;
                }
                return false;
            }
        });

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                inputLayout.setError(null);
            }
        });

        builder.title(getString(R.string.dialog_unit_title)).customView(inputLayout, true).positiveText(getString(R.string.dialog_unit_ok)).negativeText(getString(R.string.dialog_cancel))
                .callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                super.onPositive(dialog);

                processInput();
            }

            @Override
            public void onNegative(MaterialDialog dialog) {
                super.onNegative(dialog);

                dialog.dismiss();
            }
        }).autoDismiss(false);

        dialog = builder.build();

        return dialog;
    }

    private void processInput() {
        if (input.getText().toString().isEmpty()) {
            inputLayout.setError(getString(R.string.input_error_empty));
        } else {
            unit.setTitle(input.getText().toString());
            unit.setLastModificationTime(System.currentTimeMillis());

            if (callback != null) {
                callback.onUnitChanged(unit, unitPos);
            }

            dialog.dismiss();
        }
    }

    public void setCallback(UnitDialogCallback callback) {
        this.callback = callback;
    }

    public interface UnitDialogCallback {
        void onUnitChanged(Unit unit, int pos);
    }
}
