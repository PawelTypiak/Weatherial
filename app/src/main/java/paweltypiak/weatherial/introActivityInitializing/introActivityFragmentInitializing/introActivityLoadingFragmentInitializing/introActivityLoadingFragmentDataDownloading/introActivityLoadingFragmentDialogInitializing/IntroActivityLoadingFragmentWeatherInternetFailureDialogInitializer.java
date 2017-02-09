package paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.introActivityLoadingFragmentDialogInitializing;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.InternetFailureDialogInitializer;
import paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.IntroActivityLoadingFragmentDataDownloader;

public class IntroActivityLoadingFragmentWeatherInternetFailureDialogInitializer {

    private Activity activity;
    private IntroActivityLoadingFragmentDataDownloader dataDownloader;
    private AlertDialog weatherInternetFailureDialog;

   public IntroActivityLoadingFragmentWeatherInternetFailureDialogInitializer(Activity activity,
                                                                              IntroActivityLoadingFragmentDataDownloader dataDownloader){
       this.activity=activity;
       this.dataDownloader=dataDownloader;
       initializeWeatherInternetFailureDialog();
   }

    public AlertDialog getWeatherInternetFailureDialog() {
        return weatherInternetFailureDialog;
    }

    private void initializeWeatherInternetFailureDialog(){
        weatherInternetFailureDialog
                = InternetFailureDialogInitializer.getInternetFailureDialog(
                activity,
                0,
                positiveButtonRunnable,
                getNegativeButtonRunnable());
    }

    private Runnable positiveButtonRunnable =new Runnable() {
        @Override
        public void run() {
            String location=dataDownloader.getLocation();
            dataDownloader.getWeatherDataDownloader().downloadWeatherData(location);
        }
    };

    private Runnable getNegativeButtonRunnable(){
        return new ShowLoadingFragmentExitDialogRunnable(activity,showWeatherInternetFailureDialogRunnable);
    }

    private Runnable showWeatherInternetFailureDialogRunnable = new Runnable() {
        public void run() {weatherInternetFailureDialog.show();}
    };
}
