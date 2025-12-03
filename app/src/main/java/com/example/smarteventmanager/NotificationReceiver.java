package com.example.smarteventmanager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {

    private static final String CHANNEL_ID = "event_reminder_channel";
    private static final int NOTIFICATION_ID = 1001;

    @Override
    public void onReceive(Context context, Intent intent) {
        int eventId = intent.getIntExtra("eventId", -1);
        String eventTitle = intent.getStringExtra("eventTitle");

        // Create notification channel for Android O and above
        createNotificationChannel(context);

        // Create intent for opening app
        Intent openIntent = new Intent(context, MainActivity.class);
        openIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                0,
                openIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Get default notification sound
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // Build notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Event Reminder")
                .setContentText(eventTitle + " is coming up soon!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setSound(alarmSound)
                .setVibrate(new long[]{0, 500, 200, 500})
                .setContentIntent(pendingIntent);

        // Show notification
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID + eventId, builder.build());
    }

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Event Reminders";
            String description = "Notifications for upcoming events";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{0, 500, 200, 500});

            NotificationManager notificationManager =
                    context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}