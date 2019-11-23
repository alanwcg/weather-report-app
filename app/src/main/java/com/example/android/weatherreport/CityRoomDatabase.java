package com.example.android.weatherreport;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.List;

@Database(entities = {City.class}, version = 1)
public abstract class CityRoomDatabase extends RoomDatabase {

    private static final String OWM_URL_REQUEST = "http://api.openweathermap.org/data/2.5/group?id=3448439,5368361,2643743,2968815,1850147&units=metric&APPID=4e6fa7b8ea8bf55d53a898611fd72f59";
    public static final String BASE_REQUEST_URL = "http://api.openweathermap.org/data/2.5/weather?";

    public abstract CityDao cityDao();

    private static volatile CityRoomDatabase instance;

    public static CityRoomDatabase getInstance(final Context context) {
        if(instance == null) {
            synchronized (CityRoomDatabase.class) {
                if(instance == null) {
                    instance = Room.databaseBuilder(context.getApplicationContext(),
                            CityRoomDatabase.class, "weather")
                            .addCallback(roomCallBack)
                            .build();
                }
            }
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private CityDao mAsyncTaskDao;

        private PopulateDbAsyncTask(CityRoomDatabase database) {
            mAsyncTaskDao = database.cityDao();
        }

        @Override
        protected Void doInBackground(final Void... voids) {
            List<City> cities = QueryUtils.fetchCitiesData(OWM_URL_REQUEST);

            for(int i = 0; i < cities.size(); i++) {
                mAsyncTaskDao.insert(cities.get(i));
            }
            return null;
        }
    }
}
