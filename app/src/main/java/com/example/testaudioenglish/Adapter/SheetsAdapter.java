package com.example.testaudioenglish.Adapter;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testaudioenglish.InterfaceAdapter.OnItemClickListener;
import com.example.testaudioenglish.Model.CheckListAnswer;
import com.example.testaudioenglish.Model.ToeicModel.ImageListeningModel;
import com.example.testaudioenglish.R;

import java.util.List;

public class SheetsAdapter extends RecyclerView.Adapter<SheetsAdapter.QuestionViewHolder> {

    private final List<CheckListAnswer> questions;
    private OnItemClickListener listener;
    public SheetsAdapter(List<CheckListAnswer> questions,OnItemClickListener listener) {
        this.questions = questions;
        this.listener = listener;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_sheets, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        holder.questionTextView.setText("Câu " + (position + 1));
        if(questions.size() == 25){
            holder.radioButtonD.setVisibility(View.GONE);
        }
        holder.radioButtonA.setTextColor(Color.BLACK);
        holder.radioButtonB.setTextColor(Color.BLACK);
        holder.radioButtonC.setTextColor(Color.BLACK);
        holder.radioButtonD.setTextColor(Color.BLACK);

        holder.radioButtonA.setText("A");
        holder.radioButtonB.setText("B");
        holder.radioButtonC.setText("C");
        holder.radioButtonD.setText("D");

        holder.linearLayout.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(position);
            }
        });

        check(position, holder);
    }

    public void check(int index, QuestionViewHolder holder) {
        if (index >= 0 && index < questions.size()) {
            CheckListAnswer answer = questions.get(index);
            if (answer.isCorrect()) {
                switch (answer.getCorrectAnswer()) {
                    case "A":
                        holder.radioButtonA.setTextColor(Color.GREEN);
                        break;
                    case "B":
                        holder.radioButtonB.setTextColor(Color.GREEN);
                        break;
                    case "C":
                        holder.radioButtonC.setTextColor(Color.GREEN);
                        break;
                    case "D":
                        holder.radioButtonD.setTextColor(Color.GREEN);
                        break;
                }
            }

            if (answer.getSelectedAnswer() != null && !answer.getSelectedAnswer().isEmpty()
                    && !answer.getCorrectAnswer().equals(answer.getSelectedAnswer())) {
                switch (answer.getSelectedAnswer()) {
                    case "A":
                        holder.radioButtonA.setTextColor(Color.RED);
                        break;
                    case "B":
                        holder.radioButtonB.setTextColor(Color.RED);
                        break;
                    case "C":
                        holder.radioButtonC.setTextColor(Color.RED);
                        break;
                    case "D":
                        holder.radioButtonD.setTextColor(Color.RED);
                        break;
                }
            }
        } else {
            Log.e("SheetsAdapter", "Index out of bounds: " + index + ", Size: " + questions.size());
        }

    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    static class QuestionViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView questionTextView;
        RadioButton radioButtonA;
        RadioButton radioButtonB;
        RadioButton radioButtonC;
        RadioButton radioButtonD;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            questionTextView = itemView.findViewById(R.id.question_text_view);
            radioButtonA = itemView.findViewById(R.id.radio_button_a);
            radioButtonB = itemView.findViewById(R.id.radio_button_b);
            radioButtonC = itemView.findViewById(R.id.radio_button_c);
            radioButtonD = itemView.findViewById(R.id.radio_button_d);
            linearLayout = itemView.findViewById(R.id.layoutSheets);
        }
    }
}
