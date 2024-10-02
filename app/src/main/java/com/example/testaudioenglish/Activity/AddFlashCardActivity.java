package com.example.testaudioenglish.Activity;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testaudioenglish.Adapter.AddFlashCardAdapter;
import com.example.testaudioenglish.Custom.SwipeToDeleteCallback;
import com.example.testaudioenglish.Entity.FlashCardEntity;
import com.example.testaudioenglish.Entity.TopicFlashCardEntity;
import com.example.testaudioenglish.Model.WordFlashCard;
import com.example.testaudioenglish.R;
import com.example.testaudioenglish.viewmodel.FlashCardAddViewModel;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import java.io.IOException;
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
    private LinearLayout layoutScanner;
    private static final int PICK_IMAGE_REQUEST = 1;
    private List<String> listEnglish = new ArrayList<>();
    private List<String> listVietNamese = new ArrayList<>();
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
        layoutScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
    }


    private void initViews() {
        textLesson = findViewById(R.id.nameLesson);
        recyclerView = findViewById(R.id.recyflashcard);
        addFlash = findViewById(R.id.add_flashcard);
        layoutScanner = findViewById(R.id.layoutScanner);
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
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter, this));
        itemTouchHelper.attachToRecyclerView(recyclerView);
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
    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            processImage(imageUri);
        }
    }

    private void processImage(Uri imageUri) {
        try {
            InputImage inputImage = InputImage.fromFilePath(this, imageUri);

            // Nhận diện văn bản với ML Kit
            TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
                    .process(inputImage)
                    .addOnSuccessListener(text -> {
                        // Xử lý văn bản nhận diện được
                        processTextRecognitionResult(text);
                    })
                    .addOnFailureListener(e -> {
                        Log.e("MLKit", "Text recognition failed: " + e.getMessage());
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processTextRecognitionResult(Text texts) {
        listEnglish.clear();
        listVietNamese.clear();

        for (Text.TextBlock block : texts.getTextBlocks()) {
            List<Text.Line> lines = block.getLines();

            for (Text.Line line : lines) {
                String lineText = line.getText().toString().trim();
                if (lineText.contains(":")) {
                    String[] parts = lineText.split("[:,.;]");

                    if (parts.length > 1) {
                        String engText = parts[0].trim();
                        String vietText = parts[1].trim();

                        if (!listVietNamese.contains(vietText)) {
                            listVietNamese.add(vietText);
                        }
                        if (!listEnglish.contains(engText)) {
                            listEnglish.add(engText);
                        }
                    }
                }
            }
        }

        list.clear();
        for (int i = 0; i < listVietNamese.size(); i++) {
            list.add(new WordFlashCard(listEnglish.get(i), listVietNamese.get(i)));
        }
        if(list == null){
            Toast.makeText(this, "Không nhận diện được hình ảnh!", Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
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
