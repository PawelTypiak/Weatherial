package paweltypiak.matweather.dialogsInitializing.dialogInitializers;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import paweltypiak.matweather.R;
import paweltypiak.matweather.dialogsInitializing.AlertDialogBuilder;
import paweltypiak.matweather.dialogsInitializing.alertDialogTools.AlertDialogButtonsCustomizer;
import paweltypiak.matweather.dataDownloading.weatherDataDownloading.WeatherDataParser;

public class WeatherResultsForLocationDialogInitializer {

    public static AlertDialog getWeatherResultsForLocationDialog(Activity activity,
                                                                  int type,
                                                                  WeatherDataParser dataInitializer,
                                                                  Runnable positiveButtonRunnable,
                                                                  Runnable neutralButtonRunnable,
                                                                  Runnable negativeButtonRunnable){
        return initializeWeatherResultsForLocationDialog(
                activity,
                type,
                dataInitializer,
                positiveButtonRunnable,
                neutralButtonRunnable,
                negativeButtonRunnable
        );
    }

    private static AlertDialog initializeWeatherResultsForLocationDialog(Activity activity,
                                                                 int type,
                                                                 WeatherDataParser weatherDataParser,
                                                                 Runnable positiveButtonRunnable,
                                                                 Runnable neutralButtonRunnable,
                                                                 Runnable negativeButtonRunnable) {
        View dialogView=initializeDialogView(activity);
        initializeMessageTextView(activity,dialogView);
        initializeLocationNameTextViews(weatherDataParser,dialogView);
        AlertDialog weatherResultsForLocationDialog = buildAlertDialog(
                activity,
                type,
                positiveButtonRunnable,
                neutralButtonRunnable,
                negativeButtonRunnable,
                dialogView
        );
        setAlertDialogOnShowListener(activity,weatherResultsForLocationDialog);
        return weatherResultsForLocationDialog;
    }

    private static View initializeDialogView(Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.map_intent_dialog, null);
        return dialogView;
    }

    private static void initializeMessageTextView(Activity activity,View dialogView){
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.location_dialog_message_text);
        messageTextView.setText(activity.getString(R.string.weather_results_for_location_dialog_message));
    }

    private static void initializeLocationNameTextViews(WeatherDataParser weatherDataParser,View dialogView){
        String city=weatherDataParser.getCity();
        String region=weatherDataParser.getRegion();
        String country=weatherDataParser.getCountry();
        TextView titleTextView=(TextView)dialogView.findViewById(R.id.location_dialog_title_text);
        titleTextView.setText(city);
        TextView subtitleTextView=(TextView)dialogView.findViewById(R.id.location_dialog_subtitle_text);
        subtitleTextView.setText(region+", "+country);
    }

    private static AlertDialog buildAlertDialog(Activity activity,
                                                int type,
                                                Runnable positiveButtonRunnable,
                                                Runnable neutralButtonRunnable,
                                                Runnable negativeButtonRunnable,
                                                View dialogView){
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder(
                activity,
                dialogView,
                R.style.DialogStyle,
                activity.getString(R.string.weather_results_for_location_dialog_title),
                R.drawable.location_icon,
                null,
                isUncancelable(type),
                activity.getString(R.string.weather_results_for_location_dialog_positive_button),
                positiveButtonRunnable,
                activity.getString(R.string.weather_results_for_location_dialog_neutral_button),
                neutralButtonRunnable,
                getNegativeButtonText(activity,type),
                negativeButtonRunnable);
        return alertDialogBuilder.getAlertDialog();
    }

    private static boolean isUncancelable(int type){
        boolean ifUncancellable=false;
        if(type==0){
            ifUncancellable=true;
        }
        else if(type==1){
            ifUncancellable=false;
        }
        return ifUncancellable;
    }

    private static String getNegativeButtonText(Activity activity,int type){
        String negativeButtonString=null;
        if(type==0){
            negativeButtonString=activity.getString(R.string.first_launch_localization_results_dialog_negative_button_type_0);
        }
        else if(type==1){
            negativeButtonString=activity.getString(R.string.weather_results_for_location_dialog_negative_button_type_1);
        }
        return negativeButtonString;
    }

    private static void setAlertDialogOnShowListener(final Activity activity,
                                                     final AlertDialog weatherResultsForLocationDialog){
        weatherResultsForLocationDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                AlertDialogButtonsCustomizer.setDialogButtonsTextFont(activity,weatherResultsForLocationDialog);
            }
        });
    }
}
