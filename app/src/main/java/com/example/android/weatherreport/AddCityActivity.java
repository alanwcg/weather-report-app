package com.example.android.weatherreport;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

public class AddCityActivity extends AppCompatActivity {

    private CityViewModel mCityViewModel;

    private EditText mNameEditText;
    private EditText mCountryEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_city_activity);

        mNameEditText = findViewById(R.id.city_name_edit_text);
        mCountryEditText = findViewById(R.id.city_country_edit_text);

        mCityViewModel = new ViewModelProvider(this).get(CityViewModel.class);
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

    private void insertCity() {

        String name = mNameEditText.getText().toString();
        String country = mCountryEditText.getText().toString();

        final String cityInfo;
        if(TextUtils.isEmpty(name)) {
            Toast.makeText(this, "City name is required!", Toast.LENGTH_SHORT).show();
            return;
        } else if(TextUtils.isEmpty(country) || country.length() != 2) {
            cityInfo = name;
        } else {
            cityInfo = name + "," + country;
        }

        Uri baseUri = Uri.parse(CityRoomDatabase.BASE_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("q", cityInfo);
        uriBuilder.appendQueryParameter("units", "metric");
        uriBuilder.appendQueryParameter("APPID", "4e6fa7b8ea8bf55d53a898611fd72f59");

        String url = uriBuilder.toString();

        mCityViewModel.getCity(url).observe(this, new Observer<City>() {
            @Override
            public void onChanged(final City city) {
                mCityViewModel.insert(city);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add_city, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                if(isConnected()) {
                    insertCity();
                } else {
                    Toast.makeText(this, "No internet connection found.",
                            Toast.LENGTH_SHORT).show();
                }
                finish();
            case android.R.id.home:
                // Navigate back to parent activity (WeatherActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
