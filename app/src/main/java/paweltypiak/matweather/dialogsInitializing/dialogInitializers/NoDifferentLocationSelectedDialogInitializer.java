package paweltypiak.matweather.dialogsInitializing.dialogInitializers;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import paweltypiak.matweather.R;
import paweltypiak.matweather.dialogsInitializing.AlertDialogBuilder;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

public class NoDifferentLocationSelectedDialogInitializer {

    public static AlertDialog getNoDifferentLocationSelectedDialog(Activity activity,
                                                                    AlertDialog searchDialog){
        return initializeNoDifferentLocationSelectedDialog(
                activity,
                searchDialog
        );
    }

    private static AlertDialog initializeNoDifferentLocationSelectedDialog(Activity activity,
                                                                          AlertDialog searchDialog){
        View dialogView=initializeDialogView(activity);
        initializeMessageTextView(activity,dialogView);
        AlertDialog noDifferentLocationSelectedDialog =buildAlertDialog(
                activity,
                dialogView,
                searchDialog
        );
        return noDifferentLocationSelectedDialog;
    }

    private static View initializeDialogView(Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_one_line_text,null);
        return dialogView;
    }

    private static void initializeMessageTextView(Activity activity,View dialogView){
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.one_line_text_dialog_message_text);
        messageTextView.setText(activity.getString(R.string.no_different_location_selected_dialog_message));
    }

    private static AlertDialog buildAlertDialog(Activity activity,
                                                View dialogView,
                                                AlertDialog searchDialog){
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder(
                activity,
                dialogView,
                R.style.DialogStyle,
                activity.getString(R.string.failure_dialog_title),
                R.drawable.dialog_warning_icon,
                null,
                false,
                activity.getString(R.string.no_different_location_selected_dialog_positive_button),
                new showDifferentLocationDialogRunnable(activity,searchDialog),
                null,
                null,
                activity.getString(R.string.no_different_location_selected_dialog_negative_button),
                null);
        return alertDialogBuilder.getAlertDialog();
    }

    private static class showDifferentLocationDialogRunnable implements Runnable{

        private Activity activity;
        private AlertDialog searchDialog;

        public showDifferentLocationDialogRunnable(Activity activity, AlertDialog searchDialog){
            this.searchDialog=searchDialog;
            this.activity=activity;
        }

        @Override
        public void run() {
            searchDialog.show();
            //UsefulFunctions.showKeyboard(activity);
        }
    }
}
