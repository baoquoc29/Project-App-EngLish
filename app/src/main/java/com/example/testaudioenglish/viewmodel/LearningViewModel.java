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
    private MutableLiveData<Boolean> sortClicked = new MutableLiveData<>();
    private LiveData<List<FlashCardEntity>> listSortByAlphabet;
    private LiveData<List<FlashCardEntity>> list_flashCard;

    public MutableLiveData<Boolean> getSortClicked() {
        return sortClicked;
    }

    public void setSortClicked(MutableLiveData<Boolean> sortClicked) {
        this.sortClicked = sortClicked;
    }
    public void onButtonClick() {
        sortClicked.setValue(true);
    }

    public LearningViewModel(@NonNull Application application) {
        super(application);
        repository = new FlashCardRepository(application);
        list_flashCard = new MutableLiveData<>(new ArrayList<>());
        listSortByAlphabet = new MutableLiveData<>(new ArrayList<>());
    }

    public LiveData<List<FlashCardEntity>> getWordByIdTopic(long id) {
        return repository.getAllFlashCardByTopic(id);
    }
    public LiveData<List<FlashCardEntity>> getWordByAplphabet(long id){
        return repository.getListSortByAplphabet(id);
    }
}
