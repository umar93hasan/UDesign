package com.numad19f.udesign;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostsViewHolder> {

    private Context mContext;
    private ArrayList<PostModel> postsList;

    public PostsAdapter(Context mContext, ArrayList<PostModel> postsList) {
        this.mContext = mContext;
        this.postsList = postsList;
    }

    @NonNull
    @Override
    public PostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.post_item, parent, false);
        return new PostsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PostsViewHolder holder, int position) {
        PostModel currentItem = postsList.get(position);

        String username = "~" + currentItem.getUsername();
        String title = currentItem.getTitle();
        String content = currentItem.getBody();

        holder.mPostTitle.setText(title);
        holder.mPostContent.setText(content);
        holder.mPostUser.setText(username);

    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    public class PostsViewHolder extends RecyclerView.ViewHolder{
        public TextView mPostTitle;
        public TextView mPostContent;
        public TextView mPostUser;
        public PostsViewHolder(@NonNull View itemView) {
            super(itemView);
            mPostTitle = itemView.findViewById(R.id.post_title);
            mPostContent = itemView.findViewById(R.id.post_content);
            mPostUser = itemView.findViewById(R.id.post_by);
        }
    }

}
