package com.rubengees.vocables.core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.rubengees.vocables.core.mode.ClassicMode;
import com.rubengees.vocables.core.mode.Mode;
import com.rubengees.vocables.core.mode.ModeData;
import com.rubengees.vocables.core.mode.PairMode;
import com.rubengees.vocables.core.mode.TimeMode;
import com.rubengees.vocables.data.Database;
import com.rubengees.vocables.data.UndoManager;
import com.rubengees.vocables.data.VocableManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Ruben on 24.04.2015.
 */
public class Core {

    private static Core ourInstance;
    private final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    private GoogleServiceConnection connection;
    private VocableManager vocableManager;
    private UndoManager undoManager;
    private List<Mode> modes;
    private Activity context;

    private Core(Activity context, Bundle savedInstanceState) {
        this.context = context;
        this.connection = new GoogleServiceConnection(context, savedInstanceState);
        this.vocableManager = new VocableManager(context);
        this.undoManager = new UndoManager();
        modes = new ArrayList<>(3);
        generateModes();
    }

    public static Core getInstance(Activity context, Bundle savedInstanceState) {
        if (ourInstance == null) {
            ourInstance = new Core(context, savedInstanceState);
        }

        return ourInstance;
    }

    public static Core getInstance(Activity context) {
        if (ourInstance == null) {
            ourInstance = new Core(context, null);
        }

        return ourInstance;
    }

    public int generateViewId() {
        for (; ; ) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    private void generateModes() {
        Database db = new Database(context);
        HashMap<Integer, ModeData> data = db.getModes();

        if (data.containsKey(0)) {
            modes.add(new ClassicMode(data.get(0)));
        } else {
            modes.add(new ClassicMode(new ModeData(0, 0, 0, 0, 0, 0, 0)));
        }

        if (data.containsKey(1)) {
            modes.add(new PairMode(data.get(1)));
        } else {
            modes.add(new PairMode(new ModeData(1, 0, 0, 0, 0, 0, 0)));
        }

        if (data.containsKey(2)) {
            modes.add(new TimeMode(data.get(2)));
        } else {
            modes.add(new TimeMode(new ModeData(2, 0, 0, 0, 0, 0, 0)));
        }

        db.close();
    }

    public void saveMode(Mode mode) {
        Database db = new Database(context);

        db.update(mode);

        db.close();
    }

    public List<Mode> getModes() {
        return modes;
    }

    public GoogleServiceConnection getConnection() {
        return connection;
    }

    public VocableManager getVocableManager() {
        return vocableManager;
    }

    public UndoManager getUndoManager() {
        return undoManager;
    }

    public void onStart() {
        connection.onStart();
    }

    public void onStop() {
        connection.onStop();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        connection.onActivityResult(requestCode, resultCode, data);
    }

    public void onSaveInstanceState(Bundle outState) {
        connection.onSaveInstanceState(outState);
    }
}
