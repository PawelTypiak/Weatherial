package paweltypiak.weatherial.dialogsInitializing.dialogInitializers.addToFavouritesDialogInitializing;

import android.app.Activity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import paweltypiak.weatherial.mainActivityInitializing.MainActivity;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.layoutUpdating.OnWeatherDataChangeLayoutUpdater;
import paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.appBarInitializing.AppBarLayoutInitializer;
import paweltypiak.weatherial.usefulClasses.FavouritesEditor;
import paweltypiak.weatherial.usefulClasses.SharedPreferencesModifier;
import paweltypiak.weatherial.usefulClasses.UsefulFunctions;

class AddToFavouritesRunnable implements Runnable {

    private Activity activity;
    private View dialogView;

    AddToFavouritesRunnable(Activity activity,
                            View dialogView) {
        this.activity=activity;
        this.dialogView = dialogView;
    }

    public void run() {
        saveNewFavouritesItem();
        updateDefaultLocation();
        updateAppBarLayout();
    }

    private void saveNewFavouritesItem(){
        String title=getTitle();
        String subtitle=getSubtitle();
        FavouritesEditor.saveNewFavouritesItem(activity,title,subtitle,null);
    }

    private String getTitle(){
        EditText titleEditText=(EditText)dialogView.findViewById(R.id.edit_location_dialog_title_edit_text);
        String title=titleEditText.getText().toString();
        return UsefulFunctions.getFormattedString(title);
    }

    private String getSubtitle(){
        EditText subtitleEditText=(EditText)dialogView.findViewById(R.id.edit_location_dialog_subtitle_edit_text);
        String subtitle=subtitleEditText.getText().toString();
        return UsefulFunctions.getFormattedString(subtitle);
    }

    private void updateDefaultLocation(){
        CheckBox checkBox=(CheckBox)dialogView.findViewById(R.id.edit_location_dialog_checkbox);
        if(checkBox.isChecked()){
            String city= OnWeatherDataChangeLayoutUpdater.getCurrentWeatherDataParser().getCity();
            String region=OnWeatherDataChangeLayoutUpdater.getCurrentWeatherDataParser().getRegion();
            String country=OnWeatherDataChangeLayoutUpdater.getCurrentWeatherDataParser().getCountry();
            String currentLocationAddressString=city+", "+region+", "+ country;
            SharedPreferencesModifier.setDefaultLocationConstant(activity,currentLocationAddressString);
        }
    }

    private void updateAppBarLayout(){
        AppBarLayoutInitializer appBarLayoutInitializer
                =((MainActivity)activity).
                getMainActivityLayoutInitializer().
                getAppBarLayoutInitializer();
        updateLocationName(appBarLayoutInitializer);
        setFloatingActionButtonOnClickIndicator(appBarLayoutInitializer);
        checkNavigationDrawerMenuItem(appBarLayoutInitializer);

    }

    private void updateLocationName(AppBarLayoutInitializer appBarLayoutInitializer){
        String [] locationName=FavouritesEditor.getFavouriteLocationNameForAppbar(activity);
        appBarLayoutInitializer.
                getAppBarLayoutDataInitializer().
                updateLocationName(locationName[0],locationName[1]);
    }

    private void setFloatingActionButtonOnClickIndicator(AppBarLayoutInitializer appBarLayoutInitializer){
        appBarLayoutInitializer.
                getAppBarLayoutButtonsInitializer().
                getFloatingActionButtonInitializer().
                setFloatingActionButtonOnClickIndicator(1);
    }

    private void checkNavigationDrawerMenuItem(AppBarLayoutInitializer appBarLayoutInitializer){
        appBarLayoutInitializer.
                getAppBarLayoutButtonsInitializer().
                getNavigationDrawerInitializer().
                checkNavigationDrawerMenuItem(1);
    }
}