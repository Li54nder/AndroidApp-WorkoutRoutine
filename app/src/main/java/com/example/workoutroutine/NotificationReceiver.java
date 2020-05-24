package com.example.workoutroutine;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {

    public static Context AppContext;

    @Override
    public void onReceive(Context context, Intent intent) {

        Global global = (Global) AppContext;
        if(global.isDone()) {
            global.markAsUndone();
        } else {
            global.setPoints(global.getPoints() - 10);
        }


        NotificationManager notificationManager;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel1 = new NotificationChannel(
                    "CHANNEL_1", "Channel 1", NotificationManager.IMPORTANCE_DEFAULT
            );
            channel1.setDescription("It's time for workout");

            notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel1);
        } else {
            notificationManager =  (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }

        Intent repeating_intent = new Intent(context, WorkoutPlanActivity.class);
        repeating_intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "CHANNEL_1")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.logo_450)
                .setContentTitle("It's time for workout")
                .setContentText("Are you ready for today's wokout. Let's do it!")
                .setAutoCancel(true);

        notificationManager.notify(100, builder.build());
    }

}
