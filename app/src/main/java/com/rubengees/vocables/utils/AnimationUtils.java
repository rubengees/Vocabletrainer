package com.rubengees.vocables.utils;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.Interpolator;

/**
 * Utils for animating Views
 *
 * @author Ruben Gees
 */
public class AnimationUtils {

    public static void translateY(@NonNull View view, int y, int duration, @Nullable Interpolator interpolator) {
        ViewPropertyAnimator animator = view.animate().translationYBy(y).setDuration(duration);

        if (interpolator != null) {
            animator.setInterpolator(interpolator);
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            animator.withLayer();
        }

        animator.start();
    }

    public interface AnimationEndListener {
        void onAnimationEnd();
    }

}
