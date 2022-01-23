package com.example.settingsnotification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Random;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {
    String[] quotes = {
            "Viața este ca mersul pe bicicletă. Pentru a-ți menține echilibrul trebuie să continui să mergi înainte.\n\n – Albert Einstein",
            "Nimic nu este permanent în această lume nebună. Nici măcar necazurile noastre.\n\n – Charlie Chaplin",
            "Eșecul este doar oportunitatea de a lua totul de la capăt, de data aceasta, într-un mod mai inteligent.\n\n – Henry Ford",
            "Fă ce trebuie să faci până poți face ceea ce vrei să faci.\n\n – Oprah Winfrey",
            "Nu te lăsa împins de probleme. Lasă-te condus de vise.\n\n – Ralph Waldo Emerson",
            "Cea mai mare glorie nu o dobândești atunci când nu ești doborât, ci atunci când te ridici după ce ai căzut.\n\n – Confucius",
            "Nu începe niciodată cu nu se poate, ci începe cu să vedem!\n\n – Nicolae Iorga",
            "Vor exista întotdeauna oameni care te vor răni, așa că trebuie să îți păstrezi încrederea și doar să ai mai multă grijă în cine te încrezi a doua oară.\n\n – Jose Garcia Marquez",
            "Trăiește ca și cum ai muri mâine. Învață ca și cum ai trăi veșnic.\n\n – Mahatma Gandhi",
            "Puterea de a fi tu însuți într-o lume care încearcă în permanență să te schimbe este una dintre cele mai mari realizări din viața ta.\n\n – Ralph Waldo Emerson",
            "Cel care nu-și schimbă niciodată opiniile și nu-și corectează greșelile, nu va deveni niciodată mai înțelept.\n\n - Tryon Edwards",
            "Nu încerca sa devi o persoană de succes, ci una de valoare.\n\n – Albert Einstein",
            "Nu mai aștepta! Momentul potrivit nu vine niciodată!\n\n – Napoleon Hill",
            "Dacă nu-ți place situația actuală, atunci fă o schimbare! Nu ești un copac!\n\n – Jim Rohn",
            "Dacă vrei să dobândeşti puterea de a suporta viaţa, fii gata să accepţi moartea.\n\n – Sigmund Freud",
            "Nu poţi fi invidios şi fericit în acelaşi timp!\n\n – Frank Tyger",
            "Niciodată nu trebuie să te ruşinezi a mărturisi că ai greşit. Înseamnă doar să spui, cu alte cuvinte, că astăzi eşti mai înţelept decât ieri.\n\n – Marcel Achard",
            "În viață nu e o tragedie faptul ca nu-ti atingi scopul. Adevărata tragedie e să nu ai unul!\n\n – Benjamin Mays",
            "Orice om are dreptul să-și riște viața pentru a și-o păstra.\n\n - Jean-Jacques Rousseau"
    };
    TextView tvQ;
    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    public static final String SHARED_PREFS = "sharedPreferences";
    public static final String LAST_NOTIFICATION_TYPE = "last notification type";

    ActionBar actionBar;
    int color = Color.parseColor("#364A88");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvQ = findViewById(R.id.tvQ);

        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(color));

        loadNotifications();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }

    @Override
    protected void onResume() {
        int min = 0;
        int max = quotes.length-1;
        int random = new Random().nextInt((max - min) + 1) + min;
        tvQ.setText(quotes[random]);

        super.onResume();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.item1:
                openSettingsActivity();
                return true;
            case R.id.item2:
                openCalendarActivity();
                return true;
            case R.id.item3:
                openStatisticsActivity();
                return true;
            case R.id.item4:
                openGraphicsActivity();
                return true;
            case R.id.item5:
                openWorkDoneTodayActivity();
                return true;
            case R.id.item6:
                openResultActivity();
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void loadNotifications()
    {
        //notification channel for notifications android 8+
        createNotificationChannel();

        //what type of notification has he choosen
        //to set the selected radio button
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String last_notification_type = sharedPreferences.getString(LAST_NOTIFICATION_TYPE, "daily");

        switch(last_notification_type)
        {
            case "daily":
                createDailyNotificationBETA();
                return;
            case "weekly":
                createWeeklyNotification();
                return;
            case "never":
                cancelNotification();
                return;
            default:
                return;
        }
    }


    private void createNotificationChannel()
    {
        //if android version is newer than 8.0 the we must create a notification channel
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            CharSequence name = "MyReminderChannel";
            String description = "Channel for my app";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;

            NotificationChannel channel = new NotificationChannel("notifyme", name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    //let radio buttons change notification type by selecting it
    public void changeNotificationSetting(View view)
    {
        RadioButton rbSelected;
        rbSelected = (RadioButton) view;

        //upload data
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        switch(rbSelected.getId())
        {
            case R.id.rbDaily:
                //createDailyNotification();
                createDailyNotificationBETA();
                editor.putString(LAST_NOTIFICATION_TYPE, "daily");
                editor.apply();
                Toast.makeText(this, "Show notification daily", Toast.LENGTH_SHORT).show();
                return;
            case R.id.rbWeekly:
                createWeeklyNotification();
                //createTestNotification();
                editor.putString(LAST_NOTIFICATION_TYPE, "weekly");
                editor.apply();
                Toast.makeText(this, "Show notification weekly", Toast.LENGTH_SHORT).show();
                return;
            case R.id.rbNever:
                cancelNotification();
                editor.putString(LAST_NOTIFICATION_TYPE, "never");
                editor.apply();
                Toast.makeText(this, "Never show notification", Toast.LENGTH_SHORT).show();
                return;
            default:
                return;
        }
    }


    private void createDailyNotificationBETA()
    {
        Intent intent = new Intent(this, ReminderBroadcast.class);

        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        // get current time
        Calendar currentTime = Calendar.getInstance();

        // setup time for alarm
        Calendar alarmTime = Calendar.getInstance();

        // set time-part of alarm
        alarmTime.set(Calendar.SECOND, 0);
        alarmTime.set(Calendar.MINUTE, 0);
        alarmTime.set(Calendar.HOUR, 12);
        alarmTime.set(Calendar.AM_PM, Calendar.AM);


        // check if it in the future
        if (currentTime.getTimeInMillis() >=  alarmTime.getTimeInMillis()) // if not in the future
            alarmTime.add(Calendar.DAY_OF_YEAR, 1);

        //set the alarm
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void createWeeklyNotification()
    {
        Intent intent = new Intent(this, ReminderBroadcast.class);

        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        // get current time
        Calendar currentTime = Calendar.getInstance();

        // setup time for alarm
        Calendar alarmTime = Calendar.getInstance();

        // set time-part of alarm
        alarmTime.set(Calendar.SECOND, 0);
        alarmTime.set(Calendar.MINUTE, 0);
        alarmTime.set(Calendar.HOUR, 6);
        alarmTime.set(Calendar.AM_PM, Calendar.PM);
        alarmTime.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        // check if it in the future
        if (currentTime.getTimeInMillis() >=  alarmTime.getTimeInMillis())
            alarmTime.add(Calendar.WEEK_OF_YEAR, 1);

        // calculate interval (7 days) in ms
        int interval = 1000 * 60 * 60 * 24 * 7;

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), interval, pendingIntent);
    }

    public void cancelNotification()
    {
        if(alarmManager != null)
        {
            alarmManager.cancel(pendingIntent);
            //Toast.makeText(this, "canceled", Toast.LENGTH_SHORT).show();
        }
    }



    public void openResultActivity()
    {
        Intent intent = new Intent(this, ResultActivity.class);
        startActivity(intent);
    }

    private void openSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void openCalendarActivity()
    {
        Intent intent = new Intent(this, CalendarActivity.class);
        startActivity(intent);
    }

    public void openStatisticsActivity()
    {
        Intent intent = new Intent(this, StatisticsActivity.class);
        startActivity(intent);
    }

    public void openGraphicsActivity()
    {
        Intent intent = new Intent(this, GraphicsActivity.class);
        startActivity(intent);
    }

    public void openWorkDoneTodayActivity()
    {
        Intent intent = new Intent(this, WorkDoneTodayActivity.class);
        startActivity(intent);
    }
}
