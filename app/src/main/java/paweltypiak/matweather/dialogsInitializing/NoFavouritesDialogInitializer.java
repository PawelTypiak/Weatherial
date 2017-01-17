package paweltypiak.matweather.dialogsInitializing;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import paweltypiak.matweather.R;

public class NoFavouritesDialogInitializer {

    public static AlertDialog initializeNoFavouritesDialog(Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_one_line_text,null);
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.one_line_text_dialog_message_text);
        messageTextView.setText(activity.getString(R.string.no_favourites_dialog_message));
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder( activity,
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
        AlertDialog noFavouritesDialog = alertDialogBuilder.getAlertDialog();
        return noFavouritesDialog;
    }
}
