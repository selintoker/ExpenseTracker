package com.example.ExpenseTracker.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.ExpenseTracker.Classes.Expense;

import java.util.ArrayList;
import java.util.List;

public class RecordsDBHelper extends DBHelper {

    public RecordsDBHelper(@Nullable Context context) {
        super(context);
    }
    
    public void AddExpenseToDB(Expense expense) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues(); // used to insert new row to table (Expenses)
        cv.put("CATEGORYNAME",expense.CategoryName); // works like a dictionary
        cv.put("EXPENSENAME",expense.ExpenseName);
        cv.put("DESCRIPTION",expense.Description);
        cv.put("PRICE",expense.Price);
        cv.put("DATEOFEXPENSE",expense.Date);
        database.insert("Expenses",null,cv); // (which table, ... , what will it put)
        database.close(); // should close to read it
    }

    public List<Expense> GetExpensesFromDB() {
        List<Expense> expenses = new ArrayList<Expense>();
        SQLiteDatabase database = getReadableDatabase();
        String query="SELECT * FROM Expenses";
        // A query is a request for data or information from a database table or combination of tables.
        Cursor cursor = database.rawQuery(query,null); // holds data from database, enables us to put them somewhere else
        if (cursor.moveToFirst()) { // to check if the cursor is empty or not, tries to go to first element
            do {
                int ID=cursor.getInt(0);
                String categoryName=cursor.getString(1);
                String expenseName=cursor.getString(2);
                String description = cursor.getString(3);
                int price=cursor.getInt(4);
                String date=cursor.getString(5);
                Expense expense = new Expense(ID,categoryName,expenseName,description,date,price);
                expenses.add(expense);
            }
            while(cursor.moveToNext());
        }
        return expenses;
    }

    public List<Expense> GetExpensesByCategory(String CategoryName) {
        List<Expense> expenses = new ArrayList<Expense>();
        SQLiteDatabase database = getReadableDatabase();
        String query="SELECT * FROM Expenses WHERE CATEGORYNAME='"+CategoryName+"';";
        // A query is a request for data or information from a database table or combination of tables.
        Cursor cursor = database.rawQuery(query,null); // holds data from database, enables us to put them somewhere else
        if (cursor.moveToFirst()) { // to check if the cursor is empty or not, tries to go to first element
            do {
                int ID=cursor.getInt(0); // expense id in first column
                String categoryName=cursor.getString(1); // category name in second column
                String expenseName=cursor.getString(2);
                String description = cursor.getString(3);
                int price=cursor.getInt(4);
                String date=cursor.getString(5);
                Expense expense = new Expense(ID,categoryName,expenseName,description,date,price);
                expenses.add(expense);
            }
            while(cursor.moveToNext());
        }return expenses;
    }

    public void DeleteExpense(int id)
    {
        SQLiteDatabase database = getWritableDatabase();
        database.delete("Expenses","ID=?",new String[]{String.valueOf(id)});
        database.close();
    }

    public void Update(Expense expense) {
        SQLiteDatabase database= getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("CATEGORYNAME",expense.CategoryName);
        cv.put("EXPENSENAME",expense.ExpenseName);
        cv.put("DESCRIPTION",expense.Description);
        cv.put("PRICE",expense.Price);
        cv.put("DATEOFEXPENSE",expense.Date);
        database.update("Expenses",cv,"ID=?",new String[]{String.valueOf(expense.ID)});
        database.close();
    }

    public Expense GetExpense(int id) {
        Expense expense = null;
        SQLiteDatabase database = getReadableDatabase();
        String query="SELECT * FROM Expenses WHERE ID='"+id+"';";
        Cursor cursor = database.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            do {
                int ID=cursor.getInt(0);
                String categoryName = cursor.getString(1);
                String expenseName = cursor.getString(2);
                String description = cursor.getString(3);
                double price = cursor.getDouble(4);
                String date = cursor.getString(5);
                expense = new Expense(ID,categoryName,expenseName,description,date,price);
            }
            while(cursor.moveToNext());
        }
        return expense;
    }

    public List<Expense> GetExpensesByMonth(int month) {
        String Month=String.valueOf(month);
        List<Expense> expenses = new ArrayList<Expense>();
        SQLiteDatabase database = getReadableDatabase();
        String query="SELECT * FROM Expenses WHERE DATEOFEXPENSE LIKE '%/" + Month + "/%';"; //Year eklenicek
        Cursor cursor = database.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            do {
                int ID=cursor.getInt(0);
                String categoryName=cursor.getString(1);
                String expenseName=cursor.getString(2);
                String description = cursor.getString(3);
                int price=cursor.getInt(4);
                String date=cursor.getString(5);
                Expense expense = new Expense(ID,categoryName,expenseName,description,date,price);
                expenses.add(expense);
            }
            while(cursor.moveToNext());
        }
        return expenses;
    }


    public List<Expense> GetExpensesByMonthAndYear(int month, int year) {
        String Month = String.valueOf(month);
        String Year = String.valueOf(year);
        String monthyear = Month+"/" +Year;
        List<Expense> expenses = new ArrayList<Expense>();
        SQLiteDatabase database = getReadableDatabase();
        String query="SELECT * FROM Expenses WHERE DATEOFEXPENSE LIKE '%"+ monthyear +"'";
        Cursor cursor = database.rawQuery(query,null);
        if (cursor.moveToFirst()) {
            do {
                int ID=cursor.getInt(0);
                String categoryName=cursor.getString(1);
                String expenseName=cursor.getString(2);
                String description = cursor.getString(3);
                int price=cursor.getInt(4);
                String date=cursor.getString(5);
                Expense expense = new Expense(ID,categoryName,expenseName,description,date,price);
                expenses.add(expense);
            }
            while(cursor.moveToNext());
        } return expenses;
    }



    public List<Double> GetTotalAndAverageOfPrevious3Months(int month, int year) {
        List<Double> data = new ArrayList<>();
        // total and average values are added to this list
        for (int i=0;i<3;i++) {
            int yeardata = 0;
            int monthdata = 0;
            month = month-1;
            if (month -3 <= 0) { // if three months ago is in last year
                int remainder = month;
                yeardata=year-1;
                monthdata = 12 + remainder;
            }
            else { // if three months ago is in the same year
                yeardata = year;
                monthdata = month;
            }
            double total =0;
            double average = 0;

            List<Expense> expenseForMonth = GetExpensesByMonthAndYear(monthdata,yeardata);
            // list of expenses in a specific month and year
            for (Expense item:expenseForMonth) {
                total+=item.Price; // all prices are added up
            }
            if (expenseForMonth.size()!=0) { // to avoid runtime error, dividing by zero
                average=total/expenseForMonth.size(); // average of prices is calculated
            }
            else{
                average=0;
            }
            data.add(total);
            data.add(average);
        }
        return data;
    }
}
