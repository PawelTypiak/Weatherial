package paweltypiak.matweather;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import paweltypiak.matweather.mainActivityLayoutInitializing.MainActivityLayoutInitializer;
import paweltypiak.matweather.mainActivityLayoutInitializing.LayoutUpdating.OnTimeChangeLayoutUpdater;
import paweltypiak.matweather.mainActivityLayoutInitializing.WeatherLayoutInitializing.swipeRefreshLayoutInitializing.onRefreshInitializing.OnRefreshInitializer;
import paweltypiak.matweather.mainActivityLayoutInitializing.appBarInitializing.appBarButtonsInitializing.NavigationDrawerInitializer;
import paweltypiak.matweather.localizationDataDownloading.GeocodingCallback;
import paweltypiak.matweather.localizationDataDownloading.GeocodingDownloader;
import paweltypiak.matweather.localizationDataDownloading.CurrentCoordinatesDownloader;
import paweltypiak.matweather.settings.Settings;
import paweltypiak.matweather.usefulClasses.DialogInitializer;
import paweltypiak.matweather.usefulClasses.SharedPreferencesModifier;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;
import paweltypiak.matweather.mainActivityLayoutInitializing.LayoutUpdating.OnWeatherDataChangeLayoutUpdater;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataDownloader;
import paweltypiak.matweather.weatherDataDownloading.WeatherDownloadCallback;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataParser;
import paweltypiak.matweather.jsonHandling.Channel;

public class MainActivity extends AppCompatActivity implements
        WeatherDownloadCallback,
        GeocodingCallback,
        NavigationDrawerInitializer.onStartGeolocalizationListener,
        OnRefreshInitializer.OnRefreshListener {

    private WeatherDataParser weatherDataParser;
    private AlertDialog geocodingInternetFailureDialog;
    private AlertDialog weatherGeolocalizationInternetFailureDialog;
    private AlertDialog exitDialog;
    private AlertDialog geolocalizationProgressDialog;
    private AlertDialog coordinatesDownloadFailureDialog;
    private AlertDialog permissionDeniedDialog;
    private AlertDialog noWeatherResultsForLocation;
    private AlertDialog geocodingServiceFailureDialog;
    private DialogInitializer dialogInitializer;
    private String geocodingLocation;
    private CurrentCoordinatesDownloader currentCoordinatesDownloader;
    private TextView geolocalizationProgressMessageTextView;
    private int downloadMode;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=1;
    private MainActivityLayoutInitializer mainActivityLayoutInitializer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeLayout();
        loadInitialWeatherData(savedInstanceState);
        loadDefeaultLocation();
    }

    private void initializeLayout(){
        setContentView(R.layout.activity_main);
        initializeDialogs();
        mainActivityLayoutInitializer =new MainActivityLayoutInitializer(this,dialogInitializer);
    }

    private void initializeDialogs(){
        dialogInitializer=new DialogInitializer(this);
        exitDialog=dialogInitializer.initializeExitDialog(1,null);
        geolocalizationProgressDialog=dialogInitializer.initializeProgressDialog(getString(R.string.waiting_for_localization_progress_message));
        noWeatherResultsForLocation =dialogInitializer.initializeNoWeatherResultsForLocationDialog(2,startGeolocalizationRunnable,null);
        geocodingInternetFailureDialog=dialogInitializer.initializeInternetFailureDialog(1,geocodingRunnable,null);
        weatherGeolocalizationInternetFailureDialog=dialogInitializer.initializeInternetFailureDialog(1, downloadWeatherDataAfterGeocodingFailureRunnable,null);
        geocodingServiceFailureDialog=dialogInitializer.initializeServiceFailureDialog(1,startGeolocalizationRunnable,null);
        permissionDeniedDialog=dialogInitializer.initializePermissionDeniedDialog(1,startGeolocalizationRunnable,null);
        coordinatesDownloadFailureDialog =dialogInitializer.initializeGeolocalizationFailureDialog(1,startGeolocalizationRunnable,null);
    }

    public MainActivityLayoutInitializer getMainActivityLayoutInitializer() {
        return mainActivityLayoutInitializer;
    }

    private void loadInitialWeatherData(Bundle savedInstanceState){
        //loading initial weather data
        if(savedInstanceState==null){
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                //after launch
                weatherDataParser =extras.getParcelable(getString(R.string.extras_data_initializer_key));
                Log.d("initial_weather_data", "from extras: "+ weatherDataParser.getCity());
            }
        }
        else {
            //after recreate
            weatherDataParser =  savedInstanceState.getParcelable(getString(R.string.extras_data_initializer_key));
            Log.d("initial_weather_data", "from savedInstanceState: "+ weatherDataParser.getCity());
        }
    }

    private void loadDefeaultLocation(){
        //delivering information if defeault location is constant, or is from geolocalization
        boolean isDefeaultLocationConstant=SharedPreferencesModifier.isDefeaultLocationConstant(this);
        if(isDefeaultLocationConstant) {
            //UsefulFunctions.updateLayoutData(this, weatherDataParser,true,false);
            mainActivityLayoutInitializer.updateLayoutOnWeatherDataChange(this,weatherDataParser,true,false);
        }
        else{
            //UsefulFunctions.updateLayoutData(this, weatherDataParser,true,true);
            mainActivityLayoutInitializer.updateLayoutOnWeatherDataChange(this,weatherDataParser,true,true);
        }
    }

    public void downloadWeatherData(String location) {
        new WeatherDataDownloader(location,this);
    }

    /////////////////////////////////////////WEATHER CALLBACK////////////////////////////////////
    @Override
    public void weatherServiceSuccess(Channel channel) {
        weatherDataParser = new WeatherDataParser(channel);
        if(downloadMode==0){
            //weather service success for geolocalization
            onWeatherServiceSuccessAfterGeolocalization();
        }
        else{
            onWeatherSuccessAfterRefresh(weatherDataParser);
        }
    }

    private void onWeatherServiceSuccessAfterGeolocalization(){
        //UsefulFunctions.updateLayoutData(this, weatherDataParser,true,true);
        mainActivityLayoutInitializer.updateLayoutOnWeatherDataChange(this, weatherDataParser,true,true);
        geolocalizationProgressDialog.dismiss();
    }

    @Override
    public void weatherServiceFailure(int errorCode) {
        if(downloadMode==0){
            //weather service failure for geolocalization
            geolocalizationProgressDialog.dismiss();
            if(errorCode==0) {
                weatherGeolocalizationInternetFailureDialog.show();
            }
            else if(errorCode==1){
                noWeatherResultsForLocation.show();
            }
        }
        else{
            //weather service failure for refreshing
            onWeatherFailureAfterRefresh(errorCode);

        }
    }

    /////////////////////////////////////////GEOCODING CALLBACK////////////////////////////////////

    @Override
    public void geocodingServiceSuccess(String location) {
        geolocalizationProgressMessageTextView.setText(getString(R.string.downloading_weather_data_progress_message));
        geocodingLocation=location;
        downloadWeatherData(geocodingLocation);
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

    public NavigationDrawerInitializer.onStartGeolocalizationListener getGeolocalizationListener(){
        return  this;
    }

    @Override
    public void startCheckingPermissions(){
        checkGeolocalizationPermissions();
    }

    private void checkGeolocalizationPermissions(){
        //permissions for Android 6.0
        if ( ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED )
        {
            ActivityCompat.requestPermissions(this, new String[] {  android.Manifest.permission.ACCESS_FINE_LOCATION  },
                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        else {
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

    private void startGeolocalization(){
        downloadMode=0;
        geolocalizationProgressDialog.show();
        if(geolocalizationProgressMessageTextView==null)geolocalizationProgressMessageTextView=(TextView)geolocalizationProgressDialog.findViewById(R.id.progress_dialog_message_text);
        int geolocalizationMethod=SharedPreferencesModifier.getGeolocalizationMethod(this);
        currentCoordinatesDownloader =new CurrentCoordinatesDownloader(
                this,
                this,
                coordinatesDownloadFailureDialog,
                permissionDeniedDialog,
                geolocalizationProgressDialog,
                geolocalizationProgressMessageTextView,
                null,
                geolocalizationMethod
        );
    }

    private Runnable geocodingRunnable = new Runnable() {
        public void run() {
            geolocalizationProgressDialog.show();
            geolocalizationProgressMessageTextView.setText(getString(R.string.looking_for_address_progress_message));
            new GeocodingDownloader(currentCoordinatesDownloader.getLocation(),MainActivity.this,geolocalizationProgressMessageTextView,MainActivity.this);
        }
    };

    Runnable startGeolocalizationRunnable = new Runnable() {
        public void run() {checkGeolocalizationPermissions();}
    };

    Runnable downloadWeatherDataAfterGeocodingFailureRunnable = new Runnable() {
        public void run() {downloadWeatherData(geocodingLocation);}
    };

    /////////////////////////////////////////REFRESH/////////////////////////////////////////

    public OnRefreshInitializer.OnRefreshListener getOnRefreshListener(){
        return this;
    }

    @Override
    public void downloadWeatherDataOnRefresh(){
        downloadMode=1;
        String currentLocation[]=UsefulFunctions.getCurrentLocationAddress();
        downloadWeatherData(currentLocation[0]+", "+currentLocation[1]);
    }

    private void onWeatherSuccessAfterRefresh(WeatherDataParser weatherDataParser){
        mainActivityLayoutInitializer.getWeatherLayoutInitializer()
                .getSwipeRefreshLayoutInitializer()
                .getOnRefreshInitializer()
                .onWeatherSuccessAfterRefresh(this, weatherDataParser);
    }

    private void onWeatherFailureAfterRefresh(int errorCode){
        mainActivityLayoutInitializer.getWeatherLayoutInitializer()
                .getSwipeRefreshLayoutInitializer()
                .getOnRefreshInitializer()
                .onWeatherFailureAfterRefresh(errorCode);
    }

    @Override
    public void onBackPressed() {
        boolean isDrawerLayoutOpened= mainActivityLayoutInitializer.getAppBarLayoutInitializer()
                .getAppBarLayoutButtonsInitializer()
                .getNavigationDrawerInitializer()
                .closeDrawerLayout();
        if(isDrawerLayoutOpened==false){
            exitDialog.show();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent e) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            mainActivityLayoutInitializer.getAppBarLayoutInitializer()
                    .getAppBarLayoutButtonsInitializer()
                    .getNavigationDrawerInitializer()
                    .openDrawerLayout();
        }
        return super.onKeyDown(keyCode, e);
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        //saving current weather information for recreate
        super.onSaveInstanceState(state);
        WeatherDataParser currentWeatherDataParser = OnWeatherDataChangeLayoutUpdater.getCurrentWeatherDataParser();
        state.putParcelable(getString(R.string.extras_data_initializer_key), currentWeatherDataParser);
    }

    @Override
    public void onRestart() {
        super.onRestart();
        //receiving information from settings
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                //postdelay to call recreate() after onResume()
                if(Settings.isUnitsPreferencesChanged()) {
                    //units change
                    refreshLayoutAfterUnitsPreferencesChange();
                    Settings.setUnitsPreferencesChanged(false);
                    Log.d("preferences_changed", "units change");
                }
                if(Settings.isLanguagePreferencesChanged()){
                    //language change
                    recreate();
                    Settings.setLanguagePreferencesChanged(false);
                    Log.d("preferences_changed", "language change");
                }
            }
        }, 0);
    }

    private void refreshLayoutAfterUnitsPreferencesChange(){
        mainActivityLayoutInitializer.updateLayoutOnWeatherDataChange(this, weatherDataParser,true,false);
    }

    private OnTimeChangeLayoutUpdater getOnTimeChangeLayoutUpdater(){
        return mainActivityLayoutInitializer.getOnTimeChangeLayoutUpdater();
    }

    @Override
    protected  void onPause(){
        super.onPause();
        //stop updating current time in AppBar
        OnTimeChangeLayoutUpdater onTimeChangeLayoutUpdater=getOnTimeChangeLayoutUpdater();
        if(onTimeChangeLayoutUpdater!=null){
            onTimeChangeLayoutUpdater.pauseUiThread();
        }
        weatherDataParser = OnWeatherDataChangeLayoutUpdater.getCurrentWeatherDataParser();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //start updating current time in AppBar
        OnTimeChangeLayoutUpdater onTimeChangeLayoutUpdater=getOnTimeChangeLayoutUpdater();
        if(onTimeChangeLayoutUpdater!=null){
            onTimeChangeLayoutUpdater.resumeUiThread();
        }
    }

    @Override
    protected void onDestroy() {
        //killing UI thread, which updates layout every second
        OnTimeChangeLayoutUpdater onTimeChangeLayoutUpdater=getOnTimeChangeLayoutUpdater();
        if(onTimeChangeLayoutUpdater!=null){
            onTimeChangeLayoutUpdater.interruptUiThread();
        }
        super.onDestroy();
    }
}
