package com.example.mvvmtest.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.mvvmtest.model.enity.GankEnity;
import com.example.mvvmtest.model.enity.Word;
import com.example.mvvmtest.model.repository.GankRepository;
import com.example.mvvmtest.model.repository.WordRepository;

import java.util.List;


public class WordViewModel extends AndroidViewModel {
    private WordRepository wordRepository;
    private GankRepository gankRepository;

    public WordViewModel(@NonNull Application application) {
        super(application);
        wordRepository=new WordRepository(application);
        gankRepository=new GankRepository(application);
    }

    public LiveData<List<Word>> getAllWordsLive() {
        return wordRepository.getAllWordsLive();
    }

    public void insertWords(Word...words){
        wordRepository.insertWords(words);
    }

    public void updateWords(Word...words){
        wordRepository.updateWords(words);
    }

    public void deleteWords(Word...words){
        wordRepository.deleteWords(words);
    }

    public void deleteAllWords(){
        wordRepository.deleteAllWords();
    }

    public LiveData<GankEnity> getAllGankLive(){
        return gankRepository.getAllGankLive();
    }

    public void updateGanks(GankEnity...gankEnities){
        gankRepository.updateGanks(gankEnities);
    }

    public void insertGanks(){
        gankRepository.insert();
    }

    public void deleteGanks(GankEnity...gankEnities){
        gankRepository.deleteGanks(gankEnities);
    }

    public void deleteAllGanks(){
        gankRepository.deleteAllGanks();
    }


}
