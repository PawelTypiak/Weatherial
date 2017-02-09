package paweltypiak.weatherial.dialogsInitializing.dialogInitializers.exitDialogInitializing;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import paweltypiak.weatherial.R;
import paweltypiak.weatherial.dialogsInitializing.AlertDialogBuilder;
import paweltypiak.weatherial.dialogsInitializing.alertDialogTools.AlertDialogButtonsCustomizer;

public class ExitDialogInitializer {

    public static AlertDialog getExitDialog(Activity activity,
                                             int type,
                                             Runnable negativeButtonRunnable){
        return initializeExitDialog(activity,type,negativeButtonRunnable);
    }

    private static AlertDialog initializeExitDialog(Activity activity,
                                                    int type,
                                                    Runnable negativeButtonRunnable){
        View dialogView=initializeDialogView(activity);
        initializeMessageTextView(activity,dialogView);
        AlertDialog exitDialog = buildAlertDialog(
                activity,
                type,
                negativeButtonRunnable,
                dialogView);
        setAlertDialogOnShowListener(activity,exitDialog);
        return exitDialog;
    }

    private static View initializeDialogView(Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_one_line_text,null);
        return dialogView;
    }

    private static void initializeMessageTextView(Activity activity, View dialogView){
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.one_line_text_dialog_message_text);
        messageTextView.setText(activity.getText(R.string.exit_dialog_message));
    }

    private static AlertDialog buildAlertDialog(Activity activity,
                                                int type,
                                                Runnable negativeButtonRunnable,
                                                View dialogView){
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder(
                activity,
                dialogView,
                R.style.DialogStyle,
                activity.getString(R.string.exit_dialog_title),
                R.drawable.warning_icon,
                null,
                isUncancelable(type),
                activity.getString(R.string.exit_dialog_positive_button),
                new FinishRunnable(activity),
                null,
                null,
                activity.getString(R.string.exit_dialog_negative_button),
                negativeButtonRunnable);
        return alertDialogBuilder.getAlertDialog();
    }

    private static boolean isUncancelable(int type){
        boolean isUncancelable=false;
        if(type==0) isUncancelable=true;
        else if(type==1) isUncancelable=false;
        return isUncancelable;
    }

    private static void setAlertDialogOnShowListener(final Activity activity, final AlertDialog exitDialog){
        exitDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                AlertDialogButtonsCustomizer.setDialogButtonsTextFont(activity,exitDialog);
            }
        });
    }
}
