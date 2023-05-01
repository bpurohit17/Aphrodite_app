package com.example.aphrodite.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.aphrodite.R;
import com.example.aphrodite.databinding.ActivityLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    // if code send failed, will used to resend code OTP
    private PhoneAuthProvider.ForceResendingToken forceResendingToken;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;

    private String mverificationId;  // will hold OTP

    private static final String TAG = "MAIN_TAG";


    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance(); // initialize mAuth


        binding.phoneL1.setVisibility(View.VISIBLE);
        binding.codeL1.setVisibility(View.GONE);

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken token) {
                super.onCodeSent(verificationId, forceResendingToken);

                Log.d(TAG, "onCodeSent: "+ verificationId);

                mverificationId = verificationId;
                forceResendingToken = token;

                binding.phoneL1.setVisibility(View.GONE);
                binding.codeL1.setVisibility(View.VISIBLE);

                Toast.makeText(LoginActivity.this, "Verification code sent", Toast.LENGTH_SHORT).show();

                binding.txtPhoneNumber.setText(""+binding.edtPhoneNumber.getText().toString().trim());


            }
        };


        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get the value which input by user in EditText and convert it to string
                String phone = binding.edtPhoneNumber.getText().toString().trim();

//                // Create the Intent object of this class Context() to Second_activity class
//                Intent intent = new Intent(getApplicationContext(), VerifyotpLoginActivity.class);
//
//                // now by putExtra method put the value in key, value pair key is
//                // message_key by this key we will receive the value, and put the string
//                intent.putExtra("phoneNumber", phone_number);
//
//                // start the Intent
//                startActivity(intent);

                if (TextUtils.isEmpty(phone)) {
                    // when mobile number text field is empty
                    binding.edtPhoneNumber.setError(getString(R.string.phone_error));
                    binding.edtPhoneNumber.requestFocus();
                } else {
                    // if the text field is not empty we are calling our
                    // send OTP method for getting OTP from Firebase.
//                    sendVerificationCode(phone);
                    startPhoneNumberVerification(phone);

                }
            }
        });

        binding.txtResendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get the value which input by user in EditText and convert it to string
                String phone = binding.edtPhoneNumber.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    // when mobile number text field is empty
                    binding.edtPhoneNumber.setError(getString(R.string.phone_error));
                    binding.edtPhoneNumber.requestFocus();
                } else {
                    // if the text field is not empty we are calling our
                    // send OTP method for getting OTP from Firebase.
                    resendVerificationCode(phone, forceResendingToken);

                }
            }
        });

        binding.btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String code = binding.pinView.getText().toString().trim();

                if (code.isEmpty()) {
                    binding.edtPhoneNumber.setError(getString(R.string.otpError));
                    binding.edtPhoneNumber.requestFocus();
                } else {
                    verifyPhoneNumberWithCode(mverificationId, code);
                }


            }
        });
    }


    private void startPhoneNumberVerification(String phone) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(callbacks)
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void resendVerificationCode(String phone, PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phone)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(callbacks)
                .setForceResendingToken(token)
                .build();

        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        String phone = mAuth.getCurrentUser().getPhoneNumber();
                        Toast.makeText(LoginActivity.this, "Logged In as "+phone, Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, ProfileActivity.class));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

}