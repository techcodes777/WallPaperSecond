package com.hdlight.wallpaperapps.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hdlight.wallpaperapps.R;
import com.hdlight.wallpaperapps.ui.WallPaperImageActivity;

import java.util.ArrayList;

public class SearchOtherAdapter extends RecyclerView.Adapter<SearchOtherAdapter.ViewHolder>{

    private ArrayList<String> list;
    private Context context;
    public static final String IMAGES = "images";
    public static final String POSITION = "position";
    public static final String SEARCHOTH = "searchoth";
    String mobileNumber;

    public SearchOtherAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewpager_adapter, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Glide.with(context)
                .load(list.get(position))
                .placeholder(new ColorDrawable(Color.BLACK))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imageView);

        holder.itemView.setOnClickListener(v -> {
//            Bundle bundle = new Bundle();
//            bundle.putString(POSITION, String.valueOf(position));
            int pos = holder.getAdapterPosition();
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//            byte[] byteArray = stream.toByteArray();
            context.startActivity(new Intent(context, WallPaperImageActivity.class).putExtra(IMAGES, list.get(position)).putExtra(POSITION, pos).putExtra(SEARCHOTH,true));
//            Log.e("WallPaperAdapter", "onBindViewHolder: " + position);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imgItem);

        }
    }
}
