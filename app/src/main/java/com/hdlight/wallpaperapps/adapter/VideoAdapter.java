package com.hdlight.wallpaperapps.adapter;

import android.app.WallpaperManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.VideoView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hdlight.wallpaperapps.R;
import java.util.ArrayList;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder>{

    ArrayList<String> list;
    Context context;
    ProgressBar progressBar;
    LinearLayout linearBottomSheetDialog;

    public VideoAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.video_adapter,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

                String videoUrl = list.get(position);
        holder.videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
            }
        });

        Uri uri = Uri.parse(videoUrl);
        holder.videoView.setVideoURI(uri);
        holder.videoView.requestFocus();
        MediaController mediaController = new MediaController(context,false);
        mediaController.setAnchorView(holder.videoView);
//        mediaController.hide();
        mediaController.setMediaPlayer(holder.videoView);
//        mediaController.setMediaPlayer(videoView);
        holder.videoView.start();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetDialog();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        VideoView videoView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

         videoView   = itemView.findViewById(R.id.videoView);
        }
    }

    private void showBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(context);
        bottomSheetDialog.setContentView(R.layout.video_bottom_sheet_dialog);

        RelativeLayout setLiveWp = bottomSheetDialog.findViewById(R.id.setLiveWp);
        RelativeLayout setVideoDownload = bottomSheetDialog.findViewById(R.id.setVideoDownload);
        progressBar = bottomSheetDialog.findViewById(R.id.progressBar);
        linearBottomSheetDialog = bottomSheetDialog.findViewById(R.id.linearBottomSheetDialog);

        setVideoDownload.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            linearBottomSheetDialog.setVisibility(View.INVISIBLE);
//            verifyPermissions();
        });

        setLiveWp.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            linearBottomSheetDialog.setVisibility(View.INVISIBLE);

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    setLiveWallpaper();
                }
            }, 1000);

        });

        bottomSheetDialog.show();
    }

    private void setLiveWallpaper() {
        Intent intent = new Intent();
        intent.setAction(WallpaperManager.ACTION_LIVE_WALLPAPER_CHOOSER);
        context.startActivity(intent);

        progressBar.setVisibility(View.GONE);
        linearBottomSheetDialog.setVisibility(View.VISIBLE);

    }

}
