package com.numad19f.udesign;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import org.json.JSONArray;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;


public class MenuActivity extends AppCompatActivity{
    private ListView lv;
    private ImageAdapter imageAdapter;
    private String type="";
    private ArrayList<ViewImageModel> imageModelArrayList=new ArrayList<>();
    private View.OnClickListener imageButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onImageAddButtonClick(v);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        lv = findViewById(R.id.listView);
        imageAdapter = new ImageAdapter(this,imageModelArrayList);
        lv.setAdapter(imageAdapter);
        String intentImageType=getIntent().getStringExtra("imageType").toLowerCase();
        if(intentImageType.contains("bed"))
            type="bed";
        else if(intentImageType.contains("living"))
            type="living";
        else if(intentImageType.contains("kids"))
            type="kids";
        else if(intentImageType.contains("garden"))
            type="garden";
        else if(intentImageType.contains("kitchen"))
            type="kitchen";
        else if(intentImageType.contains("patio"))
            type="patio";
        getImages();
        imageAdapter.notifyDataSetChanged();
        FloatingActionButton addLinkButton = findViewById(R.id.addImageBtn);
        addLinkButton.setOnClickListener(imageButtonListener);

    }

    public void onImageAddButtonClick(View v) {
        Intent intent = new Intent(this, UploadImageActivity.class);
        intent.putExtra("imageType", getIntent().getStringExtra("imageType"));
        intent.putExtra("userName", getIntent().getStringExtra("userName"));
        startActivity(intent);
    }
    private void getImages(){

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String imagesUrl = "https://api.appery.io/rest/1/apiexpress/api/images?apiKey=98e36939-6f84-4c40-ba2f-d70a64c8b141";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                imagesUrl,
                null,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response){
                        Log.e("Rest Response", response.toString());
                        Gson gson = new Gson();
                        ImageModel[] images = gson.fromJson(response.toString(), ImageModel[].class);
                        getLikes(images);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.e("Error Response", error.toString());
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);

    }

    public void onBackPressed() {
        Intent intent = new Intent(this, DesignCategoryMenu.class);
        intent.putExtra("imageType", getIntent().getStringExtra("imageType"));
        intent.putExtra("userName", getIntent().getStringExtra("userName"));
        startActivity(intent);
    }
    private void getLikes(final ImageModel[] images){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        String likesUrl = "https://api.appery.io/rest/1/apiexpress/api/likes?apiKey=98e36939-6f84-4c40-ba2f-d70a64c8b141";
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                likesUrl,
                null,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response){
                        Log.e("Rest Response", response.toString());
                        Gson gson = new Gson();
                        LikeModel[] likes = gson.fromJson(response.toString(), LikeModel[].class);
                        getViewImages(images,likes);
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.e("Error Response", error.toString());
                    }
                }
        );

        requestQueue.add(jsonArrayRequest);
    }

    private void getViewImages(ImageModel[] images, LikeModel[] likes){

        ArrayList<ViewImageModel> list = new ArrayList<>();
        String user = getIntent().getStringExtra("userName");
        HashSet<Integer> userLikedImageId = new HashSet<>();
        List<Integer> likeIds=new ArrayList<>();
        for(LikeModel like:likes){
            if(like!= null && like.getUserName().equalsIgnoreCase(user)){
                userLikedImageId.add(like.getImageId());
                likeIds.add(like.getId());
            }
        }
        int i=0;
        for(ImageModel img:images){
            boolean liked  = userLikedImageId.contains(img.getId());
            int likeId=0;
            if(liked) {
                likeId=likeIds.get(i);
                i++;
            }

            ViewImageModel v = new ViewImageModel(img.getId(), img.getImage(), img.getImagetype(), img.getLikecount(), liked, img.getUsername(),likeId);
            list.add(v);
        }
        for(ViewImageModel v:list) {
            if(v.getImageType().toLowerCase().contains(type))
                imageModelArrayList.add(v);
        }
        imageAdapter.notifyDataSetChanged();
    }
}
