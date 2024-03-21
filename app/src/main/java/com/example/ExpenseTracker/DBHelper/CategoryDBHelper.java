package com.example.ExpenseTracker.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.ExpenseTracker.Classes.Category;

import androidx.annotation.Nullable;


import java.util.ArrayList;
import java.util.List;

public class CategoryDBHelper extends DBHelper {

    public CategoryDBHelper(@Nullable Context context) {
        super(context);
    }

    public void AddCategoryToDB(Category category) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues(); // used to insert new row to table (Categories)
        cv.put("CATEGORYNAME",category.CategoryName);
        database.insert("Categories",null,cv); // no id given bc it will be created automatically
        database.close();
    }

    public List<Category> GetAllCategories() {
        List<Category> categories= new ArrayList<Category>();
        String query = "SELECT * FROM Categories";
        // A query is a request for data or information from a database table or combination of tables.
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery(query,null); // holds data from database, enables us to put them somewhere else
        if (cursor.moveToFirst()) { // to check if the cursor is empty or not, tries to go to first element
            do {
                int id = cursor.getInt(0); // category id in first column
                String name = cursor.getString(1); // category name in second column
                categories.add(new Category(id,name));
            }
            while(cursor.moveToNext());
        }
        return categories;
    }

    public void UpdateCategory(Category category) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("CATEGORYNAME",category.CategoryName);
        database.update("Categories",cv,"ID=?",new String[]{String.valueOf(category.ID)});
        database.close();
    }

    public void DeleteCategory(int ID) {
        SQLiteDatabase database = getWritableDatabase();
        database.delete("Categories","ID=?", new String[]{String.valueOf(ID)});
        database.close();
    }

    public boolean CheckIsDataAlreadyInDBorNot(String categoryName){
        // to check if a category with the same name already exists
        SQLiteDatabase database = getReadableDatabase();
        String query = "SELECT * FROM Categories WHERE CATEGORYNAME ='" + categoryName + "';";
        Cursor cursor = database.rawQuery(query, null);
        if(cursor.getCount() <= 0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }
}

