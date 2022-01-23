package com.example.settingsnotification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    public static final String SHARED_PREFS = "sharedPreferences";
    public static final String LAST_NOTIFICATION_TYPE = "last notification type";

    public Context context;
    public DatabaseHelper myDb;
    public DatabaseHelperForItems myDbItems;

    private ArrayList<exemple_item> exemple_items;

    private RecyclerView recyclerView;
    private example_adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    EditText etSettingItemName, etSettingsItemCost;
    Button bSettingsSaveNewItem, bSettingsDeleteItem, bSettingsChangeItem;

    ActionBar actionBar;
    int color = Color.parseColor("#364A88");

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(color));

        loadSettingsData();

        context = this;

        myDb = new DatabaseHelper(context);
        myDbItems = new DatabaseHelperForItems(context);

        etSettingItemName = (EditText) findViewById(R.id.etSettingItemName);
        etSettingsItemCost = (EditText) findViewById(R.id.etSettingsItemCost);

        bSettingsSaveNewItem = (Button) findViewById(R.id.bSettingsSaveNewItem);
        bSettingsDeleteItem = (Button) findViewById(R.id.bSettingsDeleteItem);
        bSettingsChangeItem = (Button) findViewById(R.id.bSettingsChangePrice);

        recyclerView = findViewById(R.id.rvEdit7);

        exemple_items = new ArrayList<>();

        getAllData();
        buildRecyclerView();

        addOnItemAdded();
    }

    private void getAllData()
    {
        List<model_adapter> arrayList;
        arrayList = myDbItems.getAllData();

        String Item, Cost;
        int Id;
        for(model_adapter per : arrayList)
        {
            Item = per.getItemName();
            Cost = Float.toString(per.getPrice());
            Id = per.getId();

            exemple_items.add(new exemple_item(Item, Cost, Id));
        }
    }

    private void buildRecyclerView()
    {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new example_adapter(exemple_items);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


    public void addOnItemAdded()
    {
        bSettingsSaveNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name = etSettingItemName.getText().toString();
                String Cost = etSettingsItemCost.getText().toString();

                if(Name.equals(""))
                {
                    Toast.makeText(SettingsActivity.this, "Introduceți denumirea obiectului!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(Name.length() > 10)
                {
                    Toast.makeText(SettingsActivity.this, "Denumirea obiectului este prea lungă", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(Name.indexOf('!') != -1)
                {
                    Toast.makeText(SettingsActivity.this, "Ai introdus un caracter interzis!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(Cost.equals(""))
                {
                    Toast.makeText(SettingsActivity.this, "Introduceți o valoare reală!", Toast.LENGTH_SHORT).show();
                    return;
                }

                float cost = Float.valueOf(Cost);

                if(cost < -10000)
                {
                    Toast.makeText(SettingsActivity.this, "Valoarea produsului este prea mica", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(cost > 10000)
                {
                    Toast.makeText(SettingsActivity.this, "Valoarea produsului este prea mare", Toast.LENGTH_SHORT).show();
                    return;
                }

                Cost = Float.toString(cost);

                int Id = myDbItems.getId(Name);
                if(Id != -1)
                {
                    Toast.makeText(SettingsActivity.this, "Obiect deja existent!", Toast.LENGTH_SHORT).show();
                    return;
                }

                model_adapter model_adapter = new model_adapter(-1, Name, Float.valueOf(Cost));
                myDbItems.insertData(model_adapter);

                //test
                //model_adapter = myDbItems.getItem(Name);
                //Toast.makeText(SettingsActivity.this, model_adapter.getId()+" "+model_adapter.getItemName()+" "+model_adapter.getPrice(), Toast.LENGTH_SHORT).show();

                Id = myDbItems.getId(Name);
                if(Id == -1)
                {
                    Toast.makeText(SettingsActivity.this, "eroare, nu are id", Toast.LENGTH_SHORT).show();
                    return;
                }
                exemple_items.add(new exemple_item(Name, Cost, Id));
                adapter.notifyDataSetChanged();
                Toast.makeText(SettingsActivity.this, "Obiect adăugat cu succes!", Toast.LENGTH_SHORT).show();
            }
        });
        // ADD BUTTON CHANGE PRICE
        bSettingsChangeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name = etSettingItemName.getText().toString();
                String Cost = etSettingsItemCost.getText().toString();

                if(Name.equals(""))
                {
                    Toast.makeText(SettingsActivity.this, "Introduceți denumirea obiectului!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(Name.length() > 10)
                {
                    Toast.makeText(SettingsActivity.this, "Denumirea obiectului este prea lungă", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(Name.indexOf('!') != -1)
                {
                    Toast.makeText(SettingsActivity.this, "Ai introdus un caracter interzis!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(Cost.equals(""))
                {
                    Toast.makeText(SettingsActivity.this, "Introduceți o valoare reală!", Toast.LENGTH_SHORT).show();
                    return;
                }

                float cost = Float.valueOf(Cost);

                if(cost < -10000)
                {
                    Toast.makeText(SettingsActivity.this, "Valoarea produsului este prea mica", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(cost > 10000)
                {
                    Toast.makeText(SettingsActivity.this, "Valoarea produsului este prea mare", Toast.LENGTH_SHORT).show();
                    return;
                }


                Cost = Float.toString(cost);

                int Id = myDbItems.getId(Name);
                if(Id == -1)
                {
                    Toast.makeText(SettingsActivity.this, "Obiect inexistent!", Toast.LENGTH_SHORT).show();
                    return;
                }

                model_adapter model_adapter = new model_adapter(Id, Name, Float.valueOf(Cost));
                myDbItems.changePrice(Name, cost);

                boolean changed = false;
                for(Iterator<exemple_item> it = exemple_items.iterator(); it.hasNext();)
                {
                    exemple_item el = it.next();

                    if(el.getLine1().equals(Name))
                    {
                        el.changeText2(Cost);
                        changed = true;
                        break;
                    }
                }
                if(!changed)
                {
                    Toast.makeText(SettingsActivity.this, "eroare, nu s-a putut schimba", Toast.LENGTH_SHORT).show();
                    return;
                }

                adapter.notifyDataSetChanged();
                Toast.makeText(SettingsActivity.this, "Obiect schimbat cu succes!", Toast.LENGTH_SHORT).show();
            }
        });

        bSettingsDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name = etSettingItemName.getText().toString();
                if(Name.equals(""))
                {
                    Toast.makeText(SettingsActivity.this, "Introduceți denumirea obiectului!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(Name.length() > 10)
                {
                    Toast.makeText(SettingsActivity.this, "Denumirea obiectului este prea lungă", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(Name.indexOf('!') != -1)
                {
                    Toast.makeText(SettingsActivity.this, "Ai introdus un caracter interzis!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String Cost = Float.toString(myDbItems.getPrice(Name));
                if(Cost.equals("0.137"))
                {
                    Toast.makeText(SettingsActivity.this, "Obiect inexistent!", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean deleted = false;
                for(Iterator<exemple_item> it = exemple_items.iterator(); it.hasNext();)
                {
                    exemple_item el = it.next();

                    if(el.getLine1().equals(Name) && el.getLine2().equals(Cost))
                    {
                        it.remove();
                        deleted = true;
                        break;
                    }
                }
                if(!deleted)
                {
                    Toast.makeText(SettingsActivity.this, "Eroare la eliminare!", Toast.LENGTH_SHORT).show();
                    return;
                }

                adapter.notifyDataSetChanged();
                myDbItems.deleteData(Name);
                Toast.makeText(SettingsActivity.this, "Obiect eliminat cu succes!", Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void loadSettingsData()
    {
        //notification channel for notifications android 8+
        createNotificationChannel();

        //what type of notification has he choosen
        //to set the selected radio button
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String last_notification_type = sharedPreferences.getString(LAST_NOTIFICATION_TYPE, "daily");

        //set the radio button as the user set it
        RadioButton rbSelected;
        switch(last_notification_type)
        {
            case "daily":
                createDailyNotificationBETA();
                //createDailyNotification();
                rbSelected = (RadioButton) findViewById(R.id.rbDaily);
                rbSelected.toggle();
                return;
            case "weekly":
                //createTestNotification();
                createWeeklyNotification();
                rbSelected = (RadioButton) findViewById(R.id.rbWeekly);
                rbSelected.toggle();
                return;
            case "never":
                cancelNotification();
                rbSelected = (RadioButton) findViewById(R.id.rbNever);
                rbSelected.toggle();
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

            NotificationChannel channel = new NotificationChannel("notifymenew", name, importance);
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
                Toast.makeText(this, "Notificările vor fi afișate zilnic", Toast.LENGTH_SHORT).show();
                return;
            case R.id.rbWeekly:
                createWeeklyNotification();
                //createTestNotification();
                editor.putString(LAST_NOTIFICATION_TYPE, "weekly");
                editor.apply();
                Toast.makeText(this, "Notificările vor fi afișate săptămânal", Toast.LENGTH_SHORT).show();
                return;
            case R.id.rbNever:
                cancelNotification();
                editor.putString(LAST_NOTIFICATION_TYPE, "never");
                editor.apply();
                Toast.makeText(this, "Notificările nu vor fi afișate", Toast.LENGTH_SHORT).show();
                return;
            default:
                return;
        }
    }

    public void createDailyNotification()
    {
        //setting hour of day at which the notification will appear
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 18);

        //what should open when alarm is ringing
        Intent intent = new Intent(SettingsActivity.this, ReminderBroadcast.class);
        pendingIntent = PendingIntent.getBroadcast(SettingsActivity.this, 0, intent, 0);

        //set an alarm that will request a notification at that time
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);

        //for debug
        Toast.makeText(SettingsActivity.this, "Reminder daily has been set", Toast.LENGTH_SHORT).show();
    }

    private void createDailyNotificationBETA()
    {
        Intent intent = new Intent(SettingsActivity.this, ReminderBroadcast.class);

        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        pendingIntent = PendingIntent.getBroadcast(SettingsActivity.this, 0, intent, 0);

        // get current time
        Calendar currentTime = Calendar.getInstance();

        // setup time for alarm
        Calendar alarmTime = Calendar.getInstance();

        // set time-part of alarm
        alarmTime.set(Calendar.SECOND, 0);
        alarmTime.set(Calendar.MINUTE, 0);
        alarmTime.set(Calendar.HOUR, 5);
        alarmTime.set(Calendar.AM_PM, Calendar.PM);


        // check if it in the future
        if (currentTime.getTimeInMillis() >=  alarmTime.getTimeInMillis()) // if not in the future
            alarmTime.add(Calendar.DAY_OF_YEAR, 1);

        //set the alarm
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    private void createWeeklyNotification()
    {
        Intent intent = new Intent(SettingsActivity.this, ReminderBroadcast.class);

        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        pendingIntent = PendingIntent.getBroadcast(SettingsActivity.this, 0, intent, 0);

        // get current time
        Calendar currentTime = Calendar.getInstance();

        // setup time for alarm
        Calendar alarmTime = Calendar.getInstance();

        // set time-part of alarm
        alarmTime.set(Calendar.SECOND, 0);
        alarmTime.set(Calendar.MINUTE, 0);
        alarmTime.set(Calendar.HOUR, 5);
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

    public void createTestNotification()
    {
        Intent intent = new Intent(SettingsActivity.this, ReminderBroadcast.class);
        pendingIntent = PendingIntent.getBroadcast(SettingsActivity.this, 0, intent, 0);

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+(5*100), 5*100, pendingIntent);
        Toast.makeText(SettingsActivity.this, "Reminder set", Toast.LENGTH_SHORT).show();
    }

}
