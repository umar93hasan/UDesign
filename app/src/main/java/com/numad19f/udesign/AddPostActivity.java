package com.numad19f.udesign;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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

public class AddPostActivity extends AppCompatActivity {

    private EditText title;
    private EditText body;
    //private Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);

        title = findViewById(R.id.addpost_title);
        body = findViewById(R.id.addpost_body);
        //submit = findViewById(R.id.submit_post_btn);
    }

    public void addPost(View view){
        SharedPreferences sp = this.getSharedPreferences("Login", MODE_PRIVATE);
        String user = sp.getString("username", null);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String url = "https://api.appery.io/rest/1/apiexpress/api/posts?apiKey=98e36939-6f84-4c40-ba2f-d70a64c8b141";
        JSONObject jsonBody=null;
        try {
            jsonBody = new JSONObject("{\"username\":\""+user+"\"," +
                    "\"body\":\""+body.getText().toString()+"\"," +
                    "\"title\":\""+title.getText().toString()+"\"}");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonBody,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response){
                        Log.e("Rest Response", response.toString());
                        Intent intent = new Intent(AddPostActivity.this, PostsActivity.class);
                        startActivity(intent);
                        toast("Post Added");
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.e("Error Response", error.toString());
                        toast("Couldn't add post. Try again later.");
                    }
                }
        );

        requestQueue.add(jsonObjectRequest);

    }

    private void toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
