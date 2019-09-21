package com.example.mvvmtest.model.api;

import com.example.mvvmtest.Gank;
import com.example.mvvmtest.model.enity.GankEnity;

import io.reactivex.Flowable;
import retrofit2.http.GET;

public interface GankService {
    @GET("today")
    Flowable<GankEnity> getMessage();

}
