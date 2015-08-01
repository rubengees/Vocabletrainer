package com.rubengees.vocables.core.test;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.easyandroidanimations.library.Animation;
import com.easyandroidanimations.library.AnimationListener;
import com.easyandroidanimations.library.FadeInAnimation;
import com.rubengees.vocables.R;
import com.rubengees.vocables.core.mode.ClassicMode;
import com.rubengees.vocables.core.test.logic.TestLogic;
import com.rubengees.vocables.core.test.logic.TrainingTestLogic;
import com.rubengees.vocables.core.testsettings.TestSettings;
import com.rubengees.vocables.core.testsettings.TrainingTestSettings;
import com.rubengees.vocables.utils.Utils;

/**
 * Created by Ruben on 20.07.2015.
 */
public class TrainingTest extends Test {

    private TrainingTestLogic logic;

    private TextView status;
    private Button show;
    private LinearLayout answerContainer;
    private boolean waiting = false;

    public TrainingTest(Context context, TestSettings settings, OnTestFinishedListener testFinishedListener, int color, int darkColor, Bundle savedInstanceState) {
        super(context, settings, testFinishedListener, color, darkColor, savedInstanceState);
    }

    public TrainingTest(Context context, TestSettings settings, OnTestFinishedListener testFinishedListener, int color, int darkColor) {
        super(context, settings, testFinishedListener, color, darkColor);

        logic = new TrainingTestLogic(context, (TrainingTestSettings) settings);
    }

    @Override
    protected TestLogic getLogic() {
        return logic;
    }

    @Override
    public View getSpecificLayout() {
        View root = View.inflate(getContext(), R.layout.layout_test_training, null);
        View header = View.inflate(getContext(), R.layout.header, null);

        status = (TextView) header.findViewById(R.id.header_text);
        answerContainer = (LinearLayout) root.findViewById(R.id.layout_test_training_answer_container);
        show = (Button) root.findViewById(R.id.layout_test_training_show);
        Button ok = (Button) answerContainer.findViewById(R.id.layout_test_training_ok);
        Button notOk = (Button) answerContainer.findViewById(R.id.layout_test_training_not_ok);

        Utils.tintButton((AppCompatButton) show, Utils.getColor(getContext(), R.color.accent));
        Utils.tintButton((AppCompatButton) ok, Utils.getColor(getContext(), R.color.green));
        Utils.tintButton((AppCompatButton) notOk, Utils.getColor(getContext(), R.color.red));

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnswer();
            }
        });
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processAnswer(true);
            }
        });
        notOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processAnswer(false);
            }
        });

        getToolbarActivity().expandToolbar();
        getToolbarActivity().setToolbarView(header);
        getToolbarActivity().disableFab();

        if (logic != null) {
            handleVisibility();
        }

        return root;
    }

    @Override
    protected void restoreSavedInstanceState(Bundle savedInstanceState) {
        super.restoreSavedInstanceState(savedInstanceState);

        logic = new TrainingTestLogic(getContext(), savedInstanceState);

        handleVisibility();
    }

    @Override
    public void saveInstanceState(Bundle outState) {
        super.saveInstanceState(outState);

        logic.saveInstanceState(outState);
    }

    private void processAnswer(boolean correct) {
        logic.processAnswer(correct);

        flip();
    }

    private void showAnswer() {
        flip();
    }

    private void flip() {
        if (!waiting) {
            logic.flip();

            if (logic.isShowingQuestion()) {
                next();
            } else {
                show();
            }

            handleVisibility();

            if (shouldAnimate()) {
                waiting = true;
                new FadeInAnimation(status).setDuration(ANIMATION_TIME).setInterpolator(new LinearOutSlowInInterpolator()).setListener(new AnimationListener() {
                    @Override
                    public void onAnimationEnd(Animation animation) {
                        waiting = false;
                    }
                }).animate();
            }
        }
    }

    private void handleVisibility() {
        if (logic.isShowingQuestion()) {
            show.setVisibility(View.VISIBLE);
            answerContainer.setVisibility(View.GONE);
        } else {
            show.setVisibility(View.GONE);
            answerContainer.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void show() {
        if (logic.getAmount() < ClassicMode.MIN_AMOUNT) {
            showError();
        } else {
            super.show();

            if (logic.isShowingQuestion()) {
                status.setText(getContext().getString(R.string.test_question) + " '" + logic.getQuestion().toString() + "'?");
            } else {
                status.setText(logic.getAnswer().toString());
            }
        }
    }

    private void next() {
        if (logic.next()) {
            show();
            changeHintVisibility(logic.getHint() != null);
        } else {
            finishTest(logic.getResult(), logic.getVocables());
        }
    }
}
