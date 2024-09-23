package com.example.testaudioenglish.View.AppView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testaudioenglish.Activity.DictationTopicActivity;
import com.example.testaudioenglish.Activity.ExamNavigationActivity;
import com.example.testaudioenglish.Activity.ExamToeicActivity;
import com.example.testaudioenglish.Adapter.ToeicAdapter;
import com.example.testaudioenglish.Adapter.TopicDictationAdapter;
import com.example.testaudioenglish.InterfaceAdapter.OnItemClickListener;
import com.example.testaudioenglish.Model.EachPartModel;
import com.example.testaudioenglish.Model.ToeicModel.TopicToeicModel;
import com.example.testaudioenglish.Model.TopicDictationModel;
import com.example.testaudioenglish.R;
import com.example.testaudioenglish.Response.DictationRespone;
import com.example.testaudioenglish.Response.TopicResponse;
import com.example.testaudioenglish.databinding.FragmentHomeBinding;
import com.example.testaudioenglish.viewmodel.HomeFragmentViewModel;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FragmentHome extends Fragment implements OnItemClickListener {
    private HomeFragmentViewModel homeFragmentViewModel;
    private TextView us;
    private RecyclerView recyclerView;
    private ToeicAdapter toeicAdapter;
    private List<TopicToeicModel> list = new ArrayList<>();
    private int position;
    private TextView[] dayofWeek = new TextView[7];
    private TextView day;
    private List<EachPartModel> listPart = new ArrayList<>();
    private LinearLayout[] cardViews = new LinearLayout[7];
    private RecyclerView dictation_topic;
    private List<TopicDictationModel> dictationModelList = new ArrayList<>();
    private TopicDictationAdapter topicDictationAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentHomeBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        homeFragmentViewModel = new ViewModelProvider(this).get(HomeFragmentViewModel.class);
        binding.setViewModel(homeFragmentViewModel);
        binding.setLifecycleOwner(this);
        View view = binding.getRoot();

        us = view.findViewById(R.id.us);
        day = binding.day;
        recyclerView = binding.topicToeic;
        dayofWeek[0] = binding.dayofweek;
        dayofWeek[1] = binding.dayofweek1;
        dayofWeek[2] = binding.dayofweek2;
        dayofWeek[3] = binding.dayofweek3;
        dayofWeek[4] = binding.dayofweek4;
        dayofWeek[5] = binding.dayofweek5;
        dayofWeek[6] = binding.dayofweek6;
        cardViews[0] = binding.monCardView;
        cardViews[1] = binding.tueCardView;
        cardViews[2] = binding.wedCardView;
        cardViews[3] = binding.thuCardView;
        cardViews[4] = binding.friCardView;
        cardViews[5] = binding.satCardView;
        cardViews[6] = binding.sunCardView;

        setDaysOfWeek();
        loadUserDetails();
        setUpRecyclerView();
        setClickToDictationTopic();
        return view;
    }
    private void setDaysOfWeek() {
        LocalDate today = LocalDate.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd", Locale.getDefault());

        LocalDate startOfWeek = today;
        while (startOfWeek.getDayOfWeek() != DayOfWeek.MONDAY) {
            startOfWeek = startOfWeek.minusDays(1);
        }

        for (int i = 0; i < 7; i++) {
            LocalDate day = startOfWeek.plusDays(i);
            dayofWeek[i].setText(day.format(formatter));
        }
    }
    private void loadUserDetails() {
        SharedPreferences sharedPref = requireActivity().getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        String storedUsername = sharedPref.getString("username", "");

        SharedPreferences sharedDate = requireActivity().getSharedPreferences("CheckDate", Context.MODE_PRIVATE);
        SharedPreferences userPrefs = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        Long idCustomer = userPrefs.getLong("idCustomer", 1L);
        int totalDay = userPrefs.getInt("totalDay",0);
        String date = sharedDate.getString("date","-1");
        us.setText(String.format("Xin chào, %s", storedUsername));

        String listDate[] = date.split(",");
        int checkedColor = getResources().getColor(R.color.color_checked);

        for (int i = 0; i < listDate.length; i++) {
            listDate[i] = listDate[i].replace("\"", "").trim();
            if (listDate[i].equals("Monday")) {
                cardViews[0].setBackgroundColor(checkedColor);
            }
            else if(listDate[i].equals("Tuesday")){
                cardViews[1].setBackgroundColor(checkedColor);
            }
            else if(listDate[i].equals("Wednesday")){
                cardViews[2].setBackgroundColor(checkedColor);
            }
            else if(listDate[i].equals("Thursday")){
                cardViews[3].setBackgroundColor(checkedColor);
            }
            else if(listDate[i].equals("Friday")){
                cardViews[4].setBackgroundColor(checkedColor);
            }
            else if(listDate[i].equals("Saturday")){
                cardViews[5].setBackgroundColor(checkedColor);
            }
            else if(listDate[i].equals("Sunday")){
                cardViews[6].setBackgroundColor(checkedColor);
            }
        }
        day.setText("" + totalDay);
    }
    private void setUpRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);
        toeicAdapter = new ToeicAdapter(getContext(), list,this);
        recyclerView.setAdapter(toeicAdapter);

        homeFragmentViewModel.getAllDataTopic().observe(getViewLifecycleOwner(), new Observer<TopicResponse>() {
            @Override
            public void onChanged(TopicResponse topicResponse) {
                if (topicResponse != null && topicResponse.getData() != null) {
                    list.clear();
                    list.addAll(topicResponse.getData());
                    toeicAdapter.notifyDataSetChanged();

                } else {
                    Toast.makeText(getContext(), "No data available", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void setClickToDictationTopic(){
        homeFragmentViewModel.getNavigateToDictationTopic().observe(getViewLifecycleOwner(),clicked->{
            if(clicked){
                Intent intent = new Intent(getActivity(), DictationTopicActivity.class);
                startActivity(intent);
            }
        });
    }
    private void showTestOptionsDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.customdialoganypart, null);


        builder.setView(dialogView);


        AlertDialog dialog = builder.create();

        Button btnDoPart = dialogView.findViewById(R.id.btn_do_part);
        Button btnDoExam = dialogView.findViewById(R.id.btn_do_exam);

        btnDoPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToAnyPart();
                dialog.dismiss();
            }
        });

        btnDoExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToExam();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void navigateToExam() {
        Intent intent = new Intent(getActivity(), ExamToeicActivity.class);
        intent.putExtra("value", list.get(position).getIdQuiz());
        intent.putExtra("idCustomer", 1);
        startActivity(intent);
    }
    private void navigateToAnyPart() {
        Intent intent = new Intent(getActivity(), ExamNavigationActivity.class);
        intent.putExtra("idTopic", list.get(position).getIdQuiz());
        intent.putExtra("idCustomer", 1);
        startActivity(intent);
    }

    @Override
    public void onItemClick(int position) {
        this.position = position;
        showTestOptionsDialog();
    }
}
