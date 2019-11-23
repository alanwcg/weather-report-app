package com.example.android.weatherreport;

import android.app.Application;
import android.os.AsyncTask;
import android.text.TextUtils;

import androidx.lifecycle.LiveData;

import java.util.List;

public class CityRepository {

    private CityDao mCityDao;
    private LiveData<List<City>> mAllCities;

    public CityRepository(Application application) {
        CityRoomDatabase database = CityRoomDatabase.getInstance(application);
        mCityDao = database.cityDao();
        mAllCities = mCityDao.getOrderedCities();
    }

    public void insert(City city) {
        String name = city.getName();
        if(TextUtils.isEmpty(name)) {
            throw new IllegalArgumentException("City requires a name!");
        }
        new InsertCityAsyncTask(mCityDao).execute(city);

    }

    public void update(City city) {
        String name = city.getName();
        if(TextUtils.isEmpty(name)) {
            throw new IllegalArgumentException("City requires a name!");
        }
        new UpdateCityAsyncTask(mCityDao).execute(city);
    }

    public void delete(City city) {
        new DeleteCityAsyncTask(mCityDao).execute(city);
    }

    public void deleteAllCities() {
        new DeleteAllCitiesAsyncTask(mCityDao).execute();
    }


    public LiveData<List<City>> getOrderedCities() {
        return mAllCities;
    }

    private static class InsertCityAsyncTask extends AsyncTask<City, Void, Void> {

        private CityDao mAsyncTaskDao;

        public InsertCityAsyncTask(CityDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(City... cities) {
            mAsyncTaskDao.insert(cities[0]);
            return null;
        }
    }

    private static class UpdateCityAsyncTask extends AsyncTask<City, Void, Void> {

        private CityDao mAsyncTaskDao;

        public UpdateCityAsyncTask(CityDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(City... cities) {
            mAsyncTaskDao.update(cities[0]);
            return null;
        }
    }

    private static class DeleteCityAsyncTask extends AsyncTask<City, Void, Void> {

        private CityDao mAsyncTaskDao;

        public DeleteCityAsyncTask(CityDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(City... cities) {
            mAsyncTaskDao.delete(cities[0]);
            return null;
        }
    }

    private static class DeleteAllCitiesAsyncTask extends AsyncTask<Void, Void, Void> {

        private CityDao mAsyncTaskDao;

        public DeleteAllCitiesAsyncTask(CityDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }
}
