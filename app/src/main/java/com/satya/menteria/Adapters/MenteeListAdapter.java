package com.satya.menteria.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.satya.menteria.Activites.ChatActivity;
import com.satya.menteria.Model.User;
import com.satya.menteria.R;
import com.satya.menteria.databinding.ItemMenteeBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class MenteeListAdapter extends RecyclerView.Adapter<MenteeListAdapter.viewHolder>{

    private ArrayList<String> menteeList;
    Context context;

    public MenteeListAdapter(Context context, ArrayList<String> menteeList)
    {
        this.context = context;
        this.menteeList = menteeList;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_mentee,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        String menteeId = menteeList.get(position);
        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(menteeId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        holder.binding.menteeName.setText(user.getUsername());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("uid", menteeId);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        ItemMenteeBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemMenteeBinding.bind(itemView);
        }
    }
}
