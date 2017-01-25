package paweltypiak.matweather.dialogsInitializing.dialogInitializers.rateDialogInitializing;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import paweltypiak.matweather.R;
import paweltypiak.matweather.dialogsInitializing.AlertDialogBuilder;
import paweltypiak.matweather.dialogsInitializing.alertDialogTools.AlertDialogButtonsCustomizer;

public class RateDialogInitializer {

    public static AlertDialog getRateDialog(Activity activity){
        return initializeRateDialog(activity);
    }

    private static AlertDialog initializeRateDialog(Activity activity){
        View dialogView=initializeDialogView(activity);
        initializeMessageDialogView(activity,dialogView);
        AlertDialog rateDialog = buildAlertDialog(activity,dialogView);
        setAlertDialogOnShowListener(activity,rateDialog);
        return rateDialog;
    }

    private static View initializeDialogView(Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_one_line_text,null);
        return dialogView;
    }

    private static void initializeMessageDialogView(Activity activity,View dialogView){
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.one_line_text_dialog_message_text);
        messageTextView.setText(activity.getText(R.string.rate_dialog_message));
    }

    private static AlertDialog buildAlertDialog(Activity activity,View dialogView){
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder(
                activity,
                dialogView,
                R.style.DialogStyle,
                activity.getString(R.string.rate_dialog_title),
                R.drawable.rate_icon,
                null,
                false,
                activity.getString(R.string.rate_dialog_positive_button),
                new RateAppRunnable(activity),
                null,
                null,
                activity.getString(R.string.rate_dialog_negative_button),
                null
        );
        return alertDialogBuilder.getAlertDialog();
    }

    private static void setAlertDialogOnShowListener(final Activity activity, final AlertDialog rateDialog){
        rateDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                AlertDialogButtonsCustomizer.setDialogButtonsTextFont(activity,rateDialog);
            }
        });
    }
}
