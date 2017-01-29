package paweltypiak.matweather.mainActivityInitializing.mainActivityDataDownloading;

import android.app.Activity;
import android.os.Bundle;

import paweltypiak.matweather.mainActivityInitializing.mainActivityDataDownloading.mainActivityGeolocalizationDownloading.MainActivityGeolocalizationDownloader;
import paweltypiak.matweather.mainActivityInitializing.mainActivityDataDownloading.mainActivityWeatherDataDownloading.MainActivityWeatherDataDownloader;
import paweltypiak.matweather.mainActivityInitializing.mainActivityLayoutInitializing.MainActivityLayoutInitializer;

public class MainActivityDataDownloader {

    private MainActivityWeatherDataDownloader weatherDataDownloader;
    private MainActivityGeolocalizationDownloader geolocalizationDownloader;

    public MainActivityDataDownloader(Activity activity,
                                      Bundle savedInstanceState,
                                      MainActivityLayoutInitializer layoutInitializer){
        this.geolocalizationDownloader=new MainActivityGeolocalizationDownloader(activity,this);
        this.weatherDataDownloader
                =new MainActivityWeatherDataDownloader(activity, savedInstanceState,layoutInitializer,this);
    }

    public MainActivityWeatherDataDownloader getWeatherDataDownloader() {
        return weatherDataDownloader;
    }

    public MainActivityGeolocalizationDownloader getGeolocalizationDownloader() {
        return geolocalizationDownloader;
    }
}
