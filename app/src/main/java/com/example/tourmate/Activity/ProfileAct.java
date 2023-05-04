package com.example.tourmate.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tourmate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class ProfileAct extends AppCompatActivity{
    TextView UserNameTV, EmailTV, OccupationTV, MobileNoTV;
    EditText UserNameET, MobileNoET;
    Button ChangeInfoBtn, LogoutBtn;
    ProgressBar Progressbar;
    Toolbar toolbar;
    String UserID;
    Intent intent;

    //Firebase
    FirebaseAuth FAuthObj;
    FirebaseFirestore FFStoreObj;
    DocumentReference DRObj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_profile);

        //TextView
        UserNameTV = findViewById(R.id.UserNameTV);
        EmailTV = findViewById(R.id.EmailTV);
        OccupationTV = findViewById(R.id.OccupationTV);
        MobileNoTV = findViewById(R.id.MobileNoTV);
        //Button
        ChangeInfoBtn = findViewById(R.id.ChangeInfoBtn);
        LogoutBtn = findViewById(R.id.LogoutBtn);

        //Firebase
        FAuthObj = FirebaseAuth.getInstance();
        FFStoreObj = FirebaseFirestore.getInstance();

        intent = new Intent(this, LoginAct.class);

        UserID = FAuthObj.getCurrentUser().getUid();
        DRObj = FFStoreObj.collection("Users").document(UserID);

        LoadUserData();
        LogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProcessLogOut();
            }
        });
    }
    private void LoadUserData() {
        DRObj.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                UserNameTV.setText("User name: "+snapshot.getString("UserName"));
                EmailTV.setText("Email: "+snapshot.getString("Email"));
                /*if(snapshot.getString("Occu")!= "")
                    OccupationTV.setText("Occupation: "+snapshot.getString("Occu"));
                else
                    OccupationTV.setText("Occupation: None"); */

                if(snapshot.getString("MobileNo")!= "")
                    MobileNoTV.setText("Mobile No: "+snapshot.getString("MobileNo"));
                else
                    MobileNoTV.setText("Mobile No: None");
            }
        });
    }
    private void ProcessLogOut() {
        FAuthObj.signOut();
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("EXIT", true);
        startActivity(intent);
        finish();
    }
}