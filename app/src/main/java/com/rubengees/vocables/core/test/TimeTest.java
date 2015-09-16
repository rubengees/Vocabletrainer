package com.rubengees.vocables.core.test;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rubengees.vocables.R;
import com.rubengees.vocables.core.mode.TimeMode;
import com.rubengees.vocables.core.test.logic.Position;
import com.rubengees.vocables.core.test.logic.TestLogic;
import com.rubengees.vocables.core.test.logic.TimeTestLogic;
import com.rubengees.vocables.core.testsettings.TestSettings;
import com.rubengees.vocables.core.testsettings.TimeTestSettings;
import com.rubengees.vocables.utils.Utils;

/**
 * Created by Ruben Gees on 13.02.2015.
 */
public class TimeTest extends Test implements View.OnClickListener, TimeTestLogic.OnTimeListener {

    private static final int SIZE_X = 2;
    private static final int SIZE_Y = 5;
    private static final String STATE_WAS_WAITING = "wasWaiting";

    private TimeTestLogic logic;

    private LinearLayout layout;
    private TextView question;
    private ProgressBar progress;

    private boolean waiting = false;

    public TimeTest(Context context, TestSettings settings, OnTestFinishedListener listener, int color, int darkColor, Bundle savedInstanceState) {
        super(context, settings, listener, color, darkColor, savedInstanceState);
    }

    public TimeTest(Context context, TestSettings settings, OnTestFinishedListener listener, int color, int darkColor) {
        super(context, settings, listener, color, darkColor);

        logic = new TimeTestLogic(context, (TimeTestSettings) settings, SIZE_X, SIZE_Y, this);
    }

    @Override
    public TestLogic getLogic() {
        return logic;
    }

    @Override
    public void show() {
        if (logic.getAmount() < TimeMode.MIN_AMOUNT) {
            showError();
        } else {
            super.show();

            ButtonContainerTools.refreshButtons(layout, logic.getField(), getColor(), getDarkColor(), false);
            question.setText(getContext().getString(R.string.test_question) + " " + "'" + logic.getQuestion() + "'?");
        }
    }

    @Override
    public View getSpecificLayout() {
        View view = View.inflate(getContext(), R.layout.layout_test_time, null);
        layout = (LinearLayout) view.findViewById(R.id.layout_test_time_button_layout);
        progress = (ProgressBar) view.findViewById(R.id.layout_test_time_progress);

        View header = View.inflate(getContext(), R.layout.header, null);
        question = (TextView) header.findViewById(R.id.header_text);

        ButtonContainerTools.buildButtonLayout(layout, SIZE_X, SIZE_Y, this);

        getToolbarActivity().expandToolbar();
        getToolbarActivity().disableFab();
        getToolbarActivity().setToolbarView(header);

        return view;
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
    protected void restoreSavedInstanceState(Bundle savedInstanceState) {
        super.restoreSavedInstanceState(savedInstanceState);

        logic = new TimeTestLogic(getContext(), savedInstanceState, this);

        if (savedInstanceState.getBoolean(STATE_WAS_WAITING, false)) {
            next();
        } else {
            show();
        }
    }

    @Override
    public void onClick(View v) {
        if (!waiting) {
            processInput((Button) v);
        }
    }

    private void processInput(Button button) {
        Position pos = ButtonContainerTools.findPositionOfButton(layout, button);

        processAnswer(pos);
    }

    private void processAnswer(Position pos) {
        Position result = logic.processAnswer(pos);

        showResult(pos, result);
    }

    private void showResult(Position answerPos, Position resultPos) {
        if (shouldAnimate()) {
            AppCompatButton answerButton = (AppCompatButton) ButtonContainerTools.getButtonAt(layout, answerPos);

            if (resultPos == null) {
                Utils.tintButton(answerButton, Utils.getColor(getContext(), R.color.green));
            } else {
                AppCompatButton resultButton = (AppCompatButton) ButtonContainerTools.getButtonAt(layout, resultPos);

                Utils.tintButton(answerButton, Utils.getColor(getContext(), R.color.red));
                Utils.tintButton(resultButton, Utils.getColor(getContext(), R.color.green));
            }

            waiting = true;
            logic.onPause();
            Utils.wait((Activity) getContext(), WAIT_TIME, new Utils.OnWaitFinishedListener() {
                @Override
                public void onWaitFinished() {
                    if (waiting) {
                        waiting = false;
                        logic.onResume();
                        next();
                    }
                }
            });
        } else {
            next();
        }
    }

    private void next() {
        if (logic.getAmount() < TimeMode.MIN_AMOUNT) {
            showError();
        } else {
            logic.next();
            show();
            changeHintVisibility(logic.getHint() != null);
        }
    }

    @Override
    public void onTimeOver() {
        finishTest(logic.getResult(), logic.getVocables());
    }

    @Override
    public void onTimeUpdate(long max, long remaining) {
        progress.setMax((int) max);
        progress.setProgress((int) remaining);
    }
}
