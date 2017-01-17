package paweltypiak.matweather.dialogsInitializing;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import paweltypiak.matweather.R;

public class GeolocalizationPermissionsDeniedDialogInitializer {

    public static AlertDialog initializeGeolocalizationPermissionsDeniedDialog(Activity activity,
                                                         int type,
                                                         Runnable positiveButtonRunnable,
                                                         Runnable negativeButtonRunnable){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_one_line_text,null);
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.one_line_text_dialog_message_text);
        messageTextView.setText(activity.getString(R.string.permission_denied_dialog_message));
        boolean isUncancelable=false;
        String positiveButtonString=null;
        String negativeButtonString=null;
        if(type==0){
            isUncancelable=true;
            positiveButtonString=activity.getString(R.string.permission_denied_dialog_positive_button_type_0);
            negativeButtonString=activity.getString(R.string.permission_denied_dialog_negative_button_type_0);
        }
        else if(type==1){
            isUncancelable=false;
            positiveButtonString=activity.getString(R.string.permission_denied_dialog_positive_button_type_1);
            negativeButtonString=activity.getString(R.string.permission_denied_dialog_negative_button_type_1);
        }
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder(activity,
                dialogView,
                R.style.DialogStyle,
                activity.getString(R.string.permission_denied_dialog_title),
                R.drawable.dialog_error_icon,
                null,
                isUncancelable,
                positiveButtonString,
                positiveButtonRunnable,
                null,
                null,
                negativeButtonString,
                negativeButtonRunnable);
        AlertDialog geolocalizationPermissionsDeniedDialog=alertDialogBuilder.getAlertDialog();
        return geolocalizationPermissionsDeniedDialog;
    }
}
