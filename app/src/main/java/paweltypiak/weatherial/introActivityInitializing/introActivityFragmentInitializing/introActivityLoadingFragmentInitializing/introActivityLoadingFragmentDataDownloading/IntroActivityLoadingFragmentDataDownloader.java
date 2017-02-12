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

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLocationFragmentInitializing.ShowLocationFragmentAgainListener;
import paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.OnRequestLocalizationPermissionsListener;
import paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLoadingFragmentInitializing.introActivityLoadingFragmentDataDownloading.introActivityLoadingFragmentDialogInitializing.IntroActivityLoadingFragmentGeolocalizationMethodsDialogInitializer;
import paweltypiak.weatherial.utils.SharedPreferencesModifier;

public class IntroActivityLoadingFragmentDataDownloader {

    private Activity activity;
    private ShowLocationFragmentAgainListener locationListener;
    private OnRequestLocalizationPermissionsListener permissionsListener;
    private boolean isFirstLaunch;
    private int selectedDefaultLocationOption;
    private int selectedDefaultLocalizationMethod;
    private String differentLocationName;
    private String location;
    private boolean isNextLaunchAfterFailure=false;
    private String changedLocation;
    private IntroActivityLoadingFragmentGeolocalizationDownloader geolocalizationDownloader;
    private IntroActivityLoadingFragmentWeatherDataDownloader weatherDataDownloader;
    private TextView messageTextView;
    private ProgressBar loadingProgressBar;

    public IntroActivityLoadingFragmentDataDownloader(Activity activity,
                                                      ShowLocationFragmentAgainListener locationListener,
                                                      OnRequestLocalizationPermissionsListener permissionsListener,
                                                      boolean isFirstLaunch,
                                                      int selectedDefaultLocalizationMethod,
                                                      int selectedDefaultLocationOption,
                                                      String differentLocationName){
        this.selectedDefaultLocalizationMethod = selectedDefaultLocalizationMethod;
        this.selectedDefaultLocationOption = selectedDefaultLocationOption;
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
        if(selectedDefaultLocationOption ==0) {
            geolocalizationDownloader.initializeCurrentLocationDataDownloading();
        }
        else if(selectedDefaultLocationOption ==1){
            setLoadingViewsVisibility(true);
            location=differentLocationName;
            weatherDataDownloader.downloadWeatherData(location);
        }
    }

    public void initializeNextLaunch(){
        if(!SharedPreferencesModifier.isDefaultLocationConstant(activity)){
            selectedDefaultLocalizationMethod =SharedPreferencesModifier.getGeolocalizationMethod(activity);
            if(selectedDefaultLocalizationMethod ==-1){
                showGeolocalizationMethodsDialog();
            }
            else{
                geolocalizationDownloader.initializeCurrentLocationDataDownloading();
            }
        }
        else {
            setLoadingViewsVisibility(true);
            location=SharedPreferencesModifier.getDefaultLocation(activity);
            weatherDataDownloader.downloadWeatherData(location);
        }
    }

    public void initializeNextLaunchAfterFailure(){
        isNextLaunchAfterFailure=true;
        if(changedLocation==null){
            if(selectedDefaultLocalizationMethod ==-1){
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
            loadingProgressBar.setVisibility(View.VISIBLE);
            messageTextView.setVisibility(View.VISIBLE);
        }
        else{
            loadingProgressBar.setVisibility(View.INVISIBLE);
            messageTextView.setVisibility(View.INVISIBLE);
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

    public int getSelectedDefaultLocationOption() {
        return selectedDefaultLocationOption;
    }

    public int getSelectedDefaultLocalizationMethod() {
        return selectedDefaultLocalizationMethod;
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

    public IntroActivityLoadingFragmentWeatherDataDownloader getWeatherDataDownloader() {return weatherDataDownloader;}

    public TextView getMessageTextView() {
        return messageTextView;
    }

    public ProgressBar getLoadingProgressBar() {
        return loadingProgressBar;
    }
}
