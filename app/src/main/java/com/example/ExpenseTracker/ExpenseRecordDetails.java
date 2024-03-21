package com.example.ExpenseTracker;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ExpenseTracker.Classes.Category;
import com.example.ExpenseTracker.Classes.Expense;
import com.example.ExpenseTracker.Classes.Photo;
import com.example.ExpenseTracker.DBHelper.CategoryDBHelper;
import com.example.ExpenseTracker.DBHelper.PhotoDBHelper;
import com.example.ExpenseTracker.DBHelper.RecordsDBHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class ExpenseRecordDetails extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    Spinner spinner;
    int ID;
    String Date;
    Expense expense;
    EditText editTextName;
    EditText editTextDesc;
    EditText editTextPrice;
    String category;
    TextView textView;
    ImageView imageView;

    String imageurl; // kaynağın cihazda tutulduğu konuma nasıl ulaşılacağını gösterir
    Uri imageUri; // kaynağı tam olarak işaret eder (kapsayıcı)
    Bitmap thumbnail; // a bitmap variable for storing the images
    Bitmap scaledbmp;
    ActivityResultLauncher<Intent> activityResultLauncher; // kameraya intent
    private static final int TAKE_PICTURE = 1;

    // the width and height for the PDF file
    int pageHeight = 1120;
    int pageWidth = 792;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_record_details);

        Intent intent = getIntent();

        spinner = findViewById(R.id.spinner2);
        editTextName = findViewById(R.id.ExpenseName);
        editTextDesc = findViewById(R.id.Description);
        editTextPrice = findViewById(R.id.Price);
        textView = findViewById(R.id.dateTextUpdate);
        imageView = findViewById(R.id.imageView);

        RecordsDBHelper recordsDBHelper = new RecordsDBHelper(ExpenseRecordDetails.this);
        CategoryDBHelper categoryDBHelper = new CategoryDBHelper(ExpenseRecordDetails.this);
        PhotoDBHelper photoDBHelper = new PhotoDBHelper(ExpenseRecordDetails.this);

        ID=intent.getIntExtra("id",-1);
        expense = recordsDBHelper.GetExpense(ID);

        Photo photo = photoDBHelper.GetPhoto(expense.ID);
        Bitmap expensePhoto= null;
        if (photo.PhotoAddress!=null) { // to check that there *is* a photo address
            try {
                expensePhoto = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(photo.PhotoAddress));
                // fotoğraf adresinden kendisi gelip expensePhoto'ya ekleniyor
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        imageView.setImageBitmap(expensePhoto); // ekrandaki alana fotoyu koyuyor

        // kameradan istenen intent
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    try {
                        thumbnail = MediaStore.Images.Media.getBitmap(
                                getContentResolver(), imageUri);
                        imageView.setImageBitmap(thumbnail);
                        imageurl = getRealPathFromURI(imageUri);
                        if (photoDBHelper.GetCountOfSpecificExpense(ID)==0) { // if expense has no photo
                            photoDBHelper.AddPhoto(ID,imageUri.toString());
                        }
                        else { // if expense already has a photo
                            Photo photo = photoDBHelper.GetPhoto(ID);
                            photo.PhotoAddress=imageUri.toString();
                            photoDBHelper.UpdatePhoto(photo);
                        }
                        Toast.makeText(ExpenseRecordDetails.this, imageurl, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        editTextName.setText(expense.ExpenseName);
        editTextDesc.setText(expense.Description);
        editTextPrice.setText(String.valueOf(expense.Price));

        ArrayAdapter arrayAdapter = new ArrayAdapter<Category>(ExpenseRecordDetails.this,android.R.layout.simple_list_item_1,categoryDBHelper.GetAllCategories());
        spinner.setAdapter(arrayAdapter);

        category = expense.CategoryName;
        spinner.setSelection(FindCategoryPosition(category,categoryDBHelper.GetAllCategories()));
        textView.setText(expense.Date);

        expense=new Expense();
        expense.ID=ID;

        // for creating PDF
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1);
        }

    }
    public void EnterDate(View view) { // when enter date button is clicked it opens a calendar
        DatePickerDialog datePickerDialog = new DatePickerDialog(ExpenseRecordDetails.this,this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        // when date is chosen and ok button is clicked (text changes into the date picked)
        Toast.makeText(this, dayOfMonth+"/"+(month+1)+"/"+year, Toast.LENGTH_SHORT).show();
        Date=dayOfMonth+"/"+(month+1)+"/"+year;
        textView.setText(Date);
    }

    // iteration (iterating over categories)
    public int FindCategoryPosition(String category, List<Category> categoryList) {
        int position=0;
        for (Category item:categoryList) {
            String c = item.CategoryName;
            if (c.equals(category)) {
                break;
            }
            else {
                position++;
            }
        }
        return position;
    }

    public void DeleteRecord(View view) {
        RecordsDBHelper dbHelper = new RecordsDBHelper(ExpenseRecordDetails.this);
        dbHelper.DeleteExpense(ID);
        Intent intent = new Intent(ExpenseRecordDetails.this,ExpenseRecordsActivity.class);
        intent.putExtra("category",category);
        startActivity(intent);
    }

    public void UpdateRecord(View view) {
        RecordsDBHelper dbHelper= new RecordsDBHelper(ExpenseRecordDetails.this);
        expense.ExpenseName=editTextName.getText().toString();
        expense.Description=editTextDesc.getText().toString();
        expense.Price=Double.parseDouble(editTextPrice.getText().toString());
        expense.CategoryName=spinner.getSelectedItem().toString();
        expense.Date=textView.getText().toString();
        dbHelper.Update(expense);
        Intent intent = new Intent(ExpenseRecordDetails.this,ExpenseRecordsActivity.class);
        intent.putExtra("category",expense.CategoryName);
        startActivity(intent);
    }

    public String getRealPathFromURI(Uri contentUri) { //
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void TakePhoto(View view) {
        //
        if (ContextCompat.checkSelfPermission(ExpenseRecordDetails.this, Manifest.permission.CAMERA )== PackageManager.PERMISSION_DENIED ){
            requestPermissions(new String[] {Manifest.permission.CAMERA},1);
        }
        if (ContextCompat.checkSelfPermission(ExpenseRecordDetails.this, Manifest.permission.WRITE_EXTERNAL_STORAGE )== PackageManager.PERMISSION_DENIED ){
            requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
        }
        //
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        activityResultLauncher.launch(intent);
    }

    // for PDF
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createPDF() {
        Paint title = new Paint(); // adding text in PDF
        Paint paint = new Paint(); // drawing shapes

        PdfDocument pdfDocument = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth,pageHeight,1).create();
        // page info is added to the PDF file and after that we call it to create PDF
        PdfDocument.Page page = pdfDocument.startPage(pageInfo); // setting start page for the PDF file
        Canvas canvas = page.getCanvas();

        //text features
        title.setTypeface(Typeface.create(Typeface.DEFAULT,Typeface.NORMAL));
        title.setTextSize(30);
        title.setColor(ContextCompat.getColor(this,R.color.black));
        title.setTextAlign(Paint.Align.CENTER);

        Intent intent = getIntent();
        RecordsDBHelper recordsDBHelper = new RecordsDBHelper(ExpenseRecordDetails.this);
        PhotoDBHelper photoDBHelper = new PhotoDBHelper(ExpenseRecordDetails.this);

        ID=intent.getIntExtra("id",-1);
        expense = recordsDBHelper.GetExpense(ID);
        Photo photo = photoDBHelper.GetPhoto(expense.ID);
        Bitmap expensePhoto= null;
        if (photo.PhotoAddress!=null) {
            try {
                expensePhoto = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(photo.PhotoAddress));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        scaledbmp = Bitmap.createScaledBitmap(expensePhoto, 400, 500, false);
        // takes the photo from the ID of the expense and rescales it

        canvas.drawText(String.valueOf(expense.ExpenseName),396,100,title);
        canvas.drawText(String.valueOf(expense.Description),396,140, title);
        canvas.drawText(String.valueOf(expense.Price),396,180, title);
        canvas.drawText(String.valueOf(expense.CategoryName),396,220, title);
        canvas.drawText(String.valueOf(expense.Date),396,260, title);

        // draw the image on the PDF file
        canvas.drawBitmap(scaledbmp, 220, 280, paint);
        pdfDocument.finishPage(page); // finished after adding all the attributes

        expense=new Expense();
        expense.ID=ID;

        ContextWrapper wrapper = new ContextWrapper(getApplicationContext());
        File downloadsDir = wrapper.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
        File file = new File(downloadsDir,"ExpenseRecord" + ".pdf");

        try {
            pdfDocument.writeTo(new FileOutputStream(file));
            Toast.makeText(ExpenseRecordDetails.this, "PDF Created", Toast.LENGTH_SHORT).show();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        pdfDocument.close();

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void CreatePdfDocument(View view) {
        ActivityCompat.requestPermissions(this, new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 200);
        // calling method to generate the PDF file
        createPDF();
    }

}