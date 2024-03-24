package com.example.project;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class ExpenseTracker extends AppCompatActivity {

    Button btnSwitchToA4, btnAddRecord, btnRefreshCV;
    EditText edtAmountSpent, edtDescription, edtCategory, edtDate;
    DBHandler dbHandler;
    ArrayList<FinanceModel> financeModelArrayList;
    FinanceRVAdapter financeRVAdapter;
    RecyclerView financeRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.finance_activity);

        btnSwitchToA4 = findViewById(R.id.btnSwitchToA4);
        btnAddRecord = findViewById(R.id.btnAddRecord);
        edtAmountSpent = findViewById(R.id.edtAmountSpent);
        edtDescription = findViewById(R.id.edtDescription);
        edtCategory = findViewById(R.id.edtCategory);
        edtDate = findViewById(R.id.edtDate);
        btnRefreshCV = findViewById(R.id.btnRefreshCV);

        //dbhandler class and passing our context to it.
        dbHandler = new DBHandler(ExpenseTracker.this);

        // needed for viewing items in recycler view
        financeModelArrayList = new ArrayList<>();

        // ADDS FINANCIAL RECORDS TO TABLE
        btnAddRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double amountSpent = Double.parseDouble(edtAmountSpent.getText().toString());
                String description = edtDescription.getText().toString();
                String category = edtCategory.getText().toString();
                String date = edtDate.getText().toString();

                // validate if the numerical value is valid and throw an error if it isn't
                // cannot for the life of me figure out how to throw an exception if a letter is added
                if (amountSpent <= 0.0) {
                    Toast.makeText(ExpenseTracker.this, "Please enter amount spent!", Toast.LENGTH_LONG).show();
                    return;
                }

                // this adds the values from the above variables into the sqlite table
                dbHandler.addNewFinanceRecord(amountSpent, description, category, date);

                // display a successfully added message
                Toast.makeText(ExpenseTracker.this, "Financial record added.", Toast.LENGTH_LONG).show();
                edtAmountSpent.setText("");
                edtDescription.setText("");
                edtCategory.setText("");
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

        // switch to activity
        btnSwitchToA4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExpenseTracker.this, MainActivity5.class);
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
}