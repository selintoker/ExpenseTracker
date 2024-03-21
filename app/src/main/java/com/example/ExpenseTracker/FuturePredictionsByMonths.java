package com.example.ExpenseTracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.example.ExpenseTracker.DBHelper.RecordsDBHelper;

import java.util.List;

public class FuturePredictionsByMonths extends AppCompatActivity {

    EditText Average1;
    EditText Average2;
    EditText Average3;
    EditText Total1;
    EditText Total2;
    EditText Total3;
    EditText FutureTotal;
    EditText FutureAverage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_future_predictions_by_months);

        Intent intent = getIntent();
        int month=intent.getIntExtra("month",-1)+1;
        String year=intent.getStringExtra("year");
        RecordsDBHelper dbHelper= new RecordsDBHelper(FuturePredictionsByMonths.this);

        List<Double> expensesForMonth = dbHelper.GetTotalAndAverageOfPrevious3Months(month,Integer.valueOf(year));

        // takes average and total values from the list filled with them
        String total1 = First2DigitDouble(expensesForMonth.get(0));
        String average1 = First2DigitDouble(expensesForMonth.get(1));
        String total2 = First2DigitDouble(expensesForMonth.get(2));
        String average2 = First2DigitDouble(expensesForMonth.get(3));
        String total3 = First2DigitDouble(expensesForMonth.get(4));
        String average3 = First2DigitDouble(expensesForMonth.get(5));

        // calls Text views
        Average1 = findViewById(R.id.average1);
        Average2 = findViewById(R.id.average2);
        Average3 = findViewById(R.id.average3);
        Total1 = findViewById(R.id.total1);
        Total2 = findViewById(R.id.total2);
        Total3 = findViewById(R.id.total3);
        FutureAverage = findViewById(R.id.futureaverage);
        FutureTotal = findViewById(R.id.futuretotal);

        // fills Text views with values taken from the list
        Average1.setText("Average: " + average1);
        Average2.setText("Average: " + average2);
        Average3.setText("Average: " + average3);
        Total1.setText("Total: " + total1);
        Total2.setText("Total: " + total2);
        Total3.setText("Total: " + total3);

        double futureTotal = (expensesForMonth.get(0)+expensesForMonth.get(2)+expensesForMonth.get(4))/3;
        // average of total values of last 3 months
        double futureAverage = (expensesForMonth.get(1)+expensesForMonth.get(3)+expensesForMonth.get(5))/3;
        // average of average values of last 3 months
        FutureTotal.setText(First2DigitDouble(futureTotal));
        FutureAverage.setText(First2DigitDouble(futureAverage));
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