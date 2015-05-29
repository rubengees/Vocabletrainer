package com.rubengees.vocables.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.rubengees.vocables.R;
import com.rubengees.vocables.fragment.FileFragment;

import java.io.File;

public class TransferActivity extends ExtendedToolbarActivity implements FileFragment.FileFragmentListener, ExtendedToolbarActivity.OnFabClickListener {

    public static final int REQUEST_IMPORT = 10010;
    public static final int REQUEST_EXPORT = 10011;

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

        styleApplicationRes(R.color.primary, R.color.primary_dark);
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
        expandToolbar(false);

        if (!isImport) {
            //TODO add drawable ic_save
            enableFab(R.drawable.ic_save, this);
        }
    }

    private boolean checkAccess() {
        String state = Environment.getExternalStorageState();

        return state.equals(Environment.MEDIA_MOUNTED) || state.equals(Environment.MEDIA_MOUNTED_READ_ONLY) && isImport;
    }

    @Override
    public void onFileClicked(File file) {
        Intent in = new Intent();
        in.putExtra("path", file.getAbsolutePath());

        setResult(RESULT_OK, in);
        finish();
    }

    @Override
    public void onFabClick() {
        if (fragment != null) {
            File current = fragment.getCurrentDirectory();
            Intent in = new Intent();
            in.putExtra("path", current.getAbsolutePath());

            setResult(RESULT_OK, in);
            finish();
        }
    }
}
