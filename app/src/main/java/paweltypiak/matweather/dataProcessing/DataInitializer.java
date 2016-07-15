package paweltypiak.matweather.dataProcessing;

import android.app.Activity;
import android.util.Log;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import paweltypiak.matweather.R;
import paweltypiak.matweather.jsonHandling.Channel;

public class DataInitializer {
    private Channel channel;
    private String link;
    private String city;
    private String country;
    private String region;
    private String lastBuildDate;
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
    private int[] forecast_code;
    private String[] forecast_high;
    private String[] forecast_low;
    private double latitude;
    private double longitude;
    private UnitFormatter unitFormatter;
    private Activity activity;


    public DataInitializer(Activity activity, Channel channel, int[] units){
        Log.d("tag", "weather");
        this.activity=activity;
        this.channel=channel;
        unitFormatter=new UnitFormatter(units);
        initializeLink();
        initializeLastBuildDate();
        initializeLocation();
        initializeAstronomy();
        initializeAtmosphere();
        initializeCondition();
        initializeForecast();
        initizlizeWind();
    }

    public class UnitFormatter {
        private int timeUnit;
        private double temperatureUnit;
        private double speedUnit;
        private double distanceUnit;
        private double pressureUnit;
        private int[] units;

        public UnitFormatter(int[] units) {
            this.units = new int[5];
            this.units = units;

        }

        private String formatTime(String time) {
            if (units[0] == 0) {
                SimpleDateFormat inputFormat = new SimpleDateFormat("hh:mm a");
                SimpleDateFormat outputFormat = new SimpleDateFormat("HH:mm");
                Date date;
                try {
                    date = inputFormat.parse(time);
                    time = outputFormat.format(date);

                } catch (ParseException pe) {
                    pe.printStackTrace();
                }

            }
            return time;
        }
        private String formatTemperature(String temperature){
            if(units[1]==0){
                temperature=Integer.toString((int)Math.round(0.55*(Integer.parseInt(temperature)-32)));
            }
            return temperature;
        }
        private String formatSpeed(String speed){
            if(units[2]==0){
                speed=Integer.toString((int)Math.round(0.625*Integer.parseInt(speed)))+" "+activity.getResources().getString(R.string.speed_kmh);
            }
            else speed=speed+" "+activity.getResources().getString(R.string.speed_mph);
            return speed;
        }
        private String formatDistance(String distance){
            if (distance.indexOf(".") != -1) distance= distance.substring(0 , distance.indexOf("."));
            if(units[3]==0) distance=distance+" "+activity.getResources().getString(R.string.distance_km);
            else {
                distance=Integer.toString((int)Math.round(0.62*Double.parseDouble(distance)));
                distance=distance+" "+activity.getResources().getString(R.string.distance_mi);
            }
            return distance;
        }
        private String formatPressure(String pressure){
            if (pressure.indexOf(".") != -1) pressure= pressure.substring(0 , pressure.indexOf("."));
            if(units[4]==0){
                pressure=pressure+" "+activity.getResources().getString(R.string.pressure_hpa);
            }
            else {
                pressure=Integer.toString((int)Math.round(0.0295*Integer.parseInt(pressure)));
                pressure=pressure+" "+activity.getResources().getString(R.string.pressure_mmHg);
            }
            return pressure;
        }
    }
    private void initializeLink(){

        link=channel.getLink();
        Log.d("weather","link: "+link);
    }

    private void initializeLastBuildDate(){
        lastBuildDate=channel.getLastBuildDate();
        lastBuildDate=lastBuildDate.substring(17);
        String time=lastBuildDate.substring(0,8);
        time=unitFormatter.formatTime(time);
        lastBuildDate=time+lastBuildDate.substring(8);
        Log.d("weather","lastBuildDate: "+lastBuildDate);
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
        sunrise=unitFormatter.formatTime(sunrise);
        sunset=channel.getAstronomy().getSunset().toString();
        sunset=unitFormatter.formatTime(sunset);
        Log.d("weather","astronomy: "+sunrise+", "+sunset);
    }

    private void initializeAtmosphere(){
        humidity=channel.getAtmosphere().getHumidity();
        humidity=humidity+" "+"%";
        pressure=channel.getAtmosphere().getPressure();
        pressure=unitFormatter.formatPressure(pressure);
        visibility=channel.getAtmosphere().getVisibility();
        visibility=unitFormatter.formatDistance(visibility);
        Log.d("weather", "atmosphere: " + humidity+", "+pressure+", "+visibility);
    }

    private void initializeCondition(){
        code=channel.getItem().getCondition().getCode();
        temperature=channel.getItem().getCondition().getTemperature();
        temperature=unitFormatter.formatTemperature(temperature)+"\u00B0";
        Log.d("weather", "temperature: " + temperature);
    }

    private void initializeForecast(){
        forecast_code = new int[5];
        forecast_high = new String[5];
        forecast_low = new String[5];
        forecast_code=channel.getItem().getForecast().getCode();
        forecast_high=channel.getItem().getForecast().getHigh();
        forecast_low=channel.getItem().getForecast().getLow();
        for(int i=0;i<5;i++){
            forecast_low[i]=unitFormatter.formatTemperature(forecast_low[i])+"\u00B0";
            forecast_high[i]=unitFormatter.formatTemperature(forecast_high[i])+"\u00B0";
            Log.d("weather", "forecast: " + forecast_code[i]+", "+forecast_low[i]+", "+forecast_high[i]);
        }
    }

    private void initizlizeWind(){
        chill=channel.getWind().getChill();
        direction=channel.getWind().getDirection();
        if( Integer.parseInt(direction)<22 ||Integer.parseInt(direction)>=337 )
            direction_name="N";
        else if( Integer.parseInt(direction)>=22 &&Integer.parseInt(direction)<67 )
            direction_name="NE";
        else if( Integer.parseInt(direction)>=67 &&Integer.parseInt(direction)<112 )
            direction_name="E";
        else if( Integer.parseInt(direction)>=112 &&Integer.parseInt(direction)<157 )
            direction_name="SE";
        else if( Integer.parseInt(direction)>=157 &&Integer.parseInt(direction)<202 )
            direction_name="S";
        else if( Integer.parseInt(direction)>=202 &&Integer.parseInt(direction)<247 )
            direction_name="SW";
        else if( Integer.parseInt(direction)>=247 &&Integer.parseInt(direction)<292 )
            direction_name="W";
        else if( Integer.parseInt(direction)>=292 &&Integer.parseInt(direction)<337 )
            direction_name="NW";
        speed=channel.getWind().getSpeed();
        chill=unitFormatter.formatTemperature(chill)+"\u00B0";
        speed=unitFormatter.formatSpeed(speed);
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
    public String getDirection_name(){
        return direction_name;
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
    public int[] getForecast_code() {
        return forecast_code;
    }
    public String[] getForecast_high() {
        return forecast_high;
    }
    public String[] getForecast_low() {
        return forecast_low;
    }
}
