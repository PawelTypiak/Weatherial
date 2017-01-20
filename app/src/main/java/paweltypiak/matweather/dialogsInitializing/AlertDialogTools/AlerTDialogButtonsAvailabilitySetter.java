package paweltypiak.matweather.dialogsInitializing.AlertDialogTools;

import android.app.AlertDialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import paweltypiak.matweather.R;

public class AlertDialogButtonsAvailabilitySetter {

    public static void setDialogButtonDisabled(AlertDialog alertDialog, Context context){
        //disable alert dialog positive button
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
    }

    public static void setDialogButtonEnabled(AlertDialog alertDialog, Context context){
        //enable alert dialog positive button
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
    }
}
