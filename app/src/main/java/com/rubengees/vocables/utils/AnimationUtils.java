package com.rubengees.vocables.utils;

import android.animation.ObjectAnimator;
import android.support.annotation.AnimRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Interpolator;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;

/**
 * Created by Ruben on 10.05.2015.
 */
public class AnimationUtils {

    public static void translateY(View view, int from, int to, int duration, Interpolator interpolator, @Nullable final AnimationEndListener listener) {
        ObjectAnimator.ofFloat(view, "translationY", from).setDuration(0).start();
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", to).setDuration(duration);

        animator.setInterpolator(interpolator);
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

    public static void animateAndroidFramework(@NonNull final View view, @AnimRes int anim, int duration, @Nullable final AnimationEndListener listener) {
        Animation animation = android.view.animation.AnimationUtils.loadAnimation(view.getContext(), anim);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (listener != null) {
                    listener.onAnimationEnd();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        animation.setDuration(duration);
        animation.setInterpolator(new LinearOutSlowInInterpolator());

        view.startAnimation(animation);
    }

    public static void animate(@NonNull final View view, @NonNull Techniques techniques, int duration, int delay, @Nullable final AnimationEndListener listener) {
        YoYo.with(techniques).duration(duration).delay(delay).withListener(new Animator.AnimatorListener() {
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
