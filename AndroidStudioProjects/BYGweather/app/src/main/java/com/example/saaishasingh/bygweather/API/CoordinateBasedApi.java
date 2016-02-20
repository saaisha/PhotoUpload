package com.example.saaishasingh.bygweather.API;

import com.example.saaishasingh.bygweather.Bean.ForecastList;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by saaishasingh on 2/19/16.
 */
public interface CoordinateBasedApi {

    @GET("/data/2.5/forecast/daily?appid=cf597f2bad2bcdc59bd5adf1be1f945c&cnt=14&units=metric&mode=json")
    ForecastList getCoordList(
            @Query("lat") Double lat,
            @Query("lon") Double lon
    );
}
