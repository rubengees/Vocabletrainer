package com.rubengees.vocables.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.rubengees.introduction.IntroductionActivity;
import com.rubengees.introduction.IntroductionBuilder;
import com.rubengees.introduction.entity.Option;
import com.rubengees.introduction.entity.Slide;
import com.rubengees.vocables.R;
import com.rubengees.vocables.core.Core;
import com.rubengees.vocables.core.mode.Mode;
import com.rubengees.vocables.dialog.DonateDialog;
import com.rubengees.vocables.dialog.EvaluationDialog;
import com.rubengees.vocables.dialog.PlayGamesDialog;
import com.rubengees.vocables.fragment.HelpFragment;
import com.rubengees.vocables.fragment.SettingsFragment;
import com.rubengees.vocables.fragment.StatisticsFragment;
import com.rubengees.vocables.fragment.TestSettingsFragment;
import com.rubengees.vocables.fragment.VocableListFragment;
import com.rubengees.vocables.utils.PreferenceUtils;
import com.rubengees.vocables.utils.ReminderUtils;
import com.rubengees.vocables.utils.SnackbarManager;
import com.rubengees.vocables.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends ExtendedToolbarActivity implements EvaluationDialog.EvaluationDialogCallback, PlayGamesDialog.PlayGamesDialogCallback {

    private static final String DIALOG_EVALUATION = "dialog_evaluation";
    private static final String DIALOG_PLAY_GAMES = "dialog_play_games";
    private static final String DONATE_DIALOG = "donate_dialog";
    private static final String STATE_DRAWER_IDENTIFIER = "drawer_identifier";

    private Drawer drawer;
    private AdView adView;
    private Core core;
    private OnBackPressedListener onBackPressedListener;
    private int currentDrawerItemIdentifier = 10;

    private Drawer.OnDrawerItemClickListener onDrawerItemClickListener = new Drawer.OnDrawerItemClickListener() {

        @Override
        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
            int identifier = drawerItem.getIdentifier();

            if (identifier != currentDrawerItemIdentifier) {
                if (drawerItem.isSelectable()) {
                    currentDrawerItemIdentifier = identifier;
                }

                if (identifier >= 10) {
                    switch (identifier) {
                        case 10:
                            setFragment(VocableListFragment.newInstance(), getString(R.string.fragment_vocable_list_title));
                            return false;
                        case 11:
                            setFragment(StatisticsFragment.newInstance((ArrayList<Mode>) core.getModeList()), getString(R.string.fragment_statistics_title));
                            return false;
                        case 12:
                            PlayGamesDialog playGamesDialog = PlayGamesDialog.newInstance();

                            playGamesDialog.setCallback(MainActivity.this);
                            playGamesDialog.show(getFragmentManager(), DIALOG_PLAY_GAMES);
                            return true;
                        case 13:
                            setFragment(HelpFragment.newInstance((ArrayList<Mode>) core.getModeList()), getString(R.string.fragment_help_title));
                            return false;
                        case 14:
                            DonateDialog donateDialog = DonateDialog.newInstance();

                            donateDialog.show(getFragmentManager(), DONATE_DIALOG);
                            return true;
                        case 15:
                            setFragment(new SettingsFragment(), getString(R.string.fragment_settings_title));
                            return false;
                    }
                } else {
                    HashMap<Integer, Mode> modes = core.getModes();
                    Mode current = modes.get(identifier);

                    setFragment(TestSettingsFragment.newInstance(current), current.getTitle(MainActivity.this), current.getColor(MainActivity.this),
                            current.getDarkColor(MainActivity.this));

                    return false;
                }
            }

            return true;
        }
    };

    private void showIntroAndDialogs() {
        if (PreferenceUtils.isFirstStart(this)) {
            new IntroductionBuilder(this).withSlides(generateIntroSlides()).introduceMyself();

            PreferenceUtils.setFirstStarted(this);
        } else if (!PreferenceUtils.hasEvaluated(this)) {
            EvaluationDialog dialog = EvaluationDialog.newInstance();
            dialog.setCallback(this);

            dialog.show(getFragmentManager(), DIALOG_EVALUATION);
        }
    }

    private List<Slide> generateIntroSlides() {
        List<Slide> slides = new ArrayList<>();

        slides.add(new Slide().withColorResource(R.color.primary)
                .withTitle(R.string.intro_welcome_title).withImage(R.drawable.ic_intro_books)
                .withDescription(R.string.intro_welcome_description));
        slides.add(new Slide().withColorResource(R.color.accent)
                .withTitle(R.string.intro_ads_title).withImage(R.drawable.ic_intro_ad)
                .withOption(new Option(getString(R.string.intro_ads_description), true)));
        slides.add(new Slide().withColorResource(R.color.pair_mode)
                .withTitle(R.string.intro_reminder_title).withImage(R.drawable.ic_intro_bell)
                .withOption(new Option(getString(R.string.intro_reminder_description), false)));
        slides.add(new Slide().withColorResource(R.color.training_mode)
                .withTitle(R.string.intro_play_games_title).withImage(R.drawable.ic_intro_play_games)
                .withOption(new Option(getString(R.string.intro_play_games_description), false)));

        return slides;
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IntroductionBuilder.INTRODUCTION_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                ArrayList<Option> options =
                        data.getParcelableArrayListExtra(IntroductionActivity.OPTION_RESULT);

                for (Option option : options) {
                    if (option.getPosition() == 1) {
                        setAdsEnabled(option.isActivated());
                    } else if (option.getPosition() == 2) {
                        setReminderEnabled(option.isActivated());
                    } else if (option.getPosition() == 3) {
                        if (option.isActivated()) {
                            core.getConnection().connect();
                        }
                    }
                }
            } else {
                setAdsEnabled(false);
                setReminderEnabled(false);
            }
        } else {
            if (resultCode != RESULT_CANCELED) {
                core.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        core.onSaveInstanceState(outState);
        drawer.saveInstanceState(outState);
        outState.putInt(STATE_DRAWER_IDENTIFIER, currentDrawerItemIdentifier);
        super.onSaveInstanceState(outState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(Bundle savedInstanceState) {
        core = Core.getInstance(this, savedInstanceState);
        adView = (AdView) findViewById(R.id.adView);

        generateDrawer(savedInstanceState);

        if (savedInstanceState == null) {
            setFragment(VocableListFragment.newInstance(), getString(R.string.fragment_vocable_list_title));

            showIntroAndDialogs();
        } else {
            FragmentManager manager = getFragmentManager();
            EvaluationDialog evaluationDialog = (EvaluationDialog) manager.findFragmentByTag(DIALOG_EVALUATION);
            PlayGamesDialog playGamesDialog = (PlayGamesDialog) manager.findFragmentByTag(DIALOG_PLAY_GAMES);

            if (evaluationDialog != null) {
                evaluationDialog.setCallback(this);
            }

            if (playGamesDialog != null) {
                playGamesDialog.setCallback(this);
            }

            Fragment current = manager.findFragmentById(R.id.content);

            if (current != null && current instanceof OnBackPressedListener) {
                onBackPressedListener = (OnBackPressedListener) current;
            }

            currentDrawerItemIdentifier = savedInstanceState.getInt(STATE_DRAWER_IDENTIFIER);
        }

        if (PreferenceUtils.shouldShowAds(this)) {
            showAds();
        }
    }

    @Override
    protected void inflateLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflater.inflate(R.layout.activity_main, container, true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        core.onStart();
    }

    @Override
    protected void onStop() {
        core.onStop();
        super.onStop();
    }

    @Override
    public void onPause() {
        adView.pause();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        adView.resume();
    }

    @Override
    public void onDestroy() {
        adView.destroy();
        core.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else {
            if (onBackPressedListener != null) {
                if (onBackPressedListener.onBackPressed()) {
                    if (drawer.getCurrentSelection() != 10) {
                        drawer.setSelection(10, true);
                    } else {
                        super.onBackPressed();
                    }
                }
            } else {
                super.onBackPressed();
            }
        }
    }

    private void generateDrawer(@Nullable Bundle savedInstanceState) {
        drawer = new DrawerBuilder().withActivity(this).withToolbar(getToolbar())
                .withDrawerItems(generateDrawerItems())
                .withStickyDrawerItems(generateStickyDrawerItems())
                .withOnDrawerItemClickListener(onDrawerItemClickListener)
                .withShowDrawerOnFirstLaunch(true).withActionBarDrawerToggleAnimated(true)
                .withHasStableIds(true).withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View view) {
                        hideKeyboard(null);
                    }

                    @Override
                    public void onDrawerClosed(View view) {

                    }

                    @Override
                    public void onDrawerSlide(View view, float v) {

                    }
                }).withSavedInstance(savedInstanceState).build();
    }

    private ArrayList<IDrawerItem> generateDrawerItems() {
        ArrayList<IDrawerItem> result = new ArrayList<>();

        result.add(new PrimaryDrawerItem().withName(getString(R.string.fragment_vocable_list_title)).withIcon(R.drawable.ic_list)
                .withSelectedTextColorRes(R.color.primary)
                .withSelectedIconColorRes(R.color.primary).withIconTintingEnabled(true).withIdentifier(10));
        result.add(new SectionDrawerItem().withName(getString(R.string.activity_main_divider_modes)));
        result.addAll(generateModeItems());
        result.add(new DividerDrawerItem());
        result.add(new PrimaryDrawerItem().withName(getString(R.string.fragment_statistics_title)).withIcon(R.drawable.ic_stats)
                .withSelectedTextColorRes(R.color.primary)
                .withIconTintingEnabled(true).withSelectedIconColorRes(R.color.primary).withIdentifier(11));

        PrimaryDrawerItem playGames = new PrimaryDrawerItem().withName(getString(R.string.dialog_play_games_title)).withIcon(R.drawable.ic_play_games)
                .withIconTintingEnabled(true).withSelectedTextColorRes(R.color.primary).withSelectedIconColorRes(R.color.primary).withSelectable(false).withIdentifier(12);

        result.add(playGames);

        return result;
    }

    private ArrayList<IDrawerItem> generateStickyDrawerItems() {
        ArrayList<IDrawerItem> result = new ArrayList<>();

        result.add(new PrimaryDrawerItem().withName(getString(R.string.fragment_help_title)).withIcon(R.drawable.ic_help).withSelectedTextColorRes(R.color.primary)
                .withIconTintingEnabled(true).withSelectedIconColorRes(R.color.primary).withIdentifier(13));

        PrimaryDrawerItem donate = new PrimaryDrawerItem().withName(getString(R.string.dialog_donate_title)).withIcon(R.drawable.ic_donate)
                .withIconTintingEnabled(true).withSelectedTextColorRes(R.color.primary).withSelectedIconColorRes(R.color.primary).withSelectable(false).withIdentifier(14);

        result.add(donate);

        result.add(new PrimaryDrawerItem().withName(getString(R.string.fragment_settings_title)).withIcon(R.drawable.ic_settings)
                .withIconTintingEnabled(true).withSelectedTextColorRes(R.color.primary).withSelectedIconColorRes(R.color.primary).withIdentifier(15));
        return result;
    }

    private List<IDrawerItem> generateModeItems() {
        List<IDrawerItem> result = new ArrayList<>();
        HashMap<Integer, Mode> modes = core.getModes();

        for (Integer id : modes.keySet()) {
            Mode current = modes.get(id);
            int color = current.getColor(this);

            result.add(new PrimaryDrawerItem().withName(current.getTitle(this)).withIcon(current.getIcon(this))
                    .withSelectedTextColor(color).withIconTintingEnabled(true).withSelectedIconColor(color).withIdentifier(current.getId()));
        }

        return result;
    }

    /**
     * Sets the current visible Fragment
     *
     * @param fragment  The new Fragment
     * @param title     The Title of the Fragment
     * @param color     The color to style the Application with
     * @param darkColor A darker Version fo the color
     */
    public void setFragment(@NonNull final Fragment fragment, @Nullable String title, int color,
                            int darkColor) {
        if (fragment instanceof OnBackPressedListener) {
            onBackPressedListener = (OnBackPressedListener) fragment;
        } else {
            onBackPressedListener = null;
        }

        getFragmentManager().beginTransaction()
                .replace(R.id.content, fragment).commitAllowingStateLoss();

        setTitle(title);
        styleApplication(color, darkColor);
        SnackbarManager.dismiss();
    }

    /**
     * Sets the current visible Fragment
     *
     * @param fragment The new Fragment
     * @param title    The Title of the Fragment
     */
    public void setFragment(@NonNull Fragment fragment, @Nullable String title) {
        int color = ContextCompat.getColor(this, R.color.primary);
        int darkColor = ContextCompat.getColor(this, R.color.primary_dark);

        setFragment(fragment, title, color, darkColor);
    }

    private void setReminderEnabled(boolean enabled) {
        if (enabled) {
            PreferenceUtils.setReminder(this, true);
            ReminderUtils.setReminder(this);
        } else {
            PreferenceUtils.setReminder(this, false);
            ReminderUtils.cancelReminder(this);
        }
    }

    private void setAdsEnabled(boolean enabled) {
        if (enabled) {
            showAds();
        } else {
            hideAds();
        }
    }

    public void hideAds() {
        PreferenceUtils.setAds(this, false);
        adView.setVisibility(View.GONE);
        adView.destroy();
    }

    public void showAds() {
        PreferenceUtils.setAds(this, true);
        adView.setVisibility(View.VISIBLE);
        AdRequest adRequest = new AdRequest.Builder().addKeyword("Vocable").addKeyword("Learning")
                .addKeyword("Game").build();

        adView.loadAd(adRequest);
    }

    @Override
    public void onEvaluate() {
        Utils.showGooglePlusPage(this);
        PreferenceUtils.setEvaluated(this);
    }

    @Override
    public void onEvaluateNot() {
        PreferenceUtils.setEvaluated(this);
    }

    @Override
    public void onSignIn() {
        core.getConnection().connect();
    }

    @Override
    public void onSignOut() {
        core.getConnection().disconnect();
    }

    @Override
    public void onShowAchievements() {
        core.getConnection().showAchievements();
    }

    public interface OnBackPressedListener {

        /**
         * Callback, which is called if the Back Button is pressed.
         * Fragments that extend MainFragment can/should override this Method.
         *
         * @return true if the App can be closed, false otherwise
         */
        boolean onBackPressed();
    }
}
