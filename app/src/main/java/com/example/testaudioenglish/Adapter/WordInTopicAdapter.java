package com.example.testaudioenglish.Adapter;

import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testaudioenglish.Entity.FlashCardEntity;
import com.example.testaudioenglish.R;
import com.example.testaudioenglish.viewmodel.LearningViewModel;

import java.util.List;
import java.util.Locale;

public class WordInTopicAdapter extends RecyclerView.Adapter<WordInTopicAdapter.ViewHolder> {

    private List<FlashCardEntity> items;
    private LearningViewModel learningViewModel;
    private TextToSpeech textToSpeech;

    public WordInTopicAdapter(List<FlashCardEntity> items, LearningViewModel learningViewModel) {
        this.items = items;
        this.learningViewModel = learningViewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wordcustom, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FlashCardEntity item = items.get(position);
        holder.engver.setText(item.getEnglishWord());
        holder.vietver.setText(item.getVietWord());

        updateStarIcon(holder.starIcon, item.getTick());

        holder.loundIcon.setOnClickListener(view -> {
            textToSpeech = new TextToSpeech(view.getContext(), i -> {
                if (i == TextToSpeech.SUCCESS) {
                    textToSpeech.setLanguage(Locale.US);
                    textToSpeech.setSpeechRate(1.0f);
                    textToSpeech.speak(item.getEnglishWord(), TextToSpeech.QUEUE_ADD, null);
                }
            });
        });

        holder.starIcon.setOnClickListener(view -> {
            boolean isTicked = item.getTick() == 1;
            if (isTicked) {
                holder.starIcon.setImageResource(R.drawable.star);
                item.setTick(0);
            } else {
                holder.starIcon.setImageResource(R.drawable.startfullfill);
                item.setTick(1);
            }

            learningViewModel.updateTickedStatus(item.getIdTopic(), item.getId(), !isTicked);
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    private void updateStarIcon(ImageView starIcon, int tickStatus) {
        if (tickStatus == 0) {
            starIcon.setImageResource(R.drawable.star);
        } else {
            starIcon.setImageResource(R.drawable.startfullfill);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView engver;
        public TextView vietver;
        public ImageView loundIcon;
        public ImageView starIcon;

        public ViewHolder(View itemView) {
            super(itemView);
            engver = itemView.findViewById(R.id.engver);
            vietver = itemView.findViewById(R.id.vietver);
            loundIcon = itemView.findViewById(R.id.lound_icon);
            starIcon = itemView.findViewById(R.id.star_icon);
        }
    }
}
