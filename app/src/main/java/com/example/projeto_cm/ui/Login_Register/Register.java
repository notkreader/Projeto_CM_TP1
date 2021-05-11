package com.example.projeto_cm.ui.Login_Register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projeto_cm.MainActivity;
import com.example.projeto_cm.R;
import com.example.projeto_cm.User;
import com.example.projeto_cm.ui.home.HomeFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Register#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Register extends Fragment implements View.OnClickListener{

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView registerUser, login;
    private EditText editTextEmail, editTextName, editTextPassword, editTextConfirmedPassword;
    private FirebaseAuth mAuth;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Register() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Register.
     */
    // TODO: Rename and change types and number of parameters
    public static Register newInstance(String param1, String param2) {
        Register fragment = new Register();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);


        mAuth = FirebaseAuth.getInstance();

        registerUser = (Button) view.findViewById (R.id.buttonRegister);
        registerUser.setOnClickListener(this);

        login = (Button) view.findViewById(R.id.registerLogin);
        login.setOnClickListener(this);

        editTextEmail = (EditText) view.findViewById(R.id.registerEmail);
        editTextName = (EditText) view.findViewById(R.id.registerName);
        editTextPassword = (EditText) view.findViewById(R.id.registerPass);
        editTextConfirmedPassword = (EditText) view.findViewById(R.id.registerConfirmedPass);

        return view;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerLogin:
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                ft.replace(this.getId(), new HomeFragment());
                ft.commit();
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
            editTextEmail.setError("Please provide valid error!");
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
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user= new User(name, email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        //Toast.makeText(RegisterUser.this, "User has been registered successfully!", Toast.LENGTH_LONG).show();
                                    }else{
                                        //Toast.makeText(RegisterUser.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }else{
                            //Toast.makeText(RegisterUser.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
