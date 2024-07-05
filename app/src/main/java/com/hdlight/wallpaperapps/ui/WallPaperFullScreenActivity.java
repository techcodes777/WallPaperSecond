package com.hdlight.wallpaperapps.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hdlight.wallpaperapps.R;
import com.hdlight.wallpaperapps.databinding.ActivityDownloadWallPaperBinding;

public class WallPaperFullScreenActivity extends BaseActivity {

    ActivityDownloadWallPaperBinding binding;
    public static final String IMAGES = "images";
    public static final String POSITION = "position";
    int position;
    ImageView imgDownloadWallPaper;
    String image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDownloadWallPaperBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setNavigationBarColor(Color.BLACK);
        imgDownloadWallPaper = findViewById(R.id.imgWallPaperDownload);
        image = getIntent().getStringExtra(IMAGES);
        position = getIntent().getIntExtra(POSITION, position);
        Log.e("Activity", "onCreate: " + image);

        Glide.with(getApplicationContext()).load(image).diskCacheStrategy(DiskCacheStrategy.ALL).into(binding.imgWallPaperDownload);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {

        }
    }
}