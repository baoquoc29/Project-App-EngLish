package com.example.testaudioenglish.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.testaudioenglish.Entity.FlashCardEntity;
import com.example.testaudioenglish.Entity.TopicFlashCardEntity;
import com.example.testaudioenglish.Repository.FlashCardRepository;
import com.example.testaudioenglish.Repository.TopicFlashCardRepository;

import java.util.List;

public class ExamViewModel extends AndroidViewModel {
    private FlashCardRepository repository;
    private TopicFlashCardRepository topicFlashCardRepository;
    private MutableLiveData<Boolean> buttonClick = new MutableLiveData<>(false);
    private MutableLiveData<Boolean> button_turn_back = new MutableLiveData<>(false);

    public MutableLiveData<Boolean> getButton_turn_back() {
        return button_turn_back;
    }

    public MutableLiveData<Boolean> getButton_exam_again() {
        return button_exam_again;
    }
    public void clickTurnBack(){
        button_turn_back.setValue(true);
    }
    public void clickExamAgain(){
        button_exam_again.setValue(true);
    }
    private MutableLiveData<Boolean> button_exam_again = new MutableLiveData<>(false);
    public LiveData<Boolean> getButtonClick() {
        return buttonClick;
    }

    public ExamViewModel(@NonNull Application application) {
        super(application);
        repository = new FlashCardRepository(application);
        topicFlashCardRepository = new TopicFlashCardRepository(application);
    }

    public LiveData<String> getTitleTopic(long idTopic){
        return topicFlashCardRepository.getTitleById(idTopic);
    }

    public LiveData<Long> getCountWord(long idTopic){
        return topicFlashCardRepository.getCount(idTopic);
    }

    public void clickToExam(){
        buttonClick.setValue(true);
    }

    public LiveData<List<FlashCardEntity>> getList(long id, int limit){
        return repository.getListLimit(id, limit);
    }

    public LiveData<List<String>> getListAnswer(String engver){
        return repository.listAnswer(engver);
    }

    public LiveData<List<String>> getListAnswerInATopic(long idTopic){
        return repository.listAnswerInTopic(idTopic);
    }
}
