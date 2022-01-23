package com.example.settingsnotification;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.StringTokenizer;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class StatisticsActivity extends AppCompatActivity {
    public DatabaseHelper myDb;
    public DatabaseHelperForItems myDbItems;
    TextView tvIncomeDay, tvIncome7, tvIncome28, tvIncome4;
    TextView tvComparisonDay, tvComparison7, tvComparison28, tvComparison4;
    ImageView ivArrowDay, ivArrow7, ivArrow28, ivArrow4;

    ActionBar actionBar;
    int color = Color.parseColor("#364A88");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(color));

        myDb = new DatabaseHelper(this);
        myDbItems = new DatabaseHelperForItems(this);

        tvIncomeDay = findViewById(R.id.tvIncomeDay);
        tvIncome7 = findViewById(R.id.tvIncome7);
        tvIncome28 = findViewById(R.id.tvIncome28);
        tvIncome4 = findViewById(R.id.tvIncome4);

        tvComparisonDay = findViewById(R.id.tvComparisonDay);
        tvComparison7 = findViewById(R.id.tvComparison7);
        tvComparison28 = findViewById(R.id.tvComparison28);
        tvComparison4 = findViewById(R.id.tvComparison4);

        ivArrowDay = findViewById(R.id.ivArrowDay);
        ivArrow7 = findViewById(R.id.ivArrow7);
        ivArrow28 = findViewById(R.id.ivArrow28);
        ivArrow4 = findViewById(R.id.ivArrow4);


        setStatisticsDay();
        setStatistics7();
        setStatistics28();
        setStatistics4();
        myDb.close();
    }

    public void setStatisticsDay()
    {
        Calendar calendar =  Calendar.getInstance();

        int intDay, intMonth, intYear;

        intDay = calendar.get(Calendar.DAY_OF_MONTH);
        intMonth = calendar.get(Calendar.MONTH)+1;
        intYear = calendar.get(Calendar.YEAR);

        float incomeYesterday = getIncome(intDay, intMonth, intYear);
        incomeYesterday = Math.min(incomeYesterday, 1000000);
        incomeYesterday = Math.max(incomeYesterday, -1000000);
        tvIncomeDay.setText(String.valueOf(incomeYesterday));
        ///

        calendar.add(Calendar.WEEK_OF_YEAR, -1);

        intDay = calendar.get(Calendar.DAY_OF_MONTH);
        intMonth = calendar.get(Calendar.MONTH)+1;
        intYear = calendar.get(Calendar.YEAR);


        float incomeLastWeek = getIncome(intDay, intMonth, intYear);
        incomeLastWeek = Math.min(incomeLastWeek, 1000000);
        incomeLastWeek = Math.max(incomeLastWeek, -1000000);

        float diff = incomeYesterday - incomeLastWeek;

        if(diff > 0)
        {
            tvComparisonDay.setTextColor(Color.parseColor("#0AFF07"));
            ivArrowDay.setImageResource(R.drawable.ic_arrow_upward_black_24dp);
        }
        else if(diff < 0)
        {
            tvComparisonDay.setTextColor(Color.parseColor("#FF0800"));
            ivArrowDay.setImageResource(R.drawable.ic_arrow_downward_black_24dp);
        }
        else
        {
            tvComparisonDay.setTextColor(Color.parseColor("#FFA506"));
            ivArrowDay.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);
        }


        ///
        int percentage = min(Math.round((float)diff/incomeLastWeek * 100), 1000);
        percentage = max(percentage, -1000);
        tvComparisonDay.setText(String.valueOf(diff) + " (" + String.valueOf(percentage) + "%)");
        ///
    }

    public void setStatistics7()
    {
        Calendar calendar =  Calendar.getInstance();

        int intDay, intMonth, intYear;


        float incomeLast7Days = 0;
        ///

        calendar.add(Calendar.DAY_OF_YEAR, 1);

        for(int i=1; i<=7; i++)
        {
            calendar.add(Calendar.DAY_OF_YEAR, -1);

            intDay = calendar.get(Calendar.DAY_OF_MONTH);
            intMonth = calendar.get(Calendar.MONTH)+1;
            intYear = calendar.get(Calendar.YEAR);

            incomeLast7Days += getIncome(intDay, intMonth, intYear);
            incomeLast7Days = Math.min(incomeLast7Days, 1000000);
            incomeLast7Days = Math.max(incomeLast7Days, -1000000);
        }
        tvIncome7.setText(String.valueOf(incomeLast7Days));


        float incomeBefore7Days = 0;

        for(int i=1; i<=7; i++)
        {
            calendar.add(Calendar.DAY_OF_YEAR, -1);

            intDay = calendar.get(Calendar.DAY_OF_MONTH);
            intMonth = calendar.get(Calendar.MONTH)+1;
            intYear = calendar.get(Calendar.YEAR);

            incomeBefore7Days += getIncome(intDay, intMonth, intYear);
            incomeLast7Days = Math.min(incomeLast7Days, 1000000);
            incomeLast7Days = Math.max(incomeLast7Days, -1000000);
        }

        float diff = incomeLast7Days - incomeBefore7Days;

        if(diff > 0)
        {
            tvComparison7.setTextColor(Color.parseColor("#0AFF07"));
            ivArrow7.setImageResource(R.drawable.ic_arrow_upward_black_24dp);
        }
        else if(diff < 0)
        {
            tvComparison7.setTextColor(Color.parseColor("#FF0800"));
            ivArrow7.setImageResource(R.drawable.ic_arrow_downward_black_24dp);
        }
        else
        {
            tvComparison7.setTextColor(Color.parseColor("#FFA506"));
            ivArrow7.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);
        }
        ///

        int percentage = min(Math.round((float)diff/incomeBefore7Days * 100), 1000);
        percentage = max(percentage, -1000);
        tvComparison7.setText(String.valueOf(diff) + " (" + String.valueOf(percentage) + "%)");
        ///
    }

    public void setStatistics28()
    {
        Calendar calendar =  Calendar.getInstance();

        int intDay, intMonth, intYear;

        float incomeLast28Days = 0;

        calendar.add(Calendar.DAY_OF_YEAR, 1);

        ///
        for(int i=1; i<=28; i++)
        {
            calendar.add(Calendar.DAY_OF_YEAR, -1);

            intDay = calendar.get(Calendar.DAY_OF_MONTH);
            intMonth = calendar.get(Calendar.MONTH)+1;
            intYear = calendar.get(Calendar.YEAR);

            incomeLast28Days += getIncome(intDay, intMonth, intYear);
            incomeLast28Days = Math.min(incomeLast28Days, 1000000);
            incomeLast28Days = Math.max(incomeLast28Days, -1000000);
        }
        tvIncome28.setText(String.valueOf(incomeLast28Days));

        float incomeBefore28Days = 0;

        for(int i=1; i<=28; i++)
        {
            calendar.add(Calendar.DAY_OF_YEAR, -1);

            intDay = calendar.get(Calendar.DAY_OF_MONTH);
            intMonth = calendar.get(Calendar.MONTH)+1;
            intYear = calendar.get(Calendar.YEAR);

            incomeBefore28Days += getIncome(intDay, intMonth, intYear);
            incomeLast28Days = Math.min(incomeLast28Days, 1000000);
            incomeLast28Days = Math.max(incomeLast28Days, -1000000);
        }

        float diff = incomeLast28Days - incomeBefore28Days;

        if(diff > 0)
        {
            tvComparison28.setTextColor(Color.parseColor("#0AFF07"));
            ivArrow28.setImageResource(R.drawable.ic_arrow_upward_black_24dp);
        }
        else if(diff < 0)
        {
            tvComparison28.setTextColor(Color.parseColor("#FF0800"));
            ivArrow28.setImageResource(R.drawable.ic_arrow_downward_black_24dp);
        }
        else
        {
            tvComparison28.setTextColor(Color.parseColor("#FFA506"));
            ivArrow28.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);
        }
        ///

        int percentage = min(Math.round((float)diff/incomeBefore28Days * 100), 1000);
        percentage = max(percentage, -1000);
        tvComparison28.setText(String.valueOf(diff) + " (" + String.valueOf(percentage) + "%)");
        ///
    }

    public void setStatistics4()
    {
        Calendar calendar =  Calendar.getInstance();

        int intDay, intMonth, intYear;

        float incomeLast4 = 0;

        calendar.add(Calendar.DAY_OF_YEAR, 1);

        ///
        for(int i=1; i<=112; i++)
        {
            calendar.add(Calendar.DAY_OF_YEAR, -1);

            intDay = calendar.get(Calendar.DAY_OF_MONTH);
            intMonth = calendar.get(Calendar.MONTH)+1;
            intYear = calendar.get(Calendar.YEAR);

            incomeLast4 += getIncome(intDay, intMonth, intYear);
            incomeLast4 = Math.min(incomeLast4, 1000000);
            incomeLast4 = Math.max(incomeLast4, -1000000);
        }
        tvIncome4.setText(String.valueOf(incomeLast4));

        float incomeBefore4 = 0;

        for(int i=1; i<=112; i++)
        {
            calendar.add(Calendar.DAY_OF_YEAR, -1);

            intDay = calendar.get(Calendar.DAY_OF_MONTH);
            intMonth = calendar.get(Calendar.MONTH)+1;
            intYear = calendar.get(Calendar.YEAR);

            incomeBefore4 += getIncome(intDay, intMonth, intYear);
            incomeBefore4 = Math.min(incomeBefore4, 1000000);
            incomeBefore4 = Math.max(incomeBefore4, -1000000);
        }

        float diff = incomeLast4 - incomeBefore4;

        if(diff > 0)
        {
            tvComparison4.setTextColor(Color.parseColor("#0AFF07"));
            ivArrow4.setImageResource(R.drawable.ic_arrow_upward_black_24dp);
        }
        else if(diff < 0)
        {
            tvComparison4.setTextColor(Color.parseColor("#FF0800"));
            ivArrow4.setImageResource(R.drawable.ic_arrow_downward_black_24dp);
        }
        else
        {
            tvComparison4.setTextColor(Color.parseColor("#FFA506"));
            ivArrow4.setImageResource(R.drawable.ic_pause_circle_outline_black_24dp);
        }
        ///

        float percentage = min(Math.round((float)diff/incomeBefore4 * 100), 1000);
        percentage = max(percentage, -1000);
        tvComparison4.setText(String.valueOf(diff) + " (" + String.valueOf(percentage) + "%)");
        ///
    }




    public float getIncome(int Day, int Month, int Year)
    {
        String Demo = myDb.getItems(Day, Month, Year);
        float TotalIncome = 0;
        if(!Demo.equals("-"))
        {
            StringTokenizer Tok = new StringTokenizer(Demo, "!");

            while (Tok.hasMoreElements())
            {
                int Id = Integer.valueOf(Tok.nextElement().toString());
                String NumberOfItems = Tok.nextElement().toString();

                model_adapter model_adapter = myDbItems.getItem(Id);
                if(model_adapter.getId() == -1)
                    continue;

                int kItems = Integer.valueOf(NumberOfItems);
                TotalIncome += kItems * model_adapter.getPrice();
            }
        }

        return TotalIncome;
    }
}
