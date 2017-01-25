package paweltypiak.matweather.dialogsInitializing.alertDialogTools;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import paweltypiak.matweather.R;

public class AlertDialogButtonsCustomizer {

    public static void setDialogButtonDisabled(AlertDialog alertDialog, Context context){
        //disable alert dialog positive button
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
        }
    }

    public static void setDialogButtonEnabled(AlertDialog alertDialog, Context context){
        //enable alert dialog positive button
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
        }
    }

    public static void setDialogButtonsTextFont(Activity activity, AlertDialog alertDialog){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Typeface robotoMediumFontTypeface=Typeface.createFromAsset(activity.getAssets(), "Roboto-Medium.ttf");
            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTypeface(robotoMediumFontTypeface);
            alertDialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTypeface(robotoMediumFontTypeface);
            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTypeface(robotoMediumFontTypeface);
        }
    }
}
