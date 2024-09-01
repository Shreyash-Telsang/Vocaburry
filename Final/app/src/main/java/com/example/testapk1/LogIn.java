package com.example.testapk1;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

//Log in page

public class LogIn extends AppCompatActivity {
    protected EditText loginEmail, loginPass;
    protected TextView signup;
    protected Button loginButton;
    protected FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FirebaseApp.initializeApp(this);
        loginEmail = findViewById(R.id.loginEmail);
        loginPass = findViewById(R.id.loginPass);
        signup = findViewById(R.id.toSignup);
        loginButton = findViewById(R.id.loginButton);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogIn.this, Signup.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = loginEmail.getText().toString();
                String pass = loginPass.getText().toString();
                loginUser(email, pass);
            }
        });


    }

    private void loginUser(String email, String pass) {
        if (TextUtils.isEmpty(email)) {
            loginEmail.setError("Email cannot be empty");
            loginEmail.requestFocus();
        } else if (TextUtils.isEmpty(pass)) {
            loginPass.setError("Password cannot be empty");
            loginPass.requestFocus();
        } else {
            auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            Toast.makeText(LogIn.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LogIn.this,activity_home_page.class));
                        } else if (user == null) {
                            Toast.makeText(LogIn.this, "Error", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(LogIn.this, "Login failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });
        }
    }
}
