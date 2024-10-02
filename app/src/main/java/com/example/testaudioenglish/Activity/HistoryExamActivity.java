package com.example.testaudioenglish.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testaudioenglish.Adapter.HistoryAdapter;
import com.example.testaudioenglish.Model.HistoryModel;
import com.example.testaudioenglish.Model.ToeicModel.UserScoreModel;
import com.example.testaudioenglish.R;
import com.example.testaudioenglish.Response.UserScoreResponse;
import com.example.testaudioenglish.databinding.ActivityHistoryExamBinding;
import com.example.testaudioenglish.viewmodel.HistoryExamViewModel;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HistoryExamActivity extends AppCompatActivity {
    private static final long DEFAULT_ID_CUSTOMER = 1L;

    private HistoryExamViewModel historyExamViewModel;
    private HistoryAdapter historyAdapter;
    private List<UserScoreModel> userScoreList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityHistoryExamBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_history_exam);

        initializeUI(binding);
        initializeViewModel();
        loadUserHistory();
    }

    private void initializeUI(ActivityHistoryExamBinding binding) {
        setupToolbar();
        setupRecyclerView(binding.recyleView);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userScoreList = new ArrayList<>();
        historyAdapter = new HistoryAdapter(userScoreList);
        recyclerView.setAdapter(historyAdapter);
    }

    private void initializeViewModel() {
        historyExamViewModel = new ViewModelProvider(this).get(HistoryExamViewModel.class);
    }

    private void loadUserHistory() {
        SharedPreferences userPrefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        long idCustomer = userPrefs.getLong("idCustomer", DEFAULT_ID_CUSTOMER);
        int page = 0;
        int size = 5;
        historyExamViewModel.getHistoryExam(idCustomer,page,size).observe(this, new Observer<UserScoreResponse>() {
            @Override
            public void onChanged(UserScoreResponse userScoreResponse) {
                if (userScoreResponse != null) {
                    updateUserScoreList(userScoreResponse.getData());
                }
            }
        });
    }

    private void updateUserScoreList(List<UserScoreModel> newData) {
        userScoreList.clear();
        userScoreList.addAll(newData);
        historyAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
