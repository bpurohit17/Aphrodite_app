package com.example.aphrodite.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.aphrodite.R;
import com.example.aphrodite.databinding.ActivityProfileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private ActivityProfileBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityProfileBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        checkUserStatus();

        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                checkUserStatus();
            }
        });


    }

    private void checkUserStatus() {
        // get current user

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        if (firebaseUser != null) {
            // user logged in


            String phoneNumber = firebaseUser.getPhoneNumber();

        }
        else {
            // user is not logged  in
            finish();
        }
    }
}