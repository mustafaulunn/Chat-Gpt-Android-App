package com.example.mustafaulunproje;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class profile extends AppCompatActivity {
    TextView ProfID;
    String kullanici;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profil");


        ProfID = findViewById(R.id.tvProfID);
        Intent intent = getIntent();
        kullanici = intent.getStringExtra("name");
        ProfID.setText(kullanici);
    }

    public boolean onSupportNavigateUp(){
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
