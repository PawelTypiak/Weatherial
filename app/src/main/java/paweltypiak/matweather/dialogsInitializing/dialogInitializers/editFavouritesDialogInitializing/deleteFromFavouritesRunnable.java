package paweltypiak.matweather.dialogsInitializing.dialogInitializers.editFavouritesDialogInitializing;

import android.app.Activity;

import paweltypiak.matweather.MainActivity;
import paweltypiak.matweather.mainActivityLayoutInitializing.LayoutUpdating.OnWeatherDataChangeLayoutUpdater;
import paweltypiak.matweather.usefulClasses.FavouritesEditor;
import paweltypiak.matweather.usefulClasses.SharedPreferencesModifier;

class DeleteFromFavouritesRunnable implements Runnable{

    private Activity activity;

    DeleteFromFavouritesRunnable(Activity activity){
        this.activity=activity;
    }

    @Override
    public void run() {
        FavouritesEditor.deleteFavouritesItem(activity);
        updateAppBarLayout();
        updateDefeaultLocation();
    }

    private void updateAppBarLayout(){
        setFloatingActionButtonOnClickIndicator();
        uncheckNavigationDrawerMenuItem();
        updateLocationName();
    }

    private void setFloatingActionButtonOnClickIndicator(){
        ((MainActivity)activity).getMainActivityLayoutInitializer()
                .getAppBarLayoutInitializer()
                .getAppBarLayoutButtonsInitializer()
                .getFloatingActionButtonInitializer()
                .setFloatingActionButtonOnClickIndicator(0);
    }

    private void uncheckNavigationDrawerMenuItem(){
        ((MainActivity)activity).getMainActivityLayoutInitializer()
                .getAppBarLayoutInitializer()
                .getAppBarLayoutButtonsInitializer()
                .getNavigationDrawerInitializer()
                .uncheckNavigationDrawerMenuItem(1);
    }

    private void updateLocationName(){
        String city= OnWeatherDataChangeLayoutUpdater.getCurrentWeatherDataParser().getCity();
        String region=OnWeatherDataChangeLayoutUpdater.getCurrentWeatherDataParser().getRegion();
        String country=OnWeatherDataChangeLayoutUpdater.getCurrentWeatherDataParser().getCountry();
        ((MainActivity)activity).getMainActivityLayoutInitializer().getAppBarLayoutInitializer().
                getAppBarLayoutDataInitializer().
                updateLocationName(city,region+", "+ country);
    }

    private void updateDefeaultLocation(){
        if(FavouritesEditor.isDefeaultLocationEqual(activity,null)) {
            SharedPreferencesModifier.setDefeaultLocationGeolocalization(activity);
        }
    }
}