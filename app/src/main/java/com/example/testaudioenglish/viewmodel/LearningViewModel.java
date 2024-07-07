package com.example.testaudioenglish.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.testaudioenglish.Entity.FlashCardEntity;
import com.example.testaudioenglish.Entity.TopicFlashCardEntity;
import com.example.testaudioenglish.Repository.FlashCardRepository;

import java.util.ArrayList;
import java.util.List;

public class LearningViewModel extends AndroidViewModel {
    private FlashCardRepository repository;
    private LiveData<List<FlashCardEntity>> list_flashCard;

    public LearningViewModel(@NonNull Application application) {
        super(application);
        repository = new FlashCardRepository(application);
        list_flashCard = new MutableLiveData<>(new ArrayList<>());
    }

    public LiveData<List<FlashCardEntity>> getWordByIdTopic(long id) {
        return repository.getAllFlashCardByTopic(id);
    }
}
