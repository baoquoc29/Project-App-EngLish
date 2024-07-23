package com.example.testaudioenglish.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.testaudioenglish.Repository.FlashCardRepository;
import com.example.testaudioenglish.Repository.TopicFlashCardRepository;

public class ExamViewModel extends AndroidViewModel {
    private FlashCardRepository repository;
    private TopicFlashCardRepository topicFlashCardRepository;
    public ExamViewModel(@NonNull Application application) {
        super(application);
        repository = new FlashCardRepository(application);
        topicFlashCardRepository = new TopicFlashCardRepository(application);
    }
    public LiveData<String> getTitleTopic(long idTopic){
        return topicFlashCardRepository.getTitleById(idTopic);
    }

}