package paweltypiak.matweather.dialogsInitializing.dialogInitializers;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import paweltypiak.matweather.R;
import paweltypiak.matweather.dialogsInitializing.AlertDialogBuilder;

public class ProgressDialogInitializer {

    public static AlertDialog getProgressDialog(Activity activity,
                                                String message){
        return initializeProgressDialog(activity,message);
    }

    private static AlertDialog initializeProgressDialog(Activity activity,
                                         String message){
        View dialogView = initializeDialogView(activity);
        initializeMessageTextView(message,dialogView);
        AlertDialog progressDialog =buildAlertDialog(activity,dialogView);
        return progressDialog;
    }

    private static View initializeDialogView(Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_progress,null);
        return dialogView;
    }

    private static void initializeMessageTextView(String message, View dialogView){
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.progress_dialog_message_text);
        messageTextView.setText(message);
    }

    private static AlertDialog buildAlertDialog(Activity activity,View dialogView){
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder(
                activity,
                dialogView,
                R.style.DialogStyle,
                null,
                0,
                null,
                true,
                null,
                null,
                null,
                null,
                null,
                null);
        return alertDialogBuilder.getAlertDialog();
    }
}
