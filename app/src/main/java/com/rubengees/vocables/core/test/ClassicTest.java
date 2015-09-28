package com.rubengees.vocables.core.test;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.rubengees.vocables.R;
import com.rubengees.vocables.activity.ExtendedToolbarActivity;
import com.rubengees.vocables.core.mode.ClassicMode;
import com.rubengees.vocables.core.test.logic.ClassicTestLogic;
import com.rubengees.vocables.core.test.logic.TestLogic;
import com.rubengees.vocables.core.testsettings.ClassicTestSettings;
import com.rubengees.vocables.core.testsettings.TestSettings;
import com.rubengees.vocables.pojo.MeaningList;
import com.rubengees.vocables.utils.AnimationUtils;
import com.rubengees.vocables.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ruben on 01.06.2015.
 */
public class ClassicTest extends Test implements ExtendedToolbarActivity.OnFabClickListener {

    private static final String STATE_WAS_WAITING = "wasWaiting";

    private ClassicTestLogic logic;

    private ViewGroup inputContainer;
    private TextView status;

    private boolean waiting = false;

    public ClassicTest(Context context, TestSettings settings, OnTestFinishedListener listener, int color, int darkColor, Bundle savedInstanceState) {
        super(context, settings, listener, color, darkColor, savedInstanceState);
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
    public View getSpecificLayout() {
        View root = View.inflate(getContext(), R.layout.layout_test_classic, null);
        View header = View.inflate(getContext(), R.layout.header, null);

        inputContainer = (ViewGroup) root.findViewById(R.id.layout_test_classic_input_container);
        status = (TextView) header.findViewById(R.id.header_text);

        getToolbarActivity().expandToolbar();
        getToolbarActivity().setToolbarView(header);
        getToolbarActivity().enableFab(R.drawable.ic_next, this);

        return root;
    }

    private void processInput() {
        if (!waiting) {
            MeaningList result = null;

            if (inputContainer.getChildCount() == 1) {
                String text = ((EditText) ((ViewGroup) inputContainer.getChildAt(0)).getChildAt(0))
                        .getText().toString().trim();

                if (text.length() <= 0) {
                    text = null;
                }

                result = logic.processAnswer(text);
            } else {
                List<String> meanings = new ArrayList<>();
                for (int i = 0; i < inputContainer.getChildCount(); i++) {
                    EditText input = (EditText) ((ViewGroup) inputContainer.getChildAt(i))
                            .getChildAt(0);

                    String text = input.getText().toString().trim();

                    meanings.add(text);
                }

                result = logic.processAnswer(new MeaningList(meanings));
            }

            showResult(result);
        }
    }

    private void showResult(MeaningList result) {
        final Spannable text;

        if (result == null) {
            text = new SpannableString(getContext().getString(R.string.test_classic_correct));
        } else {
            String resultText = getContext().getString(R.string.test_classic_wrong) + " ";
            text = new SpannableString(resultText + result.toString());

            text.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), resultText.length() - 1, text.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }

        status.setText(text);

        if (shouldAnimate()) {
            waiting = true;

            AnimationUtils.fadeIn(status, ANIMATION_TIME, null, new AnimationUtils.AnimationEndListener() {
                @Override
                public void onAnimationEnd() {
                    Utils.wait(getToolbarActivity(), WAIT_TIME, new Utils.OnWaitFinishedListener() {
                        @Override
                        public void onWaitFinished() {
                            if (waiting) {
                                waiting = false;

                                next();
                            }
                        }
                    });
                }
            });
        } else {
            Utils.wait(getToolbarActivity(), WAIT_TIME, new Utils.OnWaitFinishedListener() {
                @Override
                public void onWaitFinished() {
                    next();
                }
            });
        }

    }

    @Override
    protected void restoreSavedInstanceState(Bundle savedInstanceState) {
        super.restoreSavedInstanceState(savedInstanceState);

        logic = new ClassicTestLogic(getContext(), savedInstanceState);

        if (savedInstanceState.getBoolean(STATE_WAS_WAITING, false)) {
            next();
        }
    }

    @Override
    public void saveInstanceState(Bundle outState) {
        super.saveInstanceState(outState);

        logic.saveInstanceState(outState);

        if (waiting) {
            waiting = false;
            outState.putBoolean(STATE_WAS_WAITING, true);
        }
    }

    @Override
    public void show() {
        if (logic.getAmount() < ClassicMode.MIN_AMOUNT) {
            showError();
        } else {
            super.show();

            status.setText(getContext().getString(R.string.test_question) + " '" + logic.getQuestion().toString() + "'?");

            insertInputViews();

            if (shouldAnimate()) {
                waiting = true;

                AnimationUtils.fadeIn(status, ANIMATION_TIME, null, new AnimationUtils.AnimationEndListener() {
                    @Override
                    public void onAnimationEnd() {
                        waiting = false;
                    }
                });
            }
        }
    }

    private void insertInputViews() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        int amount = ((ClassicTestSettings) getSettings()).isAllMeanings() ?
                logic.getAnswer().size() : 1;

        inputContainer.removeAllViews();
        for (int i = 0; i < amount; i++) {
            TextInputLayout inputLayout = (TextInputLayout) inflater.inflate(R.layout.input,
                    inputContainer, false);
            EditText input = (EditText) inputLayout.getChildAt(0);

            input.setHint(R.string.fragment_test_classic_input_hint);
            input.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

            if (i == amount - 1) {
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
            } else {
                input.setImeOptions(EditorInfo.IME_ACTION_NEXT);
            }

            inputContainer.addView(inputLayout);
            inputContainer.getChildAt(0).requestFocus();
        }
    }


    @Override
    public void onFabClick() {
        processInput();
    }

    private void next() {
        if (logic.next()) {
            show();
            changeHintVisibility(logic.getHint() != null);
        } else {
            getToolbarActivity().hideKeyboard(inputContainer.getFocusedChild());
            finishTest(logic.getResult(), logic.getVocables());
        }
    }
}
