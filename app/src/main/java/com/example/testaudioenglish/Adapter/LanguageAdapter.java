package com.example.testaudioenglish.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testaudioenglish.Model.Translations;
import com.example.testaudioenglish.R;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LanguageViewHolder> {

    private List<Translations> languageList;

    public LanguageAdapter(List<Translations> languageList) {
        this.languageList = languageList;

        sortByPartOfSpeech();
    }

    @NonNull
    @Override
    public LanguageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_languagedetails, parent, false);
        return new LanguageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LanguageViewHolder holder, int position) {
        Translations currentItem = languageList.get(position);
        // Hiển thị loại từ theo ngôn ngữ tiếng Việt
        String partOfSpeech = getVietnamesePartOfSpeech(currentItem.getPosTag());
        holder.nounTextView.setText(partOfSpeech);
        holder.translationTextView.setText(currentItem.getTranslation());
    }

    @Override
    public int getItemCount() {
        return languageList.size();
    }


    private void sortByPartOfSpeech() {
        Collections.sort(languageList, new Comparator<Translations>() {
            @Override
            public int compare(Translations t1, Translations t2) {
                return getPriority(t1.getPosTag()) - getPriority(t2.getPosTag());
            }
        });
    }


    private int getPriority(String posTag) {
        switch (posTag) {
            case "NOUN":
                return 1;
            case "VERB":
                return 2;
            case "ADJ":
                return 3;
            default:
                return 4;
        }
    }

    private String getVietnamesePartOfSpeech(String posTag) {
        switch (posTag) {
            case "NOUN":
                return "Danh từ";
            case "VERB":
                return "Động từ";
            case "ADJ":
                return "Tính từ";
            default:
                return "Loại từ khác";
        }
    }

    public static class LanguageViewHolder extends RecyclerView.ViewHolder {
        public TextView nounTextView;
        public TextView translationTextView;

        public LanguageViewHolder(@NonNull View itemView) {
            super(itemView);
            nounTextView = itemView.findViewById(R.id.text_view_noun);
            translationTextView = itemView.findViewById(R.id.text_view_translation);
        }
    }

}
