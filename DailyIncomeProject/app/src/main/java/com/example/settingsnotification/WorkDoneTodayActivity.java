package com.example.settingsnotification;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;


public class WorkDoneTodayActivity extends AppCompatActivity {
    //public String sDay, sMonth, sYear;
    public int intDay, intMonth, intYear;
    public TextView tvDateEditWork;
    public EditText multilineText;

    public DatabaseHelper myDb;
    public DatabaseHelperForItems myDbItems;

    private RecyclerView recyclerView;
    private example_edit_adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<exemple_item_edit> exemple_items;
    private ArrayList<exemple_item_edit> last_items;

    private HashMap<String, Integer> items_position;

    public Button bSaveEditData;

    ActionBar actionBar;
    int color = Color.parseColor("#364A88");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_done_today);
        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(color));

        myDb = new DatabaseHelper(this);
        myDbItems = new DatabaseHelperForItems(this);

        tvDateEditWork = (TextView) findViewById(R.id.tvDateEditWork);
        multilineText = (EditText) findViewById(R.id.NotitaInput);
        Calendar calendar =  Calendar.getInstance();

        intDay = calendar.get(Calendar.DAY_OF_MONTH);
        intMonth = calendar.get(Calendar.MONTH)+1;
        intYear = calendar.get(Calendar.YEAR);

        recyclerView = findViewById(R.id.rvEdit);
        createExampleList(myDb.getItems(intDay, intMonth, intYear));
        multilineText.setText(myDb.getMesaj(intDay, intMonth, intYear));
        multilineText.setSelection(multilineText.length());
        buildRecyclerView();

        bSaveEditData = findViewById(R.id.bSaveEdited);

        setOnSaveData();
    }


    private void createExampleList(String Demo)
    {
        exemple_items = new ArrayList<>();
        items_position = new HashMap<>();
        if(!Demo.equals("-"))
        {
            StringTokenizer Tok = new StringTokenizer(Demo, "!");

            while (Tok.hasMoreElements())
            {
                int Id = Integer.valueOf(Tok.nextElement().toString());
                String NumberOfItems = Tok.nextElement().toString();

                model_adapter model_adapter = myDbItems.getItem(Id);
                //Toast.makeText(this, model_adapter.getId()+" "+model_adapter.getItemName()+" "+model_adapter.getPrice(), Toast.LENGTH_SHORT).show();
                if(model_adapter.getId() == -1)
                    continue;
                String Item = model_adapter.getItemName();

                items_position.put(Item, exemple_items.size());
                exemple_items.add(new exemple_item_edit(Item, NumberOfItems, Id));
            }
        }
        introduceAllKindsOfItems();
    }


    private void buildRecyclerView()
    {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        adapter = new example_edit_adapter(exemple_items);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }


    private void setOnSaveData()
    {
        bSaveEditData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDataOnDay();
            }
        });
    }

    private void saveDataOnDay()
    {
        String NewItems="", Item="", NumOfItem="", NumWas="";
        String Notita="";
        int numOfItems;
        int SizeOfArray = exemple_items.size();
        int Id;
        for(int i=0; i<SizeOfArray; i++)
        {
            NumOfItem = adapter.getText2(i);
            Item = exemple_items.get(i).getLine1();
            Id = exemple_items.get(i).getId();

            if(NumOfItem.equals(""))
                NumOfItem = "0";

            numOfItems = Integer.valueOf(NumOfItem);
            //exista input
            if(numOfItems > 10000)
            {
                Toast.makeText(this, "Numărul unui produs este prea mare", Toast.LENGTH_SHORT).show();
                return;
            }
            if(numOfItems == 0)
                continue;

            NumOfItem = Integer.toString(numOfItems);


            NewItems += Id;
            NewItems += "!";
            NewItems += NumOfItem;
            NewItems += "!";
        }
        Notita = multilineText.getText().toString();
        //Toast.makeText(this, Notita, Toast.LENGTH_SHORT).show();


        myDb.deleteData(intDay, intMonth, intYear);
        myDb.insertData(intDay, intMonth, intYear,  NewItems, Notita);

        Toast.makeText(this, "Obiecte adăugate", Toast.LENGTH_SHORT).show();
    }


    void introduceAllKindsOfItems()
    {
        List<model_adapter> arrayList;
        arrayList = myDbItems.getAllData();

        String Item;
        int Id;
        for(model_adapter per : arrayList)
        {
            Item = per.getItemName();
            Id = per.getId();

            if(!items_position.containsKey(Item))
            {
                items_position.put(Item, exemple_items.size());
                exemple_items.add(new exemple_item_edit(Item, "0", Id));
            }
        }
    }
}
