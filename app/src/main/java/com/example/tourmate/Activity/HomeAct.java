package com.example.tourmate.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.tourmate.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeAct extends AppCompatActivity {
    FirebaseAuth FAuthObj; static FirebaseUser FUserObj;
    Button Explore;
    ImageView ProfileImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_home);

        FAuthObj = FirebaseAuth.getInstance();

        Explore = findViewById(R.id.Explore);

        ProfileImg = findViewById(R.id.ProfileImg);

        Explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeAct.this, TourAct.class);
                startActivity(intent);
            }
        });

        ProfileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeAct.this, ProfileAct.class));
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        FUserObj = FAuthObj.getCurrentUser();
        if(FUserObj == null){
            startActivity(new Intent(this, LoginAct.class));
            if(FAuthObj.getCurrentUser() == null)
                finish();
        }
    }
}