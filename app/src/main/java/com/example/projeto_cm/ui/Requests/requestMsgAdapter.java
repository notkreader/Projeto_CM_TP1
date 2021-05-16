package com.example.projeto_cm.ui.Requests;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projeto_cm.R;
import com.example.projeto_cm.Requests;
import com.example.projeto_cm.Visits;
import com.example.projeto_cm.ui.home.DescFragment;
import com.example.projeto_cm.ui.home.MyAdapter;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import org.jetbrains.annotations.NotNull;

public class requestMsgAdapter extends FirebaseRecyclerAdapter<Requests,requestMsgAdapter.myviewholder > {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public requestMsgAdapter(@NonNull @NotNull FirebaseRecyclerOptions<Requests> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull myviewholder holder, int position, @NonNull @NotNull Requests model) {
        holder.title.setText(model.getTitulo());
        holder.email.setText(model.getUserEmail());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity= (AppCompatActivity) v.getContext();
                FragmentTransaction fm = activity.getSupportFragmentManager().beginTransaction();

                fm.replace(R.id.nav_host_fragment, new DescFragment(model.getTitulo(),model.getDescricao(), model.getImages())).addToBackStack(null);

                Fragment frag = activity.getSupportFragmentManager().findFragmentById(R.id.mid_frag);
                if(frag!=null) {
                    fm.hide(frag);
                }
                fm.commit();
            }
        });
    }

    @NonNull
    @NotNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.requestmsgdesign, parent, false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder{
        TextView title, email;


        public myviewholder(@NonNull @NotNull View itemView) {
            super(itemView);

           email =itemView.findViewById(R.id.email_msg_req);
            title= itemView.findViewById(R.id.request_msg_title);
        }
    }
}
