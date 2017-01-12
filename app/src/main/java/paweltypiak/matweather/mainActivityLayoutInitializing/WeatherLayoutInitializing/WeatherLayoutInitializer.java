package paweltypiak.matweather.mainActivityLayoutInitializing.WeatherLayoutInitializing;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.RelativeLayout;
import paweltypiak.matweather.R;
import paweltypiak.matweather.mainActivityLayoutInitializing.MainActivityLayoutInitializer;
import paweltypiak.matweather.mainActivityLayoutInitializing.WeatherLayoutInitializing.swipeRefreshLayoutInitializing.SwipeRefreshLayoutInitializer;
import paweltypiak.matweather.usefulClasses.DialogInitializer;
import paweltypiak.matweather.mainActivityLayoutInitializing.LayoutUpdating.OnWeatherDataChangeLayoutUpdater;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataFormatter;

public class WeatherLayoutInitializer {

    private Activity activity;
    private SwipeRefreshLayoutInitializer swipeRefreshLayoutInitializer;
    private WeatherGeneralInfoLayoutInitializer weatherGeneralInfoLayoutInitializer;
    private WeatherDetailsLayoutInitializer weatherDetailsLayoutInitializer;
    private WeatherForecastLayoutInitializer weatherForecastLayoutInitializer;

    public WeatherLayoutInitializer(Activity activity,
                                    DialogInitializer dialogInitializer,
                                    MainActivityLayoutInitializer mainActivityLayoutInitializer){
        this.activity = activity;
        setWeatherLayoutOnClickListener(dialogInitializer);
        initializeSwipeRefreshLayout(activity, dialogInitializer, mainActivityLayoutInitializer);
        initializeWeatherGeneralInfoLayout(activity, mainActivityLayoutInitializer);
        initializeWeatherDetailsLayout(activity);
        initializeWeatherForecastLayout(activity);
    }

    private void initializeSwipeRefreshLayout(Activity activity,
                                              DialogInitializer dialogInitializer,
                                              MainActivityLayoutInitializer mainActivityLayoutInitializer) {
        swipeRefreshLayoutInitializer = new SwipeRefreshLayoutInitializer(
                activity,
                dialogInitializer,
                mainActivityLayoutInitializer);
    }

    private void setWeatherLayoutOnClickListener(final DialogInitializer dialogInitializer) {
        RelativeLayout weatherLayout = (RelativeLayout) activity.findViewById(R.id.weather_layout);
        weatherLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String yahooWeatherLink = OnWeatherDataChangeLayoutUpdater.getCurrentDataFormatter().getLink();
                AlertDialog yahooWeatherRedirectDialog = dialogInitializer.initializeYahooRedirectDialog(1,
                        yahooWeatherLink);
                yahooWeatherRedirectDialog.show();
            }
        });
    }

    private void initializeWeatherGeneralInfoLayout(Activity activity, MainActivityLayoutInitializer
            mainActivityLayoutInitializer) {
        weatherGeneralInfoLayoutInitializer = new WeatherGeneralInfoLayoutInitializer(activity,
                mainActivityLayoutInitializer);
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
        weatherGeneralInfoLayoutInitializer.updateWeatherGeneralInfoLayoutData(activity, weatherDataFormatter, this);
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
        weatherGeneralInfoLayoutInitializer.updateWeatherGeneralInfoLayoutTheme(activity, themeColorsUpdater);
    }

    private void updateWeatherDetailsLayoutTheme(Activity activity, WeatherLayoutThemeColorsUpdater
            themeColorsUpdater, boolean isDay) {
        weatherDetailsLayoutInitializer.updateWeatherDetailsLayoutTheme(activity, themeColorsUpdater, isDay);
    }

    private void updateWeatherForecastLayoutTheme(Activity activity, WeatherLayoutThemeColorsUpdater
            themeColorsUpdater) {
        weatherForecastLayoutInitializer.updateWeatherForecastLayoutTheme(activity, themeColorsUpdater);
    }

    public WeatherDetailsLayoutInitializer getWeatherDetailsLayoutInitializer() {
        return weatherDetailsLayoutInitializer;
    }

    public SwipeRefreshLayoutInitializer getSwipeRefreshLayoutInitializer() {
        return swipeRefreshLayoutInitializer;
    }
}
