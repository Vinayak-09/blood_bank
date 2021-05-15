package com.vinayak09.bloodbankbyvinayak;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class RegisterIIIActivity extends AppCompatActivity {


    AutoCompleteTextView bloodgrp;

    com.google.android.material.textfield.TextInputEditText mobile,textVerification;
    com.google.android.material.button.MaterialButton submit;

    boolean isVerified = false, isSubmit = false;

    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_i_i_i);

        initializeComponents();

        String[] bloodGroups = getResources().getStringArray(R.array.blood_groups);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line,bloodGroups);

        bloodgrp.setAdapter(adapter);
    }

    private void initializeComponents() {
        bloodgrp = findViewById(R.id.bloodGrpDropDown);
        mobile = findViewById(R.id.mobileEditText);
        textVerification = findViewById(R.id.verificationText);
        submit = findViewById(R.id.btnVerifySubmit);
    }


    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential credential) {
            textVerification.setText("Verified ! ✔");
            addToDatabase();
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {

            if (e instanceof FirebaseAuthInvalidCredentialsException) {
               textVerification.setText("Failed!");
            } else if (e instanceof FirebaseTooManyRequestsException) {
                textVerification.setText("Message Quota Exceeded!\nTry Again After few Hours!");
            }

           mobile.setEnabled(true);

        }

        @Override
        public void onCodeSent(@NonNull String verificationId,
                               @NonNull PhoneAuthProvider.ForceResendingToken token) {

            textVerification.setText("Enter OTP!");
            submit.setText("Submit");
            id = verificationId;
            isSubmit = true;

        }

    };

    private void addToDatabase() {

        HashMap<String,Object> values = new HashMap<>();
        values.put("Step","Done");
        values.put("Mobile",mobile.getText().toString());
        values.put("BloodGroup",bloodgrp.getText().toString());
        values.put("Visible","True");
        FirebaseDatabase.getInstance().getReference("Donors/"+FirebaseAuth.getInstance().getUid())
                .updateChildren(values)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(RegisterIIIActivity.this,DispalyRequestsActivity.class));
                            RegisterIIIActivity.this.finish();
                        }else {
                            Toast.makeText(RegisterIIIActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public void verifyAndSubmit(View view) {

        mobile.setEnabled(false);
        if(!isSubmit) {
            if (!isVerified && !mobile.getText().toString().isEmpty() && !bloodgrp.getText().toString().isEmpty()) {
                PhoneAuthOptions options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
                        .setPhoneNumber("+91" + mobile.getText().toString())
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(RegisterIIIActivity.this)
                        .setCallbacks(mCallbacks)
                        .build();

                PhoneAuthProvider.verifyPhoneNumber(options);
                textVerification.setText("Verifying...");
            }
            if (mobile.getText().toString().isEmpty()) {
                mobile.setError("Enter Mobile Number!");
            }
        }else {
            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(id,textVerification.getText().toString());
            FirebaseAuth.getInstance().getCurrentUser().linkWithCredential(credential)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                addToDatabase();
                            }else {
                                Toast.makeText(RegisterIIIActivity.this, "Error!\n"+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                submit.setText("Verify");
                                mobile.setEnabled(true);
                                textVerification.setText("Not Verified!");
                                isVerified = false;
                                isSubmit = false;
                            }
                        }
                    });
        }


    }
}