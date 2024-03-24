package com.example.project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginController extends AppCompatActivity {

    Button btnLogin, btnRegister;
    EditText txtEmail, txtPassword;
    FirebaseAuth mAuth;
    ProgressBar progressBar;

    //if user is already logged in redirect to dashboard activity
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and Update UI accordingly
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(LoginController.this, DashboardController.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);

        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressBar);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser(txtEmail.getText().toString(), txtPassword.getText().toString());
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser(txtEmail.getText().toString(), txtPassword.getText().toString());
            }
        });
    }

    public void loginUser(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        // Empty Field if statements
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(LoginController.this, "Enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(LoginController.this, "Enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    // If sign in is success, then load dashboard activity with user's information
                    Toast.makeText(LoginController.this, "Login Successful.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginController.this, DashboardController.class);
                    startActivity(intent);
                }
                else {
                    // If sign in fails, display a message.
                    Toast.makeText(LoginController.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void registerUser(String email, String password){
        progressBar.setVisibility(View.VISIBLE);
        // Empty Field if statements
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(LoginController.this, "Enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(LoginController.this, "Enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    // If sign in is success, then load dashboard activity with user's information
                        Toast.makeText(LoginController.this, "Account Created", Toast.LENGTH_SHORT).show();
                    } else {
                        // If sign in fails, display a message.
                        Toast.makeText(LoginController.this, "Registration failed.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }
}