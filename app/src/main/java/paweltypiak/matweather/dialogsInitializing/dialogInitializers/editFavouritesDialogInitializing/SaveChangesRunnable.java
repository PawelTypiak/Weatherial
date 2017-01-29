package paweltypiak.matweather.dialogsInitializing.dialogInitializers.editFavouritesDialogInitializing;

import android.app.Activity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import paweltypiak.matweather.mainActivityInitializing.MainActivity;
import paweltypiak.matweather.R;
import paweltypiak.matweather.mainActivityInitializing.mainActivityLayoutInitializing.layoutUpdating.OnWeatherDataChangeLayoutUpdater;
import paweltypiak.matweather.usefulClasses.FavouritesEditor;
import paweltypiak.matweather.usefulClasses.SharedPreferencesModifier;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

class SaveChangesRunnable implements Runnable {

    private Activity activity;
    private View dialogView;

    SaveChangesRunnable(Activity activity, View dialogView) {
        this.activity=activity;
        this.dialogView = dialogView;
    }

    public void run() {
        updateFavouriteLocationName();
        updateDefeaultLocation();
    }

    private void updateFavouriteLocationName(){
        String[] locationName=getEditedLocationName();
        FavouritesEditor.editFavouriteLocationName(activity,locationName[0],locationName[1]);
        updateAppBarLocationName(locationName[0],locationName[1]);
    }

    private String[] getEditedLocationName(){
        String title= getEditedLocationNameTitle();
        String subtitle= getEditedLocationNameSubtitle();
        String[] locationName={title,subtitle};
        return locationName;
    }

    private String getEditedLocationNameTitle(){
        EditText titleEditText=(EditText)dialogView.findViewById(R.id.edit_location_dialog_title_edit_text);
        String title=titleEditText.getText().toString();
        return UsefulFunctions.getFormattedString(title);
    }

    private String getEditedLocationNameSubtitle(){
        EditText subtitleEditText=(EditText)dialogView.findViewById(R.id.edit_location_dialog_subtitle_edit_text);
        String subtitle=subtitleEditText.getText().toString();
        return UsefulFunctions.getFormattedString(subtitle);
    }

    private void updateAppBarLocationName(String title, String  subtitle){
        ((MainActivity)activity).getMainActivityLayoutInitializer().getAppBarLayoutInitializer().
                getAppBarLayoutDataInitializer().
                updateLocationName(title,subtitle);
    }

    private void updateDefeaultLocation(){
        CheckBox checkBox=(CheckBox)dialogView.findViewById(R.id.edit_location_dialog_checkbox);
        if(checkBox.isChecked()){
            String currentLocationAddress= getLocationAddress();
            SharedPreferencesModifier.setDefeaultLocationConstant(activity,currentLocationAddress);
        }
        else{
            if(FavouritesEditor.isDefeaultLocationEqual(activity,null)) {
                SharedPreferencesModifier.setDefeaultLocationGeolocalization(activity);
            }
        }
    }

    private String getLocationAddress(){
        String city= OnWeatherDataChangeLayoutUpdater.getCurrentWeatherDataParser().getCity();
        String region=OnWeatherDataChangeLayoutUpdater.getCurrentWeatherDataParser().getRegion();
        String country=OnWeatherDataChangeLayoutUpdater.getCurrentWeatherDataParser().getCountry();
        String address=city+", "+region+", "+country;
        return address;
    }
}
