package com.rubengees.vocables.core;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.rubengees.vocables.data.VocableManager;

/**
 * Created by Ruben on 24.04.2015.
 */
public class Core {
    private static Core ourInstance;
    private GoogleServiceConnection connection;
    private VocableManager vocableManager;
    private Activity context;

    private Core(Activity context, Bundle savedInstanceState) {
        this.context = context;
        this.connection = new GoogleServiceConnection(context, savedInstanceState);
        this.vocableManager = new VocableManager(context);
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

    public GoogleServiceConnection getConnection(){
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
