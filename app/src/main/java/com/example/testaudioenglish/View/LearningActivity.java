package com.example.testaudioenglish.View;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.testaudioenglish.Adapter.MyBottomSheetDialog;
import com.example.testaudioenglish.Adapter.WordAdapter;
import com.example.testaudioenglish.Adapter.WordInTopicAdapter;
import com.example.testaudioenglish.Entity.FlashCardEntity;
import com.example.testaudioenglish.R;
import com.example.testaudioenglish.SortClicked;
import com.example.testaudioenglish.databinding.ActivityLearningBinding;
import com.example.testaudioenglish.viewmodel.LearningViewModel;

import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class LearningActivity extends AppCompatActivity implements SortClicked {
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private WordAdapter adapter;
    private LearningViewModel learningViewModel;
    private List<FlashCardEntity> list;
    private WordInTopicAdapter wordInTopicAdapter;
    private RecyclerView recyclerViewInTopic;
    private LinearLayout sort;
    private long id;
    private MyBottomSheetDialog bottomSheetDialog;
    private List<FlashCardEntity> listRecyleView;

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

        viewPager = binding.viewPager;
        circleIndicator = binding.circleIndicator;

        mapping();
        setRecyleViewAndViewPager();
    }

    public void mapping(){
        recyclerViewInTopic = findViewById(R.id.recyleviewWord);
        recyclerViewInTopic.setLayoutManager(new LinearLayoutManager(this));
        Intent intent = getIntent();
        id = intent.getLongExtra("value", -1);
    }

    public void setRecyleViewAndViewPager(){
        learningViewModel.getWordByIdTopic(id).observe(this, new Observer<List<FlashCardEntity>>() {
            @Override
            public void onChanged(List<FlashCardEntity> flashCardEntities) {
                if (flashCardEntities != null && !flashCardEntities.isEmpty()) {
                    list = flashCardEntities;
                    listRecyleView = flashCardEntities;
                    adapter = new WordAdapter(LearningActivity.this, list);
                    viewPager.setAdapter(adapter);
                    circleIndicator.setViewPager(viewPager);
                    wordInTopicAdapter = new WordInTopicAdapter(listRecyleView);
                    recyclerViewInTopic.setAdapter(wordInTopicAdapter);
                } else {
                    Toast.makeText(LearningActivity.this, "No data available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        sortClicked();
    }

    public void sortClicked(){
        learningViewModel.getSortClicked().observe(this, clicked -> {
            if (clicked) {
                bottomDialog();
            }
        });
    }

    public void bottomDialog(){
        bottomSheetDialog = MyBottomSheetDialog.newInstance(this);
        bottomSheetDialog.show(getSupportFragmentManager(), "MyBottomSheetDialog");
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onNatureSortSelected() {
        bottomSheetDialog.dismiss();
    }

    @Override
    public void onAlphabetSortSelected() {
        learningViewModel.getWordByAplphabet(id).observe(this, sortedList -> {
            listRecyleView.clear();
            if (sortedList != null) {
                listRecyleView.addAll(sortedList);
            }
            wordInTopicAdapter.notifyDataSetChanged();
        });
        bottomSheetDialog.dismiss();
    }
}
