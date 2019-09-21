package com.example.mvvmtest.model.Converter;

import androidx.room.TypeConverter;

import com.example.mvvmtest.model.enity.GankEnity;
import com.google.gson.Gson;


public class ResultConverter {
    @TypeConverter
    public static String convert(GankEnity.ResultsBean resultbean){
        Gson gson=new Gson();
        return gson.toJson(resultbean);
    }

    @TypeConverter
    public static GankEnity.ResultsBean revert(String result){
        Gson gson=new Gson();
        return gson.fromJson(result,GankEnity.ResultsBean.class);

    }
}
