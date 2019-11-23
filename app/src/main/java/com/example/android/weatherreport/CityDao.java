package com.example.android.weatherreport;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(City city);

    @Update
    void update(City city);

    @Delete
    void delete(City city);

    @Query("DELETE FROM cities")
    void deleteAll();

    @Query("SELECT * FROM cities ORDER BY name")
    LiveData<List<City>> getOrderedCities();

}
