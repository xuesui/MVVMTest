package com.example.mvvmtest.model.Converter;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class ListConverter {
    @TypeConverter
    public static String convert(List<String> stringList){
        Gson gson=new Gson();
        return gson.toJson(stringList);
    }

    @TypeConverter
    public static List<String> revert(String s){
        Gson gson=new Gson();
        Type listTypeToken=new TypeToken<List<String>>(){}.getType();
        return gson.fromJson(s,listTypeToken);
    }
}
