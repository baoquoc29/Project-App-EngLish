package com.example.testaudioenglish.View.ToeicView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.testaudioenglish.Activity.ExamToeicActivity;
import com.example.testaudioenglish.Adapter.QuestionPageAdapter;
import com.example.testaudioenglish.InterfaceAdapter.RadioButtonClickedAnswer;
import com.example.testaudioenglish.Model.Answer;
import com.example.testaudioenglish.Model.ToeicModel.MultichoiceToeicModel;
import com.example.testaudioenglish.Model.ToeicModel.ReadingToeicModel;
import com.example.testaudioenglish.R;
import com.example.testaudioenglish.Response.MultipleChoiceRespone;
import com.example.testaudioenglish.databinding.ActivityExamToeicBinding;
import com.example.testaudioenglish.viewmodel.MultipleChoiceToeicViewModel;
import com.example.testaudioenglish.Model.ToeicModel.ImageListeningModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class MultipleChoiceToeicFragment extends Fragment implements RadioButtonClickedAnswer {
    private MultipleChoiceToeicViewModel multipleChoiceToeicViewModel;
    private List<ImageListeningModel> list;
    private int currentIndex = 101;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private List<MultichoiceToeicModel> listMultiple = new ArrayList<>();
    private List<ReadingToeicModel> listReading = new ArrayList<>();
    private QuestionPageAdapter adapter;
    private LinearLayout multipleChoice;
    private HashMap<Integer,String> mapMultiple = new HashMap<>();
    private List<Answer> listAnswer = new ArrayList<>();
    private int totalQuestion = 31;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ActivityExamToeicBinding binding = DataBindingUtil.inflate(inflater, R.layout.activity_exam_toeic, container, false);
        multipleChoiceToeicViewModel = new ViewModelProvider(this).get(MultipleChoiceToeicViewModel.class);
        binding.setViewModel(multipleChoiceToeicViewModel);
        binding.setLifecycleOwner(this);
        initializeUIComponents(binding);
        list = new ArrayList<>();
        for(int i = 0;i<totalQuestion;i++){
            mapMultiple.put(currentIndex + i,"");
        }
        setUpPartFive();
        return binding.getRoot();
    }

    private void initializeUIComponents(ActivityExamToeicBinding binding) {
        viewPager = binding.viewPager;
        circleIndicator = binding.circleIndicator;
        multipleChoice = binding.multipleChoice;


    }


    public long getIdTopic(){
        return   ((ExamToeicActivity) getActivity()).getIdTopic();
    }
    private void setUpPartFive() {
        multipleChoiceToeicViewModel.getListeningAllDataPart5(getIdTopic()).observe(getViewLifecycleOwner(), new Observer<MultipleChoiceRespone>() {
            @Override
            public void onChanged(MultipleChoiceRespone multipleChoiceRespone) {
                listMultiple.clear();
                listMultiple.addAll(multipleChoiceRespone.getData());
                for (int i = 0; i < listMultiple.size(); i++) {
                    listAnswer.add(new Answer(101 + i, listMultiple.get(i).getAnswerCorrect()));
                }
                ((ExamToeicActivity) getActivity()).setUpAnswer(listAnswer);
                adapter = new QuestionPageAdapter(listMultiple, new QuestionPageAdapter.OnRadioButtonClickListener() {
                    @Override
                    public void onRadioButtonClicked(int position, String selected) {
                        Toast.makeText(getContext(), "Question " + (currentIndex + position) + ": Selected Option: " + selected, Toast.LENGTH_SHORT).show();
                        ((ExamToeicActivity) getActivity()).pushUpHashMap(currentIndex + position,selected);
                    }
                });

                viewPager.setAdapter(adapter);
                circleIndicator.setViewPager(viewPager);
            }
        });
    }



    @Override
    public void onClick(int pos, String selected) {
        // Handle radio button clicks if needed
    }
}
