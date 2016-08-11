package paweltypiak.matweather.localizationDataDownloading;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import paweltypiak.matweather.usefulClasses.DialogInitializer;
import paweltypiak.matweather.R;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

public class LocalizationDownloader implements  ActivityCompat.OnRequestPermissionsResultCallback{
    private LocationManager locationManager;
    private Activity activity;
    private ProgressBar loadingBar;
    private TextView messageTextView;
    private Location location;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=1;
    private boolean isPermissionGranted;
    private GeocodingDownloader geocodingDownloader;
    private AlertDialog localizationFailureDialog;
    private AlertDialog permissionDeniedDialog;
    private AlertDialog providerUnavailableDialog;
    private GeocodingCallback geocodingCallack;
    private boolean gpsEnabled=false;
    private boolean networkEnabled=false;
    int choosenLocalizationOption;

    public LocalizationDownloader(Activity activity,
                                  GeocodingCallback geocodingCallback,
                                  AlertDialog localizationFailureDialog,
                                  AlertDialog permissionDeniedDialog,
                                  TextView messageTextView,
                                  ProgressBar loadingBar,
                                  int choosenLocalizationOption){
        this.activity=activity;
        this.geocodingCallack=geocodingCallback;
        this.localizationFailureDialog=localizationFailureDialog;
        this.permissionDeniedDialog=permissionDeniedDialog;
        this.messageTextView=messageTextView;
        this.loadingBar=loadingBar;
        this.choosenLocalizationOption=choosenLocalizationOption;
        isPermissionGranted=checkPermissions();
        if(isPermissionGranted==true) getCurrentLocation();
        else ActivityCompat.requestPermissions( activity, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
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
        messageTextView.setText(activity.getString(R.string.first_launch_layout_loading_header_waiting_for_localization));
        if(locationManager==null) locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        Criteria locationCriteria = new Criteria();
        locationCriteria.setAccuracy(Criteria.ACCURACY_MEDIUM);
        try{
            gpsEnabled=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            networkEnabled=locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        }catch(Exception ex){
            showDialog(localizationFailureDialog);
        }
        if(!gpsEnabled && !networkEnabled) showDialog(localizationFailureDialog);
        else{
            if (choosenLocalizationOption==1) {
                if(gpsEnabled){
                    Log.d("dostepnosc:", "gps");
                    try {
                        locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null);
                    }catch (SecurityException exception){
                        showDialog(permissionDeniedDialog);
                        Log.d("permissions", ""+exception);
                    }
                }
                else{
                    Log.d("niedostepnosc:", "gps");
                    DialogInitializer dialogInitializer=new DialogInitializer(activity);
                    providerUnavailableDialog=dialogInitializer.initializeProviderUnavailableDialog(1,gpsUnavailableRunnable);
                    providerUnavailableDialog.show();
                }
            }
            else if (choosenLocalizationOption==2) {
                if(networkEnabled){
                    Log.d("dostepnosc:", "net");
                    try {
                        locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER, locationListener, null);
                    }catch (SecurityException exception){
                        showDialog(permissionDeniedDialog);
                        Log.d("permissions", ""+exception);
                    }
                } else{
                    Log.d("niedostepnosc:", "net");
                    DialogInitializer dialogInitializer=new DialogInitializer(activity);
                    providerUnavailableDialog=dialogInitializer.initializeProviderUnavailableDialog(2,networkUnavailableRunnable);
                    providerUnavailableDialog.show();
                }
            }
        }
    }

    private LocationListener locationListener = new LocationListener() {
        public void onLocationChanged(Location location) {
            LocalizationDownloader.this.location=location;
            Log.d("longitude", ""+location.getLatitude());
            Log.d("latitude", ""+location.getLatitude());
            geocodingDownloader=new GeocodingDownloader(location,geocodingCallack,messageTextView,activity);
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    /*private int getLocalizationOption(){
        SharedPreferences sharedPreferences=UsefulFunctions.getSharedPreferences(activity);
        int localizationOption=sharedPreferences.getInt(activity.getString(R.string.shared_preferences_localization_option_key),0);
        return localizationOption;
    }*/

    public Location getLocation() {
        return location;
    }

    private Runnable networkUnavailableRunnable=new Runnable() {
        @Override
        public void run() {
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
            try{
                locationManager.requestSingleUpdate(LocationManager.NETWORK_PROVIDER,locationListener,null);

            }catch (SecurityException exception){
                showDialog(permissionDeniedDialog);
            }
        }
    };

    private void showDialog(AlertDialog alertDialog){
        alertDialog.show();
        if(messageTextView!=null) UsefulFunctions.setViewInvisible(messageTextView);
        if(loadingBar!=null) UsefulFunctions.setViewInvisible(loadingBar);
    }
}
