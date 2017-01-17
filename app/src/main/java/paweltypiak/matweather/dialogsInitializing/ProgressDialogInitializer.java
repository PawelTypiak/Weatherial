package paweltypiak.matweather.dialogsInitializing;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import paweltypiak.matweather.R;

public class ProgressDialogInitializer {

    public static AlertDialog initializeProgressDialog(Activity activity,
                                         String message){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_progress,null);
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.progress_dialog_message_text);
        messageTextView.setText(message);
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder(activity,
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
        AlertDialog progressDialog =alertDialogBuilder.getAlertDialog();
        return progressDialog;
    }
}
