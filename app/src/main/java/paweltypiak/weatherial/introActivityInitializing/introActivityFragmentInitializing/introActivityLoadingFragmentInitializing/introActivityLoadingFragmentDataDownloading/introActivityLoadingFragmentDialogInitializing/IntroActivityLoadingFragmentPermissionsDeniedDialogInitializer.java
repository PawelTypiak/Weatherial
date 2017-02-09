package paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.introActivityLoadingFragmentDialogInitializing;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.GeolocalizationPermissionsDeniedDialogInitializer;
import paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.IntroActivityLoadingFragmentDataDownloader;

public class IntroActivityLoadingFragmentPermissionsDeniedDialogInitializer {

    private Activity activity;
    private IntroActivityLoadingFragmentDataDownloader dataDownloader;
    private AlertDialog permissionsDeniedDialog;

    public IntroActivityLoadingFragmentPermissionsDeniedDialogInitializer(Activity activity,
                                                                          IntroActivityLoadingFragmentDataDownloader dataDownloader){
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
            ShowLoadingFragmentChangeLocationAfterFailureDialogRunnableInitializer runnableInitializer
                    =new ShowLoadingFragmentChangeLocationAfterFailureDialogRunnableInitializer(activity,dataDownloader);
            return runnableInitializer.getShowChangeLocationAfterFailureDialogRunnable();
        }
    }

    private Runnable getNegativeButtonRunnable(){
        return new ShowLoadingFragmentExitDialogRunnable(activity,showPermissionDeniedDialogRunnable);
    }

    private Runnable showPermissionDeniedDialogRunnable = new Runnable() {
        public void run() {permissionsDeniedDialog.show();}
    };
}
