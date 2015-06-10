package com.rubengees.vocables.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rubengees.vocables.R;
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

        builder.title(getActivity().getString(R.string.dialog_play_games_title));
        if (connection.isConnected()) {
            builder.positiveText(getActivity().getString(R.string.dialog_play_games_sign_out))
                    .neutralText(getActivity().getString(R.string.dialog_play_games_show_achievements)).callback(new MaterialDialog.ButtonCallback() {
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
            }).content(getActivity().getString(R.string.dialog_play_games_content_singed_in));
        } else {
            builder.positiveText(getActivity().getString(R.string.dialog_play_games_sign_in)).callback(new MaterialDialog.ButtonCallback() {
                @Override
                public void onPositive(MaterialDialog dialog) {
                    if (callback != null) {
                        callback.onSignIn();
                    }
                }
            }).content(getActivity().getString(R.string.dialog_play_games_content_signed_out));
        }
        builder.negativeText(getActivity().getString(R.string.dialog_cancel));

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
