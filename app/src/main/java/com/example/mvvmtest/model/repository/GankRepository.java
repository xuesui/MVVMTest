package com.example.mvvmtest.model.repository;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.mvvmtest.model.api.GankService;
import com.example.mvvmtest.model.dao.GankDao;
import com.example.mvvmtest.model.enity.GankEnity;
import com.example.mvvmtest.model.database.WordDatabase;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class GankRepository {
    private static final String BASE_URL = "http://gank.io/api/";
    private GankDao gankDao;
    private Retrofit retrofit;

    public GankRepository(Context context) {
        WordDatabase wordDatabase=WordDatabase.getInstance(context);
        gankDao=wordDatabase.getGankDao();
        retrofit= new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @SuppressLint("CheckResult")
    public LiveData<GankEnity> getAllGankLive(){
        retrofit.create(GankService.class)
                .getMessage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::insertGanks);

        return gankDao.getAllGanks();
    }

    @SuppressLint("CheckResult")
    public void insert(){
        retrofit.create(GankService.class)
                .getMessage()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::insertGanks);
    }

    public void insertGanks(GankEnity...gankEnities){

        new GankRepository.InsertAsyncTask(gankDao).execute(gankEnities);
    }

    public void updateGanks(GankEnity...gankEnities){
        new GankRepository.UpdateAsyncTask(gankDao).execute(gankEnities);
    }

    public void deleteGanks(GankEnity...gankEnities){
        new GankRepository.DeleteAsyncTask(gankDao).execute(gankEnities);
    }

    public void deleteAllGanks(){
        new GankRepository.DeleteAllAsyncTask(gankDao).execute();
    }

    static class InsertAsyncTask extends AsyncTask<GankEnity,Void,Void> {
        private GankDao gankDao;

        InsertAsyncTask(GankDao gankDao) {
            this.gankDao = gankDao;
        }


        @Override
        protected Void doInBackground(GankEnity... gankEnities) {
            gankDao.insertGank(gankEnities);
            return null;
        }
    }

    static class UpdateAsyncTask extends AsyncTask<GankEnity,Void,Void>{
        private GankDao gankDao;

        UpdateAsyncTask(GankDao gankDao) {
            this.gankDao = gankDao;
        }


        @Override
        protected Void doInBackground(GankEnity... gankEnities) {
            gankDao.updateGank(gankEnities);
            return null;
        }
    }

    static class DeleteAsyncTask extends AsyncTask<GankEnity,Void,Void>{
        private GankDao gankDao;

        DeleteAsyncTask(GankDao gankDao) {
            this.gankDao = gankDao;
        }


        @Override
        protected Void doInBackground(GankEnity... gankEnities) {
            gankDao.deleteGank(gankEnities);
            return null;
        }
    }

    static class DeleteAllAsyncTask extends AsyncTask<Void,Void,Void>{
        private GankDao gankDao;

        DeleteAllAsyncTask(GankDao gankDao) {
            this.gankDao = gankDao;
        }

        @Override
        protected Void doInBackground(Void...voids) {
            gankDao.deleteAllGanks();
            return null;
        }
    }
}
