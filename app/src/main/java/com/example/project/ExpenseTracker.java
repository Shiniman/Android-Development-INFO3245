package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class ExpenseTracker extends AppCompatActivity {

    Button btnAddRecord, btnRefreshCV, btnFinanceToHome, btnDatePicker;
    EditText edtAmountSpent, edtDescription, edtDate;
    Spinner spnrCategory;
    DBHandler dbHandler;
    ArrayList<FinanceModel> financeModelArrayList;
    FinanceRVAdapter financeRVAdapter;
    RecyclerView financeRV;
    String categoryChosen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finance_activity);

        btnAddRecord = findViewById(R.id.btnAddRecord);
        edtAmountSpent = findViewById(R.id.edtAmountSpent);
        edtDescription = findViewById(R.id.edtDescription);
        spnrCategory = findViewById(R.id.spnrCategory);
        edtDate = findViewById(R.id.edtDate);
        btnRefreshCV = findViewById(R.id.btnRefreshCV);
        btnFinanceToHome = findViewById(R.id.btnFinanceToHome);
        btnDatePicker = findViewById(R.id.btnDatePicker);

        //dbhandler class and passing our context to it.
        dbHandler = new DBHandler(ExpenseTracker.this);

        // needed for viewing items in recycler view
        financeModelArrayList = new ArrayList<>();

        // Spinner Array: Holds drop down menu items for categories
        ArrayList<String> categories = new ArrayList<>();
            categories.add("Select Category"); // Hint item
            categories.add("Entertainment");
            categories.add("Utilities");
            categories.add("Food");
            categories.add("Other");

        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spnrCategory.setAdapter(adapter);
        // Obtain Spinner Input
        spnrCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // these if statements make the "hint" unselectable
                if (position == 0) {
                    spnrCategory.setSelection(0, false); // Set the selection without triggering listener
                    categoryChosen = "Other"; // Automatically gives it the other category for categorization purposes
                } else {
                    // if a value is selected give it to categoryChosen
                    categoryChosen = parent.getItemAtPosition(position).toString();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // this opens a calendar dialog
        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        // ADDS FINANCIAL RECORDS TO TABLE
        btnAddRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double amountSpent = Double.parseDouble(edtAmountSpent.getText().toString());
                String description = edtDescription.getText().toString();
                String date = edtDate.getText().toString();
                //String amountSpentText = edtAmountSpent.getText().toString();

                // validate if the numerical value is valid and throw an error if it isn't
                // cannot for the life of me figure out how to throw an exception if a letter is added
                if (amountSpent <= 0.0) {
                    Toast.makeText(ExpenseTracker.this, "Please enter amount spent!", Toast.LENGTH_LONG).show();
                    return;
                }
                /* can't figure out how to do a null/is empty error. Don't have enough time to figure it out
                if (amountSpentText == null || amountSpentText.isEmpty()) {
                    Toast.makeText(ExpenseTracker.this, "Please enter amount spent!", Toast.LENGTH_LONG).show();
                    return;
                }*/

                // this adds the values from the above variables into the sqlite table
                dbHandler.addNewFinanceRecord(amountSpent, description, categoryChosen, date);

                // display a successfully added message
                Toast.makeText(ExpenseTracker.this, "Financial record added.", Toast.LENGTH_LONG).show();
                edtAmountSpent.setText("");
                edtDescription.setText("");
                spnrCategory.setSelection(0);
                edtDate.setText("");
            }
        });

        viewFinancialRecords(); // upon launching display
        // button refreshes the card view
        btnRefreshCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFinancialRecords();
            }
        });

        // SWITCH TO: Dashboard
        btnFinanceToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExpenseTracker.this, DashboardController.class);
                startActivity(intent);
            }
        });
    }

    // Lets you view the cards
    public void viewFinancialRecords() {
        // Obtain finance record array list from the DBHandler class
        financeModelArrayList = dbHandler.readFinances();

        // Pass array list to adapter class
        financeRVAdapter = new FinanceRVAdapter(financeModelArrayList, ExpenseTracker.this);
        financeRV = findViewById(R.id.rvFinanceStats);

        // setting layout manager for recycler view
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ExpenseTracker.this, RecyclerView.VERTICAL, false);
        financeRV.setLayoutManager((linearLayoutManager));

        // set adapter to recycler view
        financeRV.setAdapter(financeRVAdapter);
    }

    //https://www.youtube.com/watch?v=guTycx3L9I4
    public void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(ExpenseTracker.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String selectedDate = year + "-" + (month + 1) + "-" + dayOfMonth;
                        // Updates edtDate plainText with the selected date
                        edtDate.setText(selectedDate);
                    }
                }, year, month, dayOfMonth);
        datePickerDialog.show();
    }
}