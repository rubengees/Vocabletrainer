package com.rubengees.vocables.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.melnykov.fab.FloatingActionButton;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.rubengees.vocables.R;
import com.rubengees.vocables.fragment.FileFragment;

import java.io.File;

public class TransferActivity extends ExtendedToolbarActivity implements FileFragment.FileFragmentListener, View.OnClickListener {

    private ViewGroup toolbarExtension;
    private FloatingActionButton fab;

    private boolean isImport;

    private FileFragment fragment;

    @Override
    public void init(Bundle savedInstanceState) {
        isImport = getIntent().getBooleanExtra("isImport", true);

        ActionBar ab = getSupportActionBar();

        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            tryShowContent();
        } else {
            FileFragment fragment = (FileFragment) getFragmentManager().findFragmentByTag("fragment_transfer");

            if (fragment != null) {
                configureFragment(fragment);
            } else {
                showSnackbar();
            }
        }
    }

    @Override
    protected void inflateLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    }

    private void showSnackbar() {
        SnackbarManager.show(Snackbar.with(this).duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
                .text("Could not access SD-Card").actionLabel("Retry").actionListener(new ActionClickListener() {
                    @Override
                    public void onActionClicked(Snackbar snackbar) {
                        tryShowContent();
                    }
                }));
    }

    @Override
    public void onBackPressed() {
        if (fragment != null) {
            if (fragment.onBackPressed()) {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    private void tryShowContent() {
        FileFragment fragment = null;

        if (checkAccess()) {
            fragment = FileFragment.newInstance(Environment.getExternalStorageDirectory().getPath());
        } else {
            showSnackbar();
        }

        if (fragment != null) {
            configureFragment(fragment);
            getFragmentManager().beginTransaction().add(R.id.content, fragment, "fragment_transfer").commit();
        }
    }

    private void configureFragment(@NonNull FileFragment fragment) {
        fragment.setFileEventListener(this);
        this.fragment = fragment;

        if (!isImport) {
            enableFab();
        }
    }

    private boolean checkAccess() {
        String state = Environment.getExternalStorageState();

        return state.equals(Environment.MEDIA_MOUNTED) || state.equals(Environment.MEDIA_MOUNTED_READ_ONLY) && isImport;
    }

    private void enableFab() {
        fab.setImageResource(R.drawable.ic_save);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(this);
    }


    public void setToolbarView(@Nullable View view) {
        toolbarExtension.removeAllViews();

        if (view != null) {
            toolbarExtension.addView(view);
        }
    }

    @Override
    public void onClick(View v) {
        if (fragment != null) {
            fragment.getCurrentDirectory();

            //TODO export
        }
    }

    @Override
    public void onFileClicked(File file) {
        //TODO import
    }
}
