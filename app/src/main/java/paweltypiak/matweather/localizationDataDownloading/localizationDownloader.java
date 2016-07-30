package paweltypiak.matweather.localizationDataDownloading;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import paweltypiak.matweather.UsefulFunctions;

/**
 * Created by Pawcioch on 30.07.2016.
 */
public class LocalizationDownloader implements  ActivityCompat.OnRequestPermissionsResultCallback{
    private LocationManager locationManager;
    private Timer localizationTimer;
    private Activity activity;
    private ProgressBar loadingBar;
    private TextView messageTextView;
    private Location networkLocation;
    private Location gpsLocation;
    private Location location;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=1;
    private boolean isPermissionGranted;
    private GeocodingDownloader geocodingDownloader;
    private AlertDialog localizationFailureDialog;
    private AlertDialog permissionDeniedDialog;
    private GeocodingCallback geocodingCallack;

    public LocalizationDownloader(Activity activity, GeocodingCallback geocodingCallback,AlertDialog localizationFailureDialog, AlertDialog permissionDeniedDialog, TextView messageTextView, ProgressBar loadingBar){
        this.activity=activity;
        this.geocodingCallack=geocodingCallback;
        this.localizationFailureDialog=localizationFailureDialog;
        this.permissionDeniedDialog=permissionDeniedDialog;
        this.messageTextView=messageTextView;
        this.loadingBar=loadingBar;
        isPermissionGranted=checkPermissions();
        if(isPermissionGranted==true) getCurrentLocation();
        else ActivityCompat.requestPermissions( activity, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        Log.d("permissions", "ispermissionsgranted: "+isPermissionGranted);
    }

    private boolean checkPermissions(){
        if ( ContextCompat.checkSelfPermission( activity, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED )
           return false;
        else return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                    Log.d("permissions", "granted");
                } else {
                    showDialog(permissionDeniedDialog);
                    Log.d("permissions", "denied");
                }
                return;
            }
        }
    }


    private void getCurrentLocation(){
        boolean gpsEnabled=false;
        boolean networkEnabled=false;
        if(locationManager==null) locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        try{
            gpsEnabled=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            networkEnabled=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }catch(Exception ex){}

        if(!gpsEnabled && !networkEnabled) showDialog(localizationFailureDialog);
        try {
            if (gpsEnabled) {
                Log.d("dostepnosc:", "gps");
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
            }
            if (networkEnabled) {
                Log.d("dostepnosc:", "net");
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListenerNetwork);
            }
        }catch (SecurityException exception){
            showDialog(permissionDeniedDialog);
            Log.d("permissions", ""+exception);
        }
        Log.d("timer", "zaczynam ");
        localizationTimer = new Timer();
        localizationTimer.schedule(new locationTimerTask(), 30000);
    }

    private LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
            localizationTimer.cancel();
            LocalizationDownloader.this.location=location;
            try{
                locationManager.removeUpdates(this);
            }catch (SecurityException exception){
                showDialog(permissionDeniedDialog);
            }
            Log.d("longitude gps", ""+location.getLatitude());
            geocodingDownloader=new GeocodingDownloader(location,geocodingCallack);
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    private LocationListener locationListenerNetwork = new LocationListener() {
        public void onLocationChanged(Location location) {
            networkLocation=location;
            try {
                locationManager.removeUpdates(this);
            }catch (SecurityException exception){
                showDialog(permissionDeniedDialog);
            }
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    private class locationTimerTask extends TimerTask {
        @Override
        public void run() {
            try {
                locationManager.removeUpdates(locationListenerGps);
                locationManager.removeUpdates(locationListenerNetwork);
            }catch (SecurityException exception){
                showDialog(permissionDeniedDialog);
            }
            location=networkLocation;
            if(networkLocation==null) {
                activity.runOnUiThread(new Runnable() {
                    public void run() {
                        showDialog(localizationFailureDialog);
                    }
                });

            }
        }
    }

    private void showDialog(AlertDialog alertDialog){
        alertDialog.show();
        UsefulFunctions.setViewInvisible(messageTextView);
        UsefulFunctions.setViewInvisible(loadingBar);
    }
}
