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

@Database(entities = {FlashCardEntity.class, TopicFlashCardEntity.class}, version = 4)
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
                                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                                    super.onOpen(db);
                                    db.execSQL("PRAGMA foreign_keys=ON;");
                                }
                            })
                            .addMigrations(MIGRATION_3_4) // Add migration from 3 to 4
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // Migration from version 3 to version 4
    private static final Migration MIGRATION_3_4 = new Migration(3, 4) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE FlashCardEntity ADD COLUMN `Check` INTEGER DEFAULT 0 NOT NULL");
        }
    };
}
