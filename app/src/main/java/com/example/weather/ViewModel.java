package com.example.weather;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.weather.TempInfo;

public class ViewModel extends androidx.lifecycle.ViewModel {
    private final MutableLiveData<TempInfo> weatherData = new MutableLiveData<>();
    private final MutableLiveData<String> favoriteCity = new MutableLiveData<>();

    public String getFavoriteCity() {
        return this.favoriteCity.getValue();
    }

    public void setFavoriteCity(String favoriteCity) {
        this.favoriteCity.setValue(favoriteCity);
    }

    public void setWeatherData(TempInfo weatherData) {
        this.weatherData.setValue(weatherData);
    }

    public LiveData<TempInfo> getWeatherData() {
        return this.weatherData;
    }
}
