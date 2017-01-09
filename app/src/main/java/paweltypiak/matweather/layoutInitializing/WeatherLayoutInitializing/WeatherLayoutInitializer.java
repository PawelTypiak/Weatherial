package paweltypiak.matweather.layoutInitializing.WeatherLayoutInitializing;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.RelativeLayout;

import paweltypiak.matweather.R;
import paweltypiak.matweather.layoutInitializing.WeatherLayoutInitializing.swipeRefreshLayoutInitializing.SwipeRefreshLayoutInitializer;
import paweltypiak.matweather.layoutInitializing.appBarInitializing.AppBarLayoutInitializer;
import paweltypiak.matweather.usefulClasses.DialogInitializer;
import paweltypiak.matweather.layoutInitializing.LayoutUpdating.OnWeatherDataChangeLayoutUpdater;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataFormatter;

public class WeatherLayoutInitializer {

    private Activity activity;
    private SwipeRefreshLayoutInitializer swipeRefreshLayoutInitializer;
    private WeatherGeneralInfoLayoutInitializer weatherGeneralInfoLayoutInitializer;
    private WeatherDetailsLayoutInitializer weatherDetailsLayoutInitializer;
    private WeatherForecastLayoutInitializer weatherForecastLayoutInitializer;

    public WeatherLayoutInitializer(Activity activity,
                                    DialogInitializer dialogInitializer,
                                    AppBarLayoutInitializer appBarLayoutInitializer){
        this.activity=activity;
        initializeSwipeRefreshLayout(activity,dialogInitializer,appBarLayoutInitializer);
        setWeatherLayoutOnClickListener(dialogInitializer);
        initializeWeatherGeneralInfoLayout(activity,appBarLayoutInitializer);
        initializeWeatherDetailsLayout(activity);
        initializeWeatherForecastLayout(activity);
    }

    private void initializeSwipeRefreshLayout(Activity activity,DialogInitializer dialogInitializer,AppBarLayoutInitializer appBarLayoutInitializer){
        swipeRefreshLayoutInitializer=new SwipeRefreshLayoutInitializer(activity,dialogInitializer,appBarLayoutInitializer);
    }

    private void setWeatherLayoutOnClickListener(final DialogInitializer dialogInitializer){
        RelativeLayout weatherLayout=(RelativeLayout)activity.findViewById(R.id.weather_layout);
        weatherLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String yahooWeatherLink = OnWeatherDataChangeLayoutUpdater.getCurrentDataFormatter().getLink();
                AlertDialog yahooWeatherRedirectDialog= dialogInitializer.initializeYahooRedirectDialog(1, yahooWeatherLink);
                yahooWeatherRedirectDialog.show();
            }
        });
    }

    private void initializeWeatherGeneralInfoLayout(Activity activity,AppBarLayoutInitializer appBarLayoutInitializer){
        weatherGeneralInfoLayoutInitializer=new WeatherGeneralInfoLayoutInitializer(activity,appBarLayoutInitializer);
    }

    private void initializeWeatherDetailsLayout(Activity activity){
        weatherDetailsLayoutInitializer=new WeatherDetailsLayoutInitializer(activity);
    }

    private void initializeWeatherForecastLayout(Activity activity){
        weatherForecastLayoutInitializer=new WeatherForecastLayoutInitializer(activity);
    }

    public void updateWeatherLayoutData(WeatherDataFormatter weatherDataFormatter){
        updateWeatherGeneralInfoLayoutData(weatherDataFormatter,swipeRefreshLayoutInitializer);
        updateWeatherDetailsLayoutData(weatherDataFormatter);
        updateWeatherForecastLayoutData(weatherDataFormatter);
    }

    private void updateWeatherGeneralInfoLayoutData(WeatherDataFormatter weatherDataFormatter,SwipeRefreshLayoutInitializer swipeRefreshLayoutInitializer){
        weatherGeneralInfoLayoutInitializer.updateWeatherGeneralInfoLayoutData(activity,weatherDataFormatter,swipeRefreshLayoutInitializer);
    }

    private void updateWeatherDetailsLayoutData(WeatherDataFormatter weatherDataFormatter){
        weatherDetailsLayoutInitializer.updateWeatherDetailsLayoutData(activity,weatherDataFormatter);
    }

    private void updateWeatherForecastLayoutData(WeatherDataFormatter weatherDataFormatter){
        weatherForecastLayoutInitializer.updateWeatherForecastLayoutData(activity,weatherDataFormatter);
    }

    public void updateWeatherLayoutTheme(boolean isDay){
        WeatherLayoutThemeColorsUpdater themeColorsUpdater=new WeatherLayoutThemeColorsUpdater(activity, isDay);
        updateWeatherGeneralInfoLayoutTheme(themeColorsUpdater);
        updateWeatherDetailsLayoutTheme(activity,themeColorsUpdater,isDay);
        updateWeatherForecastLayoutTheme(activity,themeColorsUpdater);
    }

    private void updateWeatherGeneralInfoLayoutTheme(WeatherLayoutThemeColorsUpdater themeColorsUpdater){
        weatherGeneralInfoLayoutInitializer.updateWeatherGeneralInfoLayoutTheme(activity,themeColorsUpdater);
    }

    private void updateWeatherDetailsLayoutTheme(Activity activity,WeatherLayoutThemeColorsUpdater themeColorsUpdater,boolean isDay){
        weatherDetailsLayoutInitializer.updateWeatherDetailsLayoutTheme(activity,themeColorsUpdater,isDay);
    }

    private void updateWeatherForecastLayoutTheme(Activity activity,WeatherLayoutThemeColorsUpdater themeColorsUpdater){
        weatherForecastLayoutInitializer.updateWeatherForecastLayoutTheme(activity,themeColorsUpdater);
    }

    public WeatherDetailsLayoutInitializer getWeatherDetailsLayoutInitializer() {
        return weatherDetailsLayoutInitializer;
    }

    public SwipeRefreshLayoutInitializer getSwipeRefreshLayoutInitializer() {
        return swipeRefreshLayoutInitializer;
    }
}
