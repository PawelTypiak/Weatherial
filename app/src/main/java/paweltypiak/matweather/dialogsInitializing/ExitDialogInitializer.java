package paweltypiak.matweather.dialogsInitializing;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import paweltypiak.matweather.R;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

public class ExitDialogInitializer {

    public static AlertDialog initializeExitDialog(Activity activity,
                                            int type,
                                            Runnable negativeButtonRunnable){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_one_line_text,null);
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.one_line_text_dialog_message_text);
        messageTextView.setText(activity.getText(R.string.exit_dialog_message));
        boolean isUncancelable=false;
        if(type==0) isUncancelable=true;
        else if(type==1) isUncancelable=false;
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder(activity,
                dialogView,
                R.style.DialogStyle,
                activity.getString(R.string.exit_dialog_title),
                R.drawable.dialog_warning_icon,
                null,
                isUncancelable,
                activity.getString(R.string.exit_dialog_positive_button),
                new FinishRunnable(activity),
                null,
                null,
                activity.getString(R.string.exit_dialog_negative_button),
                negativeButtonRunnable);
        AlertDialog exitDialog = alertDialogBuilder.getAlertDialog();
        return exitDialog;
    }

    private static class FinishRunnable implements Runnable{

        private Activity activity;

        public FinishRunnable(Activity activity){
            this.activity=activity;
        }

        @Override
        public void run() {
            activity.finish();
            return;
        }
    }
}
