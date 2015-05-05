package com.rubengees.vocables.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

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
        final CheckBox ads = (CheckBox) view.findViewById(R.id.dialog_welcome_ads);
        final CheckBox reminder = (CheckBox) view.findViewById(R.id.dialog_welcome_reminder);
        final CheckBox playGames = (CheckBox) view.findViewById(R.id.dialog_welcome_play_games);

        builder.title("Welcome!").customView(view, true).positiveText("Let's go!").callback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                super.onPositive(dialog);
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
