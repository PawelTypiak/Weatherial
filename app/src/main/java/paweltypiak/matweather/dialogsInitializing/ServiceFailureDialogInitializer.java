package paweltypiak.matweather.dialogsInitializing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import paweltypiak.matweather.R;

public class ServiceFailureDialogInitializer {

    private AlertDialog serviceFailureDialog;
    private AlertDialogBuilder alertDialogBuilder;

    public ServiceFailureDialogInitializer(Activity activity,
                                           int type,
                                           Runnable positiveButtonRunnable,
                                           final Runnable negativeButtonRunnable){
        serviceFailureDialog=initializeServiceFailureDialog(activity,type,positiveButtonRunnable,negativeButtonRunnable);
    }

    public AlertDialog initializeServiceFailureDialog(Activity activity,
                                               int type,
                                               Runnable positiveButtonRunnable,
                                               final Runnable negativeButtonRunnable){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_one_line_text,null);
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.one_line_text_dialog_message_text);
        messageTextView.setText(activity.getString(R.string.service_failure_dialog_message));
        String negativeButtonString=null;
        boolean isUncancelable=false;
        if(type==0){
            negativeButtonString=activity.getString(R.string.service_failure_dialog_negative_button_type_0);
            isUncancelable=true;
        }
        else if(type==1){
            negativeButtonString=activity.getString(R.string.service_failure_dialog_negative_button_type_1);
            isUncancelable=false;
        }
        alertDialogBuilder=new AlertDialogBuilder(activity,
                dialogView,
                R.style.DialogStyle,
                activity.getString(R.string.failure_dialog_title),
                R.drawable.dialog_error_icon,
                null,
                isUncancelable,
                activity.getString(R.string.service_failure_dialog_positive_button),
                positiveButtonRunnable,
                null,
                null,
                negativeButtonString,
                negativeButtonRunnable);
        AlertDialog serviceFailureDialog = alertDialogBuilder.getAlertDialog();
        serviceFailureDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if(negativeButtonRunnable!=null && alertDialogBuilder.isWasDialogClickedOutside()==true){
                    negativeButtonRunnable.run();
                }
            }
        });
        return serviceFailureDialog;
    }

    public AlertDialog getServiceFailureDialog() {
        return serviceFailureDialog;
    }
}
