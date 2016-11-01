package beastandroidapps.beastweather.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherFullDetails {
    double pressure;
    double humidity;
    double speed;

    @SerializedName("temp")
    TemperatureDetails temperatureDetails;

    @SerializedName("weather")
    List<WeatherDetails> weatherDetailses;


    public double getPressure() {
        return pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public double getSpeed() {
        return speed;
    }

    public TemperatureDetails getTemperatureDetails() {
        return temperatureDetails;
    }

    public List<WeatherDetails> getWeatherDetailses() {
        return weatherDetailses;
    }
}
