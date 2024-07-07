package com.example.testaudioenglish.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.testaudioenglish.Entity.TopicFlashCardEntity;

import java.util.List;

@Dao
public interface TopicFlashCardDao {
    @Insert
    public long insertTopic(TopicFlashCardEntity topicFlashCardEntity);
    @Query("SELECT * FROM TopicFlashCardEntity WHERE username = :username")
    LiveData<List<TopicFlashCardEntity>> getFlashCardsByUsername(String username);
    @Query("Select count(*) from " +
            "FlashCardEntity where idTopic = :idTopic")
    LiveData<Long> getCountWordInTopic(long idTopic);
    @Query("SELECT * FROM TopicFlashCardEntity WHERE title LIKE '%' || :title || '%'")
    LiveData<List<TopicFlashCardEntity>> getFiller(String title);

}
