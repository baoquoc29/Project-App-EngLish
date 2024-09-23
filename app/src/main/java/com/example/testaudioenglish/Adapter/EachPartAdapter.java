package com.example.testaudioenglish.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testaudioenglish.InterfaceAdapter.OnItemClickListener;
import com.example.testaudioenglish.Model.EachPartModel;
import com.example.testaudioenglish.R;
import java.util.List;

public class EachPartAdapter extends RecyclerView.Adapter<EachPartAdapter.ItemViewHolder> {
    private List<EachPartModel> itemList;
    private OnItemClickListener listener;

    public EachPartAdapter(List<EachPartModel> itemList, OnItemClickListener listener) {
        this.itemList = itemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_exam_part, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        EachPartModel item = itemList.get(position);
        holder.titleTextView.setText(item.getTitle());
        if(item.getNamePart().equals("Bài nghe")){
            holder.imageView.setImageResource(R.drawable.listening2);
        } else {
            holder.imageView.setImageResource(R.drawable.reading2);
        }

        holder.listeningTextView.setText(item.getNamePart());
        holder.countTextView.setText("Số lượng bài: " + item.getQuantity());

        // Handle item click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    // Define ViewHolder as a static inner class
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public ImageView imageView;
        public TextView listeningTextView;
        public TextView countTextView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            imageView = itemView.findViewById(R.id.imageView);
            listeningTextView = itemView.findViewById(R.id.listeningTextView);
            countTextView = itemView.findViewById(R.id.countTextView);
        }
    }
}
