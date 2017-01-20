package paweltypiak.matweather.dialogsInitializing.dialogInitializers;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import paweltypiak.matweather.R;
import paweltypiak.matweather.dialogsInitializing.AlertDialogBuilder;

public class NoFavouritesDialogInitializer {

    public static AlertDialog getNoFavouritesDialog(Activity activity){
        return initializeNoFavouritesDialog(activity);
    }

    private static AlertDialog initializeNoFavouritesDialog(Activity activity){
        View dialogView=initializeDialogView(activity);
        initializeMessageTextView(activity,dialogView);
        AlertDialog noFavouritesDialog = buildDialog(activity,dialogView);
        return noFavouritesDialog;
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
                activity.getString(R.string.no_favourites_dialog_title),
                R.drawable.dialog_info_icon,
                null,
                true,
                activity.getString(R.string.no_favourites_dialog_positive_button),
                null,
                null,
                null,
                null,
                null);
        return alertDialogBuilder.getAlertDialog();
    }
}
