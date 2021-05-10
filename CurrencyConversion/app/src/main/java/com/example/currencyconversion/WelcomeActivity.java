package com.example.currencyconversion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class WelcomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    public void passaTela(View view) {

        Intent i1 = new Intent(WelcomeActivity.this, MainActivity.class);
        startActivity(i1);
        finish();
    }
}