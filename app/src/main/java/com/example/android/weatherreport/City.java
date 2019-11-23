package com.example.android.weatherreport;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "cities")
public class City {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private int mId;

    @NonNull
    @ColumnInfo(name = "name")
    private String mName;

    @NonNull
    @ColumnInfo(name = "country")
    private String mCountry;

    @NonNull
    @ColumnInfo(name = "temp")
    private double mTemp;

    @NonNull
    @ColumnInfo(name = "dt")
    private long mDt;

    @NonNull
    @ColumnInfo(name = "description")
    private String mDescription;

    @Ignore
    public City() {}


    public City(int id, String name, String country, double temp, long dt, String description) {
        mId = id;
        mName = name;
        mCountry = country;
        mTemp = temp;
        mDt = dt;
        mDescription = description;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public int getId() {
        return mId;
    }

    @NonNull
    public String getName() {
        return mName;
    }

    @NonNull
    public String getCountry() {
        return mCountry;
    }

    public double getTemp() {
        return mTemp;
    }

    public long getDt() {
        return mDt;
    }

    @NonNull
    public String getDescription() {
        return mDescription;
    }
}
