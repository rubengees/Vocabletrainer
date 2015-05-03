package com.rubengees.vocables.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.rubengees.vocables.R;

/**
 * Created by Ruben on 02.05.2015.
 */
public class WelcomeDialog extends DialogFragment {

    private WelcomeDialogCallback callback;

    public static WelcomeDialog newInstance() {
        return new WelcomeDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        MaterialDialog.Builder builder = new MaterialDialog.Builder(getActivity());

        View view = View.inflate(getActivity(), R.layout.dialog_welcome, null);
        final AppCompatCheckBox ads = (AppCompatCheckBox) view.findViewById(R.id.dialog_welcome_ads);
        final AppCompatCheckBox reminder = (AppCompatCheckBox) view.findViewById(R.id.dialog_welcome_reminder);
        final AppCompatCheckBox playGames = (AppCompatCheckBox) view.findViewById(R.id.dialog_welcome_play_games);

        builder.title("Welcome!").customView(view, true).positiveText(getActivity().getString(android.R.string.ok)).dismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (callback != null) {
                    callback.onWelcomeDialogClosed(ads.isChecked(), reminder.isChecked(), playGames.isChecked());
                }
            }
        });

        return builder.build();

    }

    public void setCallback(WelcomeDialogCallback callback) {
        this.callback = callback;
    }

    public interface WelcomeDialogCallback {
        void onWelcomeDialogClosed(boolean showAds, boolean enableReminder, boolean signIntoPlayGames);
    }
}
