package com.example.mustafaulunproje;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.google.firebase.auth.FirebaseAuth;

public class ID extends AppCompatActivity {

    private EditText secretKeyEditText;
    private Button sendButton;
    ImageButton menuBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id);
        getSupportActionBar().hide();
        menuBtn = findViewById(R.id.menu_btn);
        menuBtn.setOnClickListener((v) -> showMenu());

        secretKeyEditText = findViewById(R.id.secretKeyEditText);
        sendButton = findViewById(R.id.sendButton);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String secretKey = secretKeyEditText.getText().toString();

                Intent intent = new Intent(ID.this, chatgpt.class);
                intent.putExtra("SECRET_KEY", secretKey);
                startActivity(intent);
            }
        });
    }




    void showMenu() {
        PopupMenu popupMenu = new PopupMenu(ID.this, menuBtn);
        popupMenu.getMenu().add("Çıkış");
        popupMenu.getMenu().add("Chat-Et");
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getTitle().equals("Çıkış")) {
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(ID.this, girisactivity.class));
                    finish();
                    return true;
                }

                if (menuItem.getTitle().equals("Chat-Et")) {
                    startActivity(new Intent(ID.this, chatgpt.class));
                    finish();
                    return true;
                }

                return false;
            }
        });
    }
}

