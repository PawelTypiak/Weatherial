package paweltypiak.matweather.dialogsInitializing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.List;

import paweltypiak.matweather.MainActivity;
import paweltypiak.matweather.R;
import paweltypiak.matweather.jsonHandling.Channel;
import paweltypiak.matweather.usefulClasses.FavouritesEditor;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataDownloader;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataParser;
import paweltypiak.matweather.weatherDataDownloading.WeatherDownloadCallback;

public class FavouritesDialogInitializer {

    private AlertDialog favouritesDialog;

    public FavouritesDialogInitializer(final Activity activity,
                                       final int type,
                                       Runnable positiveButtonRunnable,
                                       Runnable negativeButtonRunnable){
        favouritesDialog=initializeFavouritesDialog(activity,type,positiveButtonRunnable,negativeButtonRunnable);
    }

    private AlertDialog initializeFavouritesDialog(final Activity activity,
                                                  final int type,
                                                  Runnable positiveButtonRunnable,
                                                  Runnable negativeButtonRunnable){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_radiogroup,null);
        final RadioGroup radioGroup = (RadioGroup) dialogView.findViewById(R.id.radiogroup_dialog_radiogroup);
        final List<String> favouritesList = FavouritesEditor.getFavouriteLocationsNamesDialogList(activity);
        String locationName=null;
        int size=favouritesList.size();
        if(type==0) {
            size=++size;
        }
        for(int i=0;i<size;i++){
            RadioButton radioButton=new RadioButton(activity);
            if(i!=size-1){
                UsefulFunctions.setRadioButtonMargins(radioButton,activity,0,0,0,16);
                if(type==0) locationName=favouritesList.get(i);
            }
            else{
                if(type==0) locationName=activity.getString(R.string.favourites_dialog_geolocalization_option);
            }
            if(type==1) locationName=favouritesList.get(i);
            radioButton.setId(i);
            radioButton.setText(Html.fromHtml(locationName));
            radioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,activity.getResources().getDimensionPixelSize(R.dimen.locations_list_text_size));
            radioButton.setSingleLine();
            radioButton.setEllipsize(TextUtils.TruncateAt.END);
            radioButton.setTextColor(ContextCompat.getColor(activity,R.color.textSecondaryLightBackground));
            radioGroup.addView(radioButton);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                UsefulFunctions.setDialogButtonEnabled(favouritesDialog,activity);
                FavouritesEditor.setSelectedFavouriteLocationID(i);
                Log.d("wybrane id", ""+i);
            }
        });
        String title=null;
        String negativeButtonString=null;
        boolean isUncancelable=false;
        int icon=0;
        if(type==0) {
            title=activity.getString(R.string.favourites_dialog_first_launch_title);
            negativeButtonString=activity.getString(R.string.favourites_dialog_negative_button_type_0);
            isUncancelable=true;
            icon=R.drawable.dialog_localization_icon;
        }
        else if(type==1){
            positiveButtonRunnable=new favouritesDialogRunnable(activity);
            negativeButtonString=activity.getString(R.string.favourites_dialog_negative_button_type_1);
            title=activity.getString(R.string.favourites_dialog_title);
            isUncancelable=false;
            icon=R.drawable.dialog_favourites_icon;
        }
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder(activity,
                dialogView,
                R.style.DialogStyle,
                title,
                icon,
                null,
                isUncancelable,
                activity.getString(R.string.favourites_dialog_positive_button),
                positiveButtonRunnable,
                null,
                null,
                negativeButtonString,
                negativeButtonRunnable);
        favouritesDialog = alertDialogBuilder.getAlertDialog();
        favouritesDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                UsefulFunctions.setDialogButtonDisabled(favouritesDialog,activity);
            }
        });
        return favouritesDialog;
    }

    private static class favouritesDialogRunnable implements Runnable,WeatherDownloadCallback {

        private Activity activity;
        private AlertDialog progressDialog;

        public favouritesDialogRunnable(Activity activity) {
            this.activity= activity;
        }

        public void run() {
            String address= FavouritesEditor.getSelectedFavouriteLocationAddress(activity);
            Log.d("selected adress", address);
            new WeatherDataDownloader(address,this);
            progressDialog
                    =ProgressDialogInitializer.initializeProgressDialog(
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
                InternetFailureDialogInitializer internetFailureDialogInitializer=new InternetFailureDialogInitializer(
                       activity,1,new  favouritesDialogRunnable(activity),null);
                AlertDialog internetFailureDialog=
                        internetFailureDialogInitializer.getInternetFailureDialog();
                internetFailureDialog.show();
            }
            else {
                ServiceFailureDialogInitializer serviceFailureDialogInitializer=new ServiceFailureDialogInitializer(
                        activity,1,new favouritesDialogRunnable(activity),null);
                AlertDialog serviceFailureDialog=
                        serviceFailureDialogInitializer.getServiceFailureDialog();
                serviceFailureDialog.show();
            }
            progressDialog.dismiss();
        }
    }

    public AlertDialog getFavouritesDialog() {
        return favouritesDialog;
    }
}
