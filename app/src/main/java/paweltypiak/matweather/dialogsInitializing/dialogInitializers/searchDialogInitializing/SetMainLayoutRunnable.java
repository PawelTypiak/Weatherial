package paweltypiak.matweather.dialogsInitializing.dialogInitializers.searchDialogInitializing;

import android.app.Activity;

import paweltypiak.matweather.MainActivity;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataParser;

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
