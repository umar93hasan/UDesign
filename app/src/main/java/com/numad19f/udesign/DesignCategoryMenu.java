package com.numad19f.udesign;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

public class DesignCategoryMenu extends AppCompatActivity {
    private List<CategoryItem> items;
    private RecyclerView rv;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_design_category_menu);
        rv=findViewById(R.id.rv);
        userName=getIntent().getStringExtra("userName");
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);
        initializeData();
        initializeAdapter();
    }

    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("imageType", getIntent().getStringExtra("imageType"));
        intent.putExtra("userName", getIntent().getStringExtra("userName"));
        startActivity(intent);
    }
    private void initializeData(){
        items = new ArrayList<>();
        items.add(new CategoryItem("Bedroom Designs", R.drawable.bedroom));
        items.add(new CategoryItem("Living Room Designs", R.drawable.living));
        items.add(new CategoryItem("Kitchen Designs", R.drawable.kitchen));
        items.add(new CategoryItem("Kids Room Designs", R.drawable.kids));
        items.add(new CategoryItem("Patio Designs",  R.drawable.patio));
        items.add(new CategoryItem("Garden Designs", R.drawable.garden));
    }

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(items,userName);
        rv.setAdapter(adapter);
    }
}
