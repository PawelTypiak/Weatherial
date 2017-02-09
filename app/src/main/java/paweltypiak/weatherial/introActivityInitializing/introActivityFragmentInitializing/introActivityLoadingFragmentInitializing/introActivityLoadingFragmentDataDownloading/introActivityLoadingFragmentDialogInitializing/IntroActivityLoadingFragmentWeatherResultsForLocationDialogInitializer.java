package paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.introActivityLoadingFragmentDialogInitializing;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.WeatherResultsForLocationDialogInitializer;
import paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.IntroActivityLoadingFragmentDataDownloader;
import paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.IntroActivityLoadingFragmentMainActivityStartingInitializer;
import paweltypiak.weatherial.dataDownloading.weatherDataDownloading.WeatherDataParser;

public class IntroActivityLoadingFragmentWeatherResultsForLocationDialogInitializer {

    private Activity activity;
    private IntroActivityLoadingFragmentDataDownloader dataDownloader;
    private WeatherDataParser weatherDataParser;
    private AlertDialog weatherResultsForLocationDialog;

    public IntroActivityLoadingFragmentWeatherResultsForLocationDialogInitializer(Activity activity,
                                                                                  IntroActivityLoadingFragmentDataDownloader dataDownloader,
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
            new IntroActivityLoadingFragmentMainActivityStartingInitializer(
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
        return new ShowLoadingFragmentExitDialogRunnable(activity,showWeatherResultsForLocationDialogRunnable);
    }

    private Runnable showWeatherResultsForLocationDialogRunnable = new Runnable() {
        public void run() {weatherResultsForLocationDialog.show();}
    };
}
