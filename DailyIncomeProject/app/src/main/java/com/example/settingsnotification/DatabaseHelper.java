package com.example.settingsnotification;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ScrollView;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "workregister121.db";
    public static final String TABLE_NAME = "everydaywork_table";
    public static final String COL_1 = "DAY";
    public static final String COL_2 = "MONTH";
    public static final String COL_3 = "YEAR";
    public static final String COL_4 = "ITEMS";
    public static final String COL_5 = "MESAJ";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (DAY INT, MONTH INT, YEAR INT, ITEMS TEXT, MESAJ TEXT)");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public void insertData(int Day, int Month, int Year, String Items, String Mesaj)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, Day);
        contentValues.put(COL_2, Month);
        contentValues.put(COL_3, Year);
        contentValues.put(COL_4, Items);
        contentValues.put(COL_5, Mesaj);

        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }

    public void deleteData(int Day, int Month, int Year)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, "Day = ? and Month = ? and Year = ?",
                new String[]{Integer.toString(Day), Integer.toString(Month), Integer.toString(Year)});
        db.close();
    }

    public String getItems(int Day, int Month, int Year)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("select * from " + TABLE_NAME +
                " where " + COL_1 + " like '" + Day + "' and " +
                COL_2 + " like '" + Month + "' and " +
                COL_3 + " like '" + Year + "'", null);

        if(res.getCount() == 0)
        {
            res.close();
            db.close();
            return "-";
        }

        res.moveToFirst();
        String result = res.getString(3);

        res.close();
        db.close();
        return result;
    }

    public ArrayList<String> GetAllItems()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);

        ArrayList<String> answ = new ArrayList<>();
        if(res.getCount() == 0)
        {
            res.close();
            db.close();
            return answ;
        }

        res.moveToFirst();
        while(!res.isAfterLast())
        {
            answ.add(res.getInt(0) + " " + res.getInt(1) + " " + res.getInt(2) + " " + res.getString(3));
            res.moveToNext();
        }

        res.close();
        db.close();
        return answ;
    }

    public String getMesaj(int Day, int Month, int Year)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("select * from " + TABLE_NAME +
                " where " + COL_1 + " like '" + Day + "' and " +
                COL_2 + " like '" + Month + "' and " +
                COL_3 + " like '" + Year + "'", null);

        if(res.getCount() == 0)
        {
            res.close();
            db.close();
            return " ";
        }

        res.moveToFirst();
        String result = res.getString(4);

        res.close();
        db.close();
        return result;
    }
}
