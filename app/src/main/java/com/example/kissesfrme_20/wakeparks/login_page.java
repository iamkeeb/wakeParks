package com.example.kissesfrme_20.wakeparks;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

public class login_page extends AppCompatActivity implements View.OnClickListener {

    private Button buttonSignIn;
    private EditText editEmail;
    private EditText editPassword;
    private Button buttonSignUp;
    private ProgressDialog progress;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        firebaseAuth = FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser() != null) {
            //start user profile
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }

        progress = new ProgressDialog(this);
        buttonSignIn = (Button) findViewById(R.id.button8);
        buttonSignUp = (Button) findViewById(R.id.button9);
        editEmail = (EditText) findViewById(R.id.editText);
        editPassword = (EditText) findViewById(R.id.editText2);

        buttonSignIn.setOnClickListener(this);
        buttonSignUp.setOnClickListener(this);
    }

    private void registerUser() {
        String email =  "";
        email = editEmail.getText().toString().trim();
        String pw = "";
        pw = editPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            //stops function from executing further
            return;
        }
        if(TextUtils.isEmpty(pw)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            //stops function from executing further
            return;
        }

        progress.setMessage("Registering User...");
        progress.show();

        firebaseAuth.createUserWithEmailAndPassword(email, pw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            //user is successfully registerd and logged in
                            //we will start the profile activity here
                            //for now, displays toast
                            //Toast.makeText(login_page.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        } else {
                            Toast.makeText(login_page.this, "Failed to Register", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        progress.dismiss();
    }

    private void userLogin() {
        String email =  "";
        email = editEmail.getText().toString().trim();
        String pw = "";
        pw = editPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            //stops function from executing further
            return;
        }
        if(TextUtils.isEmpty(pw)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            //stops function from executing further
            return;
        }

        progress.setMessage("Logging In...");
        progress.show();

        firebaseAuth.signInWithEmailAndPassword(email, pw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progress.dismiss();
                        if(task.isSuccessful()) {
                            //start profile
                            finish();
                            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if(view == buttonSignIn) {
            //open user view
            userLogin();
        }
        if (view == buttonSignUp) {
            //register new user
            registerUser();
        }
    }
}
