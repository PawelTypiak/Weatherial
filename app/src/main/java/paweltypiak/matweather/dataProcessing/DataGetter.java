package paweltypiak.matweather.dataProcessing;

import android.util.Log;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import paweltypiak.matweather.jsonHandling.Channel;

public class DataGetter {
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

    public DataGetter(Channel channel){
        Log.d("tag", "weather");
        this.channel=channel;
        formatAstronomy();
        formatAtmosphere();
        formatCondition();
        formatForecast();
        formatWind();
    }

    public void formatAstronomy(){
        DateFormat inputFormat = new SimpleDateFormat("hh:mm a");
        DateFormat outputFormat= new SimpleDateFormat("HH:mm");
        Date date;
        sunrise=channel.getAstronomy().getSunrise().toString();
        sunset=channel.getAstronomy().getSunset().toString();
        try{
            date= inputFormat.parse(sunrise);
            sunrise = outputFormat.format(date);
            date=inputFormat.parse(sunset);
            sunset=outputFormat.format(date);
        }catch(ParseException pe){
            pe.printStackTrace();
        }
    }

    public void formatAtmosphere(){
        humidity=channel.getAtmosphere().getHumidity();
        pressure=channel.getAtmosphere().getPressure();
        int dot_number = pressure.indexOf(".");
        if (dot_number != -1)
            pressure= pressure.substring(0 , dot_number);
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
        chill=(int)(0.55*(chill-32));
        speed=(8/5)*speed;
        Log.d("weather", "wind: " + chill+", "+direction+", "+speed+", "+direction_name);
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
