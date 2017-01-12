package paweltypiak.matweather.mainActivityLayoutInitializing.appBarInitializing.appBarButtonsInitializing;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;

import paweltypiak.matweather.R;
import paweltypiak.matweather.usefulClasses.DialogInitializer;

public class YahooLogoButtonInitializer {

    public YahooLogoButtonInitializer(Activity activity, DialogInitializer dialogInitializer){
        setYahooLogoButtonOnClickListener(activity,dialogInitializer);
    }

    private void setYahooLogoButtonOnClickListener(Activity activity, final DialogInitializer dialogInitializer){
        LinearLayout yahooLayout=(LinearLayout) activity.findViewById(R.id.toolbar_layout_yahoo_logo_layout);
        yahooLayout.setOnClickListener(new View.OnClickListener() {
            AlertDialog yahooMainRedirectDialog=dialogInitializer.initializeYahooRedirectDialog(0,null);
            @Override
            public void onClick(View v) {
                yahooMainRedirectDialog.show();
            }
        });
    }
}
