package com.rubengees.vocables.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rubengees.vocables.R;
import com.rubengees.vocables.dialog.OverrideDialog;
import com.rubengees.vocables.dialog.PermissionExplanationDialog;
import com.rubengees.vocables.fragment.FileFragment;
import com.rubengees.vocables.utils.SnackbarManager;
import com.rubengees.vocables.utils.TransferUtils;

import java.io.File;

public class TransferActivity extends ExtendedToolbarActivity implements FileFragment.FileFragmentListener, ExtendedToolbarActivity.OnFabClickListener {

    public static final int REQUEST_IMPORT = 10010;
    public static final int REQUEST_EXPORT = 10011;
    public static final int PERMISSION_WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 5000;
    private static final String OVERRIDE_DIALOG = "override_dialog";
    private static final String FRAGMENT_TRANSFER = "transfer_fragment";
    private static final String PERMISSION_DIALOG = "permission_dialog";

    private boolean isImport;

    private FileFragment fragment;
    private PermissionExplanationDialog.PermissionExplanationDialogCallback permissionExplanationDialogCallback = new PermissionExplanationDialog.PermissionExplanationDialogCallback() {
        @Override
        public void onDialogClosed() {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        }
    };
    private OverrideDialog.OverrideDialogCallback overrideDialogCallback = new OverrideDialog.OverrideDialogCallback() {
        @Override
        public void onOverride(File file) {
            Intent in = new Intent();
            in.putExtra("path", file.getAbsolutePath());

            setResult(RESULT_OK, in);
            finish();
        }
    };

    @Override
    public void init(Bundle savedInstanceState) {
        isImport = getIntent().getBooleanExtra("isImport", true);

        ActionBar ab = getSupportActionBar();

        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }

        if (isImport) {
            setTitle(getString(R.string.import_title));
        } else {
            setTitle(getString(R.string.export_title));
        }

        if (savedInstanceState == null) {
            tryShowContent();
        } else {
            FileFragment fragment = (FileFragment) getFragmentManager().
                    findFragmentByTag(FRAGMENT_TRANSFER);
            OverrideDialog overrideDialog = (OverrideDialog) getFragmentManager().
                    findFragmentByTag(OVERRIDE_DIALOG);
            PermissionExplanationDialog permissionExplanationDialog = (PermissionExplanationDialog)
                    getFragmentManager().findFragmentByTag(PERMISSION_DIALOG);

            if (fragment != null) {
                configureFragment(fragment);
            } else {
                showSnackbar();
            }

            if (overrideDialog != null) {
                overrideDialog.setCallback(overrideDialogCallback);
            }

            if (permissionExplanationDialog != null) {
                permissionExplanationDialog.setCallback(permissionExplanationDialogCallback);
            }

        }

        styleApplicationRes(R.color.primary, R.color.primary_dark);
    }

    @Override
    protected void inflateLayout(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        inflater.inflate(R.layout.activity_transfer, container, true);
    }

    private void showSnackbar() {
        SnackbarManager.show(Snackbar.make(findViewById(R.id.content), getString(R.string.activity_transfer_error_sd), Snackbar.LENGTH_INDEFINITE),
                getString(R.string.activity_transfer_error_retry), new SnackbarManager.SnackbarCallback() {
                    @Override
                    public void onClick(View v) {
                        tryShowContent();
                    }
                });
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

        if (!hasPermission()) {
            requestPermission();
        } else {
            if (checkAccess()) {
                fragment = FileFragment.newInstance(Environment.getExternalStorageDirectory().getPath());
            } else {
                showSnackbar();
            }

            if (fragment != null) {
                configureFragment(fragment);
                getFragmentManager().beginTransaction().add(R.id.content, fragment, FRAGMENT_TRANSFER).commit();
            }
        }
    }

    private void requestPermission() {
        if (shouldShowRequestPermissionRationale(
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            PermissionExplanationDialog dialog = PermissionExplanationDialog.newInstance(
                    getString(R.string.dialog_permission_explanation_write_external_storage));

            dialog.setCallback(permissionExplanationDialogCallback);
            dialog.show(getFragmentManager(), PERMISSION_DIALOG);
        } else {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSION_WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        }
    }

    private void configureFragment(@NonNull FileFragment fragment) {
        fragment.setFileEventListener(this);
        this.fragment = fragment;
        expandToolbar();

        if (!isImport) {
            enableFab(R.drawable.ic_save, this);
        }
    }

    private boolean checkAccess() {
        String state = Environment.getExternalStorageState();

        return state.equals(Environment.MEDIA_MOUNTED) ||
                state.equals(Environment.MEDIA_MOUNTED_READ_ONLY) && isImport;
    }

    private boolean hasPermission() {
        return checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onFileClicked(@NonNull File file) {
        if (isImport && TransferUtils.isFileSupported(file)) {
            Intent in = new Intent();
            in.putExtra("path", file.getAbsolutePath());

            setResult(RESULT_OK, in);
            finish();
        } else {
            Toast.makeText(this, getString(R.string.activity_transfer_error_file_type),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onFabClick() {
        if (fragment != null) {
            File current = new File(fragment.getCurrentDirectory() + "/" + "backup.xml");
            if (current.exists()) {
                OverrideDialog dialog = OverrideDialog.newInstance(current.getAbsolutePath());

                dialog.setCallback(overrideDialogCallback);
                dialog.show(getFragmentManager(), OVERRIDE_DIALOG);
            } else {
                Intent in = new Intent();
                in.putExtra("path", current.getAbsolutePath());

                setResult(RESULT_OK, in);
                finish();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_WRITE_EXTERNAL_STORAGE_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    tryShowContent();
                } else {
                    showSnackbar();
                }
                break;
        }
    }
}
