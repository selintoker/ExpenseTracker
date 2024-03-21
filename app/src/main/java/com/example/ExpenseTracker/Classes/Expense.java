package com.example.ExpenseTracker.Classes;

public class Expense {
    public int ID;
    public String CategoryName;
    public String ExpenseName;
    public String Description;
    public String Date;
    public double Price;

    public Expense() { }

    public Expense(String categoryName,String expenseName,String description, String date, double price) {
        CategoryName=categoryName;
        ExpenseName=expenseName;
        Description=description;
        Date=date;
        Price=price;
    }

    public Expense(int Id,String categoryName,String expenseName,String description, String date, double price) {
        ID=Id;
        CategoryName=categoryName;
        ExpenseName=expenseName;
        Description=description;
        Date=date;
        Price=price;
    }

    @Override
    public String toString() { return ExpenseName; }
}

