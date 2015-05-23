package com.rubengees.vocables.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.melnykov.fab.FloatingActionButton;
import com.rubengees.vocables.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruben on 22.05.15.
 */
public abstract class ExtendedToolbarActivity extends AppCompatActivity {

    private static final String SAVED_INSTANCE_STATE_TITLE = "activity_title";
    private static final String SAVED_INSTANCE_STATE_COLOR = "activity_color";
    private static final String SAVED_INSTANCE_STATE_COLOR_DARK = "activity_color_dark";
    private static final String SAVED_INSTANCE_STATE_IS_EXTENDED = "activity_is_extended";

    private Toolbar toolbar;
    private View toolbarExtensionPlaceHolder;
    private ViewGroup toolbarExtension;
    private FloatingActionButton fab;
    private ViewGroup content;

    private OnFabClickListener onFabClickListener;

    private CharSequence currentTitle;
    private Integer currentColor;
    private Integer currentColorDark;

    private boolean isExtended = false;

    private AnimatorSet currentAnimator;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extended_toolbar);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarExtension = (ViewGroup) findViewById(R.id.toolbar_extension);
        toolbarExtensionPlaceHolder = findViewById(R.id.toolbar_extension_placeholder);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        content = (ViewGroup) findViewById(R.id.content);

        setSupportActionBar(toolbar);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onFabClickListener != null) {
                    onFabClickListener.onFabClick();
                }
            }
        });
        inflateLayout(LayoutInflater.from(this), content, savedInstanceState);

        if (savedInstanceState != null) {
            currentTitle = savedInstanceState.getCharSequence(SAVED_INSTANCE_STATE_TITLE);
            currentColor = savedInstanceState.getInt(SAVED_INSTANCE_STATE_COLOR);
            currentColorDark = savedInstanceState.getInt(SAVED_INSTANCE_STATE_COLOR_DARK);
            isExtended = savedInstanceState.getBoolean(SAVED_INSTANCE_STATE_IS_EXTENDED);

            setTitle(currentTitle);
            styleApplication(currentColor, currentColorDark);
        }

        init(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        if (currentColor != null) {
            outState.putInt(SAVED_INSTANCE_STATE_COLOR, currentColor);
        }

        if (currentColorDark != null) {
            outState.putInt(SAVED_INSTANCE_STATE_COLOR_DARK, currentColorDark);
        }

        if (currentTitle != null) {
            outState.putCharSequence(SAVED_INSTANCE_STATE_TITLE, currentTitle);
        }

        outState.putBoolean(SAVED_INSTANCE_STATE_IS_EXTENDED, isExtended);
    }

    /**
     * A Method as an equivalent to onCreate which can not be overridden.
     */
    public void init(Bundle savedInstanceState) {

    }

    /**
     * Called during onCreate. The inheriting class can inflate it's specific layout here and adds it HIMSELF to the parent.
     * This Method is called before init().
     *
     * @param savedInstanceState The Instance State
     * @param container          The parent to insert the specific layout in
     * @param inflater           The inflater which can be used to inflate the specific layout
     */
    protected abstract void inflateLayout(LayoutInflater inflater, ViewGroup container,
                                          Bundle savedInstanceState);

    /**
     * Sets the View inside the extended Toolbar. Pass null to remove all Views.
     *
     * @param view The View to add
     */
    public void setToolbarView(@Nullable View view) {
        if (isExtended) {
            toolbarExtension.removeAllViews();
            if (view != null) {
                toolbarExtension.addView(view);
            }
        } else {
            throw new RuntimeException("Extension is not visible. Can't add a View to it.");
        }
    }

    /**
     * Expands or collapses the toolbar, depending on its state.
     *
     * @param withAnimation Animate the toggle
     */
    public final void toggleExtendedToolbar(boolean withAnimation) {
        if (isExtended) {
            collapseToolbar(withAnimation);
        } else {
            expandToolbar(withAnimation);
        }
    }

    public final void collapseToolbar(boolean withAnimation) {
        if (currentAnimator != null) {
            currentAnimator.end();
        }

        if (isExtended) {
            isExtended = false;

            if (withAnimation) {
                animateToolbar(0, -toolbarExtension.getHeight(), false);
            } else {
                setToolbarExtensionVisibility(false);
            }
        }
    }

    public final void expandToolbar(boolean withAnimation) {
        if (currentAnimator != null) {
            currentAnimator.end();
        }

        if (!isExtended) {
            isExtended = true;

            if (withAnimation) {
                toolbarExtension.setVisibility(View.VISIBLE);
                toolbarExtensionPlaceHolder.setVisibility(View.VISIBLE);
                animateToolbar(-toolbarExtension.getHeight(), 0, true);
            } else {
                setToolbarExtensionVisibility(true);
            }
        }
    }

    private void setToolbarExtensionVisibility(boolean visible) {
        if (visible) {
            toolbarExtension.setVisibility(View.VISIBLE);
            toolbarExtensionPlaceHolder.setVisibility(View.VISIBLE);
        } else {
            toolbarExtension.setVisibility(View.GONE);
            toolbarExtensionPlaceHolder.setVisibility(View.GONE);
            if (fab.isVisible()) {
                fab.setVisibility(View.GONE);
            }
        }
    }

    private void animateToolbar(int from, int to, final boolean visibleAfter) {
        currentAnimator = new AnimatorSet();
        List<Animator> animators = new ArrayList<>(2);

        setTranslationY(toolbarExtension, from);
        setTranslationY(toolbarExtensionPlaceHolder, from);
        setTranslationY(content, from);

        animators.add(ObjectAnimator.ofFloat(toolbarExtension, "translationY", to));
        animators.add(ObjectAnimator.ofFloat(toolbarExtensionPlaceHolder, "translationY", to));
        animators.add(ObjectAnimator.ofFloat(content, "translationY", to));

        currentAnimator.setInterpolator(new LinearOutSlowInInterpolator());
        currentAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setToolbarExtensionVisibility(visibleAfter);
                currentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        currentAnimator.playTogether(animators);
    }

    private void setTranslationY(@NonNull View view, int translationY) {
        view.setTranslationY(translationY);
    }

    /**
     * Enables the FAB below the Toolbar.
     *
     * @param drawable The drawable which should be displayed on the fab
     * @param listener An optional listener to monitor clicks on the fab
     */
    public final void enableFab(@DrawableRes int drawable, @Nullable OnFabClickListener listener) {
        if (isExtended) {
            fab.setVisibility(View.VISIBLE);
        } else {
            throw new RuntimeException("Extension is not visible. The FAB cannot be enabled without the Extended Toolbar.");
        }

        this.onFabClickListener = listener;
    }

    public final void styleApplication(Integer color, Integer darkColor) {
        if (color == null || darkColor == null) {
            return;
        }

        currentColor = color;
        currentColorDark = darkColor;

        toolbar.setBackgroundColor(color);
        toolbarExtension.setBackgroundColor(color);
        toolbarExtensionPlaceHolder.setBackgroundColor(color);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();

            window.setNavigationBarColor(color);
            window.setStatusBarColor(darkColor);
        }
    }

    public final void styleApplicationRes(@ColorRes int colorRes, @ColorRes int darkColorRes) {
        Resources resources = getResources();
        int color = resources.getColor(colorRes);
        int darkColor = resources.getColor(darkColorRes);

        styleApplication(color, darkColor);
    }

    @Override
    public final void setTitle(CharSequence title) {
        if (title == null) {
            return;
        }

        ActionBar ab = getSupportActionBar();

        if (ab != null) {
            ab.setTitle(title);
            currentTitle = ab.getTitle();
        }
    }

    @Override
    public final void setTitle(@StringRes int titleId) {
        ActionBar ab = getSupportActionBar();

        if (ab != null) {
            ab.setTitle(titleId);
            currentTitle = ab.getTitle();
        }
    }

    public final Integer getCurrentColor() {
        return currentColor;
    }

    public final Integer getCurrentColorDark() {
        return currentColorDark;
    }

    protected final Toolbar getToolbar() {
        return toolbar;
    }

    public interface OnFabClickListener {
        void onFabClick();
    }

}


