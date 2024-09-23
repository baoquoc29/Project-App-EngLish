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


@Database(entities = {FlashCardEntity.class, TopicFlashCardEntity.class}, version = 5)
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
                            .addMigrations(MIGRATION_4_5) // Thêm migration từ 4 đến 5
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    // Migration từ version 4 đến version 5
    private static final Migration MIGRATION_4_5 = new Migration(4, 5) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // Thêm cột "status" vào bảng TopicFlashCardEntity
            database.execSQL("ALTER TABLE TopicFlashCardEntity ADD COLUMN `status` INTEGER DEFAULT 0 NOT NULL");
        }
    };
}
