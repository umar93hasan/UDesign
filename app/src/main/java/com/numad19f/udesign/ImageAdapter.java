package com.numad19f.udesign;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ViewImageModel> imageModelArrayList;
    private ViewImageModel image = null;

    public ImageAdapter(Context context, ArrayList<ViewImageModel> imageModelArrayList) {
        this.context = context;
        this.imageModelArrayList = imageModelArrayList;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    @Override
    public int getCount() {
        return imageModelArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageModelArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.image_listview_item, null, true);
            holder.iv = convertView.findViewById(R.id.imgView);
            holder.tvlikes = convertView.findViewById(R.id.likes);
            holder.likeBtn = convertView.findViewById(R.id.star_button);
            convertView.setTag(holder);
        } else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder) convertView.getTag();
        }

        Picasso.with(context)
                .load(imageModelArrayList.get(position).getImage())
                .fit()
                .centerCrop()
                .into(holder.iv);
        holder.likeBtn.setLiked(imageModelArrayList.get(position).isUserLiked());
        holder.likeBtn.setOnLikeListener(new OnLikeListener() {

            @Override
            public void liked(LikeButton likeButton) {
                likeButton.setLiked(true);
                ViewImageModel imgModel = imageModelArrayList.get(position);
                imgModel.setLikeCount(imgModel.getLikeCount() + 1);
                imgModel.setUserLiked(true);
                imageModelArrayList.set(position, imgModel);
                System.out.println("image id unlike"+ imgModel.getId());
                System.out.println("image username"+imgModel.getUserName());
                notifyDataSetChanged();
                ImagePutRequest req = new ImagePutRequest(context);
                try {
                    req.imageUpdate(imgModel.getId(), imgModel.getImage(), imgModel.getImageType(), imgModel.getUserName(), imgModel.getLikeCount());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                LikePostRequest likePost = new LikePostRequest(context);
                try {
                    SharedPreferences sp = context.getSharedPreferences("Login",Context.MODE_PRIVATE);
                    String user = sp.getString("username", null);
                    likePost.likeSave(imgModel.getId(), user);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                notifyDataSetChanged();
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                likeButton.setLiked(false);
                ViewImageModel imgModel = imageModelArrayList.get(position);
                System.out.println("image id unlike"+ imgModel.getId());
                System.out.println("image username"+imgModel.getUserName());
                imgModel.setLikeCount(imgModel.getLikeCount() - 1);
                imgModel.setUserLiked(false);
                imageModelArrayList.set(position, imgModel);
                notifyDataSetChanged();
//                ImagePutRequest req = new ImagePutRequest(context);
//                try {
//                    req.imageUpdate(imgModel.getId(), imageModelArrayList.get(position).getImage(), imgModel.getImageType(), imgModel.getUserName(), imgModel.getLikeCount());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                notifyDataSetChanged();
                LikeDeleteRequest likePost = new LikeDeleteRequest(context);
                try {
                    likePost.likeDelete(imgModel.getId(),imgModel.getUserName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                notifyDataSetChanged();
            }
        });

        holder.tvlikes.setText(imageModelArrayList.get(position).getLikeCount() + " likes");
        return convertView;
    }

    private class ViewHolder {

        private ImageView iv;
        private TextView tvlikes;
        private LikeButton likeBtn;

    }


}
