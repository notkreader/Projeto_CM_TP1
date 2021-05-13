package com.example.projeto_cm;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projeto_cm.ui.AccountSettings.AccountSettingsFragment;
import com.example.projeto_cm.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;


public class ReturnHomeMessageFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_return_home_message, container, false);

        Button btnYes = view.findViewById(R.id.btn_yes);
        Button btnNo = view.findViewById(R.id.btn_no);

        btnYes.setOnClickListener(v -> {
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment, new HomeFragment());
            ft.commit();
        });

        btnNo.setOnClickListener(v -> {
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment, new AccountSettingsFragment());
            ft.commit();
        });


        return view;
    }
}