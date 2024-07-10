package com.example.testaudioenglish.Adapter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testaudioenglish.Entity.FlashCardEntity;
import com.example.testaudioenglish.R;
import com.example.testaudioenglish.viewmodel.CardPairingViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class CardPairingAdapter extends RecyclerView.Adapter<CardPairingAdapter.ViewHolder> {

    private final List<FlashCardEntity> data;
    private final HashMap<Integer, Integer> usedRandomPositions;
    private FlashCardEntity selectedEngCard;
    private FlashCardEntity selectedVietCard;
    private final CardPairingViewModel cardPairingViewModel;
    private int count = 0;
    public boolean previous = false;

    public CardPairingAdapter(List<FlashCardEntity> data, CardPairingViewModel cardPairingViewModel) {
        this.data = data;
        this.usedRandomPositions = new HashMap<>();
        this.cardPairingViewModel = cardPairingViewModel;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardpairing, parent, false);
        return new ViewHolder(view);
    }

    public boolean isPrevious() {
        return previous;
    }

    public void setPrevious(boolean previous) {
        this.previous = previous;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        FlashCardEntity item = data.get(position);
        Random random = new Random();
        int randomPosition;
        do {
            randomPosition = random.nextInt(data.size());
        } while (usedRandomPositions.containsValue(randomPosition));
        usedRandomPositions.put(position, randomPosition);
        holder.engVer.setText(item.getEnglishWord());
        holder.vietVer.setText(data.get(randomPosition).getVietWord());

        holder.engVer.setOnClickListener(view -> {
            holder.engCard.setCardBackgroundColor(Color.GRAY);
            selectedEngCard = item;
            if (selectedEngCard != null) {
                Toast.makeText(view.getContext(), "Selected English: " + selectedEngCard.getEnglishWord(), Toast.LENGTH_SHORT).show();
                count++;
                checkForMatch(holder);
            }
        });

        holder.vietVer.setOnClickListener(view -> {
            previous = true;
            int index = usedRandomPositions.get(position);
            holder.vietCard.setCardBackgroundColor(Color.GRAY);
            selectedVietCard = data.get(index);
            if (selectedVietCard != null) {
                Toast.makeText(view.getContext(), "Selected Vietnamese: " + selectedVietCard.getVietWord(), Toast.LENGTH_SHORT).show();
                count++;
                checkForMatch(holder);
            }
        });
    }

    private void checkForMatch(ViewHolder holder) {
        if (selectedEngCard != null && selectedVietCard != null) {
            if (selectedEngCard.getEnglishWord().equals(selectedVietCard.getEnglishWord())) {
                removeItem(selectedEngCard);
                removeItem(selectedVietCard);
                Toast.makeText(holder.itemView.getContext(), "Match found!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(holder.itemView.getContext(), "Incorrect!", Toast.LENGTH_SHORT).show();
                resetCardColors(holder);
            }
            resetSelections();
        }
    }

    private void resetSelections() {
        selectedEngCard = null;
        selectedVietCard = null;
        count = 0;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private void removeItem(FlashCardEntity cardEntity) {
        int position = data.indexOf(cardEntity);
        if (position != -1) {
            data.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, data.size());
            usedRandomPositions.clear();
        }
    }

    private void resetCardColors(ViewHolder holder) {
        holder.engCard.setCardBackgroundColor(Color.WHITE);
        holder.vietCard.setCardBackgroundColor(Color.WHITE);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView engVer;
        public final TextView vietVer;
        public final FrameLayout container;
        public final CardView engCard;
        public final CardView vietCard;

        public ViewHolder(View view) {
            super(view);
            container = view.findViewById(R.id.frame_layout);
            engVer = view.findViewById(R.id.engver);
            vietVer = view.findViewById(R.id.vietver);
            engCard = view.findViewById(R.id.eng_card);
            vietCard = view.findViewById(R.id.viet_card);
        }
    }
}
