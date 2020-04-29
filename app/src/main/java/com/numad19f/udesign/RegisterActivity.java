package com.numad19f.udesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    private EditText username;
    private EditText email;
    private EditText password;
    private EditText confPass;
    private Button register;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.regUserName);
        email = findViewById(R.id.regEmail);
        password = findViewById(R.id.regPassword);
        confPass = findViewById(R.id.regConfPass);
        register = findViewById(R.id.register_btn);
        requestQueue = Volley.newRequestQueue(this);
        register.setOnClickListener(view -> register(username.getText().toString(),email.getText().toString(),password.getText().toString(),confPass.getText().toString()));

        requestQueue.addRequestFinishedListener((RequestQueue.RequestFinishedListener<String>) request -> {
            register.setEnabled(true);
        });
    }

    private void register(String username, String email, String password, String confPass) {
        if(!password.equals(confPass)){
            toast("Passwords Don't Match");
            return;
        }
        if(username.isEmpty() || email.isEmpty() || password.isEmpty()){
            toast("Please fill all fields.");
            return;
        }
        //code to add user
        String url = "https://api.appery.io/rest/1/apiexpress/api/users?apiKey=98e36939-6f84-4c40-ba2f-d70a64c8b141";

        JSONObject jsonBody=null;
        try {
            jsonBody = new JSONObject("{\"username\":\""+username+"\"," +
                    "\"email\":\""+email+"\"," +
                    "\"password\":\""+password+"\"}");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody,
                response -> {
                    Log.e("Rest Response", response.toString());
                    Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(loginIntent);
                    toast("User Created. Login to get started.");
                },
                error -> {
                    Log.e("Error Response", error.toString());
                    toast("Couldn't create user. Try different username.");
                }
        );
        register.setEnabled(false);
        requestQueue.add(jsonObjectRequest);
    }

    private void toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
