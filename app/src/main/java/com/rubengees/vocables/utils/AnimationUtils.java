package com.rubengees.vocables.utils;

import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.View;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;

/**
 * Created by Ruben on 10.05.2015.
 */
public class AnimationUtils {

    public static void translateY(View view, int from, int to, int duration, @Nullable final AnimationEndListener listener) {
        ObjectAnimator.ofFloat(view, "translationY", from).setDuration(0).start();
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", to).setDuration(duration);

        animator.setInterpolator(new LinearOutSlowInInterpolator());
        animator.addListener(new android.animation.Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(android.animation.Animator animation) {

            }

            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                if (listener != null) {
                    listener.onAnimationEnd();
                }
            }

            @Override
            public void onAnimationCancel(android.animation.Animator animation) {

            }

            @Override
            public void onAnimationRepeat(android.animation.Animator animation) {

            }
        });
        animator.start();
    }

    public static void animate(@NonNull final View view, @NonNull Techniques techniques, int duration, int delay, @Nullable final AnimationEndListener listener) {
        YoYo.with(techniques).duration(duration).interpolate(new LinearOutSlowInInterpolator()).delay(delay).withListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (listener != null) {
                    listener.onAnimationEnd();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        }).playOn(view);
    }

    public interface AnimationEndListener {
        void onAnimationEnd();
    }

}
