package paweltypiak.matweather.dialogsInitializing.dialogInitializers.yahooRedirectDialogInitializing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import paweltypiak.matweather.usefulClasses.UsefulFunctions;

class WebIntentRunnable implements Runnable {

    private Activity activity;
    private String url;

    WebIntentRunnable(Activity activity,String url) {
        this.activity=activity;
        this.url=url;
    }
    public void run() {
        initializeWebIntent(activity,url);
    }

    public static void initializeWebIntent(Context context, String url){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }
}