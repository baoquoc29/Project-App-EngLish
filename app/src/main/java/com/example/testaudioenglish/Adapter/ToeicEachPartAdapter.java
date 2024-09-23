package com.example.testaudioenglish.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.testaudioenglish.InterfaceAdapter.OnItemClickListener;
import com.example.testaudioenglish.Model.ToeicModel.TopicToeicModel;
import com.example.testaudioenglish.R;

import java.util.List;

public class ToeicEachPartAdapter extends RecyclerView.Adapter<ToeicEachPartAdapter.ViewHolder> {

    private List<TopicToeicModel> itemList;
    private Context context;
    private OnItemClickListener onItemClickListener;
    public ToeicEachPartAdapter(Context context, List<TopicToeicModel> itemList,OnItemClickListener onItemClickListener) {
        this.context = context;
        this.onItemClickListener = onItemClickListener;
        this.itemList = itemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_layout_toeic_any_part, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TopicToeicModel item = itemList.get(position);
        holder.title.setText(item.getTitle());
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView subtitle;
        public TextView difficulty;
        public TextView totalComplete;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
        }
    }
}
