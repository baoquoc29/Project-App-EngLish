package com.example.testaudioenglish.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testaudioenglish.Adapter.TopicDictationAdapter;
import com.example.testaudioenglish.InterfaceAdapter.OnItemClickListener;
import com.example.testaudioenglish.Model.TopicDictationModel;
import com.example.testaudioenglish.R;
import com.example.testaudioenglish.Response.DictationRespone;
import com.example.testaudioenglish.databinding.ActivityDictationTopicBinding;
import com.example.testaudioenglish.viewmodel.DictationViewModel;

import java.util.ArrayList;
import java.util.List;

public class DictationTopicActivity extends AppCompatActivity implements OnItemClickListener{
    private DictationViewModel dictationViewModel;
    private RecyclerView dictation_topic;
    private List<TopicDictationModel> dictationModelList = new ArrayList<>();
    private TopicDictationAdapter topicDictationAdapter;

    private long idTopic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDictationTopicBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_dictation_topic);
        dictationViewModel = new ViewModelProvider(this).get(DictationViewModel.class);
        binding.setViewModel(dictationViewModel);
        binding.setLifecycleOwner(this);


        dictation_topic = binding.dictationRecyleview;


        setupToolbar();

        loadData();

    }
    public void setClickToDetails(){
        Intent intent = new Intent(this,DictationQuestionActivity.class);
        intent.putExtra("idTopic",idTopic);
        startActivity(intent);
    }
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                toolbar.setNavigationOnClickListener(v -> onBackPressed());
            }
        }
    }

    public void loadData() {
        // Setup layout manager for RecyclerView
        LinearLayoutManager layoutManagerDictation = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        dictation_topic.setLayoutManager(layoutManagerDictation);

        topicDictationAdapter = new TopicDictationAdapter(dictationModelList,this);
        dictation_topic.setAdapter(topicDictationAdapter);

        dictationViewModel.getAllDictationTopic().observe(this, new Observer<DictationRespone>() {
            @Override
            public void onChanged(DictationRespone dictationRespone) {
                if (dictationRespone != null && dictationRespone.getData() != null) {
                    dictationModelList.clear();
                    dictationModelList.addAll(dictationRespone.getData());
                    topicDictationAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(DictationTopicActivity.this, "No data available", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onItemClick(int position) {
        idTopic = dictationModelList.get(position).getId();
        setClickToDetails();
    }
}
