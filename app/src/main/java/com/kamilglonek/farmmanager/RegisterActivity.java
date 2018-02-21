package com.kamilglonek.farmmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText etName = (EditText) findViewById(R.id.etName);
        final EditText etUsername = (EditText) findViewById(R.id.etUsername);
        final EditText etEmailAddress = (EditText) findViewById(R.id.etEmailAddress);
        final EditText etPassword = (EditText) findViewById(R.id.etPassword);
        final EditText etConfirmPassword = (EditText) findViewById(R.id.etConfirmPassword);
        final EditText etAnimalType = (EditText) findViewById(R.id.etAnimalType);
        final Button bRegister = (Button) findViewById(R.id.bRegister);

        bRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                    ParseUser newUser = new ParseUser();
                    newUser.logOut();
                    newUser.setUsername(etUsername.getText().toString());
                    newUser.setPassword(etPassword.getText().toString());
                    newUser.setEmail(etEmailAddress.getText().toString());
                    newUser.put("name", etName.getText().toString());
                    newUser.put("animalType", etAnimalType.getText().toString());
                    newUser.signUpInBackground(new SignUpCallback() {
                        @Override
                        public void done(ParseException e) {

                            if (e == null) {
                                Log.i("Sign Up", "Successful");
                                Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                                RegisterActivity.this.startActivity(loginIntent);

                            } else {
                                Log.i("Sign Up", "Failed");
                                AlertDialog signUpFailed= new AlertDialog.Builder(RegisterActivity.this).create();
                                signUpFailed.setTitle("Passwords do not match each other");
                                signUpFailed.setMessage("Correct passwords");
                                signUpFailed.show();
                            }
                        }
                    });
                }
                else {
                    AlertDialog missmatchedPasswordsAlert = new AlertDialog.Builder(RegisterActivity.this).create();
                    missmatchedPasswordsAlert.setTitle("Passwords do not match each other");
                    missmatchedPasswordsAlert.setMessage("Correct passwords");
                    missmatchedPasswordsAlert.show();
                }
            }
        });
    }
}
