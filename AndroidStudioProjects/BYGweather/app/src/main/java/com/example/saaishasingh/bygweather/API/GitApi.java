package com.example.saaishasingh.bygweather.API;

import android.telecom.Call;

import com.example.saaishasingh.bygweather.Bean.CityList;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by saaishasingh on 2/19/16.
 */
public interface GitApi {

    @GET("/data/2.5/find?mode=json&appid=cf597f2bad2bcdc59bd5adf1be1f945c&type=like&cnt=20")
    CityList getCityList(
            @Query("q") String[] text
    );


}
