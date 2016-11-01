package beastandroidapps.beastweather.entites;



public class WeatherData{
    private double temperatureMax;
    private double temperatureMin;
    private double pressure;
    private double humidity;
    private String weatherBasic;
    private String weatherDetailed;
    private String weatherDate;
    private String weatherUrl;


    public WeatherData(double temperatureMax, double temperatureMin, double pressure, double humidity, String weatherBasic, String weatherDetailed, String weatherDate, String weatherUrl) {
        this.temperatureMax = temperatureMax;
        this.temperatureMin = temperatureMin;
        this.pressure = pressure;
        this.humidity = humidity;
        this.weatherBasic = weatherBasic;
        this.weatherDetailed = weatherDetailed;
        this.weatherDate = weatherDate;
        this.weatherUrl = weatherUrl;
    }

    public WeatherData() {
    }

    public double getTemperatureMax() {
        return temperatureMax;
    }

    public double getTemperatureMin() {
        return temperatureMin;
    }

    public double getPressure() {
        return pressure;
    }

    public double getHumidity() {
        return humidity;
    }

    public String getWeatherBasic() {
        return weatherBasic;
    }

    public String getWeatherDetailed() {
        return weatherDetailed;
    }

    public String getWeatherDate() {
        return weatherDate;
    }

    public String getWeatherUrl() {
        return weatherUrl;
    }


    public void setWeatherBasic(String weatherBasic) {
        this.weatherBasic = weatherBasic;
    }

    public void setWeatherDetailed(String weatherDetailed) {
        this.weatherDetailed = weatherDetailed;
    }

    public void setWeatherUrl(String weatherUrl) {
        this.weatherUrl = weatherUrl;
    }
}
