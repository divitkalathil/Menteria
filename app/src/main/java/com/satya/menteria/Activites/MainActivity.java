package com.satya.menteria.Activites;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.satya.menteria.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    String codeforcesHandle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        //codeforcesHandle = binding.codeforcesHandleBox.getText().toString();



    }
}