package com.example.mustafaulunproje;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class yenikullanici extends AppCompatActivity {
    EditText editTextemail;
    EditText editTextpassword;
    Button reg_button;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    Button üyegiris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth=FirebaseAuth.getInstance();
        setContentView(R.layout.activity_yenikullanici);
        editTextemail=findViewById(R.id.edmail);
        editTextpassword=findViewById(R.id.edpassword);
        reg_button=findViewById(R.id.reg_button);
        progressBar=findViewById(R.id.progressBar);
        üyegiris=findViewById(R.id.üyegiris);

        reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password;

                email = String.valueOf(editTextemail.getText());
                password = String.valueOf(editTextpassword.getText());

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(yenikullanici.this, "Mail Giriniz", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(yenikullanici.this, "Şifre Giriniz", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    progressBar.setVisibility(View.GONE);

                                    Toast.makeText(yenikullanici.this, "Kullanıcı Oluşturuldu.", Toast.LENGTH_SHORT).show();

                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(yenikullanici.this, "Kullanıcı oluşturma işlemi başarısız oldu.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        üyegiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(yenikullanici.this,girisactivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
