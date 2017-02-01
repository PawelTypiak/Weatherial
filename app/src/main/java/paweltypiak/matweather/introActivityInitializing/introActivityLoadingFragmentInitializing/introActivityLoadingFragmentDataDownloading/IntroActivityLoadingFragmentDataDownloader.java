package paweltypiak.matweather.introActivityInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import paweltypiak.matweather.R;
import paweltypiak.matweather.introActivityInitializing.introActivityLocationFragmentInitializing.ShowLocationFragmentAgainListener;
import paweltypiak.matweather.introActivityInitializing.introActivityLoadingFragmentInitializing.OnRequestLocalizationPermissionsListener;
import paweltypiak.matweather.introActivityInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.introActivityLoadingFragmentDialogInitializing.IntroActivityLoadingFragmentGeolocalizationMethodsDialogInitializer;
import paweltypiak.matweather.usefulClasses.SharedPreferencesModifier;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

public class IntroActivityLoadingFragmentDataDownloader {

    private Activity activity;
    private ShowLocationFragmentAgainListener locationListener;
    private OnRequestLocalizationPermissionsListener permissionsListener;
    private boolean isFirstLaunch;
    private int selectedDefeaultLocationOption;
    private int selectedDefeaultLocalizationMethod ;
    private String differentLocationName;
    private String location;
    private boolean isNextLaunchAfterFailure=false;
    private String changedLocation;
    private IntroActivityLoadingFragmentGeolocalizationDownloader geolocalizationDownloader;
    private IntroActivityLoadingFragmentWeatherDataDownloader weatherDataDownloader;
    private TextView messageTextView;
    private ProgressBar loadingProgressBar;
    private View marginView;

    public IntroActivityLoadingFragmentDataDownloader(Activity activity,
                                                      ShowLocationFragmentAgainListener locationListener,
                                                      OnRequestLocalizationPermissionsListener permissionsListener,
                                                      boolean isFirstLaunch,
                                                      int selectedDefeaultLocalizationMethod,
                                                      int selectedDefeaultLocationOption,
                                                      String differentLocationName){
        this.selectedDefeaultLocalizationMethod=selectedDefeaultLocalizationMethod;
        this.selectedDefeaultLocationOption=selectedDefeaultLocationOption;
        this.isFirstLaunch=isFirstLaunch;
        this.differentLocationName=differentLocationName;
        this.activity=activity;
        this.locationListener=locationListener;
        this.permissionsListener=permissionsListener;
        getViews();
        initializeGeolocalizationDownloader();
        initializeWeatherDataDownloader();
        initializeLoadingProcess();
    }

    private void getViews(){
        loadingProgressBar=(ProgressBar)activity.findViewById(R.id.intro_activity_loading_fragment_progress_circle);
        messageTextView=(TextView)activity.findViewById(R.id.intro_activity_loading_fragment_message);
        marginView=activity.findViewById(R.id.intro_activity_loading_fragment_margin_view);
    }

    private void initializeGeolocalizationDownloader(){
        geolocalizationDownloader
                =new IntroActivityLoadingFragmentGeolocalizationDownloader(
                activity,
                this);
    }

    private void initializeWeatherDataDownloader(){
        weatherDataDownloader
                =new IntroActivityLoadingFragmentWeatherDataDownloader(
                activity,
                this);
    }

    private void initializeLoadingProcess(){
        if(isFirstLaunch==true) {
            initializeFirstLaunch();
        }
        else {
            initializeNextLaunch();
        }
    }

    private void initializeFirstLaunch(){
        if(selectedDefeaultLocationOption ==0) {
            geolocalizationDownloader.initializeCurrentLocationDataDownloading();
        }
        else if(selectedDefeaultLocationOption ==1){
            setLoadingViewsVisibility(true);
            location=differentLocationName;
            weatherDataDownloader.downloadWeatherData(location);
        }
    }

    public void initializeNextLaunch(){
        if(!SharedPreferencesModifier.isDefeaultLocationConstant(activity)){
            selectedDefeaultLocalizationMethod =SharedPreferencesModifier.getGeolocalizationMethod(activity);
            if(selectedDefeaultLocalizationMethod ==-1){
                showGeolocalizationMethodsDialog();
            }
            else{
                geolocalizationDownloader.initializeCurrentLocationDataDownloading();
            }
        }
        else {
            setLoadingViewsVisibility(true);
            location=SharedPreferencesModifier.getDefeaultLocation(activity);
            weatherDataDownloader.downloadWeatherData(location);
        }
    }

    public void initializeNextLaunchAfterFailure(){
        isNextLaunchAfterFailure=true;
        if(changedLocation==null){
            if(selectedDefeaultLocalizationMethod ==-1){
                setLoadingViewsVisibility(false);
                showGeolocalizationMethodsDialog();
            }
            else {
                geolocalizationDownloader.initializeCurrentLocationDataDownloading();
            }
        }
        else{
            setLoadingViewsVisibility(true);
            weatherDataDownloader.downloadWeatherData(changedLocation);
        }
    }

    private void showGeolocalizationMethodsDialog(){
        IntroActivityLoadingFragmentGeolocalizationMethodsDialogInitializer geolocalizationMethodsDialogInitializer
                =new IntroActivityLoadingFragmentGeolocalizationMethodsDialogInitializer(activity,this);
        AlertDialog geolocalizationMethodsDialog
                =geolocalizationMethodsDialogInitializer.getGeolocalizationMethodsDialog();
        geolocalizationMethodsDialog.show();
    }

    public void setLoadingViewsVisibility(boolean isVisible){
        if(isVisible==true){
            UsefulFunctions.setViewVisible(loadingProgressBar);
            UsefulFunctions.setViewVisible(messageTextView);
        }
        else{
            UsefulFunctions.setViewInvisible(loadingProgressBar);
            UsefulFunctions.setViewInvisible(messageTextView);
        }
    }

    public ShowLocationFragmentAgainListener getLocationListener() {
        return locationListener;
    }

    public OnRequestLocalizationPermissionsListener getPermissionsListener() {
        return permissionsListener;
    }

    public boolean isFirstLaunch() {
        return isFirstLaunch;
    }

    public int getSelectedDefeaultLocationOption() {
        return selectedDefeaultLocationOption;
    }

    public int getSelectedDefeaultLocalizationMethod() {
        return selectedDefeaultLocalizationMethod;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isNextLaunchAfterFailure() {
        return isNextLaunchAfterFailure;
    }

    public String getChangedLocation() {
        return changedLocation;
    }

    public void setChangedLocation(String changedLocation) {
        this.changedLocation = changedLocation;
    }

    public IntroActivityLoadingFragmentGeolocalizationDownloader getGeolocalizationDownloader() {return geolocalizationDownloader;}

    public IntroActivityLoadingFragmentWeatherDataDownloader getWeatherDataDownloader() {
        return weatherDataDownloader;
    }

    public TextView getMessageTextView() {
        return messageTextView;
    }

    public ProgressBar getLoadingProgressBar() {
        return loadingProgressBar;
    }

    public View getMarginView() {
        return marginView;
    }
}
