package com.lobxy.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "Reg";
    private EditText edit_password;
    private EditText edit_email;

    private FirebaseAuth mAuth;

    private ProgressBar progressBar;

    private String password, email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edit_email = findViewById(R.id.reg_email);
        edit_password = findViewById(R.id.reg_password);
        progressBar = findViewById(R.id.reg_progressBar);

        mAuth = FirebaseAuth.getInstance();

        Button submit = findViewById(R.id.reg_submit);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });
    }

    private void validate() {
        email = edit_email.getText().toString().trim();
        password = edit_password.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Field Empty", Toast.LENGTH_SHORT).show();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 6) {
            Toast.makeText(this, "Password is weak", Toast.LENGTH_SHORT).show();
        } else {
            registerUser();
        }
    }

    private void registerUser() {
        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.INVISIBLE);

                if (task.isSuccessful()) {
                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Error Occured", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "onComplete: reg User : " + task.getException().getMessage());
                }
            }
        });
    }
}
