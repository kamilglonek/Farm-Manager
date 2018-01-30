package com.kamilglonek.farmmanager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.parse.ParseAnalytics;

public class WelcomeScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                // login normally
                Intent loginIntent = new Intent(WelcomeScreen.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}

