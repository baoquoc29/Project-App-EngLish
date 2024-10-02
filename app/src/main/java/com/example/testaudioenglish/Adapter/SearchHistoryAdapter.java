package com.example.testaudioenglish.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testaudioenglish.InterfaceAdapter.OnItemClickListener;
import com.example.testaudioenglish.InterfaceAdapter.OnItemMoveClicked;
import com.example.testaudioenglish.Model.HistoryModel;
import com.example.testaudioenglish.R;

import java.util.List;

public class SearchHistoryAdapter extends RecyclerView.Adapter<SearchHistoryAdapter.MyViewHolder> {

    private List<HistoryModel> items;
    private OnItemClickListener onItemClickListener;
    private OnItemMoveClicked onItemMoveClicked;
    public SearchHistoryAdapter(List<HistoryModel> items, OnItemClickListener onItemClickListener,OnItemMoveClicked onItemMoveClicked) {
        this.items = items;
        this.onItemMoveClicked = onItemMoveClicked;
        this.onItemClickListener = onItemClickListener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customhistorysearch, parent, false); // `item_layout.xml` là layout từng item
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        HistoryModel item = items.get(position);
        holder.textViewTitle.setText(item.getWord());
        holder.textPronunciation.setText(item.getPronunciation());
        holder.textMeaning.setText(item.getMeaning());
        holder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(position);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemMoveClicked.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTitle;
        TextView textPronunciation;
        TextView textMeaning;
        ImageView close;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textTitle); // Tìm TextView theo id
            textPronunciation = itemView.findViewById(R.id.textPronunciation);
            textMeaning = itemView.findViewById(R.id.textMeaning);
            close = itemView.findViewById(R.id.imageClose);
        }
    }
}
