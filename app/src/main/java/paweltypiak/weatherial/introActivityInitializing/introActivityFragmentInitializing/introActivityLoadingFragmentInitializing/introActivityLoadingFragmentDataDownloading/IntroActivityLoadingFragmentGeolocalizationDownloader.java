/*******************************************************************************
 * Copyright 2017 Paweł Typiak
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading;

import android.Manifest;
import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.introActivityLoadingFragmentDialogInitializing.IntroActivityLoadingFragmentGeocodingInternetFailureInitializer;
import paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.introActivityLoadingFragmentDialogInitializing.IntroActivityLoadingFragmentGeolocalizationFailureInitializer;
import paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.introActivityLoadingFragmentDialogInitializing.IntroActivityLoadingFragmentPermissionsDeniedDialogInitializer;
import paweltypiak.weatherial.dataDownloading.currentLocationDataDownloading.CurrentLocationCoordinatesDownloader;
import paweltypiak.weatherial.dataDownloading.currentLocationDataDownloading.GeocodingCallback;
import paweltypiak.weatherial.dataDownloading.currentLocationDataDownloading.GeocodingDownloader;

public class IntroActivityLoadingFragmentGeolocalizationDownloader
        implements GeocodingCallback {

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
            setLoadingViewsVisibility(false);
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
                dataDownloader.getSelectedDefaultLocalizationMethod()
        );
    }

    public void initializeGeocodingDownloading(){
        new GeocodingDownloader(
                activity,
                currentLocationCoordinatesDownloader.getLocation(),
                this,
                dataDownloader.getMessageTextView()
                );
        setLoadingViewsVisibility(true);
    }

    private void setLoadingViewsVisibility(boolean isVisible){
        dataDownloader.setLoadingViewsVisibility(isVisible);
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
        setLoadingViewsVisibility(false);
    }
}
