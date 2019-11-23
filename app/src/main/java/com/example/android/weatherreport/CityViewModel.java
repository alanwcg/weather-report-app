package com.example.android.weatherreport;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class CityViewModel extends AndroidViewModel {

    private CityRepository mRepository;

    private static String mUrl;

    private static MutableLiveData<City> mCity = null;

    private LiveData<List<City>> mAllCities;

    public CityViewModel(Application application) {
        super(application);
        mRepository = new CityRepository(application);
        mAllCities = mRepository.getOrderedCities();
    }

    public LiveData<City> getCity(String url) {
        mCity = new MutableLiveData<>();
        mUrl = url;
        new LoadCityAsyncTask().execute();
        return mCity;
    }

    private static class LoadCityAsyncTask extends AsyncTask<Void, Void, City> {
        @Override
        protected City doInBackground(Void... voids) {
            return QueryUtils.fetchCityData(mUrl);
        }

        @Override
        protected void onPostExecute(City city) {
            if(city == null) {
                return;
            }
            mCity.postValue(city);
        }
    }

    public void insert(City city) {
        mRepository.insert(city);
    }

    public void update(City city) {
        mRepository.update(city);
    }

    public void delete(City city) {
        mRepository.delete(city);
    }

    public void deleteAllCities() {
        mRepository.deleteAllCities();
    }

    public LiveData<List<City>> getOrderedCities() {
        return mAllCities;
    }
}
