package com.example.cheshta.gymapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout tilYourEmail, tilLoginPassword;
    Toolbar loginToolbar;
    Button btnLogin;
    ProgressDialog loginProgress;

    FirebaseAuth mAuth;
    DatabaseReference mUserDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        tilYourEmail = findViewById(R.id.tilYourEmail);
        tilLoginPassword = findViewById(R.id.tilLoginPassword);
        btnLogin = findViewById(R.id.btnLogin);

        loginToolbar = findViewById(R.id.loginToolbar);
        setSupportActionBar(loginToolbar);
        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loginProgress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = tilYourEmail.getEditText().getText().toString();
                String password = tilLoginPassword.getEditText().getText().toString();

                if(!email.isEmpty() && !password.isEmpty()){

                    loginProgress.setTitle("Logging In");
                    loginProgress.setMessage("Please wait while we check your credentials.");
                    loginProgress.setCanceledOnTouchOutside(false);
                    loginProgress.show();
                    LoginUser(email, password);
                }
            }
        });
    }

    private void LoginUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){
                    loginProgress.dismiss();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            }
        });
    }
}
