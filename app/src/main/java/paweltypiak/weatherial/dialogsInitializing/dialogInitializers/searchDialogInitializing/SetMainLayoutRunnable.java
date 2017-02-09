package paweltypiak.weatherial.dialogsInitializing.dialogInitializers.searchDialogInitializing;

import android.app.Activity;

import paweltypiak.weatherial.mainActivityInitializing.MainActivity;
import paweltypiak.weatherial.dataDownloading.weatherDataDownloading.WeatherDataParser;

class SetMainLayoutRunnable implements Runnable {

    private Activity activity;
    private WeatherDataParser dataInitializer;

    public SetMainLayoutRunnable(Activity activity,
                                 WeatherDataParser dataInitializer) {
        this.activity=activity;
        this.dataInitializer = dataInitializer;
    }

    public void run() {
        ((MainActivity)activity).getMainActivityLayoutInitializer().
                updateLayoutOnWeatherDataChange(activity,dataInitializer,true,false);
    }
}
