package paweltypiak.matweather.dialogsInitializing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import paweltypiak.matweather.R;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

public class FeedbackDialogInitializer {

    public static AlertDialog initializeFeedbackDialog(final Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_two_line_text,null);
        TextView message1TextView=(TextView)dialogView.findViewById(R.id.two_line_text_dialog_message1_text);
        message1TextView.setText(activity.getString(R.string.feedback_dialog_message));
        TextView message2TextView=(TextView)dialogView.findViewById(R.id.two_line_text_dialog_message2_text);
        message2TextView.setText(activity.getString(R.string.feedback_dialog_mail));
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray typedArray = activity.obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        message2TextView.setBackgroundResource(backgroundResource);
        typedArray.recycle();
        message2TextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                UsefulFunctions.copyToClipboard(activity,activity.getString(R.string.mail_address));
                return true;
            }
        });
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder(activity,
                dialogView,
                R.style.DialogStyle,
                activity.getString(R.string.feedback_dialog_title),
                R.drawable.dialog_feedback_icon,
                null,
                false,
                activity.getString(R.string.feedback_dialog_positive_button),
                new initializeEmailIntentRunnable(activity,
                        activity.getString(R.string.mail_address),
                        activity.getString(R.string.clipboard_mail_feedback_title),
                        null,
                        NoEmailApplicationDialogInitializer.initializeNoEmailApplicationDialog(activity)
                ),
                null,
                null,
                activity.getString(R.string.feedback_dialog_negative_button),
                null);
        AlertDialog feedbackDialog = alertDialogBuilder.getAlertDialog();
        return feedbackDialog;
    }

    private static class initializeEmailIntentRunnable implements Runnable {

        private Activity activity;
        private String address;
        private String subject;
        private String body;
        private AlertDialog dialog;

        public initializeEmailIntentRunnable(Activity activity,
                                             String address,
                                             String subject,
                                             String body,
                                             AlertDialog dialog) {
            this.activity=activity;
            this.dialog=dialog;
            this.address=address;
            this.subject=subject;
            this.body=body;
        }

        public void run() {
            UsefulFunctions.initializeEmailIntent(activity,address,subject,body,dialog);
        }
    }
}
