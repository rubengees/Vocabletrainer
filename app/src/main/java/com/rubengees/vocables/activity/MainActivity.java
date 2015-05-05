package com.rubengees.vocables.activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.rubengees.vocables.R;
import com.rubengees.vocables.core.Core;
import com.rubengees.vocables.core.mode.Mode;
import com.rubengees.vocables.dialog.EvaluationDialog;
import com.rubengees.vocables.dialog.WelcomeDialog;
import com.rubengees.vocables.fragment.HelpFragment;
import com.rubengees.vocables.fragment.SettingsFragment;
import com.rubengees.vocables.fragment.StatisticsFragment;
import com.rubengees.vocables.fragment.TestSettingsFragment;
import com.rubengees.vocables.fragment.VocableListFragment;
import com.rubengees.vocables.utils.PreferenceUtils;
import com.rubengees.vocables.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements Drawer.OnDrawerItemClickListener {

    private Toolbar toolbar;
    private ViewGroup toolbarExtension;
    private Drawer.Result drawer;

    private Core core;

    private String currentTitle;
    private int currentColor;
    private int currentColorDark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarExtension = (ViewGroup) findViewById(R.id.toolbar_extension);
        core = Core.getInstance(this, savedInstanceState);

        setSupportActionBar(toolbar);

        generateDrawer(savedInstanceState);

        if (savedInstanceState == null) {
            setFragment(VocableListFragment.newInstance(), "Vocablelist");

            showDialog();
        } else {
            styleApplication(savedInstanceState.getString("current_title"), savedInstanceState.getInt("current_color"), savedInstanceState.getInt("current_color_dark"));
        }
    }

    private void showDialog() {
        if (PreferenceUtils.isFirstStart(this)) {
            WelcomeDialog dialog = WelcomeDialog.newInstance();
        } else if (!PreferenceUtils.hasEvaluated(this)) {
            EvaluationDialog dialog = EvaluationDialog.newInstance();
        }
    }

    @Override
    protected void onStop() {
        core.onStop();
        super.onStop();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        core.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        core.onSaveInstanceState(outState);
        drawer.saveInstanceState(outState);

        outState.putString("current_title", currentTitle);
        outState.putInt("current_color", currentColor);
        outState.putInt("current_color_dark", currentColorDark);
    }

    @Override
    protected void onStart() {
        super.onStart();
        core.onStart();
    }

    private void generateDrawer(Bundle savedInstanceState) {
        drawer = new Drawer().withActivity(this).withToolbar(toolbar)
                .withDrawerItems(generateDrawerItems()).withSavedInstance(savedInstanceState).withStickyDrawerItems(generateStickyDrawerItems())
                .withOnDrawerItemClickListener(this).withActionBarDrawerToggleAnimated(true).build();
    }

    private ArrayList<IDrawerItem> generateDrawerItems() {
        ArrayList<IDrawerItem> result = new ArrayList<>();

        result.add(new PrimaryDrawerItem().withName("Vocablelist").withIcon(R.drawable.ic_list).withSelectedTextColorRes(R.color.primary).withSelectedIconColorRes(R.color.primary).withIdentifier(0));
        result.add(new SectionDrawerItem().withName("Modes"));
        result.addAll(generateModeItems());
        result.add(new DividerDrawerItem());
        result.add(new PrimaryDrawerItem().withName("Statistics").withIcon(R.drawable.ic_stats).withSelectedTextColorRes(R.color.primary).withSelectedIconColorRes(R.color.primary).withIdentifier(2));
        result.add(new PrimaryDrawerItem().withName("Play Games").withIcon(R.drawable.ic_play_games).withSelectedTextColorRes(R.color.primary).withSelectedIconColorRes(R.color.primary).withIdentifier(3));

        return result;
    }

    private ArrayList<IDrawerItem> generateStickyDrawerItems() {
        ArrayList<IDrawerItem> result = new ArrayList<>();

        result.add(new SecondaryDrawerItem().withName("Help").withIcon(R.drawable.ic_help).withSelectedTextColorRes(R.color.primary).withSelectedIconColorRes(R.color.primary).withIdentifier(4));
        result.add(new SecondaryDrawerItem().withName("Donate").withIcon(R.drawable.ic_donate).withSelectedTextColorRes(R.color.primary).withSelectedIconColorRes(R.color.primary).withIdentifier(5));
        result.add(new SecondaryDrawerItem().withName("Settings").withIcon(R.drawable.ic_settings).withSelectedTextColorRes(R.color.primary).withSelectedIconColorRes(R.color.primary).withIdentifier(6));
        return result;
    }

    private List<IDrawerItem> generateModeItems() {
        List<IDrawerItem> result = new ArrayList<>();

        for (Mode mode : core.getModes()) {
            int color = mode.getColor(this);

            result.add(new PrimaryDrawerItem().withName(mode.getTitle(this)).withIcon(mode.getIcon(this))
                    .withSelectedTextColor(color).withSelectedIconColor(color).withIdentifier(1).withTag(mode));
        }

        return result;
    }

    public void setFragment(Fragment fragment, String title, int color, int darkColor) {
        getFragmentManager().beginTransaction().replace(R.id.content, fragment).commit();

        setToolbarView(null, 0);
        styleApplication(title, color, darkColor);
    }

    public void setFragment(Fragment fragment, String title) {
        int color = getResources().getColor(R.color.primary);
        int darkColor = getResources().getColor(R.color.primary_dark);

        setFragment(fragment, title, color, darkColor);
    }

    public void setToolbarView(@Nullable View view, int color) {

        toolbarExtension.removeAllViews();

        if (view != null) {
            toolbarExtension.addView(view);
            toolbarExtension.setBackgroundColor(color);
        }
    }

    public void styleApplication(String title, int color, int darkColor) {

        currentTitle = title;
        currentColor = color;
        currentColorDark = darkColor;

        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(title);
            ab.setSubtitle(null);
        }

        toolbar.setBackgroundColor(color);
        Utils.colorWindow(this, color, darkColor);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {

        switch (drawerItem.getIdentifier()) {
            case 0:
                setFragment(VocableListFragment.newInstance(), "Vocablelist");
                break;
            case 1:
                Mode mode = (Mode) drawerItem.getTag();

                setFragment(TestSettingsFragment.newInstance(mode), mode.getTitle(this), mode.getColor(this), mode.getDarkColor(this));
                break;
            case 2:
                setFragment(StatisticsFragment.newInstance(), "Statistics");
                break;
            case 3:
                break;
            case 4:
                setFragment(HelpFragment.newInstance(), "Help");
                break;
            case 5:
                break;
            case 6:
                setFragment(new SettingsFragment(), "Settings");
                break;
        }
    }
}
