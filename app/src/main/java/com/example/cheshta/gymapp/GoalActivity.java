package com.example.cheshta.gymapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class GoalActivity extends AppCompatActivity {

    Toolbar goalAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal);

        goalAppBar = findViewById(R.id.goalAppBar);
        setSupportActionBar(goalAppBar);
        getSupportActionBar().setTitle("What's your goal");
    }
}
