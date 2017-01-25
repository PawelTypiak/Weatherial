package paweltypiak.matweather.dialogsInitializing.dialogInitializers.noDifferentLocationSelectedDialogInitializing;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import paweltypiak.matweather.R;
import paweltypiak.matweather.dialogsInitializing.AlertDialogBuilder;
import paweltypiak.matweather.dialogsInitializing.alertDialogTools.AlertDialogButtonsCustomizer;

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
        setAlertDialogOnShowListener(activity,noDifferentLocationSelectedDialog);
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
                R.drawable.warning_icon,
                null,
                false,
                activity.getString(R.string.no_different_location_selected_dialog_positive_button),
                new showDifferentLocationDialogRunnable(searchDialog),
                null,
                null,
                activity.getString(R.string.no_different_location_selected_dialog_negative_button),
                null);
        return alertDialogBuilder.getAlertDialog();
    }

    private static void setAlertDialogOnShowListener(final Activity activity,
                                                     final AlertDialog noDifferentLocationSelectedDialog){
        noDifferentLocationSelectedDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                AlertDialogButtonsCustomizer.setDialogButtonsTextFont(activity,noDifferentLocationSelectedDialog);
            }
        });
    }
}
