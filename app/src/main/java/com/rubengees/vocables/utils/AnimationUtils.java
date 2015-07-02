package com.rubengees.vocables.utils;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;

/**
 * Utils for animating Views
 *
 * @author Ruben Gees
 */
public class AnimationUtils {

    public static void translateY(View view, int y, int duration, Interpolator interpolator) {
        ViewPropertyAnimator animator = view.animate().translationYBy(y).setDuration(duration).setInterpolator(interpolator);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            animator.withLayer();
        }
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
