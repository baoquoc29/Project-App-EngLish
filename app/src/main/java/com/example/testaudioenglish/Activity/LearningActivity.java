package com.example.testaudioenglish.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import com.example.testaudioenglish.View.CardPairingFragment;
import com.example.testaudioenglish.databinding.ActivityLearningBinding;
import com.example.testaudioenglish.viewmodel.LearningViewModel;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class LearningActivity extends AppCompatActivity implements SortClicked {
    private ViewPager viewPager;
    private CircleIndicator circleIndicator;
    private WordAdapter adapter;
    private LearningViewModel learningViewModel;
    private List<FlashCardEntity> list = new ArrayList<>();
    private WordInTopicAdapter wordInTopicAdapter;
    private RecyclerView recyclerViewInTopic;
    private LinearLayout sort;
    private long id;
    private MyBottomSheetDialog bottomSheetDialog;
    private List<FlashCardEntity> listRecyleView;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "SortPreferences";
    private static final String SORT_KEY = "sort_key";
    private int position = -1;
    private int clicked = 0;
    private String name;
    private String totalWord;
    private String title;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLearningBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_learning);
        learningViewModel = new ViewModelProvider(this).get(LearningViewModel.class);
        binding.setViewModel(learningViewModel);
        binding.setLifecycleOwner(this);

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
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

    private void mapping() {
        recyclerViewInTopic = findViewById(R.id.recyleviewWord);
        recyclerViewInTopic.setLayoutManager(new LinearLayoutManager(this));
         intent = getIntent();
        if (intent != null) {
            id = intent.getLongExtra("value", -1);
            totalWord = intent.getStringExtra("totalWord");
            name = intent.getStringExtra("name");
            title  = intent.getStringExtra("title");
            learningViewModel.setTitle(title);
            learningViewModel.setUsername(name);
            learningViewModel.setCountWord(totalWord + " thuật ngữ");

        }
    }

    private void setRecyleViewAndViewPager() {
        learningViewModel.getWordByIdTopic(id).observe(this, new Observer<List<FlashCardEntity>>() {
            @Override
            public void onChanged(List<FlashCardEntity> flashCardEntities) {
                if (flashCardEntities != null && !flashCardEntities.isEmpty()) {
                    list.clear();
                    list.addAll(flashCardEntities);
                    listRecyleView = flashCardEntities;
                    adapter = new WordAdapter(LearningActivity.this, list);
                    viewPager.setAdapter(adapter);
                    circleIndicator.setViewPager(viewPager);
                    wordInTopicAdapter = new WordInTopicAdapter(listRecyleView, learningViewModel);
                    recyclerViewInTopic.setAdapter(wordInTopicAdapter);
                } else {
                    Toast.makeText(LearningActivity.this, "No data available", Toast.LENGTH_SHORT).show();
                }
            }
        });
        sortClicked();
        onClickToPairingCard(); // Gọi phương thức để xử lý chuyển Fragment
    }

    private void sortClicked() {
        learningViewModel.getSortClicked().observe(this, clicked -> {
            if (clicked) {
                bottomDialog();
            }
        });
    }

    private void bottomDialog() {
        if (bottomSheetDialog == null) {
            bottomSheetDialog = MyBottomSheetDialog.newInstance(this);
        }
        bottomSheetDialog.show(getSupportFragmentManager(), "MyBottomSheetDialog");
    }

    private void onClickToPairingCard() {
        learningViewModel.getNavigateToPairingCard().observe(this, clicked -> {
            if (clicked) {
                switchToNewFragment(); // Gọi phương thức để chuyển Fragment khi cần thiết
            }
        });
    }

    private void switchToNewFragment() {
//        CardPairingFragment fragment = new CardPairingFragment();
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.container,fragment).commit();
        Intent intent = new Intent(this, ExamActivity.class);
        intent.putExtra("idTopic",id);
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        sharedPreferences.edit().clear().apply();

        onBackPressed();
        return true;
    }

    @Override
    public void onNatureSortSelected() {
        learningViewModel.getWordByIdTopic(id).observe(this, listNature -> {
            listRecyleView.clear();
            if (listNature != null) {
                listRecyleView.addAll(listNature);
            }
            wordInTopicAdapter.notifyDataSetChanged();
        });
        if (bottomSheetDialog != null) {
            bottomSheetDialog.dismiss();
        }
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

        if (bottomSheetDialog != null) {
            bottomSheetDialog.dismiss();
        }
    }
}
