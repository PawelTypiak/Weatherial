package paweltypiak.matweather.dialogsInitializing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import paweltypiak.matweather.MainActivity;
import paweltypiak.matweather.R;
import paweltypiak.matweather.mainActivityLayoutInitializing.LayoutUpdating.OnWeatherDataChangeLayoutUpdater;
import paweltypiak.matweather.usefulClasses.FavouritesEditor;
import paweltypiak.matweather.usefulClasses.SharedPreferencesModifier;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

public class AddToFavouritesDialogInitializer {

    public static AlertDialog initializeAddToFavouritesDialog(final Activity activity){
        View dialogView=initializeDialogView(activity);
        setDialogViewClickableAndFocusable(dialogView);
        EditText[] editTextArray=initializeEditTexts(activity,dialogView);
        final AlertDialog addToFavouritesDialog=buildAlertDialog(activity,dialogView);
        setDialogOnShowListener(activity,addToFavouritesDialog,editTextArray);
        setDialogOnDissmissListener(activity,addToFavouritesDialog);
        return addToFavouritesDialog;
    }

    private static View initializeDialogView(Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_edit_location,null);
        return dialogView;
    }

    private static void setDialogViewClickableAndFocusable(View dialogView){
        dialogView.setFocusableInTouchMode(true);
        dialogView.setClickable(true);
    }

    private static EditText[] initializeEditTexts(Activity activity, View dialogView){
        String [] location=((MainActivity)activity).
                getMainActivityLayoutInitializer().
                getAppBarLayoutInitializer().
                getAppBarLayoutDataInitializer().
                getAppBarLocationName();
        final EditText titleEditText=(EditText)dialogView.findViewById(R.id.edit_location_dialog_title_edit_text);
        titleEditText.setText(location[0]);
        final EditText subheaderEditText=(EditText)dialogView.findViewById(R.id.edit_location_dialog_subtitle_edit_text);
        subheaderEditText.setText(location[1]);
        EditText[] editTextArray={titleEditText,subheaderEditText};
        return editTextArray;
    }

    private static AlertDialog buildAlertDialog(Activity activity, View dialogView){
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder(activity,
                dialogView,
                R.style.DialogStyle,
                activity.getString(R.string.add_location_dialog_title),
                R.drawable.dialog_favourites_icon,
                null,
                false,
                activity.getString(R.string.add_location_dialog_positive_button),
                new addToFavouritesRunnable(activity,dialogView),
                null,
                null,
                activity.getString(R.string.add_location_dialog_negative_button),
                null);
        return alertDialogBuilder.getAlertDialog();
    }

    private static void setDialogOnShowListener(final Activity activity,
                                                final AlertDialog addToFavouritesDialog,
                                                final EditText[] editTextArray){
        addToFavouritesDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                UsefulFunctions.customizeEditText(activity,addToFavouritesDialog,editTextArray[0]);
                UsefulFunctions.customizeEditText(activity,addToFavouritesDialog,editTextArray[1]);
            }
        });
    }

    private static void setDialogOnDissmissListener(final Activity activity,AlertDialog addToFavouritesDialog){
        addToFavouritesDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                UsefulFunctions.hideKeyboard(activity,null);
            }
        });
    }


    private static class addToFavouritesRunnable implements Runnable {

        private Activity activity;
        private View dialogView;

        public addToFavouritesRunnable(Activity activity,
                                       View dialogView) {
            this.activity=activity;
            this.dialogView = dialogView;
        }

        public void run() {
            EditText headerEditText=(EditText)dialogView.findViewById(R.id.edit_location_dialog_title_edit_text);
            EditText subheaderEditText=(EditText)dialogView.findViewById(R.id.edit_location_dialog_subtitle_edit_text);
            String customHeaderString=headerEditText.getText().toString();
            String customSubheaderString=subheaderEditText.getText().toString();
            FavouritesEditor.saveNewFavouritesItem(activity,customHeaderString,customSubheaderString,null);
            CheckBox checkBox=(CheckBox)dialogView.findViewById(R.id.edit_location_dialog_checkbox);
            if(checkBox.isChecked()){
                Log.d("checkbox", "checked");
                String city= OnWeatherDataChangeLayoutUpdater.getCurrentWeatherDataParser().getCity();
                String region=OnWeatherDataChangeLayoutUpdater.getCurrentWeatherDataParser().getRegion();
                String country=OnWeatherDataChangeLayoutUpdater.getCurrentWeatherDataParser().getCountry();
                String currentLocationAddressString=city+", "+region+", "+ country;
                SharedPreferencesModifier.setDefeaultLocationConstant(activity,currentLocationAddressString);
            }
            String [] locationName=FavouritesEditor.getFavouriteLocationNameForAppbar(activity);
            ((MainActivity)activity).getMainActivityLayoutInitializer().getAppBarLayoutInitializer().
                    getAppBarLayoutDataInitializer().
                    updateLocationName(locationName[0],locationName[1]);
            ((MainActivity)activity).getMainActivityLayoutInitializer()
                    .getAppBarLayoutInitializer()
                    .getAppBarLayoutButtonsInitializer()
                    .getFloatingActionButtonInitializer()
                    .setFloatingActionButtonOnClickIndicator(1);
            ((MainActivity)activity).getMainActivityLayoutInitializer()
                    .getAppBarLayoutInitializer()
                    .getAppBarLayoutButtonsInitializer()
                    .getNavigationDrawerInitializer()
                    .checkNavigationDrawerMenuItem(1);
        }
    }
}
