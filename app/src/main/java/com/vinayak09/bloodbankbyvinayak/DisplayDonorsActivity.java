package com.vinayak09.bloodbankbyvinayak;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vinayak09.bloodbankbyvinayak.adapters.UserAdapter;
import com.vinayak09.bloodbankbyvinayak.model.User;

import java.util.ArrayList;
import java.util.HashMap;

public class DisplayDonorsActivity extends AppCompatActivity {


    RecyclerView list;
    UserAdapter adapter;
    ArrayList<User> users,temp;
    EditText districtFilter;
    User self;
    String uid = FirebaseAuth.getInstance().getUid();
    PopupMenu popupMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_donors);

        initializeComponents();
        getDonors();
    }



    void initializeComponents() {



        popupMenu = new PopupMenu(this, findViewById(R.id.more));
        popupMenu.getMenuInflater().inflate(R.menu.donors_menu,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {


            if(item.getItemId() == R.id.changePass){
                FirebaseAuth.getInstance().sendPasswordResetEmail(self.getEmail());
                Snackbar snack = Snackbar.make(findViewById(android.R.id.content),"Password Reset Link Sent On Registered Email.", Snackbar.LENGTH_LONG);
                View view1 = snack.getView();
                FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view1.getLayoutParams();
                params.gravity = Gravity.CENTER_VERTICAL;
                view1.setLayoutParams(params);

                if(self!=null) {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(self.getEmail());
                }else {
                    snack.setText("Error Occurred!");
                }

                snack.show();
            }else if(item.getItemId() == R.id.logout){
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this,SplashScreen.class));
                DisplayDonorsActivity.this.finish();
            }else if(item.getItemId() == R.id.visibleDonors){
                if(item.isChecked()){
                    item.setChecked(false);
                    updateVisible(false);
                }else {
                    item.setChecked(true);
                    updateVisible(true);
                }
            }
            return true;
        });
        self = new User();
        districtFilter = findViewById(R.id.districtFilter);
        list = findViewById(R.id.donorsList);
        users = new ArrayList<>();
        temp = new ArrayList<>();
        adapter = new UserAdapter(this, users, position -> {
            //Handle call button event
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:"+temp.get(position).getMobile()));
            startActivity(intent);
        }, position -> {
            //Handle share button event
            User sent = temp.get(position);
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TITLE,"Be Hero, Donate Blood.");
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Hello.\n"+"Here is the Information about blood Donor:\n"+sent.getFName()+" "+sent.getLName()+"\nBlood Group : "+sent.getBloodGroup()+"\nAddress:"+sent.getState()+" "+sent.getDistrict()+" "+sent.getTehsil()+"\nMobile Number :"+sent.getMobile());
            sendIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sendIntent, "Be Hero, Donate Blood.");
            startActivity(shareIntent);

        });
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);
        districtFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                updateList(s.toString());
            }
        });
    }

    private void updateVisible(boolean b) {
        HashMap<String, Object> updateValues = new HashMap<>();

        if(b){
            updateValues.put("Visible","True");
        }else {
            updateValues.put("Visible","False");
        }
        FirebaseDatabase.getInstance().getReference("Donors").child(uid).updateChildren(updateValues);
    }

    private void updateList(String s) {
        System.out.println(s);
        temp.clear();
        for( User v : users){
            if(v.getDistrict().toUpperCase().contains(s)||s.equalsIgnoreCase("ALL")) {
                System.out.println(v.getDistrict());
                temp.add(v);
            }
        }
        adapter.updateList(temp);
    }


    private void getDonors() {
        FirebaseDatabase.getInstance().getReference("Donors").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                temp.clear();
                for(DataSnapshot ds:snapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    if(user.getStep().equals("Done")) {
                        if (user.getVisible().equals("True")) {
                            users.add(user);
                            temp.add(user);
                        }
                        if (user.getUID().equals(uid)) {
                            self = user;
                            popupMenu.getMenu().findItem(R.id.visibleDonors).setChecked(self.getVisible().equals("True"));
                        }
                    }
                }
                updateList(districtFilter.getText().toString());
                filterList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void filterList() {
        adapter.notifyDataSetChanged();
    }

    public void viewRequestList(View view) {
        startActivity(new Intent(DisplayDonorsActivity.this,DispalyRequestsActivity.class));
        this.finish();
    }

    public void popUp(View view) {

        popupMenu.show();
    }



}