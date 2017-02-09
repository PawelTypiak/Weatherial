package paweltypiak.weatherial.infoActivityInitializing.infoActivityNestedScrollViewInitializing;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;
import paweltypiak.weatherial.R;

public class InfoActivityNestedScrollViewInitializer {

    public InfoActivityNestedScrollViewInitializer(Activity activity){
        initializeApplicationSourceSection(activity);
        initializeApiSection(activity);
        initializeExternalLibrariesSection(activity);
    }

    private void initializeApplicationSourceSection(Activity activity){
        new ApplicationSourceSectionInitializer(activity,this);
    }

    private void initializeApiSection(Activity activity){
        new ApiSectionInitializer(activity,this);
    }

    private void initializeExternalLibrariesSection(Activity activity){
        new ExternalLibrariesSectionInitializer(activity,this);
    }

    void initializeWebIntent(Activity activity,String url){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        activity.startActivity(intent);
    }

    public void copyAddressToClipboard(Activity activity,String copiedText){
        ClipboardManager myClipboard;
        ClipData myClip;
        myClipboard = (ClipboardManager)activity.getSystemService(Context.CLIPBOARD_SERVICE);
        myClip = ClipData.newPlainText("text", copiedText);
        myClipboard.setPrimaryClip(myClip);
        Toast.makeText(
                activity,
                activity.getString(R.string.info_activity_clipboard_toast_message),
                Toast.LENGTH_SHORT).show();
    }
}
