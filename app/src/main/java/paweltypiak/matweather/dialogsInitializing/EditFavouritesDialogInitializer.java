package paweltypiak.matweather.dialogsInitializing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

public class EditFavouritesDialogInitializer {

    public static  AlertDialog initializeEditFavouritesDialog(final Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_location,null);
        dialogView.setFocusableInTouchMode(true);
        dialogView.setClickable(true);
        String [] location=((MainActivity)activity).
                getMainActivityLayoutInitializer().
                getAppBarLayoutInitializer().
                getAppBarLayoutDataInitializer().
                getAppBarLocationName();
        final EditText headerEditText=(EditText)dialogView.findViewById(R.id.edit_location_dialog_title_edit_text);
        headerEditText.setText(location[0]);
        final EditText subheaderEditText=(EditText)dialogView.findViewById(R.id.edit_location_dialog_subtitle_edit_text);
        subheaderEditText.setText(location[1]);
        CheckBox checkBox=(CheckBox)dialogView.findViewById(R.id.edit_location_dialog_checkbox);
        checkBox.setChecked(FavouritesEditor.isDefeaultLocationEqual(activity,null));
        AlertDialogBuilder alertDialogBuilder=new AlertDialogBuilder(activity,
                dialogView,
                R.style.DialogStyle,
                activity.getString(R.string.edit_location_dialog_title),
                R.drawable.dialog_edit_icon,
                null,
                false,
                activity.getString(R.string.edit_location_dialog_positive_button),
                new saveFavouritesChangesRunnable(activity,dialogView),
                activity.getString(R.string.edit_location_dialog_neutral_button),
                new deleteFromFavouritesRunnable(activity),
                activity.getString(R.string.edit_location_dialog_negative_button),
                null);
        final AlertDialog editFavouritesDialog = alertDialogBuilder.getAlertDialog();
        editFavouritesDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                UsefulFunctions.customizeEditText(activity,editFavouritesDialog,headerEditText);
                UsefulFunctions.customizeEditText(activity,editFavouritesDialog,subheaderEditText);
            }
        });
        editFavouritesDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                UsefulFunctions.hideKeyboard(activity,null);
            }
        });
        return editFavouritesDialog;
    }

    private static class saveFavouritesChangesRunnable implements Runnable {

        private Activity activity;
        private View dialogView;

        public saveFavouritesChangesRunnable(Activity activity,View dialogView) {
            this.activity=activity;
            this.dialogView = dialogView;
        }

        public void run() {
            EditText headerEditText=(EditText)dialogView.findViewById(R.id.edit_location_dialog_title_edit_text);
            EditText subheaderEditText=(EditText)dialogView.findViewById(R.id.edit_location_dialog_subtitle_edit_text);
            String customHeaderString=headerEditText.getText().toString();
            customHeaderString=UsefulFunctions.getFormattedString(customHeaderString);
            String customSubheaderString=subheaderEditText.getText().toString();
            customSubheaderString=UsefulFunctions.getFormattedString(customSubheaderString);
            FavouritesEditor.editFavouriteLocationName(activity,customHeaderString,customSubheaderString);
            ((MainActivity)activity).getMainActivityLayoutInitializer().getAppBarLayoutInitializer().
                    getAppBarLayoutDataInitializer().
                    updateLocationName(customHeaderString,customSubheaderString);
            CheckBox checkBox=(CheckBox)dialogView.findViewById(R.id.edit_location_dialog_checkbox);
            if(checkBox.isChecked()){
                String city= OnWeatherDataChangeLayoutUpdater.getCurrentWeatherDataParser().getCity();
                String region=OnWeatherDataChangeLayoutUpdater.getCurrentWeatherDataParser().getRegion();
                String country=OnWeatherDataChangeLayoutUpdater.getCurrentWeatherDataParser().getCountry();
                ((MainActivity)activity).getMainActivityLayoutInitializer().getAppBarLayoutInitializer().
                        getAppBarLayoutDataInitializer().
                        updateLocationName(city,region+", "+ country);
                String currentLocationAddressString=city+", "+region+", "+ country;
                SharedPreferencesModifier.setDefeaultLocationConstant(activity,currentLocationAddressString);
            }
            else{
                if(FavouritesEditor.isDefeaultLocationEqual(activity,null)) {
                    SharedPreferencesModifier.setDefeaultLocationGeolocalization(activity);
                }
            }
        }
    }

    private static class deleteFromFavouritesRunnable implements Runnable{

        private Activity activity;

        public deleteFromFavouritesRunnable(Activity activity){
            this.activity=activity;
        }

        @Override
        public void run() {
            FavouritesEditor.deleteFavouritesItem(activity);
            ((MainActivity)activity).getMainActivityLayoutInitializer()
                    .getAppBarLayoutInitializer()
                    .getAppBarLayoutButtonsInitializer()
                    .getFloatingActionButtonInitializer()
                    .setFloatingActionButtonOnClickIndicator(0);
            ((MainActivity)activity).getMainActivityLayoutInitializer()
                    .getAppBarLayoutInitializer()
                    .getAppBarLayoutButtonsInitializer()
                    .getNavigationDrawerInitializer()
                    .uncheckNavigationDrawerMenuItem(1);
            String city=OnWeatherDataChangeLayoutUpdater.getCurrentWeatherDataParser().getCity();
            String region=OnWeatherDataChangeLayoutUpdater.getCurrentWeatherDataParser().getRegion();
            String country=OnWeatherDataChangeLayoutUpdater.getCurrentWeatherDataParser().getCountry();
            ((MainActivity)activity).getMainActivityLayoutInitializer().getAppBarLayoutInitializer().
                    getAppBarLayoutDataInitializer().
                    updateLocationName(city,region+", "+ country);
            if(FavouritesEditor.isDefeaultLocationEqual(activity,null)) {
                SharedPreferencesModifier.setDefeaultLocationGeolocalization(activity);
            }
        }
    }
}
