package com.rubengees.vocables.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.android.vending.billing.IInAppBillingService;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.rubengees.vocables.R;
import com.rubengees.vocables.core.Core;
import com.rubengees.vocables.core.mode.Mode;
import com.rubengees.vocables.dialog.DonateDialog;
import com.rubengees.vocables.dialog.EvaluationDialog;
import com.rubengees.vocables.dialog.PlayGamesDialog;
import com.rubengees.vocables.dialog.WelcomeDialog;
import com.rubengees.vocables.fragment.HelpFragment;
import com.rubengees.vocables.fragment.SettingsFragment;
import com.rubengees.vocables.fragment.StatisticsFragment;
import com.rubengees.vocables.fragment.TestSettingsFragment;
import com.rubengees.vocables.fragment.VocableListFragment;
import com.rubengees.vocables.utils.PreferenceUtils;
import com.rubengees.vocables.utils.ReminderUtils;
import com.rubengees.vocables.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;


public class MainActivity extends ExtendedToolbarActivity implements Drawer.OnDrawerItemClickListener, WelcomeDialog.WelcomeDialogCallback, EvaluationDialog.EvaluationDialogCallback, PlayGamesDialog.PlayGamesDialogCallback, DonateDialog.DonateDialogCallback {

    private static final String DIALOG_WELCOME = "dialog_welcome";
    private static final String DIALOG_EVALUATION = "dialog_evaluation";
    private static final String DIALOG_PLAY_GAMES = "dialog_play_games";
    private static final String CURRENT_DRAWER_ID = "current_drawer_id";
    private static final String CURRENT_MODE = "current_mode";
    private static final int REQUEST_PURCHASE = 10513;
    private static final String DONATE_DIALOG = "donate_dialog";
    IInAppBillingService billingService;
    ServiceConnection billingServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            billingService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name,
                                       IBinder service) {
            billingService = IInAppBillingService.Stub.asInterface(service);
        }
    };
    private Drawer drawer;
    private AdView adView;
    private Core core;
    private OnBackPressedListener onBackPressedListener;
    private long currentDrawerId;
    private Mode currentMode;

    private void showDialog() {
        if (PreferenceUtils.isFirstStart(this)) {
            WelcomeDialog dialog = WelcomeDialog.newInstance();
            dialog.setCallback(this);

            dialog.show(getFragmentManager(), DIALOG_WELCOME);
        } else if (!PreferenceUtils.hasEvaluated(this)) {
            EvaluationDialog dialog = EvaluationDialog.newInstance();
            dialog.setCallback(this);

            dialog.show(getFragmentManager(), DIALOG_EVALUATION);
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        if (requestCode == REQUEST_PURCHASE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, getString(R.string.activity_main_donation_message), Toast.LENGTH_SHORT).show();

                try {
                    JSONObject object = new JSONObject(data.getStringExtra("INAPP_PURCHASE_DATA"));

                    new AsyncTask<String, Void, Void>() {

                        @Override
                        protected Void doInBackground(String... params) {
                            try {
                                billingService.consumePurchase(3, getPackageName(), params[0]);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }

                    }.execute(object.getString("purchaseToken"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            core.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        core.onSaveInstanceState(outState);
        drawer.saveInstanceState(outState);
        outState.putLong(CURRENT_DRAWER_ID, currentDrawerId);
        outState.putParcelable(CURRENT_MODE, currentMode);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void init(Bundle savedInstanceState) {
        Fabric.with(this, new Crashlytics());
        core = Core.getInstance(this, savedInstanceState);
        generateDrawer(savedInstanceState);

        adView = (AdView) findViewById(R.id.adView);

        if (savedInstanceState == null) {
            setFragment(VocableListFragment.newInstance(), getString(R.string.fragment_vocable_list_title));

            showDialog();
        } else {
            FragmentManager manager = getFragmentManager();

            WelcomeDialog welcomeDialog = (WelcomeDialog) manager.findFragmentByTag(DIALOG_WELCOME);
            EvaluationDialog evaluationDialog = (EvaluationDialog) manager.findFragmentByTag(DIALOG_EVALUATION);
            PlayGamesDialog playGamesDialog = (PlayGamesDialog) manager.findFragmentByTag(DIALOG_PLAY_GAMES);
            DonateDialog donateDialog = (DonateDialog) manager.findFragmentByTag(DONATE_DIALOG);

            if (welcomeDialog != null) {
                welcomeDialog.setCallback(this);
            }

            if (evaluationDialog != null) {
                evaluationDialog.setCallback(this);
            }

            if (playGamesDialog != null) {
                playGamesDialog.setCallback(this);
            }

            if (donateDialog != null) {
                donateDialog.setCallback(this);
            }

            Fragment current = manager.findFragmentById(R.id.content);

            if (current != null && current instanceof OnBackPressedListener) {
                onBackPressedListener = (OnBackPressedListener) current;
            }

            currentDrawerId = savedInstanceState.getLong(CURRENT_DRAWER_ID);
            currentMode = savedInstanceState.getParcelable(CURRENT_MODE);
        }

        if (PreferenceUtils.shouldShowAds(this)) {
            showAds();
        }

        connectBillingService();
    }

    private void connectBillingService() {
        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        bindService(serviceIntent, billingServiceConnection, Context.BIND_AUTO_CREATE);
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
        if (billingService != null) {
            unbindService(billingServiceConnection);
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else {
            if (onBackPressedListener != null && onBackPressedListener.onBackPressed()) {
                if (onBackPressedListener instanceof VocableListFragment) {
                    super.onBackPressed();
                } else {
                    drawer.setSelection(0);
                }
            }
        }
    }

    private void generateDrawer(Bundle savedInstanceState) {
        drawer = new DrawerBuilder().withActivity(this).withToolbar(getToolbar())
                .withDrawerItems(generateDrawerItems()).withSavedInstance(savedInstanceState).withStickyDrawerItems(generateStickyDrawerItems())
                .withOnDrawerItemClickListener(this).withShowDrawerOnFirstLaunch(true).withActionBarDrawerToggleAnimated(true).withOnDrawerListener(new Drawer.OnDrawerListener() {
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
                }).build();
    }

    private ArrayList<IDrawerItem> generateDrawerItems() {
        ArrayList<IDrawerItem> result = new ArrayList<>();

        result.add(new PrimaryDrawerItem().withName(getString(R.string.fragment_vocable_list_title)).withIcon(R.drawable.ic_list)
                .withSelectedTextColorRes(R.color.primary)
                .withSelectedIconColorRes(R.color.primary).withIconTintingEnabled(true).withIdentifier(0));
        result.add(new SectionDrawerItem().withName(getString(R.string.activity_main_divider_modes)));
        result.addAll(generateModeItems());
        result.add(new DividerDrawerItem());
        result.add(new PrimaryDrawerItem().withName(getString(R.string.fragment_statistics_title)).withIcon(R.drawable.ic_stats)
                .withSelectedTextColorRes(R.color.primary)
                .withIconTintingEnabled(true).withSelectedIconColorRes(R.color.primary).withIdentifier(2));

        PrimaryDrawerItem playGames = new PrimaryDrawerItem().withName(getString(R.string.dialog_play_games_title)).withIcon(R.drawable.ic_play_games)
                .withIconTintingEnabled(true).withSelectedTextColorRes(R.color.primary).withSelectedIconColorRes(R.color.primary).withIdentifier(3);

        playGames.setCheckable(false);
        result.add(playGames);

        return result;
    }

    private ArrayList<IDrawerItem> generateStickyDrawerItems() {
        ArrayList<IDrawerItem> result = new ArrayList<>();

        result.add(new PrimaryDrawerItem().withName(getString(R.string.fragment_help_title)).withIcon(R.drawable.ic_help).withSelectedTextColorRes(R.color.primary)
                .withIconTintingEnabled(true).withSelectedIconColorRes(R.color.primary).withIdentifier(4));

        PrimaryDrawerItem donate = new PrimaryDrawerItem().withName(getString(R.string.dialog_donate_title)).withIcon(R.drawable.ic_donate)
                .withIconTintingEnabled(true).withSelectedTextColorRes(R.color.primary).withSelectedIconColorRes(R.color.primary).withIdentifier(5);

        donate.setCheckable(false);
        result.add(donate);

        result.add(new PrimaryDrawerItem().withName(getString(R.string.fragment_settings_title)).withIcon(R.drawable.ic_settings)
                .withIconTintingEnabled(true).withSelectedTextColorRes(R.color.primary).withSelectedIconColorRes(R.color.primary).withIdentifier(6));
        return result;
    }

    private List<IDrawerItem> generateModeItems() {
        List<IDrawerItem> result = new ArrayList<>();

        for (Mode mode : core.getModes()) {
            int color = mode.getColor(this);

            result.add(new PrimaryDrawerItem().withName(mode.getTitle(this)).withIcon(mode.getIcon(this))
                    .withSelectedTextColor(color).withIconTintingEnabled(true).withSelectedIconColor(color).withIdentifier(1).withTag(mode));
        }

        return result;
    }

    public void setFragment(Fragment fragment, String title, int color, int darkColor) {
        if (fragment instanceof OnBackPressedListener) {
            onBackPressedListener = (OnBackPressedListener) fragment;
        } else {
            onBackPressedListener = null;
        }

        getFragmentManager().beginTransaction()
                .replace(R.id.content, fragment).commit();

        setTitle(title);
        styleApplication(color, darkColor);
    }

    public void setFragment(Fragment fragment, String title) {
        int color = getResources().getColor(R.color.primary);
        int darkColor = getResources().getColor(R.color.primary_dark);

        setFragment(fragment, title, color, darkColor);
    }

    @Override
    public boolean onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
        boolean switchSection = false;

        if (drawerItem.getIdentifier() != currentDrawerId || drawerItem.getTag() instanceof Mode) {
            switchSection = true;
        }

        if (switchSection) {

            if (drawerItem instanceof PrimaryDrawerItem) {
                if (((PrimaryDrawerItem) drawerItem).isCheckable()) {
                    currentDrawerId = drawerItem.getIdentifier();
                }

                if (drawerItem.getTag() == null) {
                    currentMode = null;
                }
            }

            switch (drawerItem.getIdentifier()) {
                case 0:
                    setFragment(VocableListFragment.newInstance(), getString(R.string.fragment_vocable_list_title));
                    return false;
                case 1:
                    Mode mode = (Mode) drawerItem.getTag();

                    if (currentMode == null || currentMode != mode) {
                        currentMode = mode;

                        setFragment(TestSettingsFragment.newInstance(mode), mode.getTitle(this), mode.getColor(this), mode.getDarkColor(this));
                        return false;
                    } else {
                        return true;
                    }
                case 2:
                    setFragment(StatisticsFragment.newInstance((ArrayList<Mode>) core.getModes()), getString(R.string.fragment_statistics_title));
                    return false;
                case 3:
                    PlayGamesDialog playGamesDialog = PlayGamesDialog.newInstance();

                    playGamesDialog.setCallback(this);
                    playGamesDialog.show(getFragmentManager(), DIALOG_PLAY_GAMES);
                    return true;
                case 4:
                    setFragment(HelpFragment.newInstance((ArrayList<Mode>) core.getModes()), getString(R.string.fragment_help_title));
                    return false;
                case 5:
                    DonateDialog donateDialog = DonateDialog.newInstance();

                    donateDialog.setCallback(this);
                    donateDialog.show(getFragmentManager(), DONATE_DIALOG);
                    return true;
                case 6:
                    setFragment(new SettingsFragment(), getString(R.string.fragment_settings_title));
                    return false;
                default:
                    return true;
            }
        }

        return true;
    }

    @Override
    public void onWelcomeDialogClosed(boolean showAds, boolean enableReminder, boolean signIntoPlayGames) {
        if (showAds) {
            showAds();
        }

        if (enableReminder) {
            PreferenceUtils.setReminder(this, true);
            ReminderUtils.setReminder(this);
        }

        if (signIntoPlayGames) {
            core.getConnection().connect();
        }

        PreferenceUtils.setFirstStarted(this);
    }

    public void hideAds() {
        PreferenceUtils.setAds(this, false);
        adView.setVisibility(View.GONE);
        adView.destroy();
    }

    public void showAds() {
        PreferenceUtils.setAds(this, true);
        adView.setVisibility(View.VISIBLE);
        AdRequest adRequest = new AdRequest.Builder().addKeyword("Vocable").addKeyword("Learning").addKeyword("Game").build();

        adView.loadAd(adRequest);
    }

    public void purchase(String item) {
        try {
            Bundle buyIntentBundle = billingService.getBuyIntent(3, getPackageName(),
                    item, "inapp", null);
            PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
            startIntentSenderForResult(pendingIntent.getIntentSender(),
                    REQUEST_PURCHASE, new Intent(), 0, 0,
                    0);
        } catch (RemoteException | IntentSender.SendIntentException e) {
            e.printStackTrace();
            Toast.makeText(this, getString(R.string.activity_main_donation_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onEvaluate() {
        Utils.showPlayStorePage(this);
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

    @Override
    public void onDonate(String item) {
        purchase(item);
    }

    public interface OnBackPressedListener {

        /**
         * Callback called if the Back Button is pressed.
         * Fragments that extend MainFragment can/should override this Method.
         *
         * @return true if the App can be closed, false otherwise
         */
        boolean onBackPressed();
    }
}
