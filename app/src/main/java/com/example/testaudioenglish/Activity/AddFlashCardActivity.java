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
import androidx.lifecycle.Observer;
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
import java.util.List;
import java.util.Locale;

public class AddFlashCardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AddFlashCardAdapter adapter;
    private ImageView addFlash;
    private FlashCardAddViewModel viewModel;
    private TextView textLesson;
    private ImageView checkFinish;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_flash_card);

        textLesson = findViewById(R.id.nameLesson);
        Toolbar toolbar = findViewById(R.id.toolbar);
        checkFinish = findViewById(R.id.checkFinish);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");

        addFlash = findViewById(R.id.add_flashcard);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");
        }

        viewModel = new ViewModelProvider(this).get(FlashCardAddViewModel.class);

        recyclerView = findViewById(R.id.recyflashcard);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new AddFlashCardAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        viewModel.getFlashCardList().observe(this, new Observer<List<WordFlashCard>>() {
            @Override
            public void onChanged(List<WordFlashCard> wordFlashCards) {
                adapter.setFlashCardList(wordFlashCards);
                adapter.notifyDataSetChanged();
            }
        });

        addFlash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewItem();
            }
        });

        // Add a default FlashCard if the list is empty
        if (viewModel.getFlashCardList().getValue() == null || viewModel.getFlashCardList().getValue().isEmpty()) {
            viewModel.addFlashCard(new WordFlashCard("", ""));
        }

        checkFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewModel.getFlashCardList().getValue().size() < 2){
                    Toast.makeText(AddFlashCardActivity.this, "Vui lòng nhập trên 2 thuật ngữ", Toast.LENGTH_SHORT).show();
                }
                else{
                    importToDatabase();
                }
            }
        });
    }

    private void addNewItem() {
        WordFlashCard newFlashCard = new WordFlashCard("", "");
        viewModel.addFlashCard(newFlashCard);
        if (viewModel.getFlashCardList().getValue() != null) {
            recyclerView.smoothScrollToPosition(viewModel.getFlashCardList().getValue().size() - 1);
        }
    }

    private String getDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date currentDate = new Date();
        return sdf.format(currentDate);
    }

    private void importToDatabase() {
        List<WordFlashCard> list = viewModel.getFlashCardList().getValue();
        if (list == null || list.isEmpty()) {
            Toast.makeText(this, "No flashcards to add", Toast.LENGTH_SHORT).show();
            return;
        }

        String username = getValueUserName();
        if (username.isEmpty()) {
            Toast.makeText(this, "Username is not set", Toast.LENGTH_SHORT).show();
            return;
        }

        String lessonName = textLesson.getText().toString();
        if (lessonName.isEmpty()) {
            Toast.makeText(this, "Lesson name is empty", Toast.LENGTH_SHORT).show();
            return;
        }

        long id = viewModel.insertTopic(new TopicFlashCardEntity(lessonName, getDate(), username));
        if (id == -1) {
            Toast.makeText(this, "Failed to add topic", Toast.LENGTH_SHORT).show();
            return;
        }

        for (WordFlashCard flashCard : list) {
            viewModel.insert(new FlashCardEntity(id, flashCard.getEngVer(), flashCard.getVietVer()));
        }

        Toast.makeText(this, "Added successfully", Toast.LENGTH_SHORT).show();
        onBackPressed();
    }

    public String getValueUserName() {
        SharedPreferences sharedPref = getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        return sharedPref.getString("username", ""); // Default value is an empty string if key 'username' is not found
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
