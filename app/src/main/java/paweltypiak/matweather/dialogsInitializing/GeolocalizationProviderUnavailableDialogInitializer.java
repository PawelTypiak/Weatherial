package paweltypiak.matweather.dialogsInitializing;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import paweltypiak.matweather.R;

public class GeolocalizationProviderUnavailableDialogInitializer {

    public static AlertDialog initializeGolocalizationProviderUnavailableDialog(Activity activity,
                                                                   int type,
                                                                   Runnable positiveButtonRunnable){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_one_line_text,null);
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.one_line_text_dialog_message_text);
        String message=null;
        String title=null;
        String positiveButtonString=null;
        if(type==0){
            message=activity.getString(R.string.gps_unavailable_dialog_message);
            title=activity.getString(R.string.gps_unavailable_dialog_title);
            positiveButtonString=activity.getString(R.string.gps_unavailable_dialog_positive_button);
        }
        else if(type==1){
            message=activity.getString(R.string.network_unavailable_dialog_message);
            title=activity.getString(R.string.network_unavailable_dialog_title);
            positiveButtonString=activity.getString(R.string.network_unavailable_dialog_positive_button);
        }
        messageTextView.setText(message);
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder(activity,
                dialogView,
                R.style.DialogStyle,
                title,
                R.drawable.dialog_warning_icon,
                null,
                true,
                positiveButtonString,
                positiveButtonRunnable,
                null,
                null,
                null,
                null);
        AlertDialog geolocalizationProviderUnavailableDialog=alertDialogBuilder.getAlertDialog();
        return geolocalizationProviderUnavailableDialog;
    }
}
