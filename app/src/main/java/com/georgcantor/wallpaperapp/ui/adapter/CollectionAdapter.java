package com.georgcantor.wallpaperapp.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.georgcantor.wallpaperapp.R;
import com.georgcantor.wallpaperapp.model.Hit;
import com.georgcantor.wallpaperapp.ui.util.WallpViewHolder;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CollectionAdapter extends RecyclerView.Adapter<WallpViewHolder> {

    private List<Hit> hit;
    private Context context;
    public int width;
    public int height;

    public CollectionAdapter(Context context) {
        this.context = context;
        this.hit = new ArrayList<>();
    }

    @NonNull
    @Override
    public WallpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.wallp_item, null);
        final WallpViewHolder rcv = new WallpViewHolder(itemView);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = rcv.getAdapterPosition();
                Intent intent = new Intent(context, PicDetail.class);
                intent.putExtra(PicDetail.EXTRA_PIC, hit.get(position));
                intent.putExtra(PicDetail.origin, R.string.collection_string);
                context.startActivity(intent);
            }
        });

        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull WallpViewHolder holder, int position) {
        Hit photo = this.hit.get(position);
        RelativeLayout.LayoutParams rlp =
                (RelativeLayout.LayoutParams) holder.discWallp.getLayoutParams();
        float height = photo.getPreviewHeight();
        float width = photo.getPreviewWidth();
        float ratio = height / width;
        rlp.height = (int) (rlp.width * ratio);
        holder.discWallp.setLayoutParams(rlp);
        holder.discWallp.setRatio(ratio);
        File file = new File(Environment.getExternalStoragePublicDirectory("/" + R.string.app_name),
                photo.getId() + context.getResources().getString(R.string.jpg));
        Picasso.with(context)
                .load(file)
                .placeholder(R.drawable.plh)
                .into(holder.discWallp);
    }

    @Override
    public int getItemCount() {
        return (hit == null) ? 0 : hit.size();
    }

    public void setHitList(List<Hit> hits) {
        if (hits != null) {
            this.hit.addAll(hits);
            notifyDataSetChanged();
        }
    }
}
