package com.example.mustafaulunproje;

import static com.example.mustafaulunproje.R.id.reset_password_button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class sifreunuttum extends AppCompatActivity {

    private EditText emailEditText;
    private FirebaseAuth mAuth;
    Button girisyap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sifreunuttum);

        emailEditText = findViewById(R.id.email_edit_text);
        mAuth = FirebaseAuth.getInstance();
        girisyap = findViewById(R.id.girisyap);

        findViewById(R.id.reset_password_button).setOnClickListener(view -> {
            String email = emailEditText.getText().toString().trim();

            if (email.isEmpty()) {
                emailEditText.setError("Email is required");
                emailEditText.requestFocus();
                return;
            }

            mAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(sifreunuttum.this, "Check your email to reset your password", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(sifreunuttum.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        });
        girisyap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(sifreunuttum.this,girisactivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
