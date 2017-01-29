package paweltypiak.matweather.mainActivityInitializing;

import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import paweltypiak.matweather.R;
import paweltypiak.matweather.dialogsInitializing.dialogInitializers.exitDialogInitializing.ExitDialogInitializer;
import paweltypiak.matweather.mainActivityInitializing.mainActivityDataDownloading.MainActivityDataDownloader;
import paweltypiak.matweather.mainActivityInitializing.mainActivityLayoutInitializing.MainActivityLayoutInitializer;
import paweltypiak.matweather.mainActivityInitializing.mainActivityLayoutInitializing.layoutUpdating.OnTimeChangeLayoutUpdater;
import paweltypiak.matweather.settings.Settings;
import paweltypiak.matweather.mainActivityInitializing.mainActivityLayoutInitializing.layoutUpdating.OnWeatherDataChangeLayoutUpdater;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataParser;

public class MainActivity extends AppCompatActivity {

    private MainActivityLayoutInitializer mainActivityLayoutInitializer;
    private MainActivityDataDownloader mainActivityDataDownloader;
    private WeatherDataParser weatherDataParser;
    private AlertDialog exitDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeLayout();
        initializeDataDownloading(savedInstanceState);
    }

    private void initializeLayout(){
        setContentView(R.layout.activity_main);
        mainActivityLayoutInitializer =new MainActivityLayoutInitializer(this);
    }

    public MainActivityLayoutInitializer getMainActivityLayoutInitializer() {
        return mainActivityLayoutInitializer;
    }

    private void initializeDataDownloading(Bundle savedInstanceState){
        mainActivityDataDownloader=new MainActivityDataDownloader(this,savedInstanceState,mainActivityLayoutInitializer);
    }

    public MainActivityDataDownloader getMainActivityDataDownloader() {
        return mainActivityDataDownloader;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        mainActivityDataDownloader.
                getGeolocalizationDownloader().
                getOnRequestLocationPermissionsResultCallback().
                onRequestLocationPermissionsResult(requestCode,grantResults);
    }

    @Override
    public void onBackPressed() {
        boolean isDrawerLayoutOpened= mainActivityLayoutInitializer.getAppBarLayoutInitializer()
                .getAppBarLayoutButtonsInitializer()
                .getNavigationDrawerInitializer()
                .closeDrawerLayout();
        if(isDrawerLayoutOpened==false){
            if(exitDialog==null){
                exitDialog= ExitDialogInitializer.getExitDialog(this,1,null);
            }
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
