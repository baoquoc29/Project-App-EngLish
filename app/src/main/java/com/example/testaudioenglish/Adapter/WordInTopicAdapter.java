package com.example.testaudioenglish.Adapter;

import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.testaudioenglish.Entity.FlashCardEntity;
import com.example.testaudioenglish.R;

import java.util.List;
import java.util.Locale;

public class WordInTopicAdapter extends RecyclerView.Adapter<WordInTopicAdapter.ViewHolder> {

    private List<FlashCardEntity> items;
    private List<FlashCardEntity> wordList;
    private TextToSpeech textToSpeech;
    public WordInTopicAdapter(List<FlashCardEntity> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.wordcustom, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FlashCardEntity item = items.get(position);
        holder.engver.setText(item.getEnglishWord());
        holder.vietver.setText(item.getVietWord());
        // Set other views' data if needed
        holder.loundIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textToSpeech = new TextToSpeech(view.getContext(), new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int i) {
                        if(i == TextToSpeech.SUCCESS){
                            textToSpeech.setLanguage(Locale.US);
                            textToSpeech.setSpeechRate(1.0f);
                            textToSpeech.speak(item.getEnglishWord().toString(),TextToSpeech.QUEUE_ADD,null);
                        }
                    }
                });
            }
        });
    }
    public void updateWordList(List<FlashCardEntity> newWordList) {
        this.wordList = newWordList;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return items.size();
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
