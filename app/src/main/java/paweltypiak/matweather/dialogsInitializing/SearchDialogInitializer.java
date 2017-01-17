package paweltypiak.matweather.dialogsInitializing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;

import paweltypiak.matweather.MainActivity;
import paweltypiak.matweather.R;
import paweltypiak.matweather.jsonHandling.Channel;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataDownloader;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataParser;
import paweltypiak.matweather.weatherDataDownloading.WeatherDownloadCallback;

public class SearchDialogInitializer {

    private AlertDialog searchDialog;

    public SearchDialogInitializer (final Activity activity, int type, RadioButton radioButton){
        initializeSearchDialog(activity,type,radioButton);
    }

    public AlertDialog initializeSearchDialog(final Activity activity,int type,RadioButton radioButton){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_search,null);
        dialogView.setFocusableInTouchMode(true);
        dialogView.setClickable(true);
        final EditText locationEditText=(EditText)dialogView.findViewById(R.id.search_edit_text);
        locationEditText.requestFocus();
        String title=null;
        int icon=0;
        String positiveButtonString=null;
        Runnable positiveButtonRunnable=null;
        if(type==0){
            String radioButtonString=radioButton.getText().toString();
            if(!radioButtonString.equals(activity.getString(R.string.first_launch_defeault_location_different))){
                locationEditText.setText(radioButtonString);
                locationEditText.setSelection(radioButtonString.length());
            }
            title=activity.getString(R.string.search_dialog_title_type_0);
            icon=R.drawable.dialog_localization_icon;
            positiveButtonString=activity.getString(R.string.search_dialog_positive_button_type_0);
            positiveButtonRunnable=new differentLocationDialogRunnable(activity,radioButton,dialogView);
        }
        else if(type==1){
            title=activity.getString(R.string.search_dialog_title_type_1);
            icon=R.drawable.dialog_search_icon;
            positiveButtonString=activity.getString(R.string.search_dialog_positive_button_type_1);
            positiveButtonRunnable=new searchRunnable(activity,dialogView);
        }
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder(activity,
                dialogView,
                R.style.DialogStyle,
                title,
                icon,
                null,
                false,
                positiveButtonString,
                positiveButtonRunnable ,
                null,
                null,
                activity.getString(R.string.search_dialog_negative_button),
                null);
        searchDialog=alertDialogBuilder.getAlertDialog();
        searchDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                Log.d("on_show", "search: ");
                UsefulFunctions.customizeEditText(activity,searchDialog,locationEditText);
            }
        });
        searchDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                UsefulFunctions.hideKeyboard(activity,null);
                locationEditText.getText().clear();
            }
        });
        return searchDialog;
    }

    private class showSearchDialogRunnable implements Runnable {

        private Activity activity;

        public showSearchDialogRunnable(Activity activity) {
            this.activity=activity;
        }

        public void run() {
            searchDialog=initializeSearchDialog(activity,1,null);
            searchDialog.show();
            UsefulFunctions.showKeyboard(activity);
        }
    }


    private class searchRunnable implements Runnable, WeatherDownloadCallback {

        private String location;
        private WeatherDataDownloader downloader;
        private WeatherDataParser dataInitializer;
        private EditText locationEditText;
        private boolean isReused;
        private AlertDialog progressDialog;
        private Activity activity;

        public searchRunnable(Activity activity,String location){
            //constructor for reload searchDialog
            this.activity=activity;
            this.location=location;
            isReused =true;
        }
        public searchRunnable(Activity activity,View dialogView){
            //constructor for using searchDialog for the first time
            this.activity=activity;
            locationEditText=(EditText)dialogView.findViewById(R.id.search_edit_text);
            isReused =false;
        }

        public void run(){
            progressDialog
                    =ProgressDialogInitializer.initializeProgressDialog(
                    activity,
                    activity.getString(R.string.searching_location_progress_message));
            progressDialog.show();
            if(isReused == false){
                location=locationEditText.getText().toString();
                location=UsefulFunctions.getFormattedString(location);
                UsefulFunctions.hideKeyboard(activity,locationEditText);
            }
            downloader=new WeatherDataDownloader(location,this);
        }

        @Override
        public void weatherServiceSuccess(Channel channel) {
            dataInitializer=new WeatherDataParser(channel);
            AlertDialog localizationResultsDialog
                    = WeatherResultsForLocationDialogInitializer.initializeWeatherResultsForLocationDialog(
                    activity,
                    1,
                    dataInitializer,
                    new setMainLayoutRunnable(activity,dataInitializer),
                    new showSearchDialogRunnable(activity),
                    null);
            localizationResultsDialog.show();
            progressDialog.dismiss();
        }

        @Override
        public void weatherServiceFailure(int errorCode) {
            if(errorCode==0){
                InternetFailureDialogInitializer internetFailureDialogInitializer
                        =new InternetFailureDialogInitializer(activity,
                        1,
                        new searchRunnable(activity,location),
                        null);
                AlertDialog internetFailureDialog
                        =internetFailureDialogInitializer.getInternetFailureDialog();
                internetFailureDialog.show();
            }
            else if(errorCode==1) {
                AlertDialog noWeatherResultsForLocation
                        = NoWeatherResultsForLocationDialogInitializer.initializeNoWeatherResultsForLocationDialog(
                        activity,
                        1,
                        new showSearchDialogRunnable(activity),
                        null);
                noWeatherResultsForLocation.show();
            }
            progressDialog.dismiss();
        }
    }

    private class setMainLayoutRunnable implements Runnable {

        private Activity activity;
        private WeatherDataParser dataInitializer;

        public setMainLayoutRunnable(Activity activity,
                                     WeatherDataParser dataInitializer) {
            this.activity=activity;
            this.dataInitializer = dataInitializer;
        }

        public void run() {
            ((MainActivity)activity).getMainActivityLayoutInitializer().
                    updateLayoutOnWeatherDataChange(activity,dataInitializer,true,false);
        }
    }


    private class differentLocationDialogRunnable implements  Runnable{

        private RadioButton radioButton;
        private View dialogView;
        private EditText editText;
        private String editTextString;
        private Activity activity;

        public differentLocationDialogRunnable(Activity activity,RadioButton radioButton, View dialogView) {
            this.activity=activity;
            this.radioButton = radioButton;
            this.dialogView=dialogView;
        }

        @Override
        public void run() {
            UsefulFunctions.hideKeyboard(activity,null);
            editText=(EditText)dialogView.findViewById(R.id.search_edit_text);
            editTextString=editText.getText().toString();
            editTextString =UsefulFunctions.getFormattedString(editTextString);
            radioButton.setText(editTextString);
        }
    }

    public AlertDialog getSearchDialog() {
        return searchDialog;
    }
}
