package paweltypiak.matweather.dialogsInitializing;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import paweltypiak.matweather.R;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

public class YahooRedirectDialogInitializer {

    public static AlertDialog buildYahooRedirectDialog(Activity activity,
                                                     int type,
                                                     String link){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_two_line_text,null);
        TextView message1TextView=(TextView)dialogView.findViewById(R.id.two_line_text_dialog_message1_text);
        message1TextView.setText(activity.getString(R.string.yahoo_redirect_dialog_message));
        TextView message2TextView=(TextView)dialogView.findViewById(R.id.two_line_text_dialog_message2_text);
        String message=null;
        Runnable positiveButtonRunnable=null;
        if(type==0){
            message=activity.getString(R.string.yahoo_main_redirect_dialog_message_service_name);
            positiveButtonRunnable=new initializeWebIntentRunnable(activity,activity.getString(R.string.yahoo_address));
        }
        else if(type==1){
            message=activity.getString(R.string.yahoo_weather_redirect_dialog_message_service_name);
            positiveButtonRunnable=new initializeWebIntentRunnable(activity,link);
        }
        message2TextView.setText(message);
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder(activity,
                dialogView,
                R.style.DialogStyle,
                activity.getString(R.string.yahoo_redirect_dialog_title),
                R.drawable.dialog_info_icon,
                null,
                false,
                activity.getString(R.string.yahoo_redirect_dialog_positive_button),
                positiveButtonRunnable,
                null,
                null,
                activity.getString(R.string.yahoo_redirect_dialog_negative_button),
                null);
        AlertDialog yahooRedirectDialog = alertDialogBuilder.getAlertDialog();
        return  yahooRedirectDialog;
    }

    private static class initializeWebIntentRunnable implements Runnable {

        private Activity activity;
        private String url;

        public initializeWebIntentRunnable(Activity activity,String url) {
            this.activity=activity;
            this.url=url;
        }

        public void run() {
            UsefulFunctions.initializeWebIntent(activity,url);
        }
    }

}
