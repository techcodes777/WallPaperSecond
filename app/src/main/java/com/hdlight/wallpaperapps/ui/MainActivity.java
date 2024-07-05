package com.hdlight.wallpaperapps.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hdlight.wallpaperapps.R;
import com.hdlight.wallpaperapps.adapter.ViewPagerAdapter;
import com.hdlight.wallpaperapps.databinding.ActivityMainBinding;
import com.hdlight.wallpaperapps.fragment.DoubleFragment;
import com.hdlight.wallpaperapps.fragment.TrendingFragment;
import com.hdlight.wallpaperapps.fragment.WallpaperFragment;
import com.hdlight.wallpaperapps.model.ScrimInsetsFrameLayout;
import com.hdlight.wallpaperapps.utils.Constant;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    ActivityMainBinding binding;
    ImageView imgMenu;
    DrawerLayout drawerLayout;
    ScrimInsetsFrameLayout mLayout;
    TabLayout tab;
    ViewPager viewPager;
    ViewPagerAdapter pagerAdapter;
    FirebaseUser user;
    DatabaseReference databaseReference;
    String userId;
    private FirebaseAuth firebaseAuth;
    String mobileNumber;
    public static final String MOBILE = "mobile";
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        setLightStatusBar(binding.drawerLayout, MainActivity.this);
//        mobileNumber = getIntent().getStringExtra(MOBILE);
        getWindow().setNavigationBarColor(Color.BLACK);
//        searchView = (SearchView) findViewById(R.id.searchView);

        tab = findViewById(R.id.tab);
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        if (userId != null){
            userId = user.getUid();
        }

        viewPager = findViewById(R.id.view_pager);
//        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
//        viewPager.setAdapter(viewPagerAdapter);
//        tab.setupWithViewPager(viewPager);
        if (viewPager != null) {
            setupViewPager(viewPager);
        }

        //Init and attach
        firebaseAuth = FirebaseAuth.getInstance();
//Call signOut()
//        imgMenu = (ImageView) findViewById(R.id.imgMenu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mLayout = (ScrimInsetsFrameLayout) findViewById(R.id.containers);
//        drawerLayout.closeDrawer(mLayout);

//        imgMenu.setOnClickListener(v -> {
//            drawerLayout.openDrawer(mLayout);
//            drawerLayout.openDrawer(GravityCompat.START);
//        });

        binding.imgDrawerMenu.setOnClickListener(this);
        binding.imgFavWP.setOnClickListener(this);
        binding.imgSearch.setOnClickListener(this);

//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                Constant.SEARCH = query;
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                Constant.SEARCH = newText;
//                return false;
//            }
//        });


    }

    private void setupViewPager(ViewPager viewPager) {
//        String normalBefore= "First Part Not Bold ";
//        Spannable sb = new SpannableString( normalBefore );
//        sb.setSpan(new StyleSpan(Typeface.BOLD),
//                sb.length() - normalBefore.length(), sb.length(),
//                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), this);
        pagerAdapter.addFragment(new TrendingFragment(), "HOME");
        pagerAdapter.addFragment(new DoubleFragment(), "CATEGORIES");
        pagerAdapter.addFragment(new WallpaperFragment(), "PREMIUM");
        viewPager.setAdapter(pagerAdapter);
        tab.setupWithViewPager(viewPager);
//        Bundle bundle = new Bundle();
//        bundle.putString("mobile", mobileNumber);
//        Fragment myObj = new TrendingFragment();
//        myObj.setArguments(bundle);
//        Bundle bundle = this.getArguments();
        }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgDrawerMenu:
                drawerLayout.openDrawer(mLayout);
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.imgFavWP:
                startActivity(new Intent(this,FavoriteWallPaperActivity.class));
                break;
            case R.id.imgSearch:
                loadFragment(new TrendingFragment());
                break;

        }
    }

    public void checkProfileStatus(){
        databaseReference.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                context.startActivity(new Intent(context,MainActivity.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void loadFragment(Fragment fragment) {
        Bundle b = new Bundle();
        b.putString("search", "1");
        fragment.setArguments(b);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("MainActivity", "onPause: " );
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void relativeHome(View view){
        closeDrawer(drawerLayout);
    }

    public void relativeLike(View view){
        closeDrawer(drawerLayout);
        startActivity(new Intent(this,FavoriteWallPaperActivity.class));
    }

    public void relativeLogout(View view){
//        closeDrawer(drawerLayout);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setMessage(getString(R.string.exit))
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        logoutAPP();
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
//        Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
    }

    public void logoutAPP(){
        firebaseAuth.signOut();
        SharedPreferences sp = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.apply();

        SharedPreferences spR = context.getSharedPreferences("pass", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = spR.edit();
        editor1.clear();
        editor1.apply();

        startActivity(new Intent(MainActivity.this,LoginActivity.class));
        finish();
    }

    public void relativeDownload(View view)
    {
        closeDrawer(drawerLayout);
        startActivity(new Intent(MainActivity.this,MyDownloadActivity.class));
    }

    public void relativeSettings(View view)
    {
        closeDrawer(drawerLayout);
        startActivity(new Intent(MainActivity.this,SettingActivity.class));
    }

    public void relativeHelp(View view){
        closeDrawer(drawerLayout);
        startActivity(new Intent(MainActivity.this,HelpActivity.class));
    }

    public void relativeInformation(View view){
        closeDrawer(drawerLayout);
        startActivity(new Intent(MainActivity.this,InformationActivity.class));
    }

    public void relativeVideoWp(View view){
        closeDrawer(drawerLayout);
        startActivity(new Intent(MainActivity.this,VideoActivity.class));
    }

//    public void relativeFav(View view){
//        closeDrawer(drawerLayout);
//        startActivity(new Intent(MainActivity.this,FavoriteWallPaperActivity.class));
//    }

    public void relativeWallpapers(View view){
        closeDrawer(drawerLayout);
    }

    @Override
    public void onBackPressed() {
        if (Constant.BACK){
//            Toast.makeText(this, "true", Toast.LENGTH_SHORT).show();
            TrendingFragment fragment = new TrendingFragment();
            Bundle b = new Bundle();
            b.putString("search", "0");
            fragment.setArguments(b);
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, fragment);
            fragmentTransaction.commit();
        }else {
            super.onBackPressed();
        }
    }



}