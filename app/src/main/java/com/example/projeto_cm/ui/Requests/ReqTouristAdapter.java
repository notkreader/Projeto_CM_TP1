package com.example.projeto_cm.ui.Requests;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_cm.R;
import com.example.projeto_cm.Requests;
import com.example.projeto_cm.ui.home.DescFragment;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ReqTouristAdapter extends RecyclerView.Adapter<ReqTouristAdapter.MyViewHolder> {

    ArrayList<Requests> mList;
    Context context;

    public ReqTouristAdapter(Context context, ArrayList<Requests> list  ){
        this.mList=list;
        this.context=context;

    }

    @NonNull
    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate( R.layout.requestmsgdesign, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull MyViewHolder holder, int position) {
        Requests req = mList.get(position);
        holder.title.setText(req.getTitulo());
        holder.email.setText(req.getUserEmail());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity= (AppCompatActivity) v.getContext();
                FragmentTransaction fm = activity.getSupportFragmentManager().beginTransaction();

                fm.replace(R.id.nav_host_fragment, new DescFragment(req.getTitulo(),req.getDescricao(),req.getImages())).addToBackStack(null);

                Fragment frag = activity.getSupportFragmentManager().findFragmentById(R.id.mid_frag);
                if(frag!=null) {
                    fm.hide(frag);
                }
                fm.commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        TextView title, email;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.request_msg_title);
            email = itemView.findViewById(R.id.email_msg_req);

        }
    }
}
