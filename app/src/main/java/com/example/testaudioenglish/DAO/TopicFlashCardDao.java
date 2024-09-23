package com.example.testaudioenglish.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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
    @Query("SELECT * FROM TopicFlashCardEntity WHERE title LIKE '%' || :title || '%' and username =:username")
    LiveData<List<TopicFlashCardEntity>> getFiller(String title,String username);

    @Query("SELECT title from TOPICFLASHCARDENTITY where id =:idTopic")
    LiveData<String> getTitle(long idTopic);

    @Query("Select 1 from TopicFlashCardEntity where title =:title ")
    LiveData<Integer> checkTitle(String title);
    @Query("UPDATE TopicFlashCardEntity SET status = :newStatus WHERE id = :idTopic")
    void updateTopicStatus(long idTopic, int newStatus); // Change int to your actual status type

    @Query("DELETE from TopicFlashCardEntity where id = :idTopic")
    void deleteTopic(long idTopic);
}
