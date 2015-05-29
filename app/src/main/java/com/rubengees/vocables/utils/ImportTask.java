package com.rubengees.vocables.utils;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.rubengees.vocables.pojo.Unit;

import java.io.File;
import java.util.List;

/**
 * Created by ruben on 29.05.15.
 */
public class ImportTask {

    private static ImportTask instance;

    private File file;
    private OnImportFinishedListener listener;
    private Task task;

    private ImportTask(File file) {
        this.file = file;
    }

    public static ImportTask getInstance(@NonNull File file, @Nullable OnImportFinishedListener listener) {
        if (instance == null) {
            instance = new ImportTask(file);
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

    public void setListener(OnImportFinishedListener listener) {
        this.listener = listener;
    }

    public void cancel() {
        task.cancel(true);
    }

    public interface OnImportFinishedListener {
        void onImportFinished(List<Unit> units);
    }

    private class Task extends AsyncTask<File, Void, List<Unit>> {

        @Override
        protected List<Unit> doInBackground(File... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Unit> result) {
            super.onPostExecute(result);

            if (listener != null) {
                listener.onImportFinished(result);
            }
        }
    }

}
