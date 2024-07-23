package com.example.testaudioenglish.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.testaudioenglish.Entity.FlashCardEntity;
import com.example.testaudioenglish.Repository.FlashCardRepository;

import java.util.ArrayList;
import java.util.List;

public class LearningViewModel extends AndroidViewModel {
    private FlashCardRepository repository;
    private MutableLiveData<Boolean> sortClicked = new MutableLiveData<>();
    private LiveData<List<FlashCardEntity>> listSortByAlphabet;
    private LiveData<List<FlashCardEntity>> list_flashCard;
    private MutableLiveData<String> countWord = new MutableLiveData<>();
    private MutableLiveData<String> title = new MutableLiveData<>();
    private MutableLiveData<String> username = new MutableLiveData<>();
    private MutableLiveData<Boolean> navigateToPairingCard = new MutableLiveData<>();
    private MutableLiveData<Boolean> navigateToMemoryCard = new MutableLiveData<>();
    private MutableLiveData<Boolean> navigateToMultipleChoice = new MutableLiveData<>();
    private MutableLiveData<Boolean> navigateToExam = new MutableLiveData<>();
    public MutableLiveData<Boolean> getSortClicked() {
        return sortClicked;
    }
    private MutableLiveData<Integer> countCheck = new MutableLiveData<>();

    public MutableLiveData<Boolean> getNavigateToExam() {
        return navigateToExam;
    }

    public void setNavigateToExam(MutableLiveData<Boolean> navigateToExam) {
        this.navigateToExam = navigateToExam;
    }

    public void setSortClicked(MutableLiveData<Boolean> sortClicked) {
        this.sortClicked = sortClicked;
    }
    public void switchToExam(){
        navigateToExam.setValue(true);
    }
    public MutableLiveData<Boolean> getNavigateToMultipleChoice() {
        return navigateToMultipleChoice;
    }

    public void setNavigateToMultipleChoice(MutableLiveData<Boolean> navigateToMultipleChoice) {
        this.navigateToMultipleChoice = navigateToMultipleChoice;
    }

    public void onButtonSwitchToMultiple(){
        navigateToMultipleChoice.setValue(true);
    }
    public void onButtonClick() {
        sortClicked.setValue(true);
    }

    public MutableLiveData<Boolean> getNavigateToMemoryCard() {
        return navigateToMemoryCard;
    }

    public void setNavigateToMemoryCard(MutableLiveData<Boolean> navigateToMemoryCard) {
        this.navigateToMemoryCard = navigateToMemoryCard;
    }
    public void clickToMemoryCard(){
        navigateToMemoryCard.setValue(true);
    }
    public LearningViewModel(@NonNull Application application) {
        super(application);
        repository = new FlashCardRepository(application);
        list_flashCard = new MutableLiveData<>(new ArrayList<>());
        listSortByAlphabet = new MutableLiveData<>(new ArrayList<>());
    }
    public void onClickNavigateToPairingCard(){
        navigateToPairingCard.setValue(true);
    }
    public MutableLiveData<Boolean> getNavigateToPairingCard() {
        return navigateToPairingCard;
    }

    public void setNavigateToPairingCard(MutableLiveData<Boolean> navigateToPairingCard) {
        this.navigateToPairingCard = navigateToPairingCard;
    }
    public LiveData<Integer> getCountCheck(long idTopic){
        return repository.countCheck(idTopic);
    }
    public LiveData<List<FlashCardEntity>> getWordByIdTopic(long id) {
        return repository.getAllFlashCardByTopic(id);
    }

    public LiveData<List<FlashCardEntity>> getWordByAplphabet(long id){
        return repository.getListSortByAlphabet(id);
    }

    public void updateTickedStatus(long id, long idWord, boolean isTicked) {
        if (isTicked) {
            repository.updateTickerClickedFull(id, idWord);
        } else {
            repository.updateTickerClickedEmpty(id, idWord);
        }
    }

    public LiveData<String> getCountWord() {
        return countWord;
    }

    public void setCountWord(String countWord) {
        this.countWord.setValue(countWord);
    }

    public LiveData<String> getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title.setValue(title);
    }

    public LiveData<String> getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username.setValue(username);
    }
}
