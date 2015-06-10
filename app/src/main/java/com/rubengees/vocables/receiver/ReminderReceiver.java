package com.rubengees.vocables.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.rubengees.vocables.R;
import com.rubengees.vocables.activity.MainActivity;

public class ReminderReceiver extends BroadcastReceiver {

    private static final int NOTIFICATION_ID = 101;

    public ReminderReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setContentTitle(context.getString(R.string.app_name));
        builder.setContentText(context.getString(R.string.notification_reminder_content));
        builder.setSmallIcon(R.drawable.ic_stat_reminder);
        builder.setAutoCancel(true).setDefaults(Notification.DEFAULT_ALL);

        Intent resultIntent = new Intent(context, MainActivity.class);
        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(context, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentIntent(resultPendingIntent);

        NotificationManager manager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        manager.notify(NOTIFICATION_ID, builder.build());
    }
}
