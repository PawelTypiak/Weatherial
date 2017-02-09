package paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.introActivityLoadingFragmentDialogInitializing;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.InternetFailureDialogInitializer;
import paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.IntroActivityLoadingFragmentDataDownloader;

public class IntroActivityLoadingFragmentGeocodingInternetFailureInitializer {

    private Activity activity;
    private IntroActivityLoadingFragmentDataDownloader dataDownloader;
    private AlertDialog geocodingInternetFailureDialog;

    public IntroActivityLoadingFragmentGeocodingInternetFailureInitializer(Activity activity,
                                                                           IntroActivityLoadingFragmentDataDownloader dataDownloader){
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
        return new ShowLoadingFragmentExitDialogRunnable(activity,showGeocodingInternetFailureDialogRunnable);
    }

    private Runnable showGeocodingInternetFailureDialogRunnable = new Runnable() {
        public void run() {geocodingInternetFailureDialog.show();}
    };
}
