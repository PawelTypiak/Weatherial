package paweltypiak.weatherial.mainActivityInitializing.mainActivityDataDownloading.mainActivityWeatherDataDownloading;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.InternetFailureDialogInitializer;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.NoWeatherResultsForLocationDialogInitializer;
import paweltypiak.weatherial.jsonHandling.Channel;
import paweltypiak.weatherial.mainActivityInitializing.mainActivityDataDownloading.MainActivityDataDownloader;
import paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.MainActivityLayoutInitializer;
import paweltypiak.weatherial.dataDownloading.weatherDataDownloading.WeatherDataDownloader;
import paweltypiak.weatherial.dataDownloading.weatherDataDownloading.WeatherDataParser;
import paweltypiak.weatherial.dataDownloading.weatherDataDownloading.WeatherDownloadCallback;

public class MainActivityWeatherDataDownloader implements WeatherDownloadCallback {

    private Activity activity;
    private MainActivityDataDownloader dataDownloader;
    private int downloadMode;
    private MainActivityLayoutInitializer layoutInitializer;
    private AlertDialog weatherGeolocalizationInternetFailureDialog;
    private AlertDialog noWeatherResultsForLocationDialog;
    private AlertDialog geolocalizationProgressDialog;

    public MainActivityWeatherDataDownloader(Activity activity,
                                             Bundle savedInstanceState,
                                             MainActivityLayoutInitializer layoutInitializer,
                                             MainActivityDataDownloader dataDownloader){
        this.activity=activity;
        this.layoutInitializer=layoutInitializer;
        this.dataDownloader=dataDownloader;
        MainActivityInitialWeatherDataSetter.setInitialWeatherData(activity,savedInstanceState,layoutInitializer);
        initializeDialogs();
    }

    private void initializeDialogs(){
        initializeWeatherGeolocalizationInternetFailureDialog();
        initializeNoWeatherResultsForLocationDialog();
        initializeGeolocalizationProgressDialog();
    }

    private void initializeWeatherGeolocalizationInternetFailureDialog(){
        weatherGeolocalizationInternetFailureDialog
                = InternetFailureDialogInitializer.getInternetFailureDialog(
                activity,
                1,
                downloadWeatherDataAfterGeocodingFailureRunnable,
                null);
    }

    private Runnable downloadWeatherDataAfterGeocodingFailureRunnable = new Runnable() {
        public void run() {
            String geocodingLocation=dataDownloader.getGeolocalizationDownloader().getGeocodingLocation();
            initializeWeatherDataDownloading(0,geocodingLocation);}
    };

    private void initializeNoWeatherResultsForLocationDialog(){
        noWeatherResultsForLocationDialog
                = NoWeatherResultsForLocationDialogInitializer.getNoWeatherResultsForLocationDialog(
                activity,
                2,
                startGeolocalizationRunnable,
                null);
    }

    private Runnable startGeolocalizationRunnable = new Runnable() {
        public void run() {
            dataDownloader.getGeolocalizationDownloader().initializeCurrentLocationDataDownloading();
        }
    };

    private void initializeGeolocalizationProgressDialog(){
        geolocalizationProgressDialog=dataDownloader.getGeolocalizationDownloader().getGeolocalizationProgressDialog();
    }

    public void initializeWeatherDataDownloading(int downloadMode,String location){
        this.downloadMode=downloadMode;
        new WeatherDataDownloader(location,this);
    }

    @Override
    public void weatherServiceSuccess(Channel channel) {
        WeatherDataParser weatherDataParser = new WeatherDataParser(channel);
        if(downloadMode==0){
            //weather service success for geolocalization
            onWeatherServiceSuccessAfterGeolocalization(weatherDataParser);
        }
        else{
            onWeatherServiceSuccessAfterRefresh(weatherDataParser);
        }
    }

    private void onWeatherServiceSuccessAfterGeolocalization(WeatherDataParser weatherDataParser){
        layoutInitializer.updateLayoutOnWeatherDataChange(activity, weatherDataParser,true,true);
        geolocalizationProgressDialog.dismiss();
    }

    private void onWeatherServiceSuccessAfterRefresh(WeatherDataParser weatherDataParser){
        layoutInitializer.getWeatherLayoutInitializer()
                .getSwipeRefreshLayoutInitializer()
                .getOnRefreshInitializer()
                .onWeatherSuccessAfterRefresh(activity, weatherDataParser);
    }

    @Override
    public void weatherServiceFailure(int errorCode) {
        if(downloadMode==0){
            //weather service failure for geolocalization
            geolocalizationProgressDialog.dismiss();
            if(errorCode==0) {
                weatherGeolocalizationInternetFailureDialog.show();
            }
            else if(errorCode==1){
                noWeatherResultsForLocationDialog.show();
            }
        }
        else{
            //weather service failure for refreshing
            onWeatherServiceFailureAfterRefresh(errorCode);
        }
    }

    private void onWeatherServiceFailureAfterRefresh(int errorCode){
        layoutInitializer.getWeatherLayoutInitializer()
                .getSwipeRefreshLayoutInitializer()
                .getOnRefreshInitializer()
                .onWeatherFailureAfterRefresh(errorCode);
    }
}
