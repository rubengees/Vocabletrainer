package com.rubengees.vocables.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.rubengees.vocables.utils.PreferenceUtils;
import com.rubengees.vocables.utils.ReminderUtils;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            if (PreferenceUtils.isReminderEnabled(context)) {
                ReminderUtils.setReminder(context);
            } else {
                ReminderUtils.cancelReminder(context);
            }
        }
    }
}
