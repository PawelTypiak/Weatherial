package paweltypiak.matweather.firstLaunching.firstLaunchLoadingFragmentInitializing.firstLaunchLoadingFragmentDataDownloading.FirstLaunchLoadingFragmentDialogInitializing;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import paweltypiak.matweather.dialogsInitializing.dialogInitializers.InternetFailureDialogInitializer;
import paweltypiak.matweather.firstLaunching.firstLaunchLoadingFragmentInitializing.firstLaunchLoadingFragmentDataDownloading.FirstLaunchLoadingFragmentDataDownloader;

public class FirstLaunchLoadingFragmentGeocodingInternetFailureInitializer {

    private Activity activity;
    private FirstLaunchLoadingFragmentDataDownloader dataDownloader;
    private AlertDialog geocodingInternetFailureDialog;

    public FirstLaunchLoadingFragmentGeocodingInternetFailureInitializer(Activity activity,
                                                                         FirstLaunchLoadingFragmentDataDownloader dataDownloader){
        this.activity=activity;
        this.dataDownloader=dataDownloader;
        initializeGeocodingInternetFailureDialog();
    }

    public AlertDialog getGeocodingInternetFailureDialog() {
        return geocodingInternetFailureDialog;
    }

    private void initializeGeocodingInternetFailureDialog(){
        geocodingInternetFailureDialog= InternetFailureDialogInitializer.getInternetFailureDialog(
                activity,
                0,
                positiveButtonRunnable,
                getNegativeButtonRunnable()
        );
    }

    private Runnable positiveButtonRunnable = new Runnable() {
        public void run() {
            dataDownloader.getGeolocalizationDownloader().initializeGeocodingDownloading();
        }
    };

    private Runnable getNegativeButtonRunnable(){
        return new ShowExitDialogRunnable(activity,showGeocodingInternetFailureDialogRunnable);
    }

    private Runnable showGeocodingInternetFailureDialogRunnable = new Runnable() {
        public void run() {geocodingInternetFailureDialog.show();}
    };
}
