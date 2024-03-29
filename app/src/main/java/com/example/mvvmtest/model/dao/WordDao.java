package com.example.mvvmtest.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mvvmtest.model.enity.Word;

import java.util.List;

@Dao
public interface WordDao {
    @Insert
    void insertWords(Word...words);

    @Update
    void updateWords(Word...words);

    @Delete
    void deleteWords(Word...words);

    @Query("DELETE FROM WORD")
    void deleteAllWords();

    @Query("SELECT * FROM WORD ORDER BY ID")
    LiveData<List<Word>> getAllWords();


}
