package paweltypiak.matweather;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import paweltypiak.matweather.localizationDataDownloading.GeocodingCallback;
import paweltypiak.matweather.localizationDataDownloading.GeocodingDownloader;
import paweltypiak.matweather.localizationDataDownloading.CurrentCoordinatesDownloader;
import paweltypiak.matweather.usefulClasses.DialogInitializer;
import paweltypiak.matweather.usefulClasses.SharedPreferencesModifier;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataDownloader;
import paweltypiak.matweather.weatherDataDownloading.WeatherDownloadCallback;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataInitializer;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataSetter;
import static paweltypiak.matweather.weatherDataDownloading.WeatherDataSetter.getTimeThreadStartedFlag;
import static paweltypiak.matweather.weatherDataDownloading.WeatherDataSetter.setStartTimeThread;
import paweltypiak.matweather.jsonHandling.Channel;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, WeatherDownloadCallback, SwipeRefreshLayout.OnRefreshListener,
        GeocodingCallback {
    private WeatherDataInitializer getter;
    private AlertDialog weatherServiceFailureDialog;
    private AlertDialog geocodingInternetFailureDialog;
    private AlertDialog weatherGeolocalizationInternetFailureDialog;
    private AlertDialog weatherRefreshInternetFailureDialog;
    private AlertDialog yahooRedirectDialog;
    private AlertDialog yahooWeatherRedirectDialog;
    private AlertDialog exitDialog;
    private AlertDialog aboutDialog;
    private AlertDialog feedbackDialog;
    private AlertDialog authorDialog;
    private AlertDialog searchDialog;
    private AlertDialog mapsDialog;
    private AlertDialog addToFavouritesDialog;
    private AlertDialog editFavouritesDialog;
    private AlertDialog favouritesDialog;
    private AlertDialog noFavouritesDialog;
    private AlertDialog geolocalizationMethodsDialog;
    private AlertDialog geolocalizationProgressDialog;
    private AlertDialog coordinatesDownloadFailureDialog;
    private AlertDialog permissionDeniedDialog;
    private AlertDialog noWeatherResultsForLocation;
    private AlertDialog geocodingServiceFailureDialog;
    private DialogInitializer dialogInitializer;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String geocodingLocation;
    private CoordinatorLayout mainLayout;
    private LinearLayout weatherLayout;
    private TextView refreshMessageTextView;
    private ImageView refreshImageView;
    private static int floatingActionButtonOnClickIndicator;
    private NavigationView navigationView;
    private static FloatingActionButton floatingActionButton;
    private CurrentCoordinatesDownloader currentCoordinatesDownloader;
    private TextView geolocalizationProgressMessageTextView;
    private int downloadMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadExtras();
        UsefulFunctions.setIsFirstWeatherDownloading(true);
        initializeLayout();
        loadDefeaultLocation();
    }

    private void loadExtras(){
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            getter=extras.getParcelable(getString(R.string.extras_data_initializer_key));
        }
    }

    private void loadDefeaultLocation(){
        boolean isDefeaultLocationConstant=SharedPreferencesModifier.isDefeaultLocationConstant(this);
        if(isDefeaultLocationConstant) new WeatherDataSetter(this,getter,true,false);
        else new WeatherDataSetter(this,getter,true,true);
        UsefulFunctions.setIsFirstWeatherDownloading(false);
    }

    @Override
    public void weatherServiceSuccess(Channel channel) {
        getter = new WeatherDataInitializer(channel);
        if(downloadMode==1){
            new WeatherDataSetter(this,getter,true,true);
            geolocalizationProgressDialog.dismiss();
        }
        else{
            new WeatherDataSetter(this,getter,false,false);
            swipeRefreshLayout.setRefreshing(false);
            UsefulFunctions.setViewInvisible(refreshMessageTextView);
            UsefulFunctions.showWeatherSublayouts(this);
        }
    }
    @Override
    public void weatherServiceFailure(int errorCode) {
        if(downloadMode==1){
            geolocalizationProgressDialog.dismiss();
            if(errorCode==1) {
                weatherGeolocalizationInternetFailureDialog.show();
            }
            else{
                noWeatherResultsForLocation.show();
            }
        }
        else{
            swipeRefreshLayout.setRefreshing(false);
            UsefulFunctions.showWeatherSublayouts(this);
            UsefulFunctions.setViewInvisible(refreshMessageTextView);
            if(errorCode==1) {
                weatherRefreshInternetFailureDialog.show();
            }
            else{
                weatherServiceFailureDialog.show();
            }
        }
    }

    @Override
    public void geocodingServiceSuccess(String location) {
        geolocalizationProgressMessageTextView.setText(getString(R.string.downloading_weather_data_progress_message));
        geocodingLocation=location;
        downloadWeatherData(geocodingLocation);
    }

    @Override
    public void geocodingServiceFailure(int errorCode) {
        geolocalizationProgressDialog.dismiss();
        if(errorCode==1){
            geocodingInternetFailureDialog.show();
        }
        else{
            geocodingServiceFailureDialog.show();
        }
    }

    public void downloadWeatherData(String location) {
       new WeatherDataDownloader(location,this);
    }

    private void startGeolocalization(){
        downloadMode=1;
        geolocalizationProgressDialog.show();
        if(geolocalizationProgressMessageTextView==null)geolocalizationProgressMessageTextView=(TextView)geolocalizationProgressDialog.findViewById(R.id.progress_dialog_message_text);
        permissionDeniedDialog=dialogInitializer.initializePermissionDeniedDialog(2,startGeolocalizationRunnable,null);
        coordinatesDownloadFailureDialog =dialogInitializer.initializeGeolocalizationFailureDialog(2,startGeolocalizationRunnable,null);
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
        public void run() {startGeolocalization();}
    };

    Runnable refreshWeatherRunnable = new Runnable() {
        public void run() {onRefresh();}
    };

    Runnable downloadWeatherDataAfterGeocodingFailureRunnable = new Runnable() {
        public void run() {downloadWeatherData(geocodingLocation);}
    };

    private void setDialogs(){
        dialogInitializer=new DialogInitializer(this);
        yahooRedirectDialog=dialogInitializer.initializeYahooRedirectDialog();
        exitDialog=dialogInitializer.initializeExitDialog(2,null);
        aboutDialog=dialogInitializer.initializeAboutDialog();
        feedbackDialog=dialogInitializer.initializeFeedbackDialog();
        authorDialog=dialogInitializer.initializeAuthorDialog();
        searchDialog=dialogInitializer.initializeSearchDialog(2,null);
        noFavouritesDialog =dialogInitializer.initializeNoFavouritesDialog();
        geolocalizationMethodsDialog=dialogInitializer.initializeGeolocalizationMethodsDialog(startGeolocalizationRunnable);
        geolocalizationProgressDialog=dialogInitializer.initializeProgressDialog(getString(R.string.waiting_for_localization_progress_message));
        noWeatherResultsForLocation =dialogInitializer.initializeNoWeatherResultsForLocationDialog(3,startGeolocalizationRunnable,null);
        geocodingInternetFailureDialog=dialogInitializer.initializeInternetFailureDialog(2,geocodingRunnable,null);
        weatherGeolocalizationInternetFailureDialog=dialogInitializer.initializeInternetFailureDialog(2, downloadWeatherDataAfterGeocodingFailureRunnable,null);
        weatherRefreshInternetFailureDialog=dialogInitializer.initializeInternetFailureDialog(2,refreshWeatherRunnable,null);
        weatherServiceFailureDialog =dialogInitializer.initializeServiceFailureDialog(2,refreshWeatherRunnable,null);
        geocodingServiceFailureDialog=dialogInitializer.initializeServiceFailureDialog(2,startGeolocalizationRunnable,null);
    }

    private void initializeLayout(){
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        refreshMessageTextView=(TextView)findViewById(R.id.app_bar_refresh_text);
        refreshImageView=(ImageView) findViewById(R.id.app_bar_refresh_image);
        weatherLayout=(LinearLayout) findViewById(R.id.weather_layout);
        setSwipeRefreshLayout();
        setDialogs();
        setButtonsClickable();
    }

    private void setButtonsClickable(){
        setYahooClickable();
        setWeatherClickable();
        setSearchClickable();
        setFloatingActionButton();
        setLocationClickable();
    }

    private void setYahooClickable(){
        LinearLayout yahooLayout=(LinearLayout)findViewById(R.id.yahoo_layout);
        yahooLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yahooRedirectDialog.show();
            }
        });
    }

    private void setWeatherClickable(){
        LinearLayout currentLayout=(LinearLayout)findViewById(R.id.current_layout);
        LinearLayout detailsLayout=(LinearLayout)findViewById(R.id.details_layout);
        LinearLayout forecastLayout=(LinearLayout)findViewById(R.id.forecast_layout);
        currentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yahooWeatherRedirectDialog= WeatherDataSetter.getYahooWeatherRedirectDialog();
                yahooWeatherRedirectDialog.show();
            }
        });
        detailsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yahooWeatherRedirectDialog= WeatherDataSetter.getYahooWeatherRedirectDialog();
                yahooWeatherRedirectDialog.show();
            }
        });
        forecastLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yahooWeatherRedirectDialog= WeatherDataSetter.getYahooWeatherRedirectDialog();
                yahooWeatherRedirectDialog.show();
            }
        });
    }

    private void setSearchClickable(){
        ImageView searchImageView =(ImageView)findViewById(R.id.search_image);
        LinearLayout searchLayout=(LinearLayout) findViewById(R.id.search_layout);
        Picasso.with(getApplicationContext()).load(R.drawable.search_black_icon).transform(new UsefulFunctions().new setDrawableColor(getResources().getColor(R.color.white))).into(searchImageView);
        searchLayout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        searchDialog.show();
                        UsefulFunctions.showKeyboard(MainActivity.this);
                    }
                });
    }

    private void setLocationClickable(){
        RelativeLayout locactionLayout=(RelativeLayout) findViewById(R.id.location_text_layout);
        locactionLayout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mapsDialog= WeatherDataSetter.getMapsDialog();
                        mapsDialog.show();
                    }
                });
    }

    private void setFloatingActionButton(){
        floatingActionButton=(FloatingActionButton)findViewById(R.id.main_fab);
        UsefulFunctions.setfloatingActionButtonOnClickIndicator(this,1);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(floatingActionButtonOnClickIndicator ==1){
                    addToFavouritesDialog = dialogInitializer.initializeAddToFavouritesDialog();
                    addToFavouritesDialog.show();
                }
                else {
                    editFavouritesDialog=dialogInitializer.initializeEditFavouritesDialog();
                    editFavouritesDialog.show();
                }
            }
        });
    }

    public static void setFloatingActionButtonOnClickIndicator(int floatingActionButtonOnClickIndicator) {
        MainActivity.floatingActionButtonOnClickIndicator = floatingActionButtonOnClickIndicator;
    }

    private void setSwipeRefreshLayout(){
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        swipeRefreshLayout.setOnTouchListener(new View.OnTouchListener() {
            boolean isTouched=false;
            float movedWidth;
            float movedHeight;
            float startWidth;
            float startHeight;
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    if(isTouched==false){
                        UsefulFunctions.setViewVisible(refreshImageView);
                        UsefulFunctions.setViewVisible(refreshMessageTextView);
                        refreshMessageTextView.setText(getString(R.string.refresh_message_pull_to_refresh));
                        isTouched=true;
                        startWidth = event.getRawX();
                        startHeight = event.getRawY();
                    }
                    movedWidth = event.getRawX() - startWidth;
                    movedHeight = event.getRawY() - startHeight;
                    double alpha=UsefulFunctions.getPullOpacity(0.2,movedHeight,MainActivity.this,true);
                    refreshMessageTextView.setTextColor(Color.argb((int)alpha, 255, 255, 255));
                    refreshMessageTextView.setText(getString(R.string.refresh_message_pull_to_refresh));
                    refreshImageView.setAlpha((float)(alpha/255));
                }
                if(event.getAction()==MotionEvent.ACTION_UP){
                    isTouched=false;
                    UsefulFunctions.setViewGone(refreshImageView);
                    UsefulFunctions.setViewInvisible(refreshMessageTextView);
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            exitDialog.show();
        }
    }

    @Override
    public void onRefresh() {
        Log.d("refresh", "refresh ");
        downloadMode=2;
        swipeRefreshLayout.setRefreshing(true);
        refreshMessageTextView.setTextColor(Color.argb(255, 255, 255, 255));
        refreshMessageTextView.setText(getString(R.string.refreshing_progress_message));
        UsefulFunctions.setViewVisible(refreshMessageTextView);
        UsefulFunctions.hideWeatherSublayouts(this);
        String currentLocation[]=UsefulFunctions.getCurrentLocationAddress();
        downloadWeatherData(currentLocation[0]+", "+currentLocation[1]);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent e) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (!drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.openDrawer(GravityCompat.START);
            }
        }
        return super.onKeyDown(keyCode, e);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.nav_button_geolocalization){
            int localizationOption=SharedPreferencesModifier.getGeolocalizationMethod(this);
            if(localizationOption==0){
                geolocalizationMethodsDialog.show();
            }
            else{
                startGeolocalization();
            }
        }
        else if(id==R.id.nav_button_favourites){
            if(SharedPreferencesModifier.getFavouriteLocationsAddresses(MainActivity.this).length==0) noFavouritesDialog.show();
            else{
                favouritesDialog=dialogInitializer.initializeFavouritesDialog(2,null,null);
                favouritesDialog.show();
            }
        }
        else if (id == R.id.nav_button_settings) {
        } else if (id == R.id.nav_button_about) {
            aboutDialog.show();
        } else if (id == R.id.nav_button_feedback) {
            feedbackDialog.show();
        } else if(id == R.id.nav_button_author){
            authorDialog.show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onPause() {
        setStartTimeThread(false);
        super.onPause();
    }

    @Override
    protected void onResume() {
        if(getTimeThreadStartedFlag()==true)    {
            setStartTimeThread(true);
            //WeatherDataSetter.newRefresh=true;
        }
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        WeatherDataSetter.interruptUiThread();
        super.onDestroy();
    }
}
