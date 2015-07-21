package com.rubengees.vocables.core;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.rubengees.vocables.R;
import com.rubengees.vocables.core.mode.ClassicMode;
import com.rubengees.vocables.core.mode.Mode;
import com.rubengees.vocables.core.mode.ModeData;
import com.rubengees.vocables.core.mode.PairMode;
import com.rubengees.vocables.core.mode.TimeMode;
import com.rubengees.vocables.core.mode.TrainingMode;
import com.rubengees.vocables.data.Database;
import com.rubengees.vocables.data.UndoManager;
import com.rubengees.vocables.data.VocableManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Ruben on 24.04.2015.
 */
public class Core {

    private static Core ourInstance;
    private GoogleServiceConnection connection;
    private VocableManager vocableManager;
    private BillingProcessor billingProcessor;
    private UndoManager undoManager;
    private List<Mode> modes;
    private Activity context;

    private Core(Activity context, Bundle savedInstanceState) {
        this.context = context;
        this.connection = new GoogleServiceConnection(context, savedInstanceState);
        this.vocableManager = new VocableManager(context);
        this.undoManager = new UndoManager();

        initBilling();

        modes = new ArrayList<>(4);
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

    private void initBilling() {
        this.billingProcessor = new BillingProcessor(context, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApdRQ/21yH5x2NeNC/9SwT1k+MJxXsahs9xlMBRv+ExkruoyAtjEqj9tQr2FHTl/AcEah0V+8OwJP20dhQm0j7zrZx7PNB/s39zJypUlv4" +
                "h1DyFC0LvMRnLoyfVfPNZN5eK9Z9Bbd1poLRob0ncRbYLBRkAtwW27Js4I6pI9v7CO5xdra6skK62soZNXyD/r0KsGbHJdCrWDj8CDh4K94LgRIXH8bUwwggMUR0ANZQ80bi" +
                "WfTLRMN1XsWz5X7nMD2pKo6LJZ48uyCTYAdc4lemhAsXLh3rbR9l4/rWKxettAtd/zNR2N/iZTQhs6XqBXuY1Eo6VRKn7ISoqA571iH9wIDAQAB", new BillingProcessor.IBillingHandler() {
            @Override
            public void onProductPurchased(String productId, TransactionDetails transactionDetails) {
                if (context != null) {
                    Toast.makeText(context, context.getString(R.string.activity_main_donation_message), Toast.LENGTH_SHORT).show();
                }
                if (billingProcessor.isInitialized()) {
                    billingProcessor.consumePurchase(productId);
                }
            }

            @Override
            public void onPurchaseHistoryRestored() {

            }

            @Override
            public void onBillingError(int errorCode, Throwable error) {
                if (context != null) {
                    Toast.makeText(context, context.getString(R.string.activity_main_donation_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onBillingInitialized() {

            }
        });
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

        if (data.containsKey(3)) {
            modes.add(new TrainingMode(data.get(3)));
        } else {
            modes.add(new TrainingMode(new ModeData(3, 0, 0, 0, 0, 0, 0)));
        }

        db.close();
    }

    public void saveMode(Mode mode) {
        Database db = new Database(context);

        db.update(mode);

        db.close();
    }

    public void donate(String item) {
        if (billingProcessor.isInitialized()) {
            billingProcessor.purchase(context, item);
        }
    }

    public List<Mode> getModes() {
        return new ArrayList<>(modes);
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

    public void onDestroy() {
        if (billingProcessor != null) {
            billingProcessor.release();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!billingProcessor.handleActivityResult(requestCode, resultCode, data)) {
            connection.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onSaveInstanceState(Bundle outState) {
        connection.onSaveInstanceState(outState);
    }
}
