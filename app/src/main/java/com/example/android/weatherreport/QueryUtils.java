package com.example.android.weatherreport;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public abstract class QueryUtils {

    private static final String LOG_TAG = AddCityActivity.class.getSimpleName();

    private QueryUtils() {}

    public static List<City> fetchCitiesData(String requestUrl) {
        URL url = createURL(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        return extractCitiesFromJSON(jsonResponse);
    }

    public static City fetchCityData(String requestUrl) {
        URL url = createURL(requestUrl);

        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        return extractCityFromJSON(jsonResponse);
    }

    private static URL createURL(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        if(url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
            if(inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if(inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<City> extractCitiesFromJSON(String citiesJSON) {
        if(TextUtils.isEmpty(citiesJSON)) {
            return null;
        }

        List<City> cities = new ArrayList<>();
        try {
            JSONObject baseJsonResponse = new JSONObject(citiesJSON);
            JSONArray list = baseJsonResponse.optJSONArray("list");
            if(list.length() > 0) {
                for(int i = 0; i < list.length(); i++) {
                    JSONObject city = list.getJSONObject(i);
                    JSONArray weatherArray = city.getJSONArray("weather");
                    JSONObject weather = weatherArray.getJSONObject(0);
                    JSONObject main = city.getJSONObject("main");
                    JSONObject sys = city.getJSONObject("sys");

                    int id = city.optInt("id");
                    String name = city.optString("name");
                    String country = sys.optString("country");
                    double temp = main.optDouble("temp");
                    long dt = city.optLong("dt");
                    String description = weather.optString("description");

                    cities.add(new City(id, name, country, temp, dt, description));
                }
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }
        return cities;
    }

    private static City extractCityFromJSON(String citiesJSON) {
        if(TextUtils.isEmpty(citiesJSON)) {
            return null;
        }

        City city = null;
        try {
            JSONObject baseJsonResponse = new JSONObject(citiesJSON);

            JSONArray weatherArray = baseJsonResponse.getJSONArray("weather");
            JSONObject weather = weatherArray.getJSONObject(0);
            JSONObject main = baseJsonResponse.getJSONObject("main");
            JSONObject sys = baseJsonResponse.getJSONObject("sys");

            int id = baseJsonResponse.optInt("id");
            String name = baseJsonResponse.optString("name");
            String country = sys.optString("country");
            double temp = main.optDouble("temp");
            long dt = baseJsonResponse.optLong("dt");
            String description = weather.optString("description");

            city = new City(id, name, country, temp, dt, description);

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }
        return city;
    }
}
