package com.example.projeto_cm.ui.AccountSettings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.projeto_cm.ChangePasswordFragment;
import com.example.projeto_cm.ChangeUsernameFragment;
import com.example.projeto_cm.DeleteAccountFragment;
import com.example.projeto_cm.MainActivity;
import com.example.projeto_cm.R;
import com.example.projeto_cm.User;
import com.example.projeto_cm.ui.home.HomeFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class AccountSettingsFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_accountsettings, container, false);

        Button btn1 = (Button) view.findViewById(R.id.back_button);

        btn1.setOnClickListener(v -> {
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment, new HomeFragment());
            ft.commit();
        });

        TextView username = view.findViewById(R.id.username);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        username.setText(mAuth.getCurrentUser().getDisplayName());

        Button editUsername = view.findViewById(R.id.editUsername);

        editUsername.setOnClickListener(v -> {
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment, new ChangeUsernameFragment());
            ft.commit();
        });

        MainActivity.mDataBase.child("Users").child(MainActivity.mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user.getIsGuide()) {
                    TextView request = (TextView) view.findViewById(R.id.text_aboutUs);
                    request.setText("Guide Account");
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });

        Button editPassword = view.findViewById(R.id.btn_changePassword);

        editPassword.setOnClickListener(v -> {
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment, new ChangePasswordFragment());
            ft.commit();
        });

        Button deleteAccount = view.findViewById(R.id.btn_deleteAccount);
        deleteAccount.setOnClickListener(v -> {
            FragmentTransaction ft = getParentFragmentManager().beginTransaction();
            ft.replace(R.id.nav_host_fragment, new DeleteAccountFragment());
            ft.commit();
        });

        Switch s = view.findViewById(R.id.switch_darkMode);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        final boolean isDarkModeOn = sharedPreferences.getBoolean("isDarkModeOn", false);

        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            s.setChecked(true);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            s.setChecked(false);
        }

        s.setOnClickListener(v -> {
            if (isDarkModeOn) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                editor.putBoolean("isDarkModeOn", false);
            }else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                editor.putBoolean("isDarkModeOn", true);
            }
            editor.apply();
        });

        return view;
    }


}