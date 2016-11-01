package beastandroidapps.beastweather.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.firebase.client.Firebase;
import com.firebase.client.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import beastandroidapps.beastweather.R;
import beastandroidapps.beastweather.entites.WeatherData;
import beastandroidapps.beastweather.infrastructure.BeastWeatherApplication;
import beastandroidapps.beastweather.model.WeatherListModel;
import beastandroidapps.beastweather.services.WeatherClient;
import beastandroidapps.beastweather.views.WeatherAdpater;
import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ForcastFragment extends Fragment implements WeatherAdpater.WeatherListener {

    @BindView(R.id.fragment_forcast_recyclerView)
    RecyclerView recyclerView;

    private WeatherAdpater adpater;
    private Subscription weatherSubscription;
    private Firebase reference;
    private ValueEventListener listener;

    public static ForcastFragment newInstance(){
        return new ForcastFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_forecast,container,false);
        ButterKnife.bind(this,rootView);
        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences appPreferences = getActivity().getSharedPreferences(BeastWeatherApplication.APP_ID_PREFERENCE,Context.MODE_PRIVATE);

        String appId = appPreferences.getString(BeastWeatherApplication.APP_ID,"");

        if (appId.isEmpty()){
            reference = new Firebase(BeastWeatherApplication.BASE_FIRE_BASE_URL).push();
            appPreferences.edit().putString(BeastWeatherApplication.APP_ID,reference.getKey()).apply();
        } else{
            reference = new Firebase(BeastWeatherApplication.BASE_FIRE_BASE_URL + appId);
        }

        String zipCode = sharedPreferences.getString(BeastWeatherApplication.LOCATION_PREFERENCE,"94043");
        String units = sharedPreferences.getString(BeastWeatherApplication.UNIT_PREFERENCE,"Metric");

        adpater = new WeatherAdpater(getActivity(),this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adpater);
        getWeather(zipCode,units);




    }

    private void getWeather(String zip, final String units){
        weatherSubscription = WeatherClient.getInstance().
                getWeather(zip,units)
                .subscribeOn(Schedulers.io())
                .map(new Func1<WeatherListModel, List<WeatherData>>() {
                    @Override
                    public List<WeatherData> call(WeatherListModel weatherListModel) {
                        return WeatherClient.getInstance().weatherDataConverter(weatherListModel,reference);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<List<WeatherData>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener = WeatherClient.getInstance().readFromFireBase(reference,adpater,getActivity());
                        reference.addValueEventListener(listener);
                        Toast.makeText(getContext(),"You are on offline mode",Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(List<WeatherData> weatherDatas) {
                        listener = WeatherClient.getInstance().readFromFireBase(reference,adpater,getActivity());
                        reference.addValueEventListener(listener);

                        if (units.equals("Metric")){
                            adpater.setMetric(true);
                        } else{
                            adpater.setMetric(false);
                        }

                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (weatherSubscription!=null && !weatherSubscription.isUnsubscribed()){
            weatherSubscription.unsubscribe();
        }

        if (listener!=null){
            reference.removeEventListener(listener);
        }


    }

    @Override
    public void WeatherClicked(WeatherData weatherData) {

    }
}
