package paweltypiak.matweather.dialogsInitializing.dialogInitializers.favouritesDialogInitializing;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import paweltypiak.matweather.mainActivityInitializing.MainActivity;
import paweltypiak.matweather.R;
import paweltypiak.matweather.dialogsInitializing.dialogInitializers.InternetFailureDialogInitializer;
import paweltypiak.matweather.dialogsInitializing.dialogInitializers.ProgressDialogInitializer;
import paweltypiak.matweather.dialogsInitializing.dialogInitializers.ServiceFailureDialogInitializer;
import paweltypiak.matweather.jsonHandling.Channel;
import paweltypiak.matweather.usefulClasses.FavouritesEditor;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataDownloader;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataParser;
import paweltypiak.matweather.weatherDataDownloading.WeatherDownloadCallback;

class FavouritesDialogRunnable implements Runnable,WeatherDownloadCallback {

    private Activity activity;
    private AlertDialog progressDialog;

    FavouritesDialogRunnable(Activity activity) {
        this.activity= activity;
    }

    public void run() {
        String address= FavouritesEditor.getSelectedFavouriteLocationAddress(activity);
        Log.d("selected adress", address);
        new WeatherDataDownloader(address,this);
        progressDialog
                = ProgressDialogInitializer.getProgressDialog(
                activity,
                activity.getString(R.string.downloading_weather_data_progress_message));
        progressDialog.show();
    }

    @Override
    public void weatherServiceSuccess(Channel channel) {
        WeatherDataParser dataInitializer=new WeatherDataParser(channel);
        ((MainActivity)activity).getMainActivityLayoutInitializer().
                updateLayoutOnWeatherDataChange(activity,dataInitializer,true,false);
        progressDialog.dismiss();
    }

    @Override
    public void weatherServiceFailure(int errorCode) {
        if(errorCode==1)   {
            AlertDialog internetFailureDialog=
                    InternetFailureDialogInitializer.getInternetFailureDialog(
                            activity,
                            1,
                            new FavouritesDialogRunnable(activity),
                            null);
            internetFailureDialog.show();
        }
        else {
            AlertDialog serviceFailureDialog=
                    ServiceFailureDialogInitializer.getServiceFailureDialog(
                            activity,
                            1,
                            new FavouritesDialogRunnable(activity),
                            null);
            serviceFailureDialog.show();
        }
        progressDialog.dismiss();
    }
}