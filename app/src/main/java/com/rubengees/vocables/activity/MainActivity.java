package com.rubengees.vocables.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;

import com.rubengees.vocables.R;
import com.rubengees.vocables.core.Core;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private FrameLayout content;
    private View toolbarView;

    private Core core;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        content = (FrameLayout) findViewById(R.id.content);

        setSupportActionBar(toolbar);

        core = Core.getInstance(this, savedInstanceState);

        if (savedInstanceState == null) {

        }
    }

    @Override
    protected void onStop() {
        core.onStop();
        super.onStop();
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        core.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onSaveInstanceState(final Bundle outState) {
        super.onSaveInstanceState(outState);
        core.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        core.onStart();
    }

    public void setToolbarView(View view) {

        if (toolbarView != null) {
            toolbar.removeView(toolbarView);
        }

        if (view != null) {
            toolbar.addView(view);
        }

        toolbarView = view;
    }
}
