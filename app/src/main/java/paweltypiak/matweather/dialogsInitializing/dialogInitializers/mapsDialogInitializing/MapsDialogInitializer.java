package paweltypiak.matweather.dialogsInitializing.dialogInitializers.mapsDialogInitializing;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import paweltypiak.matweather.MainActivity;
import paweltypiak.matweather.R;
import paweltypiak.matweather.dialogsInitializing.AlertDialogBuilder;
import paweltypiak.matweather.dialogsInitializing.alertDialogTools.AlertDialogButtonsCustomizer;
import paweltypiak.matweather.mainActivityLayoutInitializing.LayoutUpdating.OnWeatherDataChangeLayoutUpdater;

public class MapsDialogInitializer {

    public static AlertDialog getMapsDialog(Activity activity){
        return initializeMapsDialog(activity);
    }

    private static AlertDialog initializeMapsDialog(Activity activity) {
        View dialogView=initializeDialogView(activity);
        initializeMessageTextView(activity,dialogView);
        String[] locationName=initializeLocationNameTextViews(activity,dialogView);
        AlertDialog mapsDialog = buildAlertDialog(
                activity,
                dialogView,
                locationName
        );
        setAlertDialogOnShowListener(activity,mapsDialog);
        return mapsDialog;
    }

    private static View initializeDialogView(Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.map_intent_dialog, null);
        return dialogView;
    }

    private static void initializeMessageTextView(Activity activity,View dialogView){
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.location_dialog_message_text);
        messageTextView.setText(activity.getString(R.string.maps_dialog_message));
    }

    private static String[] initializeLocationNameTextViews(Activity activity,View dialogView){
        String [] locationName=((MainActivity)activity).
                getMainActivityLayoutInitializer().
                getAppBarLayoutInitializer().
                getAppBarLayoutDataInitializer().
                getAppBarLocationName();
        TextView titleTextView=(TextView)dialogView.findViewById(R.id.location_dialog_title_text);
        titleTextView.setText(locationName[0]);
        TextView subtitleTextView=(TextView)dialogView.findViewById(R.id.location_dialog_subtitle_text);
        subtitleTextView.setText(locationName[1]);
        return locationName;
    }

    private static AlertDialog buildAlertDialog(Activity activity,
                                                View dialogView,
                                                String[] locationName){
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder(
                activity,
                dialogView,
                R.style.DialogStyle,
                activity.getString(R.string.maps_dialog_title),
                R.drawable.maps_icon,
                null,
                false,
                activity.getString(R.string.maps_dialog_positive_button),
                new MapsIntentRunnable(activity,
                        locationName[0],
                        getCurrentLocationLatitude(),
                        getCurrentLocationLongitude()),
                null,
                null,
                activity.getString(R.string.maps_dialog_negative_button),
                null);
        return alertDialogBuilder.getAlertDialog();
    }

    private static double getCurrentLocationLatitude(){
        double currentLatitude= OnWeatherDataChangeLayoutUpdater.getCurrentWeatherDataParser().getLatitude();
        return currentLatitude;
    }

    private static double getCurrentLocationLongitude(){
        double currentLongitude=OnWeatherDataChangeLayoutUpdater.getCurrentWeatherDataParser().getLongitude();
        return currentLongitude;
    }

    private static void setAlertDialogOnShowListener(final Activity activity, final AlertDialog mapsDialog){
        mapsDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                AlertDialogButtonsCustomizer.setDialogButtonsTextFont(activity,mapsDialog);
            }
        });
    }
}
