package com.numad19f.udesign;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;

public class PostsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private PostsAdapter mPostsAdapter;
    private ArrayList<PostModel> postsList;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts);

        mRecyclerView = findViewById(R.id.posts_recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        postsList = new ArrayList<>();

        mRequestQueue = Volley.newRequestQueue(this);

        getPosts();
    }

    private void getPosts(){

        String postsUrl = "https://api.appery.io/rest/1/apiexpress/api/posts?apiKey=98e36939-6f84-4c40-ba2f-d70a64c8b141";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                postsUrl,
                null,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response){
                        Log.e("Rest Response", response.toString());
                        Gson gson = new Gson();
                        PostModel[] posts = gson.fromJson(response.toString(), PostModel[].class);
                        postsList.addAll(Arrays.asList(posts));
                        //topImagesList.addAll(Arrays.asList(images));
                        mPostsAdapter = new PostsAdapter(PostsActivity.this, postsList);
                        mRecyclerView.setAdapter(mPostsAdapter);
                        //getLikes(images);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.e("Error Response", error.toString());
                    }
                }
        );

        mRequestQueue.add(jsonArrayRequest);

    }

    public void createNewPost(View v){
        Intent intent = new Intent(this, AddPostActivity.class);
        startActivity(intent);
    }

    private void toast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
    }
}
