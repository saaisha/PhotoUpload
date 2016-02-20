package com.example.saaishasingh.bygweather.Bean;

import java.util.List;

/**
 * Created by saaishasingh on 2/19/16.
 */
public class ForecastList {

    private City city;
    private String cod;
    private double message;
    private int cnt;

    public List<ForecastResults> getList() {
        return list;
    }

    public void setList(List<ForecastResults> list) {
        this.list = list;
    }

    private List<ForecastResults> list;

    public int getCnt() {
        return cnt;
    }

    public void setCnt(int cnt) {
        this.cnt = cnt;
    }


    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public double getMessage() {
        return message;
    }

    public void setMessage(double message) {
        this.message = message;
    }


    public class City{
        private int id;

        public String getName() {
            return name;
        }

        private String name;
        private Coordinates coord;
        private String country;
        private int population;


        public class Coordinates {
            double lon;
            double lat;
        }
    }



}
