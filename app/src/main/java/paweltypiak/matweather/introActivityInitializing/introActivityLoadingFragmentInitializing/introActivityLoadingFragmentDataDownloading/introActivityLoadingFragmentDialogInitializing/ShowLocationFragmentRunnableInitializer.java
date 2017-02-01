package paweltypiak.matweather.introActivityInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.introActivityLoadingFragmentDialogInitializing;

import paweltypiak.matweather.introActivityInitializing.introActivityLocationFragmentInitializing.ShowLocationFragmentAgainListener;
import paweltypiak.matweather.introActivityInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.IntroActivityLoadingFragmentDataDownloader;

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
