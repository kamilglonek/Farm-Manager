package com.kamilglonek.farmmanager.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamilglonek.farmmanager.R;
import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class WelcomeScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);
        ParseAnalytics.trackAppOpenedInBackground(getIntent());
        ArrayList<String> loginData = loadData();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {



                if(!loginData.isEmpty()) {
                    ParseUser.logInInBackground(loginData.get(loginData.size() - 2), loginData.get(loginData.size() - 1), new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {

                            if (e == null) {
                                Log.i("Signup", "Login successful");
                                Intent userIntent = new Intent(WelcomeScreen.this, UserActivity.class);
                                WelcomeScreen.this.startActivity(userIntent);

                            } else {
                                Toast.makeText(WelcomeScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

                // login normally
                Intent loginIntent = new Intent(WelcomeScreen.this, LoginActivity.class);
                startActivity(loginIntent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    public ArrayList<String> loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("passes", null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        ArrayList<String> loginData = gson.fromJson(json, type);
        return loginData;
    }
}

