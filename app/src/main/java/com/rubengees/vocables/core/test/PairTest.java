package com.rubengees.vocables.core.test;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.rubengees.vocables.R;
import com.rubengees.vocables.core.mode.PairMode;
import com.rubengees.vocables.core.test.logic.MeaningField;
import com.rubengees.vocables.core.test.logic.PairTestLogic;
import com.rubengees.vocables.core.test.logic.Position;
import com.rubengees.vocables.core.test.logic.TestLogic;
import com.rubengees.vocables.core.testsettings.TestSettings;
import com.rubengees.vocables.utils.AnimationUtils;
import com.rubengees.vocables.utils.Utils;

/**
 * Created by ruben on 08.06.15.
 */
public class PairTest extends Test implements View.OnClickListener {

    private static final int SIZE_X = 2;
    private static final int SIZE_Y = 5;
    private static final String STATE_WAS_WAITING = "wasWaiting";

    private PairTestLogic logic;

    private LinearLayout layout;
    private boolean wasEmpty;
    private boolean waiting;

    public PairTest(Context context, TestSettings settings, OnTestFinishedListener listener, int color, int darkColor, Bundle savedInstanceState) {
        super(context, settings, listener, color, darkColor, savedInstanceState);
    }

    public PairTest(Context context, TestSettings settings, OnTestFinishedListener listener, int color, int darkColor) {
        super(context, settings, listener, color, darkColor);

        logic = new PairTestLogic(context, settings, SIZE_X, SIZE_Y);
    }

    @Override
    protected TestLogic getLogic() {
        return logic;
    }

    @Override
    public void show() {
        if (logic.getAmount() < PairMode.MIN_AMOUNT) {
            showError();
        } else {
            super.show();
            MeaningField field = logic.getField();

            ButtonContainerTools.refreshButtons(layout, field, getColor(), getDarkColor(), wasEmpty);
            changeHintVisibility(logic.getHint() != null);
            wasEmpty = false;
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
    protected void restoreSavedInstanceState(Bundle savedInstanceState) {
        super.restoreSavedInstanceState(savedInstanceState);

        logic = new PairTestLogic(getContext(), savedInstanceState);

        if (savedInstanceState.getBoolean(STATE_WAS_WAITING, false)) {
            next();
        } else {
            show();
        }
    }

    @Override
    public View getSpecificLayout() {
        View view = View.inflate(getContext(), R.layout.layout_test_pair, null);
        layout = (LinearLayout) view.findViewById(R.id.layout_test_pair_button_layout);

        ButtonContainerTools.buildButtonLayout(layout, SIZE_X, SIZE_Y, this);

        getToolbarActivity().collapseToolbar();

        return view;
    }

    @Override
    public void onClick(View view) {
        if (!waiting) {
            processInput((Button) view);
        }
    }

    private void processInput(Button button) {
        Position pos = ButtonContainerTools.findPositionOfButton(layout, button);

        if (logic.hasSelectedButton()) {
            Position selectedPos = logic.getSelected();

            if (pos.equals(selectedPos)) {
                logic.unSelect();
                show();
            } else {
                processAnswer(selectedPos, pos);
            }
        } else {
            logic.select(pos);
            show();
        }
    }

    private void processAnswer(Position first, Position second) {
        Position result = logic.processAnswer(second);

        showResult(first, second, result);
    }

    private void showResult(Position first, Position second, Position result) {
        if (shouldAnimate()) {
            final AppCompatButton firstButton = (AppCompatButton) ButtonContainerTools.getButtonAt(layout, first);
            final AppCompatButton secondButton = (AppCompatButton) ButtonContainerTools.getButtonAt(layout, second);
            final AppCompatButton correctButton;

            if (result == null) {
                correctButton = null;

                Utils.tintButton(firstButton, Utils.getColor(getContext(), R.color.green));
                Utils.tintButton(secondButton, Utils.getColor(getContext(), R.color.green));
            } else {
                correctButton = (AppCompatButton) ButtonContainerTools.getButtonAt(layout, result);

                Utils.tintButton(firstButton, Utils.getColor(getContext(), R.color.red));
                Utils.tintButton(secondButton, Utils.getColor(getContext(), R.color.red));
                Utils.tintButton(correctButton, Utils.getColor(getContext(), R.color.green));
            }

            waiting = true;
            firstButton.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AnimationUtils.fadeOut(firstButton, ANIMATION_TIME, null, null);
                }
            }, WAIT_TIME);
            if (correctButton == null) {
                secondButton.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AnimationUtils.fadeOut(secondButton, ANIMATION_TIME, null, new AnimationUtils.AnimationEndListener() {
                            @Override
                            public void onAnimationEnd() {
                                if (waiting) {
                                    waiting = false;
                                    next();
                                }
                            }
                        });
                    }
                }, WAIT_TIME);
            } else {
                correctButton.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        AnimationUtils.fadeOut(correctButton, ANIMATION_TIME, null, new AnimationUtils.AnimationEndListener() {
                            @Override
                            public void onAnimationEnd() {
                                if (waiting) {
                                    waiting = false;
                                    Utils.tintButton(secondButton, getColor());
                                    next();
                                }
                            }
                        });
                    }
                }, WAIT_TIME);
            }
        } else {
            next();
        }
    }

    private void fadeOut(@NonNull View view, @NonNull AnimationUtils.AnimationEndListener listener) {

    }

    private void next() {
        wasEmpty = logic.getField().isEmpty();

        if (logic.next()) {
            show();
        } else {
            finishTest(logic.getResult(), logic.getVocables());
        }
    }
}
