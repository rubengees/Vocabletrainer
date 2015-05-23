package com.rubengees.vocables.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.Display;

import com.rubengees.vocables.activity.ExtendedToolbarActivity;
import com.rubengees.vocables.activity.MainActivity;

/**
 * Created by ruben on 01.05.15.
 */
public class MainFragment extends Fragment implements MainActivity.OnBackPressedListener {

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar ab = getActivity().getActionBar();

        if (ab != null) {
            ab.setSubtitle(null);
        }
    }

    protected ExtendedToolbarActivity getToolbarActivity() {
        return (ExtendedToolbarActivity) getActivity();
    }

    @Override
    public boolean onBackPressed() {
        return true;
    }

    /**
     * By faizan ali (Comment-Section of http://trickyandroid.com/fragments-translate-animation/)
     * @param transit Transit Mode
     * @param enter If it is enter or exit Animation
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

        if(enter) {
            animator = ObjectAnimator.ofFloat(this, "translationX", displayWidth / 4, 0);
        }

        if(animator != null) {
            animator.setDuration(500);
            animator.setInterpolator(new LinearOutSlowInInterpolator());
        }

        return animator;
    }
}
