package com.example.mvvmtest.model.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mvvmtest.model.enity.GankEnity;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface GankDao {
    @Insert(onConflict = REPLACE)
    void insertGank(GankEnity... gankEnity);

    @Update
    void updateGank(GankEnity... gankEnity);

    @Delete
    void deleteGank(GankEnity... gankEnity);

    @Query("DELETE FROM GANK")
    void deleteAllGanks();

    @Query("SELECT * FROM Gank ORDER BY ID")
    LiveData<GankEnity> getAllGanks();
}
