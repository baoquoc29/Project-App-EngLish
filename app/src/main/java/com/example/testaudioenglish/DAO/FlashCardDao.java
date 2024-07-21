package com.example.testaudioenglish.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.testaudioenglish.Entity.FlashCardEntity;

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
    @Query("Select count(*) from FlashCardEntity where idTopic =:idTopic and `Check` = 1")
    int countCheck(long idTopic);
    @Query("Select count(*) from FlashCardEntity where idTopic =:idTopic and `Check` = 0")
    int countCheckZero(long idTopic);
    @Query("UPDATE FlashCardEntity SET `Check` = 1 WHERE idTopic = :idTopic AND id = :idWord")
    void updateCheck(long idTopic, long idWord);

    @Query("Select * from FLASHCARDENTITY where idTopic = :idTopic and `Check` = 0")
    LiveData<List<FlashCardEntity>> getAllFlashCardByTopicAndCheck(long idTopic);

    @Query("UPDATE FlashCardEntity SET `Check` = 0 WHERE idTopic = :idTopic")
    void updateResetCheck(long idTopic);

    @Query("SELECT VietnameseWord FROM FLASHCARDENTITY WHERE EnglishWord !=:engWord ORDER BY RANDOM() LIMIT 3  ")
    List<String> listRandomAnswers(String engWord);
}
