package paweltypiak.matweather.dialogsInitializing.dialogInitializers.noEmailApplicationDialogInitializing;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;
import paweltypiak.matweather.R;

class CopyToClipboardRunnable implements Runnable {

    private Activity activity;

    public CopyToClipboardRunnable(Activity activity) {
        this.activity=activity;
    }

    public void run() {
        copyToClipboard(activity);
    }

    public static void copyToClipboard(Activity activity){
        ClipboardManager myClipboard;
        ClipData myClip;
        myClipboard = (ClipboardManager)activity.getSystemService(Context.CLIPBOARD_SERVICE);
        String text=activity.getString(R.string.mail_address);
        myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);
        Toast.makeText(
                activity,
                activity.getString(R.string.clipboard_toast_message),
                Toast.LENGTH_SHORT).show();
    }
}
