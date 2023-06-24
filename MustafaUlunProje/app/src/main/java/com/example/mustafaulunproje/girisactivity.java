package com.example.mustafaulunproje;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mustafaulunproje.databinding.ActivityGirisactivityBinding;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class girisactivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks {
    ActivityGirisactivityBinding binding;
    CheckBox checkBox;
    GoogleApiClient googleApiClient;
    String SiteKey = "6LdKuWQmAAAAAHy8vitfjROUAuAtQoVG1Q0jePVK";
    FirebaseAuth mAuth;
    private boolean isRecaptchaVerified = false;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGirisactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean rememberMeChecked = sharedPreferences.getBoolean("rememberMeChecked", false);
        String savedEmail = sharedPreferences.getString("savedEmail", "");
        String savedPassword = sharedPreferences.getString("savedPassword", "");
        checkBox = binding.checkBox; // CheckBox nesnesini atama yapın
        checkBox.setChecked(rememberMeChecked);
        binding.edemail.setText(savedEmail);
        binding.edpassword.setText(savedPassword);

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(girisactivity.this)
                .addApi(SafetyNet.API)
                .addConnectionCallbacks(girisactivity.this)
                .build();
        googleApiClient.connect();

        binding.logButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isRecaptchaVerified) {
                    // Perform the login process
                    String email, password;
                    email = String.valueOf(binding.edemail.getText());
                    password = String.valueOf(binding.edpassword.getText());

                    if (TextUtils.isEmpty(email)) {
                        Toast.makeText(girisactivity.this, "Lütfen e-posta adresinizi girin.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (TextUtils.isEmpty(password)) {
                        Toast.makeText(girisactivity.this, "Lütfen şifrenizi girin.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(girisactivity.this, "Başarıyla giriş yapıldı.", Toast.LENGTH_SHORT).show();

                                        String kullanici = binding.edemail.getText().toString();
                                        Intent profil = new Intent(girisactivity.this, kararver.class);
                                        profil.putExtra("name", kullanici);
                                        startActivity(profil);
                                        finish(); // Giriş yapıldıktan sonra bu aktiviteyi kapat

                                        // Giriş işlemi başarıyla tamamlandı, flag değerini güncelle
                                        SharedPreferences sp = getSharedPreferences("giris", MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putBoolean("flag", true);
                                        editor.apply();
                                    } else {
                                        String errorText = "Giriş yapılamadı: " + task.getException().getMessage();
                                        Toast.makeText(girisactivity.this, errorText, Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else {
                    // Recaptcha doğrulanmadıysa
                    Toast.makeText(girisactivity.this, "Lütfen reCAPTCHA'yı doğrulayın.", Toast.LENGTH_SHORT).show();
                }
            }
        });


        binding.sifreunuttum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(girisactivity.this, sifreunuttum.class);
                startActivity(intent);
                finish();
            }
        });

        binding.yenikullanici.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(girisactivity.this, yenikullanici.class);
                startActivity(intent);
                finish();
            }
        });

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox.isChecked()) {
                    SafetyNet.SafetyNetApi.verifyWithRecaptcha(googleApiClient, SiteKey)
                            .setResultCallback(new ResultCallback<SafetyNetApi.RecaptchaTokenResult>() {
                                @Override
                                public void onResult(@NonNull SafetyNetApi.RecaptchaTokenResult recaptchaTokenResult) {
                                    Status status = recaptchaTokenResult.getStatus();

                                    if ((status != null) && status.isSuccess()) {
                                        Toast.makeText(girisactivity.this, "Başarılı Doğrulama", Toast.LENGTH_SHORT).show();
                                        // Update the flag
                                        isRecaptchaVerified = true;
                                    } else {
                                        // Update the flag
                                        isRecaptchaVerified = false;
                                    }
                                }
                            });
                } else {
                    // Update the flag
                    isRecaptchaVerified = false;
                }
            }
        });
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("rememberMeChecked", checkBox.isChecked());
        editor.putString("savedEmail", binding.edemail.getText().toString());
        editor.putString("savedPassword", binding.edpassword.getText().toString());
        editor.putBoolean("isRecaptchaVerified", isRecaptchaVerified); // reCAPTCHA doğrulama durumunu kaydet
        editor.apply();
    }
}
