package com.example.projeto_cm.ui.Login_Register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projeto_cm.MainActivity;
import com.example.projeto_cm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPassword extends AppCompatActivity {

    private EditText recoveryEmail;
    private Button recoveryButton;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar acb = getSupportActionBar();
        acb.hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        recoveryEmail = (EditText) findViewById(R.id.recoveryEmail);
        recoveryButton = (Button) findViewById(R.id.resetPassButton);

        mAuth= FirebaseAuth.getInstance();

        recoveryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });


    }

    private void resetPassword(){
        String email = recoveryEmail.getText().toString().trim();
        System.out.println(email);
        if(email.isEmpty()){
            recoveryEmail.setError("Email required!");
            recoveryEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            recoveryEmail.setError("Please enter a valid email!");
            recoveryEmail.requestFocus();
            return;
        }

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful()){
                    Toast.makeText(ResetPassword.this, "Check your email!", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(ResetPassword.this, "Try again! Something wrong happened!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(this, LoginUser.class);
        startActivity(startMain);
        finish();
    }
}