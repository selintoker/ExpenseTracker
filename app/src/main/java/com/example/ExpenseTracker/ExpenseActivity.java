package com.example.ExpenseTracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.ExpenseTracker.Classes.Category;
import com.example.ExpenseTracker.DBHelper.CategoryDBHelper;

public class ExpenseActivity extends AppCompatActivity {

    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        listView=findViewById(R.id.ExpenseList);

        CategoryDBHelper categoryDBHelper= new CategoryDBHelper(ExpenseActivity.this);
        ArrayAdapter arrayAdapter = new ArrayAdapter<Category>(ExpenseActivity.this,android.R.layout.simple_list_item_1,categoryDBHelper.GetAllCategories());
        listView.setAdapter(arrayAdapter);
        // an array adaptor (type, context, how its going to come out, data) is needed to fill the list bc list view doesn't accept normal list
        // adds the categories from database to list

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // when a list item is clicked
            @Override // writing over in a category
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // view = all stuff like button // position = position in the list
                Intent intent = new Intent(ExpenseActivity.this,ExpenseRecordsActivity.class);
                Category category = (Category) listView.getItemAtPosition(position); // object from that position = category
                intent.putExtra("category",category.CategoryName);
                intent.putExtra("id",category.ID);
                startActivity(intent);
            }
        });
    }

    public void AddCategory(View view) { // when the ADD CATEGORY button is clicked
        Intent intent = new Intent(ExpenseActivity.this,CategoryAddActivity.class);
        startActivity(intent);
    }
}