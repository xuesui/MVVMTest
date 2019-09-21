package com.example.mvvmtest.model.repository;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.mvvmtest.model.enity.Word;
import com.example.mvvmtest.model.dao.WordDao;
import com.example.mvvmtest.model.database.WordDatabase;

import java.util.List;

public class WordRepository {
    private LiveData<List<Word>> allWordsLive;
    private WordDao wordDao;

    public WordRepository(Context context) {
        WordDatabase wordDatabase=WordDatabase.getInstance(context);
        wordDao=wordDatabase.getWordDao();
        allWordsLive=wordDao.getAllWords();
    }

    public LiveData<List<Word>> getAllWordsLive() {
        return allWordsLive;
    }


    public void insertWords(Word...words){

        new InsertAsyncTask(wordDao).execute(words);
    }

    public void updateWords(Word...words){
        new UpdateAsyncTask(wordDao).execute(words);
    }

    public void deleteWords(Word...words){
        new DeleteAsyncTask(wordDao).execute(words);
    }

    public void deleteAllWords(){
        new DeleteAllAsyncTask(wordDao).execute();
    }


    static class InsertAsyncTask extends AsyncTask<Word,Void,Void> {
        private WordDao wordDao;

        InsertAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.insertWords(words);
            return null;
        }
    }

    static class UpdateAsyncTask extends AsyncTask<Word,Void,Void>{
        private WordDao wordDao;

        UpdateAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.updateWords(words);
            return null;
        }
    }

    static class DeleteAsyncTask extends AsyncTask<Word,Void,Void>{
        private WordDao wordDao;

        DeleteAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.deleteWords(words);
            return null;
        }
    }

    static class DeleteAllAsyncTask extends AsyncTask<Void,Void,Void>{
        private WordDao wordDao;

        DeleteAllAsyncTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Void...voids) {
            wordDao.deleteAllWords();
            return null;
        }
    }
}
