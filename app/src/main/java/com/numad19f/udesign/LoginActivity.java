package com.numad19f.udesign;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button login;
    private Button newUser;
    private TextView info;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username_txt);
        password = findViewById(R.id.password_txt);
        login = findViewById(R.id.login_btn);
        newUser = findViewById(R.id.newUser_btn);
        info = findViewById(R.id.failedLogin_txtView);

        requestQueue = Volley.newRequestQueue(this);

        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                loginRequest(username.getText().toString(),password.getText().toString());
            }
        });

        newUser.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent regIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(regIntent);
            }
        });

        requestQueue.addRequestFinishedListener((RequestQueue.RequestFinishedListener<String>) request -> {
            login.setEnabled(true);
        });
    }

    private void login(String username, String password){
        if(username.equalsIgnoreCase("admin") && password.equals("admin")){
            SharedPreferences sp=getSharedPreferences("Login", MODE_PRIVATE);
            SharedPreferences.Editor Ed=sp.edit();
            Ed.putString("username", username);
            Ed.apply();
            openHomePage();
        }else{
            info.setText("Attempt Failed!!!");
        }
    }

    private void loginRequest(final String username, String password){
        String url = "https://api.appery.io/rest/1/apiexpress/api/login/?apiKey=98e36939-6f84-4c40-ba2f-d70a64c8b141&user=" +
                username +
                "&pass=" +
                password;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response){
                        Log.i("Rest Response", response.toString());
                        if(response.toString().contains("\"loginSuccess\":1")){
                            SharedPreferences sp=getSharedPreferences("Login", MODE_PRIVATE);
                            SharedPreferences.Editor Ed=sp.edit();
                            Ed.putString("username", username);
                            Ed.apply();
                            openHomePage();
                        }else{
                            info.setText("Attempt Failed!!!");
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.e("Error Response", error.toString());
                        toast("Can't connect to server");
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
        login.setEnabled(false);
    }

    private void openHomePage(){
        Intent home = new Intent(LoginActivity.this, HomeActivity.class);
        startActivity(home);
    }

    private void toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
}
