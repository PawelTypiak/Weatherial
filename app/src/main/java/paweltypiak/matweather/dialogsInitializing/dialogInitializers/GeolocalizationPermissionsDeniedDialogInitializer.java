package paweltypiak.matweather.dialogsInitializing.dialogInitializers;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import paweltypiak.matweather.R;
import paweltypiak.matweather.dialogsInitializing.AlertDialogBuilder;

public class GeolocalizationPermissionsDeniedDialogInitializer {

    public static AlertDialog getGeolocalizationPermissionsDeniedDialog(Activity activity,
                                                            int type,
                                                            Runnable positiveButtonRunnable,
                                                            Runnable negativeButtonRunnable){
        return initializeGeolocalizationPermissionsDeniedDialog(
                activity,
                type,
                positiveButtonRunnable,
                negativeButtonRunnable);
    }

    private static AlertDialog initializeGeolocalizationPermissionsDeniedDialog(Activity activity,
                                                         int type,
                                                         Runnable positiveButtonRunnable,
                                                         Runnable negativeButtonRunnable){
        View dialogView=initializeDialogView(activity);
        initializeMessageTextView(activity,dialogView);
        AlertDialog geolocalizationPermissionsDeniedDialog=buildAlertDialog(
                activity,
                type,
                positiveButtonRunnable,
                negativeButtonRunnable,
                dialogView);
        return geolocalizationPermissionsDeniedDialog;
    }

    private static View initializeDialogView(Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_one_line_text,null);
        return dialogView;
    }

    private static void initializeMessageTextView(Activity activity,View dialogView){
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.one_line_text_dialog_message_text);
        messageTextView.setText(activity.getString(R.string.permission_denied_dialog_message));
    }

    private static AlertDialog buildAlertDialog(Activity activity,
                                                int type,
                                                Runnable positiveButtonRunnable,
                                                Runnable negativeButtonRunnable,
                                                View dialogView){
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder(
                activity,
                dialogView,
                R.style.DialogStyle,
                activity.getString(R.string.permission_denied_dialog_title),
                R.drawable.dialog_error_icon,
                null,
                isUncancelable(type),
                getPositiveButtonText(activity,type),
                positiveButtonRunnable,
                null,
                null,
                getNegativeButtonText(activity,type),
                negativeButtonRunnable);
        return alertDialogBuilder.getAlertDialog();
    }

    private static boolean isUncancelable(int type){
        boolean isUncancelable=false;
        if(type==0){
            isUncancelable=true;
        }
        else if(type==1){
            isUncancelable=false;
        }
        return  isUncancelable;
    }

    private static String getPositiveButtonText(Activity activity, int type){
        String positiveButtonText=null;
        if(type==0){
            positiveButtonText=activity.getString(R.string.permission_denied_dialog_positive_button_type_0);
        }
        else if(type==1){
            positiveButtonText=activity.getString(R.string.permission_denied_dialog_positive_button_type_1);
        }
        return positiveButtonText;
    }

    private static String getNegativeButtonText(Activity activity, int type){
        String negativeButtonText=null;
        if(type==0){
            negativeButtonText=activity.getString(R.string.permission_denied_dialog_negative_button_type_0);
        }
        else if(type==1){
            negativeButtonText=activity.getString(R.string.permission_denied_dialog_negative_button_type_1);
        }
        return negativeButtonText;
    }
}
