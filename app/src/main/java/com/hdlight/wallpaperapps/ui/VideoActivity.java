package com.hdlight.wallpaperapps.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hdlight.wallpaperapps.adapter.VideoAdapter;
import com.hdlight.wallpaperapps.databinding.ActivityVideoBinding;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class VideoActivity extends AppCompatActivity {

    ActivityVideoBinding binding;
    GridLayoutManager gridLayoutManager;
    private DatabaseReference reference;
    ArrayList<String> list;
    VideoAdapter videoAdapter;
    private Timer myTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setNavigationBarColor(Color.BLACK);

        gridLayoutManager = new GridLayoutManager(this,3);
        reference = FirebaseDatabase.getInstance().getReference().child("videowp");

        getData();
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        getData();
//    }

    private void getData() {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                progressBar.setVisibility(View.GONE);
                list = new ArrayList<>();
                for (DataSnapshot shot : snapshot.getChildren()) {
                    String data = shot.getValue().toString();
                    list.add(data);
                }
                binding.recyclerVideoPlay.setLayoutManager(gridLayoutManager);
                binding.recyclerVideoPlay.setNestedScrollingEnabled(false);
//                binding.recyclerVideoPlay.setHasFixedSize(false);
                videoAdapter = new VideoAdapter(list, VideoActivity.this);

                binding.progressBar.setVisibility(View.VISIBLE);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.recyclerVideoPlay.setAdapter(videoAdapter);
                        videoAdapter.notifyDataSetChanged();
                        binding.progressBar.setVisibility(View.GONE);
                    }
                }, 2000);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                progressBar.setVisibility(View.GONE);
                Toast.makeText(VideoActivity.this, "Error :" + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}