package paweltypiak.matweather.dialogsInitializing.dialogInitializers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import paweltypiak.matweather.R;
import paweltypiak.matweather.dialogsInitializing.AlertDialogBuilder;

public class ServiceFailureDialogInitializer {

    public static AlertDialog getServiceFailureDialog(Activity activity,
                                               int type,
                                               Runnable positiveButtonRunnable,
                                               Runnable negativeButtonRunnable) {
        return initializeServiceFailureDialog(
                activity,
                type,
                positiveButtonRunnable,
                negativeButtonRunnable);
    }

    private static AlertDialog initializeServiceFailureDialog(Activity activity,
                                               int type,
                                               Runnable positiveButtonRunnable,
                                               Runnable negativeButtonRunnable){
        View dialogView=initializeDialogView(activity);
        initializeMessageTextView(activity,dialogView);
        AlertDialogBuilder alertDialogBuilder =getAlertDialogBuilder(
                activity,
                type,
                positiveButtonRunnable,
                negativeButtonRunnable,
                dialogView);
        AlertDialog serviceFailureDialog = alertDialogBuilder.getAlertDialog();
        setAlertDialogOnDismissListener(negativeButtonRunnable,alertDialogBuilder,serviceFailureDialog);
        return serviceFailureDialog;
    }

    private static View initializeDialogView(Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_one_line_text,null);
        return dialogView;
    }

    private static void initializeMessageTextView(Activity activity, View dialogView){
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.one_line_text_dialog_message_text);
        messageTextView.setText(activity.getString(R.string.service_failure_dialog_message));
    }

    private static AlertDialogBuilder getAlertDialogBuilder(Activity activity,
                                                            int type,
                                                            Runnable positiveButtonRunnable,
                                                            Runnable negativeButtonRunnable,
                                                            View dialogView){
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder(
                activity,
                dialogView,
                R.style.DialogStyle,
                activity.getString(R.string.failure_dialog_title),
                R.drawable.dialog_error_icon,
                null,
                isUncancelable(type),
                activity.getString(R.string.service_failure_dialog_positive_button),
                positiveButtonRunnable,
                null,
                null,
                getNegativeButtonText(activity,type),
                negativeButtonRunnable);
        return alertDialogBuilder;
    }

    private static boolean isUncancelable(int type){
        boolean isUncancelable=false;
        if(type==0){
            isUncancelable=true;
        }
        else if(type==1){
            isUncancelable=false;
        }
        return isUncancelable;
    }

    private static String getNegativeButtonText(Activity activity, int type){
        String negativeButtonText=null;
        if(type==0){
            negativeButtonText=activity.getString(R.string.service_failure_dialog_negative_button_type_0);
        }
        else if(type==1){
            negativeButtonText=activity.getString(R.string.service_failure_dialog_negative_button_type_1);
        }
        return negativeButtonText;
    }

    private static void setAlertDialogOnDismissListener(final Runnable negativeButtonRunnable,
                                                        final AlertDialogBuilder alertDialogBuilder,
                                                        AlertDialog serviceFailureDialog
                                                        ){
        serviceFailureDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if(negativeButtonRunnable!=null && alertDialogBuilder.isWasDialogClickedOutside()==true){
                    negativeButtonRunnable.run();
                }
            }
        });
    }
}
