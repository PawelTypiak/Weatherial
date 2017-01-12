package paweltypiak.matweather.mainActivityLayoutInitializing.appBarInitializing.appBarButtonsInitializing;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;

import paweltypiak.matweather.R;
import paweltypiak.matweather.usefulClasses.DialogInitializer;

public class ToolbarTitleButtonInitializer {

    public ToolbarTitleButtonInitializer(Activity activity, DialogInitializer dialogInitializer){
        setToolbarTitleButtonOnClickListener(activity,dialogInitializer);
    }

    private void setToolbarTitleButtonOnClickListener(final Activity activity,final DialogInitializer dialogInitializer){
        View clickableView= activity.findViewById(R.id.toolbar_layout_title_clickable_view);
        clickableView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog mapsDialog= dialogInitializer.initializeMapsDialog(activity);
                        mapsDialog.show();
                    }
                });
    }
}
