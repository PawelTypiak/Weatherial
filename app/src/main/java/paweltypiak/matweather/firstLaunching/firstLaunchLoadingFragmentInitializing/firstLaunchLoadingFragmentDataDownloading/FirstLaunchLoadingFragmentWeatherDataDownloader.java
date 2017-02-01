package paweltypiak.matweather.firstLaunching.firstLaunchLoadingFragmentInitializing.firstLaunchLoadingFragmentDataDownloading;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;
import paweltypiak.matweather.R;
import paweltypiak.matweather.firstLaunching.firstLaunchLoadingFragmentInitializing.firstLaunchLoadingFragmentDataDownloading.FirstLaunchLoadingFragmentDialogInitializing.FirstLaunchLoadingFragmentNoWeatherResultsForLocationDialogInitializer;
import paweltypiak.matweather.firstLaunching.firstLaunchLoadingFragmentInitializing.firstLaunchLoadingFragmentDataDownloading.FirstLaunchLoadingFragmentDialogInitializing.FirstLaunchLoadingFragmentWeatherInternetFailureDialogInitializer;
import paweltypiak.matweather.firstLaunching.firstLaunchLoadingFragmentInitializing.firstLaunchLoadingFragmentDataDownloading.FirstLaunchLoadingFragmentDialogInitializing.FirstLaunchLoadingFragmentWeatherResultsForLocationDialogInitializer;
import paweltypiak.matweather.firstLaunching.firstLaunchLoadingFragmentInitializing.firstLaunchLoadingFragmentDataDownloading.FirstLaunchLoadingFragmentDialogInitializing.FirstLaunchLoadingFragmentWeatherServiceFailureDialogInitializer;
import paweltypiak.matweather.jsonHandling.Channel;
import paweltypiak.matweather.usefulClasses.SharedPreferencesModifier;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataDownloader;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataParser;
import paweltypiak.matweather.weatherDataDownloading.WeatherDownloadCallback;

public class FirstLaunchLoadingFragmentWeatherDataDownloader implements WeatherDownloadCallback {

    private Activity activity;
    private AlertDialog weatherInternetFailureDialog;
    private AlertDialog weatherServiceFailureDialog;
    private AlertDialog noWeatherResultsForLocationDialog;
    private FirstLaunchLoadingFragmentDataDownloader dataDownloader;

    public FirstLaunchLoadingFragmentWeatherDataDownloader(Activity activity,
                                                           FirstLaunchLoadingFragmentDataDownloader dataDownloader){
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
        FirstLaunchLoadingFragmentWeatherInternetFailureDialogInitializer weatherInternetFailureDialogInitializer
                =new FirstLaunchLoadingFragmentWeatherInternetFailureDialogInitializer(activity,dataDownloader);
        weatherInternetFailureDialog=weatherInternetFailureDialogInitializer.getWeatherInternetFailureDialog();
    }

    private void getWeatherServiceFailureDialog(){
        FirstLaunchLoadingFragmentWeatherServiceFailureDialogInitializer weatherServiceFailureDialogInitializer
                =new FirstLaunchLoadingFragmentWeatherServiceFailureDialogInitializer(activity,dataDownloader);
        weatherServiceFailureDialog=weatherServiceFailureDialogInitializer.getWeatherServiceFailureDialog();
    }

    private void getNoWeatherResultsForLocationDialog(){
        FirstLaunchLoadingFragmentNoWeatherResultsForLocationDialogInitializer noWeatherResultsForLocationDialogInitializer
                =new FirstLaunchLoadingFragmentNoWeatherResultsForLocationDialogInitializer(activity,dataDownloader);
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
        FirstLaunchLoadingFragmentWeatherResultsForLocationDialogInitializer weatherResultsForLocationDialogInitializer
                =new FirstLaunchLoadingFragmentWeatherResultsForLocationDialogInitializer(activity,dataDownloader,weatherDataParser);
        AlertDialog weatherResultsForLocationDialog=weatherResultsForLocationDialogInitializer.getWeatherResultsForLocationDialog();
        showDialog(weatherResultsForLocationDialog);
    }

    private void startMainActivity(WeatherDataParser weatherDataParser){
        new FirstLaunchLoadingFragmentMainActivityStartingInitializer(
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
