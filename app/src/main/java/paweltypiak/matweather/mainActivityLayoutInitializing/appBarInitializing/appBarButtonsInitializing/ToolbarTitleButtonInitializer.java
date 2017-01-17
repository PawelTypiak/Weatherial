package paweltypiak.matweather.mainActivityLayoutInitializing.appBarInitializing.appBarButtonsInitializing;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;

import paweltypiak.matweather.R;
import paweltypiak.matweather.dialogsInitializing.MapsDialogInitializer;

public class ToolbarTitleButtonInitializer {

    public ToolbarTitleButtonInitializer(Activity activity){
        setToolbarTitleButtonOnClickListener(activity);
    }

    private void setToolbarTitleButtonOnClickListener(final Activity activity){
        View clickableView= activity.findViewById(R.id.toolbar_layout_title_clickable_view);
        clickableView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog mapsDialog= MapsDialogInitializer.initializeMapsDialog(activity);
                        mapsDialog.show();
                    }
                });
    }
}
