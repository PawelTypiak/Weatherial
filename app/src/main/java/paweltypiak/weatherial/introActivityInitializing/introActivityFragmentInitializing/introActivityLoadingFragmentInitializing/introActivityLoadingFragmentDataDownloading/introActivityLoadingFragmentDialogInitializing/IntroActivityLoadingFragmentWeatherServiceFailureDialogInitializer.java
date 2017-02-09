package paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.introActivityLoadingFragmentDialogInitializing;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.ServiceFailureDialogInitializer;
import paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.IntroActivityLoadingFragmentDataDownloader;

public class IntroActivityLoadingFragmentWeatherServiceFailureDialogInitializer {

    private Activity activity;
    private IntroActivityLoadingFragmentDataDownloader dataDownloader;
    private AlertDialog weatherServiceFailureDialog;

    public IntroActivityLoadingFragmentWeatherServiceFailureDialogInitializer(Activity activity,
                                                                              IntroActivityLoadingFragmentDataDownloader dataDownloader){
        this.activity=activity;
        this.dataDownloader=dataDownloader;
        initializeWeatherServiceFailureDialog();
    }

    public AlertDialog getWeatherServiceFailureDialog() {
        return weatherServiceFailureDialog;
    }

    private void initializeWeatherServiceFailureDialog(){
        weatherServiceFailureDialog = ServiceFailureDialogInitializer.getServiceFailureDialog(
                activity,
                0,
                positiveButtonRunnable,
                getNegativeButtonRunnable()
        );
    }

    private Runnable positiveButtonRunnable =new Runnable() {
        @Override
        public void run() {
            String location=dataDownloader.getLocation();
            dataDownloader.getWeatherDataDownloader().downloadWeatherData(location);
        }
    };

    private Runnable getNegativeButtonRunnable(){
        return new ShowLoadingFragmentExitDialogRunnable(activity,showWeatherServiceFailureDialogRunnable);
    }

    private Runnable showWeatherServiceFailureDialogRunnable = new Runnable() {
        public void run() {weatherServiceFailureDialog.show();}
    };
}
