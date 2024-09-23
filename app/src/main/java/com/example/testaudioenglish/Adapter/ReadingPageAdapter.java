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
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.testaudioenglish.Model.ToeicModel.ReadingToeicModel;
import com.example.testaudioenglish.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadingPageAdapter extends PagerAdapter {

    private List<ReadingToeicModel> questionList;
    private OnRadioButtonClickListener onRadioButtonClickListener;
    private Map<Integer, String> selectedOptions = new HashMap<>();
    private static final int BASE_INDEX_PART_SEVEN = 147;
    private ViewPager viewPager;
    public ReadingPageAdapter(List<ReadingToeicModel> questionList, OnRadioButtonClickListener onRadioButtonClickListener) {
        this.questionList = questionList;
        this.onRadioButtonClickListener = onRadioButtonClickListener;
    }

    @Override
    public int getCount() {
        return 15;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View view = inflater.inflate(R.layout.layout_part7, container, false);

        ImageView imagePartSix = view.findViewById(R.id.imagePartSix);
        TextView[] questionViews = new TextView[5];
        RadioGroup[] radioGroups = new RadioGroup[5];
        RadioButton[][] radioButtons = new RadioButton[5][5];

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

        questionViews[4] = view.findViewById(R.id.numberQuestionSix5);
        radioGroups[4] = view.findViewById(R.id.radioGroupSix5);
        radioButtons[4][0] = view.findViewById(R.id.radioSixA5);
        radioButtons[4][1] = view.findViewById(R.id.radioSixB5);
        radioButtons[4][2] = view.findViewById(R.id.radioSixC5);
        radioButtons[4][3] = view.findViewById(R.id.radioSixD5);
        // 0 - 3 //2 don vi
        // 4 - 6 // 3 don vi
        // 7 - 9 // 3 don vi
        if(position < 4) {
            for (int i = 0; i < 4; i++) {
                int questionIndex = position * 2 + i;
                if (questionIndex >= questionList.size()) break;
                ReadingToeicModel question = questionList.get(questionIndex);
                if (i < 2) {
                    questionViews[i].setText("Câu " + (BASE_INDEX_PART_SEVEN + questionIndex) + ": " + question.getQuestion());
                    radioButtons[i][0].setText(question.getOptionA());
                    radioButtons[i][1].setText(question.getOptionB());
                    radioButtons[i][2].setText(question.getOptionC());
                    radioButtons[i][3].setText(question.getOptionD());
                    setRadioGroupListener(radioGroups[i], questionIndex);
                    questionViews[2].setVisibility(View.GONE);
                    questionViews[3].setVisibility(View.GONE);
                    radioGroups[2].setVisibility(View.GONE);
                    radioGroups[3].setVisibility(View.GONE);
                    questionViews[4].setVisibility(View.GONE);
                    radioGroups[4].setVisibility(View.GONE);
                }
                String imageUrl = question.getIamge();
                if (!imageUrl.isEmpty()) {
                    Glide.with(view).load(imageUrl).into(imagePartSix);
                }
            }
        }
        else if(position > 3 && position < 7) {
            // 4
            for (int i = 0; i < 4; i++) {
                int questionIndex = position * 3 + i - 4;
                if (questionIndex >= questionList.size()) break;
                ReadingToeicModel question = questionList.get(questionIndex);
                if (i < 3) {
                    questionViews[i].setText("Câu " + (BASE_INDEX_PART_SEVEN + questionIndex) + ": " + question.getQuestion());
                    radioButtons[i][0].setText(question.getOptionA());
                    radioButtons[i][1].setText(question.getOptionB());
                    radioButtons[i][2].setText(question.getOptionC());
                    radioButtons[i][3].setText(question.getOptionD());
                    questionViews[3].setVisibility(View.GONE);
                    radioGroups[3].setVisibility(View.GONE);
                    questionViews[4].setVisibility(View.GONE);
                    radioGroups[4].setVisibility(View.GONE);
                    setRadioGroupListener(radioGroups[i], questionIndex);
                }
                String imageUrl = question.getIamge();
                if (!imageUrl.isEmpty()) {
                    Glide.with(view).load(imageUrl).into(imagePartSix);
                }
            }
        }
        else if(position   > 6 && position < 10 ){
            for (int i = 0; i < 4; i++) {
                int questionIndex = position * 4 + i - 11;
                if (questionIndex >= questionList.size()) break;
                ReadingToeicModel question = questionList.get(questionIndex);
                    questionViews[i].setText("Câu " + (BASE_INDEX_PART_SEVEN + questionIndex) + ": " + question.getQuestion());
                    radioButtons[i][0].setText(question.getOptionA());
                    radioButtons[i][1].setText(question.getOptionB());
                    radioButtons[i][2].setText(question.getOptionC());
                    radioButtons[i][3].setText(question.getOptionD());
                questionViews[4].setVisibility(View.GONE);
                radioGroups[4].setVisibility(View.GONE);
                    setRadioGroupListener(radioGroups[i], questionIndex);

                String imageUrl = question.getIamge();
                if (!imageUrl.isEmpty()) {
                    Glide.with(view).load(imageUrl).into(imagePartSix);
                }
            }
        }
        else {
            for (int i = 0; i < 5; i++) {
                int questionIndex = (position * 5 + i - 21);
                if (questionIndex >= questionList.size()) break;
                ReadingToeicModel question = questionList.get(questionIndex);
                questionViews[i].setText("Câu " + (BASE_INDEX_PART_SEVEN + questionIndex) + ": " + question.getQuestion());
                radioButtons[i][0].setText(question.getOptionA());
                radioButtons[i][1].setText(question.getOptionB());
                radioButtons[i][2].setText(question.getOptionC());
                radioButtons[i][3].setText(question.getOptionD());

                setRadioGroupListener(radioGroups[i], questionIndex);

                String imageUrl = question.getIamge();
                if (!imageUrl.isEmpty()) {
                    Glide.with(view).load(imageUrl).into(imagePartSix);
                }
            }
        }

        container.addView(view);
        return view;
    }

    private void setRadioGroupListener(RadioGroup radioGroup, int questionIndex) {
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String selected = "";
            if (checkedId == R.id.radioSixA1 || checkedId == R.id.radioSixA2 || checkedId == R.id.radioSixA3 || checkedId == R.id.radioSixA4 || checkedId == R.id.radioSixA5) {
                selected = "A";
            } else if (checkedId == R.id.radioSixB1 || checkedId == R.id.radioSixB2 || checkedId == R.id.radioSixB3 || checkedId == R.id.radioSixB4 || checkedId == R.id.radioSixB5) {
                selected = "B";
            } else if (checkedId == R.id.radioSixC1 || checkedId == R.id.radioSixC2 || checkedId == R.id.radioSixC3 || checkedId == R.id.radioSixC4 || checkedId == R.id.radioSixC5) {
                selected = "C";
            } else if (checkedId == R.id.radioSixD1 || checkedId == R.id.radioSixD2 || checkedId == R.id.radioSixD3 || checkedId == R.id.radioSixD4 || checkedId == R.id.radioSixD5) {
                selected = "D";
            }

            selectedOptions.put(questionIndex, selected);

            if (onRadioButtonClickListener != null) {
                onRadioButtonClickListener.onRadioButtonClicked(questionIndex, selected);
            }
        });

        String selectedOption = selectedOptions.get(questionIndex);
        if (selectedOption != null) {
            switch (selectedOption) {
                case "A":
                    radioGroup.check(radioGroup.getChildAt(0).getId());
                    break;
                case "B":
                    radioGroup.check(radioGroup.getChildAt(1).getId());
                    break;
                case "C":
                    radioGroup.check(radioGroup.getChildAt(2).getId());
                    break;
                case "D":
                    radioGroup.check(radioGroup.getChildAt(3).getId());
                    break;
            }
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
