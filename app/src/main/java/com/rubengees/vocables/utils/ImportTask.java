package com.rubengees.vocables.utils;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.rubengees.vocables.core.Core;
import com.rubengees.vocables.pojo.Unit;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by ruben on 29.05.15.
 */
public class ImportTask {

    private static ImportTask instance;

    private Context context;

    private File file;
    private OnImportFinishedListener listener;
    private Task task;

    private ImportTask(Context context, File file) {
        this.context = context;
        this.file = file;
    }

    public static ImportTask getInstance(Context context, @NonNull File file, @Nullable OnImportFinishedListener listener) {
        if (instance == null) {
            instance = new ImportTask(context, file);
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
        void onImportFinished(String result);
    }

    private class Task extends AsyncTask<File, Void, String> {

        @Override
        protected String doInBackground(File... params) {
            try {
                List<Unit> units = TransferUtils.getList(context, params[0]);
                Core.getInstance((Activity) context).getVocableManager().addUnits(units);

                return null;
            } catch (TransferUtils.FormatException e) {
                return e.getMessage();
            } catch (IOException e) {
                return "Import failed: There was a problem with the storage";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (listener != null) {
                listener.onImportFinished(result);
            }
        }
    }

}
