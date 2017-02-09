package paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.introActivityLoadingFragmentDialogInitializing;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.GeolocalizationMethodsDialogInitializer;
import paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.IntroActivityLoadingFragmentDataDownloader;

public class IntroActivityLoadingFragmentGeolocalizationMethodsDialogInitializer {

    private Activity activity;
    private IntroActivityLoadingFragmentDataDownloader dataDownloader;
    private AlertDialog geolocalizationMethodsDialog;

    public IntroActivityLoadingFragmentGeolocalizationMethodsDialogInitializer(Activity activity,
                                                                               IntroActivityLoadingFragmentDataDownloader dataDownloader){
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
