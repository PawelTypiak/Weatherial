package paweltypiak.weatherial.dialogsInitializing.dialogInitializers.noEmailApplicationDialogInitializing;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;
import paweltypiak.weatherial.R;

class CopyToClipboardRunnable implements Runnable {

    private Activity activity;

    CopyToClipboardRunnable(Activity activity) {
        this.activity=activity;
    }

    public void run() {
        copyToClipboard(activity);
    }

    private static void copyToClipboard(Activity activity){
        ClipboardManager myClipboard;
        ClipData myClip;
        myClipboard = (ClipboardManager)activity.getSystemService(Context.CLIPBOARD_SERVICE);
        String text=activity.getString(R.string.author_email_address);
        myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);
        Toast.makeText(
                activity,
                activity.getString(R.string.feedback_dialog_clipboard_toast_message),
                Toast.LENGTH_SHORT).show();
    }
}
