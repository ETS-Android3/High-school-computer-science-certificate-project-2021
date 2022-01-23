package com.example.settingsnotification;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.StringTokenizer;

public class GraphicsActivity extends AppCompatActivity {
    private BarChart mBarChart;
    public TextView tvGraph;
    public DatabaseHelper myDb;
    public DatabaseHelperForItems myDbItems;
    public ImageView left;
    public ImageView right;
    public int gfIndex;

    ActionBar actionBar;
    int color = Color.parseColor("#364A88");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphics);
        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(color));

        myDb = new DatabaseHelper(this);
        myDbItems = new DatabaseHelperForItems(this);


        left = findViewById(R.id.ivLeft);
        right = findViewById(R.id.ivRight);
        setOnClicks();

        mBarChart = findViewById(R.id.barChart);
        tvGraph = findViewById(R.id.tvGraph);
        gfIndex = 0;
        getInformationLast14Days();
    }

    public void setOnClicks()
    {
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gfIndex --;
                gfIndex = Math.min(gfIndex, 2);
                gfIndex = Math.max(gfIndex, 0);
                intoSwitch(gfIndex);
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gfIndex ++;
                gfIndex = Math.min(gfIndex, 2);
                gfIndex = Math.max(gfIndex, 0);
                intoSwitch(gfIndex);
            }
        });
    }


    public void intoSwitch(int index)
    {
        switch(index)
        {
            case 0:
                Log.i("SSSSSSSSSSSSSSSS", "start ");
                getInformationLast14Days();
                Log.i("SSSSSSSSSSSSSSSS", "end  ");
                return;
            case 1:
                Log.i("SSSSSSSSSSSSSSSS", "start ");
                getInformationLast12Weeks();
                Log.i("SSSSSSSSSSSSSSSS", "end  ");
                return;
            case 2:
                Log.i("SSSSSSSSSSSSSSSS", "start ");
                getInformationLast12Months();
                Log.i("SSSSSSSSSSSSSSSS", "end  ");
                return;
            case 3:
                //getInformationLast12Years();
                return;
            default:
                return;
        }
    }

    public void getInformationLast14Days()
    {
        tvGraph.setText("Ultimele 14 zile");
        List<BarEntry> barEntryList = new ArrayList<>();
        Calendar calendar =  Calendar.getInstance();

        int intDay, intMonth, intYear;

        float Income;

        calendar.add(Calendar.DAY_OF_YEAR, 1);

        for(int i=14; i>=1; i--)
        {
            calendar.add(Calendar.DAY_OF_YEAR, -1);

            intDay = calendar.get(Calendar.DAY_OF_MONTH);
            intMonth = calendar.get(Calendar.MONTH)+1;
            intYear = calendar.get(Calendar.YEAR);

            Income = Float.parseFloat(getIncome(intDay, intMonth, intYear));
            Income = Math.min(Income, 1000000);
            Income = Math.max(Income, -1000000);

            barEntryList.add(new BarEntry(i, Income));
        }

        BarDataSet barDataSet = new BarDataSet(barEntryList, "Progres");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.9f);
        mBarChart.setVisibility(View.VISIBLE);
        mBarChart.animateY(2000);
        mBarChart.setData(barData);
        mBarChart.setFitBars(true);
        Description description = new Description();
        description.setText("");
        mBarChart.setDescription(description);
        mBarChart.invalidate();
    }

    public void getInformationLast12Weeks()
    {
        tvGraph.setText("Ultimele 12 săptămâni");
        List<BarEntry> barEntryList = new ArrayList<>();
        Calendar calendar =  Calendar.getInstance();

        int intDay, intMonth, intYear;

        float Income;

        calendar.add(Calendar.DAY_OF_YEAR, 1);

        for(int i=12; i>=1; i--)
        {
            Income = 0;
            for(int j=1; j<=7; j++)
            {
                calendar.add(Calendar.DAY_OF_YEAR, -1);

                intDay = calendar.get(Calendar.DAY_OF_MONTH);
                intMonth = calendar.get(Calendar.MONTH)+1;
                intYear = calendar.get(Calendar.YEAR);

                Income += Float.parseFloat(getIncome(intDay, intMonth, intYear));
                Income = Math.min(Income, 1000000);
                Income = Math.max(Income, -1000000);
            }
            Log.i("SSSSSSSSSSSSSSSS", "here ");
            barEntryList.add(new BarEntry(i, Income));
        }

        BarDataSet barDataSet = new BarDataSet(barEntryList, "Progres");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.9f);
        mBarChart.setVisibility(View.VISIBLE);
        mBarChart.animateY(2000);
        mBarChart.setData(barData);
        mBarChart.setFitBars(true);
        Description description = new Description();
        description.setText("");
        mBarChart.setDescription(description);
        mBarChart.invalidate();
    }

    public void getInformationLast12Months()
    {
        tvGraph.setText("Ultimele 12 luni");
        List<BarEntry> barEntryList = new ArrayList<>();
        Calendar calendar =  Calendar.getInstance();

        int intDay, intMonth, intYear;

        float Income;

        calendar.add(Calendar.DAY_OF_YEAR, 1);

        for(int i=12; i>=1; i--)
        {
            Income = 0;
            for(int j=1; j<=28; j++)
            {
                calendar.add(Calendar.DAY_OF_YEAR, -1);

                intDay = calendar.get(Calendar.DAY_OF_MONTH);
                intMonth = calendar.get(Calendar.MONTH)+1;
                intYear = calendar.get(Calendar.YEAR);

                Income += Float.parseFloat(getIncome(intDay, intMonth, intYear));
                Income = Math.min(Income, 1000000);
                Income = Math.max(Income, -1000000);
            }
            barEntryList.add(new BarEntry(i, Income));
        }

        BarDataSet barDataSet = new BarDataSet(barEntryList, "Progres");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.9f);
        mBarChart.setVisibility(View.VISIBLE);
        mBarChart.animateY(2000);
        mBarChart.setData(barData);
        mBarChart.setFitBars(true);
        Description description = new Description();
        description.setText("");
        mBarChart.setDescription(description);
        mBarChart.invalidate();
    }

    public void getInformation()
    {
        tvGraph.setText("Last 14 days");
        List<BarEntry> barEntryList = new ArrayList<>();
        Calendar calendar =  Calendar.getInstance();

        int intDay, intMonth, intYear;

        float Income;

        for(int i=14; i>=1; i--)
        {
            calendar.add(Calendar.DAY_OF_YEAR, -1);

            intDay = calendar.get(Calendar.DAY_OF_MONTH);
            intMonth = calendar.get(Calendar.MONTH)+1;
            intYear = calendar.get(Calendar.YEAR);

            Income = Float.parseFloat(getIncome(intDay, intMonth, intYear));
            Income = Math.min(Income, 50000);
            Income = Math.max(Income, -50000);

            barEntryList.add(new BarEntry(i, Income));
        }

        BarDataSet barDataSet = new BarDataSet(barEntryList, "Progres");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.9f);
        mBarChart.setVisibility(View.VISIBLE);
        mBarChart.animateY(2000);
        mBarChart.setData(barData);
        mBarChart.setFitBars(true);
        Description description = new Description();
        description.setText("");
        mBarChart.setDescription(description);
        mBarChart.invalidate();
    }

    public String getIncome(int Day, int Month, int Year)
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

        return Float.toString(TotalIncome);
    }
}


