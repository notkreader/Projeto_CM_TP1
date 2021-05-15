package com.example.projeto_cm.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.projeto_cm.MainActivity;
import com.example.projeto_cm.R;
import com.example.projeto_cm.ui.Map.MapFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class HomeFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        TextView text= view.findViewById(R.id.text_Empty);

        MainActivity.mDataBase.child("Visits").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (getActivity() == null) {
                    return;
                }
                if(snapshot.exists()){
                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    text.setText("");
                    ft.replace(R.id.mid_frag ,new recfragment());
                    ft.commit();
                }else{

                }
            }
            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        return view;
    }
}