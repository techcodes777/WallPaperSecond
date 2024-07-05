package com.hdlight.wallpaperapps.ui;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hdlight.wallpaperapps.BuildConfig;
import com.hdlight.wallpaperapps.R;
import com.hdlight.wallpaperapps.database.DataBaseHelper;
import com.hdlight.wallpaperapps.databinding.ActivityFullWallPaperImageBinding;
import com.hdlight.wallpaperapps.model.Download;
import com.hdlight.wallpaperapps.model.Position;
import com.jackandphantom.blurimage.BlurImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class WallPaperImageActivity extends BaseActivity implements View.OnClickListener {

    public static final String IMAGES = "images";
    public static final String POSITION = "position";
    public static final String SEARCH = "search";
    public static final String SEARCHOTH = "searchoth";

    ImageView imgFullImage;
    public String single_choice_selected;
    MaterialAlertDialogBuilder builder;
    AlertDialog progressDialog;
    private ViewGroup root;
    ActivityFullWallPaperImageBinding binding;
    String image;
    int position;
    byte[] byteArray;
    Bitmap bitmap;
    Bitmap finalBitmap;
    ProgressBar progressBar;
    LinearLayout linearBottomSheetDialog;
    WallpaperManager myWallpaperManager;
    private ArrayList<Download> downloadArrayList;
    HashMap<Integer, Integer> map = new HashMap<>();
    String aString;
    DatabaseReference reference;
    String mobileNumber;
    boolean search = false;
    boolean searchOth = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFullWallPaperImageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setNavigationBarColor(Color.BLACK);
        image = getIntent().getStringExtra(IMAGES);
        position = getIntent().getIntExtra(POSITION, position);
        search = getIntent().getBooleanExtra(SEARCH, false);
        searchOth = getIntent().getBooleanExtra(SEARCHOTH, false);

        aString = Integer.toString(position);
        binding.imgShare.setOnClickListener(this);
        binding.imgDown.setOnClickListener(this);
        binding.checkLike.setOnClickListener(this);
        downloadImage(image);

        downloadArrayList = new ArrayList<Download>();
        SharedPreferences sp = getSharedPreferences("pass", MODE_PRIVATE);
        if (sp.contains("passVerify")) {
            mobileNumber = sp.getString("mobileNo", "");
        }

        SharedPreferences spLogin = getSharedPreferences("login", MODE_PRIVATE);
        if (spLogin.contains("mobile")) {
            mobileNumber = spLogin.getString("mobile", "");
        }

        if (search) {
            reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://wallpaper-19f8f-default-rtdb.firebaseio.com/");
            reference.child("likes").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.child(mobileNumber).hasChild(aString)) {

                        final Long favImage = snapshot.child(mobileNumber).child(aString).child("position").getValue(Long.class);
                        Log.e("FavImage", "onDataChange: " + favImage);
                        if (favImage == position) {
                            binding.checkLike.setChecked(true);
//                        Toast.makeText(WallPaperImageActivity.this, "likeImage", Toast.LENGTH_SHORT).show();
                        } else {
                            binding.checkLike.setChecked(false);
//                        Toast.makeText(WallPaperImageActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                        }
                    } else {
//                    Toast.makeText(WallPaperImageActivity.this, "Wrong mobile number   ", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        } else {

            reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://wallpaper-19f8f-default-rtdb.firebaseio.com/");
            reference.child("searchlikes").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.child(mobileNumber).hasChild(aString)) {

                        final Long favImage = snapshot.child(mobileNumber).child(aString).child("position").getValue(Long.class);
                        Log.e("FavImage", "onDataChange: " + favImage);
                        if (favImage == position) {
                            binding.checkLike.setChecked(true);
                        } else {
                            binding.checkLike.setChecked(false);
                        }
                    } else {
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        if (searchOth) {
            reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://wallpaper-19f8f-default-rtdb.firebaseio.com/");
            reference.child("searchlikesoth").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    if (snapshot.child(mobileNumber).hasChild(aString)) {

                        final Long favImage = snapshot.child(mobileNumber).child(aString).child("position").getValue(Long.class);
                        Log.e("FavImage", "onDataChange: " + favImage);
                        if (favImage == position) {
                            binding.checkLike.setChecked(true);
                        } else {
                            binding.checkLike.setChecked(false);
                        }
                    } else {
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        Log.e("Position", "onCreate: " + position);
//        Dali.create(this).load().blurRadius(20)
//                .downScale(2).concurrent().reScale().skipCache().into(binding.imageView);
        Glide.with(this)
                .asBitmap()
                .load(image)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        BlurImage.with(getApplicationContext()).load(resource).intensity(25).Async(true).into(binding.imageView);
//                        Toast.makeText(WallPaperImageActivity.this, resource.toString(), Toast.LENGTH_SHORT).show();
                        finalBitmap = resource;
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });
        String img = "," + image;
        Log.e("Good", "onCreate: " + image);
        String base64Image = img.split(",")[1];
        Log.e("Good", "onCreate: " + base64Image);
        Log.e("FUll", "onCreate: " + position);
        imgFullImage = findViewById(R.id.imgFullImage);
        root = findViewById(R.id.wallPaper);
        Glide.with(getApplicationContext()).load(image).diskCacheStrategy(DiskCacheStrategy.ALL).into(imgFullImage);
        binding.cardViewWallPaper.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cardViewWallPaper:
                startActivity(new Intent(WallPaperImageActivity.this, WallPaperFullScreenActivity.class).putExtra(IMAGES, image).putExtra(POSITION, position));
                break;
            case R.id.imgShare:
                BitmapDrawable bitmapDrawable = (BitmapDrawable) binding.imgFullImage.getDrawable();
                Bitmap bitmap = bitmapDrawable.getBitmap();
                shareImageFromBitmap(bitmap);
//                shareImageandText(bitmap);
                break;
            case R.id.imgDown:
                showBottomSheetDialog();
                break;
            case R.id.checkLike:

                if (search) {
//                    Toast.makeText(this, "true", Toast.LENGTH_SHORT).show();
                    if (binding.checkLike.isChecked()) {
                        reference.child("likes").child(mobileNumber).child(aString).child("position").setValue(position);
                        reference.child("likes").child(mobileNumber).child(aString).child("image").setValue(image);
//                    Toast.makeText(this, "like", Toast.LENGTH_SHORT).show();
                    } else {
                        reference.child("likes").child(mobileNumber).child(aString).child("position").removeValue();
                        reference.child("likes").child(mobileNumber).child(aString).child("image").removeValue();
//                    Toast.makeText(this, "Decelike", Toast.LENGTH_SHORT).show();
                    }
                } else {
//                    Toast.makeText(this, "false", Toast.LENGTH_SHORT).show();
                    if (binding.checkLike.isChecked()) {
                        reference.child("searchlikes").child(mobileNumber).child(aString).child("position").setValue(position);
                        reference.child("searchlikes").child(mobileNumber).child(aString).child("image").setValue(image);
//                    Toast.makeText(this, "like", Toast.LENGTH_SHORT).show();
                    } else {
                        reference.child("searchlikes").child(mobileNumber).child(aString).child("position").removeValue();
                        reference.child("searchlikes").child(mobileNumber).child(aString).child("image").removeValue();
//                    Toast.makeText(this, "Decelike", Toast.LENGTH_SHORT).show();
                    }

                }


                if (searchOth) {
                    if (binding.checkLike.isChecked()) {
                        reference.child("searchlikesoth").child(mobileNumber).child(aString).child("position").setValue(position);
                        reference.child("searchlikesoth").child(mobileNumber).child(aString).child("image").setValue(image);
//                    Toast.makeText(this, "like", Toast.LENGTH_SHORT).show();
                    } else {
                        reference.child("searchlikesoth").child(mobileNumber).child(aString).child("position").removeValue();
                        reference.child("searchlikesoth").child(mobileNumber).child(aString).child("image").removeValue();
//                    Toast.makeText(this, "Decelike", Toast.LENGTH_SHORT).show();
                    }

                }
                break;
        }
    }

    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_wp_dialog);

        RelativeLayout setWp = bottomSheetDialog.findViewById(R.id.setWp);
        RelativeLayout setLockScreen = bottomSheetDialog.findViewById(R.id.setLockScreen);
        RelativeLayout setBoth = bottomSheetDialog.findViewById(R.id.setBoth);
        RelativeLayout setDownload = bottomSheetDialog.findViewById(R.id.setDownload);
        progressBar = bottomSheetDialog.findViewById(R.id.progressBar);
        linearBottomSheetDialog = bottomSheetDialog.findViewById(R.id.linearBottomSheetDialog);

        setDownload.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            linearBottomSheetDialog.setVisibility(View.INVISIBLE);
            verifyPermissions();
        });

        setWp.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            linearBottomSheetDialog.setVisibility(View.INVISIBLE);

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    setWallpaper();
                }
            }, 1000);

        });

        setLockScreen.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            linearBottomSheetDialog.setVisibility(View.INVISIBLE);
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    setLockWallpaper();
                }
            }, 1000);
//            Toast.makeText(WallPaperImageActivity.this, "Wallpaper set lock successfully!", Toast.LENGTH_SHORT).show();
        });

        setBoth.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            linearBottomSheetDialog.setVisibility(View.INVISIBLE);
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    setBothScreenWallpaper();
                }
            }, 1000);
        });

        bottomSheetDialog.show();
    }

    public void setWallpaper() {
        Bitmap icon = ((BitmapDrawable) binding.imgFullImage.getDrawable()).getBitmap();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "wallpaper.jpg");
        try {
            f.createNewFile();
            new FileOutputStream(f).write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Toast.makeText(getApplicationContext(), "Wallpaper Set", Toast.LENGTH_SHORT).show();
        setCustomToast(getString(R.string.wallpaper_set),WallPaperImageActivity.this);
        progressBar.setVisibility(View.GONE);
        linearBottomSheetDialog.setVisibility(View.VISIBLE);
        new Thread() {
            public void run() {
                DisplayMetrics metrics = new DisplayMetrics();
                WallPaperImageActivity.this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int height = metrics.heightPixels;
                int width = metrics.widthPixels;
                Bitmap bitmap = Bitmap.createScaledBitmap(icon, width, height, true);
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(WallPaperImageActivity.this);
                wallpaperManager.setWallpaperOffsetSteps(1.0f, 1.0f);
                wallpaperManager.suggestDesiredDimensions(width, height);
                try {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM);
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }
//                WallPaperImageActivity.this.handler.post(new Runnable() {
//                    public void run() {
//                    }
//                });
            }
        }.start();
    }

    public void setLockWallpaper() {
        Bitmap icon = ((BitmapDrawable) binding.imgFullImage.getDrawable()).getBitmap();
//        Toast.makeText(getApplicationContext(), "Wallpaper Set", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
        linearBottomSheetDialog.setVisibility(View.VISIBLE);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "wallpaper.jpg");
        try {
            f.createNewFile();
            new FileOutputStream(f).write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Toast.makeText(getApplicationContext(), "Wallpaper Lock Screen", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.GONE);
        linearBottomSheetDialog.setVisibility(View.VISIBLE);
        new Thread() {
            public void run() {
                DisplayMetrics metrics = new DisplayMetrics();
                WallPaperImageActivity.this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int height = metrics.heightPixels;
                int width = metrics.widthPixels;
                Bitmap bitmap = Bitmap.createScaledBitmap(icon, width, height, true);
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(WallPaperImageActivity.this);
                wallpaperManager.setWallpaperOffsetSteps(1.0f, 1.0f);
                wallpaperManager.suggestDesiredDimensions(width, height);
                try {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
//                WallPaperImageActivity.this.handler.post(new Runnable() {
//                    public void run() {
//                    }
//                });
            }
        }.start();

//        Toast.makeText(getApplicationContext(), "Wallpaper Lock Screen Set", Toast.LENGTH_SHORT).show();
        setCustomToast(getString(R.string.wallpaper_set),WallPaperImageActivity.this);
    }

    public void setBothScreenWallpaper() {
        Bitmap icon = ((BitmapDrawable) binding.imgFullImage.getDrawable()).getBitmap();
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        icon.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File f = new File(Environment.getExternalStorageDirectory() + File.separator + "wallpaper.jpg");
        try {
            f.createNewFile();
            new FileOutputStream(f).write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Toast.makeText(getApplicationContext(), "Wallpaper Set Both Screen", Toast.LENGTH_SHORT).show();
        setCustomToast(getString(R.string.wallpaper_set),WallPaperImageActivity.this);
        progressBar.setVisibility(View.GONE);
        linearBottomSheetDialog.setVisibility(View.VISIBLE);
        new Thread() {
            public void run() {
                DisplayMetrics metrics = new DisplayMetrics();
                WallPaperImageActivity.this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                int height = metrics.heightPixels;
                int width = metrics.widthPixels;
                Bitmap bitmap = Bitmap.createScaledBitmap(icon, width, height, true);
                WallpaperManager wallpaperManager = WallpaperManager.getInstance(WallPaperImageActivity.this);
                wallpaperManager.setWallpaperOffsetSteps(1.0f, 1.0f);
                wallpaperManager.suggestDesiredDimensions(width, height);
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM | WallpaperManager.FLAG_LOCK);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


    private void shareImageFromBitmap(Bitmap bmp) {
        Uri uri = getUriImageFromBitmap(bmp, WallPaperImageActivity.this);
        if(uri == null) {
            //Show no URI message
            return;
        }

        final Intent shareIntent = new Intent(Intent.ACTION_SEND);

        shareIntent.putExtra(Intent.EXTRA_TEXT, image);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/png");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        startActivity(Intent.createChooser(shareIntent, "Share image using"));
    }

    private Uri getUriImageFromBitmap(Bitmap bmp, Context context) {
        if(bmp == null)
            return null;

        Uri bmpUri = null;

        try {

            File file =  new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "IMG_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();

//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            bmpUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
//            else
//                bmpUri = Uri.fromFile(file);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

//    private void shareImageandText(Bitmap bitmap) {
//        Uri uri = getmageToShare(bitmap);
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        // putting uri of image to be shared
//        intent.putExtra(Intent.EXTRA_STREAM, uri);
//        // adding text to share
//        intent.putExtra(Intent.EXTRA_TEXT, "Sharing Image");
//        // Add subject Here
//        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject Here");
//        // setting type to image
//        intent.setType("image/png");
//        // calling startactivity() to share
//        startActivity(Intent.createChooser(intent, "Share With"));
//    }

    // Retrieving the url to share
//    private Uri getmageToShare(Bitmap bitmap) {
//        File imagefolder = new File(getCacheDir(), "images");
//        Uri uri = null;
//        try {
//            imagefolder.mkdirs();
//            File file = new File(imagefolder, "shared_image.png");
//            FileOutputStream outputStream = new FileOutputStream(file);
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
//            outputStream.flush();
//            outputStream.close();
//            uri = FileProvider.getUriForFile(this, "com.hdlight.wallpaperapps", file);
//        } catch (Exception e) {
//            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
//        }
//        return uri;
//    }

    //Check Permission image storage
    public Boolean verifyPermissions() {
        // This will return the current Status
        int permissionExternalMemory = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionExternalMemory != PackageManager.PERMISSION_GRANTED) {

            String[] STORAGE_PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
            // If permission not granted then ask for permission real time.
            ActivityCompat.requestPermissions(this, STORAGE_PERMISSIONS, 5000);

//            saveImage(bitmap, image);
//            Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
            return false;
        } else {
//            Toast.makeText(this, "Jemis", Toast.LENGTH_SHORT).show();
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    saveImage(bitmap, image);
                }
            }, 1000);
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 5000:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Granted.
                    saveImage(bitmap, image);
//                    Toast.makeText(this, "Granted Successfully", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    linearBottomSheetDialog.setVisibility(View.VISIBLE);

                } else {
                    //Denied.
//                    Toast.makeText(this, "denied click", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    linearBottomSheetDialog.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    public void downloadImage(String imageURL) {

        String dirPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getString(R.string.app_name) + "/";
//        final File dir = new File(dirPath);
//        final String fileName = imageURL.substring(imageURL.lastIndexOf('/') + 1);
        Glide.with(this)
                .load(imageURL)
                .into(new CustomTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @com.google.firebase.database.annotations.Nullable Transition<? super Drawable> transition) {

                        Bitmap bitmap = ((BitmapDrawable) resource).getBitmap();
                        WallPaperImageActivity.this.bitmap = bitmap;
//                        Toast.makeText(DownloadWallPaperActivity.this, "Saving Image...", Toast.LENGTH_SHORT).show();
                        Log.e("WPImageActivity", "onResourceReady: ");
                    }

                    @Override
                    public void onLoadCleared(@com.google.firebase.database.annotations.Nullable Drawable placeholder) {
                    }

                    @Override
                    public void onLoadFailed(@com.google.firebase.database.annotations.Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
//                        Toast.makeText(DownloadWallPaperActivity.this, "Failed to Download Image! Please try again later.", Toast.LENGTH_SHORT).show();
                        Log.e("WPImageActivity", "onLoadFailed: ");
                    }
                });

    }

    //Save image in Phone Albums
    private String saveImage(Bitmap images, String imageURL) {
        String savedImagePath = null;

        final String fileName = imageURL.substring(imageURL.lastIndexOf('/') + 1);
        String imageFileName = "JPEG_" + fileName + ".jpg";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + "/WallPaper");
//        boolean success = true;
        File imageFile = new File(storageDir, imageFileName);

        if (imageFile.exists()) {
//            Toast.makeText(this, "already download", Toast.LENGTH_SHORT).show();
            setCustomToast(getString(R.string.already_download),WallPaperImageActivity.this);
            progressBar.setVisibility(View.GONE);
            linearBottomSheetDialog.setVisibility(View.VISIBLE);
        } else {
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                images.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Add the image to the system gallery
            galleryAddPic(savedImagePath);
            downloadArrayList.add(new Download(image));
//            SharedPreferences sharedPreferences = getSharedPreferences("download preferences", MODE_PRIVATE);
//            String save = sharedPreferences.getString("download", "");
//            Gson gson = new Gson();
//            ArrayList<Save> saveArrayList;
//            saveArrayList = gson.fromJson(save, new TypeToken<ArrayList<Save>>() {
//            }.getType());

            DataBaseHelper dataBaseHelper = DataBaseHelper.getDb(this);
            ArrayList<Position> positionArrayList = (ArrayList<Position>) dataBaseHelper.positionDAO().getAllPosition();

            boolean match = false;
            for (int i = 0; i < positionArrayList.size(); i++) {
                Log.e("PositionElse", "saveImage: ");
                Log.e("PositionLoop", "saveImage: " + positionArrayList.get(i).getPosition());
                if (positionArrayList.get(i).getPosition() == position) {
//                    Toast.makeText(this, "match", Toast.LENGTH_SHORT).show();
                    match = true;
                }
            }
            if (match == false) {
                saveImageDownloadData();
            }
//            Toast.makeText(WallPaperImageActivity.this, "Download Successfully", Toast.LENGTH_LONG).show();
            setCustomToast(getString(R.string.download_successfully),WallPaperImageActivity.this);
            progressBar.setVisibility(View.GONE);
            linearBottomSheetDialog.setVisibility(View.VISIBLE);
        }

        return savedImagePath;
    }

    private void galleryAddPic(String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

    //Save image in SharedPreferences
    private void saveImageDownloadData() {
        SharedPreferences sharedPreferences = getSharedPreferences("download preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String old = sharedPreferences.getString("download", null);
        Gson gson = new Gson();
        if (old == null) {
            String json = gson.toJson(downloadArrayList);
            Log.e("true", "saveImageDownloadData: " + json);
            editor.putString("download", json);
        } else {
            ArrayList<Download> list;
            list = gson.fromJson(old, new TypeToken<ArrayList<Download>>() {
            }.getType());
//            map.put(position, position);
//            Log.e("Hash", "saveImageDownloadData: " + map.get(position));
            for (int i = 0; i < downloadArrayList.size(); i++) {
                Log.e("Download", "saveImageDownloadData: " + downloadArrayList.get(i).getImage());
                list.add(downloadArrayList.get(i));
            }
//            Set<String> keys = map.keySet();
//            for (String key : keys){
//                map.get(key);
//            }
            String json = gson.toJson(list);
            editor.putString("download", json);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            editor.apply();
        }

        DataBaseHelper dataBaseHelper = DataBaseHelper.getDb(this);

        dataBaseHelper.positionDAO().addPos(new Position(position));
//        Toast.makeText(this, "Saved Array List to Shared preferences. ", Toast.LENGTH_SHORT).show();
    }

}