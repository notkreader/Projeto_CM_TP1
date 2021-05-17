package com.example.projeto_cm.ui.home;

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

import com.bumptech.glide.Glide;
import com.example.projeto_cm.R;
import com.example.projeto_cm.Visits;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import org.jetbrains.annotations.NotNull;

public class MyAdapter extends FirebaseRecyclerAdapter<Visits, MyAdapter.myviewholder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public MyAdapter(@NonNull @NotNull FirebaseRecyclerOptions<Visits> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull myviewholder holder, int position, @NonNull @NotNull Visits model) {
        holder.title.setText(model.getTitulo());
        holder.location.setText(model.getLocation());
        Glide.with(holder.img1.getContext()).load(model.getImages().get(0)).into(holder.img1);

        holder.img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatActivity activity= (AppCompatActivity) v.getContext();

                FragmentTransaction fm = activity.getSupportFragmentManager().beginTransaction();
                fm.replace(R.id.nav_host_fragment, new DescFragment(model.getTitulo(), model.getLocation(), model.getDescricao(), model.getImages())).addToBackStack(null);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerowdesign, parent, false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder{
        ImageView img1;
        TextView title, location;


        public myviewholder(@NonNull @NotNull View itemView) {
            super(itemView);

            img1 = itemView.findViewById(R.id.visitImg);
            title = itemView.findViewById(R.id.visitTitle);
            location = itemView.findViewById(R.id.visitLocal);

        }
    }

}
