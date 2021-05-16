package com.example.projeto_cm.ui.Messages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_cm.MainActivity;
import com.example.projeto_cm.R;
import com.example.projeto_cm.Requests;
import com.example.projeto_cm.ui.Requests.recMessage;
import com.example.projeto_cm.ui.Requests.recMessageTourist;
import com.example.projeto_cm.ui.Requests.reqTouristAdapter;
import com.example.projeto_cm.ui.home.HomeFragment;
import com.example.projeto_cm.ui.home.RecFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Objects;

public class MessagesFragment extends Fragment {




    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_messages, container, false);

        Button backBtn = (Button) view.findViewById(R.id.back_button);
        backBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment, new HomeFragment());
                ft.commit();
            }
        });

        TextView text= view.findViewById(R.id.text_Empty);
        text.setText("");

        if(MainActivity.isGuide) {
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mid_frag, new recMessage());
            ft.commit();
        }else{
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.mid_frag, new recMessageTourist());
            ft.commit();
        }

        return view;
    }

}