package paweltypiak.matweather.dialogsInitializing.dialogInitializers.rateDialogInitializing;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class RateAppRunnable implements Runnable{

    private Activity activity;

    public RateAppRunnable(Activity activity){
        this.activity=activity;
    }

    public void run() {
        rateRunnable();
    }

    private void rateRunnable (){
        try {
            activity.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=" + activity.getPackageName())));
        } catch (android.content.ActivityNotFoundException e) {
            activity.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + activity.getPackageName())));
        }
    }
}
