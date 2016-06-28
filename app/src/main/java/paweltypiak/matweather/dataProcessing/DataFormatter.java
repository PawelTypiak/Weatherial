package paweltypiak.matweather.dataProcessing;

import android.util.Log;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import paweltypiak.matweather.jsonHandling.Channel;

public class DataFormatter {
    private Channel channel;
    private int chill;
    private String direction;
    private String direction_name;
    private int speed;
    private int humidity;
    private String pressure;
    private String visibility;
    private String sunrise;
    private String sunset;
    private int code;
    private int temperature;
    private int[] forecast_code;
    private int[] forecast_high;
    private int[] forecast_low;

    public DataFormatter(Channel channel){
        Log.d("tag", "weather");
        this.channel=channel;
        //format all the data
        formatAstronomy();
        formatAtmosphere();
        formatCondition();
        formatForecast();
        formatWind();
    }

    public void formatAstronomy(){
        DateFormat df = new SimpleDateFormat("hh:mm a");
        DateFormat outputformat= new SimpleDateFormat("HH:mm");
        Date date;
        sunrise=channel.getAstronomy().getSunrise().toString();
        sunset=channel.getAstronomy().getSunset().toString();
        try{
            date= df.parse(sunrise);
            sunrise = outputformat.format(date);
            date=df.parse(sunset);
            sunset=outputformat.format(date);
        }catch(ParseException pe){
            pe.printStackTrace();
        }
    }

    public void formatAtmosphere(){
        humidity=channel.getAtmosphere().getHumidity();
        pressure=channel.getAtmosphere().getPressure();
        int dot_number = pressure.indexOf(".");
        if (dot_number != -1)
            pressure= pressure.substring(0 , dot_number); //this will give abc
        visibility=channel.getAtmosphere().getVisibility();
        Log.d("weather", "atmosphere: " + humidity+", "+pressure+", "+visibility);
    }

    public void formatCondition(){
        code=channel.getItem().getCondition().getCode();
        temperature=channel.getItem().getCondition().getTemperature();
        temperature=(int)(0.55*(temperature-32));
        Log.d("weather", "temperature: " + temperature);
    }

    public void formatForecast(){
        forecast_code = new int[5];
        forecast_high = new int[5];
        forecast_low = new int[5];
        forecast_code=channel.getItem().getForecast().getCode();
        forecast_high=channel.getItem().getForecast().getHigh();
        forecast_low=channel.getItem().getForecast().getLow();
        for(int i=0;i<5;i++){
            forecast_low[i]=(int)(0.55*(forecast_low[i]-32));
            forecast_high[i]=(int)(0.55*(forecast_high[i]-32));
            Log.d("weather", "forecast: " + forecast_code[i]+", "+forecast_low[i]+", "+forecast_high[i]);
        }
    }
    public void formatWind(){
        chill=channel.getWind().getChill();
        direction=channel.getWind().getDirection();
        speed=channel.getWind().getSpeed();
        if( Integer.parseInt(direction)<11 ||Integer.parseInt(direction)>349 )
            direction_name="N";
        else if( Integer.parseInt(direction)>=11 &&Integer.parseInt(direction)<34 )
            direction_name="NNE";
        else if( Integer.parseInt(direction)>=34 &&Integer.parseInt(direction)<56 )
            direction_name="NE";
        else if( Integer.parseInt(direction)>=56 &&Integer.parseInt(direction)<79 )
            direction_name="ENE";
        else if( Integer.parseInt(direction)>=79 &&Integer.parseInt(direction)<101 )
            direction_name="E";
        else if( Integer.parseInt(direction)>=101 &&Integer.parseInt(direction)<124 )
            direction_name="ESE";
        else if( Integer.parseInt(direction)>=124 &&Integer.parseInt(direction)<146 )
            direction_name="SE";
        else if( Integer.parseInt(direction)>=146 &&Integer.parseInt(direction)<169 )
            direction_name="SSE";
        else if( Integer.parseInt(direction)>=169 &&Integer.parseInt(direction)<191 )
            direction_name="S";
        else if( Integer.parseInt(direction)>=191 &&Integer.parseInt(direction)<213 )
            direction_name="SSW";
        else if( Integer.parseInt(direction)>=213 &&Integer.parseInt(direction)<236 )
            direction_name="SW";
        else if( Integer.parseInt(direction)>=236 &&Integer.parseInt(direction)<258)
            direction_name="WSW";
        else if( Integer.parseInt(direction)>=258 &&Integer.parseInt(direction)<281 )
            direction_name="W";
        else if (Integer.parseInt(direction) >= 281 && Integer.parseInt(direction)<304 )
            direction_name="WNW";
        else if( Integer.parseInt(direction)>=304 &&Integer.parseInt(direction)<326 )
            direction_name="NW";
        else if( Integer.parseInt(direction)>=326 &&Integer.parseInt(direction)< 349)
            direction_name="NNW";

        chill=(int)(0.55*(chill-32));
        speed=(8/5)*speed;
        Log.d("weather", "wind: " + chill+", "+direction+", "+speed+direction_name);
    }

    public int getHumidity() {
        return humidity;
    }
    public int getChill() {
        return chill;
    }
    public String getDirection() {
        return direction;
    }
    public String getDirection_name(){
        return direction_name;
    }
    public int getSpeed() {
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
    public int getTemperature() {
        return temperature;
    }
    public int[] getForecast_code() {
        return forecast_code;
    }
    public int[] getForecast_high() {
        return forecast_high;
    }
    public int[] getForecast_low() {
        return forecast_low;
    }
}
