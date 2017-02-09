package paweltypiak.weatherial.dialogsInitializing.dialogInitializers.exitDialogInitializing;

import android.app.Activity;

class FinishRunnable implements Runnable{

    private Activity activity;

    FinishRunnable(Activity activity){
        this.activity=activity;
    }

    @Override
    public void run() {
        activity.finish();
        return;
    }
}
