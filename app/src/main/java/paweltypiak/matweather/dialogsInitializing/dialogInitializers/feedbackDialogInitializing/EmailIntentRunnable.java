package paweltypiak.matweather.dialogsInitializing.dialogInitializers.feedbackDialogInitializing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import paweltypiak.matweather.R;
import paweltypiak.matweather.dialogsInitializing.dialogInitializers.noEmailApplicationDialogInitializing.NoEmailApplicationDialogInitializer;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

class EmailIntentRunnable implements Runnable {

    private Activity activity;

    EmailIntentRunnable(Activity activity) {
        this.activity=activity;
    }

    public void run() {
        initializeEmailIntent(activity);
    }

    public static void initializeEmailIntent(Activity activity){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/plain");
        String subject=activity.getString(R.string.clipboard_feedback_mail_subject);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        String address=activity.getString(R.string.mail_address);
        intent.setData(Uri.parse("mailto:"+address));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            activity.startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            AlertDialog dialog=NoEmailApplicationDialogInitializer.getNoEmailApplicationDialog(activity);
            dialog.show();
        }
    }
}
