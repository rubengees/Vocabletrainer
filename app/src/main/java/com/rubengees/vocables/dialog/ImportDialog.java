package com.rubengees.vocables.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rubengees.vocables.R;
import com.rubengees.vocables.utils.ImportTask;

import java.io.File;

/**
 * A dialog showing the progress of an import.
 *
 * @author Ruben Gees
 */
public class ImportDialog extends DialogFragment implements ImportTask.OnImportFinishedListener {

    private static final String KEY_PATH = "path";

    private ImportTask task;
    private ImportTask.OnImportFinishedListener listener;

    public static ImportDialog newInstance(String path) {
        ImportDialog dialog = new ImportDialog();
        Bundle bundle = new Bundle();

        bundle.putString(KEY_PATH, path);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        File file = new File(getArguments().getString(KEY_PATH));

        task = ImportTask.getInstance(getActivity(), file, this);

        task.startIfNotRunning();
    }

    public void setListener(ImportTask.OnImportFinishedListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());

        return builder.title(getString(R.string.import_title)).content(getString(R.string.dialog_import_content))
                .progress(true, 100).negativeText(getString(R.string.dialog_cancel)).callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        task.cancel();
                    }
                }).build();
    }

    @Override
    public void onImportFinished(String result) {
        if (result != null) {
            Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
        } else {
            if (listener != null) {
                listener.onImportFinished(null);
            }
        }
        dismiss();
    }
}
