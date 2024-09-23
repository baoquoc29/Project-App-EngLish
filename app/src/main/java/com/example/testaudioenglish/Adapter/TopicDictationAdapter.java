package com.example.testaudioenglish.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testaudioenglish.InterfaceAdapter.OnItemClickListener;
import com.example.testaudioenglish.Model.TopicDictationModel;
import com.example.testaudioenglish.R;

import java.util.List;

public class TopicDictationAdapter extends RecyclerView.Adapter<TopicDictationAdapter.ViewHolder> {

    private List<TopicDictationModel> topicList;
    private OnItemClickListener onItemClickListener;
    public TopicDictationAdapter(List<TopicDictationModel> topicList,OnItemClickListener onItemClickListener) {
        this.topicList = topicList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customdictation, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TopicDictationModel topic = topicList.get(position);
        holder.nameTopic.setText(topic.getName());
        holder.totalComplete.setText("Số câu hỏi: " + topic.getTotalQuestions());
        holder.difficulty.setText("Mức độ khó: " + topic.getDifficulty());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return topicList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nameTopic;
        public TextView totalComplete;
        public TextView difficulty;

        public ViewHolder(View itemView) {
            super(itemView);
            nameTopic = itemView.findViewById(R.id.nameTopic);
            totalComplete = itemView.findViewById(R.id.totalComplete);
            difficulty = itemView.findViewById(R.id.difficulty);
        }
    }
}
