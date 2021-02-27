package com.satya.menteria.Activites;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.satya.menteria.Model.User;
import com.satya.menteria.R;
import com.satya.menteria.databinding.ActivitySetUpProfileBinding;

public class SetUpProfileActivity extends AppCompatActivity {

    FirebaseDatabase database;
    FirebaseStorage storage;
    FirebaseAuth auth;
    private static final int IMAGE_SELECTOR_CODE = 233;
    ActivitySetUpProfileBinding binding;
    Uri selectedImageUri=null;
    String username;
    String codeforcesHandle;
    String codeforcesRating;
    String levelPool = "level_5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetUpProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("images/*");
                startActivityForResult(intent, IMAGE_SELECTOR_CODE);;
            }
        });

        binding.setUpProfilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ValidateFeilds()) return;

                InsertUserDataIntoFirebase();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == IMAGE_SELECTOR_CODE)
        {
            if(data!=null)
            {
                if(data.getData()!=null)
                {
                    selectedImageUri = data.getData();
                    binding.profileImage.setImageURI(selectedImageUri);
                }
            }
        }
    }

    private boolean ValidateFeilds() {

        String error = "This field can not be left blank";
        username = username.trim();
        codeforcesHandle = codeforcesHandle.trim();
        codeforcesRating = codeforcesRating.trim();

//        if(!DecideUserLevelPool())
//        {
//            binding.codeforcesHandleBox.setError("Invalid Rating");
//            return false;
//        }
//        if(username.isEmpty())
//        {
//            binding.usernameBox.setError(error);
//        }
//        if(codeforcesHandle.isEmpty())
//        {
//            binding.codeforcesHandleBox.setError(error);
//            return false;
//        }
//        if(codeforcesRating.isEmpty() || !DecideUserLevelPool())
//        {
//            binding.codeforcesRatingBox.setError(error);
//            return false;
//        }
        return true;
    }


    private void InsertUserDataIntoFirebase(){

        database.getReference().child(levelPool)
                .push().setValue(auth.getUid())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });


        if(selectedImageUri!=null)
        {
            StorageReference reference = storage.getReference().child("Profiles").child(auth.getUid());
            reference.putFile(selectedImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if(task.isSuccessful())
                    {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                User user = new User(username, codeforcesHandle, codeforcesRating, levelPool, uri.toString());
                                database.getReference()
                                        .child("users")
                                        .push()
                                        .setValue(user)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                HeadToDashboard();
                                            }
                                        });
                            }
                        });
                    }
                }
            });
        }

        else{
            User user = new User(username, codeforcesHandle, codeforcesRating, levelPool, "No Image");
            database.getReference()
                    .child("users")
                    .push()
                    .setValue(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            HeadToDashboard();
                        }
                    });
        }
    }

    private boolean DecideUserLevelPool() {
        int rating = Integer.parseInt(codeforcesRating);
        if(rating<0) return false;
        rating/=100;
        switch(rating)
        {
            case 40: return false;
            case 30: levelPool = "level_1";
                return true;
            case 20: levelPool = "level_2";
                return true;
            case 15: levelPool = "level_3";
                return true;
            case 10: levelPool = "level_4";
                return true;
            default: levelPool = "level_5";
                return true;
        }
    }

    private void HeadToDashboard()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}