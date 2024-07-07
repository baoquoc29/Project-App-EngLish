package com.example.testaudioenglish.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testaudioenglish.Entity.TopicFlashCardEntity;
import com.example.testaudioenglish.OnItemClickListener;
import com.example.testaudioenglish.R;
import com.example.testaudioenglish.Repository.TopicFlashCardRepository;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TopicFlashCardAdapter extends RecyclerView.Adapter<TopicFlashCardAdapter.FlashCardViewHolder> {

    private List<TopicFlashCardEntity> flashCardList;
    private TopicFlashCardRepository repository;
    private OnItemClickListener onItemClickListener;

    public TopicFlashCardAdapter(List<TopicFlashCardEntity> flashCardList, TopicFlashCardRepository repository, OnItemClickListener onItemClickListener) {
        this.flashCardList = flashCardList;
        this.repository = repository;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public FlashCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customflashcard, parent, false);
        return new FlashCardViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashCardViewHolder holder, int position) {
        TopicFlashCardEntity flashCard = flashCardList.get(position);
        holder.dateCreate.setText(flashCard.getDate());
        holder.exerciseDay.setText(flashCard.getTitle());

        LiveData<Long> termCountLiveData = repository.getCount(flashCard.getId());
        termCountLiveData.observeForever(new Observer<Long>() {
            @Override
            public void onChanged(Long termCount) {
                holder.termCount.setText(String.valueOf(termCount + " thuật ngữ"));
                termCountLiveData.removeObserver(this);
            }
        });

        holder.username.setText(flashCard.getUsername());

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return flashCardList.size();
    }

    public static class FlashCardViewHolder extends RecyclerView.ViewHolder {
        public TextView dateCreate;
        public TextView exerciseDay;
        public TextView termCount;
        public TextView username;
        public CircleImageView profileImage;

        public FlashCardViewHolder(View view) {
            super(view);
            dateCreate = view.findViewById(R.id.dateCreate);
            exerciseDay = view.findViewById(R.id.tv_exercise_day);
            termCount = view.findViewById(R.id.tv_term_count);
            username = view.findViewById(R.id.tv_username);
            profileImage = view.findViewById(R.id.profile_image);
        }
    }
}
