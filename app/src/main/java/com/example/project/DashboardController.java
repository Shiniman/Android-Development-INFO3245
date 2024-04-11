package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class DashboardController extends AppCompatActivity {

    // Java Attributes
    Button btnLogout, btnToFinanceActivity, btnToBudgetActivity;
    FirebaseAuth mAuth;
    FirebaseUser user;
    TextView txtUserDetails, txtTotalSpent, txtExpectedSaved, txtEstimatedMonthlySavings, txtExpectedSpent, txtExpectedMonthlySpending;

    // Instantiate DBHandler class
    DBHandler dbHandler; // = new DBHandler(DashboardController.this);

    // Array fills up because the method readFinances(); fetches all financial records from the database
    ArrayList<FinanceModel> financeRecords;

    // variable holds total amount spent
    double totalExpenses;
    double income = 0.0;
    double entertainmentBudget = 0.0;
    double utilitiesBudget = 0.0;
    double foodBudget = 0.0;
    double otherExpensesBudget = 0.0;
    double savingsPercentage = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);

        // Connecting Java attributes to xml attributes
        btnLogout = findViewById(R.id.btnLogout);
        btnToFinanceActivity = findViewById(R.id.btnToFinanceActivity);
        btnToBudgetActivity = findViewById(R.id.btnToBudgetActivity);
        txtUserDetails = findViewById(R.id.txtUserDetails);
        txtTotalSpent = findViewById(R.id.txtTotalSpent);
        txtExpectedSaved = findViewById(R.id.txtExpectedSaved);
        txtEstimatedMonthlySavings = findViewById(R.id.txtEstimatedMonthlySavings);
        txtExpectedSpent = findViewById(R.id.txtExpectedSpent);
        txtExpectedMonthlySpending = findViewById(R.id.txtExpectedMonthlySpending);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        dbHandler = new DBHandler(DashboardController.this);
        financeRecords = dbHandler.readFinances();
        totalExpenses = calculateTotalExpenses(financeRecords); // calcs total expenses

        // if currentUser() is null load login screen
        if (user == null) {
            Intent intent = new Intent(DashboardController.this, LoginController.class);
            startActivity(intent);
        }
        // debug to prove we logged in. This can be thrown in the dashboard under user details or some shit
        else {
            txtUserDetails.setText(user.getEmail());
        }

        calculateBudget();

        // LOGOUT BUTTON
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(DashboardController.this, LoginController.class);
                startActivity(intent);
            }
        });

        // SWITCH TO: Finance Activity
        btnToFinanceActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardController.this, ExpenseTracker.class);
                startActivity(intent);
            }
        });

        // SWITCH TO: Budgeting Activity
        btnToBudgetActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardController.this, BudgetController.class);
                startActivity(intent);
            }
        });

        // total expenses displayed
        txtTotalSpent.setText("$" + String.format("%.2f", totalExpenses));



    }

    // Calculates total expenses
    double calculateTotalExpenses(ArrayList<FinanceModel> expenses) {
        double total = 0.0;
        for (FinanceModel record : financeRecords) {
            // Add the spent amount of each record to the total expenses
            total += record.getSpentAmount();
        }
        return total;
    }

    public void calculateBudget() {
        ArrayList<BudgetModel> budgetList = dbHandler.readBudget();
        if (budgetList.size() > 0) {
            BudgetModel budget = budgetList.get(0);
            income = budget.getIncomeAmount();
            entertainmentBudget = budget.getEntertainment();
            utilitiesBudget = budget.getUtilities();
            foodBudget = budget.getFood();
            otherExpensesBudget = budget.getOther();
            savingsPercentage = budget.getSavingsPercentage();

            double yearlySavings = income * (savingsPercentage / 100);
            double monthlySavings = yearlySavings / 12;
            double spendingPercentage = 100 - savingsPercentage;
            double yearlySpending = income * (spendingPercentage / 100);
            double monthlySpending = yearlySpending / 12;

            txtExpectedSaved.setText("$" + String.format("%.2f", (yearlySavings)));
            txtEstimatedMonthlySavings.setText("$" + String.format("%.2f", (monthlySavings)));
            txtExpectedSpent.setText("$" + String.format("%.2f", (yearlySpending)));
            txtExpectedMonthlySpending.setText("$" + String.format("%.2f", (monthlySpending)));


            // Set other TextViews as needed
        }
    }
}