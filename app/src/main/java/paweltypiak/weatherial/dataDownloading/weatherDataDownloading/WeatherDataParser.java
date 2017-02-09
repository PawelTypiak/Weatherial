package paweltypiak.weatherial.dataDownloading.weatherDataDownloading;

import android.os.Parcel;
import android.os.Parcelable;
import paweltypiak.weatherial.usefulClasses.UsefulFunctions;
import paweltypiak.weatherial.jsonHandling.Channel;

public class WeatherDataParser implements Parcelable {

    private Channel channel;
    private String link;
    private String city;
    private String country;
    private String region;
    private String lastBuildDate;
    private String chill;
    private String direction;
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

    public WeatherDataParser(Channel channel){
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
    }

    private void initializeLastBuildDate(){
        lastBuildDate=channel.getLastBuildDate();
    }

    private void initializeLocation(){
        city=channel.getLocation().getCity();
        country=channel.getLocation().getCountry();
        region= UsefulFunctions.getFormattedString(channel.getLocation().getRegion());
        latitude=channel.getItem().getLatitude();
        longitude=channel.getItem().getLongitude();
    }

    private void initializeAstronomy(){
        sunrise=channel.getAstronomy().getSunrise();
        sunset=channel.getAstronomy().getSunset();
    }

    private void initializeAtmosphere(){
        humidity=channel.getAtmosphere().getHumidity();
        pressure=channel.getAtmosphere().getPressure();
        visibility=channel.getAtmosphere().getVisibility();
    }

    private void initializeCondition(){
        code=channel.getItem().getCondition().getCode();
        temperature=channel.getItem().getCondition().getTemperature();
    }

    private void initializeForecast(){
        forecastCode = new int[5];
        forecastHighTemperature = new String[5];
        forecastLowTemperature = new String[5];
        forecastCode =channel.getItem().getForecast().getCode();
        forecastHighTemperature =channel.getItem().getForecast().getHigh();
        forecastLowTemperature =channel.getItem().getForecast().getLow();
    }

    private void initizlizeWind(){
        chill=channel.getWind().getChill();
        direction=channel.getWind().getDirection();
        speed=channel.getWind().getSpeed();
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

    //create parcelable
    public WeatherDataParser(Parcel in){
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
        public WeatherDataParser createFromParcel(Parcel in) {
            return new WeatherDataParser(in);
        }

        public WeatherDataParser[] newArray(int size) {
            return new WeatherDataParser[size];
        }
    };
}
