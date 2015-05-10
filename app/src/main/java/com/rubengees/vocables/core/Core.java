package com.rubengees.vocables.core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.rubengees.vocables.core.mode.Mode;
import com.rubengees.vocables.data.VocableManager;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import dalvik.system.DexFile;

/**
 * Created by Ruben on 24.04.2015.
 */
public class Core {

    private static Core ourInstance;
    private GoogleServiceConnection connection;
    private VocableManager vocableManager;
    private List<Mode> modes;
    private Activity context;

    private Core(Activity context, Bundle savedInstanceState) {
        this.context = context;
        this.connection = new GoogleServiceConnection(context, savedInstanceState);
        this.vocableManager = new VocableManager(context);
        modes = new ArrayList<>(3);
        try {
            generateModes();
        } catch (IOException | ClassNotFoundException | InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
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

    private void generateModes() throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        DexFile df = new DexFile(context.getPackageCodePath());

        for (Enumeration<String> iter = df.entries(); iter.hasMoreElements(); ) {
            String s = iter.nextElement();

            if (s.contains("com.rubengees.vocables.core.mode") && !(s.substring(s.lastIndexOf(".") + 1, s.length()).equals("Mode"))) {
                Constructor c = Class.forName(s).getConstructor(Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE);
                Mode mode = (Mode) c.newInstance(0, 0, 0, 0, 0, 0);

                modes.add(mode);
            }
        }
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
