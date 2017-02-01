package paweltypiak.matweather.firstLaunching.firstLaunchLoadingFragmentInitializing.firstLaunchLoadingFragmentDataDownloading.FirstLaunchLoadingFragmentDialogInitializing;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import paweltypiak.matweather.dialogsInitializing.dialogInitializers.InternetFailureDialogInitializer;
import paweltypiak.matweather.firstLaunching.firstLaunchLoadingFragmentInitializing.firstLaunchLoadingFragmentDataDownloading.FirstLaunchLoadingFragmentDataDownloader;

public class FirstLaunchLoadingFragmentWeatherInternetFailureDialogInitializer {

    private Activity activity;
    private FirstLaunchLoadingFragmentDataDownloader dataDownloader;
    private AlertDialog weatherInternetFailureDialog;

   public FirstLaunchLoadingFragmentWeatherInternetFailureDialogInitializer(Activity activity,
                                                                            FirstLaunchLoadingFragmentDataDownloader dataDownloader){
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
        return new ShowExitDialogRunnable(activity,showWeatherInternetFailureDialogRunnable);
    }

    private Runnable showWeatherInternetFailureDialogRunnable = new Runnable() {
        public void run() {weatherInternetFailureDialog.show();}
    };
}
