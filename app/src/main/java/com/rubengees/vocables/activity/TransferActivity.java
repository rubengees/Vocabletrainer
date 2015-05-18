package com.rubengees.vocables.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rubengees.vocables.R;

import java.io.File;

public class TransferActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        boolean isImport = getIntent().getBooleanExtra("isImport", true);
    }

    public interface OnFinishedListener {
        void onImport(File file);

        void onExport();
    }
}
