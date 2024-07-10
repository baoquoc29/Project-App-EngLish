package com.example.testaudioenglish.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.testaudioenglish.Entity.TopicFlashCardEntity;
import com.example.testaudioenglish.Navigation.Event;
import com.example.testaudioenglish.Repository.TopicFlashCardRepository;

import java.util.List;

public class VocabularyFragmentViewModel extends AndroidViewModel {
    public MutableLiveData<Event<Boolean>> navigateToAddFlashCard = new MutableLiveData<>();
    public MutableLiveData<Event<Boolean>> navigateToTopic = new MutableLiveData<>();
    private TopicFlashCardRepository repository;
    private LiveData<List<TopicFlashCardEntity>> allFlashCards;

    public VocabularyFragmentViewModel(@NonNull Application application) {
        super(application);
        repository = new TopicFlashCardRepository(application);
    }
    public LiveData<List<TopicFlashCardEntity>> getFlashCardsByUsername(String username) {
        allFlashCards = repository.getFlashCardsByUsername(username);
        return allFlashCards;
    }
    public LiveData<Long> getCount(long id){
        return repository.getCount(id);
    }
    public LiveData<List<TopicFlashCardEntity>> getFlashCardsByTitle(String title) {
        return repository.getFlashCardsByTitle(title);
    }

    public void navigateToAdd(){
        navigateToAddFlashCard.setValue(new Event<>(true));
    }
    public void navigateToTopic(){
        navigateToTopic.setValue(new Event<>(true));
    }
}
