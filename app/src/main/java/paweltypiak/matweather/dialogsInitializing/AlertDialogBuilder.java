package paweltypiak.matweather.dialogsInitializing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;

public class AlertDialogBuilder {

    private boolean wasDialogClickedOutside;
    private AlertDialog alertDialog;

    public AlertDialogBuilder (Activity activity,
                               View dialogView,
                               int theme,
                               String title,
                               int iconResource,
                               String message,
                               boolean ifUncancelable,
                               String positiveButtonText,
                               final Runnable positiveButtonRunnable,
                               String neutralButtonText,
                               final Runnable neutralButtonRunnable,
                               String negativeButtonText,
                               final Runnable negativeButtonRunnable){
        alertDialog=buildDialog(activity,
                dialogView,
                theme,
                title,
                iconResource,
                message,
                ifUncancelable,
                positiveButtonText,
                positiveButtonRunnable,
                neutralButtonText,
                neutralButtonRunnable,
                negativeButtonText,
                negativeButtonRunnable);
    }

    private AlertDialog buildDialog(Activity activity,
                                   View dialogView,
                                   int theme,
                                   String title,
                                   int iconResource,
                                   String message,
                                   boolean ifUncancelable,
                                   String positiveButtonText,
                                   final Runnable positiveButtonRunnable,
                                   String neutralButtonText,
                                   final Runnable neutralButtonRunnable,
                                   String negativeButtonText,
                                   final Runnable negativeButtonRunnable){
        //custom dialog builder
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity, theme);
        setDialogView(alertBuilder,dialogView);
        setDialogTitle(alertBuilder,title);
        setDialogIcon(alertBuilder,iconResource);
        setDialogMessage(alertBuilder,message);
        setDialogCancelable(alertBuilder,ifUncancelable);
        setDialogPositiveButton(alertBuilder,positiveButtonText,positiveButtonRunnable);
        setDialogNeutralButton(alertBuilder,neutralButtonText,neutralButtonRunnable);
        setDialogNegativeButton(alertBuilder,negativeButtonText,negativeButtonRunnable);
        AlertDialog alertDialog = alertBuilder.create();
        setDialogOnShowListener(alertDialog);
        return alertDialog;
    }

    private void setDialogView(AlertDialog.Builder alertBuilder, View dialogView){
        if(dialogView!=null)alertBuilder.setView(dialogView);
    }

    private void setDialogTitle(AlertDialog.Builder alertBuilder, String title){
        if(title!=null) alertBuilder.setTitle(title);
    }

    private void setDialogIcon(AlertDialog.Builder alertBuilder,int iconResource){
        if(iconResource!=0)  alertBuilder.setIcon(iconResource);
    }

    private void setDialogMessage(AlertDialog.Builder alertBuilder, String message){
        if(message!=null) alertBuilder.setMessage(message);
    }

    private void setDialogCancelable(AlertDialog.Builder alertBuilder, boolean ifUncancelable){
        if(ifUncancelable==true) alertBuilder.setCancelable(false);
    }

    private void setDialogPositiveButton(AlertDialog.Builder alertBuilder,
                                         String positiveButtonText,
                                         final Runnable positiveButtonRunnable){
        if(positiveButtonText!=null) {
            alertBuilder.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if(positiveButtonRunnable!=null) {
                        positiveButtonRunnable.run();
                        wasDialogClickedOutside =false;
                    }
                }
            });
        }
    }

    private void setDialogNeutralButton(AlertDialog.Builder alertBuilder,
                                        String neutralButtonText,
                                        final Runnable neutralButtonRunnable){
        if(neutralButtonText!=null) {
            alertBuilder.setNeutralButton(neutralButtonText, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if(neutralButtonRunnable!=null) {
                        neutralButtonRunnable.run();
                        wasDialogClickedOutside =false;
                    }
                }
            });
        }
    }

    private void setDialogNegativeButton(AlertDialog.Builder alertBuilder,
                                         String negativeButtonText,
                                         final Runnable negativeButtonRunnable){
        if(negativeButtonText!=null) {
            alertBuilder.setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if(negativeButtonRunnable!=null) {
                        negativeButtonRunnable.run();
                        wasDialogClickedOutside =false;
                    }
                }
            });
        }
    }

    private void setDialogOnShowListener(AlertDialog alertDialog){
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                wasDialogClickedOutside =true;
            }
        });
    }

    public boolean isWasDialogClickedOutside() {
        return wasDialogClickedOutside;
    }

    public AlertDialog getAlertDialog() {
        return alertDialog;
    }
}
