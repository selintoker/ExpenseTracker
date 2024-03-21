package com.example.ExpenseTracker;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ExpenseTracker.Classes.Category;
import com.example.ExpenseTracker.Classes.Expense;
import com.example.ExpenseTracker.DBHelper.CategoryDBHelper;
import com.example.ExpenseTracker.DBHelper.RecordsDBHelper;

import java.util.Calendar;

public class ExpenseAddActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    Spinner spinner;
    EditText ExpenseName;
    EditText Description;
    EditText Price;
    String Date;
    TextView dateText;
    boolean isAllFieldsChecked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_add);

        spinner = findViewById(R.id.spinner); // dropdown for categories
        CategoryDBHelper categoryDBHelper = new CategoryDBHelper(ExpenseAddActivity.this);
        ArrayAdapter arrayAdapter = new ArrayAdapter<Category>(ExpenseAddActivity.this,android.R.layout.simple_list_item_1,categoryDBHelper.GetAllCategories());
        spinner.setAdapter(arrayAdapter);
        // adds the categories from database to the spinner

        dateText = findViewById(R.id.textView);
        ExpenseName = findViewById(R.id.ExpenseName);
        Description = findViewById(R.id.ExpenseDescription);
        Price = findViewById(R.id.Price);
    }

    public void EnterDate(View view) { // when enter date button is clicked it opens a calendar
        DatePickerDialog datePickerDialog = new DatePickerDialog(ExpenseAddActivity.this,this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    public void AddExpenseToDB(View view) { // when add expense button is clicked adds to database
        isAllFieldsChecked = CheckAllFields();

        if (isAllFieldsChecked) {
            RecordsDBHelper dbHelper=new RecordsDBHelper(ExpenseAddActivity.this);
            Expense expense= new Expense();

            expense.ExpenseName=ExpenseName.getText().toString();
            expense.CategoryName=spinner.getSelectedItem().toString();
            expense.Description=Description.getText().toString();
            expense.Date=Date;
            expense.Price=Double.parseDouble(Price.getText().toString());
            dbHelper.AddExpenseToDB(expense); // what user entered to those spaces gets added to the expense table in database

            Intent intent = new Intent(ExpenseAddActivity.this, ExpenseRecordsActivity.class);
            intent.putExtra("category",expense.CategoryName);
            startActivity(intent);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // when date is chosen and ok button is clicked (text changes into the date picked)
        Toast.makeText(this, dayOfMonth+"/"+(month+1)+"/"+year, Toast.LENGTH_SHORT).show();
        Date=dayOfMonth+"/"+(month+1)+"/"+year;
        dateText.setText(Date);
    }

    // function that checks if all the text fields are filled by the user
    // when user clicks on the ADD button this function is triggered
    private boolean CheckAllFields() {
        if (ExpenseName.length() == 0) {
            ExpenseName.setError("This field is required");
            return false;
        }

        if (Description.length() == 0) {
            Description.setError("This field is required");
            return false;
        }

        if (Price.length() == 0) {
            Price.setError("This field is required");
            return false;
        }

        // after all validation return true.
        return true;
    }
}

