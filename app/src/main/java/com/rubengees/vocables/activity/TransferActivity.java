package com.rubengees.vocables.activity;

import android.app.ActionBar;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;

import com.melnykov.fab.FloatingActionButton;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.rubengees.vocables.R;
import com.rubengees.vocables.fragment.ExportFragment;
import com.rubengees.vocables.fragment.ImportFragment;
import com.rubengees.vocables.fragment.TransferFragment;
import com.rubengees.vocables.utils.DrawableType;
import com.rubengees.vocables.utils.Utils;

import java.io.File;

public class TransferActivity extends AppCompatActivity implements View.OnClickListener, TransferFragment.OnFinishedListener {

    private Toolbar toolbar;
    private ViewGroup toolbarExtension;
    private FloatingActionButton fab;

    private boolean isImport;

    private OnSaveClickedListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarExtension = (ViewGroup) findViewById(R.id.toolbar_extension);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        toolbar.bringToFront();
        setSupportActionBar(toolbar);

        isImport = getIntent().getBooleanExtra("isImport", true);

        ActionBar ab = getActionBar();

        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            tryShowContent();
        }
    }

    private void showSnackbar() {
        SnackbarManager.show(Snackbar.with(this).duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
                .text("Could not access SD-Card").actionLabel("Retry").actionListener(new ActionClickListener() {
                    @Override
                    public void onActionClicked(Snackbar snackbar) {

                    }
                }));
    }

    private void tryShowContent() {
        String state = Environment.getExternalStorageState();
        TransferFragment fragment = null;
        if (isImport) {
            if (state.equals(Environment.MEDIA_MOUNTED_READ_ONLY) || state.equals(Environment.MEDIA_MOUNTED)) {
                fragment = ImportFragment.newInstance(Environment.getExternalStorageDirectory().getPath());
            } else {
                showSnackbar();
            }
        } else {
            if (state.equals(Environment.MEDIA_MOUNTED)) {
                fragment = ExportFragment.newInstance(Environment.getExternalStorageDirectory().getPath());
                enableFab((OnSaveClickedListener) fragment, Utils.generateDrawable(this, GoogleMaterial.Icon.gmd_save, DrawableType.GENERIC, android.R.color.white));
            } else {
                showSnackbar();
            }
        }

        if (fragment != null) {
            fragment.setListener(this);
            getFragmentManager().beginTransaction().add(R.id.content, fragment, "fragment_transfer").commit();
        }
    }

    public void enableFab(OnSaveClickedListener listener, Drawable icon) {
        this.listener = listener;

        fab.setImageDrawable(icon);
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
        if (listener != null) {
            listener.onSaveClicked();
        }
    }

    @Override
    public void onImport(File file) {

    }

    @Override
    public void onExport(File file) {

    }

    public interface OnSaveClickedListener {
        void onSaveClicked();
    }
}
