package com.example.saaishasingh.bygweather;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import retrofit.RestAdapter;

import com.example.saaishasingh.bygweather.API.CoordinateBasedApi;
import com.example.saaishasingh.bygweather.API.GitApi;
import com.example.saaishasingh.bygweather.API.WeatherApi;
import com.example.saaishasingh.bygweather.Adapter.CityAdapter;
import com.example.saaishasingh.bygweather.Adapter.WeatherAdapter;
import com.example.saaishasingh.bygweather.Bean.CityList;
import com.example.saaishasingh.bygweather.Bean.CitySuggestions;
import com.example.saaishasingh.bygweather.Bean.ForecastList;
import com.example.saaishasingh.bygweather.Bean.ForecastResults;

import java.util.List;


public class MainActivity extends Activity implements LocationListener {
    private EditText country, city;
    private Button current;
    ListView cityListView, weatherList;
    List<CitySuggestions> citySuggestionsList;
    List<ForecastResults> results;
    boolean isGPSEnabled = false;
    boolean isNetworkEnabled = false;
    boolean canGetLocation = false;
    ProgressBar progressBar;
    Location location; // location
    double latitude; // latitude
    double longitude; // longitude
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        city = (EditText) findViewById(R.id.city_et);
        current = (Button) findViewById(R.id.current_button);
        cityListView = (ListView) findViewById(R.id.list);
        weatherList = (ListView) findViewById(R.id.weather_list);
        weatherList.setVisibility(View.GONE);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);


        city.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    BackgroundTask task = new BackgroundTask();
                    String pattern = city.getEditableText().toString();
                    task.execute(new String[]{pattern});
                    return true;
                }
                return false;
            }
        });

        cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos,
                                    long id) {
                try {
                    int idOfCity = citySuggestionsList.get(pos).getId();
                    Log.d("id", String.valueOf(idOfCity));
                    city.setText("");
                    city.append(citySuggestionsList.get(pos).getName());
                    FetchCityWeather fetch = new FetchCityWeather();
                    fetch.execute(idOfCity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        current.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    LocationManager locManager = (LocationManager) getSystemService(getApplicationContext().LOCATION_SERVICE);
                    isGPSEnabled = locManager
                            .isProviderEnabled(LocationManager.GPS_PROVIDER);
                    isNetworkEnabled = locManager
                            .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                    if (!isGPSEnabled && !isNetworkEnabled) {
                        showAlert();
                    } else {
                        canGetLocation = true;
                        // First get location from Network Provider
                        if (isNetworkEnabled) {
                            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, MainActivity.this);
                            Log.d("Network", "Network");
                            if (locManager != null) {
                                location = locManager
                                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                    Log.d("network", String.valueOf(latitude) + "," + String.valueOf(longitude));
                                }
                            }
                        }
                    }
                    if (isGPSEnabled) {
                        if (location == null) {
                            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, MainActivity.this);
                            Log.d("GPS Enabled", "GPS Enabled");
                            if (locManager != null) {
                                location = locManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null) {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                    Log.d("gps", String.valueOf(latitude) + "," + String.valueOf(longitude));

                                }
                            }
                        }
                    }

                    if (location != null) {
                        Double[] doubles = new Double[2];
                        doubles[0] = location.getLatitude();
                        doubles[1] = location.getLongitude();
                        FetchWeatherFromCoordinates fetchWeatherFromCoordinates = new FetchWeatherFromCoordinates();
                        fetchWeatherFromCoordinates.execute(doubles);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    private class BackgroundTask extends AsyncTask<String, Void, CityList> {
        RestAdapter restAdapter;

        @Override
        protected void onPreExecute() {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(city.getWindowToken(), 0);
            progressBar.setVisibility(View.VISIBLE);
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://api.openweathermap.org")
                    .build();
        }

        @Override
        protected CityList doInBackground(String... params) {
            GitApi methods = restAdapter.create(GitApi.class);
            CityList cityList = methods.getCityList(params);

            return cityList;
        }

        @Override
        protected void onPostExecute(CityList cityList) {
            progressBar.setVisibility(View.GONE);
            if (cityList != null) {
                citySuggestionsList = cityList.getList();
                if (citySuggestionsList != null && (citySuggestionsList.size() != 0)) {
                    CityAdapter cityAdapter = new CityAdapter(MainActivity.this, citySuggestionsList);
                    cityListView.setVisibility(View.VISIBLE);
                    cityListView.setAdapter(cityAdapter);
                } else {
                    showAlert();
                }

            } else {
                showAlert();
            }
        }
    }


    private class FetchCityWeather extends AsyncTask<Integer, Void, ForecastList> {
        RestAdapter restAdapter;

        @Override
        protected void onPreExecute() {
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://api.openweathermap.org")
                    .build();
        }

        @Override
        protected ForecastList doInBackground(Integer... params) {
            WeatherApi methods = restAdapter.create(WeatherApi.class);
            Log.d("param0", String.valueOf(params[0]));
            try {
                ForecastList forecastList = methods.getForecastList(params[0]);
                return forecastList;

            } catch (Exception e) {
                return null;
            }

        }

        @Override
        protected void onPostExecute(ForecastList forecastList) {
            if (forecastList != null) {
                results = forecastList.getList();
                cityListView.setVisibility(View.GONE);
                weatherList.setVisibility(View.VISIBLE);
                WeatherAdapter weatherAdapter = new WeatherAdapter(MainActivity.this, results);
                weatherList.setAdapter(weatherAdapter);

//               for (ForecastList.Forecasts forecasts : forecastList.getForecasts())
//               {
//                   Log.d("forecast",forecasts.getWeather()[0].getMain());
//
//               }

            } else {
                showAlert();
            }
        }
    }


    private class FetchWeatherFromCoordinates extends AsyncTask<Double, Void, ForecastList> {
        RestAdapter restAdapter;

        @Override
        protected void onPreExecute() {
            cityListView.setVisibility(View.GONE);
            restAdapter = new RestAdapter.Builder()
                    .setEndpoint("http://api.openweathermap.org")
                    .build();
        }

        @Override
        protected ForecastList doInBackground(Double... params) {
            CoordinateBasedApi methods = restAdapter.create(CoordinateBasedApi.class);
            Log.d("param0", String.valueOf(params[0]));
            Log.d("param1", String.valueOf(params[1]));

            try {
                ForecastList forecastList = methods.getCoordList(params[0], params[1]);
                return forecastList;

            } catch (Exception e) {
                return null;
            }

        }

        @Override
        protected void onPostExecute(ForecastList forecastList) {
            if (forecastList != null) {
                city.setText("");
                city.append(forecastList.getCity().getName());
                results = forecastList.getList();
                weatherList.setVisibility(View.VISIBLE);
                WeatherAdapter weatherAdapter = new WeatherAdapter(MainActivity.this, results);
                weatherList.setAdapter(weatherAdapter);

//               for (ForecastList.Forecasts forecasts : forecastList.getForecasts())
//               {
//                   Log.d("forecast",forecasts.getWeather()[0].getMain());
//
//               }

            } else {
                showAlert();
            }
        }
    }


    public void showAlert() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle("No Information Available");

        alertDialogBuilder
                .setMessage("Sorry , We have no weather information for this city")
                .setCancelable(true);

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }
}




