package paweltypiak.matweather.introActivityInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;
import paweltypiak.matweather.R;
import paweltypiak.matweather.introActivityInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.introActivityLoadingFragmentDialogInitializing.IntroActivityLoadingFragmentNoWeatherResultsForLocationDialogInitializer;
import paweltypiak.matweather.introActivityInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.introActivityLoadingFragmentDialogInitializing.IntroActivityLoadingFragmentWeatherInternetFailureDialogInitializer;
import paweltypiak.matweather.introActivityInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.introActivityLoadingFragmentDialogInitializing.IntroActivityLoadingFragmentWeatherResultsForLocationDialogInitializer;
import paweltypiak.matweather.introActivityInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.introActivityLoadingFragmentDialogInitializing.IntroActivityLoadingFragmentWeatherServiceFailureDialogInitializer;
import paweltypiak.matweather.jsonHandling.Channel;
import paweltypiak.matweather.usefulClasses.SharedPreferencesModifier;
import paweltypiak.matweather.dataDownloading.weatherDataDownloading.WeatherDataDownloader;
import paweltypiak.matweather.dataDownloading.weatherDataDownloading.WeatherDataParser;
import paweltypiak.matweather.dataDownloading.weatherDataDownloading.WeatherDownloadCallback;

public class IntroActivityLoadingFragmentWeatherDataDownloader implements WeatherDownloadCallback {

    private Activity activity;
    private AlertDialog weatherInternetFailureDialog;
    private AlertDialog weatherServiceFailureDialog;
    private AlertDialog noWeatherResultsForLocationDialog;
    private IntroActivityLoadingFragmentDataDownloader dataDownloader;

    public IntroActivityLoadingFragmentWeatherDataDownloader(Activity activity,
                                                             IntroActivityLoadingFragmentDataDownloader dataDownloader){
        this.activity=activity;
        this.dataDownloader=dataDownloader;
        initializeDialogs();
    }

    private void initializeDialogs(){
        getWeatherInternetFailureDialog();
        getWeatherServiceFailureDialog();
        getNoWeatherResultsForLocationDialog();
    }

    private void getWeatherInternetFailureDialog(){
        IntroActivityLoadingFragmentWeatherInternetFailureDialogInitializer weatherInternetFailureDialogInitializer
                =new IntroActivityLoadingFragmentWeatherInternetFailureDialogInitializer(activity,dataDownloader);
        weatherInternetFailureDialog=weatherInternetFailureDialogInitializer.getWeatherInternetFailureDialog();
    }

    private void getWeatherServiceFailureDialog(){
        IntroActivityLoadingFragmentWeatherServiceFailureDialogInitializer weatherServiceFailureDialogInitializer
                =new IntroActivityLoadingFragmentWeatherServiceFailureDialogInitializer(activity,dataDownloader);
        weatherServiceFailureDialog=weatherServiceFailureDialogInitializer.getWeatherServiceFailureDialog();
    }

    private void getNoWeatherResultsForLocationDialog(){
        IntroActivityLoadingFragmentNoWeatherResultsForLocationDialogInitializer noWeatherResultsForLocationDialogInitializer
                =new IntroActivityLoadingFragmentNoWeatherResultsForLocationDialogInitializer(activity,dataDownloader);
        noWeatherResultsForLocationDialog=noWeatherResultsForLocationDialogInitializer.getNoWeatherResultsForLocationDialog();
    }

    public void downloadWeatherData(String location){
        Log.d("weather", "start weather downloading");
        TextView messageTextView=dataDownloader.getMessageTextView();
        if(dataDownloader.isFirstLaunch()&& dataDownloader.getSelectedDefeaultLocationOption() ==1) {
            messageTextView.setText(activity.getString(R.string.searching_location_progress_message));
        }
        else messageTextView.setText(activity.getString(R.string.downloading_weather_data_progress_message));
        dataDownloader.setLoadingViewsVisibility(true);
        new WeatherDataDownloader(location,this);
    }

    @Override
    public void weatherServiceSuccess(Channel channel) {
        WeatherDataParser weatherDataParser = new WeatherDataParser(channel);
        if(dataDownloader.isFirstLaunch()&& dataDownloader.getSelectedDefeaultLocationOption() ==1){
            showWeatherResultsForLocationDialog(weatherDataParser);
        }
        else {
            startMainActivity(weatherDataParser);
        }
    }

    private void showWeatherResultsForLocationDialog(WeatherDataParser weatherDataParser){
        IntroActivityLoadingFragmentWeatherResultsForLocationDialogInitializer weatherResultsForLocationDialogInitializer
                =new IntroActivityLoadingFragmentWeatherResultsForLocationDialogInitializer(activity,dataDownloader,weatherDataParser);
        AlertDialog weatherResultsForLocationDialog=weatherResultsForLocationDialogInitializer.getWeatherResultsForLocationDialog();
        showDialog(weatherResultsForLocationDialog);
    }

    private void startMainActivity(WeatherDataParser weatherDataParser){
        new IntroActivityLoadingFragmentMainActivityStartingInitializer(
                activity,
                dataDownloader,
                weatherDataParser
        );
    }

    @Override
    public void weatherServiceFailure(int errorCode) {
        if(errorCode==0) {showDialog(weatherInternetFailureDialog);}
        else if(errorCode==1){
            if(dataDownloader.isFirstLaunch()) {
                showDialog(noWeatherResultsForLocationDialog);
            }
            else {
                if(!SharedPreferencesModifier.isDefeaultLocationConstant(activity)
                        || (dataDownloader.getChangedLocation()==null&&dataDownloader.isNextLaunchAfterFailure()==true)){
                    showDialog(noWeatherResultsForLocationDialog);
                }
                else{
                    showDialog(weatherServiceFailureDialog);
                }
            }
        }
    }

    private void showDialog(AlertDialog alertDialog){
        alertDialog.show();
        dataDownloader.setLoadingViewsVisibility(false);
    }
}
