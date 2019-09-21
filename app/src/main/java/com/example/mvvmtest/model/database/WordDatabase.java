package com.example.mvvmtest.model.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.mvvmtest.model.Converter.ListConverter;
import com.example.mvvmtest.model.Converter.ResultConverter;
import com.example.mvvmtest.model.enity.GankEnity;
import com.example.mvvmtest.model.enity.Word;
import com.example.mvvmtest.model.dao.GankDao;
import com.example.mvvmtest.model.dao.WordDao;

@Database(entities = {Word.class, GankEnity.class}, version = 4, exportSchema = false)
@TypeConverters({ResultConverter.class, ListConverter.class})
public abstract class WordDatabase extends RoomDatabase {
    public abstract WordDao getWordDao();
    public abstract GankDao getGankDao();
    private static volatile WordDatabase instance;


    public static WordDatabase getInstance(Context context){
        if (instance==null){
            synchronized (WordDatabase.class){
                if (instance==null){
                    instance= Room.databaseBuilder(context.getApplicationContext(),WordDatabase.class,"word_database")
//                            .addMigrations(MIGRATION_2_3)
                            .build();
                }
            }
        }
        return instance;
    }

//    private static final Migration MIGRATION_2_3=new Migration(2,3) {
//        @Override
//        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            database.execSQL("ALTER TABLE word ADD COLUMN  results STRING NOT NULL DEFAULT 0");
//            database.execSQL("ALTER TABLE word ADD COLUMN is_visible BOOLEAN NOT NULL DEFAULT 0");
//        }
//    };
}
