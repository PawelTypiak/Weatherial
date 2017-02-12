/*******************************************************************************
 * Copyright 2017 Paweł Typiak
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package paweltypiak.weatherial.dataDownloading.weatherDataDownloading;

import android.os.Parcel;
import android.os.Parcelable;
import paweltypiak.weatherial.utils.UsefulFunctions;
import paweltypiak.weatherial.dataDownloading.jsonHandling.weatherServiceTagTreeInitializing.Channel;

public class WeatherDataParser implements Parcelable {

    private Channel channel;
    private String link;
    private String city;
    private String country;
    private String region;
    private String lastBuildDate;
    private String direction;
    private String speed;
    private String humidity;
    private String pressure;
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
        initializeWind();
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

    private void initializeWind(){
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
    public String getDirection() {
        return direction;
    }
    public String getSpeed() {
        return speed;
    }
    public String getPressure() {
        return pressure;
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
        String[] stringData = new String[15];
        in.readStringArray(stringData);
        this.link=stringData[0];
        this.city=stringData[1];
        this.region=stringData[2];
        this.country=stringData[3];
        this.latitude=Double.parseDouble(stringData[4]);
        this.longitude=Double.parseDouble(stringData[5]);
        this.lastBuildDate=stringData[6];
        this.humidity=stringData[7];
        this.direction=stringData[8];
        this.speed=stringData[9];
        this.pressure=stringData[10];
        this.sunrise=stringData[11];
        this.sunset=stringData[12];
        this.code=Integer.parseInt(stringData[13]);
        this.temperature=stringData[14];
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
                        this.direction,
                        this.speed,
                        this.pressure,
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
