package com.example.mustafaulunproje;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class chatgpt extends AppCompatActivity {
    RecyclerView recyclerView;
    EditText messageEditText;
    ImageButton send_button;
    List<Message> messagesList;
    MessageAdapter messageAdapter;
    private String secretKey;
    ImageButton menuBtn;

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatgpt);
        Intent intent = getIntent();
        secretKey = intent.getStringExtra("SECRET_KEY");
        menuBtn = findViewById(R.id.menu_btn);
        menuBtn.setOnClickListener((v)->showMenu() );
        getSupportActionBar().hide();
        messagesList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messagesList);

        recyclerView = findViewById(R.id.recyler_view);
        messageEditText = findViewById(R.id.message_edit_text);
        send_button = findViewById(R.id.send_button);

        recyclerView.setAdapter(messageAdapter);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

        send_button.setOnClickListener(v -> {
            String question = messageEditText.getText().toString().trim();
            addToChat(question, Message.SENT_BY_ME);
            messageEditText.setText("");
            callAPI(question,secretKey);
        });
    }

    void addToChat(String message, String sentBy) {
        runOnUiThread(() -> {
            messagesList.add(new Message(message, sentBy));
            messageAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
        });
    }

    void addResponse(String response) {
        messagesList.remove(messagesList.size() - 1);
        addToChat(response, Message.SENT_BY_BOT);
    }

    void callAPI(String question, String secretKey) {
        messagesList.add(new Message("Yazıyor...", Message.SENT_BY_BOT));
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("model", "gpt-3.5-turbo");
            JSONArray messageArr = new JSONArray();
            JSONObject obj = new JSONObject();
            obj.put("role", "user");
            obj.put("content", question);
            messageArr.put(obj);
            jsonBody.put("messages", messageArr);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        RequestBody body = RequestBody.create(jsonBody.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .header("Authorization", "Bearer " + secretKey)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addResponse("Mesajlar yüklenemedi" + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = new JSONObject(response.body().string());
                        JSONArray jsonArray = jsonObject.getJSONArray("choices");
                        String result = jsonArray.getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content");
                        addResponse(result.trim());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    addResponse("Cevap Verilemedi" + response.body().string());
                }
            }
        });
    }


    void showMenu(){
        PopupMenu popupMenu  = new PopupMenu(chatgpt.this,menuBtn);
        popupMenu.getMenu().add("Çıkış");
        popupMenu.getMenu().add("Secret Key");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getTitle()=="Çıkış"){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(chatgpt.this,girisactivity.class));
                    finish();
                    return true;
                }
                if(menuItem.getTitle()=="Secret Key"){
                    startActivity(new Intent(chatgpt.this,ID.class));
                    finish();
                    return true;
                }
                return false;
            }
        });

    }
}



