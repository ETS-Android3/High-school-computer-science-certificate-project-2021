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
import java.util.StringTokenizer;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditButton_ViewIncomeButton extends Fragment { //implements PopupMenu.OnMenuItemClickListener {

    public int intDay, intMonth, intYear;
    public Context context;
    public DatabaseHelper myDb;
    public DatabaseHelperForItems myDbItems;
    
    Button bEditIncome;
    TextView etIncome;
    TextView tvDate;
    TextView tvNotita;
    Button bOpenEdit;

    private RecyclerView recyclerView;
    private example_adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<exemple_item> exemple_items;

    // TextView tvSelected;
   // Button addNewItem;

    ActionBar actionBar;
    int color = Color.parseColor("#628ff0");

    public EditButton_ViewIncomeButton(Context ctx, int Day, int Month, int Year)
    {
        intDay = Day;
        intMonth = Month;
        intYear = Year;
        context = ctx;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View v = inflater.inflate(R.layout.fragment_edit_button__view_income_button, container, false);

        myDb = new DatabaseHelper(context);
        myDbItems = new DatabaseHelperForItems(context);

        etIncome = (TextView) v.findViewById(R.id.etIncome);
        tvDate = (TextView) v.findViewById(R.id.tvDate);
        tvNotita = (TextView) v.findViewById(R.id.NotitaOutput);
        //bEditIncome = (Button) v.findViewById(R.id.bEditIncome);
        recyclerView = v.findViewById(R.id.rv);
        bOpenEdit = v.findViewById(R.id.bOpenEdit);
        onEditPress();
        //tvSelected = (TextView) v.findViewById(R.id.tvItem);
        //addNewItem = (Button) v.findViewById(R.id.bAddItemToTV);
        //setOnClickShowPopUp();

        createExampleList(myDb.getItems(intDay, intMonth, intYear));
        buildRecyclerView();

        //setButtons();


        String sDate = intDay + "/" + intMonth + "/" + intYear;
        tvDate.setText(sDate);
        String sIncome = getIncome(intDay, intMonth, intYear);
        if(sIncome == null || sIncome == "-")
            etIncome.setText("Venit: 0 lei");
        else
            etIncome.setText("Venit: " + sIncome + " lei");
        String sNotita = myDb.getMesaj(intDay, intMonth, intYear);
        int cond = 0;
        for(int i=0; i<sNotita.length(); i++)
            if(sNotita.charAt(i) != ' ') {
                cond = 1;
                break;
            }
        if(cond == 1)
            tvNotita.setText(sNotita);
        else
            tvNotita.setText("Nu ai notat nimic.");



        return v;
    }

    //??//

    public void removeItem(int position)
    {
        exemple_items.remove(position);
        adapter.notifyItemRemoved(position);
    }

    public void changeItem(int position, String text)
    {
        exemple_items.get(position).changeText1(text);
        adapter.notifyItemChanged(position);
    }

    private void createExampleList(String Demo)
    {
        exemple_items = new ArrayList<>();
        if(Demo.equals("-"))
            return;
        StringTokenizer Tok = new StringTokenizer(Demo, "!");

        while (Tok.hasMoreElements())
        {
            int Id = Integer.valueOf(Tok.nextElement().toString());
            String NumberOfItems = Tok.nextElement().toString();

            model_adapter model_adapter = myDbItems.getItem(Id);
            if(model_adapter.getId() == -1)
                continue;
            String Item = model_adapter.getItemName();

            exemple_items.add(new exemple_item(Item, NumberOfItems, Id));
        }
    }

    private void buildRecyclerView()
    {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        adapter = new example_adapter(exemple_items);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new example_adapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                changeItem(position, "Clicked");
            }

            @Override
            public void onDeleteClick(int position) {
                removeItem(position);
            }
        });

    }

    public void onEditPress()
    {
        bOpenEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditDayWork.class);
                intent.putExtra("Day", intDay);
                intent.putExtra("Month", intMonth);
                intent.putExtra("Year", intYear);

                startActivity(intent);
            }
        });
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
                int NumberOfItems = Integer.valueOf(Tok.nextElement().toString());

                model_adapter model_adapter = myDbItems.getItem(Id);
                if(model_adapter.getId() == -1)
                    continue;
                float Price = model_adapter.getPrice();
                TotalIncome += NumberOfItems * Price;
            }
        }

        return Float.toString(TotalIncome);
    }

    /*
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {

        switch(menuItem.getItemId())
        {
            case R.id.item1:
                tvSelected.setText("item1");
                return true;
            case R.id.item2:
                tvSelected.setText("item2");
                return true;
            case R.id.item3:
                tvSelected.setText("item3");
                return true;
            case R.id.item4:
                tvSelected.setText("item4");
                return true;
            default:
                return false;
        }
    }

     */
}
