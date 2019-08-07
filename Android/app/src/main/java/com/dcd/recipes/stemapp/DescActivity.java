package com.dcd.recipes.stemapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DescActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_desc);

        Intent descIntent = getIntent();
        getSupportActionBar().setTitle(descIntent.getStringExtra("title"));
        String desc = descIntent.getStringExtra("desc");

        TextView descView = findViewById(R.id.tvDesc);
        descView.setText(desc);
    }
}
