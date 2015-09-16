package com.rubengees.vocables.utils;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;

import java.util.List;

/**
 * Utils for animating Views
 *
 * @author Ruben Gees
 */
public class AnimationUtils {

    public static void translateY(@NonNull View view, int y, int duration,
                                  @Nullable Interpolator interpolator) {
        ViewPropertyAnimator animator = view.animate().translationYBy(y).setDuration(duration);

        if (interpolator != null) {
            animator.setInterpolator(interpolator);
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            animator.withLayer();
        }

        animator.start();
    }

    public static void fadeIn(@NonNull View view, int duration, @Nullable Interpolator interpolator,
                              @Nullable final AnimationEndListener listener) {
        ViewCompat.setAlpha(view, 0);
        ViewCompat.animate(view).alpha(1).setDuration(duration)
                .setInterpolator(interpolator == null ?
                        new LinearOutSlowInInterpolator() : interpolator)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null) {
                            listener.onAnimationEnd();
                        }
                    }
                }).start();
    }

    public static void fadeInParallel(@NonNull List<View> views, int duration,
                                      @Nullable Interpolator interpolator) {
        for (View view : views) {
            ViewCompat.setAlpha(view, 0);
            ViewCompat.animate(view).alpha(1).setDuration(duration).setInterpolator(interpolator == null ?
                    new LinearOutSlowInInterpolator() : interpolator).start();
        }
    }

    public static void fadeOut(@NonNull View view, int duration, @Nullable Interpolator interpolator,
                               @Nullable final AnimationEndListener listener) {
        ViewCompat.animate(view).alpha(0).setDuration(duration)
                .setInterpolator(interpolator == null ?
                        new LinearOutSlowInInterpolator() : interpolator)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null) {
                            listener.onAnimationEnd();
                        }
                    }
                }).start();
    }

    public static void scaleIn(@NonNull View view, int duration, @Nullable Interpolator interpolator,
                               @Nullable final AnimationEndListener listener) {
        ViewCompat.setScaleX(view, 2);
        ViewCompat.setScaleY(view, 2);
        ViewCompat.animate(view).scaleX(1).scaleY(1).setDuration(duration)
                .setInterpolator(interpolator == null ? new LinearOutSlowInInterpolator()
                        : interpolator).withEndAction(new Runnable() {
            @Override
            public void run() {
                if (listener != null) {
                    listener.onAnimationEnd();
                }
            }
        }).start();
    }

    public interface AnimationEndListener {
        void onAnimationEnd();
    }

}
