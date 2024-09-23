package com.example.testaudioenglish.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.testaudioenglish.Model.ToeicModel.MultichoiceToeicModel;
import com.example.testaudioenglish.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuestionPageAdapter extends PagerAdapter {

    private List<MultichoiceToeicModel> questionList;
    private OnRadioButtonClickListener onRadioButtonClickListener;
    private Map<Integer, String> selectedAnswers = new HashMap<>();  // Map to store selected answers
    private int currentIndex = 101;
    private boolean showCorrectAnswers = false;  // Flag to control visibility of correct answers

    public QuestionPageAdapter(List<MultichoiceToeicModel> questionList, OnRadioButtonClickListener onRadioButtonClickListener) {
        this.questionList = questionList;
        this.onRadioButtonClickListener = onRadioButtonClickListener;
    }

    @Override
    public int getCount() {
        return questionList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View view = inflater.inflate(R.layout.layout_part5, container, false);

        TextView numberQuestion = view.findViewById(R.id.numberQuestion);
        RadioGroup radioGroup = view.findViewById(R.id.radioGroup);
        RadioButton radioA = view.findViewById(R.id.radioA);
        RadioButton radioB = view.findViewById(R.id.radioB);
        RadioButton radioC = view.findViewById(R.id.radioC);
        RadioButton radioD = view.findViewById(R.id.radioD);

        MultichoiceToeicModel question = questionList.get(position);
        numberQuestion.setText("Câu " + (position + currentIndex) + " " + question.getQuestion());
        radioA.setText(question.getOptionA());
        radioB.setText(question.getOptionB());
        radioC.setText(question.getOptionC());
        radioD.setText(question.getOptionD());
        String selectedOption = selectedAnswers.get(position);
        if (selectedOption != null) {
            switch (selectedOption) {
                case "A":
                    radioA.setChecked(true);
                    break;
                case "B":
                    radioB.setChecked(true);
                    break;
                case "C":
                    radioC.setChecked(true);
                    break;
                case "D":
                    radioD.setChecked(true);
                    break;
            }
        } else {
            radioGroup.clearCheck();
        }

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (onRadioButtonClickListener != null) {
                String selectedOptionNew = "";
                if (checkedId == R.id.radioA) {
                    selectedOptionNew = "A";
                } else if (checkedId == R.id.radioB) {
                    selectedOptionNew = "B";
                } else if (checkedId == R.id.radioC) {
                    selectedOptionNew = "C";
                } else if (checkedId == R.id.radioD) {
                    selectedOptionNew = "D";
                }

                selectedAnswers.put(position, selectedOptionNew);
                onRadioButtonClickListener.onRadioButtonClicked(position, selectedOptionNew);
            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public interface OnRadioButtonClickListener {
        void onRadioButtonClicked(int position, String selectedOption);
    }
}
