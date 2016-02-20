package com.example.saaishasingh.bygweather.Bean;

/**
 * Created by saaishasingh on 2/19/16.
 */
public class ForecastResults {

    private int dt;
    private Temp temp;

    public int getDt() {
        return dt;
    }

    public Temp getTemp() {
        return temp;
    }

    public double getPressure() {
        return pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getSpeed() {
        return speed;
    }

    public double getDeg() {
        return deg;
    }

    public int getClouds() {
        return clouds;
    }

    public double getSnow() {
        return snow;
    }

    private double pressure;
    private double humidity;

    public Weather[] getWeather() {
        return weather;
    }


    private Weather[] weather;
    private double speed;
    private double deg;
    private int clouds;
    private double snow;


    public class Temp {
        private double day;
        private double min;
        private double max;
        private double night;

        public double getDay() {
            return day;
        }

        public double getMin() {
            return min;
        }

        public double getMax() {
            return max;
        }

        public double getNight() {
            return night;
        }

        public double getEve() {
            return eve;
        }

        public double getMorn() {
            return morn;
        }

        private double eve;
        private double morn;

    }

    public class Weather {
        private int id;

        public int getId() {
            return id;
        }

        public String getMain() {
            return main;
        }

        public String getDescription() {
            return description;
        }

        public String getIcon() {
            return icon;
        }

        private String main;
        private String description;
        private String icon;
    }



}

