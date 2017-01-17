package paweltypiak.matweather.dialogsInitializing;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import paweltypiak.matweather.R;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

public class NoEmailApplicationDialogInitializer {

    public static AlertDialog initializeNoEmailApplicationDialog(Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_one_line_text,null);
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.one_line_text_dialog_message_text);
        messageTextView.setText(activity.getString(R.string.no_email_application_dialog_message));
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder( activity,
                dialogView,R.style.DialogStyle,
                activity.getString(R.string.no_email_application_dialog_title),
                R.drawable.dialog_error_icon,
                null,
                false,
                activity.getString(R.string.no_email_application_dialog_positive_button),
                new copyToClipboardRunnable(activity,activity.getString(R.string.mail_address)),
                null,
                null,
                activity.getString( R.string.no_email_application_dialog_negative_button),
                null);
        AlertDialog noEmailApplicationDialog = alertDialogBuilder.getAlertDialog();
        return noEmailApplicationDialog;
    }

    private static class copyToClipboardRunnable implements Runnable {

        private Activity activity;
        private String text;

        public copyToClipboardRunnable(Activity activity,String text) {
            this.activity=activity;
            this.text=text;
        }

        public void run() {
            UsefulFunctions.copyToClipboard(activity,text);
        }
    }

}
