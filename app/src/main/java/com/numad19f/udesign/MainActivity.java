package com.numad19f.udesign;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(isLoggedIn()){
            openHome();
        }else{
            openLogin();
        }
    }

    private void openLogin(){
        Intent login = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(login);
    }

    private void openHome(){
        Intent home = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(home);
    }

    private boolean isLoggedIn(){
        SharedPreferences sp = this.getSharedPreferences("Login", MODE_PRIVATE);
        String user = sp.getString("username", null);
        if(user!=null && !user.isEmpty()){
            return true;
        }
        return false;
    }
}
