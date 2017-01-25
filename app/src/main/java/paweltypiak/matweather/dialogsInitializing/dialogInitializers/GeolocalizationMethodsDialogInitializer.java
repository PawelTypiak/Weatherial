package paweltypiak.matweather.dialogsInitializing.dialogInitializers;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import paweltypiak.matweather.R;
import paweltypiak.matweather.dialogsInitializing.AlertDialogBuilder;
import paweltypiak.matweather.dialogsInitializing.alertDialogTools.AlertDialogButtonsCustomizer;
import paweltypiak.matweather.usefulClasses.SharedPreferencesModifier;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

public class GeolocalizationMethodsDialogInitializer {

    private AlertDialog geolocalizationMethodsDialog;

    public GeolocalizationMethodsDialogInitializer(Activity activity,
                                                   int type,
                                                   Runnable positiveButtonRunnable){
        initializeGeolocalizationMethodsDialog(activity,type,positiveButtonRunnable);
    }

    public AlertDialog getGeolocalizationMethodsDialog() {
        return geolocalizationMethodsDialog;
    }

    private AlertDialog initializeGeolocalizationMethodsDialog(Activity activity,
                                                              int type,
                                                              Runnable positiveButtonRunnable){
        View dialogView = initializeDialogView(activity);
        initializeRadioGroup(activity,dialogView);
        geolocalizationMethodsDialog = buildAlertDialog(
                activity,
                type,
                positiveButtonRunnable,
                dialogView);
        setAlertDialogOnShowListener(activity);
        return geolocalizationMethodsDialog;
    }

    private View initializeDialogView(Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_geolocalization_options,null);
        return dialogView;
    }

    private void initializeRadioGroup(Activity activity,View dialogView){
        RadioGroup radioGroup=(RadioGroup)dialogView.findViewById(R.id.geolocalization_options_dialog_radio_group);
        initializeGpsRadioButton(activity,radioGroup);
        initializeNetworkRadioButton(activity,radioGroup);
        setRadioGroupOnCheckedChangeListener(activity,radioGroup);
    }

    private void initializeGpsRadioButton(Activity activity, RadioGroup radioGroup)
    {
        RadioButton gpsRadioButton=new RadioButton(activity);
        gpsRadioButton.setText(activity.getString(R.string.geolocalization_method_gps));
        gpsRadioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,activity.getResources().getDimensionPixelSize(R.dimen.dialog_text_size));
        gpsRadioButton.setTextColor(activity.getResources().getColor(R.color.textSecondaryLightBackground));
        gpsRadioButton.setId(R.id.geolocalization_method_dialog_gps_radio_button_id);
        UsefulFunctions.setRadioButtonMargins(gpsRadioButton,activity,0,0,0,16);
        radioGroup.addView(gpsRadioButton);
    }

    private void initializeNetworkRadioButton(Activity activity,RadioGroup radioGroup){
        RadioButton networkRadioButton=new RadioButton(activity);
        networkRadioButton.setText(activity.getString(R.string.geolocalization_method_network));
        networkRadioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,activity.getResources().getDimensionPixelSize(R.dimen.dialog_text_size));
        networkRadioButton.setTextColor(activity.getResources().getColor(R.color.textSecondaryLightBackground));
        networkRadioButton.setId(R.id.geolocalization_method_dialog_network_radio_button_id);
        radioGroup.addView(networkRadioButton);
    }

    private void setRadioGroupOnCheckedChangeListener(final Activity activity, RadioGroup radioGroup){
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                AlertDialogButtonsCustomizer.setDialogButtonEnabled(geolocalizationMethodsDialog,activity);
                if (i == R.id.geolocalization_method_dialog_gps_radio_button_id) {
                    SharedPreferencesModifier.setGeolocalizationMethod(activity,0);
                } else if (i == R.id.geolocalization_method_dialog_network_radio_button_id) {
                    SharedPreferencesModifier.setGeolocalizationMethod(activity,1);
                }
            }
        });
    }

    private AlertDialog buildAlertDialog(Activity activity,
                                         int type,
                                         Runnable positiveButtonRunnable,
                                         View dialogView){
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder(
                activity,
                dialogView,
                R.style.LoadingDialogStyle,
                activity.getString(R.string.geolocalization_methods_dialog_title),
                R.drawable.localization_method_icon,
                null,
                isUncancelable(type),
                activity.getString(R.string.geolocalization_methods_dialog_positive_button),
                positiveButtonRunnable,
                null,
                null,
                null,
                null);
        return alertDialogBuilder.getAlertDialog();
    }

    private boolean isUncancelable(int type){
        boolean isUncancelable=true;
        if(type==0){
            isUncancelable=true;
        }
        else if(type==1){
            isUncancelable=false;
        }
        return isUncancelable;
    }

    private void setAlertDialogOnShowListener(final Activity activity){
        geolocalizationMethodsDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                AlertDialogButtonsCustomizer.setDialogButtonsTextFont(activity,geolocalizationMethodsDialog);
                AlertDialogButtonsCustomizer.setDialogButtonsTextFont(activity,geolocalizationMethodsDialog);
                AlertDialogButtonsCustomizer.setDialogButtonDisabled(geolocalizationMethodsDialog,activity);
            }
        });
    }
}
