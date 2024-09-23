package com.example.testaudioenglish.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testaudioenglish.Adapter.AddFlashCardAdapter;
import com.example.testaudioenglish.Entity.FlashCardEntity;
import com.example.testaudioenglish.Entity.TopicFlashCardEntity;
import com.example.testaudioenglish.Model.WordFlashCard;
import com.example.testaudioenglish.R;
import com.example.testaudioenglish.viewmodel.FlashCardAddViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class AddFlashCardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AddFlashCardAdapter adapter;
    private ImageView addFlash;
    private FlashCardAddViewModel viewModel;
    private TextView textLesson;
    private ImageView checkFinish;
    private List<WordFlashCard> list = new ArrayList<>();
    private long idTopic;
    private String statusEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flash_card);

        initViews();
        setupToolbar();
        handleIntentData();
        setupRecyclerView();

        addFlash.setOnClickListener(view -> addNewItem());
        checkFinish.setOnClickListener(view -> validateAndImport());
    }

    private void initViews() {
        textLesson = findViewById(R.id.nameLesson);
        recyclerView = findViewById(R.id.recyflashcard);
        addFlash = findViewById(R.id.add_flashcard);
        checkFinish = findViewById(R.id.checkFinish);
        viewModel = new ViewModelProvider(this).get(FlashCardAddViewModel.class);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");
        }
    }

    private void handleIntentData() {
        Intent intent = getIntent();
        idTopic = intent.getLongExtra("value", -1);
        statusEdit = intent.getStringExtra("status");

        if (idTopic == -1 && statusEdit == null) {
            list.add(new WordFlashCard("", ""));
        } else {
            setRecyclerViewData();
        }
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AddFlashCardAdapter(list);
        recyclerView.setAdapter(adapter);
    }

    private void setRecyclerViewData() {
        viewModel.getTitleTopic(idTopic).observe(this, title -> textLesson.setText(title));
        viewModel.getWordByIdTopic(idTopic).observe(this, flashCardEntities -> {
            for (FlashCardEntity flashCardEntity : flashCardEntities) {
                list.add(new WordFlashCard(flashCardEntity.getEnglishWord(), flashCardEntity.getVietWord()));
            }
            adapter.notifyDataSetChanged();
        });
    }

    private void addNewItem() {
        list.add(new WordFlashCard("", ""));
        adapter.notifyItemInserted(list.size() - 1);
        recyclerView.smoothScrollToPosition(list.size() - 1);
    }

    private void validateAndImport() {
        if (list.size() < 2) {
            showToast("Vui lòng nhập trên 2 thuật ngữ");
        } else if (hasDuplicateEnglishWords(list)) {
            showToast("Thuật ngữ tiếng Anh không được trùng nhau");
        } else {
            importToDatabase(1);

        }
    }

    private boolean hasDuplicateEnglishWords(List<WordFlashCard> flashCardList) {
        Set<String> englishWords = new HashSet<>();
        for (WordFlashCard flashCard : flashCardList) {
            if (!englishWords.add(flashCard.getEngVer())) {
                return true;
            }
        }
        return false;
    }

    private void importToDatabase(int status) {
        if (list.isEmpty()) {
            showToast("Không có thẻ ghi chú để thêm");
            return;
        }

        String username = getValueUserName();
        if (username.isEmpty()) {
            showToast("Tên người dùng chưa được thiết lập");
            return;
        }

        String lessonName = textLesson.getText().toString();
        if (lessonName.isEmpty()) {
            showToast("Tên bài học trống");
            return;
        }

        if (idTopic == -1 || "duplicate".equals(statusEdit)) {
            handleNewTopic(lessonName, status, username);
        } else {
            updateExistingTopic(status);
        }
        finish();
    }

    private void handleNewTopic(String lessonName, int status, String username) {
        long newIdTopic = viewModel.insertTopic(new TopicFlashCardEntity(lessonName, getDate(), username, status));
        if (newIdTopic == -1) {
            showToast("Thêm chủ đề thất bại vì tên bài tồn tại");
            return;
        }
        for (WordFlashCard flashCard : list) {
            if (flashCard.getEngVer().isEmpty()) {
                showToast("Từ tiếng Anh không được để trống");
                return;
            }
            viewModel.insert(new FlashCardEntity(newIdTopic, flashCard.getEngVer(), flashCard.getVietVer()));
        }
        showToast(status == 1 ? "Thêm thành công" : "Đã lưu vào bản nháp");
        if (status == 1) finish();
    }

    private void updateExistingTopic(int status) {
        viewModel.updateStatus(idTopic, status);
        viewModel.deleteAllFlashCardsByTopic(idTopic);
        for (WordFlashCard flashCard : list) {
            viewModel.insert(new FlashCardEntity(idTopic, flashCard.getEngVer(), flashCard.getVietVer()));
        }
        showToast(status == 1 ? "Thêm thành công" : "Đã lưu vào bản nháp");
        if (status == 1){
           finish();
        }
    }

    private String getDate() {
        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
    }

    public String getValueUserName() {
        SharedPreferences sharedPref = getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        return sharedPref.getString("username", "");
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (statusEdit == null) {
            importToDatabase(0);
        }
        finish();
        return true;
    }
}
