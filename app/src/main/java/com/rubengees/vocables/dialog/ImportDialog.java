package com.rubengees.vocables.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rubengees.vocables.pojo.Unit;
import com.rubengees.vocables.utils.ImportTask;

import java.io.File;
import java.util.List;

/**
 * Created by ruben on 29.05.15.
 */
public class ImportDialog extends DialogFragment implements ImportTask.OnImportFinishedListener {

    private ImportTask task;
    private ImportTask.OnImportFinishedListener listener;

    private File file;
    private MaterialDialog dialog;

    public static ImportDialog newInstance(String path) {
        ImportDialog dialog = new ImportDialog();
        Bundle bundle = new Bundle();

        bundle.putString("path", path);
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        file = new File(getArguments().getString("path"));

        task = ImportTask.getInstance(file, this);

        task.startIfNotRunning();
    }

    public void setListener(ImportTask.OnImportFinishedListener listener) {
        this.listener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        dialog = builder.title("Import").content("Importing...").progress(true, 100).negativeText("Cancel").callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onNegative(MaterialDialog dialog) {
                super.onNegative(dialog);
                task.cancel();
            }
        }).build();

        return dialog;
    }

    @Override
    public void onImportFinished(List<Unit> units) {
        if (units == null) {
            Toast.makeText(getActivity(), "Import failed", Toast.LENGTH_SHORT).show();
        } else {
            if (listener != null) {
                listener.onImportFinished(units);
            }
        }
    }
}
