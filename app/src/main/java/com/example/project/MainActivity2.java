package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity2 extends AppCompatActivity {

    // Java Attributes
    Button btnLogout, btnSwitchToA3;
    FirebaseAuth mAuth;
    FirebaseUser user;
    TextView txtUserDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        // Connecting Java attributes to xml attributes
        btnLogout = findViewById(R.id.btnLogout);
        btnSwitchToA3 = findViewById(R.id.btnSwitchToA3);
        txtUserDetails = findViewById(R.id.txtUserDetails);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        // if currentUser() is null load login screen
        if (user == null) {
            Intent intent = new Intent(MainActivity2.this, LoginActivity.class);
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
                Intent intent = new Intent(MainActivity2.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // SWITCH TO A3
        btnSwitchToA3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, ExpenseTracker.class);
                startActivity(intent);
            }
        });
    }
}