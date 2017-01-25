package paweltypiak.matweather.dialogsInitializing.dialogInitializers.noEmailApplicationDialogInitializing;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import paweltypiak.matweather.R;
import paweltypiak.matweather.dialogsInitializing.AlertDialogBuilder;
import paweltypiak.matweather.dialogsInitializing.alertDialogTools.AlertDialogButtonsCustomizer;

public class NoEmailApplicationDialogInitializer {

    public static AlertDialog getNoEmailApplicationDialog(Activity activity){
        return initializeNoEmailApplicationDialog(activity);
    }

    private static AlertDialog initializeNoEmailApplicationDialog(Activity activity){
        View dialogView=initializeDialogView(activity);
        initializeMessageTextView(activity,dialogView);
        AlertDialog noEmailApplicationDialog = buildDialogView(activity,dialogView);
        setAlertDialogOnShowListener(activity,noEmailApplicationDialog);
        return noEmailApplicationDialog;
    }

    private static View initializeDialogView(Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_one_line_text,null);
        return dialogView;
    }

    private static void initializeMessageTextView(Activity activity,View dialogView){
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.one_line_text_dialog_message_text);
        messageTextView.setText(activity.getString(R.string.no_email_application_dialog_message));
    }

    private static AlertDialog buildDialogView(Activity activity,View dialogView){
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder(
                activity,
                dialogView,R.style.DialogStyle,
                activity.getString(R.string.no_email_application_dialog_title),
                R.drawable.error_icon,
                null,
                false,
                activity.getString(R.string.no_email_application_dialog_positive_button),
                new CopyToClipboardRunnable(activity),
                null,
                null,
                activity.getString( R.string.no_email_application_dialog_negative_button),
                null);
        return alertDialogBuilder.getAlertDialog();
    }

    private static void setAlertDialogOnShowListener(final Activity activity, final AlertDialog noEmailApplicationDialog){
        noEmailApplicationDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                AlertDialogButtonsCustomizer.setDialogButtonsTextFont(activity,noEmailApplicationDialog);
            }
        });
    }
}
