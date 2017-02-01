package paweltypiak.matweather.firstLaunching.firstLaunchLoadingFragmentInitializing.firstLaunchLoadingFragmentDataDownloading.FirstLaunchLoadingFragmentDialogInitializing;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import paweltypiak.matweather.dialogsInitializing.dialogInitializers.ServiceFailureDialogInitializer;
import paweltypiak.matweather.firstLaunching.firstLaunchLoadingFragmentInitializing.firstLaunchLoadingFragmentDataDownloading.FirstLaunchLoadingFragmentDataDownloader;

public class FirstLaunchLoadingFragmentWeatherServiceFailureDialogInitializer {

    private Activity activity;
    private FirstLaunchLoadingFragmentDataDownloader dataDownloader;
    private AlertDialog weatherServiceFailureDialog;

    public FirstLaunchLoadingFragmentWeatherServiceFailureDialogInitializer(Activity activity,
                                                                            FirstLaunchLoadingFragmentDataDownloader dataDownloader){
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
        return new ShowExitDialogRunnable(activity,showWeatherServiceFailureDialogRunnable);
    }

    private Runnable showWeatherServiceFailureDialogRunnable = new Runnable() {
        public void run() {weatherServiceFailureDialog.show();}
    };
}
