package paweltypiak.matweather.firstLaunching.firstLaunchLoadingFragmentInitializing.firstLaunchLoadingFragmentDataDownloading.FirstLaunchLoadingFragmentDialogInitializing;

import android.app.Activity;
import android.support.v7.app.AlertDialog;

import paweltypiak.matweather.dialogsInitializing.dialogInitializers.exitDialogInitializing.ExitDialogInitializer;

class ShowExitDialogRunnable implements Runnable {

    private Activity activity;
    private Runnable showDialogRunnable;

    ShowExitDialogRunnable(Activity activity,
                                  Runnable showDialogRunnable) {
        this.activity=activity;
        this.showDialogRunnable = showDialogRunnable;
    }

    public void run() {
        AlertDialog exitDialog= ExitDialogInitializer.getExitDialog(activity,0, showDialogRunnable);
        exitDialog.show();
    }
}
