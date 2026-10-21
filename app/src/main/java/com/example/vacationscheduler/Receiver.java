package com.example.vacationscheduler;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class Receiver extends BroadcastReceiver {
    String channel_id = "vacation_channel";

    @Override
    public void onReceive(Context context, Intent intent){


        String message = intent.getStringExtra("message");

        NotificationChannel channel = new NotificationChannel(
                channel_id,
                "Vacation Notifications",
                NotificationManager.IMPORTANCE_HIGH

        );

        NotificationManager notificationManager =
                context.getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        Notification notification = new Notification.Builder(context, channel_id)
                .setContentTitle("Vacation Alert")
                .setContentText(message)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .build();
        int notificationID = intent.getIntExtra("notificationID", 1);
        notificationManager.notify(notificationID, notification);
    }
}
