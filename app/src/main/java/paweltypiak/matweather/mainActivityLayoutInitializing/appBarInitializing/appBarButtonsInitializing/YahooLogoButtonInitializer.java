package paweltypiak.matweather.mainActivityLayoutInitializing.appBarInitializing.appBarButtonsInitializing;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;

import paweltypiak.matweather.R;
import paweltypiak.matweather.dialogsInitializing.YahooRedirectDialogInitializer;

public class YahooLogoButtonInitializer {

    public YahooLogoButtonInitializer(Activity activity){
        setYahooLogoButtonOnClickListener(activity);
    }

    private void setYahooLogoButtonOnClickListener(final Activity activity){
        LinearLayout yahooLayout=(LinearLayout) activity.findViewById(R.id.toolbar_layout_yahoo_logo_layout);
        yahooLayout.setOnClickListener(new View.OnClickListener() {
            AlertDialog yahooMainRedirectDialog= YahooRedirectDialogInitializer.buildYahooRedirectDialog(activity,0,null);
            @Override
            public void onClick(View v) {
                yahooMainRedirectDialog.show();
            }
        });
    }
}
