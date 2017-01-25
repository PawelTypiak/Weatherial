package paweltypiak.matweather.mainActivityLayoutInitializing.appBarInitializing.appBarButtonsInitializing;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import paweltypiak.matweather.R;
import paweltypiak.matweather.dialogsInitializing.dialogInitializers.addToFavouritesDialogInitializing.AddToFavouritesDialogInitializer;
import paweltypiak.matweather.dialogsInitializing.dialogInitializers.editFavouritesDialogInitializing.EditFavouritesDialogInitializer;

public class FloatingActionButtonInitializer {

    private FloatingActionButton floatingActionButton;
    private int floatingActionButtonOnClickIndicator=0;

    public FloatingActionButtonInitializer(Activity activity){
        setFloatingActionButtonOnClickListener(
                activity);
    }

    private void setFloatingActionButtonOnClickListener(final Activity activity){
        floatingActionButton =(FloatingActionButton) activity.findViewById(R.id.floating_action_button);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(floatingActionButtonOnClickIndicator ==0){
                    //if location is not in favourites
                    AlertDialog addToFavouritesDialog = AddToFavouritesDialogInitializer.getAddToFavouritesDialog(activity);
                    addToFavouritesDialog.show();
                }
                else if(floatingActionButtonOnClickIndicator ==1){
                    //if location is in favourites
                    AlertDialog editFavouritesDialog= EditFavouritesDialogInitializer.getEditFavouritesDialog(activity);
                    editFavouritesDialog.show();
                }
            }
        });
    }

    public void setFloatingActionButtonOnClickIndicator(int  floatingActionButtonIndicator) {
        floatingActionButtonOnClickIndicator=floatingActionButtonIndicator;
        if(floatingActionButtonIndicator==0) {
            floatingActionButton.setImageResource(R.drawable.favourites_icon);
        }
        else if (floatingActionButtonIndicator==1) {
            floatingActionButton.setImageResource(R.drawable.edit_icon);
        }
    }
}
