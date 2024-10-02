package com.example.testaudioenglish.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testaudioenglish.Adapter.LanguageAdapter;
import com.example.testaudioenglish.ApiService.RapidAPI;
import com.example.testaudioenglish.Model.HistoryModel;
import com.example.testaudioenglish.Model.LanguageDetails;
import com.example.testaudioenglish.Model.Pronunciations;
import com.example.testaudioenglish.Model.Translations;
import com.example.testaudioenglish.R;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchDetailsActivity extends AppCompatActivity {
    private TextView textViewVocabulary, textViewPronunciation, textViewUk, textViewNoun, textViewUs;
    private ImageView imageViewLound1, imageViewLound2;
    private RecyclerView recyclerView;
    private TextView verb, noun, adj;
    private List<String> partOfSpeechList = new ArrayList<>();
    private LanguageAdapter adapter;
    private TextToSpeech textToSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_details);
        initializeViews();
        setupToolbar();

        Intent intent = getIntent();
        String text = intent.getStringExtra("search_query");

        fetchLanguageDetails(text);

        imageViewLound1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initializeTextToSpeech(Locale.UK, text);
            }
        });

        imageViewLound2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initializeTextToSpeech(Locale.US, text);
            }
        });
    }

    private void initializeTextToSpeech(Locale locale, String text) {
        if (textToSpeech != null) {
            textToSpeech.shutdown();
        }

        textToSpeech = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = textToSpeech.setLanguage(locale);
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("TTS", "Ngôn ngữ không được hỗ trợ hoặc thiếu dữ liệu");
                } else {
                    textToSpeech.setSpeechRate(1.0f);
                    speakText(text);
                }
            } else {
                Log.e("TTS", "Khởi tạo TTS thất bại");
            }
        });
    }

    public void speakText(String text) {
        if (textToSpeech != null && text != null && !text.isEmpty()) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else {
            Log.e("TTS", "Văn bản rỗng hoặc TTS chưa sẵn sàng");
        }
    }

    private void initializeViews() {
        textViewVocabulary = findViewById(R.id.text_view_vocabulary);
        textViewPronunciation = findViewById(R.id.text_view_pronunciation);
        imageViewLound1 = findViewById(R.id.image_view_lound_1);
        textViewUk = findViewById(R.id.text_view_uk);
        textViewNoun = findViewById(R.id.text_view_noun);
        imageViewLound2 = findViewById(R.id.image_view_lound_2);
        textViewUs = findViewById(R.id.text_view_us);
        recyclerView = findViewById(R.id.recycler_view);
        noun = findViewById(R.id.noun);
        verb = findViewById(R.id.verb);
        adj = findViewById(R.id.adj);
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

    private void fetchLanguageDetails(String text) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://ai-translate.p.rapidapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RapidAPI rapidAPI = retrofit.create(RapidAPI.class);
        Call<LanguageDetails> call = rapidAPI.getLanguageDetails(text, "vi");
        call.enqueue(new Callback<LanguageDetails>() {
            @Override
            public void onResponse(Call<LanguageDetails> call, Response<LanguageDetails> response) {
                if (response.isSuccessful() && response.body() != null) {
                    handleApiResponse(response.body());
                }
            }

            @Override
            public void onFailure(Call<LanguageDetails> call, Throwable t) {
                Log.e("API", "Lỗi kết nối API: " + t.getMessage());
            }
        });
    }

    private void handleApiResponse(LanguageDetails languageDetails) {
        List<Pronunciations> pronunciationsList = languageDetails.getPronunciations();
        List<Translations> translationsList = languageDetails.getTranslations();
        if(pronunciationsList != null || translationsList !=null){
            textViewVocabulary.setText(languageDetails.getWord());
            textViewPronunciation.setText(pronunciationsList.get(0).getPronunciation());
            getSupportActionBar().setTitle(languageDetails.getWord());
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new LanguageAdapter(translationsList);
            recyclerView.setAdapter(adapter);
            populatePartOfSpeechList(translationsList);
            updateUiBasedOnPartOfSpeech();
            addVocabulary(languageDetails.getWord(),pronunciationsList.get(0).getPronunciation(),translationsList.get(0).getTranslation());
        }
    }

    private void populatePartOfSpeechList(List<Translations> translationsList) {
        for (Translations translation : translationsList) {
            if (!partOfSpeechList.contains(translation.getPosTag())) {
                partOfSpeechList.add(translation.getPosTag());
            }
        }
    }

    private void updateUiBasedOnPartOfSpeech() {
        if (!partOfSpeechList.contains("NOUN")) {
            noun.setVisibility(View.GONE);
        }

        if (!partOfSpeechList.contains("ADJ")) {
            adj.setVisibility(View.GONE);
        }

        if (!partOfSpeechList.contains("VERB")) {
            verb.setVisibility(View.GONE);
        }

        textViewNoun.setText(getPartOfSpeechTranslation("NOUN"));
    }

    private String getPartOfSpeechTranslation(String posTag) {
        switch (posTag) {
            case "NOUN":
                return "Danh từ";
            case "VERB":
                return "Động từ";
            case "ADJ":
                return "Tính từ";
            default:
                return "";
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
    private void addVocabulary(String word, String pronunciation, String meaning) {
        List<HistoryModel> vocabularyList = loadVocabularyList();
        HistoryModel newEntry = new HistoryModel(word, pronunciation, meaning);
        if(!vocabularyList.contains(newEntry)){
            vocabularyList.add(newEntry);
        }
        saveVocabularyList(vocabularyList);
    }

    private List<HistoryModel> loadVocabularyList() {
        SharedPreferences sharedPreferences = getSharedPreferences("VocabularyPrefs", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("vocabulary_list", null);

        Type type = new TypeToken<ArrayList<HistoryModel>>() {}.getType();
        List<HistoryModel> vocabularyList = gson.fromJson(json, type);

        if (vocabularyList == null) {
            vocabularyList = new ArrayList<>();
        }

        return vocabularyList;
    }

    private void saveVocabularyList(List<HistoryModel> vocabularyList) {
        SharedPreferences sharedPreferences = getSharedPreferences("VocabularyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(vocabularyList);
        editor.putString("vocabulary_list", json);

        editor.apply();
    }
    @Override
    protected void onDestroy() {
        // Giải phóng tài nguyên TextToSpeech
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }
}
