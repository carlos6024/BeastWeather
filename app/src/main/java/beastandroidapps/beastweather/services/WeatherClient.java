package beastandroidapps.beastweather.services;



import android.content.Context;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import beastandroidapps.beastweather.entites.WeatherData;
import beastandroidapps.beastweather.infrastructure.BeastWeatherApplication;
import beastandroidapps.beastweather.model.WeatherDetails;
import beastandroidapps.beastweather.model.WeatherFullDetails;
import beastandroidapps.beastweather.model.WeatherListModel;
import beastandroidapps.beastweather.views.WeatherAdpater;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class WeatherClient {
    private static WeatherClient weatherClient;
    private WeatherWebService weatherWebService;

    private WeatherClient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BeastWeatherApplication.BASE_WEATHER_URL)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        weatherWebService = retrofit.create(WeatherWebService.class);

    }

    public static WeatherClient getInstance(){
        if (weatherClient ==null){
            weatherClient = new WeatherClient();
        }
        return weatherClient;
    }

    public Observable<WeatherListModel> getWeather(String zip, String units){
        return weatherWebService.getWeather(zip,"json",units,"7",BeastWeatherApplication.BASE_WEAHTER_API);
    }


    public List<WeatherData> weatherDataConverter(WeatherListModel weatherListModel, final Firebase reference){
        final List<WeatherData> weatherDataList = new ArrayList<>();
        int posisiton = 0;

        for (WeatherFullDetails weatherFullDetails: weatherListModel.fullDetailsList){
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.add(GregorianCalendar.DATE,posisiton);
            Date time = gregorianCalendar.getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MM dd");

            WeatherData weatherData = new WeatherData(weatherFullDetails.getTemperatureDetails().getTemperatureMax(),
                    weatherFullDetails.getTemperatureDetails().getTemperatureMin(),weatherFullDetails.getPressure(),
                    weatherFullDetails.getHumidity(),"","",simpleDateFormat.format(time),"");


            for (WeatherDetails weatherDetails: weatherFullDetails.getWeatherDetailses()){
                weatherData.setWeatherBasic(weatherDetails.getBasicWeatherDescription());
                weatherData.setWeatherDetailed(weatherDetails.getDetailedWeatherDescription());
                weatherData.setWeatherUrl(weatherDetails.getWeatherIcon());
            }

            weatherDataList.add(weatherData);

            posisiton ++;
        }

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChildren()){
                    int index =0;
                    for (WeatherData weatherData: weatherDataList){
                        reference.child(Integer.toString(index)).setValue(weatherData);
                        index ++;
                    }
                } else{
                    int index =0;
                    for (WeatherData weatherData: weatherDataList){
                        Map newWeatherData = new HashMap();
                        newWeatherData.put("humidity",weatherData.getHumidity());
                        newWeatherData.put("temperatureMax",weatherData.getTemperatureMax());
                        newWeatherData.put("temperatureMin",weatherData.getTemperatureMin());
                        newWeatherData.put("pressure",weatherData.getPressure());
                        newWeatherData.put("weatherBasic",weatherData.getWeatherBasic());
                        newWeatherData.put("weatherDetailed",weatherData.getWeatherDetailed());
                        newWeatherData.put("weatherDate",weatherData.getWeatherDate());
                        newWeatherData.put("weatherUrl",weatherData.getWeatherUrl());
                        reference.child(Integer.toString(index)).updateChildren(newWeatherData);
                        index++;
                    }


                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        return weatherDataList;
    }


    public ValueEventListener readFromFireBase(Firebase reference, final WeatherAdpater adpater, final Context context){
        return reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<WeatherData> weatherDataList = new ArrayList<WeatherData>();
                if (dataSnapshot!=null && dataSnapshot.hasChildren()){
                    for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                        WeatherData weatherData = dataSnapshot1.getValue(WeatherData.class);
                        weatherDataList.add(weatherData);
                    }
                }


                if (weatherDataList.isEmpty()){
                    Toast.makeText(context,"Please get check your internet",Toast.LENGTH_LONG).show();
                }
                adpater.setWeatherDataList(weatherDataList);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }







}
