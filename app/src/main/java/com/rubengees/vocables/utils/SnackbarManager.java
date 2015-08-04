package com.rubengees.vocables.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by Ruben on 18.07.2015.
 */
public class SnackbarManager {

    private static Snackbar current;

    public static void show(@NonNull final Snackbar snackbar, @Nullable String actionTitle, final @Nullable SnackbarCallback callback) {
        dismiss();
        current = snackbar;

        if (actionTitle != null && callback != null) {
            snackbar.setAction(actionTitle, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    current = null;

                    callback.onClick(v);
                }
            });
        }

        snackbar.getView().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {

            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                current = null;

                if (callback != null) {
                    callback.onDismiss(v);
                }
            }
        });

        snackbar.show();
    }

    public static void dismiss() {
        if (current != null) {
            current.dismiss();
        }

        current = null;
    }

    public static boolean hasSnackbar() {
        return current != null;
    }

    public static void update(@NonNull String text) {
        current.setText(text);
    }

    public static abstract class SnackbarCallback {
        public void onDismiss(View v) {

        }

        public void onClick(View v) {

        }
    }
}
