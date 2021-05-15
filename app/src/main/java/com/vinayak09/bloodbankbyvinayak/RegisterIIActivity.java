package com.vinayak09.bloodbankbyvinayak;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterIIActivity extends AppCompatActivity {


    AutoCompleteTextView states;

    com.google.android.material.textfield.TextInputEditText District,Tehsil,Village;
    com.google.android.material.button.MaterialButton nextToIII;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_i_i);

        initializeComponents();

        states.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.states)));

    }

    private void initializeComponents() {
        District = findViewById(R.id.districtRegister);
        Tehsil = findViewById(R.id.TehsilRegister);
        Village = findViewById(R.id.VillageRegister);
        states = findViewById(R.id.stateDropDrown);
        nextToIII = findViewById(R.id.nextButtonII);
    }

    public void registerIII(View view) {
        String districtT,tehsilT,villageT,stateT;
        districtT = District.getText().toString();
        tehsilT = Tehsil.getText().toString();
        villageT = Village.getText().toString();
        stateT = states.getText().toString();

        if(!districtT.isEmpty() && !tehsilT.isEmpty() && !villageT.isEmpty() && !stateT.isEmpty() && !stateT.equalsIgnoreCase("State")){
            addDataToFirebaseStorage(stateT,districtT,tehsilT,villageT,FirebaseAuth.getInstance().getUid());
        }

        if(stateT.isEmpty() || stateT.equalsIgnoreCase("state")){
            states.setError("Fill this field.");
        }
        if(districtT.isEmpty()){
            District.setError("Fill this field.");
        }
        if(tehsilT.isEmpty()){
            Tehsil.setError("Fill this field.");
        }
        if(villageT.isEmpty()){
            Village.setError("Fill this field.");
        }
        startActivity(new Intent(RegisterIIActivity.this,RegisterIIIActivity.class));
    }

    private void addDataToFirebaseStorage(String stateT, String districtT, String tehsilT, String villageT, String uid) {

        HashMap<String,Object> values = new HashMap<>();
        values.put("State",stateT);
        values.put("District",districtT);
        values.put("Tehsil",tehsilT);
        values.put("Village",villageT);
        values.put("Step","2");

        FirebaseDatabase.getInstance().getReference("Donors")
                .child(uid).updateChildren(values);
    }


}