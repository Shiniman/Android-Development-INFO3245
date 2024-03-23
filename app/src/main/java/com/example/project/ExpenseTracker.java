package com.example.project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ExpenseTracker extends AppCompatActivity {

    Button btnSwitchToA4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        btnSwitchToA4 = findViewById(R.id.btnSwitchToA4);

        btnSwitchToA4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExpenseTracker.this, MainActivity4.class);
                startActivity(intent);
            }
        });
    }
}