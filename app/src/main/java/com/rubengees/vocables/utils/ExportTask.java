package com.rubengees.vocables.utils;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;

/**
 * Created by ruben on 29.05.15.
 */
public class ExportTask {

    private static ExportTask instance;

    private File file;
    private OnExportFinishedListener listener;
    private Task task;

    private ExportTask(File file) {
        this.file = file;
    }

    public static ExportTask getInstance(@NonNull File file, @Nullable OnExportFinishedListener listener) {
        if (instance == null) {
            instance = new ExportTask(file);
        }

        instance.setListener(listener);

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
        void onExportFinished(boolean success);
    }

    private class Task extends AsyncTask<File, Void, Boolean> {

        @Override
        protected Boolean doInBackground(File... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            if (listener != null) {
                listener.onExportFinished(result);
            }
        }
    }
}
