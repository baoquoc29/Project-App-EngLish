package com.example.testaudioenglish.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class TopicFlashCardEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;
    @ColumnInfo(name = "title")
    public  String title;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @ColumnInfo(name = "date")
    public String date;
    @ColumnInfo(name = "username")
    public String username;

    public TopicFlashCardEntity(String title, String date, String username) {
        this.title = title;
        this.date = date;
        this.username = username;
    }
}
