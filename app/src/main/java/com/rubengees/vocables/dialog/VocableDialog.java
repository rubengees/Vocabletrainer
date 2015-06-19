package com.rubengees.vocables.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rubengees.vocables.R;
import com.rubengees.vocables.core.Core;
import com.rubengees.vocables.data.VocableManager;
import com.rubengees.vocables.pojo.Meaning;
import com.rubengees.vocables.pojo.Unit;
import com.rubengees.vocables.pojo.Vocable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Ruben on 04.05.2015.
 */
public class VocableDialog extends DialogFragment {

    public static final String KEY_UNIT_ID = "unit_id";
    public static final String KEY_VOCABLE = "vocable";
    public static final String KEY_VOCABLE_POS = "vocable_pos";
    public static final String STATE_FIRST_MEANINGS = "first_meanings";
    private static final String STATE_SECOND_MEANINGS = "second_meanings";
    private Vocable vocable;
    private Unit unit;
    private int vocablePos;
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
    private ArrayAdapter<Unit> adapter;

    public static VocableDialog newInstance(Integer unitId, Vocable vocable, Integer vocablePos) {
        VocableDialog dialog = new VocableDialog();
        Bundle bundle = new Bundle();

        if (unitId != null) {
            bundle.putInt(KEY_UNIT_ID, unitId);
        }
        bundle.putParcelable(KEY_VOCABLE, vocable);
        if (vocablePos != null) {
            bundle.putInt(KEY_VOCABLE_POS, vocablePos);
        }
        dialog.setArguments(bundle);

        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        manager = Core.getInstance(getActivity()).getVocableManager();

        if (getArguments() != null) {
            vocable = getArguments().getParcelable(KEY_VOCABLE);

            if (getArguments().containsKey(KEY_UNIT_ID)) {
                unit = manager.getUnit(getArguments().getInt(KEY_UNIT_ID));
            }
            if (getArguments().containsKey(KEY_VOCABLE_POS)) {
                vocablePos = getArguments().getInt(KEY_VOCABLE_POS);
            }
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());

        builder.title(getString(R.string.dialog_vocable_title)).customView(inflateView(), true)
                .positiveText(getString(R.string.dialog_vocable_ok))
                .negativeText(getString(R.string.dialog_cancel)).callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                super.onPositive(dialog);

                if (processInput()) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onNegative(MaterialDialog dialog) {
                super.onNegative(dialog);

                dialog.dismiss();
            }
        }).autoDismiss(false);

        if (savedInstanceState == null) {
            processVocable();
        } else {
            processMeanings(savedInstanceState.getStringArrayList(STATE_FIRST_MEANINGS), savedInstanceState.getStringArrayList(STATE_SECOND_MEANINGS));
        }

        processUnit();
        setupButtons();

        return builder.build();
    }

    private boolean processInput() {
        List<String> firstMeanings;
        List<String> secondMeanings;
        Meaning firstMeaning;
        Meaning secondMeaning;
        String hint = null;
        String unitTitle;
        Unit unit = null;

        firstMeanings = getMeanings(meaningContainer1);
        secondMeanings = getMeanings(meaningContainer2);

        boolean error = false;

        if (firstMeanings.isEmpty()) {
            TextInputLayout input = (TextInputLayout) meaningContainer1.getChildAt(0);

            input.setError(getString(R.string.input_error_empty));
            input.setErrorEnabled(true);
            error = true;
        }

        if (secondMeanings.isEmpty()) {
            TextInputLayout input = (TextInputLayout) meaningContainer2.getChildAt(0);

            input.setError(getString(R.string.input_error_empty));
            input.setErrorEnabled(true);
            error = true;
        }

        if (((View) inputUnit.getParent()).getVisibility() == View.VISIBLE) {
            unitTitle = inputUnit.getText().toString().trim();

            if (!unitTitle.isEmpty()) {
                unit = new Unit();
                unit.setTitle(unitTitle);
                unit.setLastModificationTime(System.currentTimeMillis());
            } else {
                TextInputLayout inputLayout = (TextInputLayout) inputUnit.getParent();
                inputLayout.setError(getString(R.string.input_error_empty));
                inputLayout.setErrorEnabled(true);

                error = true;
            }
        } else {
            unit = adapter.getItem(units.getSelectedItemPosition());
        }

        if (error) {
            return false;
        }

        firstMeaning = new Meaning(firstMeanings);
        secondMeaning = new Meaning(secondMeanings);

        String hintText = this.hint.getText().toString().trim();

        if (!hintText.isEmpty()) {
            hint = hintText;
        }

        if (vocable == null) {
            Vocable current = new Vocable(firstMeaning, secondMeaning, hint, System.currentTimeMillis());

            if (callback != null) {
                callback.onVocableAdded(unit, current);
            }

            cleanUp(unit);

            return false;
        } else {
            vocable.setFirstMeaning(firstMeaning);
            vocable.setSecondMeaning(secondMeaning);
            vocable.setLastModificationTime(System.currentTimeMillis());
            vocable.setHint(hint);

            if (callback != null) {
                callback.onVocableChanged(unit, this.unit, vocable, vocablePos);
            }

            return true;
        }
    }

    private ArrayList<String> getMeanings(ViewGroup layout) {
        ArrayList<String> result = new ArrayList<>();

        for (int i = 0; i < layout.getChildCount(); i++) {
            EditText current = (EditText) ((ViewGroup) layout.getChildAt(i)).getChildAt(0);
            String currentText = current.getText().toString().trim();

            if (!currentText.isEmpty()) {
                result.add(currentText);
            }
        }

        return result;
    }

    private void cleanUp(Unit unit) {
        for (int i = meaningContainer1.getChildCount() - 1; i > 0; i--) {
            meaningContainer1.removeViewAt(i);
        }

        for (int i = meaningContainer2.getChildCount() - 1; i > 0; i--) {
            meaningContainer2.removeViewAt(i);
        }

        EditText meaningInput1 = (EditText) ((ViewGroup) meaningContainer1.getChildAt(0)).getChildAt(0);
        EditText meaningInput2 = (EditText) ((ViewGroup) meaningContainer2.getChildAt(0)).getChildAt(0);

        meaningInput1.getText().clear();
        meaningInput2.getText().clear();
        hint.getText().clear();

        int index = -1;

        for (int i = 0; i < adapter.getCount(); i++) {
            if (unit.getTitle().equals(adapter.getItem(i).getTitle())) {
                index = i;

                break;
            }
        }

        if (index > -1) {
            units.setSelection(index);
        } else {
            boolean found = false;
            for (int i = 0; i < adapter.getCount(); i++) {
                Unit current = adapter.getItem(i);

                if (unit.compareTo(current) > 0) {
                    adapter.insert(unit, i);
                    units.setSelection(i);
                    found = true;

                    break;
                }
            }

            if (!found) {
                adapter.add(unit);
                units.setSelection(adapter.getCount() - 1);
            }
        }

        toggleUnit.setVisibility(View.VISIBLE);
        setShowUnitInput(false);

        meaningInput1.requestFocus();
    }

    private void setupButtons() {
        addMeaning1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meaningContainer1.addView(generateInput(null, getString(R.string.dialog_vocable_input_hint_first_meaning)));
            }
        });

        addMeaning2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                meaningContainer2.addView(generateInput(null, getString(R.string.dialog_vocable_input_hint_second_meaning)));
            }
        });

        toggleUnit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (units.getVisibility() == View.VISIBLE) {
                    setShowUnitInput(true);
                } else {
                    setShowUnitInput(false);
                }
            }
        });
    }

    private void setShowUnitInput(boolean show) {
        if (show) {
            units.setVisibility(View.GONE);
            ((View) inputUnit.getParent()).setVisibility(View.VISIBLE);
            toggleUnit.setImageResource(R.drawable.btn_hide);
        } else {
            units.setVisibility(View.VISIBLE);
            ((View) inputUnit.getParent()).setVisibility(View.GONE);
            toggleUnit.setImageResource(R.drawable.btn_expand);
        }
    }

    private void processUnit() {
        List<Unit> unitList = manager.getUnitList();

        Collections.sort(unitList);
        adapter = new ArrayAdapter<>(getActivity(), R.layout.support_simple_spinner_dropdown_item, unitList);

        units.setAdapter(adapter);

        if (unit == null) {
            if (adapter.isEmpty()) {
                toggleUnit.setVisibility(View.GONE);
            } else {
                units.setSelection(0);
            }

            setShowUnitInput(true);
        } else {
            units.setSelection(adapter.getPosition(unit));
            setShowUnitInput(false);
        }
    }

    private void processVocable() {
        if (vocable != null) {
            processMeanings(vocable.getFirstMeaning().toList(), vocable.getSecondMeaning().toList());

            this.hint.setText(vocable.getHint());
        } else {
            meaningContainer1.addView(generateInput(null, getString(R.string.dialog_vocable_input_hint_first_meaning)));
            meaningContainer2.addView(generateInput(null, getString(R.string.dialog_vocable_input_hint_second_meaning)));
        }
    }

    private void processMeanings(List<String> firstMeanings, List<String> secondMeanings) {
        if (firstMeanings.isEmpty()) {
            meaningContainer1.addView(generateInput(null, getString(R.string.dialog_vocable_input_hint_first_meaning)));
        } else {
            for (String firstMeaning : firstMeanings) {
                meaningContainer1.addView(generateInput(firstMeaning, getString(R.string.dialog_vocable_input_hint_first_meaning)));
            }
        }

        if (secondMeanings.isEmpty()) {
            meaningContainer2.addView(generateInput(null, getString(R.string.dialog_vocable_input_hint_second_meaning)));
        } else {
            for (String secondMeaning : secondMeanings) {
                meaningContainer2.addView(generateInput(secondMeaning, getString(R.string.dialog_vocable_input_hint_second_meaning)));
            }
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

        hint.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        inputUnit.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        inputUnit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                TextInputLayout inputLayout = (TextInputLayout) inputUnit.getParent();
                inputLayout.setErrorEnabled(false);
            }
        });

        return content;
    }

    private View generateInput(String text, String hint) {
        final TextInputLayout result = (TextInputLayout) View.inflate(getActivity(), R.layout.input, null);
        EditText input = (EditText) result.getChildAt(0);

        result.setHint(hint);
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                result.setErrorEnabled(false);
            }
        });
        input.setText(text);
        input.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        return result;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(STATE_FIRST_MEANINGS, getMeanings(meaningContainer1));
        outState.putStringArrayList(STATE_SECOND_MEANINGS, getMeanings(meaningContainer2));
    }

    public void setCallback(VocableDialogCallback callback) {
        this.callback = callback;
    }

    public interface VocableDialogCallback {
        void onVocableAdded(Unit unit, Vocable vocable);

        void onVocableChanged(Unit newUnit, Unit oldUnit, Vocable vocable, int pos);
    }
}
