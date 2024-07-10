package com.example.testaudioenglish.RoomDataBase;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.testaudioenglish.DAO.FlashCardDao;
import com.example.testaudioenglish.DAO.TopicFlashCardDao;
import com.example.testaudioenglish.Entity.FlashCardEntity;
import com.example.testaudioenglish.Entity.TopicFlashCardEntity;

@Database(entities = {FlashCardEntity.class, TopicFlashCardEntity.class}, version = 3)
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
                            .addMigrations(MIGRATION_2_3) // Add migration from 2 to 3
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // Migration from version 2 to version 3
    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE FlashCardEntity ADD COLUMN Tick INTEGER DEFAULT 0 NOT NULL");
        }
    };
}
