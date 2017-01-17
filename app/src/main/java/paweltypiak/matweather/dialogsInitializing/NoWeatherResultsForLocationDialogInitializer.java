package paweltypiak.matweather.dialogsInitializing;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import paweltypiak.matweather.R;

public class NoWeatherResultsForLocationDialogInitializer {

    public static AlertDialog initializeNoWeatherResultsForLocationDialog(Activity activity,
                                                                           int type,
                                                                           Runnable positiveButtonRunnable,
                                                                           Runnable negativeButtonRunnable){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_one_line_text,null);
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.one_line_text_dialog_message_text);
        messageTextView.setText(activity.getString(R.string.no_weather_results_for_location_dialog_message));
        boolean isUncancelable=false;
        String negativeButtonString=null;
        String positiveButtonString=null;
        if(type==0){
            isUncancelable=true;
            negativeButtonString=activity.getString(R.string.no_weather_results_for_location_dialog_negative_button_type_0);
            positiveButtonString=activity.getString(R.string.no_weather_results_for_location_dialog_positive_button_type_0_1);
        }
        else if(type==1){
            isUncancelable=false;
            negativeButtonString=activity.getString(R.string.no_weather_results_for_location_dialog_negative_button_type_1_2);
            positiveButtonString=activity.getString(R.string.no_weather_results_for_location_dialog_positive_button_type_0_1);
        }
        else if(type==2){
            isUncancelable=false;
            negativeButtonString=activity.getString(R.string.no_weather_results_for_location_dialog_negative_button_type_1_2);
            positiveButtonString=activity.getString(R.string.no_weather_results_for_location_dialog_positive_button_type_2);
        }
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder( activity,
                dialogView,
                R.style.DialogStyle,
                activity.getString(R.string.failure_dialog_title),
                R.drawable.dialog_error_icon,
                null,
                isUncancelable,
                positiveButtonString,
                positiveButtonRunnable,
                null,
                null,
                negativeButtonString,
                negativeButtonRunnable);
        AlertDialog noWeatherResultsForLocationDialog =alertDialogBuilder.getAlertDialog();
        return noWeatherResultsForLocationDialog;
    }
}
