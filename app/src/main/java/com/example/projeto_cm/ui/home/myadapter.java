package com.example.projeto_cm.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projeto_cm.R;
import com.example.projeto_cm.Visits;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import org.jetbrains.annotations.NotNull;

public class myadapter extends FirebaseRecyclerAdapter<Visits,myadapter.myviewholder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public myadapter(@NonNull @NotNull FirebaseRecyclerOptions<Visits> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull @NotNull myviewholder holder, int position, @NonNull @NotNull Visits model) {
        holder.title.setText(model.getTitulo());
        Glide.with(holder.img1.getContext()).load(model.getImages().get(0)).into(holder.img1);

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
        TextView title;


        public myviewholder(@NonNull @NotNull View itemView) {
            super(itemView);

            img1 =itemView.findViewById(R.id.visitImg);
            title= itemView.findViewById(R.id.visitTitle);
        }
    }

}
