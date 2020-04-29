package com.numad19f.udesign;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.CategoryViewHolder>{
    List<CategoryItem> items;
    String userName;

    RVAdapter(List<CategoryItem> items, String userName){
        this.items = items;
        this.userName=userName;
    }
    public class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView cv;
        TextView categoryName;
        ImageView categoryPhoto;
        private final Context context;

        CategoryViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            cv = itemView.findViewById(R.id.cv);
            categoryName = itemView.findViewById(R.id.category_name);
            categoryPhoto = itemView.findViewById(R.id.category_photo);
            cv.setOnClickListener(this);

        }
        @Override
        public void onClick(View v) {
            //for find item that hold in list


            final Intent intent;
            intent =  new Intent(context, MenuActivity.class);
            intent.putExtra("userName",RVAdapter.this.userName);
            switch (getAdapterPosition()){

                case 0:
                    intent.putExtra("imageType", "Bedroom Designs");
                    break;

                case 1:
                    intent.putExtra("imageType", "Living Room Designs");
                    break;
                case 2:
                    intent.putExtra("imageType", "Kitchen Designs");
                    break;

                case 3:
                    intent.putExtra("imageType", "Kids Room Designs");
                    break;
                case 4:
                    intent.putExtra("imageType", "Patio Designs");
                    break;
                case 5:
                    intent.putExtra("imageType", "Garden Designs");
                    break;
                default:
                    break;
            }
            context.startActivity(intent);
        }
    }
    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        CategoryViewHolder pvh = new CategoryViewHolder(v);
        return pvh;
    }
    @Override
    public void onBindViewHolder(CategoryViewHolder categoryViewHolder, int i) {
        categoryViewHolder.categoryName.setText(items.get(i).categoryName);
        categoryViewHolder.categoryPhoto.setImageResource(items.get(i).photoId);
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}