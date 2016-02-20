package com.example.saaishasingh.bygweather.Bean;

import java.util.List;

/**
 * Created by saaishasingh on 2/19/16.
 */
public class CityList {

    private String message;
    private String cod;
    private int count;
    private List<CitySuggestions> list;


    public List<CitySuggestions> getList() {
        return list;
    }

    public void setList(List<CitySuggestions> list) {
        this.list = list;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



}

