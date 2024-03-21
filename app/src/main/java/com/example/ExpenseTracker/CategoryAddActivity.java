package com.example.ExpenseTracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.ExpenseTracker.Classes.Category;
import com.example.ExpenseTracker.DBHelper.CategoryDBHelper;

public class CategoryAddActivity extends AppCompatActivity {
    boolean doesNotExist = false;
    EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_add);
        editText=findViewById(R.id.categoryname);
    }

    public void AddCategory(View view) { // when the ADD CATEGORY button is clicked
        doesNotExist = CheckIfExists();
        if (doesNotExist) {
            CategoryDBHelper dbHelper = new CategoryDBHelper(CategoryAddActivity.this);
            Category category = new Category(editText.getText().toString()); // entered text is saved as a category
            dbHelper.AddCategoryToDB(category); // adds to database

            Intent intent = new Intent(CategoryAddActivity.this, ExpenseActivity.class);
            startActivity(intent); // goes back to previous page
        }
    }

    private boolean CheckIfExists(){
        CategoryDBHelper dbHelper=new CategoryDBHelper(CategoryAddActivity.this);
        if (dbHelper.CheckIsDataAlreadyInDBorNot(editText.getText().toString())){
            editText.setError("This category already exists"); // error message is displayed on screen
            return false;
        }
        return true;
    }
}