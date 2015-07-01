package com.rubengees.vocables.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.Display;
import android.view.View;

import com.rubengees.vocables.activity.ExtendedToolbarActivity;
import com.rubengees.vocables.activity.MainActivity;

/**
 * A Fragment with some helper-methods, from which every Fragment should inherit if possible.
 *
 * @author Ruben Gees
 */
public class MainFragment extends Fragment implements MainActivity.OnBackPressedListener {

    protected static final int ANIMATION_DURATION = 500;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Reset the subtitle every time a new Fragment is shown
        getToolbarActivity().setSubtitle(null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Set the primary color after every Orientation change
        if (savedInstanceState != null) {
            getToolbarActivity().restyleApplication();
        }
    }

    /**
     * Simple Util-method returning the parent {@link android.app.Activity} directly as an {@link ExtendedToolbarActivity}.
     *
     * @return The parent Activity
     */
    protected ExtendedToolbarActivity getToolbarActivity() {
        return (ExtendedToolbarActivity) getActivity();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onBackPressed() {
        return true;
    }

    /**
     * Animates this Fragment in.
     * By faizan ali (Comment-Section of http://trickyandroid.com/fragments-translate-animation/)
     *
     * @param transit  Transit Mode
     * @param enter    If it is enter or exit Animation
     * @param nextAnim The next Animation
     * @return the Animator
     */
    @Override
    public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        float displayWidth = size.x;
        Animator animator = null;

        if (enter) {
            animator = ObjectAnimator.ofFloat(this, "translationX", displayWidth / 4, 0);
            animator.setDuration(ANIMATION_DURATION);
            animator.setInterpolator(new LinearOutSlowInInterpolator());
        }

        return animator;
    }
}
