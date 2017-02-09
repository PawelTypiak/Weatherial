package paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.introActivityLoadingFragmentDialogInitializing;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.NoWeatherResultsForLocationDialogInitializer;
import paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.IntroActivityLoadingFragmentDataDownloader;

public class IntroActivityLoadingFragmentNoWeatherResultsForLocationDialogInitializer {

    private Activity activity;
    private IntroActivityLoadingFragmentDataDownloader dataDownloader;
    private AlertDialog noWeatherResultsForLocationDialog;

    public IntroActivityLoadingFragmentNoWeatherResultsForLocationDialogInitializer(Activity activity,
                                                                                    IntroActivityLoadingFragmentDataDownloader dataDownloader){
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
            ShowLoadingFragmentChangeLocationAfterFailureDialogRunnableInitializer runnableInitializer
                    =new ShowLoadingFragmentChangeLocationAfterFailureDialogRunnableInitializer(activity,dataDownloader);
            return runnableInitializer.getShowChangeLocationAfterFailureDialogRunnable();
        }
    }

    private Runnable getNegativeButtonRunnable(){
       return new ShowLoadingFragmentExitDialogRunnable(activity,showNoWeatherResultsForLocationRunnable);
    }

    private Runnable showNoWeatherResultsForLocationRunnable = new Runnable() {
        public void run() {noWeatherResultsForLocationDialog.show();}
    };
}
