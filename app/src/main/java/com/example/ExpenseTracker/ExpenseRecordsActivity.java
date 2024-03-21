package com.example.ExpenseTracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.ExpenseTracker.Classes.Expense;
import com.example.ExpenseTracker.DBHelper.CategoryDBHelper;
import com.example.ExpenseTracker.DBHelper.RecordsDBHelper;

import java.util.List;

public class ExpenseRecordsActivity extends AppCompatActivity {

    ListView listView;
    double id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_records);

        String position;
        Intent intent = getIntent();
        position = intent.getStringExtra("category");
        id=intent.getIntExtra("id",-1);
        listView=findViewById(R.id.listview);

        RecordsDBHelper helper=new RecordsDBHelper(ExpenseRecordsActivity.this);

        List<Expense> expenses = helper.GetExpensesByCategory(position);

        ArrayAdapter arrayAdapter = new ArrayAdapter<Expense>(ExpenseRecordsActivity.this,android.R.layout.simple_list_item_1,expenses);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // when a list item is clicked
            @Override // writing over in a category
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ExpenseRecordsActivity.this,ExpenseRecordDetails.class);
                Expense expense = (Expense) listView.getItemAtPosition(position);
                intent.putExtra("id",expense.ID);
                intent.putExtra("category",expense.CategoryName);
                startActivity(intent);
            }
        });
    }

    public void AddNewRecord(View view) {
        Intent intent = new Intent(ExpenseRecordsActivity.this,ExpenseAddActivity.class);
        startActivity(intent);
    }


    public void DeleteCategory(View view) {
        CategoryDBHelper categoryDBHelper = new CategoryDBHelper(ExpenseRecordsActivity.this);
        categoryDBHelper.DeleteCategory((int)id);

        Intent intent = new Intent(ExpenseRecordsActivity.this,ExpenseActivity.class);
        startActivity(intent);
    }
}