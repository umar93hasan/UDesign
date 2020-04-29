package com.numad19f.udesign;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TopLikedAdapter extends RecyclerView.Adapter<TopLikedAdapter.TopLikedViewHolder> {

    private Context mContext;
    private ArrayList<ImageModel> topImageList;

    public TopLikedAdapter(Context mContext, ArrayList<ImageModel> topImageList) {
        this.mContext = mContext;
        this.topImageList = topImageList;
    }

    @NonNull
    @Override
    public TopLikedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.top_liked_item, parent, false);
        return new TopLikedViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull TopLikedViewHolder holder, int position) {
        ImageModel currentItem = topImageList.get(position);

        //byte[] data = Base64.decode(currentItem.getImage(), Base64.DEFAULT);
        //Bitmap image = BitmapFactory.decodeByteArray(data, 0, data.length);
        String username = currentItem.getUsername();
        int likeCount = currentItem.getLikecount();

        holder.mTextViewUser.setText(username);
        holder.mTextViewLikes.setText("Likes: " + likeCount);
        //holder.mImageView.setImageBitmap(image);
        Picasso.with(mContext)
                .load(topImageList.get(position).getImage())
                .fit()
                .centerCrop()
                .into(holder.mImageView);

        Drawable badge = null;

        switch (position){
            case 0: badge = holder.mBadge.getContext().getResources().getDrawable(R.drawable.top1);
                break;
            case 1: badge = holder.mBadge.getContext().getResources().getDrawable(R.drawable.top2);
                break;
            default:    badge = holder.mBadge.getContext().getResources().getDrawable(R.drawable.top3);
                break;
        }

        holder.mBadge.setImageDrawable(badge);
    }

    @Override
    public int getItemCount() {
        return topImageList.size();
    }

    public class TopLikedViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageView;
        public TextView mTextViewUser;
        public TextView mTextViewLikes;
        public ImageView mBadge;
        public TopLikedViewHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.top_image_view);
            mTextViewUser = itemView.findViewById(R.id.top_image_user);
            mTextViewLikes = itemView.findViewById(R.id.top_image_likes);
            mBadge = itemView.findViewById(R.id.star_badge);
        }
    }
}
