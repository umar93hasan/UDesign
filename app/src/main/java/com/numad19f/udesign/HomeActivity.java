package com.numad19f.udesign;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomeActivity extends AppCompatActivity {

    private View.OnClickListener newActivityButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            newActivityButtonClick(v);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        CardView btn = findViewById(R.id.getdata_button);
        btn.setOnClickListener(newActivityButtonListener);
    }

    public void openTopLiked(View view){
        Intent intent = new Intent(this, TopLikedActivity.class);
        startActivity(intent);
    }

    public void openPosts(View view){
        Intent intent = new Intent(this, PostsActivity.class);
        startActivity(intent);
    }

    public void logOut(View v){
        SharedPreferences sp = this.getSharedPreferences("Login",MODE_PRIVATE);
        SharedPreferences.Editor Ed = sp.edit();
        Ed.remove("username");
        Ed.apply();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void newActivityButtonClick(View view){
        SharedPreferences sp = this.getSharedPreferences("Login", MODE_PRIVATE);
        String user = sp.getString("username", null);
        Intent intent=new Intent(this,DesignCategoryMenu.class);
        intent.putExtra("userName", user);
        startActivity(intent);
    }

    public void openARCamera(View view){
        //Add code here to start ARCamera Activity
        //Intent intent = new Intent(this, ARCameraActivity.class);
        //startActivity(intent);
        Intent intent = new Intent(this, ARActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Exit")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        startActivity(intent);
                    }
                }).create().show();
    }
}
