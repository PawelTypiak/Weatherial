package paweltypiak.weatherial.dialogsInitializing.dialogInitializers;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.dialogsInitializing.AlertDialogBuilder;
import paweltypiak.weatherial.dialogsInitializing.alertDialogTools.AlertDialogButtonsCustomizer;

public class InternetFailureDialogInitializer {

    public static AlertDialog getInternetFailureDialog(Activity activity,
                                                       int type,
                                                       Runnable positiveButtonRunnable,
                                                       Runnable negativeButtonRunnable) {
        return initializeInternetFailureDialog(activity,type,positiveButtonRunnable,negativeButtonRunnable);
    }

    private static AlertDialog initializeInternetFailureDialog(Activity activity,
                                                               int type,
                                                               Runnable positiveButtonRunnable,
                                                               Runnable negativeButtonRunnable){
        View dialogView=initializeDialogView(activity);
        initializeMessageTextView(activity,dialogView);
        AlertDialogBuilder alertDialogBuilder=getAlertDialogBuilder(
                activity,
                type,
                positiveButtonRunnable,
                negativeButtonRunnable,
                dialogView
        );
        AlertDialog internetFailureDialog = alertDialogBuilder.getAlertDialog();
        setAlertDialogOnShowListener(
                activity,
                alertDialogBuilder,
                internetFailureDialog);
        setAlertDialogOnDismissListener(
                type,
                negativeButtonRunnable,
                alertDialogBuilder,
                internetFailureDialog);
        return internetFailureDialog;
    }

    private static View initializeDialogView(Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_one_line_text,null);
        return dialogView;
    }

    private static void initializeMessageTextView(Activity activity,View dialogView){
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.one_line_text_dialog_message_text);
        messageTextView.setText(activity.getString(R.string.internet_failure_dialog_message));
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
                R.drawable.error_icon,
                null,
                isUncancelable(type),
                activity.getString(R.string.internet_failure_dialog_positive_button),
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
            negativeButtonText=activity.getString(R.string.internet_failure_dialog_negative_button_type_0);
        }
        else if(type==1){
            negativeButtonText=activity.getString(R.string.internet_failure_dialog_negative_button_type_1);
        }
        return negativeButtonText;
    }

    private static void setAlertDialogOnShowListener(final Activity activity,
                                                     final AlertDialogBuilder alertDialogBuilder,
                                                     final AlertDialog internetFailureDialog){
        internetFailureDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                AlertDialogButtonsCustomizer.setDialogButtonsTextFont(activity,internetFailureDialog);
                alertDialogBuilder.setWasDialogClickedOutside();
            }
        });
    }

    private static void setAlertDialogOnDismissListener(int type,
                                                 final Runnable negativeButtonRunnable,
                                                 final AlertDialogBuilder alertDialogBuilder,
                                                 AlertDialog internetFailureDialog){
        if(type==1){
            internetFailureDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    if(negativeButtonRunnable!=null && alertDialogBuilder.getWasDialogClickedOutside()==true){
                        negativeButtonRunnable.run();
                    }
                }
            });
        }
    }
}
