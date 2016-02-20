package com.example.saaishasingh.bygweather.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saaishasingh on 2/19/16.
 */
public class CitySuggestions {
    private int id;
    private String name;
    private Coordinates coord;
    private Main main;
    private int dt;
    private Wind wind;

    public Sys getSys() {
        return sys;
    }

    public void setSys(Sys sys) {
        this.sys = sys;
    }

    private Sys sys;
    private Clouds clouds;
    private Weather[] weather;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public class Coordinates {
        double lon;
        double lat;
    }

    public class Main {
        double temp;
        double pressure;
        double humidity;
        double temp_min;
        double temp_max;
    }

    public class Wind {
        double speed;
        double deg;
    }

    public class Sys {
        public String getCountry() {
            return country;
        }

        String country;
    }

    public class Clouds {
        int all;
    }

    public class Weather {
        int id;

        public String getMain() {
            return main;
        }

        String main;
        String description;
        String icon;
    }
}


