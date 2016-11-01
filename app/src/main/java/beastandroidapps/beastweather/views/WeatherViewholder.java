package beastandroidapps.beastweather.views;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import beastandroidapps.beastweather.R;
import beastandroidapps.beastweather.entites.WeatherData;
import beastandroidapps.beastweather.infrastructure.BeastWeatherApplication;
import butterknife.BindView;
import butterknife.ButterKnife;

public class WeatherViewholder extends RecyclerView.ViewHolder {

    @BindView(R.id.list_weather_item_date)
    TextView weatherDate;

    @BindView(R.id.list_weather_item_ImageView)
    ImageView weatherImage;

    @BindView(R.id.list_weather_item_max)
    TextView weatherMax;

    @BindView(R.id.list_weather_item_min)
    TextView weatherMin;

    @BindView(R.id.list_weather_item_weatherDescition)
    TextView weatherDescription;

    public WeatherViewholder(View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public void populate(WeatherData weatherData, int position, Context context,boolean isMetric){
        itemView.setTag(weatherData);
        String date;

        if (position ==0){
            date = "Today";
        } else if (position ==1){
            date = "Tomorrow";
        } else {
            date = weatherData.getWeatherDate();
        }

        weatherDate.setText(date);


        if (isMetric){
            weatherMax.setText(context.getString(R.string.format_temperature,roundWeather(weatherData.getTemperatureMax()),"C"));
            weatherMin.setText(context.getString(R.string.format_temperature,roundWeather(weatherData.getTemperatureMin()),"C"));
        } else {
            weatherMax.setText(context.getString(R.string.format_temperature,roundWeather(weatherData.getTemperatureMax()),"F"));
            weatherMin.setText(context.getString(R.string.format_temperature,roundWeather(weatherData.getTemperatureMin()),"F"));
        }

        weatherDescription.setText(weatherData.getWeatherDetailed());

        Picasso.with(context).load(BeastWeatherApplication.BASE_WEATHER_IMAGE + weatherData.getWeatherUrl() + ".png")
                .into(weatherImage);


    }

    private String roundWeather(double temperature){
        double roundedWeather = Math.round(temperature);
        return Double.toString(roundedWeather);
    }
}
