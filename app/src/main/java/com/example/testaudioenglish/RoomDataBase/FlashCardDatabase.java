package com.example.testaudioenglish.RoomDataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.testaudioenglish.DAO.FlashCardDao;
import com.example.testaudioenglish.DAO.TopicFlashCardDao;
import com.example.testaudioenglish.Entity.FlashCardEntity;
import com.example.testaudioenglish.Entity.TopicFlashCardEntity;

@Database(entities = {FlashCardEntity.class, TopicFlashCardEntity.class}, version = 2)
public abstract class FlashCardDatabase extends RoomDatabase {
    public abstract FlashCardDao flashCardDao();
    public abstract TopicFlashCardDao topicFlashCardDao();

    private static volatile FlashCardDatabase INSTANCE;

    public static FlashCardDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FlashCardDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    FlashCardDatabase.class, "flashcard_database")
                            .addCallback(new RoomDatabase.Callback() {
                                @Override
                                public void onOpen(@androidx.annotation.NonNull SupportSQLiteDatabase db) {
                                    super.onOpen(db);
                                    db.execSQL("PRAGMA foreign_keys=ON;");
                                }
                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
