package com.example.ExpenseTracker.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.ExpenseTracker.Classes.Photo;

public class PhotoDBHelper extends DBHelper{

    public PhotoDBHelper(@Nullable Context context) {
        super(context);
    }

    public void AddPhoto(int expenseId, String address) {
        SQLiteDatabase database= getWritableDatabase();
        ContentValues values = new ContentValues(); // used to insert new row to table (Categories)
        values.put("EXPENSEID",expenseId);
        values.put("PHOTOADDRESS",address);
        database.insert("Photos",null,values);
        database.close();
    }

    public Photo GetPhoto(int expenseId){ // expense'in id'sine göre ona ait olan fotoyu çekiyor
        SQLiteDatabase database = getReadableDatabase();
        String query = "Select * From Photos Where EXPENSEID="+expenseId;
        // A query is a request for data or information from a database table or combination of tables.
        Cursor cursor = database.rawQuery(query,null); // holds data from database, enables us to put them somewhere else
        Photo photo = new Photo();
        cursor.moveToFirst();
        try{
            photo.ID=cursor.getInt(0);
            photo.ExpenseID=cursor.getInt(1);
            photo.PhotoAddress=cursor.getString(2);
        }
        catch (Exception e)
        {
           e.printStackTrace(); // will pinpoint the exact line in which the method raised the exception
        }
        return photo;
    }

    public void UpdatePhoto(Photo photo) { //eski fotoğrafın yerine yeni fotoğraf gelince
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("PHOTOADDRESS",photo.PhotoAddress);
        database.update("Photos",cv,"EXPENSEID=?",new String[]{String.valueOf(photo.ExpenseID)});
        database.close();
    }

    public int GetCountOfSpecificExpense(int id) { // to check if an expense has a photo
        SQLiteDatabase database = getReadableDatabase();
        String query = "Select * From Photos Where EXPENSEID="+id;
        Cursor cursor = database.rawQuery(query,null);
        return cursor.getCount(); // 0 if no photo 1 if already has a photo
        // later called in ExpenseRecordDetails
    }
}
