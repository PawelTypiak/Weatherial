package paweltypiak.weatherial.dialogsInitializing.dialogInitializers;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.dialogsInitializing.AlertDialogBuilder;
import paweltypiak.weatherial.dialogsInitializing.alertDialogTools.AlertDialogButtonsCustomizer;

public class NoFavouritesAvailableDialogInitializer {

    public static AlertDialog getNoFavouritesAvailableDialog(Activity activity){
        return initializeNoFavouritesDialog(activity);
    }

    private static AlertDialog initializeNoFavouritesDialog(Activity activity){
        View dialogView=initializeDialogView(activity);
        initializeMessageTextView(activity,dialogView);
        AlertDialog noFavouritesAvailableDialog = buildDialog(activity,dialogView);
        setAlertDialogOnShowListener(activity,noFavouritesAvailableDialog);
        return noFavouritesAvailableDialog;
    }

    private static View initializeDialogView(Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_one_line_text,null);
        return dialogView;
    }

    private static void initializeMessageTextView(Activity activity, View dialogView){
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.one_line_text_dialog_message_text);
        messageTextView.setText(activity.getString(R.string.no_favourites_dialog_message));
    }

    private static AlertDialog buildDialog(Activity activity,View dialogView){
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder(
                activity,
                dialogView,
                R.style.DialogStyle,
                activity.getString(R.string.no_favourites_available_dialog_title),
                R.drawable.info_icon,
                null,
                true,
                activity.getString(R.string.no_favourites_available_dialog_positive_button),
                null,
                null,
                null,
                null,
                null);
        return alertDialogBuilder.getAlertDialog();
    }

    private static void setAlertDialogOnShowListener(final Activity activity,
                                                     final AlertDialog noFavouritesAvailableDialog){
        noFavouritesAvailableDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                AlertDialogButtonsCustomizer.setDialogButtonsTextFont(activity,noFavouritesAvailableDialog);
            }
        });
    }
}
