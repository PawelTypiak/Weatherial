package paweltypiak.matweather.infoActivityInitializing.infoActivityNestedScrollViewInitializing;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.widget.Space;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import me.henrytao.smoothappbarlayout.SmoothAppBarLayout;
import paweltypiak.matweather.R;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

public class InfoActivityNestedScrollViewInitializer {

    public InfoActivityNestedScrollViewInitializer(Activity activity){
        setTopPaddingSpace(activity);
        initializeApplicationSourceSection(activity);
        initializeApiSection(activity);
        initializeExternalLibrariesSection(activity);
    }

    private void setTopPaddingSpace(final Activity activity){
        final SmoothAppBarLayout appBarLayout
                =(SmoothAppBarLayout)activity.findViewById(R.id.info_activity_app_bar);
        ViewTreeObserver observer = appBarLayout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int appBarHeight=appBarLayout.getHeight();
                Space topPaddingSpace
                        =(Space)activity.findViewById(R.id.info_activity_nested_scrollview_top_padding_space);
                LinearLayout.LayoutParams topPaddingSpaceParams
                        = (LinearLayout.LayoutParams)topPaddingSpace.getLayoutParams();
                topPaddingSpaceParams.height = appBarHeight;
                topPaddingSpace.setLayoutParams(topPaddingSpaceParams);
            }
        });
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

    void setWebIcon(Activity activity,ImageView imageView){
        Drawable webIconDrawable
                = UsefulFunctions.getColoredDrawable(activity,R.drawable.author_icon,R.color.colorPrimary);
        imageView.setImageDrawable(webIconDrawable);
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
