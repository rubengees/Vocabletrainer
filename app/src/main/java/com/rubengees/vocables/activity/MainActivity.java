package com.rubengees.vocables.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.rubengees.vocables.R;
import com.rubengees.vocables.core.Core;
import com.rubengees.vocables.core.mode.Mode;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FrameLayout content;
    private Drawer.Result drawer;

    private Core core;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        content = (FrameLayout) findViewById(R.id.content);

        setSupportActionBar(toolbar);

        core = Core.getInstance(this, savedInstanceState);

        initDrawer(savedInstanceState);
    }

    private void initDrawer(Bundle savedInstanceState) {
        drawer = new Drawer().withActivity(this).withToolbar(toolbar).withActionBarDrawerToggleAnimated(true).withDrawerItems(generateDrawerItems())
                .withStickyDrawerItems(generateStickyDrawerItems()).withSavedInstance(savedInstanceState).build();
    }

    private ArrayList<IDrawerItem> generateStickyDrawerItems() {
        ArrayList<IDrawerItem> result = new ArrayList<>();

        result.add(new PrimaryDrawerItem().withName("Help").withIcon(R.drawable.ic_help).withSelectedIconColorRes(R.color.primary).withTintSelectedIcon(true));
        result.add(new PrimaryDrawerItem().withName("Donate").withIcon(R.drawable.ic_donate).withCheckable(false));
        result.add(new PrimaryDrawerItem().withName("Settings").withIcon(R.drawable.ic_settings).withSelectedIconColorRes(R.color.primary).withTintSelectedIcon(true));

        return result;
    }

    private ArrayList<IDrawerItem> generateDrawerItems() {
        ArrayList<IDrawerItem> result = new ArrayList<>();

        result.add(new PrimaryDrawerItem().withName("Vocablelist").withIcon(R.drawable.ic_list).withSelectedIconColorRes(R.color.primary).withTintSelectedIcon(true));
        result.add(new SectionDrawerItem().withName("Modes"));

        for (Mode mode : core.getModes()) {
            result.add(new PrimaryDrawerItem().withName(mode.getTitle(this)).withIcon(mode.getIcon(this)).withSelectedIconColor(mode.getColor(this))
                    .withSelectedTextColor(mode.getColor(this)).withTintSelectedIcon(true));
        }

        result.add(new DividerDrawerItem());
        result.add(new PrimaryDrawerItem().withName("Statistics").withIcon(R.drawable.ic_stats).withSelectedIconColorRes(R.color.primary).withTintSelectedIcon(true));
        result.add(new PrimaryDrawerItem().withName("Play Games").withIcon(R.drawable.ic_play_games).withCheckable(false));

        return result;
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
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        core.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        drawer.saveInstanceState(outState);
        core.onSaveInstanceState(outState);
    }
}
