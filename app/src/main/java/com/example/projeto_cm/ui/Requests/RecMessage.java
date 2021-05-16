package com.example.projeto_cm.ui.Requests;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projeto_cm.MainActivity;
import com.example.projeto_cm.R;
import com.example.projeto_cm.Requests;
import com.firebase.ui.database.FirebaseRecyclerOptions;


public class RecMessage extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    RecyclerView recview;
    RequestMsgAdapter adapter;

    public RecMessage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment recMessage.
     */
    // TODO: Rename and change types and number of parameters
    public static RecMessage newInstance(String param1, String param2) {
        RecMessage fragment = new RecMessage();
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
        View view= inflater.inflate(R.layout.fragment_rec_message, container, false);


        recview = (RecyclerView) view.findViewById(R.id.res_message);
        recview.setLayoutManager(new LinearLayoutManager(getContext()));


        if(MainActivity.isGuide) {
            FirebaseRecyclerOptions<Requests> options = new FirebaseRecyclerOptions.Builder<Requests>()
                    .setQuery(MainActivity.mDataBase.child("Requests"), Requests.class)
                    .build();
            adapter= new RequestMsgAdapter(options);
            recview.setAdapter(adapter);
        }
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop(){
        super.onStop();
        adapter.startListening();

    }
}