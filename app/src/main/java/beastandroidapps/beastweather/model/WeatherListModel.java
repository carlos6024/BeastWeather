package beastandroidapps.beastweather.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherListModel {
    @SerializedName("list")
    public List<WeatherFullDetails> fullDetailsList;
}
