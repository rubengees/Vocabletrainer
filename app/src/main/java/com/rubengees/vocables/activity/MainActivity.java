package com.rubengees.vocables.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.rubengees.vocables.R;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FrameLayout content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        content = (FrameLayout) findViewById(R.id.content);

        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {

        }
    }

}
