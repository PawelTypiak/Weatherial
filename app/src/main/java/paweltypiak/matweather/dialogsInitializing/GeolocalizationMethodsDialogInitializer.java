package paweltypiak.matweather.dialogsInitializing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import paweltypiak.matweather.R;
import paweltypiak.matweather.usefulClasses.SharedPreferencesModifier;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

public class GeolocalizationMethodsDialogInitializer {

    private AlertDialog geolocalizationMethodsDialog;

    public GeolocalizationMethodsDialogInitializer(final Activity activity,
                                                   int type,
                                                   Runnable positiveButtonRunnable){
        initializeGeolocalizationMethodsDialog(activity,type,positiveButtonRunnable);
    }

    public AlertDialog initializeGeolocalizationMethodsDialog(final Activity activity,
                                                              int type,
                                                              Runnable positiveButtonRunnable){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_geolocalization_options,null);
        RadioGroup radioGroup=(RadioGroup)dialogView.findViewById(R.id.geolocalization_options_dialog_radio_group);
        RadioButton gpsRadioButton=new RadioButton(activity);
        gpsRadioButton.setText(activity.getString(R.string.geolocalization_method_gps));
        gpsRadioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,activity.getResources().getDimensionPixelSize(R.dimen.dialog_text_size));
        gpsRadioButton.setTextColor(activity.getResources().getColor(R.color.textSecondaryLightBackground));
        gpsRadioButton.setId(R.id.geolocalization_method_dialog_gps_radio_button_id);
        UsefulFunctions.setRadioButtonMargins(gpsRadioButton,activity,0,0,0,16);
        radioGroup.addView(gpsRadioButton);
        RadioButton networkRadioButton=new RadioButton(activity);
        networkRadioButton.setText(activity.getString(R.string.geolocalization_method_network));
        networkRadioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,activity.getResources().getDimensionPixelSize(R.dimen.dialog_text_size));
        networkRadioButton.setTextColor(activity.getResources().getColor(R.color.textSecondaryLightBackground));
        networkRadioButton.setId(R.id.geolocalization_method_dialog_network_radio_button_id);
        radioGroup.addView(networkRadioButton);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                UsefulFunctions.setDialogButtonEnabled(geolocalizationMethodsDialog,activity);
                if (i == R.id.geolocalization_method_dialog_gps_radio_button_id) {
                    SharedPreferencesModifier.setGeolocalizationMethod(activity,0);
                } else if (i == R.id.geolocalization_method_dialog_network_radio_button_id) {
                    SharedPreferencesModifier.setGeolocalizationMethod(activity,1);
                }
            }
        });
        boolean isUncancelable=true;
        if(type==0){
            isUncancelable=true;
        }
        else if(type==1){
            isUncancelable=false;
        }
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder(activity,
                dialogView,
                R.style.LoadingDialogStyle,
                activity.getString(R.string.geolocalization_methods_dialog_title),
                R.drawable.dialog_geolocalization_method_icon,
                null,
                isUncancelable,
                activity.getString(R.string.geolocalization_methods_dialog_positive_button),
                positiveButtonRunnable,
                null,
                null,
                null,
                null);
        geolocalizationMethodsDialog = alertDialogBuilder.getAlertDialog();
        geolocalizationMethodsDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                UsefulFunctions.setDialogButtonDisabled(geolocalizationMethodsDialog,activity);
            }
        });
        return geolocalizationMethodsDialog;
    }

    public AlertDialog getGeolocalizationMethodsDialog() {
        return geolocalizationMethodsDialog;
    }
}
