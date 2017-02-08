package paweltypiak.matweather.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.introActivityLoadingFragmentDialogInitializing;

import paweltypiak.matweather.introActivityInitializing.introActivityFragmentInitializing.introActivityLocationFragmentInitializing.ShowLocationFragmentAgainListener;
import paweltypiak.matweather.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.IntroActivityLoadingFragmentDataDownloader;

class ShowLocationFragmentRunnableInitializer {

    private IntroActivityLoadingFragmentDataDownloader dataDownloader;

    ShowLocationFragmentRunnableInitializer(IntroActivityLoadingFragmentDataDownloader dataDownloader){
        this.dataDownloader=dataDownloader;
    }

    Runnable getShowLocationFragmentRunnable() {
        return showLocationFragmentRunnable;
    }

    private Runnable showLocationFragmentRunnable = new Runnable() {
        public void run() {
            ShowLocationFragmentAgainListener showLocationFragmentAgainListener
                    =dataDownloader.getLocationListener();
            showLocationFragmentAgainListener.showLocationFragmentAgain();
        }
    };
}