package com.rubengees.vocables.activity;

import android.app.ActivityManager;
import android.app.Service;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
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
import android.view.inputmethod.InputMethodManager;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.melnykov.fab.FloatingActionButton;
import com.rubengees.vocables.R;
import com.rubengees.vocables.utils.Utils;

/**
 * Class wjich gives additional Methods to inheritors.
 * Every Activity should inherit from it.
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

    private OnFabClickListener onFabClickListener;

    private CharSequence currentTitle;
    private Integer currentColor;
    private Integer currentColorDark;

    private boolean isExtended = false;

    @Override
    protected final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extended_toolbar);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarExtension = (ViewGroup) findViewById(R.id.toolbar_extension);
        toolbarExtensionPlaceHolder = findViewById(R.id.toolbar_extension_placeholder);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        ViewGroup content = (ViewGroup) findViewById(R.id.content_container);

        toolbar.bringToFront();

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
            isExtended = !savedInstanceState.getBoolean(SAVED_INSTANCE_STATE_IS_EXTENDED);

            setTitle(currentTitle);
            toggleExtendedToolbar();
        } else {
            currentTitle = getString(R.string.app_name);
            currentColor = getResources().getColor(R.color.primary);
            currentColorDark = getResources().getColor(R.color.primary_dark);
        }

        init(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SAVED_INSTANCE_STATE_COLOR, currentColor);
        outState.putInt(SAVED_INSTANCE_STATE_COLOR_DARK, currentColorDark);
        outState.putCharSequence(SAVED_INSTANCE_STATE_TITLE, currentTitle);
        outState.putBoolean(SAVED_INSTANCE_STATE_IS_EXTENDED, isExtended);
        super.onSaveInstanceState(outState);
    }

    /**
     * A Method as an equivalent to onCreate, which can not be overridden.
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
     */
    public final void toggleExtendedToolbar() {
        if (isExtended) {
            collapseToolbar();
        } else {
            expandToolbar();
        }
    }

    /**
     * Collapses the toolbar if it is extended. If not, nothing happens.
     */
    public final void collapseToolbar() {
        if (isExtended) {
            isExtended = false;
            setToolbarExtensionVisibility(false);

            if (fab != null) {
                fab.setVisibility(View.GONE);
            }
        }
    }

    /**
     * Expands the toolbar if is collapsed. If not, nothing happens.
     */
    public final void expandToolbar() {
        if (!isExtended) {
            isExtended = true;
            setToolbarExtensionVisibility(true);
        }
    }

    /**
     * Helper Method to hide the soft keyboard from the window.
     *
     * @param view The view that is currently focused. (e.g. a EditText)
     */
    public final void hideKeyboard(@Nullable View view) {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Service.INPUT_METHOD_SERVICE);
        if (imm != null) {
            IBinder windowToken;

            if (view == null) {
                windowToken = getWindow().getDecorView().getRootView().getWindowToken();
            } else {
                view.clearFocus();
                windowToken = view.getWindowToken();
            }
            if (windowToken != null) {
                imm.hideSoftInputFromWindow(windowToken, 0);
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
        }
    }

    /**
     * Enables the FAB below the Toolbar.
     *
     * @param drawable The drawable which should be displayed on the fab
     * @param listener An optional listener to monitor clicks on the fab
     * @throws RuntimeException Is thrown if the Toolbar is not extended
     */
    public final void enableFab(@DrawableRes int drawable, @Nullable OnFabClickListener listener) {
        if (isExtended) {
            fab.setImageResource(drawable);
            if (fab.getVisibility() != View.VISIBLE) {
                YoYo.with(Techniques.Landing).duration(500).interpolate(new LinearOutSlowInInterpolator()).playOn(fab);
                fab.setVisibility(View.VISIBLE);
            }
            fab.bringToFront();
        } else {
            throw new RuntimeException("Extension is not visible. The Fab cannot be enabled without the Extended Toolbar.");
        }

        this.onFabClickListener = listener;
    }

    /**
     * Diables the Fab and removes the listener on it
     */
    public final void disableFab() {
        fab.setVisibility(View.GONE);
        onFabClickListener = null;
    }

    /**
     * Styles the Window of the Application according to the colors.
     * This contains the Toolbar, NavigationBar and the Statusbar.
     * {@link #restyleApplication()}
     *
     * @param color     The color
     * @param darkColor A darker Version of the color
     */
    public final void styleApplication(int color, int darkColor) {

        currentColor = color;
        currentColorDark = darkColor;

        if (toolbar != null) {
            toolbar.setBackgroundColor(color);
        }
        if (toolbarExtension != null) {
            toolbarExtension.setBackgroundColor(color);
        }
        if (toolbarExtensionPlaceHolder != null) {
            toolbarExtensionPlaceHolder.setBackgroundColor(color);
        }

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();

            window.setNavigationBarColor(color);
            window.setStatusBarColor(darkColor);

            Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            ActivityManager.TaskDescription taskDesc = new ActivityManager.TaskDescription(getString(R.string.app_name), bm, Utils.darkenColor(color));
            setTaskDescription(taskDesc);
        }
    }

    /**
     * Used to restyle the Application after an Orientation Change.
     * Call in onCreateView Methods.
     * {@link #styleApplication(int, int)}
     */
    public final void restyleApplication() {
        if (currentColor != null && currentColorDark != null) {
            styleApplication(currentColor, currentColorDark);
        }
    }

    /**
     * Styles the Window of the Application according to the colors.
     * This contains the Toolbar, NavigationBar and the Statusbar.
     * {@link #restyleApplication()}
     * @param colorRes The color as Resource-reference
     * @param darkColorRes The color as Resource-reference
     */
    public final void styleApplicationRes(@ColorRes int colorRes, @ColorRes int darkColorRes) {
        Resources resources = getResources();
        int color = resources.getColor(colorRes);
        int darkColor = resources.getColor(darkColorRes);

        styleApplication(color, darkColor);
    }

    /**
     * Sets the Title of the Application
     * @param title the Title
     */
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

    /**
     * Sets the Title of the Application
     * @param titleId the Title as Resource-reference
     */
    @Override
    public final void setTitle(@StringRes int titleId) {
        ActionBar ab = getSupportActionBar();

        if (ab != null) {
            ab.setTitle(titleId);
            currentTitle = ab.getTitle();
        }
    }

    /**
     * Sets the Subtitle of the Application
     * @param subtitle The Subtitle
     */
    public void setSubtitle(String subtitle) {
        ActionBar ab = getSupportActionBar();

        if (ab != null) {
            ab.setSubtitle(subtitle);
        }
    }

    /**
     * Returns the current Color of the Application Window
     * @return The color
     */
    public final Integer getCurrentColor() {
        return currentColor;
    }

    /**
     * Returns the current dark Color of the Application Window
     * @return The color
     */
    public final Integer getCurrentColorDark() {
        return currentColorDark;
    }

    /**
     * Returns the Toolbar
     * @return The Toolbar
     */
    protected final Toolbar getToolbar() {
        return toolbar;
    }

    public interface OnFabClickListener {
        void onFabClick();
    }

}


