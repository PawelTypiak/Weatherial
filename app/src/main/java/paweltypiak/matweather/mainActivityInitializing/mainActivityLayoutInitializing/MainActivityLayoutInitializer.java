package paweltypiak.matweather.mainActivityInitializing.mainActivityLayoutInitializing;

import android.app.Activity;
import paweltypiak.matweather.mainActivityInitializing.mainActivityLayoutInitializing.layoutUpdating.OnTimeChangeLayoutUpdater;
import paweltypiak.matweather.mainActivityInitializing.mainActivityLayoutInitializing.layoutUpdating.OnWeatherDataChangeLayoutUpdater;
import paweltypiak.matweather.mainActivityInitializing.mainActivityLayoutInitializing.weatherLayoutInitializing.WeatherLayoutInitializer;
import paweltypiak.matweather.mainActivityInitializing.mainActivityLayoutInitializing.appBarInitializing.AppBarLayoutInitializer;
import paweltypiak.matweather.dataDownloading.weatherDataDownloading.WeatherDataParser;

public class MainActivityLayoutInitializer {

    private OnTimeChangeLayoutUpdater onTimeChangeLayoutUpdater;
    private AppBarLayoutInitializer appBarLayoutInitializer;
    private WeatherLayoutInitializer weatherLayoutInitializer;
    private OnWeatherDataChangeLayoutUpdater onWeatherDataChangeLayoutUpdater;

    public MainActivityLayoutInitializer(Activity activity){
        initializeAppBarLayout(activity);
        initializeWeatherLayout(activity);
        initializeOnTimeChangeLayoutUpdater(activity);
    }

    private void initializeAppBarLayout(Activity activity){
        appBarLayoutInitializer=new AppBarLayoutInitializer(activity);
    }

    private void initializeWeatherLayout(Activity activity){
        weatherLayoutInitializer=new WeatherLayoutInitializer(
                activity,
                this);
    }

    private void initializeOnTimeChangeLayoutUpdater(Activity activity){
        onTimeChangeLayoutUpdater=new OnTimeChangeLayoutUpdater(
                activity,
                this);
    }

    public void updateLayoutOnWeatherDataChange(final Activity activity,
                                                final WeatherDataParser weatherDataParser,
                                                final boolean doSetAppBar,
                                                final boolean isGeolocalizationMode){
        onWeatherDataChangeLayoutUpdater =new OnWeatherDataChangeLayoutUpdater(
                activity,
                this,
                weatherDataParser,
                doSetAppBar,
                isGeolocalizationMode);
    }

    public AppBarLayoutInitializer getAppBarLayoutInitializer() {
        return appBarLayoutInitializer;
    }

    public WeatherLayoutInitializer getWeatherLayoutInitializer() {
        return weatherLayoutInitializer;
    }

    public OnTimeChangeLayoutUpdater getOnTimeChangeLayoutUpdater() {
        return onTimeChangeLayoutUpdater;
    }

    public OnWeatherDataChangeLayoutUpdater getOnWeatherDataChangeLayoutUpdater() {
        return onWeatherDataChangeLayoutUpdater;
    }
}
