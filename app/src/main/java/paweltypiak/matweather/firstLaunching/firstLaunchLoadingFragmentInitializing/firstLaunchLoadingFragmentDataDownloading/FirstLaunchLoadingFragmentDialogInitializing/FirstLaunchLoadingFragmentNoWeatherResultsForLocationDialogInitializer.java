package paweltypiak.matweather.firstLaunching.firstLaunchLoadingFragmentInitializing.firstLaunchLoadingFragmentDataDownloading.FirstLaunchLoadingFragmentDialogInitializing;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import paweltypiak.matweather.dialogsInitializing.dialogInitializers.NoWeatherResultsForLocationDialogInitializer;
import paweltypiak.matweather.firstLaunching.firstLaunchLoadingFragmentInitializing.firstLaunchLoadingFragmentDataDownloading.FirstLaunchLoadingFragmentDataDownloader;

public class FirstLaunchLoadingFragmentNoWeatherResultsForLocationDialogInitializer {

    private Activity activity;
    private FirstLaunchLoadingFragmentDataDownloader dataDownloader;
    private AlertDialog noWeatherResultsForLocationDialog;

    public FirstLaunchLoadingFragmentNoWeatherResultsForLocationDialogInitializer(Activity activity,
                                                                                  FirstLaunchLoadingFragmentDataDownloader dataDownloader){
        this.activity=activity;
        this.dataDownloader=dataDownloader;
        initializeNoWeatherResultsForLocationDialog();
    }

    public AlertDialog getNoWeatherResultsForLocationDialog() {
        return noWeatherResultsForLocationDialog;
    }

    private void initializeNoWeatherResultsForLocationDialog(){
        noWeatherResultsForLocationDialog
                = NoWeatherResultsForLocationDialogInitializer.getNoWeatherResultsForLocationDialog(
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
       return new ShowExitDialogRunnable(activity,showNoWeatherResultsForLocationRunnable);
    }

    private Runnable showNoWeatherResultsForLocationRunnable = new Runnable() {
        public void run() {noWeatherResultsForLocationDialog.show();}
    };
}
