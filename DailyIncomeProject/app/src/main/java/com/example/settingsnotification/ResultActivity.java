package com.example.settingsnotification;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.StringTokenizer;



public class ResultActivity extends AppCompatActivity {

    public TextView tvLuna;
    public TextView tvGraph2;
    public ImageView left2;
    public ImageView right2;

    public int IndexLuna;
    public int maxi;
    public int mini;

    public int intMonth;

    public String[] Luni = {"Ianuarie", "Februarie", "Martie", "Aprilie", "Mai", "Iunie", "Iulie", "August", "Septembrie", "Octombrie", "Noiembrie", "Decembrie", "Ianuarie", "Februarie", "Martie", "Aprilie", "Mai", "Iunie", "Iulie", "August", "Septembrie", "Octombrie", "Noiembrie", "Decembrie"};

    ActionBar actionBar;
    int color = Color.parseColor("#364A88");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(color));

        tvLuna = findViewById(R.id.Luna);
        tvGraph2 = findViewById(R.id.tvGraph2);
        left2 = findViewById(R.id.ivLeft2);
        right2 = findViewById(R.id.ivRight2);

        IndexLuna = Calendar.getInstance().get(Calendar.MONTH);
        maxi = 12 + IndexLuna;
        mini = IndexLuna + 1;
        IndexLuna = maxi;

        addOnButtonClicks();

        changeFragm();
    }

    private void addOnButtonClicks()
    {
        left2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IndexLuna --;
                IndexLuna = Math.min(IndexLuna, maxi);
                IndexLuna = Math.max(IndexLuna, mini);
                changeFragm();
            }
        });

        right2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IndexLuna ++;
                IndexLuna = Math.min(IndexLuna, maxi);
                IndexLuna = Math.max(IndexLuna, mini);
                changeFragm();
            }
        });
    }


    public void changeFragm()
    {
        tvLuna.setText(Luni[IndexLuna]);
        intMonth = IndexLuna%12 + 1;

        fragment_lunar fragment_lunar = new fragment_lunar(ResultActivity.this, intMonth);
        getSupportFragmentManager().beginTransaction().replace(R.id.FragmentPlace2, fragment_lunar).commit();
    }




}
