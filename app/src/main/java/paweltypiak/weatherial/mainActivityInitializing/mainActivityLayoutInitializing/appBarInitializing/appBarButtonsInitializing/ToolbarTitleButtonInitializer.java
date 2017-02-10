package paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.appBarInitializing.appBarButtonsInitializing;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.mapsDialogInitializing.MapsDialogInitializer;

class ToolbarTitleButtonInitializer {

    ToolbarTitleButtonInitializer(Activity activity){
        setToolbarTitleButtonOnClickListener(activity);
    }

    private void setToolbarTitleButtonOnClickListener(final Activity activity){
        View clickableView= activity.findViewById(R.id.toolbar_layout_title_clickable_view);
        clickableView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog mapsDialog= MapsDialogInitializer.getMapsDialog(activity);
                        mapsDialog.show();
                    }
                });
    }
}
