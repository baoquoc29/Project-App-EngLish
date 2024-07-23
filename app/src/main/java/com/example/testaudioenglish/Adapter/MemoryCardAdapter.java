package com.example.testaudioenglish.Adapter;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testaudioenglish.Entity.FlashCardEntity;
import com.example.testaudioenglish.R;
import com.example.testaudioenglish.viewmodel.MemoryCardViewModel;

import java.util.List;
import java.util.Locale;

public class MemoryCardAdapter extends RecyclerView.Adapter<MemoryCardAdapter.ViewHolder> {

    private List<FlashCardEntity> dataSet;
    private Context context;  // Add context
    private TextToSpeech textToSpeech;
    private MemoryCardViewModel memoryCardViewModel;
    // ViewHolder class to hold individual items
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewEngVer;
        public TextView textViewVietVer;
        public CardView cardView;  // Add CardView reference
        public ImageView speak;
        public ImageView star;
        public TextView getTextViewVietVer() {
            return textViewVietVer;
        }

        public void setTextViewVietVer(TextView textViewVietVer) {
            this.textViewVietVer = textViewVietVer;
        }

        public ViewHolder(View view) {
            super(view);
            textViewEngVer = view.findViewById(R.id.engMemoryVer);
            textViewVietVer = view.findViewById(R.id.vietMemoryVer);
            cardView = view.findViewById(R.id.memoryCard);
            speak = view.findViewById(R.id.speak);
            star = view.findViewById(R.id.star);// Initialize CardView
        }
    }

    // Adapter constructor with context
    public MemoryCardAdapter(List<FlashCardEntity> dataSet, Context context, MemoryCardViewModel memoryCardViewModel) {
        this.dataSet = dataSet;
        this.context = context;
        this.memoryCardViewModel = memoryCardViewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the custom layout for each item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customemory, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind the data to the views
        FlashCardEntity data = dataSet.get(position);
        holder.textViewEngVer.setText(data.getEnglishWord());
        holder.textViewVietVer.setText(data.getVietWord());
        updateStarIcon(holder.star, data.getTick());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimatorSet animatorSet = (AnimatorSet) AnimatorInflater.loadAnimator(context, R.animator.transaction);
                animatorSet.setTarget(view);
                animatorSet.start();
                if (holder.textViewVietVer.getVisibility() == View.GONE) {
                    holder.textViewVietVer.setVisibility(View.VISIBLE);
                    holder.textViewEngVer.setVisibility(View.GONE);
                } else {
                    holder.textViewVietVer.setVisibility(View.GONE);
                    holder.textViewEngVer.setVisibility(View.VISIBLE);
                }
            }
        });
        holder.speak.setOnClickListener(view -> {
            textToSpeech = new TextToSpeech(view.getContext(), i -> {
                if (i == TextToSpeech.SUCCESS) {
                    textToSpeech.setLanguage(Locale.US);
                    textToSpeech.setSpeechRate(1.0f);
                    textToSpeech.speak(data.getEnglishWord(), TextToSpeech.QUEUE_ADD, null);
                }
            });
        });
        holder.star.setOnClickListener(view -> {
            boolean isTicked = data.getTick() == 1;
            if (isTicked) {
                holder.star.setImageResource(R.drawable.star);
                data.setTick(0);
            } else {
                holder.star.setImageResource(R.drawable.startfullfill);
                data.setTick(1);
            }
            memoryCardViewModel.updateTickedStatus(data.getIdTopic(), data.getId(), !isTicked);
            notifyItemChanged(position);
        });

    }

    @Override
    public int getItemCount() {
        return Math.min(dataSet.size(), 1); // Display only 1 item at a time
    }
    private void updateStarIcon(ImageView starIcon, int tickStatus) {
        if (tickStatus == 0) {
            starIcon.setImageResource(R.drawable.star);
        } else {
            starIcon.setImageResource(R.drawable.startfullfill);
        }
    }

    public void updateData(List<FlashCardEntity> newItems) {
        dataSet = newItems;
       notifyDataSetChanged();
    }
}
