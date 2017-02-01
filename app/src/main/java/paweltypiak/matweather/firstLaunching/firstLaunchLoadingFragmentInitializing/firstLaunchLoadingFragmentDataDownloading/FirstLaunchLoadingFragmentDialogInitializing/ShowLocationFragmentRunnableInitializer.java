package paweltypiak.matweather.firstLaunching.firstLaunchLoadingFragmentInitializing.firstLaunchLoadingFragmentDataDownloading.FirstLaunchLoadingFragmentDialogInitializing;

import paweltypiak.matweather.firstLaunching.ShowLocationFragmentAgainListener;
import paweltypiak.matweather.firstLaunching.firstLaunchLoadingFragmentInitializing.firstLaunchLoadingFragmentDataDownloading.FirstLaunchLoadingFragmentDataDownloader;

class ShowLocationFragmentRunnableInitializer {

    private FirstLaunchLoadingFragmentDataDownloader dataDownloader;

    ShowLocationFragmentRunnableInitializer(FirstLaunchLoadingFragmentDataDownloader dataDownloader){
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
