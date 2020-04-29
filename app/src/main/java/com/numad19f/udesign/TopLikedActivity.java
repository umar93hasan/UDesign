package com.numad19f.udesign;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TopLikedActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private TopLikedAdapter mTopLikedAdapter;
    private ArrayList<ImageModel> topImagesList;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_liked);

        mRecyclerView = findViewById(R.id.topLiked_recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        topImagesList = new ArrayList<>();

        mRequestQueue = Volley.newRequestQueue(this);

        getImages();
    }

    private void getImages(){

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

        mRequestQueue.add(jsonArrayRequest);

    }

    private void getLikes(final ImageModel[] images){

        String likesUrl = "https://api.appery.io/rest/1/apiexpress/api/likes/?apiKey=98e36939-6f84-4c40-ba2f-d70a64c8b141";
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

        mRequestQueue.add(jsonArrayRequest);
    }

    private void getViewImages(ImageModel[] images, LikeModel[] likes){
        HashMap<Integer,Integer> map = new HashMap<>();
        for (LikeModel like:likes){
            //2019-12-07 12:00:43
            try {
                String likedDate = like.getLikedOn();
                SimpleDateFormat formatter=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date=formatter.parse(likedDate);
                long DAY_IN_MS = 1000 * 60 * 60 * 24;
                Date date1 = new Date(System.currentTimeMillis() - (7 * DAY_IN_MS));
                Date date2 = new Date(System.currentTimeMillis());
                if(date1.compareTo(date) * date.compareTo(date2) < 0){
                    continue;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            int imgId = like.getImageId();
            if(map.containsKey(imgId)){
                map.put(imgId,map.get(imgId)+1);
            }else{
                map.put(imgId,1);
            }
        }
        List<Map.Entry<Integer, Integer>> sorted = sortByValue(map);

        HashMap<Integer,ImageModel> imageMap = new HashMap<>();
        for(ImageModel img:images){
            imageMap.put(img.getId(),img);
        }

        int count=1;

        for (Map.Entry<Integer, Integer> entry : sorted) {
            if(count>3)
                break;
            topImagesList.add(imageMap.get(entry.getKey()));
            count++;
        }

        mTopLikedAdapter = new TopLikedAdapter(TopLikedActivity.this, topImagesList);
        mRecyclerView.setAdapter(mTopLikedAdapter);
    }


    private static List<Map.Entry<Integer, Integer>> sortByValue(HashMap<Integer, Integer> hm)
    {
        List<Map.Entry<Integer, Integer> > list =
                new LinkedList<Map.Entry<Integer, Integer> >(hm.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<Integer, Integer> >() {
            public int compare(Map.Entry<Integer, Integer> o1,
                               Map.Entry<Integer, Integer> o2)
            {
                return (o2.getValue()).compareTo(o1.getValue());
            }
        });

        HashMap<Integer, Integer> sorted = new LinkedHashMap<Integer, Integer>();
        for (Map.Entry<Integer, Integer> aa : list) {
            sorted.put(aa.getKey(), aa.getValue());
        }
        return list;
    }

}
