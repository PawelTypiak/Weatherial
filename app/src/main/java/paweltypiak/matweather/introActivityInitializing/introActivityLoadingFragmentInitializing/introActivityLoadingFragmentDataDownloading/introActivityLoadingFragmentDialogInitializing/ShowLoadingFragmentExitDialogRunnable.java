package paweltypiak.matweather.introActivityInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.introActivityLoadingFragmentDialogInitializing;

import android.app.Activity;
import android.support.v7.app.AlertDialog;

import paweltypiak.matweather.dialogsInitializing.dialogInitializers.exitDialogInitializing.ExitDialogInitializer;

class ShowLoadingFragmentExitDialogRunnable implements Runnable {

    private Activity activity;
    private Runnable showDialogRunnable;

    ShowLoadingFragmentExitDialogRunnable(Activity activity,
                                          Runnable showDialogRunnable) {
        this.activity=activity;
        this.showDialogRunnable = showDialogRunnable;
    }

    public void run() {
        AlertDialog exitDialog= ExitDialogInitializer.getExitDialog(activity,0, showDialogRunnable);
        exitDialog.show();
    }
}
