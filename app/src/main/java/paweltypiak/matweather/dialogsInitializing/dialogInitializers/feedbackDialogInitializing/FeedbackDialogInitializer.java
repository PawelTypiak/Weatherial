package paweltypiak.matweather.dialogsInitializing.dialogInitializers.feedbackDialogInitializing;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import paweltypiak.matweather.R;
import paweltypiak.matweather.dialogsInitializing.AlertDialogBuilder;
import paweltypiak.matweather.dialogsInitializing.alertDialogTools.AlertDialogButtonsCustomizer;

public class FeedbackDialogInitializer {

    public static AlertDialog getFeedbackDialog(Activity activity){
        return initializeFeedbackDialog(activity);
    }

    private static AlertDialog initializeFeedbackDialog(Activity activity){
        View dialogView=initializeDialogView(activity);
        initializeMessageTextView(activity,dialogView);
        initializeEmailAddressTextView(activity,dialogView);
        AlertDialog feedbackDialog = buildAlertDialog(activity,dialogView);
        setAlertDialogOnShowListener(activity,feedbackDialog);
        return feedbackDialog;
    }

    private static View initializeDialogView(Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_two_line_text,null);
        return dialogView;
    }

    private static void initializeMessageTextView(Activity activity,
                                                   View dialogView){
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.two_line_text_dialog_message_text_1);
        messageTextView.setText(activity.getString(R.string.feedback_dialog_message));
    }

    private static void initializeEmailAddressTextView(Activity activity,
                                                       View dialogView){
        TextView emailAddressTextView=(TextView)dialogView.findViewById(R.id.two_line_text_dialog_message_text_2);
        emailAddressTextView.setText(activity.getString(R.string.feedback_dialog_mail));
        setSelectableBackground(activity,emailAddressTextView);
        setOnLongClickListener(activity,emailAddressTextView);
    }

    private static void setSelectableBackground(Activity activity,TextView emailAddressTextView){
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray typedArray = activity.obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        emailAddressTextView.setBackgroundResource(backgroundResource);
        typedArray.recycle();
    }

    private static void setOnLongClickListener(final Activity activity, TextView emailAddressTextView){
        emailAddressTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                copyToClipboard(activity);
                return true;
            }
        });
    }

    public static void copyToClipboard(Activity activity){
        ClipboardManager myClipboard;
        ClipData myClip;
        myClipboard = (ClipboardManager)activity.getSystemService(Context.CLIPBOARD_SERVICE);
        String text=activity.getString(R.string.mail_address);
        myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);
        Toast.makeText(activity, activity.getString(R.string.clipboard_toast_message),Toast.LENGTH_SHORT).show();
    }

    private static AlertDialog buildAlertDialog(Activity activity,View dialogView){
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder(
                activity,
                dialogView,
                R.style.DialogStyle,
                activity.getString(R.string.feedback_dialog_title),
                R.drawable.feedback_icon,
                null,
                false,
                activity.getString(R.string.feedback_dialog_positive_button),
                new EmailIntentRunnable(activity),
                null,
                null,
                activity.getString(R.string.feedback_dialog_negative_button),
                null);
        return alertDialogBuilder.getAlertDialog();
    }

    private static void setAlertDialogOnShowListener(final Activity activity, final AlertDialog feedbackDialog){
        feedbackDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                AlertDialogButtonsCustomizer.setDialogButtonsTextFont(activity,feedbackDialog);
            }
        });
    }
}
