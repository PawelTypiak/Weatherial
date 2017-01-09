package paweltypiak.matweather.layoutInitializing.LayoutUpdating;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.view.View;
import android.widget.LinearLayout;
import paweltypiak.matweather.R;
import paweltypiak.matweather.layoutInitializing.LayoutInitializer;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataFormatter;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataParser;

public class OnWeatherDataChangeLayoutUpdater {

    private static WeatherDataFormatter currentDataFormatter;
    private static WeatherDataParser currentWeatherDataParser;

    public OnWeatherDataChangeLayoutUpdater(Activity activity,
                                            LayoutInitializer layoutInitializer,
                                            WeatherDataParser weatherDataParser,
                                            boolean doSetAppBar,
                                            boolean isGeolocalizationMode) {
        currentWeatherDataParser =weatherDataParser;
        currentDataFormatter=new WeatherDataFormatter(activity, weatherDataParser);
        final LinearLayout weatherLayout=(LinearLayout)activity.findViewById(R.id.weather_inner_layout);
        if(weatherLayout.getVisibility()== View.VISIBLE) {
            updateLayoutWithWeatherLayoutFadeOut(activity,layoutInitializer,doSetAppBar,isGeolocalizationMode,weatherLayout);
        }
        else {
            updateLayout(activity,layoutInitializer,doSetAppBar,isGeolocalizationMode);
        }
    }

    private void updateLayoutWithWeatherLayoutFadeOut(final Activity activity,
                                                      final LayoutInitializer layoutInitializer,
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
                        layoutInitializer
                                .getWeatherLayoutInitializer()
                                .getSwipeRefreshLayoutInitializer()
                                .getPullListenersInitializer()
                                .setNestedScrollViewScrollingDisabled();
                            weatherLayout.setVisibility(View.INVISIBLE);
                            updateLayout(activity,layoutInitializer,doSetAppBar,isGeolocalizationMode);
                        }
                    });
    }

    private void updateLayout(Activity activity,
                              LayoutInitializer layoutInitializer,
                              boolean doSetAppBar,
                              boolean isGeolocalizationMode){
        updateOnTimeChangeLayoutUpdater(layoutInitializer);
        if(doSetAppBar==true) updateAppBarLayoutData(activity,layoutInitializer,isGeolocalizationMode);
        updateWeatherLayoutData(layoutInitializer);
        updateWeatherLayoutTheme(layoutInitializer);
    }

    private void updateAppBarLayoutData(Activity activity,
                                        LayoutInitializer layoutInitializer,
                                        boolean isGeolocalizationMode){
        layoutInitializer.getAppBarLayoutInitializer().
                getAppBarLayoutDataInitializer().
                updateAppBarLayoutData(activity,currentDataFormatter,isGeolocalizationMode);
    }

    private void updateWeatherLayoutData(LayoutInitializer layoutInitializer){
        layoutInitializer.getWeatherLayoutInitializer().updateWeatherLayoutData(
                currentDataFormatter);
    }

    private void updateWeatherLayoutTheme(LayoutInitializer layoutInitializer){
        layoutInitializer.getWeatherLayoutInitializer().updateWeatherLayoutTheme(currentDataFormatter.getDay());
    }

    private void updateOnTimeChangeLayoutUpdater( LayoutInitializer layoutInitializer){
        layoutInitializer.getOnTimeChangeLayoutUpdater().onWeatherDataChangeUpdate(currentDataFormatter);

    }

    public static WeatherDataParser getCurrentWeatherDataParser() {
        return currentWeatherDataParser;
    }

    // TODO: change from static to activity.get
    public static WeatherDataFormatter getCurrentDataFormatter() {return currentDataFormatter;}
}
