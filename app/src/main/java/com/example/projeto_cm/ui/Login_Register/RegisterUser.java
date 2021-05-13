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
import android.widget.TextView;
import android.widget.Toast;

import com.example.projeto_cm.MainActivity;
import com.example.projeto_cm.R;
import com.example.projeto_cm.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends AppCompatActivity implements View.OnClickListener{

    private TextView registerUser, login;
    private EditText editTextEmail, editTextName, editTextPassword, editTextConfirmedPassword;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar acb = getSupportActionBar();
        acb.hide();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();

        registerUser = (Button) findViewById(R.id.buttonRegister);
        registerUser.setOnClickListener(this);

        login = (Button) findViewById(R.id.registerLogin);
        login.setOnClickListener(this);

        editTextEmail = (EditText) findViewById(R.id.registerEmail);
        editTextName = (EditText) findViewById(R.id.registerName);
        editTextPassword = (EditText) findViewById(R.id.registerPass);
        editTextConfirmedPassword = (EditText) findViewById(R.id.registerConfirmedPass);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerLogin:
                startActivity(new Intent(RegisterUser.this, LoginUser.class));
                finish();
                break;
            case R.id.buttonRegister:
                registerUser();
                break;
        }
    }

    private void registerUser(){

        String email = editTextEmail.getText().toString().trim();
        String name =  editTextName.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmedPassword = editTextConfirmedPassword.getText().toString().trim();

        if(email.isEmpty()){
            editTextEmail.setError("Email required!");
            editTextEmail.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please provide valid email!");
            editTextEmail.requestFocus();
            return;
        }
        if(name.isEmpty()){
            editTextName.setError("Name required!");
            editTextName.requestFocus();
            return;
        }
        if(!password.equals(confirmedPassword)){
            editTextConfirmedPassword.setError("Passwords don't match!");
            editTextConfirmedPassword.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editTextPassword.setError("Password required!");
            editTextPassword.requestFocus();
            return;
        }
        if(confirmedPassword.isEmpty()){
            editTextConfirmedPassword.setError("Password required!");
            editTextConfirmedPassword.requestFocus();
            return;
        }

        if(password.length() < 8){
            editTextConfirmedPassword.setError("Password to short min. 8 characters");
            editTextConfirmedPassword.requestFocus();
            return;
        }

       mAuth.createUserWithEmailAndPassword(email,password)
               .addOnCompleteListener(task -> {
                   if(task.isSuccessful()){
                       User user= new User(name, email,false);
                       UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                               .setDisplayName(name).build();
                       FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates);
                       MainActivity.mDataBase.child("Users")
                               .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                               .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(RegisterUser.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(RegisterUser.this, LoginUser.class));
                                    finish();
                                }else{
                                    Toast.makeText(RegisterUser.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                }
                           }
                       });
                   }else{
                       Toast.makeText(RegisterUser.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();

                   }
               });
    }
}