package paweltypiak.matweather.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading;

import android.Manifest;
import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import paweltypiak.matweather.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.introActivityLoadingFragmentDialogInitializing.IntroActivityLoadingFragmentGeocodingInternetFailureInitializer;
import paweltypiak.matweather.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.introActivityLoadingFragmentDialogInitializing.IntroActivityLoadingFragmentGeolocalizationFailureInitializer;
import paweltypiak.matweather.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.introActivityLoadingFragmentDialogInitializing.IntroActivityLoadingFragmentPermissionsDeniedDialogInitializer;
import paweltypiak.matweather.dataDownloading.currentLocationDataDownloading.CurrentLocationCoordinatesDownloader;
import paweltypiak.matweather.dataDownloading.currentLocationDataDownloading.GeocodingCallback;
import paweltypiak.matweather.dataDownloading.currentLocationDataDownloading.GeocodingDownloader;

public class IntroActivityLoadingFragmentGeolocalizationDownloader
        implements GeocodingCallback
        {

    private Activity activity;
    private AlertDialog geocodingInternetFailureDialog;
    private AlertDialog geolocalizationFailureDialog;
    private AlertDialog permissionDeniedDialog;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=1;
    private IntroActivityLoadingFragmentDataDownloader dataDownloader;
    private CurrentLocationCoordinatesDownloader currentLocationCoordinatesDownloader;

    public IntroActivityLoadingFragmentGeolocalizationDownloader(Activity activity,
                                                                 IntroActivityLoadingFragmentDataDownloader dataDownloader){
        this.activity=activity;
        this.dataDownloader=dataDownloader;
        initializeDialogs();
    }

    private void initializeDialogs(){
        getGeocodingInternetFailureDialog();
        getGeolocalizationFailureDialog();
        getPermissionsDeniedDialog();
    }

    private void getGeocodingInternetFailureDialog(){
        IntroActivityLoadingFragmentGeocodingInternetFailureInitializer geocodingInternetFailureInitializer
                = new IntroActivityLoadingFragmentGeocodingInternetFailureInitializer(activity,dataDownloader);
        geocodingInternetFailureDialog=geocodingInternetFailureInitializer.getGeocodingInternetFailureDialog();
    }

    private void getPermissionsDeniedDialog(){
        IntroActivityLoadingFragmentPermissionsDeniedDialogInitializer permissionsDeniedDialogInitializer
                =new IntroActivityLoadingFragmentPermissionsDeniedDialogInitializer(activity,dataDownloader);
        permissionDeniedDialog=permissionsDeniedDialogInitializer.getPermissionDeniedDialog();
    }

    private void getGeolocalizationFailureDialog(){
        IntroActivityLoadingFragmentGeolocalizationFailureInitializer geolocalizationFailureInitializer
                =new IntroActivityLoadingFragmentGeolocalizationFailureInitializer(activity,dataDownloader);
        geolocalizationFailureDialog=geolocalizationFailureInitializer.getGeolocalizationFailureDialog();
    }

    public void initializeCurrentLocationDataDownloading() {
        //permissions for Android 6.0
        if (areGeolocalizationPermissionsGranted() == true) {
            downloadCurrentLocationCoordinates();
        } else {
            dataDownloader.getPermissionsListener().requestLocalizationPermissions();
        }
    }

    private boolean areGeolocalizationPermissionsGranted(){
        if ( ContextCompat.checkSelfPermission( activity, Manifest.permission.ACCESS_FINE_LOCATION )
                == PackageManager.PERMISSION_GRANTED ) {
            return true;
        }
        else {
            return false;
        }
    }

    public void onRequestLocationPermissionsResult(int requestCode, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    downloadCurrentLocationCoordinates();
                } else {
                    permissionDeniedDialog.show();
                }
            }
        }
    }

    private void downloadCurrentLocationCoordinates(){
        setLoadingViewsVisibility(true);
        currentLocationCoordinatesDownloader =new CurrentLocationCoordinatesDownloader(
                activity,
                this,
                geolocalizationFailureDialog,
                permissionDeniedDialog,
                null,
                dataDownloader.getMessageTextView(),
                dataDownloader.getLoadingProgressBar(),
                dataDownloader.getSelectedDefeaultLocalizationMethod()
        );
    }

    public void initializeGeocodingDownloading(){
        new GeocodingDownloader(
                activity,
                currentLocationCoordinatesDownloader.getLocation(),
                this,
                dataDownloader.getMessageTextView()
                );
        dataDownloader.setLoadingViewsVisibility(true);
    }

    private void setLoadingViewsVisibility(boolean isVisible){
        dataDownloader.setLoadingViewsVisibility(isVisible);
    }

    public CurrentLocationCoordinatesDownloader getCurrentLocationCoordinatesDownloader(){
        return currentLocationCoordinatesDownloader;
    }

    @Override
    public void geocodingServiceSuccess(String location) {
        dataDownloader.setLocation(location);
        dataDownloader.getWeatherDataDownloader().downloadWeatherData(location);
    }

    @Override
    public void geocodingServiceFailure(int errorCode) {
        if(errorCode==0){
            showDialog(geocodingInternetFailureDialog);
        }
        else if(errorCode==1){
            showDialog(geolocalizationFailureDialog);
        }
    }

    private void showDialog(AlertDialog alertDialog){
        alertDialog.show();
        dataDownloader.setLoadingViewsVisibility(false);
    }
}
