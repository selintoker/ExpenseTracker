package com.example.ExpenseTracker.Classes;

public class Category {
    public int ID;
    public String CategoryName;
    Category() {

    }

    public Category(String categoryName) {
        CategoryName=categoryName;
    }

    public Category(int Id, String categoryName) {
        ID=Id;
        CategoryName=categoryName;
    }

    @Override
    public String toString() { return CategoryName; }
    // needs to print the names on screen as strings, not as object

}

