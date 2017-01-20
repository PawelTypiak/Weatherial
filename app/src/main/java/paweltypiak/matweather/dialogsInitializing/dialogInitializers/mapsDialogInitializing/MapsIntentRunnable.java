package paweltypiak.matweather.dialogsInitializing.dialogInitializers.mapsDialogInitializing;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

class MapsIntentRunnable implements Runnable{

    private Activity activity;
    private String label;
    private double longitude;
    private double latitude;

    MapsIntentRunnable(Activity activity,
                                        String label,
                                        double latitude,
                                        double longitude){
        this.activity=activity;
        this.label=label;
        this.longitude=longitude;
        this.latitude=latitude;
    }

    @Override
    public void run() {
        initializeMapsIntent(activity, latitude, longitude,label);
    }

    private static void initializeMapsIntent(Context context, double latitude, double longitude , String label){
        String uriBegin = "geo:" + latitude + "," + longitude;
        String query = latitude + "," + longitude + "(" + label + ")";
        String encodedQuery = Uri.encode(query);
        String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
        Uri uri = Uri.parse(uriString);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }
}
