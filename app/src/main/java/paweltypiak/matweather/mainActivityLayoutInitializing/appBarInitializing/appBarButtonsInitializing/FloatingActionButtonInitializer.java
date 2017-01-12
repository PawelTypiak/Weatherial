package paweltypiak.matweather.mainActivityLayoutInitializing.appBarInitializing.appBarButtonsInitializing;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import paweltypiak.matweather.R;
import paweltypiak.matweather.usefulClasses.DialogInitializer;

public class FloatingActionButtonInitializer {

    private FloatingActionButton floatingActionButton;
    private int floatingActionButtonOnClickIndicator=0;

    public FloatingActionButtonInitializer(Activity activity,
                                           DialogInitializer dialogInitializer){
        setFloatingActionButtonOnClickListener(
                activity,
                dialogInitializer);
    }

    private void setFloatingActionButtonOnClickListener(Activity activity,
                                                        final DialogInitializer dialogInitializer){
        floatingActionButton =(FloatingActionButton) activity.findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(floatingActionButtonOnClickIndicator ==0){
                    //if location is not in favourites
                    AlertDialog addToFavouritesDialog = dialogInitializer.initializeAddToFavouritesDialog();
                    addToFavouritesDialog.show();
                }
                else if(floatingActionButtonOnClickIndicator ==1){
                    //if location is in favourites
                    AlertDialog editFavouritesDialog=dialogInitializer.initializeEditFavouritesDialog();
                    editFavouritesDialog.show();
                }
            }
        });
    }

    public void setFloatingActionButtonOnClickIndicator(int  floatingActionButtonIndicator) {
        floatingActionButtonOnClickIndicator=floatingActionButtonIndicator;
        if(floatingActionButtonIndicator==0) {
            floatingActionButton.setImageResource(R.drawable.fab_favourites_icon);
        }
        else if (floatingActionButtonIndicator==1) {
            floatingActionButton.setImageResource(R.drawable.fab_edit_icon);
        }
    }
}
