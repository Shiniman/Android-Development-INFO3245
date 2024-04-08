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
    Button btnLogout, btnSwitchToA3;
    FirebaseAuth mAuth;
    FirebaseUser user;
    TextView txtUserDetails;
    TextView txtTotalSpent;

    // Instantiate DBHandler class
    DBHandler dbHandler; // = new DBHandler(DashboardController.this);

    // Array fills up because the method readFinances(); fetches all financial records from the database
    ArrayList<FinanceModel> financeRecords;

    // variable holds total amount spent
    double totalExpenses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);

        // Connecting Java attributes to xml attributes
        btnLogout = findViewById(R.id.btnLogout);
        btnSwitchToA3 = findViewById(R.id.btnSwitchToA3);
        txtUserDetails = findViewById(R.id.txtUserDetails);
        txtTotalSpent = findViewById(R.id.txtTotalSpent);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        dbHandler = new DBHandler(DashboardController.this);
        financeRecords = dbHandler.readFinances();
        // calcs total expenses
        totalExpenses = calculateTotalExpenses(financeRecords);

        // if currentUser() is null load login screen
        if (user == null) {
            Intent intent = new Intent(DashboardController.this, LoginController.class);
            startActivity(intent);
        }
        // debug to prove we logged in. This can be thrown in the dashboard under user details or some shit
        else {
            txtUserDetails.setText(user.getEmail());
        }

        // LOGOUT BUTTON
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(DashboardController.this, LoginController.class);
                startActivity(intent);
            }
        });

        // SWITCH TO A3
        btnSwitchToA3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardController.this, ExpenseTracker.class);
                startActivity(intent);
            }
        });

        // total expenses displayed
        txtTotalSpent.setText("Total Expenses: $" + totalExpenses);
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
}