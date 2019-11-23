package com.example.android.weatherreport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class WeatherActivity extends AppCompatActivity {

    private CityViewModel mCityViewModel;

    private TextView mEmptyView;

    private CityAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_activity);

        mEmptyView = findViewById(R.id.no_cities_text);
        final ProgressBar progressBar = findViewById(R.id.progress_bar);

        FloatingActionButton fab = findViewById(R.id.add_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WeatherActivity.this, AddCityActivity.class);
                startActivity(intent);
            }
        });

        mCityViewModel = new ViewModelProvider(this).get(CityViewModel.class);
        mCityViewModel.getOrderedCities().observe(this, new Observer<List<City>>() {
            @Override
            public void onChanged(List<City> cities) {
                progressBar.setVisibility(View.GONE);
                displayDatabaseInfo(cities);
            }
        });

    }

    private void displayDatabaseInfo(List<City> cities) {

        ListView list = findViewById(R.id.city_list);
        registerForContextMenu(list);

        if(cities.isEmpty()) {
            mEmptyView.setText(R.string.no_cities);
            list.setEmptyView(mEmptyView);
        }

        mAdapter = new CityAdapter(this, cities);
        list.setAdapter(mAdapter);

    }

    private String getUrl(City city) {
        Uri baseUri = Uri.parse(CityRoomDatabase.BASE_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        String id = String.valueOf(city.getId());

        uriBuilder.appendQueryParameter("id", id);
        uriBuilder.appendQueryParameter("units", "metric");
        uriBuilder.appendQueryParameter("APPID", "4e6fa7b8ea8bf55d53a898611fd72f59");

        return uriBuilder.toString();
    }

    private boolean isConnected() {
        //Check if there is an internet connection
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        return isConnected;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        getMenuInflater().inflate(R.menu.list_item_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        City city = mAdapter.getItem(info.position);
        switch (item.getItemId()) {
            case R.id.delete_city:
                mCityViewModel.delete(city);
                return true;
            case R.id.update_city:
                if(isConnected()) {
                    String url = getUrl(city);
                    mCityViewModel.getCity(url).observe(this, new Observer<City>() {
                        @Override
                        public void onChanged(City city) {
                            mCityViewModel.update(city);
                        }
                    });
                } else {
                    Toast.makeText(this, "No internet connection found.",
                            Toast.LENGTH_SHORT).show();
                }
            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.weather_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete_all_entries) {
            mCityViewModel.deleteAllCities();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
