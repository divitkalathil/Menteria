package com.satya.menteria.Activites;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.satya.menteria.Adapters.MenteeListAdapter;
import com.satya.menteria.Model.User;
import com.satya.menteria.R;
import com.satya.menteria.databinding.ActivityMainBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseDatabase database;
    FirebaseAuth auth;
    FirebaseStorage storage;

    String currentUid;
    String mentorLevelPool;
    String mentorId;
    ArrayList<String> menteeList;
    MenteeListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        menteeList = new ArrayList<>();
        mAdapter = new MenteeListAdapter(this, menteeList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        binding.recyclerView.setAdapter(mAdapter);

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();

        currentUid = auth.getUid();

        database.getReference().child("users").child(currentUid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        User user = snapshot.getValue(User.class);
                        binding.usernameBox.setText(user.getUsername());
                        binding.codeforcesHandleBox.setText(user.getCodeforcesHandle());
                        if(user.getMentor().equals("NO_MENTOR_ASSIGNED")){
                            DecideMentorLevelPool(user.getLevelPool());
                        }
                        mentorId = user.getMentor();
                        Glide.with(MainActivity.this)
                                .load(user.getImageUrl())
                                .placeholder(R.drawable.ic_baseline_account_circle_100)
                                .into(binding.profileImage);

                        menteeList = user.getMentees();
                        mAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        database.getReference().child("levels")
                .child(mentorLevelPool)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(mentorId.equals("NO_MENTOR_ASSIGNED"))
                        {
                            String id = null;
                            int temp=Integer.MAX_VALUE;
                            for(DataSnapshot snapshot1:snapshot.getChildren())
                            {
                                HashMap<String,Object> count = (HashMap<String, Object>) snapshot1.getValue();

                                if((int) count.get("MenteeCount") < temp)
                                {
                                    temp = (int) count.get("MenteeCount");
                                    id = snapshot1.getKey();
                                }
                            }
                            HashMap<String, Object> mentorInfo = new HashMap<>();
                            mentorInfo.put("mentor", id);
                            mentorId = id;
                            String finalId = id;
                            database.getReference().child("users")
                                    .child(currentUid).updateChildren(mentorInfo)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            database.getReference().child("users").child(finalId)
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            binding.mentorName.setText(snapshot.getValue(User.class).getUsername());
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                        }
                                    });

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



        binding.mentorName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra("uid", mentorId);
                startActivity(intent);
            }
        });
    }

    private void DecideMentorLevelPool(String userLevelPool)
    {
        switch(userLevelPool)
        {
            case "level_5": mentorLevelPool = "level_4"; break;
            case "level_4": mentorLevelPool = "level_3"; break;
            case "level_3": mentorLevelPool = "level_2"; break;
            case "level_2": mentorLevelPool = "level_1"; break;
            default: mentorLevelPool = "mentor_of_mentors"; break;
        }
    }
}