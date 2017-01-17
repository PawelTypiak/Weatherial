package paweltypiak.matweather.mainActivityLayoutInitializing;

import android.app.Activity;
import paweltypiak.matweather.mainActivityLayoutInitializing.LayoutUpdating.OnTimeChangeLayoutUpdater;
import paweltypiak.matweather.mainActivityLayoutInitializing.LayoutUpdating.OnWeatherDataChangeLayoutUpdater;
import paweltypiak.matweather.mainActivityLayoutInitializing.WeatherLayoutInitializing.WeatherLayoutInitializer;
import paweltypiak.matweather.mainActivityLayoutInitializing.appBarInitializing.AppBarLayoutInitializer;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataParser;

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
