package com.example.mvvmtest.model.enity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Word {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "english")
    private String english;
    @ColumnInfo(name = "chinese")
    private String chinese;
    @ColumnInfo(name = "is_chinese")
    private boolean isChinese;

    public boolean isChinese() {
        return isChinese;
    }

    public void setIsChinese(boolean chinese) {
        isChinese = chinese;
    }

    public Word(String english, String chinese) {
        this.english = english;
        this.chinese = chinese;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }

    public String getChinese() {
        return chinese;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;
    }
}
