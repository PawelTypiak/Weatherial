package paweltypiak.matweather.dataProcessing;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import paweltypiak.matweather.jsonHandling.Channel;

public class DataInitializer implements Parcelable {
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
    public String getRegion() {return region;}
    public String getCountry() {return country;}
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


    public DataInitializer(Parcel in){
        String[] stringData = new String[17];
        in.readStringArray(stringData);
        this.link=stringData[0];
        this.city=stringData[1];
        this.region=stringData[2];
        this.country=stringData[3];
        this.latitude=Double.parseDouble(stringData[4]);
        this.longitude=Double.parseDouble(stringData[5]);
        this.lastBuildDate=stringData[6];
        this.humidity=stringData[7];
        this.chill=stringData[8];
        this.direction=stringData[9];
        this.speed=stringData[10];
        this.pressure=stringData[11];
        this.visibility=stringData[12];
        this.sunrise=stringData[13];
        this.sunset=stringData[14];
        this.code=Integer.parseInt(stringData[15]);
        this.temperature=stringData[16];
        this.forecastCode=in.createIntArray();
        this.forecastHighTemperature=in.createStringArray();
        this.forecastLowTemperature=in.createStringArray();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeStringArray(new String[]
                {
                        this.link,
                        this.city,
                        this.region,
                        this.country,
                        String.valueOf(this.latitude),
                        String.valueOf(this.longitude),
                        this.lastBuildDate,
                        this.humidity,
                        this.chill,
                        this.direction,
                        this.speed,
                        this.pressure,
                        this.visibility,
                        this.sunrise,
                        this.sunset,
                        String.valueOf(this.code),
                        this.temperature,
                });
        dest.writeIntArray(this.forecastCode);
        dest.writeStringArray(this.forecastHighTemperature);
        dest.writeStringArray(this.forecastLowTemperature);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public DataInitializer createFromParcel(Parcel in) {
            return new DataInitializer(in);
        }

        public DataInitializer[] newArray(int size) {
            return new DataInitializer[size];
        }
    };
}
