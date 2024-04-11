package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class BudgetController extends AppCompatActivity {

    Button btnBudgetToHome, btnSaveBudgetInfo;
    TextView edtNetIncome, edtEntertainment, edtUtilities, edtFood, edtOtherExpenses, edtSavingsGoal;
    DBHandler dbHandler;
    ArrayList<BudgetModel> budgetModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.budget_activity);

        edtNetIncome = findViewById(R.id.edtNetIncome);
        edtEntertainment = findViewById(R.id.edtEntertainment);
        edtUtilities = findViewById(R.id.edtUtilities);
        edtFood = findViewById(R.id.edtFood);
        edtOtherExpenses = findViewById(R.id.edtOtherExpenses);
        edtSavingsGoal = findViewById(R.id.edtSavingsGoal);
        btnSaveBudgetInfo = findViewById(R.id.btnSaveBudgetInfo);
        btnBudgetToHome = findViewById(R.id.btnBudgetToHome);

        // Initialize DBHandler
        dbHandler = new DBHandler(BudgetController.this);

        budgetModelArrayList = new ArrayList<>();
        btnSaveBudgetInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBudget();
            }
        });

        btnBudgetToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BudgetController.this, LoginController.class);
                startActivity(intent);
            }
        });
    }

    public void saveBudget() {
        double netIncome = Double.parseDouble(edtNetIncome.getText().toString());
        double entertainmentBudget = Double.parseDouble(edtEntertainment.getText().toString());
        double utilitiesBudget = Double.parseDouble(edtUtilities.getText().toString());
        double foodBudget = Double.parseDouble(edtFood.getText().toString());
        double otherExpensesBudget = Double.parseDouble(edtOtherExpenses.getText().toString());
        double savingsGoal = Double.parseDouble(edtSavingsGoal.getText().toString());

        // Add the user-entered values to the new table in the database
        dbHandler.addBudget(netIncome, entertainmentBudget, utilitiesBudget, foodBudget, otherExpensesBudget, savingsGoal);

        // Display a success message
        Toast.makeText(BudgetController.this, "Budget information saved successfully", Toast.LENGTH_SHORT).show();

        // Clear the input fields
        edtNetIncome.setText("");
        edtEntertainment.setText("");
        edtUtilities.setText("");
        edtFood.setText("");
        edtOtherExpenses.setText("");
        edtSavingsGoal.setText("");
    }
}