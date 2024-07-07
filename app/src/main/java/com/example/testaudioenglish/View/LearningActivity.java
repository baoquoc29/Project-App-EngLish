package com.example.testaudioenglish.View;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.example.testaudioenglish.Adapter.WordAdapter;
import com.example.testaudioenglish.Entity.FlashCardEntity;
import com.example.testaudioenglish.R;
import com.example.testaudioenglish.databinding.ActivityLearningBinding;
import com.example.testaudioenglish.viewmodel.LearningViewModel;

import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class LearningActivity extends AppCompatActivity {
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private WordAdapter adapter;
    private LearningViewModel learningViewModel;
    private List<FlashCardEntity> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLearningBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_learning);
        learningViewModel = new ViewModelProvider(this).get(LearningViewModel.class);
        binding.setViewModel(learningViewModel);
        binding.setLifecycleOwner(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Luyện tập từ vựng");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");
        }

        // Initialize ViewPager and CircleIndicator
        viewPager = binding.viewPager;
        circleIndicator = binding.circleIndicator;

        // Get the topic ID from the Intent
        Intent intent = getIntent();
        long id = intent.getLongExtra("value", -1);



        // Observe the LiveData from the ViewModel
        learningViewModel.getWordByIdTopic(id).observe(this, new Observer<List<FlashCardEntity>>() {
            @Override
            public void onChanged(List<FlashCardEntity> flashCardEntities) {
                if (flashCardEntities != null && !flashCardEntities.isEmpty()) {
                    list = flashCardEntities;
                    adapter = new WordAdapter(LearningActivity.this, list);
                    viewPager.setAdapter(adapter);
                    circleIndicator.setViewPager(viewPager);
                } else {
                    // Handle case where list is empty or null
                    //Toast.makeText(LearningActivity.this, "No data available", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
