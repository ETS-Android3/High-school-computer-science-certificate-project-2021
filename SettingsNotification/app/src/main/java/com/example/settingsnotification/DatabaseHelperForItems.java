
package com.example.settingsnotification;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.Pair;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelperForItems extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "itemregister421.db";
    public static final String TABLE_NAME = "items_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "ITEM";
    public static final String COL_3 = "PRICE";



    public DatabaseHelperForItems(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, ITEM TEXT, PRICE REAL)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }



    public void insertData(model_adapter model_adapter)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_2, model_adapter.getItemName());
        contentValues.put(COL_3, model_adapter.getPrice());

        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }

    public List<model_adapter> getAllData()
    {
        List<model_adapter> result = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        Log.i("SSSSSSSSSSSSSSSSSSS", "DA");
        if(cursor.moveToFirst())
        {

            do {
                int ID = cursor.getInt(0);
                String ITEM = cursor.getString(1);
                float PRICE = cursor.getFloat(2);

                model_adapter model_adapter = new model_adapter(ID, ITEM, PRICE);
                Log.i("SSSSSSSSSSSSSSSSSSS", model_adapter.getId()+" "+model_adapter.getItemName()+" "+model_adapter.getPrice());

                result.add(model_adapter);
            }while(cursor.moveToNext());
        }


        cursor.close();
        db.close();
        return result;
    }

    public float getPrice(int Id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_1 + " = " + Id;
        Cursor cursor = db.rawQuery(query, null);

        float price = 0;

        if(cursor.moveToFirst())
            price = cursor.getFloat(2);
        cursor.close();
        db.close();

        return price;
    }

    public model_adapter getItem(int Id)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_1 + " = " + "'" + Id + "'";
        Cursor cursor = db.rawQuery(query, null);

        model_adapter model_adapter = new model_adapter(-1,"0", 0);
        if(cursor.moveToFirst())
        {
            model_adapter.setId(cursor.getInt(0));
            model_adapter.setItemName(cursor.getString(1));
            model_adapter.setPrice(cursor.getFloat(2));
        }

        cursor.close();
        db.close();
        return model_adapter;
    }

    public model_adapter getItem(String Item)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_2 + " = " + "'" + Item + "'";
        Cursor cursor = db.rawQuery(query, null);

        model_adapter model_adapter = new model_adapter(-1,"0", 0);
        if(cursor.moveToFirst())
        {
           // Log.i("SSSSSSSSSSSSSSSSSSSSSS", );
            model_adapter.setId(cursor.getInt(0));
            model_adapter.setItemName(cursor.getString(1));
            model_adapter.setPrice(cursor.getFloat(2));
        }

        cursor.close();
        db.close();
        return model_adapter;
    }

    public float getPrice(String Item)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_2 + " = " + "'" + Item + "'";
        Cursor cursor = db.rawQuery(query, null);

        float price = (float)0.137;

        if(cursor.moveToFirst())
            price = cursor.getFloat(2);
        cursor.close();
        db.close();

        return price;
    }



    public boolean changePrice(String Item, float newPrice)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, Item);
        contentValues.put(COL_3, newPrice);

        int changed = db.update(TABLE_NAME, contentValues,   COL_2 + " = " +  "'" + Item + "'", null);
        db.close();

        if(changed > 0)
            return true;
        return false;
    }

    public void deleteData(String Item)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        //String query = "DELETE FROM " + TABLE_NAME + " WHERE " + COL_2 + " = " +  "'" + Item + "'";

        db.delete(TABLE_NAME,  COL_2 + " = " + "'" + Item + "'", null);
        db.close();
    }

    public int getId(String Item)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL_2 + " = " + "'" + Item + "'";
        Cursor cursor = db.rawQuery(query, null);

        int id = -1;
        if(cursor.moveToFirst())
            id = cursor.getInt(0);

        cursor.close();
        db.close();
        return id;
    }

    /*
    public void odeleteData(String Item)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, "Item = ?", new String[] {Item} );
        db.close();
    }

    public void insertData(String Item, String Income)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1, Item);
        contentValues.put(COL_2, Income);

        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }

    public void editData(Context context, String NewItem, String NewIncome)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("select * from " + TABLE_NAME +
                " where " + COL_1 + " like '" + NewItem + "'", null);

        if(res.getCount() != 0)
        {
            Toast.makeText(context, "Obiect deja existent", Toast.LENGTH_SHORT).show();
            res.close();
            db.close();
            return;
        }
        insertData(NewItem, NewIncome);

        res.close();
        db.close();
    }

    public String getPrice(String Item)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("select * from " + TABLE_NAME +
                " where " + COL_1 + " LIKE '" + Item + "'", null);

        if(res.getCount() == 0)
        {
            res.close();
            db.close();
            return "0";
        }

        res.moveToFirst();
        String price = res.getString(1);

        res.close();
        db.close();
        return price;
    }

    public ArrayList<Pair<String, String>> getAllItems()
    {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);

        ArrayList<Pair<String, String>> arrayList = new ArrayList<>();

        if(res.getCount() == 0)
        {
            res.close();
            db.close();
            return arrayList;
        }

        while(res.moveToNext())
        {
            String Item = res.getString(0);
            String Cost = res.getString(1);
            arrayList.add(new Pair(Item, Cost));
        }

        res.close();
        db.close();
        return arrayList;
    }

     */

}
