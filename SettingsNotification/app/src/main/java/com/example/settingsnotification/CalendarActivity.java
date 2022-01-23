package com.example.settingsnotification;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

public class CalendarActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    DatabaseHelperForItems myDbItems;
    CompactCalendarView cv;
    Calendar currentTime;
    TextView tvLuna;
    ScrollView sc;
    int intDay, intMonth, intYear;
    int intNewDay, intNewMonth, intNewYear;
    String sDay, sMonth, sYear;

    ActionBar actionBar;
    int color = Color.parseColor("#364A88");

    public String[] Luni = {"0", "Ianuarie", "Februarie", "Martie", "Aprilie", "Mai", "Iunie", "Iulie", "August", "Septembrie", "Octombrie", "Noiembrie", "Decembrie", "Ianuarie", "Februarie", "Martie", "Aprilie", "Mai", "Iunie", "Iulie", "August", "Septembrie", "Octombrie", "Noiembrie", "Decembrie"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(color));

        myDb = new DatabaseHelper(this);
        myDbItems = new DatabaseHelperForItems(this);

        cv = (CompactCalendarView) findViewById(R.id.cv);
        tvLuna = findViewById(R.id.tvLUNA);
        currentTime = Calendar.getInstance();
        sc = findViewById(R.id.sc);
        sc.smoothScrollTo(0,0);
        intNewDay = intDay = currentTime.get(Calendar.DAY_OF_MONTH);
        intNewMonth = intMonth = currentTime.get(Calendar.MONTH)+1;
        tvLuna.setText(Luni[intMonth]);
        intNewYear = intYear = currentTime.get(Calendar.YEAR);

        sDay = String.valueOf(intDay);
        sMonth = String.valueOf(intMonth);
        sYear = String.valueOf(intYear);

        //setEventsMonthFor36();
        setEvents();
        addOnCalendarPress();
    }


    @Override
    public void onResume() {
        super.onResume();

        setOnlyNewDay();



        //cv.removeAllEvents();
        //setEventsMonth(intNewMonth);
        EditButton_ViewIncomeButton editButton_viewIncomeButton = new EditButton_ViewIncomeButton(CalendarActivity.this, intNewDay, intNewMonth, intNewYear);
        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentPlace, editButton_viewIncomeButton).commit();

        //Toast.makeText(MainActivity.this, "12", Toast.LENGTH_SHORT).show();
    }


    public void setOnlyNewDay()
    {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, intNewYear);
        calendar.set(Calendar.MONTH, intNewMonth - 1);
        calendar.set(Calendar.DAY_OF_MONTH, intNewDay);

        cv.removeEvents(calendar.getTimeInMillis());

        String items = myDb.getItems(intNewDay, intNewMonth, intNewYear);
        if(verifItems(items))
        {
            long timeEv = calendar.getTimeInMillis();
            int color = Color.parseColor("#2A3C6B");
            Event ev = new Event(color, timeEv);
            cv.addEvent(ev);
        }
    }

    public boolean verifItems(String items)
    {
        if(!items.equals("-"))
        {
            StringTokenizer Tok = new StringTokenizer(items, "!");

            boolean cond = false;
            while (Tok.hasMoreElements())
            {
                int Id = Integer.valueOf(Tok.nextElement().toString());
                int NumberOfItems = Integer.valueOf(Tok.nextElement().toString());

                model_adapter model_adapter = myDbItems.getItem(Id);
                if(model_adapter.getId() == -1)
                    continue;
                else
                {
                    cond = true;
                    break;
                }
            }
            return cond;
        }
        return false;
    }




    public void setEvents()
    {
        ArrayList<String> items = myDb.GetAllItems();
        for(String item : items)
        {
            try {
                StringTokenizer Tok = new StringTokenizer(item, " ");

                int day = Integer.valueOf(Tok.nextElement().toString());
                int month = Integer.valueOf(Tok.nextElement().toString());
                int year = Integer.valueOf(Tok.nextElement().toString());
                String it = Tok.nextElement().toString();

                if(verifItems(it))
                {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month - 1);
                    calendar.set(Calendar.DAY_OF_MONTH, day);

                    long timeEv = calendar.getTimeInMillis();
                    int color = Color.parseColor("#2A3C6B");
                    Event ev = new Event(color, timeEv);
                    cv.addEvent(ev);
                   // Toast.makeText(this, day + "/" + month + "/" + year + "/" + it, Toast.LENGTH_SHORT).show();
                }
            }
            catch (Exception e)
            {

            }



        }
    }

    public void setEventsMonthFor36()
    {
        Calendar calendar =  Calendar.getInstance();
        int kYear = Calendar.YEAR;
       // while(calendar.get(Calendar.YEAR) < )

    }



    public void setEventsMonth(int intMonth)
    {
        Calendar calendar =  Calendar.getInstance();

        int kMonth = intMonth - 1;

        if(kMonth > calendar.get(Calendar.MONTH))
            calendar.add(Calendar.YEAR, -1);
        calendar.set(Calendar.MONTH, kMonth);
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        while(calendar.get(Calendar.MONTH) == kMonth)
        {
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH)+1;
            int year = calendar.get(Calendar.YEAR);

            String items = myDb.getItems(day, month, year);
            if(!items.equals("-"))
            {
                StringTokenizer Tok = new StringTokenizer(items, "!");

                int cond = 0;
                while (Tok.hasMoreElements())
                {
                    int Id = Integer.valueOf(Tok.nextElement().toString());
                    int NumberOfItems = Integer.valueOf(Tok.nextElement().toString());

                    model_adapter model_adapter = myDbItems.getItem(Id);
                    if(model_adapter.getId() == -1)
                        continue;
                    else
                    {
                        cond = 1;
                        break;
                    }
                }
                if(cond == 1)
                {
                    long timeEv = calendar.getTimeInMillis();
                    int color = Color.parseColor("#2A3C6B");
                    Event ev = new Event(color, timeEv);
                    cv.addEvent(ev);
                }
            }

            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
    }

    /*
    public void setEvents(int month)
    {
        while(calendar.get(Calendar.MONTH) == month)
        {
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int month = calendar.get(Calendar.MONTH)+1;
            int year = calendar.get(Calendar.YEAR);

            putItems(day, month, year);

            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }




        String items = myDb.getItems(Day, Month, Year);

        Calendar calendar = Calendar.getInstance();
        calendar.set(2020, 11, 2);
        long startTime = calendar.getTimeInMillis();
        int color = Color.parseColor("#2A3C6B");
        Event ev = new Event(color, startTime);
        cv.addEvent(ev);
    }

     */

    public void addOnCalendarPress()
    {
        cv.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dateClicked);

                intNewDay = calendar.get(Calendar.DAY_OF_MONTH);
                intNewMonth = calendar.get(Calendar.MONTH)+1;
                intNewYear = calendar.get(Calendar.YEAR);

                if(isFirstEventBeforeSecondEvent(intDay, intMonth, intYear, intNewDay, intNewMonth, intNewYear))
                {
                    DayInFuture dayInFuture = new DayInFuture(CalendarActivity.this, intNewDay, intNewMonth, intNewYear);
                    getSupportFragmentManager().beginTransaction().replace(R.id.FragmentPlace, dayInFuture).commit();
                }
                else
                {
                    EditButton_ViewIncomeButton editButton_viewIncomeButton = new EditButton_ViewIncomeButton(CalendarActivity.this, intNewDay, intNewMonth, intNewYear);
                    getSupportFragmentManager().beginTransaction().replace(R.id.FragmentPlace, editButton_viewIncomeButton).commit();
                }
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(firstDayOfNewMonth);

                intNewDay = calendar.get(Calendar.DAY_OF_MONTH);
                intNewMonth = calendar.get(Calendar.MONTH)+1;
                intNewYear = calendar.get(Calendar.YEAR);

                if(isFirstEventBeforeSecondEvent(intDay, intMonth, intYear, intNewDay, intNewMonth, intNewYear))
                {
                    DayInFuture dayInFuture = new DayInFuture(CalendarActivity.this, intNewDay, intNewMonth, intNewYear);
                    getSupportFragmentManager().beginTransaction().replace(R.id.FragmentPlace, dayInFuture).commit();
                }
                else
                {
                    EditButton_ViewIncomeButton editButton_viewIncomeButton = new EditButton_ViewIncomeButton(CalendarActivity.this, intNewDay, intNewMonth, intNewYear);
                    getSupportFragmentManager().beginTransaction().replace(R.id.FragmentPlace, editButton_viewIncomeButton).commit();
                }


                //cv.removeAllEvents();
                //setEventsMonth(intNewMonth);
                tvLuna.setText(Luni[intNewMonth]);
            }
        });

        /*
        cv.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                intNewDay = i2;
                intNewMonth = i1 + 1;
                intNewYear = i;

                if(isFirstEventBeforeSecondEvent(intDay, intMonth, intYear, intNewDay, intNewMonth, intNewYear))
                {
                    DayInFuture dayInFuture = new DayInFuture(CalendarActivity.this, intNewDay, intNewMonth, intNewYear);
                    getSupportFragmentManager().beginTransaction().replace(R.id.FragmentPlace, dayInFuture).commit();
                }
                else
                {
                    EditButton_ViewIncomeButton editButton_viewIncomeButton = new EditButton_ViewIncomeButton(CalendarActivity.this, intNewDay, intNewMonth, intNewYear);
                    getSupportFragmentManager().beginTransaction().replace(R.id.FragmentPlace, editButton_viewIncomeButton).commit();
                }
            }
        });
         */
    }

    public boolean isFirstEventBeforeSecondEvent(int day, int month, int year, int day1, int month1, int year1)
    {
        if(year > year1)
            return false;
        if(year == year1)
        {
            if(month > month1)
                return false;
            if(month == month1)
            {
                if(day >= day1)
                    return false;
            }
        }
        return true;
    }

}
