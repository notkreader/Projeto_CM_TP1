package com.example.projeto_cm;

import android.content.Context;
import android.content.SharedPreferences;
import android.inputmethodservice.Keyboard;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projeto_cm.ui.AccountSettings.AccountSettingsFragment;
import com.example.projeto_cm.ui.Login_Register.RegisterUser;
import com.example.projeto_cm.ui.Messages.MessagesFragment;
import com.example.projeto_cm.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

public class ChangeUsernameFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_username, container, false);

        Button btnCancel = view.findViewById(R.id.btn_cancel);
        Button btnSave = view.findViewById(R.id.save_new_username);
        EditText newUsername = view.findViewById(R.id.userName);

        btnCancel.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment, new AccountSettingsFragment());
            ft.commit();
        });

        btnSave.setOnClickListener(v -> {
            if(!newUsername.getText().toString().equals("")){
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(newUsername.getText().toString()).build();
                FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates);

                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment, new ReturnHomeMessageFragment());
                ft.commit();
                Toast.makeText(getActivity(), "Username Updated Successfully!", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(getActivity(), "Username can't be empty! Try again!", Toast.LENGTH_LONG).show();
            }

        });


        return view;
    }
}