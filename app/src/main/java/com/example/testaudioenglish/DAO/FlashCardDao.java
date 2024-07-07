package com.example.testaudioenglish.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.testaudioenglish.Entity.FlashCardEntity;
import com.example.testaudioenglish.Entity.TopicFlashCardEntity;

import java.util.List;


@Dao
public interface FlashCardDao {

    @Insert
    long insertFlashCard(FlashCardEntity flashCard);
    @Query("Select * from FLASHCARDENTITY where idTopic = :idTopic")
    LiveData<List<FlashCardEntity>> getAllFlashCardByTopic(long idTopic);

}
