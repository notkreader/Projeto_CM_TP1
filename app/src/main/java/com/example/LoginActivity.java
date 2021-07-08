package com.example.clickandvisit;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private Button registerButton;
    private TextView emailAddress;
    private TextView password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailAddress = (TextView) findViewById(R.id.editTextEmailAddress);
        password = (TextView) findViewById(R.id.editTextPassword);

        registerButton = (Button) findViewById(R.id.button1Register);



    }

    public void goToRegister(View v){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}
