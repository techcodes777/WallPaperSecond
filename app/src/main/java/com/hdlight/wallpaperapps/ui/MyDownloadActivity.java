package com.hdlight.wallpaperapps.ui;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hdlight.wallpaperapps.adapter.DownloadAdapter;
import com.hdlight.wallpaperapps.databinding.ActivityMyDownloadBinding;
import com.hdlight.wallpaperapps.model.Download;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MyDownloadActivity extends BaseActivity {

    ActivityMyDownloadBinding binding;
    ArrayList<Download> downloadArrayList;
    private DownloadAdapter downloadAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyDownloadBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getWindow().setNavigationBarColor(Color.BLACK);

        downloadArrayList = new ArrayList<>();

        loadData();

        binding.recyclerViewDownload.setLayoutManager(new GridLayoutManager(this,3));
        binding.recyclerViewDownload.setHasFixedSize(true);
        downloadAdapter = new DownloadAdapter(downloadArrayList);
        binding.recyclerViewDownload.setAdapter(downloadAdapter);
        downloadAdapter.notifyDataSetChanged();

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
//            case R.id.imgBack:
//                onBackPressed();
//                break;
        }
    }

    private void loadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("download preferences", MODE_PRIVATE);
        // creating a variable for gson.
        Gson gson = new Gson();
        String json = sharedPreferences.getString("download", null);
        Type type = new TypeToken<ArrayList<Download>>() {}.getType();
        downloadArrayList = gson.fromJson(json, type);
//        Toast.makeText(this, "json" + json, Toast.LENGTH_SHORT).show();
        Log.e("json", "loadData: " + json);
        Log.e("json", "loadData: " + downloadArrayList);
        if (downloadArrayList == null) {
            // if the array list is empty
            // creating a new array list.
            downloadArrayList = new ArrayList<>();
        }
    }


}