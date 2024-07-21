package com.example.testaudioenglish.Repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.testaudioenglish.Entity.FlashCardEntity;
import com.example.testaudioenglish.DAO.FlashCardDao;
import com.example.testaudioenglish.RoomDataBase.FlashCardDatabase;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FlashCardRepository {
    private FlashCardDao flashCardDao;
    private LiveData<List<FlashCardEntity>> allFlashCards;
    private ExecutorService executorService;

    public FlashCardRepository(Application application) {
        FlashCardDatabase db = FlashCardDatabase.getDatabase(application);
        flashCardDao = db.flashCardDao();
        executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<FlashCardEntity>> getAllFlashCards() {
        return allFlashCards;
    }

    public LiveData<List<FlashCardEntity>> getAllFlashCardByTopic(long id) {
        return flashCardDao.getAllFlashCardByTopic(id);
    }
    public LiveData<List<FlashCardEntity>> getAllFlashCardByTopicAndCheck(long id) {
        return flashCardDao.getAllFlashCardByTopicAndCheck(id);
    }

    public LiveData<List<FlashCardEntity>> getListSortByAlphabet(long id) {
        return flashCardDao.getAllFlashCardSortByAlphabet(id);
    }

    public LiveData<Integer> countCheck(long idTopic) {
        MutableLiveData<Integer> count = new MutableLiveData<>();
        executorService.execute(() -> count.postValue(flashCardDao.countCheck(idTopic)));
        return count;
    }
    public LiveData<Integer> countCheckZero(long idTopic) {
        MutableLiveData<Integer> count = new MutableLiveData<>();
        executorService.execute(() -> count.postValue(flashCardDao.countCheckZero(idTopic)));
        return count;
    }

    public void insert(FlashCardEntity flashCard) {
        executorService.execute(() -> flashCardDao.insertFlashCard(flashCard));
    }

    public void updateTickerClickedEmpty(long id, long word) {
        executorService.execute(() -> flashCardDao.updateTickEmpty(id, word));
    }

    public void updateCheck(long id, long word) {
        executorService.execute(() -> flashCardDao.updateCheck(id, word));
    }

    public void updateResetCheck(long id) {
        executorService.execute(() -> flashCardDao.updateResetCheck(id));
    }
    public LiveData<List<String>> listAnswer(String engVer){
        LiveData<List<String>> list = new MutableLiveData<>();
         executorService.execute(() -> ((MutableLiveData<List<String>>) list).postValue(flashCardDao.listRandomAnswers(engVer)));
         return list;
    }

    public void updateTickerClickedFull(long id, long word) {
        executorService.execute(() -> flashCardDao.updateTickFill(id, word));
    }
}
