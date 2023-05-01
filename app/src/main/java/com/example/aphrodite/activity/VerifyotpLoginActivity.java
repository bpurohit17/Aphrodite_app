package com.example.aphrodite.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.example.aphrodite.R;
import com.example.aphrodite.databinding.ActivityLoginBinding;
import com.example.aphrodite.databinding.ActivityVerifyotpLoginBinding;
import com.example.aphrodite.helper.FirebaseAuthHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyotpLoginActivity extends AppCompatActivity {

//    private FirebaseAuthHelper firebaseAuthHelper;

    private FirebaseAuth mAuth;

    private ActivityVerifyotpLoginBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityVerifyotpLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

//        // display the string into textView
//        binding.textPhoneNumber.setText(phone_number);


        // create the get Intent object and receive the value by getStringExtra() method and key must be same which is send by first activity
        String phoneNumber = getIntent().getStringExtra("phoneNumber");



        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = binding.pinView.getText().toString();

                if (code.isEmpty()) {
                    Toast.makeText(VerifyotpLoginActivity.this, getString(R.string.otpError), Toast.LENGTH_SHORT).show();
                } else {
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(phoneNumber, code);
                    signInWithPhoneAuthCredential(credential);
                }

//                // validating if the OTP text field is empty or not.
//                if (TextUtils.isEmpty(binding.pinView.getText().toString())) {
//                    // if the OTP text field is empty display
//                    // a message to user to enter OTP
//                    binding.pinView.setError(getString(R.string.otpError));
//                    binding.pinView.requestFocus();
//
//                } else {
//                    // if OTP field is not empty calling
//                    // method to verify the OTP.
//                    verifyCode(edtOTP.getText().toString());
//                }

            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        // inside this method we are checking if
        // the code entered is correct or not.
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // if the code is correct and the task is successful
                            // we are sending our user to new activity.
                            Intent i = new Intent(VerifyotpLoginActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        } else {
                            // if the code is not correct then we are
                            // displaying an error message to the user.
                            Toast.makeText(VerifyotpLoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
