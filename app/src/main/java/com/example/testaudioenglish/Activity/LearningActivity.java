package com.example.testaudioenglish.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.example.testaudioenglish.InterfaceAdapter.SortClicked;
import com.example.testaudioenglish.R;
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
    private TextView levelFinish;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLearningBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_learning);
        learningViewModel = new ViewModelProvider(this).get(LearningViewModel.class);
        binding.setViewModel(learningViewModel);
        binding.setLifecycleOwner(this);
        Log.d("LearningActivity", "onCreate called");

        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Luyện tập từ vựng");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        viewPager = binding.viewPager;
        circleIndicator = binding.circleIndicator;
        levelFinish = binding.levelFinish;
        mapping();
        setRecycleViewAndViewPager();
    }

    private void mapping() {
        recyclerViewInTopic = findViewById(R.id.recyleviewWord);
        recyclerViewInTopic.setLayoutManager(new LinearLayoutManager(this));
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getLongExtra("value", -1);
            totalWord = intent.getStringExtra("totalWord");
            name = intent.getStringExtra("name");
            title = intent.getStringExtra("title");
            learningViewModel.setTitle(title);
            learningViewModel.setUsername(name);
            learningViewModel.setCountWord(totalWord + " thuật ngữ");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("LearningActivity", "onResume called");
    }

    public void setTextLevelFinish(long idTopic) {
        learningViewModel.getCountCheck(idTopic).observe(this, integer -> {
            if (integer == 0) {
                levelFinish.setText("Chưa luyện tập học phần này");
            } else {
                double percentFinish = ((double) integer / list.size()) * 100;
                levelFinish.setText("Đã ghi nhớ " + String.format("%.0f", percentFinish) + "%");
            }
        });
    }

    private void setRecycleViewAndViewPager() {
        learningViewModel.getWordByIdTopic(id).observe(this, flashCardEntities -> {
            if (flashCardEntities != null && !flashCardEntities.isEmpty()) {
                list.clear();
                list.addAll(flashCardEntities);
                listRecyleView = flashCardEntities;

                if (adapter == null) {
                    adapter = new WordAdapter(LearningActivity.this, list);
                    viewPager.setAdapter(adapter);
                    circleIndicator.setViewPager(viewPager);
                }

                if (wordInTopicAdapter == null) {
                    wordInTopicAdapter = new WordInTopicAdapter(listRecyleView, learningViewModel);
                    recyclerViewInTopic.setAdapter(wordInTopicAdapter);
                } else {
                    wordInTopicAdapter.updateData(flashCardEntities);
                }

                setTextLevelFinish(id);
            } else {
                Toast.makeText(LearningActivity.this, "Danh sách trống", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        sortClicked();
        onClickToPairingCard();
        onClickToMemoryCard();
        onClickToSwitchToMultiple();
        onClickToSwitchToExam();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    private void showDeleteConfirmationDialog() {
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.deletedialoglayout, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);

        Button btnConfirm = dialogView.findViewById(R.id.btn_confirm);
        Button btnCancel = dialogView.findViewById(R.id.btn_cancel);

        AlertDialog dialog = builder.create();

        btnConfirm.setOnClickListener(v -> {
            learningViewModel.delete(id);
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            showDeleteConfirmationDialog();
            return true;
        }
        else if(item.getItemId() == R.id.edit){
            startAddFlashCardActivity("edit");
            return true;
        }
        else if(item.getItemId() == R.id.duplicate){
            startAddFlashCardActivity("duplicate");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void startAddFlashCardActivity(String status) {
        Intent intent = new Intent(this, AddFlashCardActivity.class);
        intent.putExtra("value", id);
        intent.putExtra("status", status);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

    private void sortClicked() {
        learningViewModel.getSortClicked().observe(this, clicked -> {
            if (clicked) {
                bottomDialog();
            }
        });
    }

    private void onClickToMemoryCard() {
        learningViewModel.getNavigateToMemoryCard().observe(this, clicked -> {
            if (clicked) {
                switchGameInLearning("Memory");
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
                switchGameInLearning("Pairing");
            }
        });
    }

    private void onClickToSwitchToMultiple() {
        learningViewModel.getNavigateToMultipleChoice().observe(this, clicked -> {
            if (clicked) {
                switchGameInLearning("Choice");
            }
        });
    }

    private void onClickToSwitchToExam() {
        learningViewModel.getNavigateToExam().observe(this, clicked -> {
            if (clicked) {
                switchGameInLearning("Exam");
            }
        });
    }

    public void switchGameInLearning(String game) {
        Intent intent = new Intent(this, ExamActivity.class);
        intent.putExtra("idTopic", id);
        intent.putExtra("game", game);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
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

    @Override
    public boolean onSupportNavigateUp() {
        sharedPreferences.edit().clear().apply();
        finish();
        return true;
    }
}
