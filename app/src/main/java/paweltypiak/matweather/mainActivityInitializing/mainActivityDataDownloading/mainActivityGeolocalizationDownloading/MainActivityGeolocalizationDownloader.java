package paweltypiak.matweather.mainActivityInitializing.mainActivityDataDownloading.mainActivityGeolocalizationDownloading;

import android.Manifest;
import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.TextView;
import paweltypiak.matweather.R;
import paweltypiak.matweather.dialogsInitializing.dialogInitializers.GeolocalizationFailureDialogInitializer;
import paweltypiak.matweather.dialogsInitializing.dialogInitializers.GeolocalizationPermissionsDeniedDialogInitializer;
import paweltypiak.matweather.dialogsInitializing.dialogInitializers.InternetFailureDialogInitializer;
import paweltypiak.matweather.dialogsInitializing.dialogInitializers.ProgressDialogInitializer;
import paweltypiak.matweather.dialogsInitializing.dialogInitializers.ServiceFailureDialogInitializer;
import paweltypiak.matweather.dataDownloading.currentLocationDataDownloading.CurrentLocationCoordinatesDownloader;
import paweltypiak.matweather.dataDownloading.currentLocationDataDownloading.GeocodingCallback;
import paweltypiak.matweather.dataDownloading.currentLocationDataDownloading.GeocodingDownloader;
import paweltypiak.matweather.mainActivityInitializing.mainActivityDataDownloading.MainActivityDataDownloader;
import paweltypiak.matweather.usefulClasses.SharedPreferencesModifier;

public class MainActivityGeolocalizationDownloader implements
        GeocodingCallback,
        OnRequestLocationPermissionsResultCallback {

    private Activity activity;
    private String geocodingLocation;
    private MainActivityDataDownloader dataDownloader;
    private CurrentLocationCoordinatesDownloader currentLocationCoordinatesDownloader;
    private AlertDialog geolocalizationProgressDialog;
    private TextView progressDialogMessageTextView;
    private AlertDialog geocodingInternetFailureDialog;
    private AlertDialog geocodingServiceFailureDialog;
    private AlertDialog permissionDeniedDialog;
    private AlertDialog coordinatesDownloadFailureDialog;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=1;

    public MainActivityGeolocalizationDownloader(Activity activity,
                                                 MainActivityDataDownloader dataDownloader){
        this.activity=activity;
        this.dataDownloader=dataDownloader;
        initializeDialogs(activity);
    }

    private void initializeDialogs(Activity activity){
        geolocalizationProgressDialog
                = ProgressDialogInitializer.getProgressDialog(
                activity,
                activity.getString(R.string.waiting_for_localization_progress_message));
        geocodingInternetFailureDialog
                = InternetFailureDialogInitializer.getInternetFailureDialog(
                activity,
                1,
                geocodingRunnable,
                null);
        geocodingServiceFailureDialog
                = ServiceFailureDialogInitializer.getServiceFailureDialog(
                activity,
                1,
                startGeolocalizationRunnable,
                null);
        permissionDeniedDialog
                = GeolocalizationPermissionsDeniedDialogInitializer.getGeolocalizationPermissionsDeniedDialog(
                activity,
                1,
                startGeolocalizationRunnable,
                null);
        coordinatesDownloadFailureDialog=
                GeolocalizationFailureDialogInitializer.getGeolocalizationFailureDialog(
                        activity,
                        1,
                        startGeolocalizationRunnable,
                        null);
    }

    public AlertDialog getGeolocalizationProgressDialog() {
        return geolocalizationProgressDialog;
    }

    @Override
    public void geocodingServiceSuccess(String location) {
        progressDialogMessageTextView.setText(activity.getString(R.string.downloading_weather_data_progress_message));
        geocodingLocation=location;
        dataDownloader.getWeatherDataDownloader().initializeWeatherDataDownloading(0,location);
    }

    @Override
    public void geocodingServiceFailure(int errorCode) {
        geolocalizationProgressDialog.dismiss();
        if(errorCode==0){
            geocodingInternetFailureDialog.show();
        }
        else if(errorCode==1){
            geocodingServiceFailureDialog.show();
        }
    }

    public String getGeocodingLocation() {
        return geocodingLocation;
    }

    public void initializeCurrentLocationDataDownloading(){
        //permissions for Android 6.0
        if(areGeolocalizationPermissionsGranted()==true){
            downloadCurrentLocationCoordinates();
        }
        else{
            ActivityCompat.requestPermissions(
                    activity,
                    new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private boolean areGeolocalizationPermissionsGranted(){
        if ( ContextCompat.checkSelfPermission( activity, Manifest.permission.ACCESS_FINE_LOCATION )
                == PackageManager.PERMISSION_GRANTED )
        {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void onRequestLocationPermissionsResult(int requestCode, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    downloadCurrentLocationCoordinates();
                    Log.d("permissions_downloader", "granted");
                } else {
                    permissionDeniedDialog.show();
                    Log.d("permissions_downloader", "denied");
                }
            }
        }
    }

    public OnRequestLocationPermissionsResultCallback getOnRequestLocationPermissionsResultCallback() {
        return this;
    }

    private void downloadCurrentLocationCoordinates(){
        geolocalizationProgressDialog.show();
        if(progressDialogMessageTextView==null){
            progressDialogMessageTextView
                    =(TextView)geolocalizationProgressDialog.findViewById(R.id.progress_dialog_message_text);
        }
        int geolocalizationMethod= SharedPreferencesModifier.getGeolocalizationMethod(activity);
        currentLocationCoordinatesDownloader =new CurrentLocationCoordinatesDownloader(
                activity,
                this,
                coordinatesDownloadFailureDialog,
                permissionDeniedDialog,
                geolocalizationProgressDialog,
                progressDialogMessageTextView,
                null,
                geolocalizationMethod
        );
    }

    private Runnable startGeolocalizationRunnable = new Runnable() {
        public void run() {
            initializeCurrentLocationDataDownloading();}
    };

    private Runnable geocodingRunnable = new Runnable() {
        public void run() {
            geolocalizationProgressDialog.show();
            progressDialogMessageTextView.setText(activity.getString(R.string.looking_for_address_progress_message));
            new GeocodingDownloader(
                    activity,
                    currentLocationCoordinatesDownloader.getLocation(),
                    MainActivityGeolocalizationDownloader.this,
                    progressDialogMessageTextView);
        }
    };
}
