package com.example.android.weatherreport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CityAdapter extends ArrayAdapter<City> {


    public CityAdapter(Context context, List<City> cities) {
        super(context, 0, cities);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View currentListItem = convertView;
        if(currentListItem == null) {
            currentListItem = LayoutInflater.from(getContext()).inflate(R.layout.city_list_item,
                    parent, false);
        }

        City currentCity = getItem(position);

        TextView name = currentListItem.findViewById(R.id.city_name);
        name.setText(currentCity.getName());

        TextView country = currentListItem.findViewById(R.id.city_country);
        country.setText(currentCity.getCountry());

        TextView description = currentListItem.findViewById(R.id.city_temp_description);
        description.setText(currentCity.getDescription());

        TextView temp = currentListItem.findViewById(R.id.city_temperature);
        temp.setText(getTemperature(currentCity.getTemp()));

        TextView time = currentListItem.findViewById(R.id.time);
        time.setText(getTime(currentCity.getDt()));

        TextView dt = currentListItem.findViewById(R.id.date);
        dt.setText(getDate(currentCity.getDt()));

        return currentListItem;
    }

    private String getTemperature(double temp) {
        return String.format(Locale.US, "%.1f %s", temp,
                getContext().getString(R.string.celsius_symbol));
    }

    private String getTime(long time) {
        Date date = new Date(time * 1000);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("h:mm a", Locale.US);
        return dateFormatter.format(date);
    }

    private String getDate(long time) {
        Date date = new Date(time * 1000);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
        return dateFormatter.format(date);
    }
}
