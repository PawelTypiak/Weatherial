package paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.appBarInitializing.appBarButtonsInitializing.navigationDrawerInitializing;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.AuthorDialogInitializer;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.GeolocalizationMethodsDialogInitializer;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.NoFavouritesAvailableDialogInitializer;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.favouritesDialogInitializing.FavouritesDialogInitializer;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.feedbackDialogInitializing.FeedbackDialogInitializer;
import paweltypiak.weatherial.infoActivityInitializing.InfoActivity;
import paweltypiak.weatherial.mainActivityInitializing.MainActivity;
import paweltypiak.weatherial.settingsActivityInitializing.SettingsActivity;
import paweltypiak.weatherial.usefulClasses.SharedPreferencesModifier;

public class NavigationItemSelectedListenerSetter implements NavigationView.OnNavigationItemSelectedListener {

    private Activity activity;
    private SmoothActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private AlertDialog geolocalizationMethodsDialog;
    private AlertDialog noFavouritesDialog;
    private AlertDialog feedbackDialog;
    private AlertDialog authorDialog;

    public NavigationItemSelectedListenerSetter (Activity activity,
                                                 SmoothActionBarDrawerToggle actionBarDrawerToggle,
                                                 DrawerLayout drawerLayout,
                                                 NavigationView navigationView){
        this.activity=activity;
        this.actionBarDrawerToggle=actionBarDrawerToggle;
        this.drawerLayout=drawerLayout;
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        final int id = item.getItemId();
        actionBarDrawerToggle.runWhenIdle(new Runnable() {
            @Override
            public void run() {
                if(id== R.id.nav_button_geolocalization){
                    //geolocalization
                    onGeolocalizationButtonClick();
                }
                else if(id==R.id.nav_button_favourites){
                    //favourites
                    onFavouritesButtonClick();
                }
                else if (id == R.id.nav_button_settings) {
                    //settings
                    onSettingsButtonClick();
                }
                else if (id == R.id.nav_button_about) {
                    //about
                    onAboutButtonClick();
                }
                else if (id == R.id.nav_button_feedback) {
                    //send feedback
                    onFeedbackButtonClick();
                }
                else if(id == R.id.nav_button_author){
                    //author
                    onAuthorButtonClick();
                }
            }
        });
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void onGeolocalizationButtonClick(){
        int localizationOption= SharedPreferencesModifier.getGeolocalizationMethod(activity);
        if(localizationOption==-1){
            if(geolocalizationMethodsDialog==null){
                GeolocalizationMethodsDialogInitializer geolocalizationMethodsDialogInitializer
                        =new GeolocalizationMethodsDialogInitializer(
                        activity,
                        1,
                        startGeolocalizationRunnable);
                geolocalizationMethodsDialog= geolocalizationMethodsDialogInitializer.getGeolocalizationMethodsDialog();
            }
            geolocalizationMethodsDialog.show();
        }
        else{
            startGeolocalizationRunnable.run();
        }
    }

    private Runnable startGeolocalizationRunnable = new Runnable() {
        public void run() {
            ((MainActivity)activity).
                    getMainActivityDataDownloader().
                    getGeolocalizationDownloader().
                    initializeCurrentLocationDataDownloading();
        }
    };

    private void onFavouritesButtonClick(){
        if(SharedPreferencesModifier.getFavouriteLocationsAddresses(activity).length==0) {
            if(noFavouritesDialog==null){
                noFavouritesDialog= NoFavouritesAvailableDialogInitializer.getNoFavouritesAvailableDialog(activity);
            }
            noFavouritesDialog.show();
        }
        else{
            FavouritesDialogInitializer favouritesDialogInitializer
                    =new FavouritesDialogInitializer(
                    activity,
                    1,
                    null,
                    null);
            AlertDialog favouritesDialog= favouritesDialogInitializer.getFavouritesDialog();
            favouritesDialog.show();
        }
    }

    private void onSettingsButtonClick(){
        Intent intent = new Intent(
                activity,
                SettingsActivity.class);
        activity.startActivity(intent);
    }

    private void onAboutButtonClick(){
        Intent intent = new Intent(
                activity,
                InfoActivity.class);
        activity.startActivity(intent);
    }

    private void onFeedbackButtonClick(){
        if (feedbackDialog == null) {
            feedbackDialog= FeedbackDialogInitializer.getFeedbackDialog(activity);
        }
        feedbackDialog.show();
    }

    private void onAuthorButtonClick(){
        if (authorDialog == null) {
            authorDialog= AuthorDialogInitializer.getAuthorDialog(activity);
        }
        authorDialog.show();
    }
}
