package paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.introActivityLoadingFragmentDialogInitializing;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.GeolocalizationFailureDialogInitializer;
import paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.IntroActivityLoadingFragmentDataDownloader;

public class IntroActivityLoadingFragmentGeolocalizationFailureInitializer {

    private Activity activity;
    private IntroActivityLoadingFragmentDataDownloader dataDownloader;
    private AlertDialog geolocalizationFailureDialog;

    public IntroActivityLoadingFragmentGeolocalizationFailureInitializer(Activity activity,
                                                                         IntroActivityLoadingFragmentDataDownloader dataDownloader){
        this.activity=activity;
        this.dataDownloader=dataDownloader;
        initializeGeolocalizationFailureDialog();
    }

    public AlertDialog getGeolocalizationFailureDialog() {
        return geolocalizationFailureDialog;
    }

    private void initializeGeolocalizationFailureDialog(){
        geolocalizationFailureDialog
                = GeolocalizationFailureDialogInitializer.getGeolocalizationFailureDialog(
                activity,
                0,
                getPositiveButtonRunnable(),
                getNegativeButtonRunnable()
                );
    }

    private Runnable getPositiveButtonRunnable(){
        boolean isFirstLaunch=dataDownloader.isFirstLaunch();
        if(isFirstLaunch) {
            return new ShowLocationFragmentRunnableInitializer(dataDownloader)
                    .getShowLocationFragmentRunnable();
        }
        else {
            return new ShowLoadingFragmentChangeLocationAfterFailureDialogRunnableInitializer(activity,dataDownloader)
                    .getShowChangeLocationAfterFailureDialogRunnable();
        }
    }

    private Runnable getNegativeButtonRunnable(){
        return new ShowLoadingFragmentExitDialogRunnable(activity,showGeolocalizationFailureDialogRunnable);
    }

    private Runnable showGeolocalizationFailureDialogRunnable = new Runnable() {
        public void run() {geolocalizationFailureDialog.show();}
    };
}
