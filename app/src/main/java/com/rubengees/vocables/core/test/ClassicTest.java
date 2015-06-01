package com.rubengees.vocables.core.test;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.rubengees.vocables.R;
import com.rubengees.vocables.activity.ExtendedToolbarActivity;
import com.rubengees.vocables.core.test.logic.ClassicTestLogic;
import com.rubengees.vocables.core.test.logic.TestLogic;
import com.rubengees.vocables.core.testsettings.ClassicTestSettings;
import com.rubengees.vocables.core.testsettings.TestSettings;
import com.rubengees.vocables.pojo.Meaning;

/**
 * Created by Ruben on 01.06.2015.
 */
public class ClassicTest extends Test implements ExtendedToolbarActivity.OnFabClickListener {

    private ClassicTestLogic logic;

    private EditText input;
    private TextView status;

    public ClassicTest(Context context, TestSettings settings, OnTestFinishedListener listener, int color, int darkColor, Bundle savedInstanceState) {
        super(context, settings, listener, color, darkColor, savedInstanceState);

        logic.next();
    }

    public ClassicTest(Context context, TestSettings settings, OnTestFinishedListener listener, int color, int darkColor) {
        super(context, settings, listener, color, darkColor);

        logic = new ClassicTestLogic(context, (ClassicTestSettings) settings);
    }

    @Override
    protected TestLogic getLogic() {
        return logic;
    }

    @Override
    public View getLayout() {
        View root = View.inflate(getContext(), R.layout.fragment_test_classic, null);
        View header = View.inflate(getContext(), R.layout.fragment_test_header, null);

        input = (EditText) root.findViewById(R.id.fragment_test_classic_input);
        status = (TextView) header.findViewById(R.id.fragment_test_header_text);

        input.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
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

        getToolbarActivity().expandToolbar();
        getToolbarActivity().setToolbarView(header);
        getToolbarActivity().enableFab(R.drawable.ic_next, this);

        return root;
    }

    private void processInput() {
        String text = input.getText().toString().trim();

        if (text.length() <= 0) {
            text = null;
        }

        Meaning result = logic.processAnswer(text);

        if (shouldAnimate()) {
            //showResult(result);
        } else {
            next();
        }
    }

    @Override
    protected void restoreSavedInstanceState(Bundle savedInstanceState) {
        super.restoreSavedInstanceState(savedInstanceState);

        logic = new ClassicTestLogic(getContext(), savedInstanceState);
    }

    @Override
    public void show() {
        super.show();
        status.setText("What means" + " '" + logic.getQuestion().toString() + "'?");
        input.getText().clear();
    }

    @Override
    public void onFabClick() {
        processInput();
    }

    private void next() {
        if (logic.next()) {
            show();
        } else {
            finishTest(logic.getResult(), logic.getVocables());
        }
    }
}
