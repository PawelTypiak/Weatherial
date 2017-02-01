package paweltypiak.matweather.firstLaunching.firstLaunchLoadingFragmentInitializing.firstLaunchLoadingFragmentDataDownloading.FirstLaunchLoadingFragmentDialogInitializing;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import paweltypiak.matweather.dialogsInitializing.dialogInitializers.GeolocalizationMethodsDialogInitializer;
import paweltypiak.matweather.firstLaunching.firstLaunchLoadingFragmentInitializing.firstLaunchLoadingFragmentDataDownloading.FirstLaunchLoadingFragmentDataDownloader;

public class FirstLaunchLoadingFragmentGeolocalizationMethodsDialogInitializer {

    private Activity activity;
    private FirstLaunchLoadingFragmentDataDownloader dataDownloader;
    private AlertDialog geolocalizationMethodsDialog;

    public FirstLaunchLoadingFragmentGeolocalizationMethodsDialogInitializer(Activity activity,
                                                                             FirstLaunchLoadingFragmentDataDownloader dataDownloader){
        this.activity=activity;
        this.dataDownloader=dataDownloader;
        initializeGeolocalizationMethodsDialog();
    }

    public AlertDialog getGeolocalizationMethodsDialog(){
        return geolocalizationMethodsDialog;
    }

    private AlertDialog initializeGeolocalizationMethodsDialog(){
        GeolocalizationMethodsDialogInitializer geolocalizationMethodsDialogInitializer
                =new GeolocalizationMethodsDialogInitializer(
                activity,
                0,
                positiveButtonRunnable
                );
        geolocalizationMethodsDialog= geolocalizationMethodsDialogInitializer.getGeolocalizationMethodsDialog();
        return geolocalizationMethodsDialog;
    }

    private Runnable positiveButtonRunnable =new Runnable() {
        @Override
        public void run() {
            boolean isNextLaunchAfterFailure=dataDownloader.isNextLaunchAfterFailure();
            if(isNextLaunchAfterFailure) {
                dataDownloader.initializeNextLaunchAfterFailure();
            }
            else dataDownloader.initializeNextLaunch();
        }
    };
}
