package paweltypiak.matweather.firstLaunching.firstLaunchLoadingFragmentInitializing.firstLaunchLoadingFragmentDataDownloading;

import android.Manifest;
import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import paweltypiak.matweather.firstLaunching.firstLaunchLoadingFragmentInitializing.firstLaunchLoadingFragmentDataDownloading.FirstLaunchLoadingFragmentDialogInitializing.FirstLaunchLoadingFragmentGeocodingInternetFailureInitializer;
import paweltypiak.matweather.firstLaunching.firstLaunchLoadingFragmentInitializing.firstLaunchLoadingFragmentDataDownloading.FirstLaunchLoadingFragmentDialogInitializing.FirstLaunchLoadingFragmentGeolocalizationFailureInitializer;
import paweltypiak.matweather.firstLaunching.firstLaunchLoadingFragmentInitializing.firstLaunchLoadingFragmentDataDownloading.FirstLaunchLoadingFragmentDialogInitializing.FirstLaunchLoadingFragmentPermissionsDeniedDialogInitializer;
import paweltypiak.matweather.localizationDataDownloading.CurrentCoordinatesDownloader;
import paweltypiak.matweather.localizationDataDownloading.GeocodingCallback;
import paweltypiak.matweather.localizationDataDownloading.GeocodingDownloader;

public class FirstLaunchLoadingFragmentGeolocalizationDownloader
        implements GeocodingCallback
        {

    private Activity activity;
    private AlertDialog geocodingInternetFailureDialog;
    private AlertDialog geolocalizationFailureDialog;
    private AlertDialog permissionDeniedDialog;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=1;
    private FirstLaunchLoadingFragmentDataDownloader dataDownloader;
    private CurrentCoordinatesDownloader currentCoordinatesDownloader;

    public FirstLaunchLoadingFragmentGeolocalizationDownloader(Activity activity,
                                                               FirstLaunchLoadingFragmentDataDownloader dataDownloader){
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
        FirstLaunchLoadingFragmentGeocodingInternetFailureInitializer geocodingInternetFailureInitializer
                = new FirstLaunchLoadingFragmentGeocodingInternetFailureInitializer(activity,dataDownloader);
        geocodingInternetFailureDialog=geocodingInternetFailureInitializer.getGeocodingInternetFailureDialog();
    }

    private void getPermissionsDeniedDialog(){
        FirstLaunchLoadingFragmentPermissionsDeniedDialogInitializer permissionsDeniedDialogInitializer
                =new FirstLaunchLoadingFragmentPermissionsDeniedDialogInitializer(activity,dataDownloader);
        permissionDeniedDialog=permissionsDeniedDialogInitializer.getPermissionDeniedDialog();
    }

    private void getGeolocalizationFailureDialog(){
        FirstLaunchLoadingFragmentGeolocalizationFailureInitializer geolocalizationFailureInitializer
                =new FirstLaunchLoadingFragmentGeolocalizationFailureInitializer(activity,dataDownloader);
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
        currentCoordinatesDownloader =new CurrentCoordinatesDownloader(
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
                currentCoordinatesDownloader.getLocation(),
                this,
                dataDownloader.getMessageTextView(),
                activity);
        dataDownloader.setLoadingViewsVisibility(true);
    }

    private void setLoadingViewsVisibility(boolean isVisible){
        dataDownloader.setLoadingViewsVisibility(isVisible);
    }

    public CurrentCoordinatesDownloader getCurrentCoordinatesDownloader(){
        return currentCoordinatesDownloader;
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
