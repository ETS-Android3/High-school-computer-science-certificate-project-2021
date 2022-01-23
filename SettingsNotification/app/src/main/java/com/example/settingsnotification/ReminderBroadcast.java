package com.example.settingsnotification;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RadioButton;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static android.content.Context.MODE_PRIVATE;

public class ReminderBroadcast extends BroadcastReceiver {

    public static final String SHARED_PREFS = "sharedPreferences";
    public static final String WORK_DONE_TODAY_REGISTERED = "work done today was registered";
    public boolean send_notification;

    @Override
    public void onReceive(Context context, Intent intent) {

        loadData(context);

        if(send_notification)
        {
            Intent resultIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "notifymenew")
                    .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                    .setContentTitle("Memento")
                    .setContentText("Adaugă progresul de astăzi")
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(200, builder.build());
        }

        uploadData(context);
        //NOT FINISHED WORK_DONE_TODAY_REGISTERED
    }

    public void loadData(Context context)
    {
        //what type of notification has he choosen
        //to set the selected radio button
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String done_today = sharedPreferences.getString(WORK_DONE_TODAY_REGISTERED, "no");

        //if the user did not register his work for the day we need to send a notification
        switch(done_today)
        {
            case "no":
                send_notification = true;
                return;
            case "yes":
                send_notification = false;
                return;
            default:
                return;
        }
    }

    public void uploadData(Context context)
    {
        //upload data
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(WORK_DONE_TODAY_REGISTERED, "no");
        editor.apply();
    }
}


