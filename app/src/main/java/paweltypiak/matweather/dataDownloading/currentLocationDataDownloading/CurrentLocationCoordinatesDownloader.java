package paweltypiak.matweather.dataDownloading.currentLocationDataDownloading;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import paweltypiak.matweather.dialogsInitializing.dialogInitializers.GeolocalizationProviderUnavailableDialogInitializer;
import paweltypiak.matweather.R;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

public class CurrentLocationCoordinatesDownloader {

    private Activity activity;
    private LocationManager locationManager;
    private GeocodingCallback geocodingCallack;
    private AlertDialog geolocalizationFailureDialog;
    private AlertDialog permissionDeniedDialog;
    private AlertDialog providerUnavailableDialog;
    private AlertDialog progressDialog;
    private TextView messageTextView;
    private ProgressBar loadingBar;
    private int geolocalizationMethod;
    private boolean gpsEnabled=false;
    private boolean networkEnabled=false;
    private Location location;

    public CurrentLocationCoordinatesDownloader(Activity activity,
                                                GeocodingCallback geocodingCallback,
                                                AlertDialog geolocalizationFailureDialog,
                                                AlertDialog permissionDeniedDialog,
                                                AlertDialog progressDialog,
                                                TextView messageTextView,
                                                ProgressBar loadingBar,
                                                int geolocalizationMethod){
        this.activity=activity;
        this.geocodingCallack=geocodingCallback;
        this.geolocalizationFailureDialog =geolocalizationFailureDialog;
        this.permissionDeniedDialog=permissionDeniedDialog;
        this.progressDialog=progressDialog;
        this.messageTextView=messageTextView;
        this.loadingBar=loadingBar;
        this.geolocalizationMethod =geolocalizationMethod;
        initializeCurrentLocationCoordinatesDownloading();
    }

    private void initializeCurrentLocationCoordinatesDownloading(){
        messageTextView.setText(activity.getString(R.string.waiting_for_localization_progress_message));
        if(locationManager==null) locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        Criteria locationCriteria = new Criteria();
        locationCriteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
        getProvidersAvaibility();
        if(!gpsEnabled && !networkEnabled) {
            Log.d("provider", "provider unavailable");
            showDialog(geolocalizationFailureDialog);
        }
        else{
            if (geolocalizationMethod ==0) {
                //selected provider is GPS
                initializeCurrentCoordinatesDownloadingForGpsProvider();
            }
            else if (geolocalizationMethod ==1) {
                //selected provider is network
               initializeCurrentCoordinatesDownloadingForNetworkProvider();
            }
        }
    }

    private void getProvidersAvaibility(){
        try{
            gpsEnabled=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            networkEnabled=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }catch(Exception ex){
            showDialog(geolocalizationFailureDialog);
        }
    }

    private void initializeCurrentCoordinatesDownloadingForGpsProvider(){
        if(gpsEnabled){
            requestLocationUpdateForGpsProvider();
        }
        else{
           showGpsProviderUnavailableDialog();
        }
    }

    private void requestLocationUpdateForGpsProvider(){
        Log.d("provider", "gps");
        try {
            locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);
        }catch (SecurityException exception){
            showDialog(permissionDeniedDialog);
            Log.d("permissions", ""+exception);
        }
    }

    private void showGpsProviderUnavailableDialog(){
        Log.d("provider", "gps unavailable");
        providerUnavailableDialog
                = GeolocalizationProviderUnavailableDialogInitializer.getGeolocalizationProviderUnavailableDialog(
                activity,0,gpsUnavailableRunnable
        );
        showDialog(providerUnavailableDialog);
    }

    private Runnable gpsUnavailableRunnable=new Runnable() {
        @Override
        public void run() {
            //gps provider unavailable
            try{
                locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER,locationListener,null);

            }catch (SecurityException exception){
                showDialog(permissionDeniedDialog);
            }
        }
    };

    private void initializeCurrentCoordinatesDownloadingForNetworkProvider(){
        if(networkEnabled){
           requestLocationUpdateForNetworkProvider();
        } else{
           showNetworkProviderUnavailableDialog();
        }
    }

    private void requestLocationUpdateForNetworkProvider(){
        Log.d("provider:", "network");
        try {
            locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener, null);
        }catch (SecurityException exception){
            showDialog(permissionDeniedDialog);
            Log.d("permissions", ""+exception);
        }
    }

    private void showNetworkProviderUnavailableDialog(){
        Log.d("provider:", "network unavailable");
        providerUnavailableDialog
                = GeolocalizationProviderUnavailableDialogInitializer.getGeolocalizationProviderUnavailableDialog(
                activity,1,networkUnavailableRunnable
        );

        showDialog(providerUnavailableDialog);
    }

    private Runnable networkUnavailableRunnable=new Runnable() {
        @Override
        public void run() {
            //network provider unavailable
            try{
                locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER,locationListener,null);
            }catch (SecurityException exception){
                showDialog(permissionDeniedDialog);
            }
        }
    };

    private void showDialog(AlertDialog alertDialog){
        //set progress bar and text invisible when alertDialog.show()
        alertDialog.show();
        if(progressDialog!=null) progressDialog.dismiss();
        else{
            if(loadingBar!=null) {
                loadingBar.setVisibility(View.INVISIBLE);
                messageTextView.setVisibility(View.INVISIBLE);
            }
        }
    }

    private LocationListener locationListener = new LocationListener() {
        //listening for geolocalization result
        public void onLocationChanged(Location location) {
            CurrentLocationCoordinatesDownloader.this.location=location;
            Log.d("coordinates", "longitude: "+location.getLongitude());
            Log.d("coordinates", "latitude: "+location.getLatitude());
            new GeocodingDownloader(activity,location,geocodingCallack,messageTextView);
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider){}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    public Location getLocation() {
        return location;
    }
}
