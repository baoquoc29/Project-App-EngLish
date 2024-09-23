package com.example.testaudioenglish.View.ToeicView;

import android.app.Dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.testaudioenglish.Activity.ExamToeicActivity;

import com.example.testaudioenglish.Adapter.ReadingPageAdapter;
import com.example.testaudioenglish.InterfaceAdapter.RadioButtonClickedAnswer;
import com.example.testaudioenglish.Model.Answer;
import com.example.testaudioenglish.Model.ToeicModel.ReadingToeicModel;
import com.example.testaudioenglish.R;

import com.example.testaudioenglish.Response.ReadingResponse;
import com.example.testaudioenglish.databinding.FragmentReadingToeic2Binding;
import com.example.testaudioenglish.viewmodel.ReadingComprehenViewModel;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class ReadingComprehenFragment extends Fragment implements RadioButtonClickedAnswer {
    private ReadingComprehenViewModel readingComprehenViewModel;
    private List<ReadingResponse> list;
    private int indexCorrect = 0;
    private Dialog dialog;
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private List<ReadingToeicModel> listReading = new ArrayList<>();
    private ReadingPageAdapter adapterReading;
    private List<Answer> listAnswer = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentReadingToeic2Binding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_reading_toeic2,container,false);
        readingComprehenViewModel = new ViewModelProvider(this).get(ReadingComprehenViewModel.class);
        binding.setViewModel(readingComprehenViewModel);
        binding.setLifecycleOwner(this);
        initializeUIComponents(binding);
        setUpPartSeven();
        return binding.getRoot();
    }
    public long getIdTopic(){
        return   ((ExamToeicActivity) getActivity()).getIdTopic();
    }
    private void initializeUIComponents(FragmentReadingToeic2Binding binding) {
        viewPager = binding.viewPager;
        circleIndicator = binding.circleIndicator;
    }
    private void setUpPartSeven(){
        readingComprehenViewModel.getListeningAllDataPart7(getIdTopic(),"Part 7").observe(getViewLifecycleOwner(), new Observer<ReadingResponse>() {
            @Override
            public void onChanged(ReadingResponse readingResponse) {
                listReading.clear();
                listReading.addAll(readingResponse.getData());
                for(int i = 0;i<listReading.size();i++){
                    listAnswer.add(new Answer(i + 147,listReading.get(i).getAnswerCorrect()));
                }
                ((ExamToeicActivity) getActivity()).setUpAnswer(listAnswer);
                adapterReading = new ReadingPageAdapter(listReading, new ReadingPageAdapter.OnRadioButtonClickListener() {
                    @Override
                    public void onRadioButtonClicked(int position, String selectedOption) {
                        ((ExamToeicActivity) getActivity()).pushUpHashMap(147 + position,selectedOption);
                    }
                });
                viewPager.setAdapter(adapterReading);
                circleIndicator.setViewPager(viewPager);
            }
        });
    }

    @Override
    public void onClick(int pos,String selected) {

    }
}