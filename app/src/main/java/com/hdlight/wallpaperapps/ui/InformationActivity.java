package com.hdlight.wallpaperapps.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.hdlight.wallpaperapps.R;
import com.hdlight.wallpaperapps.databinding.ActivityInformationBinding;

public class InformationActivity extends AppCompatActivity implements View.OnClickListener {

    ActivityInformationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInformationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setNavigationBarColor(Color.BLACK);
        binding.txtTermsCondition.setOnClickListener(this);
        binding.txtAboutZuku.setOnClickListener(this);  
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txtTermsCondition:
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://zuku-c337a.web.app/"));
                startActivity(viewIntent);
                break;
            case R.id.txtAboutZuku:
                Intent viewIntent1 =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://about-5b73a.web.app/"));
                startActivity(viewIntent1);
                break;
        }
    }
}