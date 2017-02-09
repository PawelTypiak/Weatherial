package paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.appBarInitializing.appBarButtonsInitializing;

import android.app.Activity;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.support.design.widget.FloatingActionButton;
import android.view.View;

import paweltypiak.weatherial.R;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.addToFavouritesDialogInitializing.AddToFavouritesDialogInitializer;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.editFavouritesDialogInitializing.EditFavouritesDialogInitializer;

public class FloatingActionButtonInitializer {

    private FloatingActionButton floatingActionButton;
    private int floatingActionButtonOnClickIndicator=0;

    public FloatingActionButtonInitializer(Activity activity){
        findFloatingActionButton(activity);
        setFloatingActionButtonMargin(activity);
        setFloatingActionButtonOnClickListener(
                activity);
    }

    private void findFloatingActionButton(Activity activity){
        floatingActionButton =(FloatingActionButton) activity.findViewById(R.id.floating_action_button);
    }

    private void setFloatingActionButtonMargin(Activity activity){
        CoordinatorLayout.LayoutParams floatingActionButtonLayoutParams
                = (CoordinatorLayout.LayoutParams)floatingActionButton.getLayoutParams();
        int margin=getMargin(activity);
        floatingActionButtonLayoutParams.setMargins(0,0,margin,0);
        floatingActionButton.setLayoutParams(floatingActionButtonLayoutParams);
    }

    private int getMargin(Activity activity){
        int margin;
        if (Build.VERSION.SDK_INT >= 21) {
            margin=(int)activity.getResources().getDimension(R.dimen.floating_action_button_margin);
        } else {
            margin=(int)activity.getResources().getDimension(R.dimen.floating_action_button_pre_lollipop_margin);
        }
        return margin;
    }

    private void setFloatingActionButtonOnClickListener(final Activity activity){
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
