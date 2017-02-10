package paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.layoutUpdating;

import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.MainActivityLayoutInitializer;
import paweltypiak.weatherial.usefulClasses.FavouritesEditor;
import paweltypiak.weatherial.dataDownloading.weatherDataDownloading.WeatherDataFormatter;
import paweltypiak.weatherial.dataDownloading.weatherDataDownloading.WeatherDataParser;

public class OnWeatherDataChangeLayoutUpdater {

    private WeatherDataFormatter currentDataFormatter;
    private static WeatherDataParser currentWeatherDataParser;

    public OnWeatherDataChangeLayoutUpdater(Activity activity,
                                            MainActivityLayoutInitializer mainActivityLayoutInitializer,
                                            WeatherDataParser weatherDataParser,
                                            boolean doUpdateAppBarData,
                                            boolean isGeolocalizationMode) {
        currentWeatherDataParser =weatherDataParser;
        currentDataFormatter=new WeatherDataFormatter(activity, weatherDataParser);
        final LinearLayout weatherLayout=(LinearLayout)activity.findViewById(R.id.weather_inner_layout);
        if(weatherLayout.getVisibility()== View.VISIBLE) {
            updateLayoutWithWeatherLayoutFadeOut(activity, mainActivityLayoutInitializer,doUpdateAppBarData,isGeolocalizationMode);
        }
        else {
            updateLayout(activity, mainActivityLayoutInitializer,doUpdateAppBarData,isGeolocalizationMode);
        }
    }

    private void updateLayoutWithWeatherLayoutFadeOut(final Activity activity,
                                                      final MainActivityLayoutInitializer mainActivityLayoutInitializer,
                                                      final boolean doUpdateAppBarData,
                                                      final boolean isGeolocalizationMode){
        mainActivityLayoutInitializer.
                getWeatherLayoutInitializer().
                getGeneralWeatherLayoutInitializer().
                fadeOutWeatherLayout(new updateLayoutRunnable(activity,
                        mainActivityLayoutInitializer,
                        doUpdateAppBarData,
                        isGeolocalizationMode));
    }

    private void updateLayout(Activity activity,
                              MainActivityLayoutInitializer mainActivityLayoutInitializer,
                              boolean doUpdateAppBarData,
                              boolean isGeolocalizationMode){
        updateOnTimeChangeLayoutUpdater(mainActivityLayoutInitializer);
        if(doUpdateAppBarData==true) {
            updateAppBarLayoutData(activity,mainActivityLayoutInitializer,currentDataFormatter,isGeolocalizationMode);
        }
        updateWeatherLayoutData(mainActivityLayoutInitializer);
        updateWeatherLayoutTheme(mainActivityLayoutInitializer);
    }

    private class updateLayoutRunnable implements Runnable{

        private Activity activity;
        private MainActivityLayoutInitializer mainActivityLayoutInitializer;
        private boolean doSetAppBar;
        private boolean isGeolocalizationMode;

        public updateLayoutRunnable(Activity activity,
                                    MainActivityLayoutInitializer mainActivityLayoutInitializer,
                                    boolean doSetAppBar,
                                    boolean isGeolocalizationMode){
            this.activity=activity;
            this.mainActivityLayoutInitializer=mainActivityLayoutInitializer;
            this.doSetAppBar=doSetAppBar;
            this.isGeolocalizationMode=isGeolocalizationMode;
        }

        public void run() {
            updateLayout(activity, mainActivityLayoutInitializer,doSetAppBar,isGeolocalizationMode);
        }
    }

    private void updateAppBarLayoutData(Activity activity,
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

    public WeatherDataFormatter getCurrentDataFormatter() {return currentDataFormatter;}
}
