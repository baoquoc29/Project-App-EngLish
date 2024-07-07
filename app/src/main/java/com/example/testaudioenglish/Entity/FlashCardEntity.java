package com.example.testaudioenglish.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(foreignKeys = @ForeignKey(entity = TopicFlashCardEntity.class,
        parentColumns = "id",
        childColumns = "idTopic",
        onDelete = ForeignKey.CASCADE))
public class FlashCardEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "EnglishWord")
    public String englishWord;

    @ColumnInfo(name = "idTopic")
    public long idTopic;

    @ColumnInfo(name = "VietnameseWord")
    public String vietWord;

    public FlashCardEntity(long idTopic, String englishWord, String vietWord) {
        this.idTopic = idTopic;
        this.englishWord = englishWord;
        this.vietWord = vietWord;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEnglishWord() {
        return englishWord;
    }

    public void setEnglishWord(String englishWord) {
        this.englishWord = englishWord;
    }

    public long getIdTopic() {
        return idTopic;
    }

    public void setIdTopic(long idTopic) {
        this.idTopic = idTopic;
    }

    public String getVietWord() {
        return vietWord;
    }

    public void setVietWord(String vietWord) {
        this.vietWord = vietWord;
    }
}
