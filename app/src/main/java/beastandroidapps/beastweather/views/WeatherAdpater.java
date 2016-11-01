package beastandroidapps.beastweather.views;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import beastandroidapps.beastweather.R;
import beastandroidapps.beastweather.entites.WeatherData;

public class WeatherAdpater extends RecyclerView.Adapter<WeatherViewholder> implements View.OnClickListener {

    private final int VIEW_TYPE_HEADER =1;
    private final int VIEW_TYPE_BODY =2;
    private List<WeatherData> weatherDataList;
    private LayoutInflater inflater;
    private Context context;
    private WeatherListener listener;

    private boolean isMetric;

    public WeatherAdpater(Context context, WeatherListener listener) {
        this.context = context;
        this.listener = listener;
        inflater = LayoutInflater.from(context);
        weatherDataList = new ArrayList<>();
        isMetric = true;
    }

    public void setMetric(boolean metric) {
        isMetric = metric;
    }

    public void setWeatherDataList(List<WeatherData> list) {
        weatherDataList.clear();
        weatherDataList.addAll(list);
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        if (position ==0){
            return VIEW_TYPE_HEADER;
        }

        position --;

        if (position<weatherDataList.size()){
            return VIEW_TYPE_BODY;
        }
        position -= weatherDataList.size();
        throw new IllegalArgumentException(" we are at the end of the list!");
    }


    @Override
    public WeatherViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View headerView = inflater.inflate(R.layout.list_weather_today,parent,false);
        View bodyView = inflater.inflate(R.layout.list_weather_item, parent,false);

        if (viewType == VIEW_TYPE_HEADER){
            headerView.setOnClickListener(this);
            return new WeatherViewholder(headerView);
        } else {
            bodyView.setOnClickListener(this);
            return new WeatherViewholder(bodyView);
        }
    }

    @Override
    public void onBindViewHolder(WeatherViewholder holder, int position) {
        holder.populate(weatherDataList.get(position),position,context,isMetric);
    }

    @Override
    public int getItemCount() {
        return weatherDataList.size();
    }

    @Override
    public void onClick(View view) {
        if (view.getTag() instanceof WeatherData){
            listener.WeatherClicked((WeatherData) view.getTag());
        }
    }

    public interface WeatherListener{
        void WeatherClicked(WeatherData weatherData);
    }
}
