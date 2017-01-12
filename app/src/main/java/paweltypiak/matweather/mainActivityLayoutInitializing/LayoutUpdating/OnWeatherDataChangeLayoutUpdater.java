package paweltypiak.matweather.mainActivityLayoutInitializing.LayoutUpdating;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;

import paweltypiak.matweather.R;
import paweltypiak.matweather.mainActivityLayoutInitializing.MainActivityLayoutInitializer;
import paweltypiak.matweather.usefulClasses.FavouritesEditor;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataFormatter;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataParser;

public class OnWeatherDataChangeLayoutUpdater {

    private static WeatherDataFormatter currentDataFormatter;
    private static WeatherDataParser currentWeatherDataParser;

    public OnWeatherDataChangeLayoutUpdater(Activity activity,
                                            MainActivityLayoutInitializer mainActivityLayoutInitializer,
                                            WeatherDataParser weatherDataParser,
                                            boolean doSetAppBar,
                                            boolean isGeolocalizationMode) {
        currentWeatherDataParser =weatherDataParser;
        currentDataFormatter=new WeatherDataFormatter(activity, weatherDataParser);
        final LinearLayout weatherLayout=(LinearLayout)activity.findViewById(R.id.weather_inner_layout);
        if(weatherLayout.getVisibility()== View.VISIBLE) {
            updateLayoutWithWeatherLayoutFadeOut(activity, mainActivityLayoutInitializer,doSetAppBar,isGeolocalizationMode,weatherLayout);
        }
        else {
            updateLayout(activity, mainActivityLayoutInitializer,doSetAppBar,isGeolocalizationMode);
        }
    }

    private void updateLayoutWithWeatherLayoutFadeOut(final Activity activity,
                                                      final MainActivityLayoutInitializer mainActivityLayoutInitializer,
                                                      final boolean doSetAppBar,
                                                      final boolean isGeolocalizationMode,
                                                      final LinearLayout weatherLayout){
        long transitionTime=100;
        weatherLayout.animate()
                .alpha(0f)
                .setDuration(transitionTime)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mainActivityLayoutInitializer
                                .getWeatherLayoutInitializer()
                                .getSwipeRefreshLayoutInitializer()
                                .getPullListenersInitializer()
                                .setNestedScrollViewScrollingDisabled();
                            weatherLayout.setVisibility(View.INVISIBLE);
                            updateLayout(activity, mainActivityLayoutInitializer,doSetAppBar,isGeolocalizationMode);
                        }
                    });
    }

    private void updateLayout(Activity activity,
                              MainActivityLayoutInitializer mainActivityLayoutInitializer,
                              boolean doSetAppBar,
                              boolean isGeolocalizationMode){
        updateOnTimeChangeLayoutUpdater(mainActivityLayoutInitializer);
        if(doSetAppBar==true) {
            updateAppBarLayoutData(activity,mainActivityLayoutInitializer,currentDataFormatter,isGeolocalizationMode);
        }
        updateWeatherLayoutData(mainActivityLayoutInitializer);
        updateWeatherLayoutTheme(mainActivityLayoutInitializer);
    }

    public void updateAppBarLayoutData(Activity activity,
                                       MainActivityLayoutInitializer mainActivityLayoutInitializer,
                                       WeatherDataFormatter weatherDataFormatter,
                                       boolean isGeolocalizationMode){
        if(FavouritesEditor.isAddressEqual(activity)){
            String [] locationName=FavouritesEditor.getFavouriteLocationNameForAppbar(activity);
            mainActivityLayoutInitializer.getAppBarLayoutInitializer().
                    getAppBarLayoutDataInitializer().
                    updateLocationName(locationName[0],locationName[1]);
            mainActivityLayoutInitializer
                    .getAppBarLayoutInitializer()
                    .getAppBarLayoutButtonsInitializer()
                    .getFloatingActionButtonInitializer()
                    .setFloatingActionButtonOnClickIndicator(1);
            mainActivityLayoutInitializer
                    .getAppBarLayoutInitializer()
                    .getAppBarLayoutButtonsInitializer()
                    .getNavigationDrawerInitializer()
                    .checkNavigationDrawerMenuItem(1);
        }
        else{
            String city=weatherDataFormatter.getCity();
            String region=weatherDataFormatter.getRegion();
            String country=weatherDataFormatter.getCountry();
            String appBarTitle=city;
            String appBarSubtitle=region+", "+country;
            mainActivityLayoutInitializer.getAppBarLayoutInitializer().
                    getAppBarLayoutDataInitializer().
                    updateLocationName(appBarTitle,appBarSubtitle);
            mainActivityLayoutInitializer.getAppBarLayoutInitializer().
                    getAppBarLayoutButtonsInitializer().
                    getFloatingActionButtonInitializer().
                    setFloatingActionButtonOnClickIndicator(0);
            if(isGeolocalizationMode==true){
                mainActivityLayoutInitializer
                        .getAppBarLayoutInitializer()
                        .getAppBarLayoutButtonsInitializer()
                        .getNavigationDrawerInitializer()
                        .checkNavigationDrawerMenuItem(0);
            }
            else {
                mainActivityLayoutInitializer
                        .getAppBarLayoutInitializer()
                        .getAppBarLayoutButtonsInitializer()
                        .getNavigationDrawerInitializer()
                        .uncheckAllNavigationDrawerMenuItems();
            }
        }
        String timezone=weatherDataFormatter.getTimezone();
        mainActivityLayoutInitializer
                .getAppBarLayoutInitializer()
                .getAppBarLayoutDataInitializer().updateTimezone(timezone);
    }

    private void updateWeatherLayoutData(MainActivityLayoutInitializer mainActivityLayoutInitializer){
        mainActivityLayoutInitializer.getWeatherLayoutInitializer().updateWeatherLayoutData(
                currentDataFormatter);
    }

    private void updateWeatherLayoutTheme(MainActivityLayoutInitializer mainActivityLayoutInitializer){
        mainActivityLayoutInitializer.getWeatherLayoutInitializer().updateWeatherLayoutTheme(currentDataFormatter.getDay());
    }

    private void updateOnTimeChangeLayoutUpdater( MainActivityLayoutInitializer mainActivityLayoutInitializer){
        mainActivityLayoutInitializer.getOnTimeChangeLayoutUpdater().onWeatherDataChangeUpdate(currentDataFormatter);

    }

    public static WeatherDataParser getCurrentWeatherDataParser() {
        return currentWeatherDataParser;
    }

    // TODO: change from static to activity.get
    public static WeatherDataFormatter getCurrentDataFormatter() {return currentDataFormatter;}
}
