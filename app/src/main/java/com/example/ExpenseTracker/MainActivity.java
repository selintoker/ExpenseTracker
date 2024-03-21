package com.example.ExpenseTracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.ExpenseTracker.DBHelper.RecordsDBHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecordsDBHelper dbHelper=new RecordsDBHelper(this);
    }

    public void ExpenseClick(View view) { // when the EXPENSE TYPES button is clicked
        Intent intent = new Intent(MainActivity.this,ExpenseActivity.class);
        startActivity(intent);
    }

    public void AverageTotal(View view) { // when the AVERAGE/TOTAL button is clicked
        Intent intent = new Intent(MainActivity.this,AverageActivity.class);
        startActivity(intent);
    }

    public void FuturePredictionsActivity(View view) {
        Intent intent = new Intent(MainActivity.this,FuturePredictionsActivity.class);
        startActivity(intent);
    } // intent: an abstract description of an operation to be performed (launching activities)
}