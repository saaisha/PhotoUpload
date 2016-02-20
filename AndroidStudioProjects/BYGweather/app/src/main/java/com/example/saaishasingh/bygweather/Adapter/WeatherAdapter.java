package com.example.saaishasingh.bygweather.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.saaishasingh.bygweather.Bean.CitySuggestions;
import com.example.saaishasingh.bygweather.Bean.ForecastResults;
import com.example.saaishasingh.bygweather.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/**
 * Created by saaishasingh on 2/19/16.
 */
public class WeatherAdapter extends ArrayAdapter {
    private Context ctx;
    private List<ForecastResults> resultsList;

    public WeatherAdapter(Context ctx, List<ForecastResults> resultsList) {
        super(ctx, R.layout.city_suggestion_layout, resultsList);
        this.resultsList = resultsList;
        this.ctx = ctx;
    }


    @Override
    public int getCount() {
        return resultsList.size();
    }


    @Override
    public ForecastResults getItem(int position) {

        return resultsList.get(position);
    }


    @Override
    public long getItemId(int position) {
        ForecastResults results = resultsList.get(position);
        return results.getWeather()[0].getId();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.forecast_layout, parent, false);
        }

        ForecastResults result = resultsList.get(position);
        TextView forecastDate = (TextView) convertView.findViewById(R.id.forecast_date);
        TextView forecastDesc = (TextView) convertView.findViewById(R.id.forecast_desc);
        TextView forecastMax = (TextView) convertView.findViewById(R.id.forecast_max);
        TextView forecastMin = (TextView) convertView.findViewById(R.id.forecast_min);

        java.util.Date date=new java.util.Date((long)(result.getDt())*1000);
        String format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(date);

        forecastDate.setText(String.valueOf(format));
        forecastDesc.setText(result.getWeather()[0].getDescription().toString());
        forecastMax.setText(String.valueOf(result.getTemp().getMax())+"°");
        forecastMin.setText(String.valueOf(result.getTemp().getMin())+"°");

        return convertView;
    }

}
