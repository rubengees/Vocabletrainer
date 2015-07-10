package com.rubengees.vocables.utils;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.rubengees.vocables.R;
import com.rubengees.vocables.core.Core;

import java.io.File;
import java.io.IOException;

/**
 * Created by ruben on 29.05.15.
 */
public class ExportTask {

    private static ExportTask instance;
    private final Context context;

    private File file;
    private OnExportFinishedListener listener;
    private Task task;

    private ExportTask(Context context, File file) {
        this.context = context;
        this.file = file;
    }

    public static ExportTask getInstance(Context context, @NonNull File file, @Nullable OnExportFinishedListener listener) {
        if (instance == null) {
            instance = new ExportTask(context, file);
        }

        instance.listener = listener;

        return instance;
    }

    public void startIfNotRunning() {
        if (task == null || task.getStatus() != AsyncTask.Status.RUNNING) {
            task = new Task();
            task.execute(file);
        }
    }

    public void setListener(OnExportFinishedListener listener) {
        this.listener = listener;
    }

    public void cancel() {
        task.cancel(true);
    }

    public interface OnExportFinishedListener {
        void onExportFinished(String success);
    }

    private class Task extends AsyncTask<File, Void, String> {

        @Override
        protected String doInBackground(File... params) {
            try {
                TransferUtils.export(Core.getInstance((Activity) context).getVocableManager().getUnitList(), params[0]);

                return null;
            } catch (IOException e) {
                return context.getString(R.string.export_error_storage);
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (listener != null) {
                listener.onExportFinished(result);
            }
        }
    }
}
