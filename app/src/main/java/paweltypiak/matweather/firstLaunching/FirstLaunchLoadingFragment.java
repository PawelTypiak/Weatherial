package paweltypiak.matweather.firstLaunching;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import paweltypiak.matweather.usefulClasses.DialogInitializer;
import paweltypiak.matweather.MainActivity;
import paweltypiak.matweather.R;
import paweltypiak.matweather.usefulClasses.FavouritesEditor;
import paweltypiak.matweather.usefulClasses.SharedPreferencesModifier;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;
import paweltypiak.matweather.localizationDataDownloading.GeocodingCallback;
import paweltypiak.matweather.localizationDataDownloading.GeocodingDownloader;
import paweltypiak.matweather.localizationDataDownloading.CurrentCoordinatesDownloader;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataDownloader;
import paweltypiak.matweather.weatherDataDownloading.WeatherDownloadCallback;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataInitializer;
import paweltypiak.matweather.jsonHandling.Channel;

public class FirstLaunchLoadingFragment extends Fragment implements WeatherDownloadCallback,GeocodingCallback{

    private int selectedDefeaultLocationOption;
    private String differentLocationName;
    private DialogInitializer dialogInitializer;
    private AlertDialog noWeatherResultsForLocationDialog;
    private AlertDialog weatherResultsForLocationDialog;
    private AlertDialog weatherInternetFailureDialog;
    private AlertDialog geocodingInternetFailureDialog;
    private AlertDialog serviceFailureDialog;
    private AlertDialog geolocalizationFailureDialog;
    private AlertDialog permissionDeniedDialog;
    private AlertDialog geolocalizationMethodsDialog;
    private AlertDialog changeLocationAfterFailureDialog;
    private WeatherDataInitializer dataInitializer;
    private String location;
    private ProgressBar loadingBar;
    private TextView messageTextView;
    private View marginView;
    private SelectLocationAgainListener locationListener;
    private boolean isFirstLaunch;
    private int selectedDefeaultLocalizationMethod;
    private CurrentCoordinatesDownloader currentCoordinatesDownloader;
    private boolean isNextLaunchAfterFailure=false;
    private String changedLocation;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=1;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SelectLocationAgainListener) {
            locationListener = (SelectLocationAgainListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement MyListFragment.OnItemSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getExtras();
        return inflater.inflate(R.layout.first_launch_loading_fragment, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeDialogs();
        getLayoutResources();
        if(isFirstLaunch==true) initializeFirstLaunch();
        else initializeNextLaunch();
    }

    public static FirstLaunchLoadingFragment newInstance(
            Activity activity,
            boolean isFirstLaunch,
            int selectedGeolocalizationMethod,
            int selectedDefeaultLocationOption,
            String differentLocationName) {
        FirstLaunchLoadingFragment loadingFragment = new FirstLaunchLoadingFragment();
        Bundle extras = new Bundle();
        extras.putInt(activity.getString(R.string.extras_selected_geolocalization_method_key),selectedGeolocalizationMethod);
        extras.putInt(activity.getString(R.string.extras_selected_defeault_location_option_key), selectedDefeaultLocationOption);
        extras.putString(activity.getString(R.string.extras_different_location_name_key), differentLocationName);
        extras.putBoolean(activity.getString(R.string.extras_is_first_launch_key),isFirstLaunch);
        loadingFragment.setArguments(extras);
        return loadingFragment;
    }

    private void getExtras(){
        selectedDefeaultLocalizationMethod =getArguments().getInt(getString(R.string.extras_selected_geolocalization_method_key), -1);
        selectedDefeaultLocationOption = getArguments().getInt(getString(R.string.extras_selected_defeault_location_option_key), -1);
        differentLocationName = getArguments().getString(getString(R.string.extras_different_location_name_key), null);
        isFirstLaunch=getArguments().getBoolean(getString(R.string.extras_is_first_launch_key));
    }

    private void getLayoutResources(){
        loadingBar=(ProgressBar)getActivity().findViewById(R.id.first_launch_loading_fragment_progress_circle);
        messageTextView=(TextView)getActivity().findViewById(R.id.first_launch_loading_fragment_message);
        marginView=getActivity().findViewById(R.id.first_launch_loading_fragment_margin_view);
    }

    private void checkPermissions(){
        //permissions for Android 6.0
        if ( ContextCompat.checkSelfPermission( getActivity(), Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED )
        {
            Log.d("permissions", "denied");
            requestPermissions( new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        else {
            Log.d("permissions", "granted");
            startGeolocalization();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startGeolocalization();
                    Log.d("permissions", "granted");
                } else {
                    permissionDeniedDialog.show();
                    Log.d("permissions", "denied");
                }
                return;
            }
        }
    }

    private void setLoadingViewsVisible(boolean isVisible){
        if(isVisible==true){
            UsefulFunctions.setViewVisible(loadingBar);
            UsefulFunctions.setViewVisible(messageTextView);
        }
        else{
            UsefulFunctions.setViewInvisible(loadingBar);
            UsefulFunctions.setViewInvisible(messageTextView);
        }
    }

    private void startGeolocalization(){
        Log.d("geolocalization", "startGeolocalization");
        setLoadingViewsVisible(true);
        currentCoordinatesDownloader =new CurrentCoordinatesDownloader(
                getActivity(),
                this,
                geolocalizationFailureDialog,
                permissionDeniedDialog,
                null,
                messageTextView,
                loadingBar,
                selectedDefeaultLocalizationMethod
        );
    }

    private void initializeFirstLaunch(){
        if(selectedDefeaultLocationOption ==0) {
            checkPermissions();
        }
        else if(selectedDefeaultLocationOption ==1){
            setLoadingViewsVisible(true);
            location=differentLocationName;
            downloadWeatherData(location);
        }
    }

    private void initializeNextLaunch(){
        if(!SharedPreferencesModifier.isDefeaultLocationConstant(getActivity())){
            selectedDefeaultLocalizationMethod =SharedPreferencesModifier.getGeolocalizationMethod(getActivity());
            if(selectedDefeaultLocalizationMethod ==-1){
                geolocalizationMethodsDialog.show();
            }
            else{
                checkPermissions();
            }
        }
        else {
            setLoadingViewsVisible(true);
            location=SharedPreferencesModifier.getDefeaultLocation(getActivity());
            downloadWeatherData(location);
        }
    }
    private void initializeNextLaunchAfterFailure(){
        isNextLaunchAfterFailure=true;
        if(changedLocation==null){
            if(selectedDefeaultLocalizationMethod ==-1){
                setLoadingViewsVisible(false);
                geolocalizationMethodsDialog.show();
            }
            else {
                checkPermissions();
            }
        }
        else{
            setLoadingViewsVisible(true);
            downloadWeatherData(changedLocation);
        }
    }

    private void downloadWeatherData(String location){
        Log.d("weather", "start weather downloading");
        if(isFirstLaunch&& selectedDefeaultLocationOption ==1) messageTextView.setText(getString(R.string.searching_location_progress_message));
        else messageTextView.setText(getString(R.string.downloading_weather_data_progress_message));
        setLoadingViewsVisible(true);
        new WeatherDataDownloader(location,this);
    }

    @Override
    public void weatherServiceSuccess(Channel channel) {
        dataInitializer = new WeatherDataInitializer(channel);
        if(isFirstLaunch && selectedDefeaultLocationOption ==1){
            weatherResultsForLocationDialog =dialogInitializer.initializeWeatherResultsForLocationDialog(0,dataInitializer,loadMainActivityRunnable,showLocationFragmentRunnable,new showExitDialogRunnable(showWeatherResultsForLocationDialogRunnable));
            showDialog(weatherResultsForLocationDialog);
        }
        else loadMainActivityRunnable.run();
    }

    @Override
    public void weatherServiceFailure(int errorCode) {
        if(errorCode==0) {showDialog(weatherInternetFailureDialog);}
        else if(errorCode==1){
            if(isFirstLaunch) {
                showDialog(noWeatherResultsForLocationDialog);
            }
            else {
                if(!SharedPreferencesModifier.isDefeaultLocationConstant(getActivity())||(changedLocation==null&&isNextLaunchAfterFailure==true)){
                    showDialog(noWeatherResultsForLocationDialog);
                }
                else{
                    showDialog(serviceFailureDialog);
                }
            }
        }
    }

    @Override
    public void geocodingServiceSuccess(String location) {
        this.location=location;
        downloadWeatherData(this.location);
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

    private void initializeDialogs(){
        dialogInitializer=new DialogInitializer(getActivity());
        weatherInternetFailureDialog =dialogInitializer.initializeInternetFailureDialog(0, downloadWeatherRunnable, new showExitDialogRunnable(showWeatherInternetFailureDialogRunnable));
        geocodingInternetFailureDialog =dialogInitializer.initializeInternetFailureDialog(0, startGeolocalizationRunnable, new showExitDialogRunnable(showGeocodingInternetFailureDialogRunnable));
        serviceFailureDialog=dialogInitializer.initializeServiceFailureDialog(0,downloadWeatherRunnable,new showExitDialogRunnable(showServiceFailureDialogRunnable));
        if(isFirstLaunch){
            noWeatherResultsForLocationDialog =dialogInitializer.initializeNoWeatherResultsForLocationDialog(0,showLocationFragmentRunnable,new showExitDialogRunnable(showNoWeatherResultsForLocationRunnable));
            geolocalizationFailureDialog =dialogInitializer.initializeGeolocalizationFailureDialog(0,showLocationFragmentRunnable,new showExitDialogRunnable(showGeolocalizationFailureDialogRunnable));
            permissionDeniedDialog=dialogInitializer.initializePermissionDeniedDialog(0,showLocationFragmentRunnable,new showExitDialogRunnable(showPermissionDeniedDialogRunnable));
        }
        else{
            geolocalizationMethodsDialog =dialogInitializer.initializeGeolocalizationMethodsDialog(0,geolocalizationMethodsDialogRunnable);
            noWeatherResultsForLocationDialog =dialogInitializer.initializeNoWeatherResultsForLocationDialog(0, showChangeLocationAfterFailureDialogRunnable,new showExitDialogRunnable(showNoWeatherResultsForLocationRunnable));
            geolocalizationFailureDialog =dialogInitializer.initializeGeolocalizationFailureDialog(0,showChangeLocationAfterFailureDialogRunnable, new showExitDialogRunnable(showGeolocalizationFailureDialogRunnable));
            permissionDeniedDialog =dialogInitializer.initializePermissionDeniedDialog(0,showChangeLocationAfterFailureDialogRunnable, new showExitDialogRunnable(showPermissionDeniedDialogRunnable));
        }
    }

    private Runnable showWeatherInternetFailureDialogRunnable = new Runnable() {
        public void run() {weatherInternetFailureDialog.show();}
    };

    private Runnable showChangeLocationAfterFailureDialogRunnable =new Runnable() {
        @Override
        public void run() {
            changeLocationAfterFailureDialog =dialogInitializer.initializeFavouritesDialog(0, changeLocationAfterFailureRunnable,new showExitDialogRunnable(showChangeLocationAfterFailureDialogRunnable));
            showDialog(changeLocationAfterFailureDialog);
        }

    };

    //runnables for passing methods to dialogs' buttons' onClicks

    private Runnable showGeocodingInternetFailureDialogRunnable = new Runnable() {
        public void run() {geocodingInternetFailureDialog.show();}
    };

    private Runnable showServiceFailureDialogRunnable = new Runnable() {
        public void run() {serviceFailureDialog.show();}
    };

    private Runnable showWeatherResultsForLocationDialogRunnable = new Runnable() {
        public void run() {weatherResultsForLocationDialog.show();}
    };

    private Runnable showGeolocalizationFailureDialogRunnable = new Runnable() {
        public void run() {geolocalizationFailureDialog.show();}
    };

    private Runnable showPermissionDeniedDialogRunnable = new Runnable() {
        public void run() {permissionDeniedDialog.show();}
    };

    private Runnable showNoWeatherResultsForLocationRunnable = new Runnable() {
        public void run() {noWeatherResultsForLocationDialog.show();}
    };

    private Runnable showLocationFragmentRunnable = new Runnable() {
        public void run() {locationListener.showLocationFragment();}
    };

    private class showExitDialogRunnable implements Runnable {
        private Runnable showDialogRunnable;
        public showExitDialogRunnable(Runnable showDialogRunnable) {
            this.showDialogRunnable = showDialogRunnable;
        }
        public void run() {
            AlertDialog exitDialog=dialogInitializer.initializeExitDialog(0, showDialogRunnable);
            exitDialog.show();
        }
    }
    private void showDialog(AlertDialog alertDialog){
        alertDialog.show();
        UsefulFunctions.setViewInvisible(messageTextView);
        UsefulFunctions.setViewGone(loadingBar);
    }

    private Runnable changeLocationAfterFailureRunnable = new Runnable() {
        public void run() {
            int selectedLocationID=FavouritesEditor.getSelectedFavouriteLocationID();
            int numberOfFavourites=FavouritesEditor.getNumberOfFavourites(getActivity());
            if(selectedLocationID==numberOfFavourites) changedLocation=null;
            else changedLocation=FavouritesEditor.getSelectedFavouriteLocationAddress(getActivity());
            initializeNextLaunchAfterFailure();
        }
    };
    private Runnable geolocalizationMethodsDialogRunnable = new Runnable() {
        public void run() {
            if(isNextLaunchAfterFailure) initializeNextLaunchAfterFailure();
            else initializeNextLaunch();
        }
    };

    private Runnable downloadWeatherRunnable = new Runnable() {
        public void run() {downloadWeatherData(location);}
    };

    private Runnable startGeolocalizationRunnable = new Runnable() {
        public void run() {
            new GeocodingDownloader(currentCoordinatesDownloader.getLocation(),FirstLaunchLoadingFragment.this,messageTextView,getActivity());
            setLoadingViewsVisible(true);
        }
    };

    private Runnable loadMainActivityRunnable = new Runnable() {
        public void run() {
            messageTextView.setText(getString(R.string.loading_content_progress_message));
            UsefulFunctions.setViewGone(loadingBar);
            UsefulFunctions.setViewVisible(marginView);
            UsefulFunctions.setViewVisible(messageTextView);
            if(isFirstLaunch) {
                if(selectedDefeaultLocationOption ==0){
                    SharedPreferencesModifier.setGeolocalizationMethod(getActivity(), selectedDefeaultLocalizationMethod);
                    SharedPreferencesModifier.setDefeaultLocationGeolocalization(getActivity());
                }
                else{
                    String city=dataInitializer.getCity();
                    String region=dataInitializer.getRegion();
                    String country=dataInitializer.getCountry();
                    String locationName=city+", "+region+", "+country;
                    SharedPreferencesModifier.setDefeaultLocationConstant(getActivity(),locationName);
                    saveNewFavouritesItem();
                }
                SharedPreferencesModifier.setNextLaunch(getActivity());
            }
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra(getString(R.string.extras_data_initializer_key),dataInitializer);
            startActivity(intent);
            getActivity().finish();
            return;
        }
    };

    private void saveNewFavouritesItem(){
        //saving selected location in favourites
        String currentLocationCity=dataInitializer.getCity();
        String currentLocationRegion=dataInitializer.getRegion();
        String currentLocationCountry=dataInitializer.getCountry();
        String currentLocationAddress=currentLocationCity+", "+currentLocationRegion+", "+currentLocationCountry;
        String header=currentLocationCity;
        String subheader=currentLocationRegion+", "+currentLocationCountry;
        FavouritesEditor.saveNewFavouritesItem(getActivity(),header,subheader,currentLocationAddress);
    }

    public interface SelectLocationAgainListener {void showLocationFragment();}
}
