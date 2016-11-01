package beastandroidapps.beastweather.model;

import com.google.gson.annotations.SerializedName;

public class TemperatureDetails {
    @SerializedName("min")
    double temperatureMin;

    @SerializedName("max")
    double temperatureMax;


    public TemperatureDetails() {
    }


    public double getTemperatureMin() {
        return temperatureMin;
    }

    public double getTemperatureMax() {
        return temperatureMax;
    }
}
