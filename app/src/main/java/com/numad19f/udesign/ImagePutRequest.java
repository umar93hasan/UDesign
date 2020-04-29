package com.numad19f.udesign;

import android.content.Context;
import android.util.Log;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class ImagePutRequest {
    private Context context;
    public ImagePutRequest(Context c)
    {
        context = c;
    }
    public void imageUpdate(int id, String image, String imageType, String userName, int likeCount)
    {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String url = "https://api.appery.io/rest/1/apiexpress/api/images/"+id+"?apiKey=98e36939-6f84-4c40-ba2f-d70a64c8b141 ";
        JSONObject jsonBody=null;
        try {
            jsonBody = new JSONObject("{\"likecount\":\""+likeCount+"\","+  "\"image\":\""+image+"\"," +
                    "\"imagetype\":\""+imageType+"\"," +  "\"username\":\""+userName+"\"}");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.PUT,
                url,
                jsonBody,
                new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response){
                        Log.e("Rest Response", response.toString());
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.e("Error Response", error.toString());
                    }
                }
        );
        requestQueue.add(jsonObjectRequest);
    }
}

