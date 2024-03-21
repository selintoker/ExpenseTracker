package com.example.ExpenseTracker.DBHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper { // inheritance
    public DBHelper(@Nullable Context context) {
        super(context,"Expenses.db", null, 1);
    }
    // constructor works when this activity starts
    // (activity, name, ...)

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query="CREATE TABLE IF NOT EXISTS Categories (ID INTEGER PRIMARY KEY AUTOINCREMENT, CATEGORYNAME TEXT)";
        db.execSQL(query);
        // query = statement used during processes related to database
        // creates a table only if it does not exist before
        // (name and type of each row)
        // auto-increment allows a unique number (ID) to be generated automatically when a new record is inserted into the table

        String query2="CREATE TABLE IF NOT EXISTS Expenses (ID INTEGER PRIMARY KEY AUTOINCREMENT, CATEGORYNAME TEXT, EXPENSENAME TEXT, DESCRIPTION TEXT, PRICE INTEGER, DATEOFEXPENSE TEXT)";
        db.execSQL(query2);


        String query3="CREATE TABLE IF NOT EXISTS Photos (ID INTEGER PRIMARY KEY AUTOINCREMENT,EXPENSEID INTEGER, PHOTOADDRESS TEXT)";
        db.execSQL(query3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

