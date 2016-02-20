package com.example.saaishasingh.bygweather.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.saaishasingh.bygweather.Bean.CitySuggestions;
import com.example.saaishasingh.bygweather.R;

import java.util.List;

/**
 * Created by saaishasingh on 2/19/16.
 */
public class CityAdapter extends ArrayAdapter {

    private Context ctx;
    private List<CitySuggestions> cityList;

    public CityAdapter(Context ctx, List<CitySuggestions> cityList) {
        super(ctx, R.layout.city_suggestion_layout, cityList);
        this.cityList = cityList;
        this.ctx = ctx;
    }


    @Override
    public int getCount() {
        return cityList.size();
    }


    @Override
    public CitySuggestions getItem(int position) {

        return cityList.get(position);
    }


    @Override
    public long getItemId(int position) {
        CitySuggestions city = cityList.get(position);
        return city.getId();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.city_suggestion_layout, parent, false);
        }

        CitySuggestions city = cityList.get(position);
        TextView cityText = (TextView) convertView.findViewById(R.id.cityName);
        cityText.setText(city.getName() + "," + city.getSys().getCountry());

        return convertView;
    }

    public void setCityList(List<CitySuggestions> cityList) {
        this.cityList = cityList;
    }

}
