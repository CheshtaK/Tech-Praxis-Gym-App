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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    TextInputLayout tilDisplayName, tilEmail, tilPassword;
    Button btnCreateAccount;
    Toolbar registerToolbar;
    ProgressDialog regProgress;

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        tilDisplayName = findViewById(R.id.tilDisplayName);
        tilEmail = findViewById(R.id.tilEmail);
        tilPassword = findViewById(R.id.tilPassword);
        btnCreateAccount = findViewById(R.id.btnCreateAccount);

        registerToolbar = findViewById(R.id.registerToolbar);
        setSupportActionBar(registerToolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        regProgress = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();

        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String displayName = tilDisplayName.getEditText().getText().toString();
                String email = tilEmail.getEditText().getText().toString();
                String password = tilPassword.getEditText().getText().toString();

                if(!displayName.isEmpty() && !email.isEmpty() && !password.isEmpty()){

                    regProgress.setTitle("Registering User");
                    regProgress.setMessage("Please wait while we create your account!");
                    regProgress.setCanceledOnTouchOutside(false);
                    regProgress.show();

                    RegisterUser(displayName, email, password);
                }
            }
        });
    }

    private void RegisterUser(final String displayName, String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){

                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    String id = currentUser.getUid();

                    mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(id);

                    HashMap<String, String> userMap = new HashMap<>();
                    userMap.put("name", displayName);

                    mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                regProgress.dismiss();
                                Toast.makeText(RegisterActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                else {
                    regProgress.hide();
                    Toast.makeText(RegisterActivity.this, "Cannot create your account. Please check the form and try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
