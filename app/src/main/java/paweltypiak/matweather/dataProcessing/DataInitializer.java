package paweltypiak.matweather.dataProcessing;

import android.app.Activity;
import android.util.Log;

import paweltypiak.matweather.jsonHandling.Channel;

public class DataInitializer {
    private Channel channel;
    private String link;
    private String city;
    private String country;
    private String region;
    private String lastBuildDate;
    private String refreshTime;
    private String timezone;
    private String chill;
    private String direction;
    private String direction_name;
    private String speed;
    private String humidity;
    private String pressure;
    private String visibility;
    private String sunrise;
    private String sunset;
    private int code;
    private String temperature;
    private int[] forecastCode;
    private String[] forecastHighTemperature;
    private String[] forecastLowTemperature;
    private double latitude;
    private double longitude;
   // private UnitFormatter unitFormatter;
    private Activity activity;
    private int[] units;


    public DataInitializer(Activity activity, Channel channel){
        Log.d("tag", "weather");
        this.activity=activity;
        this.channel=channel;
        initializeLink();
        initializeLastBuildDate();
        initializeLocation();
        initializeAstronomy();
        initializeAtmosphere();
        initializeCondition();
        initializeForecast();
        initizlizeWind();
    }

    private void initializeLink(){
        link=channel.getLink();
        Log.d("weather","link: "+link);
    }

    private void initializeLastBuildDate(){
        lastBuildDate=channel.getLastBuildDate();
        Log.d("weather","lastBuildDate: "+ lastBuildDate);
    }

    private void initializeLocation(){
        city=channel.getLocation().getCity();
        country=channel.getLocation().getCountry();
        region=channel.getLocation().getRegion();
        latitude=channel.getItem().getLatitude();
        longitude=channel.getItem().getLongitude();
        Log.d("weather","location: "+city+", "+region+", "+country+", "+latitude+", "+longitude);
    }

    private void initializeAstronomy(){
        sunrise=channel.getAstronomy().getSunrise().toString();
        sunset=channel.getAstronomy().getSunset().toString();
        Log.d("weather","astronomy: "+sunrise+", "+sunset);
    }

    private void initializeAtmosphere(){
        humidity=channel.getAtmosphere().getHumidity();
        pressure=channel.getAtmosphere().getPressure();
        visibility=channel.getAtmosphere().getVisibility();
        Log.d("weather", "atmosphere: " + humidity+", "+pressure+", "+visibility);
    }

    private void initializeCondition(){
        code=channel.getItem().getCondition().getCode();
        temperature=channel.getItem().getCondition().getTemperature();
        Log.d("weather", "temperature, code: " + temperature+" "+code);
    }

    private void initializeForecast(){
        forecastCode = new int[5];
        forecastHighTemperature = new String[5];
        forecastLowTemperature = new String[5];
        forecastCode =channel.getItem().getForecast().getCode();
        forecastHighTemperature =channel.getItem().getForecast().getHigh();
        forecastLowTemperature =channel.getItem().getForecast().getLow();
        for(int i=0;i<5;i++){
            Log.d("weather", "forecast code, forecastLowTemperature, forecast high: "+forecastCode [i] + ", " +forecastLowTemperature[i]+", "+forecastHighTemperature[i]);
        }
    }

    private void initizlizeWind(){
        chill=channel.getWind().getChill();
        direction=channel.getWind().getDirection();
        speed=channel.getWind().getSpeed();
        Log.d("weather", "wind: " + chill+", "+direction+", "+speed+", "+direction_name);
    }
    public String getLink() {return link;}
    public String getCity() {return city;}
    public String getCountry() {return country;}public String getRegion() {return region;}
    public double getLatitude() {return latitude;}
    public double getLongitude() {return longitude;}
    public String getLastBuildDate() {return lastBuildDate;}
    public String getHumidity() {
        return humidity;
    }
    public String getChill() {
        return chill;
    }
    public String getDirection() {
        return direction;
    }
    public String getSpeed() {
        return speed;
    }
    public String getPressure() {
        return pressure;
    }
    public String getVisibility() {
        return visibility;
    }
    public String getSunrise() {
        return sunrise;
    }
    public String getSunset() {
        return sunset;
    }
    public int getCode() {
        return code;
    }
    public String getTemperature() {
        return temperature;
    }
    public int[] getForecastCode() {
        return forecastCode;
    }
    public String[] getForecastHighTemperature() {
        return forecastHighTemperature;
    }
    public String[] getForecastLowTemperature() {
        return forecastLowTemperature;
    }
}
