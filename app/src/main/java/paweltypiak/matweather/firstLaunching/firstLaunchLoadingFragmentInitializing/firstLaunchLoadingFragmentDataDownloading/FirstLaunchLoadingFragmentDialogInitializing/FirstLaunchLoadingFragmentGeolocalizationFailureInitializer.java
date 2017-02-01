package paweltypiak.matweather.firstLaunching.firstLaunchLoadingFragmentInitializing.firstLaunchLoadingFragmentDataDownloading.FirstLaunchLoadingFragmentDialogInitializing;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import paweltypiak.matweather.dialogsInitializing.dialogInitializers.GeolocalizationFailureDialogInitializer;
import paweltypiak.matweather.firstLaunching.firstLaunchLoadingFragmentInitializing.firstLaunchLoadingFragmentDataDownloading.FirstLaunchLoadingFragmentDataDownloader;

public class FirstLaunchLoadingFragmentGeolocalizationFailureInitializer {

    private Activity activity;
    private FirstLaunchLoadingFragmentDataDownloader dataDownloader;
    private AlertDialog geolocalizationFailureDialog;

    public FirstLaunchLoadingFragmentGeolocalizationFailureInitializer(Activity activity,
                                                                       FirstLaunchLoadingFragmentDataDownloader dataDownloader){
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
            return new ShowChangeLocationAfterFailureDialogRunnableInitializer(activity,dataDownloader)
                    .getShowChangeLocationAfterFailureDialogRunnable();
        }
    }

    private Runnable getNegativeButtonRunnable(){
        return new ShowExitDialogRunnable(activity,showGeolocalizationFailureDialogRunnable);
    }

    private Runnable showGeolocalizationFailureDialogRunnable = new Runnable() {
        public void run() {geolocalizationFailureDialog.show();}
    };
}
