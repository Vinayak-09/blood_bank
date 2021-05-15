package com.vinayak09.bloodbankbyvinayak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterIActivity extends AppCompatActivity {

    com.google.android.material.textfield.TextInputEditText fName,lName,email,pass;
    com.google.android.material.button.MaterialButton nextToI;
    com.google.android.material.progressindicator.LinearProgressIndicator progressIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_i);
        initializeComponents();
    }

    private void initializeComponents() {
        fName = findViewById(R.id.fNameInput);
        lName = findViewById(R.id.lNameInput);
        email = findViewById(R.id.emailInput);
        pass = findViewById(R.id.passInput);
        nextToI = findViewById(R.id.nextButtonI);
        progressIndicator = findViewById(R.id.iProgressbar);
    }

    public void nextRegisterPage(View view) {
        String f_name,l_name,emailText,passText;
        f_name = fName.getText().toString();
        l_name = lName.getText().toString();
        emailText = email.getText().toString().toLowerCase();
        passText = pass.getText().toString();

        if(f_name.isEmpty()){
            fName.setError("Fill this field.");
        }
        if(emailText.isEmpty()){
            email.setError("Fill this field.");
        }
        if (l_name.isEmpty()){
            lName.setError("Fill this field.");
        }
        if(passText.isEmpty()){
            pass.setError("Fill this field.");
        }

        if(! f_name.isEmpty() && ! l_name.isEmpty() && ! emailText.isEmpty() && ! passText.isEmpty()){
            RegisterUser(f_name,l_name,emailText,passText);
        }

    }

    private void RegisterUser(String f_name, String l_name, String emailText, String passText) {
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailText,passText)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        addToDatabase(task.getResult().getUser().getUid(),f_name,l_name,emailText);
                    }else {
                        Toast.makeText(RegisterIActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void addToDatabase(String uid, String f_name, String l_name, String emailText) {

        HashMap<String,String>values = new HashMap<>();
        values.put("FName",f_name);
        values.put("LName",l_name);
        values.put("Email",emailText);
        values.put("UID",uid);
        values.put("Step","1");
        values.put("Visible","False");
        values.put("RequestBlood","False");
        values.put("State","None");
        values.put("District","None");
        values.put("Tehsil","None");
        values.put("Village","None");
        values.put("Mobile","None");
        values.put("BloodGroup","None");

        FirebaseDatabase.getInstance().getReference("Donors")
                .child(uid).setValue(values).addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        startActivity(new Intent(RegisterIActivity.this,RegisterIIActivity.class));
                    }
                });


    }
}