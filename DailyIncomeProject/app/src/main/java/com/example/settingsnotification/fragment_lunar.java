package com.example.settingsnotification;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;


/**
 * A simple {@link Fragment} subclass.
 */
public class fragment_lunar extends Fragment { //implements PopupMenu.OnMenuItemClickListener {

    public DatabaseHelper myDb;
    public DatabaseHelperForItems myDbItems;

    private ArrayList<exemple_item> exemple_items;
    private HashMap<String, Integer> items_position;

    private RecyclerView recyclerView;
    private example_adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    public TextView tvIncomeLuna;

    public int intMonth;
    public Context context;

    public TextView tvBucatiTotal;


    public float total = 0;

    public int ktotal = 0;

    public fragment_lunar(Context ctx, int Month)
    {
        intMonth = Month;
        context = ctx;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_fragment_lunar, container, false);
        myDb = new DatabaseHelper(context);
        myDbItems = new DatabaseHelperForItems(context);

        items_position = new HashMap<>();
        exemple_items = new ArrayList<>();

        recyclerView = v.findViewById(R.id.rvEdit22);

        tvIncomeLuna = v.findViewById(R.id.incomeLuna2);
        tvBucatiTotal = v.findViewById(R.id.bucatiLuna2);

        getAllMonth(intMonth);
        buildRecyclerView();

        tvIncomeLuna.setText("Venit brut în valoare de " + Float.toString(total) + " lei");
        tvBucatiTotal.setText("În total ați realizat " + Integer.toString(ktotal) + " obiecte");
        return v;
    }

    public void getAllMonth(int intMonth)
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

            putItems(day, month, year);

            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        createExampleList();
    }

    public void createExampleList()
    {
        Iterator it = items_position.entrySet().iterator();
        while(it.hasNext())
        {
            HashMap.Entry pair = (HashMap.Entry) it.next();
            String Item = pair.getKey().toString();
            String NumberOfItems = pair.getValue().toString();
            exemple_items.add(new exemple_item(Item, NumberOfItems, -1));

            it.remove();
        }
    }


    public void putItems(int Day, int Month, int Year)
    {
        String items = myDb.getItems(Day, Month, Year);
        addItems(items);
    }




    public void addItems(String Demo)
    {
        if(!Demo.equals("-"))
        {
            StringTokenizer Tok = new StringTokenizer(Demo, "!");

            while (Tok.hasMoreElements())
            {
                int Id = Integer.valueOf(Tok.nextElement().toString());
                int NumberOfItems = Integer.valueOf(Tok.nextElement().toString());

                model_adapter model_adapter = myDbItems.getItem(Id);
                if(model_adapter.getId() == -1)
                    continue;

                String Item = model_adapter.getItemName();
                ktotal += NumberOfItems;
                total += model_adapter.getPrice() * NumberOfItems;
                if(items_position.containsKey(Item))
                {
                    NumberOfItems += items_position.get(Item);
                    items_position.remove(Item);
                }
                items_position.put(Item, NumberOfItems);
            }
        }
    }

    private void buildRecyclerView()
    {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        adapter = new example_adapter(exemple_items);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
}
