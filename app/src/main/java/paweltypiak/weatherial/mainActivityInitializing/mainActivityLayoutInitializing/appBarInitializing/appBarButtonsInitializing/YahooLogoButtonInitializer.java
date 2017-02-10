package paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.appBarInitializing.appBarButtonsInitializing;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.yahooRedirectDialogInitializing.YahooRedirectDialogInitializer;

class YahooLogoButtonInitializer {

    YahooLogoButtonInitializer(Activity activity){
        setYahooLogoButtonOnClickListener(activity);
    }

    private void setYahooLogoButtonOnClickListener(final Activity activity){
        LinearLayout yahooLayout=(LinearLayout) activity.findViewById(R.id.toolbar_layout_yahoo_logo_layout);
        yahooLayout.setOnClickListener(new View.OnClickListener() {
            AlertDialog yahooMainRedirectDialog= YahooRedirectDialogInitializer.getYahooRedirectDialog(activity,0,null);
            @Override
            public void onClick(View v) {
                yahooMainRedirectDialog.show();
            }
        });
    }
}
