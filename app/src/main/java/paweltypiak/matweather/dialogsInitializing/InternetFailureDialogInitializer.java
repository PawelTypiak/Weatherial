package paweltypiak.matweather.dialogsInitializing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import paweltypiak.matweather.R;

public class InternetFailureDialogInitializer {

    private AlertDialogBuilder alertDialogBuilder;
    private AlertDialog internetFailureDialog;

    public InternetFailureDialogInitializer (Activity activity,
                                             int type,
                                             Runnable positiveButtonRunnable,
                                             final Runnable negativeButtonRunnable){
        internetFailureDialog=initializeInternetFailureDialog(activity,type,positiveButtonRunnable,negativeButtonRunnable);
    }

    private AlertDialog initializeInternetFailureDialog(Activity activity,
                                                int type,
                                                Runnable positiveButtonRunnable,
                                                final Runnable negativeButtonRunnable){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_one_line_text,null);
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.one_line_text_dialog_message_text);
        messageTextView.setText(activity.getString(R.string.internet_failure_dialog_message));
        boolean isUncancelable=false;
        String negativeButtonString=null;
        if(type==0){
            isUncancelable=true;
            negativeButtonString=activity.getString(R.string.internet_failure_dialog_negative_button_type_0);
        }
        else if(type==1){
            isUncancelable=false;
            negativeButtonString=activity.getString(R.string.internet_failure_dialog_negative_button_type_1);
        }
        alertDialogBuilder=new AlertDialogBuilder( activity,
                dialogView,
                R.style.DialogStyle,
                activity.getString(R.string.failure_dialog_title),
                R.drawable.dialog_error_icon,
                null,
                isUncancelable,
                activity.getString(R.string.internet_failure_dialog_positive_button),
                positiveButtonRunnable,
                null,
                null,
                negativeButtonString,
                negativeButtonRunnable);
        AlertDialog internetFailureDialog = alertDialogBuilder.getAlertDialog();
        if(type==1){
            internetFailureDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    if(negativeButtonRunnable!=null && alertDialogBuilder.isWasDialogClickedOutside()==true){
                        negativeButtonRunnable.run();
                    }
                }
            });
        }
        return internetFailureDialog;
    }

    public AlertDialog getInternetFailureDialog() {
        return internetFailureDialog;
    }
}
