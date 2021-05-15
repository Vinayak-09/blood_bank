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
import com.google.firebase.auth.FirebaseUser;

public class LoginScreenActivity extends AppCompatActivity {

    
    com.google.android.material.textfield.TextInputEditText Email,Pass;
    com.google.android.material.button.MaterialButton login;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        
        Email = findViewById(R.id.emailLogin);
        Pass = findViewById(R.id.passLogin);
    }

    public void OpenRegisterActivity(View view) {
        startActivity(new Intent(LoginScreenActivity.this,RegisterIActivity.class));
    }


    public void LoginNow(View view) {
        if(!Email.getText().toString().isEmpty() && !Pass.getText().toString().isEmpty()){
            FirebaseAuth.getInstance().signInWithEmailAndPassword(Email.getText().toString(),Pass.getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                startActivity(new Intent(LoginScreenActivity.this,SplashScreen.class));
                            }else {
                                Toast.makeText(LoginScreenActivity.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}