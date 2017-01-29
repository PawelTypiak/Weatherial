package paweltypiak.matweather.mainActivityInitializing.mainActivityLayoutInitializing.weatherLayoutInitializing;

import android.app.Activity;

import paweltypiak.matweather.mainActivityInitializing.mainActivityLayoutInitializing.MainActivityLayoutInitializer;
import paweltypiak.matweather.mainActivityInitializing.mainActivityLayoutInitializing.weatherLayoutInitializing.swipeRefreshLayoutInitializing.SwipeRefreshLayoutInitializer;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataFormatter;

public class WeatherLayoutInitializer {

    private Activity activity;
    private GeneralWeatherLayoutInitializer generalWeatherLayoutInitializer;
    private SwipeRefreshLayoutInitializer swipeRefreshLayoutInitializer;
    private WeatherBasicInfoLayoutInitializer weatherBasicInfoLayoutInitializer;
    private WeatherDetailsLayoutInitializer weatherDetailsLayoutInitializer;
    private WeatherForecastLayoutInitializer weatherForecastLayoutInitializer;

    public WeatherLayoutInitializer(Activity activity,
                                    MainActivityLayoutInitializer mainActivityLayoutInitializer){
        this.activity = activity;
        initializeGeneralWeatherLayout(activity,mainActivityLayoutInitializer);
        initializeSwipeRefreshLayout(activity, mainActivityLayoutInitializer);
        initializeWeatherGeneralInfoLayout(activity, mainActivityLayoutInitializer);
        initializeWeatherDetailsLayout(activity);
        initializeWeatherForecastLayout(activity);
    }

    private void initializeGeneralWeatherLayout(Activity activity,
                                                MainActivityLayoutInitializer mainActivityLayoutInitializer){
        generalWeatherLayoutInitializer=new GeneralWeatherLayoutInitializer(
                activity,
                mainActivityLayoutInitializer);
    }

    private void initializeSwipeRefreshLayout(Activity activity,
                                              MainActivityLayoutInitializer mainActivityLayoutInitializer) {
        swipeRefreshLayoutInitializer = new SwipeRefreshLayoutInitializer(
                activity,
                mainActivityLayoutInitializer,this);
    }

    private void initializeWeatherGeneralInfoLayout(Activity activity, MainActivityLayoutInitializer
            mainActivityLayoutInitializer) {
        weatherBasicInfoLayoutInitializer = new WeatherBasicInfoLayoutInitializer(activity,
                mainActivityLayoutInitializer,this);
    }

    private void initializeWeatherDetailsLayout(Activity activity) {
        weatherDetailsLayoutInitializer = new WeatherDetailsLayoutInitializer(activity);
    }

    private void initializeWeatherForecastLayout(Activity activity) {
        weatherForecastLayoutInitializer = new WeatherForecastLayoutInitializer(activity);
    }

    public void updateWeatherLayoutData(WeatherDataFormatter weatherDataFormatter) {
        updateWeatherGeneralInfoLayoutData(weatherDataFormatter);
        updateWeatherDetailsLayoutData(weatherDataFormatter);
        updateWeatherForecastLayoutData(weatherDataFormatter);
    }

    private void updateWeatherGeneralInfoLayoutData(WeatherDataFormatter weatherDataFormatter) {
        weatherBasicInfoLayoutInitializer.updateWeatherBasicInfoLayoutData(activity, weatherDataFormatter, this);
    }

    private void updateWeatherDetailsLayoutData(WeatherDataFormatter weatherDataFormatter) {
        weatherDetailsLayoutInitializer.updateWeatherDetailsLayoutData(activity, weatherDataFormatter);
    }

    private void updateWeatherForecastLayoutData(WeatherDataFormatter weatherDataFormatter) {
        weatherForecastLayoutInitializer.updateWeatherForecastLayoutData(activity, weatherDataFormatter);
    }

    public void updateWeatherLayoutTheme(boolean isDay) {
        WeatherLayoutThemeColorsUpdater themeColorsUpdater = new WeatherLayoutThemeColorsUpdater(activity, isDay);
        updateWeatherGeneralInfoLayoutTheme(themeColorsUpdater);
        updateWeatherDetailsLayoutTheme(activity, themeColorsUpdater, isDay);
        updateWeatherForecastLayoutTheme(activity, themeColorsUpdater);
    }

    private void updateWeatherGeneralInfoLayoutTheme(WeatherLayoutThemeColorsUpdater themeColorsUpdater) {
        weatherBasicInfoLayoutInitializer.updateWeatherBasicInfoLayoutTheme(activity, themeColorsUpdater);
    }

    private void updateWeatherDetailsLayoutTheme(Activity activity, WeatherLayoutThemeColorsUpdater
            themeColorsUpdater, boolean isDay) {
        weatherDetailsLayoutInitializer.updateWeatherDetailsLayoutTheme(activity, themeColorsUpdater, isDay);
    }

    private void updateWeatherForecastLayoutTheme(Activity activity, WeatherLayoutThemeColorsUpdater
            themeColorsUpdater) {
        weatherForecastLayoutInitializer.updateWeatherForecastLayoutTheme(activity, themeColorsUpdater);
    }

    public GeneralWeatherLayoutInitializer getGeneralWeatherLayoutInitializer() {
        return generalWeatherLayoutInitializer;
    }

    public WeatherDetailsLayoutInitializer getWeatherDetailsLayoutInitializer() {
        return weatherDetailsLayoutInitializer;
    }

    public SwipeRefreshLayoutInitializer getSwipeRefreshLayoutInitializer() {
        return swipeRefreshLayoutInitializer;
    }
}
