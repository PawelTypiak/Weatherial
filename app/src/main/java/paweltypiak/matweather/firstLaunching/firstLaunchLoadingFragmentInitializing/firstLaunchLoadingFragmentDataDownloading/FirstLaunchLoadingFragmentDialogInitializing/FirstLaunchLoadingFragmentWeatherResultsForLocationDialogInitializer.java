package paweltypiak.matweather.firstLaunching.firstLaunchLoadingFragmentInitializing.firstLaunchLoadingFragmentDataDownloading.FirstLaunchLoadingFragmentDialogInitializing;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import paweltypiak.matweather.dialogsInitializing.dialogInitializers.WeatherResultsForLocationDialogInitializer;
import paweltypiak.matweather.firstLaunching.firstLaunchLoadingFragmentInitializing.firstLaunchLoadingFragmentDataDownloading.FirstLaunchLoadingFragmentDataDownloader;
import paweltypiak.matweather.firstLaunching.firstLaunchLoadingFragmentInitializing.firstLaunchLoadingFragmentDataDownloading.FirstLaunchLoadingFragmentMainActivityStartingInitializer;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataParser;

public class FirstLaunchLoadingFragmentWeatherResultsForLocationDialogInitializer {

    private Activity activity;
    private FirstLaunchLoadingFragmentDataDownloader dataDownloader;
    private WeatherDataParser weatherDataParser;
    private AlertDialog weatherResultsForLocationDialog;

    public FirstLaunchLoadingFragmentWeatherResultsForLocationDialogInitializer(Activity activity,
                                                                                FirstLaunchLoadingFragmentDataDownloader dataDownloader,
                                                                                WeatherDataParser weatherDataParser){
        this.activity=activity;
        this.dataDownloader=dataDownloader;
        this.weatherDataParser=weatherDataParser;
        initializeWeatherResultsForLocationDialog();
    }

    public AlertDialog getWeatherResultsForLocationDialog() {
        return weatherResultsForLocationDialog;
    }

    private AlertDialog initializeWeatherResultsForLocationDialog(){
        weatherResultsForLocationDialog
                = WeatherResultsForLocationDialogInitializer.getWeatherResultsForLocationDialog(
                activity,
                0,
                weatherDataParser,
                positiveButtonRunnable,
                getNeutralButtonRunnable(),
                getNegativeButtonRunnable()
        );
        return weatherResultsForLocationDialog;
    }

    private Runnable positiveButtonRunnable = new Runnable() {
        public void run() {
            new FirstLaunchLoadingFragmentMainActivityStartingInitializer(
                    activity,
                    dataDownloader,
                    weatherDataParser
            );
            }
    };

    private Runnable getNeutralButtonRunnable(){
        return new ShowLocationFragmentRunnableInitializer(dataDownloader).
                getShowLocationFragmentRunnable();
    }

    private Runnable getNegativeButtonRunnable(){
        return new ShowExitDialogRunnable(activity,showWeatherResultsForLocationDialogRunnable);
    }

    private Runnable showWeatherResultsForLocationDialogRunnable = new Runnable() {
        public void run() {weatherResultsForLocationDialog.show();}
    };
}
