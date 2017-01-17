package paweltypiak.matweather.localizationDataDownloading;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import paweltypiak.matweather.dialogsInitializing.GeolocalizationProviderUnavailableDialogInitializer;
import paweltypiak.matweather.R;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

public class CurrentCoordinatesDownloader {

    private LocationManager locationManager;
    private Activity activity;
    private ProgressBar loadingBar;
    private TextView messageTextView;
    private Location location;
    private AlertDialog geolocalizationFailureDialog;
    private AlertDialog permissionDeniedDialog;
    private AlertDialog providerUnavailableDialog;
    private AlertDialog progressDialog;
    private GeocodingCallback geocodingCallack;
    private boolean gpsEnabled=false;
    private boolean networkEnabled=false;
    int geolocalizationMethod;

    public CurrentCoordinatesDownloader(Activity activity,
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
        getCurrentLocation();
    }

    private void getCurrentLocation(){
        messageTextView.setText(activity.getString(R.string.waiting_for_localization_progress_message));
        if(locationManager==null) locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        Criteria locationCriteria = new Criteria();
        locationCriteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
        try{
            gpsEnabled=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            networkEnabled=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }catch(Exception ex){
            showDialog(geolocalizationFailureDialog);
        }
        if(!gpsEnabled && !networkEnabled) {
            Log.d("provider", "provider unavailable");
            showDialog(geolocalizationFailureDialog);
        }
        else{
            if (geolocalizationMethod ==0) {
                //selected provider is GPS
                if(gpsEnabled){
                    Log.d("provider", "gps");
                    try {
                        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);
                    }catch (SecurityException exception){
                        showDialog(permissionDeniedDialog);
                        Log.d("permissions", ""+exception);
                    }
                }
                else{
                    Log.d("provider", "gps unavailable");
                    /*DialogInitializer dialogInitializer=new DialogInitializer(activity);
                    providerUnavailableDialog=dialogInitializer.initializeProviderUnavailableDialog(0,gpsUnavailableRunnable);*/
                    providerUnavailableDialog
                            = GeolocalizationProviderUnavailableDialogInitializer.initializeGolocalizationProviderUnavailableDialog(
                            activity,0,gpsUnavailableRunnable
                    );
                    showDialog(providerUnavailableDialog);
                }
            }
            else if (geolocalizationMethod ==1) {
                //selected provider is network
                if(networkEnabled){
                    Log.d("provider:", "network");
                    try {
                        locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener, null);
                    }catch (SecurityException exception){
                        showDialog(permissionDeniedDialog);
                        Log.d("permissions", ""+exception);
                    }
                } else{
                    Log.d("provider:", "network unavailable");
                   /* DialogInitializer dialogInitializer=new DialogInitializer(activity);
                    providerUnavailableDialog=dialogInitializer.initializeProviderUnavailableDialog(1,networkUnavailableRunnable);*/
                    providerUnavailableDialog
                            = GeolocalizationProviderUnavailableDialogInitializer.initializeGolocalizationProviderUnavailableDialog(
                            activity,1,networkUnavailableRunnable
                    );

                    showDialog(providerUnavailableDialog);
                }
            }
        }
    }

    private LocationListener locationListener = new LocationListener() {
        //listening for geolocalization result
        public void onLocationChanged(Location location) {
            CurrentCoordinatesDownloader.this.location=location;
            Log.d("coordinates", "longitude: "+location.getLongitude());
            Log.d("coordinates", "latitude: "+location.getLatitude());
            new GeocodingDownloader(location,geocodingCallack,messageTextView,activity);
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

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

    private void showDialog(AlertDialog alertDialog){
        //set progress bar and text invisible when alertDialog.show()
        alertDialog.show();
        if(progressDialog!=null) progressDialog.dismiss();
        else{
            if(loadingBar!=null) {
                UsefulFunctions.setViewInvisible(loadingBar);
                UsefulFunctions.setViewInvisible(messageTextView);
            }
        }
    }

    public Location getLocation() {
        return location;
    }
}
