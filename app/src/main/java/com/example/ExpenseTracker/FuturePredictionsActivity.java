package com.example.ExpenseTracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class FuturePredictionsActivity extends AppCompatActivity {

    Spinner spinner;
    Spinner spinner2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_future_predictions);

        spinner = findViewById(R.id.spinner4);
        spinner2 = findViewById(R.id.spinner5);

        List<String> months = new ArrayList<String>();
        months.add("January");
        months.add("February");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("August");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");

        List<String> years= new ArrayList<String>();
        years.add("2021");
        years.add("2022");
        years.add("2023");
        years.add("2024");
        years.add("2025");

        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<String>(FuturePredictionsActivity.this,android.R.layout.simple_list_item_1,months);
        spinner.setAdapter(arrayAdapter);

        ArrayAdapter<String> arrayAdapter2= new ArrayAdapter<String>(FuturePredictionsActivity.this,android.R.layout.simple_list_item_1,years);
        spinner2.setAdapter(arrayAdapter2);

    }

    public void FuturePredictionsByMonths(View view) {
        Intent intent = new Intent(FuturePredictionsActivity.this,FuturePredictionsByMonths.class);
        intent.putExtra("month",spinner.getSelectedItemPosition());
        intent.putExtra("year",spinner2.getSelectedItem().toString());
        startActivity(intent);
    }
}