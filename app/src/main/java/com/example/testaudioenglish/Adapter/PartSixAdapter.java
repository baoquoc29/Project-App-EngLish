package com.example.testaudioenglish.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.testaudioenglish.Model.ToeicModel.ReadingToeicModel;
import com.example.testaudioenglish.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PartSixAdapter extends PagerAdapter {

    private List<ReadingToeicModel> questionList;
    private OnRadioButtonClickListener onRadioButtonClickListener;
    private Map<Integer, String> selectedOptions = new HashMap<>();
    private static final int QUESTIONS_PER_PAGE = 4;
    private static final int BASE_INDEX = 131;

    public PartSixAdapter(List<ReadingToeicModel> questionList, OnRadioButtonClickListener onRadioButtonClickListener) {
        this.questionList = questionList;
        this.onRadioButtonClickListener = onRadioButtonClickListener;
    }

    @Override
    public int getCount() {
        return (int) Math.ceil((double) questionList.size() / QUESTIONS_PER_PAGE);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View view = inflater.inflate(R.layout.layout_part6, container, false);

        ImageView imagePartSix = view.findViewById(R.id.imagePartSix);
        TextView[] questionViews = new TextView[QUESTIONS_PER_PAGE];
        RadioGroup[] radioGroups = new RadioGroup[QUESTIONS_PER_PAGE];
        RadioButton[][] radioButtons = new RadioButton[QUESTIONS_PER_PAGE][4];

        // Mapping UI components to variables
        questionViews[0] = view.findViewById(R.id.numberQuestionSix);
        radioGroups[0] = view.findViewById(R.id.radioGroupSix1);
        radioButtons[0][0] = view.findViewById(R.id.radioSixA1);
        radioButtons[0][1] = view.findViewById(R.id.radioSixB1);
        radioButtons[0][2] = view.findViewById(R.id.radioSixC1);
        radioButtons[0][3] = view.findViewById(R.id.radioSixD1);

        questionViews[1] = view.findViewById(R.id.numberQuestionSix2);
        radioGroups[1] = view.findViewById(R.id.radioGroupSix2);
        radioButtons[1][0] = view.findViewById(R.id.radioSixA2);
        radioButtons[1][1] = view.findViewById(R.id.radioSixB2);
        radioButtons[1][2] = view.findViewById(R.id.radioSixC2);
        radioButtons[1][3] = view.findViewById(R.id.radioSixD2);

        questionViews[2] = view.findViewById(R.id.numberQuestionSix3);
        radioGroups[2] = view.findViewById(R.id.radioGroupSix3);
        radioButtons[2][0] = view.findViewById(R.id.radioSixA3);
        radioButtons[2][1] = view.findViewById(R.id.radioSixB3);
        radioButtons[2][2] = view.findViewById(R.id.radioSixC3);
        radioButtons[2][3] = view.findViewById(R.id.radioSixD3);

        questionViews[3] = view.findViewById(R.id.numberQuestionSix4);
        radioGroups[3] = view.findViewById(R.id.radioGroupSix4);
        radioButtons[3][0] = view.findViewById(R.id.radioSixA4);
        radioButtons[3][1] = view.findViewById(R.id.radioSixB4);
        radioButtons[3][2] = view.findViewById(R.id.radioSixC4);
        radioButtons[3][3] = view.findViewById(R.id.radioSixD4);

        // Set up questions and options
        for (int i = 0; i < QUESTIONS_PER_PAGE; i++) {
            int questionIndex = position * QUESTIONS_PER_PAGE + i;
            if (questionIndex < questionList.size()) {
                ReadingToeicModel question = questionList.get(questionIndex);
                questionViews[i].setText("Câu " + (BASE_INDEX + questionIndex) + ": " + question.getQuestion());
                radioButtons[i][0].setText(question.getOptionA());
                radioButtons[i][1].setText(question.getOptionB());
                radioButtons[i][2].setText(question.getOptionC());
                radioButtons[i][3].setText(question.getOptionD());
                setRadioGroupListener(radioGroups[i], questionIndex);

                // Load image for the first question of each set
                if (i == 0) {
                    String imageUrl = questionList.get(questionIndex).getIamge();
                    Glide.with(view).load(imageUrl).into(imagePartSix);
                }
            } else {
                radioGroups[i].setVisibility(View.GONE);
                questionViews[i].setVisibility(View.GONE);
            }
        }

        container.addView(view);
        return view;
    }

    private void setRadioGroupListener(RadioGroup radioGroup, int questionIndex) {
        // Restore previously selected option if it exists
        String selectedOption = selectedOptions.get(questionIndex);
        if (selectedOption != null) {
            switch (selectedOption) {
                case "A":
                    radioGroup.check(getRadioButtonId(questionIndex, 0));
                    break;
                case "B":
                    radioGroup.check(getRadioButtonId(questionIndex, 1));
                    break;
                case "C":
                    radioGroup.check(getRadioButtonId(questionIndex, 2));
                    break;
                case "D":
                    radioGroup.check(getRadioButtonId(questionIndex, 3));
                    break;
            }
        }

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String selected = "";
            if (checkedId == getRadioButtonId(questionIndex, 0)) {
                selected = "A";
            } else if (checkedId == getRadioButtonId(questionIndex, 1)) {
                selected = "B";
            } else if (checkedId == getRadioButtonId(questionIndex, 2)) {
                selected = "C";
            } else if (checkedId == getRadioButtonId(questionIndex, 3)) {
                selected = "D";
            }

            selectedOptions.put(questionIndex, selected);

            if (onRadioButtonClickListener != null) {
                onRadioButtonClickListener.onRadioButtonClicked(questionIndex, selected);
            }
        });
    }

    private int getRadioButtonId(int questionIndex, int optionIndex) {
        switch (questionIndex % QUESTIONS_PER_PAGE) {
            case 0:
                return optionIndex == 0 ? R.id.radioSixA1 :
                        optionIndex == 1 ? R.id.radioSixB1 :
                                optionIndex == 2 ? R.id.radioSixC1 : R.id.radioSixD1;
            case 1:
                return optionIndex == 0 ? R.id.radioSixA2 :
                        optionIndex == 1 ? R.id.radioSixB2 :
                                optionIndex == 2 ? R.id.radioSixC2 : R.id.radioSixD2;
            case 2:
                return optionIndex == 0 ? R.id.radioSixA3 :
                        optionIndex == 1 ? R.id.radioSixB3 :
                                optionIndex == 2 ? R.id.radioSixC3 : R.id.radioSixD3;
            case 3:
                return optionIndex == 0 ? R.id.radioSixA4 :
                        optionIndex == 1 ? R.id.radioSixB4 :
                                optionIndex == 2 ? R.id.radioSixC4 : R.id.radioSixD4;
            default:
                throw new IllegalArgumentException("Invalid option index");
        }
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public interface OnRadioButtonClickListener {
        void onRadioButtonClicked(int position, String selectedOption);
    }
}
