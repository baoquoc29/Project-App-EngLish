package com.example.testaudioenglish.viewmodel;
import android.app.Application;

import com.example.testaudioenglish.Entity.FlashCardEntity;
import com.example.testaudioenglish.Entity.TopicFlashCardEntity;
import com.example.testaudioenglish.Model.WordFlashCard;
import com.example.testaudioenglish.Repository.FlashCardRepository;
import com.example.testaudioenglish.Repository.TopicFlashCardRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class FlashCardAddViewModel extends AndroidViewModel {
    private final MutableLiveData<List<WordFlashCard>> flashCardList;
    private FlashCardRepository repository;
    private TopicFlashCardRepository topicFlashCardRepository;
    public FlashCardAddViewModel(@NonNull Application application) {
        super(application);
        repository = new FlashCardRepository(application);
        flashCardList = new MutableLiveData<>(new ArrayList<>());
        topicFlashCardRepository = new TopicFlashCardRepository(application);
    }


    public LiveData<List<WordFlashCard>> getFlashCardList() {
        return flashCardList;
    }

    public void addFlashCard(WordFlashCard flashCard) {
        List<WordFlashCard> currentList = flashCardList.getValue();
        if (currentList != null) {
            currentList.add(flashCard);
            flashCardList.setValue(currentList);
        }
    }
    public void updateFlashCard(int position, WordFlashCard updatedFlashCard) {
        List<WordFlashCard> currentList = flashCardList.getValue();
        if (currentList != null && position >= 0 && position < currentList.size()) {
            currentList.set(position, updatedFlashCard);
            flashCardList.setValue(currentList);
        }
    }
    public  long insertTopic(TopicFlashCardEntity topicFlashCardEntity){
        return topicFlashCardRepository.insert(topicFlashCardEntity);
    }
    public void insert(FlashCardEntity flashCard) {
        repository.insert(flashCard);
    }
}
