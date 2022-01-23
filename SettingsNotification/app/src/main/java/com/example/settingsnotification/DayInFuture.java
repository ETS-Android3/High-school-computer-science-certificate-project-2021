package com.example.settingsnotification;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class DayInFuture extends Fragment {

    //public String sDay, sMonth, sYear;
    public int intDay, intMonth, intYear;
    public Context context;
    public DatabaseHelper myDb;
    public DatabaseHelperForItems myDbItems;
    TextView tvDate1;

    public DayInFuture(Context ctx, int Day, int Month, int Year)
    {
        intDay = Day;
        intMonth = Month;
        intYear = Year;
        context = ctx;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_day_in_future, container, false);
        myDb = new DatabaseHelper(context);
        myDbItems = new DatabaseHelperForItems(context);

        tvDate1 = (TextView) v.findViewById(R.id.tvDate1);

        String sDate = intDay + "/" + intMonth + "/" + intYear;
        tvDate1.setText(sDate);

        return v;
    }

}
