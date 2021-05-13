package com.example.projeto_cm.ui.Requests;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.projeto_cm.R;
import com.example.projeto_cm.ui.Map.MapFragment;
import com.example.projeto_cm.ui.home.HomeFragment;

public class RequestsFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_requests, container, false);

        Button backBtn = (Button) view.findViewById(R.id.back_button);
        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment, new HomeFragment());
                ft.commit();
            }
        });

        Button mapsBtn = (Button) view.findViewById(R.id.button_maps);
        mapsBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment, new MapFragment());
                ft.commit();
            }
        });

        return view;
    }

}
