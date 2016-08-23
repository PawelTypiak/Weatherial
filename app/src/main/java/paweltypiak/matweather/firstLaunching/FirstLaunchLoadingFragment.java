package paweltypiak.matweather.firstLaunching;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
    private int choosenDefeaultLocationOption;
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
    private ChooseLocationAgainListener locationListener;
    private boolean isFirstLaunch;
    private int choosenDefeaultLocalizationMethod;
    private CurrentCoordinatesDownloader currentCoordinatesDownloader;
    private boolean isNextLaunchAfterFailure=false;
    private String changedLocation;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ChooseLocationAgainListener) {
            locationListener = (ChooseLocationAgainListener) context;
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

    public static FirstLaunchLoadingFragment newInstance(Activity activity,boolean isFirstLaunch, int choosenGeolocalizationMethod, int choosenDefeaultLocationOption, String differentLocationName) {
        FirstLaunchLoadingFragment loadingFragment = new FirstLaunchLoadingFragment();
        Bundle extras = new Bundle();
        extras.putInt(activity.getString(R.string.extras_choosen_geolocalization_method_key),choosenGeolocalizationMethod);
        extras.putInt(activity.getString(R.string.extras_choosen_defeault_location_option_key), choosenDefeaultLocationOption);
        extras.putString(activity.getString(R.string.extras_different_location_name_key), differentLocationName);
        extras.putBoolean(activity.getString(R.string.extras_is_first_launch_key),isFirstLaunch);
        loadingFragment.setArguments(extras);
        return loadingFragment;
    }

    private void getExtras(){
        choosenDefeaultLocalizationMethod =getArguments().getInt(getString(R.string.extras_choosen_geolocalization_method_key), -1);
        choosenDefeaultLocationOption = getArguments().getInt(getString(R.string.extras_choosen_defeault_location_option_key), -1);
        differentLocationName = getArguments().getString(getString(R.string.extras_different_location_name_key), null);
        isFirstLaunch=getArguments().getBoolean(getString(R.string.extras_is_first_launch_key));
    }

    private void getLayoutResources(){
        loadingBar=(ProgressBar)getActivity().findViewById(R.id.first_launch_loading_fragment_progress_circle);
        messageTextView=(TextView)getActivity().findViewById(R.id.first_launch_loading_fragment_message);
        marginView=getActivity().findViewById(R.id.first_launch_loading_fragment_margin_view);
    }

    private void startGeolocalization(){
        Log.d("geolocalization", "startGeolocalization");
        UsefulFunctions.setViewVisible(loadingBar);
        UsefulFunctions.setViewVisible(messageTextView);
        currentCoordinatesDownloader =new CurrentCoordinatesDownloader(
                getActivity(),
                this,
                geolocalizationFailureDialog,
                permissionDeniedDialog,
                null,
                messageTextView,
                loadingBar,
                choosenDefeaultLocalizationMethod
        );
    }

    private void initializeFirstLaunch(){
        if(choosenDefeaultLocationOption ==0) {
            startGeolocalization();
        }
        else if(choosenDefeaultLocationOption==1){
            location=differentLocationName;
            downloadWeatherData(location);
        }
    }

    private void initializeNextLaunch(){
        if(!SharedPreferencesModifier.isDefeaultLocationConstant(getActivity())){
            Log.d("launch", "geolokalizacja");
            choosenDefeaultLocalizationMethod =SharedPreferencesModifier.getGeolocalizationMethod(getActivity());
            if(choosenDefeaultLocalizationMethod ==-1){
                UsefulFunctions.setViewInvisible(loadingBar);
                UsefulFunctions.setViewInvisible(messageTextView);
                geolocalizationMethodsDialog.show();
            }
            else{
                startGeolocalization();
            }
        }
        else {
            Log.d("launch", "stala");
            location=SharedPreferencesModifier.getDefeaultLocation(getActivity());
            downloadWeatherData(location);
        }
    }
    private void initializeNextLaunchAfterFailure(){
        isNextLaunchAfterFailure=true;
        if(changedLocation==null){
            if(choosenDefeaultLocalizationMethod ==-1){
                UsefulFunctions.setViewInvisible(loadingBar);
                UsefulFunctions.setViewInvisible(messageTextView);
                geolocalizationMethodsDialog.show();
            }
            else {
                startGeolocalization();
            }
        }
        else{
            downloadWeatherData(changedLocation);
        }
    }

    private void downloadWeatherData(String location){
        Log.d("weather", "start weather downloading");
        UsefulFunctions.setViewVisible(loadingBar);
        if(isFirstLaunch&& choosenDefeaultLocationOption ==1) messageTextView.setText(getString(R.string.searching_location_progress_message));
        else messageTextView.setText(getString(R.string.downloading_weather_data_progress_message));
        UsefulFunctions.setViewVisible(messageTextView);
        new WeatherDataDownloader(location,this);
    }

    @Override
    public void weatherServiceSuccess(Channel channel) {
        dataInitializer = new WeatherDataInitializer(channel);
        if(isFirstLaunch && choosenDefeaultLocationOption ==1){
            weatherResultsForLocationDialog =dialogInitializer.initializeWeatherResultsForLocationDialog(0,dataInitializer,loadMainActivityRunnable,showLocationFragmentRunnable,new showExitDialogRunnable(showWeatherResultsForLocationDialogRunnable));
            showDialog(weatherResultsForLocationDialog);
        }
        else loadMainActivityRunnable.run();
    }

    @Override
    public void weatherServiceFailure(int errorCode) {
        if(errorCode==1) {showDialog(weatherInternetFailureDialog);}
        else{
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
        if(errorCode==1){
            showDialog(geocodingInternetFailureDialog);
        }
        else{
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
            geolocalizationMethodsDialog =dialogInitializer.initializeGeolocalizationMethodsDialog(geolocalizationMethodsDialogRunnable);
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
            int choosenLocationID=FavouritesEditor.getChoosenFavouriteLocationID();
            int numberOfFavourites=FavouritesEditor.getNumberOfFavourites(getActivity());
            if(choosenLocationID==numberOfFavourites) changedLocation=null;
            else changedLocation=FavouritesEditor.getChoosenFavouriteLocationAddress(getActivity());
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
            UsefulFunctions.setViewVisible(messageTextView);
            UsefulFunctions.setViewVisible(loadingBar);
        }
    };

    private Runnable loadMainActivityRunnable = new Runnable() {
        public void run() {
            messageTextView.setText(getString(R.string.loading_content_progress_message));
            UsefulFunctions.setViewGone(loadingBar);
            UsefulFunctions.setViewVisible(marginView);
            UsefulFunctions.setViewVisible(messageTextView);
            if(isFirstLaunch) {
                if(choosenDefeaultLocationOption ==0){
                    SharedPreferencesModifier.setGeolocalizationMethod(getActivity(), choosenDefeaultLocalizationMethod);
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
        String currentLocationCity=dataInitializer.getCity();
        String currentLocationRegion=dataInitializer.getRegion();
        String currentLocationCountry=dataInitializer.getCountry();
        String currentLocationAddress=currentLocationCity+", "+currentLocationRegion+", "+currentLocationCountry;
        String header=currentLocationCity;
        String subheader=currentLocationRegion+", "+currentLocationCountry;
        String currentLocationLatitude= Double.toString(dataInitializer.getLatitude());
        String currentLocationLongitude=Double.toString(dataInitializer.getLongitude());
        String currentLocationCoordinates=currentLocationLatitude+"%"+currentLocationLongitude;
        FavouritesEditor.saveNewFavouritesItem(getActivity(),header,subheader,currentLocationAddress,currentLocationCoordinates);
    }

    public interface ChooseLocationAgainListener {void showLocationFragment();}
}
