package com.example.ExpenseTracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.example.ExpenseTracker.Classes.Expense;
import com.example.ExpenseTracker.DBHelper.RecordsDBHelper;

import java.util.List;

public class AverageMonthActivities extends AppCompatActivity {

    EditText editText;
    EditText editText2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_average_month_activities);

        editText=findViewById(R.id.Total);
        editText2=findViewById(R.id.Average);

        Intent intent = getIntent();
        int month=intent.getIntExtra("month",-1)+1;
        String year=intent.getStringExtra("year");

        RecordsDBHelper helper = new RecordsDBHelper(AverageMonthActivities.this);
        List<Expense> expenses = helper.GetExpensesByMonthAndYear(month, Integer.valueOf(year));
        // calculations with price values entered by the user for a specific month and year
        double total = 0;
        double average = 0;

        for (int i=0;i<expenses.size();i++) {
            total=total+expenses.get(i).Price;
        }

        if (expenses.size()!=0) { // to avoid runtime error (dividing with zero)
            average = total / expenses.size();
        }

        editText.setText(First2DigitDouble(total));
        editText2.setText(First2DigitDouble(average));
    }

    public String First2DigitDouble(double value){ // rounding to two decimal places
        double roundUp = value + 0.005;
        int intValue = (int) roundUp;
        int digitCount = 1;
        while (intValue > 9){
            intValue = intValue / 10;
            digitCount++;
        }
        String stringValue = "" + roundUp;
        return stringValue.substring(0,digitCount + 3);
    }
}