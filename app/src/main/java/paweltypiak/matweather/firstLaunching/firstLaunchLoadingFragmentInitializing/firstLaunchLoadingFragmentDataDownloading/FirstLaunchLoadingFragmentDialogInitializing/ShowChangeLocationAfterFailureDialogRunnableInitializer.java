package paweltypiak.matweather.firstLaunching.firstLaunchLoadingFragmentInitializing.firstLaunchLoadingFragmentDataDownloading.FirstLaunchLoadingFragmentDialogInitializing;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import paweltypiak.matweather.dialogsInitializing.dialogInitializers.favouritesDialogInitializing.FavouritesDialogInitializer;
import paweltypiak.matweather.firstLaunching.firstLaunchLoadingFragmentInitializing.firstLaunchLoadingFragmentDataDownloading.FirstLaunchLoadingFragmentDataDownloader;
import paweltypiak.matweather.usefulClasses.FavouritesEditor;

class ShowChangeLocationAfterFailureDialogRunnableInitializer {

    private Activity activity;
    private FirstLaunchLoadingFragmentDataDownloader dataDownloader;

    ShowChangeLocationAfterFailureDialogRunnableInitializer(Activity activity,
                                                                   FirstLaunchLoadingFragmentDataDownloader dataDownloader){
        this.activity=activity;
        this.dataDownloader=dataDownloader;
    }

    Runnable getShowChangeLocationAfterFailureDialogRunnable() {
        return showChangeLocationAfterFailureDialogRunnable;
    }

    private Runnable showChangeLocationAfterFailureDialogRunnable =new Runnable() {
        @Override
        public void run() {
            showDialog(getChangeLocationAfterFailureDialog());
        }

    };

    private AlertDialog getChangeLocationAfterFailureDialog(){
        FavouritesDialogInitializer favouritesDialogInitializer
                = new FavouritesDialogInitializer(
                activity,
                0,
                positiveButtonRunnable,
                getNegativeButtonRunnable());
        return favouritesDialogInitializer.getFavouritesDialog();
    }

    private Runnable positiveButtonRunnable = new Runnable() {
        public void run() {
            dataDownloader.setChangedLocation(getChangedLocation());
            dataDownloader.initializeNextLaunchAfterFailure();
        }
    };

    private Runnable getNegativeButtonRunnable(){
        return new ShowExitDialogRunnable(activity,showChangeLocationAfterFailureDialogRunnable);
    }

    private String getChangedLocation(){
        int selectedLocationID= FavouritesEditor.getSelectedFavouriteLocationID();
        int numberOfFavourites=FavouritesEditor.getNumberOfFavourites(activity);
        if(selectedLocationID==numberOfFavourites) {
            return null;
        }
        else {
            return FavouritesEditor.getSelectedFavouriteLocationAddress(activity);
        }
    }

    private void showDialog(AlertDialog alertDialog){
        alertDialog.show();
        dataDownloader.setLoadingViewsVisibility(false);
    }
}
