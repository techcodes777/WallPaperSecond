package com.hdlight.wallpaperapps.ui;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hdlight.wallpaperapps.R;
import com.hdlight.wallpaperapps.adapter.FavoriteAdapter;
import com.hdlight.wallpaperapps.adapter.WallPaperAdapter;
import com.hdlight.wallpaperapps.databinding.ActivityFavoriteWallPaperBinding;
import com.hdlight.wallpaperapps.model.Download;
import com.hdlight.wallpaperapps.model.Like;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class FavoriteWallPaperActivity extends BaseActivity  {

    ActivityFavoriteWallPaperBinding binding;

    FavoriteAdapter favoriteAdapter;
    ArrayList<Like> likeArrayList;
    ArrayList<String> list;
    private DatabaseReference reference;
    String mobileNumber;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFavoriteWallPaperBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setNavigationBarColor(Color.BLACK);

        SharedPreferences sp = getSharedPreferences("pass",MODE_PRIVATE);
        if (sp.contains("passVerify")){
            mobileNumber = sp.getString("mobileNo","");
        }

        SharedPreferences spLogin = getSharedPreferences("login",MODE_PRIVATE);
        if (spLogin.contains("mobile")){
            mobileNumber = spLogin.getString("mobile","");
        }

        reference = FirebaseDatabase.getInstance().getReference().child("likes").child(mobileNumber);
        likeArrayList = new ArrayList<>();
        list = new ArrayList<>();
//        loadData();
//        binding.recyclerView.setLayoutManager(new GridLayoutManager(this,3));
//        binding.recyclerView.setHasFixedSize(true);
//        favoriteAdapter = new FavoriteAdapter(likeArrayList,this);
//        binding.recyclerView.setAdapter(favoriteAdapter);
//        favoriteAdapter.notifyDataSetChanged();
//        binding.imgBack.setOnClickListener(this);
        getData();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()){
            case R.id.imgBack:
                onBackPressed();
                break;
        }
    }

    private void getData() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                progressBar.setVisibility(View.GONE);
                list = new ArrayList<>();
                for (DataSnapshot shot : snapshot.getChildren()) {
                    String data = shot.getValue().toString();
                    String[] separated = data.split(",");
                    String image = separated[0];
//                    String image1 = separated[1];
//
//                    Log.e("data", "onDataChange: " + data);
//                    Log.e("image", "onDataChange: " + image);
                    Log.e("image1", "onDataChange: " + image);
//                    Log.e("Gson", "onDataChange: " + organisation);
//                  if (image.length() >= 8){
//                  }
                    String url = image.substring(7);
                    Log.e("image", "onDataChange: " + url);
                    Gson gson = new Gson();
                    String json = gson.toJson(image);
                    list.add(url);
                }
                binding.recyclerView.setLayoutManager(new GridLayoutManager(context, 3));
                favoriteAdapter = new FavoriteAdapter(list, context);
                binding.recyclerView.setAdapter(favoriteAdapter);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    }
                },1000);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                progressBar.setVisibility(View.GONE);
//                swipeRefreshLayout.setVisibility(View.GONE);
                Toast.makeText(context, "Error :" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("shared preferences", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("image", null);
//        Toast.makeText(this, "json" + json, Toast.LENGTH_SHORT).show();
        Log.e("json", "loadData: " + json);
        Type type = new TypeToken<ArrayList<Like>>() {}.getType();
        likeArrayList = gson.fromJson(json, type);


        if (likeArrayList == null) {
            likeArrayList = new ArrayList<>();
        }
    }
}