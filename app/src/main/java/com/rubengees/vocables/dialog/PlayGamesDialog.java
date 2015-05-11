package com.rubengees.vocables.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rubengees.vocables.core.Core;
import com.rubengees.vocables.core.GoogleServiceConnection;

/**
 * Created by Ruben on 11.05.2015.
 */
public class PlayGamesDialog extends DialogFragment {

    private PlayGamesDialogCallback callback;

    public static PlayGamesDialog newInstance() {
        return new PlayGamesDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());
        GoogleServiceConnection connection = Core.getInstance(getActivity()).getConnection();

        builder.title("Play Games");
        if (connection.isConnected()) {
            builder.positiveText("Sign Out").neutralText("Show Achievements").callback(new MaterialDialog.ButtonCallback() {
                @Override
                public void onPositive(MaterialDialog dialog) {
                    if (callback != null) {
                        callback.onSignOut();
                    }
                }

                @Override
                public void onNeutral(MaterialDialog dialog) {
                    if (callback != null) {
                        callback.onShowAchievements();
                    }
                }
            }).content("Do you want to Sign Out or ciew your Achievements?");
        } else {
            builder.positiveText("Sign In").callback(new MaterialDialog.ButtonCallback() {
                @Override
                public void onPositive(MaterialDialog dialog) {
                    if (callback != null) {
                        callback.onSignIn();
                    }
                }
            }).content("Do you want to sign in?");
        }
        builder.negativeText("Cancel");

        return builder.build();
    }

    public void setCallback(PlayGamesDialogCallback callback) {
        this.callback = callback;
    }

    public interface PlayGamesDialogCallback {
        void onSignIn();

        void onSignOut();

        void onShowAchievements();
    }
}
