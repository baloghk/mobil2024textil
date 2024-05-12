package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class TextilItemAdapter extends RecyclerView.Adapter<TextilItemAdapter.ViewHolder> implements Filterable {

    private ArrayList<TextilItem> textilData = new ArrayList<>();
    private ArrayList<TextilItem> allTextilData = new ArrayList<>();
    private Context tContext;
    private int lastPosition = -1;

    public TextilItemAdapter( Context context, ArrayList<TextilItem> textilData) {
        this.textilData = textilData;
        this.allTextilData = textilData;
        this.tContext = context;
    }

    @NonNull
    @Override
    public TextilItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(tContext).inflate(R.layout.item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TextilItemAdapter.ViewHolder holder, int position) {
        TextilItem currentItem = textilData.get(position);

        holder.bindTo(currentItem);

        if(holder.getAdapterPosition() > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(tContext, R.anim.slide_in_row);
            holder.itemView.startAnimation(animation);
            lastPosition = holder.getAdapterPosition();
        }
    }

    @Override
    public int getItemCount() {
        return textilData.size();
    }

    @Override
    public Filter getFilter() {
        return shopFilter;
    }
    private Filter shopFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<TextilItem> filteredTextil = new ArrayList<>();
            FilterResults results = new FilterResults();

            if(constraint == null || constraint.length() == 0) {
                results.count = allTextilData.size();
                results.values = allTextilData;
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(TextilItem item : allTextilData) {
                    if(item.getName().toLowerCase().contains(filterPattern)){
                        filteredTextil.add(item);
                    }
                }

                results.count = filteredTextil.size();
                results.values = filteredTextil;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            textilData = (ArrayList)results.values;
            notifyDataSetChanged();
        }
    };

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView itemTitle;
        private TextView itemInfo;
        private TextView itemPrice;
        private ImageView itemImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemTitle = itemView.findViewById(R.id.itemTitle);
            itemInfo = itemView.findViewById(R.id.subTitle);
            itemImage = itemView.findViewById(R.id.itemImage);
            itemPrice = itemView.findViewById(R.id.price);
        }

        public void bindTo(TextilItem currentItem) {
            itemTitle.setText(currentItem.getName());
            itemInfo.setText(currentItem.getInfo());
            itemPrice.setText(currentItem.getPrice());

            Glide.with(tContext).load(currentItem.getImageResource()).into(itemImage);
        }
    }
}

