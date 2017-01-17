package paweltypiak.matweather.dialogsInitializing;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import paweltypiak.matweather.MainActivity;
import paweltypiak.matweather.R;
import paweltypiak.matweather.mainActivityLayoutInitializing.LayoutUpdating.OnWeatherDataChangeLayoutUpdater;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

public class MapsDialogInitializer {

    public static AlertDialog initializeMapsDialog(Activity activity) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.map_intent_dialog, null);
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.location_dialog_message_text);
        messageTextView.setText(activity.getString(R.string.maps_dialog_message));
        TextView cityTextView=(TextView)dialogView.findViewById(R.id.location_dialog_city_text);
        String [] locationName=((MainActivity)activity).
                getMainActivityLayoutInitializer().
                getAppBarLayoutInitializer().
                getAppBarLayoutDataInitializer().
                getAppBarLocationName();
        cityTextView.setText(locationName[0]);
        TextView regionCountryTextView=(TextView)dialogView.findViewById(R.id.location_dialog_region_county_text);
        regionCountryTextView.setText(locationName[1]);
        double currentLatitude= OnWeatherDataChangeLayoutUpdater.getCurrentWeatherDataParser().getLatitude();
        double currentLongitude=OnWeatherDataChangeLayoutUpdater.getCurrentWeatherDataParser().getLongitude();
        double [] currentLocationCoordinates={currentLatitude,currentLongitude};
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder(activity,
                dialogView,
                R.style.DialogStyle,
                activity.getString(R.string.maps_dialog_title),
                R.drawable.dialog_maps_icon,
                null,
                false,
                activity.getString(R.string.maps_dialog_positive_button),
                new initializeMapsIntentRunnable(activity,locationName[0],currentLocationCoordinates[0], currentLocationCoordinates[1]),
                null,
                null,
                activity.getString(R.string.maps_dialog_negative_button),
                null);
        AlertDialog mapsDialog = alertDialogBuilder.getAlertDialog();
        return mapsDialog;
    }

    private static class initializeMapsIntentRunnable implements Runnable{

        private Activity activity;
        private String label;
        private double longitude;
        private double latitude;

        public initializeMapsIntentRunnable(Activity activity,
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
            UsefulFunctions.initializeMapsIntent(activity, latitude, longitude,label);
        }
    }
}
