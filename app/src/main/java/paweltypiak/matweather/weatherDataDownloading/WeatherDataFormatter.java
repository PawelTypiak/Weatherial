package paweltypiak.matweather.weatherDataDownloading;

import android.app.Activity;
import android.text.format.DateFormat;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import paweltypiak.matweather.R;
import paweltypiak.matweather.usefulClasses.SharedPreferencesModifier;

public class WeatherDataFormatter {
    private int[] units;
    private Activity activity;
    private String chill;
    private String direction;
    private String directionName;
    private String speed;
    private String humidity;
    private String pressure;
    private String visibility;
    private String sunrise;
    private String sunset;
    private String sunrise24;
    private String sunset24;
    private int code;
    private String temperature;
    private int[] forecastCode;
    private String[] forecastHighTemperature;
    private String[] forecastLowTemperature;
    private String link;
    private String city;
    private String country;
    private String region;
    private String time;
    private String time24;
    private String unformattedTime;
    private String timezone;
    private String lastBuildDate;
    private double latitude;
    private double longitude;
    private boolean isDay;
    private long currentDiffMinutes;
    private long sunsetSunriseDiffMinutes;
    private WeatherDataInitializer dataInitializer;
    private int hourDifference;

    public WeatherDataFormatter(Activity activity, WeatherDataInitializer dataInitializer){
        this.units= SharedPreferencesModifier.getUnits(activity);
        this.activity=activity;
        this.dataInitializer=dataInitializer;
        getData();
        formatData();
    }

    private void formatData(){
        formatLastBuildDate();
        formatAstronomy();
        formatAtmosphere();
        formatCondition();


        formatForecast();
        formatWind();
        countSunPosition(time24);
        countHourDifference();
    }

    private void formatLastBuildDate(){
        lastBuildDate=lastBuildDate.substring(17);
        unformattedTime =lastBuildDate.substring(0,8);
        time =formatTimeUnit(unformattedTime);
        time24=get24Time(unformattedTime);
        timezone=lastBuildDate.substring(9);
    }

    private void formatAtmosphere(){
        humidity=humidity+" "+"%";
        pressure=formatPressureUnit(pressure);
        visibility=formatDistanceUnit(visibility);
    }

    private void formatAstronomy(){
        sunrise24=get24Time(sunrise);
        sunset24=get24Time(sunset);
        sunrise=formatTimeUnit(sunrise);
        sunset=formatTimeUnit(sunset);
    }

    private void formatCondition(){
        temperature=formatTemperatureUnit(temperature)+"\u00B0";
    }

    private void formatForecast(){
        String dagreesSymbol="\u00B0";
        for(int i=0;i<5;i++){
            forecastLowTemperature[i]=formatTemperatureUnit(forecastLowTemperature[i])+dagreesSymbol;
            forecastHighTemperature[i]=formatTemperatureUnit(forecastHighTemperature[i])+dagreesSymbol;
        }
    }

    private void formatWind(){
        if( Integer.parseInt(direction)<22 ||Integer.parseInt(direction)>=337 )
            directionName="N";
        else if( Integer.parseInt(direction)>=22 &&Integer.parseInt(direction)<67 )
            directionName="NE";
        else if( Integer.parseInt(direction)>=67 &&Integer.parseInt(direction)<112 )
            directionName="E";
        else if( Integer.parseInt(direction)>=112 &&Integer.parseInt(direction)<157 )
            directionName="SE";
        else if( Integer.parseInt(direction)>=157 &&Integer.parseInt(direction)<202 )
            directionName="S";
        else if( Integer.parseInt(direction)>=202 &&Integer.parseInt(direction)<247 )
            directionName="SW";
        else if( Integer.parseInt(direction)>=247 &&Integer.parseInt(direction)<292 )
            directionName="W";
        else if( Integer.parseInt(direction)>=292 &&Integer.parseInt(direction)<337 )
            directionName="NW";
        chill=formatTemperatureUnit(chill)+"\u00B0";
        speed= formatSpeedUnit(speed);
    }

    private void countHourDifference() {
        String timeHour=null;
        String actualTimeHour=null;
        SimpleDateFormat inputFormat=new SimpleDateFormat("H:mm");
        SimpleDateFormat outputFormat = new SimpleDateFormat("H");
        Date date;
        Calendar calendar= Calendar.getInstance();
        try {
            actualTimeHour = DateFormat.format("H", calendar).toString();
            date = inputFormat.parse(time24);
            timeHour = outputFormat.format(date);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        hourDifference=Integer.parseInt(timeHour)-Integer.parseInt(actualTimeHour);
    }

    public String[] countSunPosition(String currentTime){
        Date sunriseHour=null;
        Date sunsetHour=null;
        Date beforeMidnight=null;
        Date afterMidnight=null;
        Date now=null;
        long sunsetSunriseDifference;
        long currentDifference;
        SimpleDateFormat inputFormat= new SimpleDateFormat("H:mm");
        try{
            sunriseHour= inputFormat.parse(sunrise24);
            sunsetHour= inputFormat.parse(sunset24);
            now=inputFormat.parse(currentTime);
        }catch(ParseException pe){
            pe.printStackTrace();
        }
        if((now.after(sunriseHour)&&now.before(sunsetHour))||now.equals(sunriseHour)||now.equals(sunsetHour)){
            isDay =true;
            sunsetSunriseDifference = Math.abs(sunsetHour.getTime() - sunriseHour.getTime());
            currentDifference= Math.abs(now.getTime()-sunriseHour.getTime());
        }
        else {
            isDay=false;
            try{
                beforeMidnight=inputFormat.parse("23:59");
                afterMidnight=inputFormat.parse("0:00");
            }catch(ParseException pe){
                pe.printStackTrace();
            }
            long twentyFourHours = Math.abs(beforeMidnight.getTime()-afterMidnight.getTime());
            sunsetSunriseDifference=twentyFourHours-Math.abs(sunsetHour.getTime() - sunriseHour.getTime());
            if(now.before(sunriseHour)){
                currentDifference= sunsetSunriseDifference-Math.abs(now.getTime() - sunriseHour.getTime());
            }
            else {
                currentDifference= Math.abs(now.getTime() - sunsetHour.getTime());
            }
        }
        sunsetSunriseDiffMinutes = sunsetSunriseDifference / (60*1000);
        currentDiffMinutes = currentDifference / (60*1000);
        String outputString[]={Long.toString(sunsetSunriseDiffMinutes),Long.toString(currentDiffMinutes),Integer.toString((isDay)? 1 : 0)};
        return outputString;
    }

    private String get24Time(String time){
        String time24=null;
        SimpleDateFormat inputFormat = new SimpleDateFormat("h:mm a");
        SimpleDateFormat outputFormat = new SimpleDateFormat("H:mm");
        Date date;
        try {
            date = inputFormat.parse(time);
            time24 = outputFormat.format(date);
        } catch (ParseException pe) {
            pe.printStackTrace();
        }
        return time24;
    }

    private String formatTemperatureUnit(String temperature){
        if(units[0]==0){
            temperature=Integer.toString((int)Math.round(0.55*(Integer.parseInt(temperature)-32)));
        }
        return temperature;
    }
    private String formatSpeedUnit(String speed){
        if(units[1]==0){
            speed=Integer.toString((int)Math.round(0.625*Integer.parseInt(speed)))+" "+activity.getResources().getString(R.string.speed_kph);
        }
        else speed=speed+" "+activity.getResources().getString(R.string.speed_mph);
        return speed;
    }
    private String formatDistanceUnit(String distance){
        if (distance.indexOf(".") != -1) distance= distance.substring(0 , distance.indexOf("."));
        if(units[2]==0) distance=distance+" "+activity.getResources().getString(R.string.distance_km);
        else {
            distance=Integer.toString((int)Math.round(0.62*Double.parseDouble(distance)));
            distance=distance+" "+activity.getResources().getString(R.string.distance_mi);
        }
        return distance;
    }
    private String formatPressureUnit(String pressure){
        if (pressure.indexOf(".") != -1) pressure= pressure.substring(0 , pressure.indexOf("."));
        if(units[3]==0){
            pressure=pressure+" "+activity.getResources().getString(R.string.pressure_hpa);
        }
        else {
            pressure=Integer.toString((int)Math.round(0.0295*Integer.parseInt(pressure)));
            pressure=pressure+" "+activity.getResources().getString(R.string.pressure_mmHg);
        }
        return pressure;
    }
    private String formatTimeUnit(String time) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("hh:mm a");
        if (units[4] == 0) {
            SimpleDateFormat outputFormat = new SimpleDateFormat("H:mm");
            Date date;
            try {
                date = inputFormat.parse(time);
                time = outputFormat.format(date);
            } catch (ParseException pe) {
                pe.printStackTrace();
            }
        }
        else {
            SimpleDateFormat outputFormat = new SimpleDateFormat("h:mm a");
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

    private void getData(){
        link=dataInitializer.getLink();
        city=dataInitializer.getCity();
        country=dataInitializer.getCountry();
        region=dataInitializer.getRegion();
        latitude=dataInitializer.getLatitude();
        longitude=dataInitializer.getLongitude();
        lastBuildDate=dataInitializer.getLastBuildDate();
        chill = dataInitializer.getChill();
        direction= dataInitializer.getDirection();
        speed= dataInitializer.getSpeed();
        humidity= dataInitializer.getHumidity();
        pressure= dataInitializer.getPressure();
        visibility= dataInitializer.getVisibility();
        sunrise= dataInitializer.getSunrise();
        sunset= dataInitializer.getSunset();
        code= dataInitializer.getCode();
        temperature= dataInitializer.getTemperature();
        forecastCode = dataInitializer.getForecastCode().clone();
        forecastHighTemperature = dataInitializer.getForecastHighTemperature().clone();
        forecastLowTemperature = dataInitializer.getForecastLowTemperature().clone();
    }

    public String getChill() {return chill;}
    public String getDirection() {return direction;}
    public String getDirectionName() {return directionName;}
    public String getSpeed() {return speed;}
    public String getHumidity() {return humidity;}
    public String getPressure() {return pressure;}
    public String getVisibility() {return visibility;}
    public String getSunrise() {return sunrise;}
    public String getSunset() {return sunset;}
    public int getCode() {return code;}
    public String getTemperature() {return temperature;}
    public int[] getForecastCode() {return forecastCode;}
    public String[] getForecastHighTemperature() {return forecastHighTemperature;}
    public String[] getForecastLowTemperature() {return forecastLowTemperature;}
    public String getLink() {return link;}
    public String getCity() {return city;}
    public String getCountry() {return country;}
    public String getRegion() {return region;}
    public String getTimezone() {return timezone;}
    public double getLatitude() {return latitude;}
    public double getLongitude() {return longitude;}
    public boolean getDay() {return isDay;}
    public long getCurrentDiffMinutes() {return currentDiffMinutes;}
    public long getSunsetSunriseDiffMinutes() {return sunsetSunriseDiffMinutes;}
    public int getHourDifference(){return hourDifference;}
}
