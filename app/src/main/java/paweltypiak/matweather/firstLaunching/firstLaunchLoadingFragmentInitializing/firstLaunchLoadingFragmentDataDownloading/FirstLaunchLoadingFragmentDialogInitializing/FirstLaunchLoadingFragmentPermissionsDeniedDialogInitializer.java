package paweltypiak.matweather.firstLaunching.firstLaunchLoadingFragmentInitializing.firstLaunchLoadingFragmentDataDownloading.FirstLaunchLoadingFragmentDialogInitializing;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import paweltypiak.matweather.dialogsInitializing.dialogInitializers.GeolocalizationPermissionsDeniedDialogInitializer;
import paweltypiak.matweather.firstLaunching.firstLaunchLoadingFragmentInitializing.firstLaunchLoadingFragmentDataDownloading.FirstLaunchLoadingFragmentDataDownloader;

public class FirstLaunchLoadingFragmentPermissionsDeniedDialogInitializer {

    private Activity activity;
    private FirstLaunchLoadingFragmentDataDownloader dataDownloader;
    private AlertDialog permissionsDeniedDialog;

    public FirstLaunchLoadingFragmentPermissionsDeniedDialogInitializer(Activity activity,
                                                                        FirstLaunchLoadingFragmentDataDownloader dataDownloader){
        this.activity=activity;
        this.dataDownloader=dataDownloader;
        initializePermissionsDeniedDialog();
    }

    public AlertDialog getPermissionDeniedDialog() {
        return permissionsDeniedDialog;
    }

    private void initializePermissionsDeniedDialog(){
        permissionsDeniedDialog
                = GeolocalizationPermissionsDeniedDialogInitializer.getGeolocalizationPermissionsDeniedDialog(
                activity,
                0,
                getPositiveButtonRunnable(),
                getNegativeButtonRunnable()
        );
    }

    private Runnable getPositiveButtonRunnable(){
        boolean isFirstLaunch=dataDownloader.isFirstLaunch();
        if(isFirstLaunch) {
            ShowLocationFragmentRunnableInitializer runnableInitializer
                    =new ShowLocationFragmentRunnableInitializer(dataDownloader);
            return runnableInitializer.getShowLocationFragmentRunnable();
        }
        else {
            ShowChangeLocationAfterFailureDialogRunnableInitializer runnableInitializer
                    =new ShowChangeLocationAfterFailureDialogRunnableInitializer(activity,dataDownloader);
            return runnableInitializer.getShowChangeLocationAfterFailureDialogRunnable();
        }
    }

    private Runnable getNegativeButtonRunnable(){
        return new ShowExitDialogRunnable(activity,showPermissionDeniedDialogRunnable);
    }

    private Runnable showPermissionDeniedDialogRunnable = new Runnable() {
        public void run() {permissionsDeniedDialog.show();}
    };
}
