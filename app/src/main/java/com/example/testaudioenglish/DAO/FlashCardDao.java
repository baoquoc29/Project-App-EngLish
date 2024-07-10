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
    @Query("SELECT * FROM FlashCardEntity WHERE idTopic = :idTopic ORDER BY EnglishWord ASC")
    LiveData<List<FlashCardEntity>> getAllFlashCardSortByAlphabet(long idTopic);
    @Query("Update FlashCardEntity set Tick = 1 Where idTopic = :idTopic and id = :idWord")
    void updateTickFill(long idTopic,long idWord);
    @Query("Update FlashCardEntity set Tick = 0 Where idTopic = :idTopic and id = :idWord")
    void updateTickEmpty(long idTopic,long idWord);
    @Query("Select id from FlashCardEntity where EnglishWord = :engVer ")
    int idWord(String engVer);
}
