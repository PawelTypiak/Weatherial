package paweltypiak.matweather.dialogsInitializing;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import paweltypiak.matweather.R;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataParser;

public class WeatherResultsForLocationDialogInitializer {

    public static AlertDialog initializeWeatherResultsForLocationDialog(Activity activity,
                                                                 int type,
                                                                 WeatherDataParser dataInitializer,
                                                                 Runnable positiveButtonRunnable,
                                                                 Runnable neutralButtonRunnable,
                                                                 Runnable negativeButtonRunnable) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.map_intent_dialog, null);
        String city=dataInitializer.getCity();
        String region=dataInitializer.getRegion();
        String country=dataInitializer.getCountry();
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.location_dialog_message_text);
        messageTextView.setText(activity.getString(R.string.weather_results_for_location_dialog_message));
        TextView cityTextView=(TextView)dialogView.findViewById(R.id.location_dialog_city_text);
        cityTextView.setText(city);
        TextView regionCountryTextView=(TextView)dialogView.findViewById(R.id.location_dialog_region_county_text);
        regionCountryTextView.setText(region+", "+country);
        boolean ifUncancellable=false;
        String negativeButtonString=null;
        if(type==0){
            ifUncancellable=true;
            negativeButtonString=activity.getString(R.string.first_launch_localization_results_dialog_negative_button_type_0);
        }
        else if(type==1){
            ifUncancellable=false;
            negativeButtonString=activity.getString(R.string.weather_results_for_location_dialog_negative_button_type_1);
        }
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder(activity,
                dialogView,
                R.style.DialogStyle,
                activity.getString(R.string.weather_results_for_location_dialog_title),
                R.drawable.dialog_localization_icon,
                null,
                ifUncancellable,
                activity.getString(R.string.weather_results_for_location_dialog_positive_button),
                positiveButtonRunnable,
                activity.getString(R.string.weather_results_for_location_dialog_neutral_button),
                neutralButtonRunnable,
                negativeButtonString,
                negativeButtonRunnable);
        AlertDialog weatherResultsForLocationDialog = alertDialogBuilder.getAlertDialog();
        return weatherResultsForLocationDialog;
    }
}
