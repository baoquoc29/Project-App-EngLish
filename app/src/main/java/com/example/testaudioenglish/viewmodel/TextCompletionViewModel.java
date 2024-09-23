package com.example.testaudioenglish.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.testaudioenglish.Repository.ToeicFullTestRepository;
import com.example.testaudioenglish.Response.ReadingResponse;

public class TextCompletionViewModel extends AndroidViewModel {
    private ToeicFullTestRepository repository;
    private MutableLiveData<ReadingResponse> readingData;
    public TextCompletionViewModel(@NonNull Application application) {
        super(application);
        repository = new ToeicFullTestRepository();
    }
    private void loadListeningAllDataPart6(long id_quiz,String part) {
        repository.getAllDataPart6(id_quiz,part).observeForever(response -> {
            if (response != null) {
                readingData.setValue(response);
            } else {
                readingData.setValue(null);
            }
        });
    }
    public LiveData<ReadingResponse> getListeningAllDataPart6(long id_quiz, String part) {
        if (readingData == null) {
            readingData = new MutableLiveData<>();
            loadListeningAllDataPart6(id_quiz,part);
        }
        return readingData;
    }
}