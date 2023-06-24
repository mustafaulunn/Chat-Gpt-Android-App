package com.example.mustafaulunproje;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        getSupportActionBar().hide();

        SharedPreferences sp = getSharedPreferences("giris", MODE_PRIVATE);
        Boolean kontrol = sp.getBoolean("flag", false);

        if (kontrol) {
            Intent intent = new Intent(splash.this, kararver.class);
            startActivity(intent);
            finish();
        } else {
            Thread thread = new Thread() {
                public void run() {
                    try {
                        sleep(4000);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    } finally {
                        Intent intent = new Intent(splash.this, girisactivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            };
            thread.start();
        }
    }
}
