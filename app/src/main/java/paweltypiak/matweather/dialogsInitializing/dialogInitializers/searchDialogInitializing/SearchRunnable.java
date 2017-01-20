package paweltypiak.matweather.dialogsInitializing.dialogInitializers.searchDialogInitializing;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import paweltypiak.matweather.R;
import paweltypiak.matweather.dialogsInitializing.AlertDialogTools.KeyboardVisibilitySetter;
import paweltypiak.matweather.dialogsInitializing.dialogInitializers.InternetFailureDialogInitializer;
import paweltypiak.matweather.dialogsInitializing.dialogInitializers.NoWeatherResultsForLocationDialogInitializer;
import paweltypiak.matweather.dialogsInitializing.dialogInitializers.ProgressDialogInitializer;
import paweltypiak.matweather.dialogsInitializing.dialogInitializers.WeatherResultsForLocationDialogInitializer;
import paweltypiak.matweather.jsonHandling.Channel;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataDownloader;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataParser;
import paweltypiak.matweather.weatherDataDownloading.WeatherDownloadCallback;

class SearchRunnable implements Runnable, WeatherDownloadCallback {

    private Activity activity;
    private String location;
    private EditText locationEditText;
    private boolean isReused;
    private AlertDialog progressDialog;


    public SearchRunnable(Activity activity,String location){
        //constructor for reload searchDialog
        this.activity=activity;
        this.location=location;
        isReused =true;
    }
    public SearchRunnable(Activity activity,View dialogView){
        //constructor for using searchDialog for the first time
        this.activity=activity;
        locationEditText=(EditText)dialogView.findViewById(R.id.search_edit_text);
        isReused =false;
    }

    public void run(){
        progressDialog
                = ProgressDialogInitializer.getProgressDialog(
                activity,
                activity.getString(R.string.searching_location_progress_message));
        progressDialog.show();
        if(isReused == false){
            location=locationEditText.getText().toString();
            location= UsefulFunctions.getFormattedString(location);
            KeyboardVisibilitySetter.hideKeyboard(activity,locationEditText);
        }
        new WeatherDataDownloader(location,this);
    }

    @Override
    public void weatherServiceSuccess(Channel channel) {
        showLocalizationResultsDialog(channel);
        progressDialog.dismiss();
    }

    private void showLocalizationResultsDialog(Channel channel){
        WeatherDataParser weatherDataParser=new WeatherDataParser(channel);
        AlertDialog localizationResultsDialog
                = WeatherResultsForLocationDialogInitializer.getWeatherResultsForLocationDialog(
                activity,
                1,
                weatherDataParser,
                new SetMainLayoutRunnable(activity,weatherDataParser),
                new ShowSearchDialogRunnable(activity),
                null);
        localizationResultsDialog.show();
    }

    @Override
    public void weatherServiceFailure(int errorCode) {
        if(errorCode==0){
            showInternetFailureDialog();
        }
        else if(errorCode==1) {
            showNoWeatherResultsForLocationDialog();
        }
        progressDialog.dismiss();
    }

    private void showInternetFailureDialog(){
        AlertDialog internetFailureDialog
                = InternetFailureDialogInitializer.getInternetFailureDialog(activity,
                1,
                new SearchRunnable(activity,location),
                null);
        internetFailureDialog.show();
    }

    private void showNoWeatherResultsForLocationDialog(){
        AlertDialog noWeatherResultsForLocation
                = NoWeatherResultsForLocationDialogInitializer.getNoWeatherResultsForLocationDialog(
                activity,
                1,
                new ShowSearchDialogRunnable(activity),
                null);
        noWeatherResultsForLocation.show();
    }
}
