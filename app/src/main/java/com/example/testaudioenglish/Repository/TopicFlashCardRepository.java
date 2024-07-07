package com.example.testaudioenglish.Repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.testaudioenglish.DAO.FlashCardDao;
import com.example.testaudioenglish.DAO.TopicFlashCardDao;
import com.example.testaudioenglish.Entity.FlashCardEntity;
import com.example.testaudioenglish.Entity.TopicFlashCardEntity;
import com.example.testaudioenglish.RoomDataBase.FlashCardDatabase;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class TopicFlashCardRepository {
    private TopicFlashCardDao topicFlashCardDao;
    private LiveData<List<FlashCardEntity>> allTopic;
    private ExecutorService executorService;

    public TopicFlashCardRepository(Application application) {
        FlashCardDatabase db = FlashCardDatabase.getDatabase(application);
        topicFlashCardDao = db.topicFlashCardDao();
        executorService = Executors.newSingleThreadExecutor();
    }


    public LiveData<List<TopicFlashCardEntity>> getFlashCardsByUsername(String username) {
        return topicFlashCardDao.getFlashCardsByUsername(username);
    }
    public LiveData<List<TopicFlashCardEntity>> getFlashCardsByTitle(String title) {
        return topicFlashCardDao.getFiller(title);
    }
    public long insert(TopicFlashCardEntity flashCard) {
        Callable<Long> insertCallable = () -> topicFlashCardDao.insertTopic(flashCard);
        Future<Long> future = executorService.submit(insertCallable);
        try {
            return future.get(); // This will block until the result is available
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
    public LiveData<Long> getCount(long id){
        return topicFlashCardDao.getCountWordInTopic(id);
    }
}
