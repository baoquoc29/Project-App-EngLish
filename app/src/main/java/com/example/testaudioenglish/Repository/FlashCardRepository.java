package com.example.testaudioenglish.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

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
    public LiveData<List<FlashCardEntity>> getAllFlashCardByTopic(long id){
        return flashCardDao.getAllFlashCardByTopic(id);
    }
    public LiveData<List<FlashCardEntity>> getListSortByAplphabet(long id){
        return flashCardDao.getAllFlashCardSortByAlphabet(id);
    }
    public void insert(FlashCardEntity flashCard) {
        executorService.execute(() -> flashCardDao.insertFlashCard(flashCard));
    }
}
