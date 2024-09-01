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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Signup extends AppCompatActivity {
    protected EditText signupName, signupEmail, signupUser, signupPass, signupCPass;
    protected Button signupButton;
    protected TextView toLogin;

    protected FirebaseAuth sAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        signupName = findViewById(R.id.signupName);
        signupEmail = findViewById(R.id.signupEmail);
        signupUser = findViewById(R.id.signupUser);
        signupPass = findViewById(R.id.signupPass);
        signupCPass = findViewById(R.id.signupCPass);
        signupButton = findViewById(R.id.signupButton);
        toLogin = findViewById(R.id.toLogin);
        sAuth = FirebaseAuth.getInstance();

        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signup.this,LogIn.class));
            }
        });

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            createUser();
            }
        });
    }

    private void createUser(){
        String email=signupEmail.getText().toString();
        String pass=signupPass.getText().toString();
        String cpass=signupCPass.getText().toString();
        if(TextUtils.isEmpty(email)){
            signupEmail.setError("Email cannot be empty");
            signupEmail.requestFocus();
        }
        else if(TextUtils.isEmpty(pass)){
            signupPass.setError("Password cannot be empty");
            signupPass.requestFocus();
        }
        else if(TextUtils.isEmpty(cpass)) {
            signupCPass.setError("Password cannot be empty");
            signupCPass.requestFocus();
        }
        else if(!pass.equals(cpass)){
            signupCPass.setError("Passwords do not match");
            signupCPass.requestFocus();
        }
        else{
            sAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(Signup.this,"User registered successfully",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Signup.this,LogIn.class));
                    }
                    else{
                        Toast.makeText(Signup.this,"Registration Error"+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}