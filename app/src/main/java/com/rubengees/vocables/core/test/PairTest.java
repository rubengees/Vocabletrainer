package com.rubengees.vocables.core.test;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.rubengees.vocables.R;
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
    private PairTestLogic logic;

    private LinearLayout layout;
    private boolean wasEmpty;
    private boolean animating;

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
        super.show();
        MeaningField field = logic.getField();

        ButtonContainerTools.refreshButtons(layout, field, getColor(), getDarkColor(), wasEmpty);
        wasEmpty = false;
    }

    @Override
    public void saveInstanceState(Bundle outState) {
        logic.saveInstanceState(outState);
    }

    @Override
    protected void restoreSavedInstanceState(Bundle savedInstanceState) {
        super.restoreSavedInstanceState(savedInstanceState);

        logic = new PairTestLogic(getContext(), savedInstanceState);
    }

    @Override
    public View getLayout() {
        View view = View.inflate(getContext(), R.layout.layout_test_pair, null);
        layout = (LinearLayout) view.findViewById(R.id.layout_test_pair_button_layout);

        ButtonContainerTools.buildButtonLayout(getContext(), layout, SIZE_X, SIZE_Y, this);

        getToolbarActivity().collapseToolbar();

        return view;
    }


    @Override
    public void onClick(View view) {
        if (!animating) {
            processInput((Button) view);
        }
    }

    private void processInput(Button button) {
        Position pos = (Position) button.getTag();

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
            animating = true;
            final AppCompatButton firstButton = (AppCompatButton) ButtonContainerTools.getButtonAt(layout, first);
            final AppCompatButton secondButton = (AppCompatButton) ButtonContainerTools.getButtonAt(layout, second);
            final AppCompatButton correctButton;

            if (result == null) {
                correctButton = null;

                Utils.setButtonColor(firstButton, Utils.getColor(getContext(), R.color.green));
                Utils.setButtonColor(secondButton, Utils.getColor(getContext(), R.color.green));
            } else {
                correctButton = (AppCompatButton) ButtonContainerTools.getButtonAt(layout, result);

                Utils.setButtonColor(firstButton, Utils.getColor(getContext(), R.color.red));
                Utils.setButtonColor(secondButton, Utils.getColor(getContext(), R.color.green));
                Utils.setButtonColor(correctButton, Utils.getColor(getContext(), R.color.red));
            }

            AnimationUtils.animate(firstButton, Techniques.FadeOut, 300, 500, null);
            if (correctButton == null) {
                AnimationUtils.animate(secondButton, Techniques.FadeOut, 300, 500, new AnimationUtils.AnimationEndListener() {
                    @Override
                    public void onAnimationEnd() {
                        animating = false;
                        next();
                    }
                });
            } else {
                AnimationUtils.animate(correctButton, Techniques.FadeOut, 300, 500, new AnimationUtils.AnimationEndListener() {
                    @Override
                    public void onAnimationEnd() {
                        animating = false;
                        Utils.setButtonColor(secondButton, getColor());
                        next();
                    }
                });
            }
        } else {
            next();
        }
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
