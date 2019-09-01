package com.alphakiwi.projet_7;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alphakiwi.projet_7.model.Restaurant;
import com.alphakiwi.projet_7.model.User;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<User> profiles ;
    User currentUser ;
    boolean showResto;

    public MyAdapter(Context c, ArrayList<User> p, User u , boolean s){
        context = c;
        profiles = p;
        currentUser = u;
        showResto = s;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (showResto == true) {
            holder.name.setText(profiles.get(position).getUsername());
            holder.descript.setText(profiles.get(position).getResto().getName());

            int comparison =  profiles.get(position).getResto().getName().compareTo("Pas encore choisit");

            if (comparison != 0 ) {


                holder.descript.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        Intent i = new Intent(context, PresentationActivity.class);
                        i.putExtra("resto", profiles.get(position).getResto());

                        context.startActivity(i);


                    }
                });

            }else{
                holder.name.setTextColor(Color.rgb(160,160,160));
                holder.descript.setTextColor(Color.rgb(200,200,200));
            }

        }else{

            if (profiles.get(position) != currentUser) {
                holder.name.setText(profiles.get(position).getUsername() + " mange ici !");
            }else{
                holder.name.setText("Je mange ici !");
            }
        }




        Glide.with(holder.avatar.getContext())
                .load(profiles.get(position).getUrlPicture())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.avatar);


    }


    @Override
    public int getItemCount() {
        return profiles.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, descript ;
        ImageView avatar;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.list_title);
            descript =  itemView.findViewById(R.id.list_desc);
            avatar = itemView.findViewById(R.id.list_image);
        }
    }
}
