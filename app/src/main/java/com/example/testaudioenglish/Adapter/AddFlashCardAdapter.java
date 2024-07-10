package com.example.testaudioenglish.Adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testaudioenglish.Model.WordFlashCard;
import com.example.testaudioenglish.R;

import java.util.List;

public class AddFlashCardAdapter extends RecyclerView.Adapter<AddFlashCardAdapter.ViewHolder> {

    private List<WordFlashCard> flashCardList;

    public AddFlashCardAdapter(List<WordFlashCard> flashCardList) {
        this.flashCardList = flashCardList;
    }

    public void setFlashCardList(List<WordFlashCard> flashCardList) {
        this.flashCardList = flashCardList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_add_flash_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WordFlashCard flashCard = flashCardList.get(position);
        holder.bind(flashCard);
    }

    @Override
    public int getItemCount() {
        return flashCardList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public EditText termInput;
        public EditText definitionInput;
        private TextWatcher termTextWatcher;
        private TextWatcher definitionTextWatcher;

        public ViewHolder(View view) {
            super(view);
            termInput = view.findViewById(R.id.term_input);
            definitionInput = view.findViewById(R.id.definition_input);
        }

        public void bind(WordFlashCard flashCard) {
            // Remove existing TextWatchers if they exist
            if (termTextWatcher != null) {
                termInput.removeTextChangedListener(termTextWatcher);
            }
            if (definitionTextWatcher != null) {
                definitionInput.removeTextChangedListener(definitionTextWatcher);
            }

            // Set initial text
            termInput.setText(flashCard.getEngVer());
            definitionInput.setText(flashCard.getVietVer());

            // Create new TextWatchers
            termTextWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    // Do nothing
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    flashCard.setEngVer(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    // Do nothing
                }
            };

            definitionTextWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    // Do nothing
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    flashCard.setVietVer(charSequence.toString());
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    // Do nothing
                }
            };

            // Add the new TextWatchers
            termInput.addTextChangedListener(termTextWatcher);
            definitionInput.addTextChangedListener(definitionTextWatcher);
        }
    }
}
