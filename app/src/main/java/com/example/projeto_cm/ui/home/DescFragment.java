package com.example.projeto_cm.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.projeto_cm.R;

import java.util.ArrayList;


public class DescFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    String titulo,descricao;
    ArrayList<String> imgs;
    public DescFragment() {

    }

    public DescFragment(String titulo, String descricao, ArrayList<String> imgs) {
        this.titulo=titulo;
        this.descricao=descricao;
        this.imgs=imgs;
        System.out.println("AAAAAAAAAAAAAA" + imgs );
    }

    public static DescFragment newInstance(String param1, String param2) {
        DescFragment fragment = new DescFragment();
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
        View view = inflater.inflate(R.layout.fragment_descfragment, container, false);

        ImageView imageholder= view.findViewById(R.id.visit_imgs);
        TextView titleholder = view.findViewById(R.id.visit_title);
        TextView descriptionholder = view.findViewById(R.id.visit_description);

        titleholder.setText(titulo);
        descriptionholder.setText(descricao);
        if(imgs.get(0)!="noImage") {
            Glide.with(getContext()).load(imgs.get(0)).into(imageholder);
        }

        Button btn1 = (Button) view.findViewById(R.id.back_button_visit);
        btn1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FragmentTransaction ft = getParentFragmentManager().beginTransaction();
                ft.replace(R.id.nav_host_fragment, new HomeFragment()).addToBackStack(null).commit();
            }
        });

        return view;
    }


}