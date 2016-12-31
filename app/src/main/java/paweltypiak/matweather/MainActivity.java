package paweltypiak.matweather;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import paweltypiak.matweather.localizationDataDownloading.GeocodingCallback;
import paweltypiak.matweather.localizationDataDownloading.GeocodingDownloader;
import paweltypiak.matweather.localizationDataDownloading.CurrentCoordinatesDownloader;
import paweltypiak.matweather.settings.Settings;
import paweltypiak.matweather.usefulClasses.DialogInitializer;
import paweltypiak.matweather.usefulClasses.SharedPreferencesModifier;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataDownloader;
import paweltypiak.matweather.weatherDataDownloading.WeatherDownloadCallback;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataInitializer;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataSetter;
import static paweltypiak.matweather.weatherDataDownloading.WeatherDataSetter.setStartTimeThread;
import paweltypiak.matweather.jsonHandling.Channel;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        WeatherDownloadCallback,
        SwipeRefreshLayout.OnRefreshListener,
        GeocodingCallback {

    private WeatherDataInitializer weatherDataInitializer;
    private AlertDialog weatherServiceFailureDialog;
    private AlertDialog geocodingInternetFailureDialog;
    private AlertDialog weatherGeolocalizationInternetFailureDialog;
    private AlertDialog weatherRefreshInternetFailureDialog;
    private AlertDialog yahooMainRedirectDialog;
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
    private TextView refreshMessageTextView;
    private ImageView refreshImageView;
    private static int floatingActionButtonOnClickIndicator;
    private NavigationView navigationView;
    private static FloatingActionButton favouritesFloatingActionButton;
    private CurrentCoordinatesDownloader currentCoordinatesDownloader;
    private TextView geolocalizationProgressMessageTextView;
    private int downloadMode;
    private String yahooWeatherLink;
    private UsefulFunctions.SmoothActionBarDrawerToggle navigationDrawerToggle;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION=1;
    private int toolbarExpandedHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadInitialWeatherData(savedInstanceState);
        UsefulFunctions.setIsFirstWeatherDownloading(true);
        initializeLayout();
        loadDefeaultLocation();
    }

    private void loadInitialWeatherData(Bundle savedInstanceState){
        //loading initial weather data
        if(savedInstanceState==null){
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                //after launch
                weatherDataInitializer =extras.getParcelable(getString(R.string.extras_data_initializer_key));
                Log.d("initial_weather_data", "from extras: "+weatherDataInitializer.getCity());
            }
        }
        else {
            //after recreate
            weatherDataInitializer =  savedInstanceState.getParcelable(getString(R.string.extras_data_initializer_key));
            Log.d("initial_weather_data", "from savedInstanceState: "+weatherDataInitializer.getCity());
        }
    }

    private void loadDefeaultLocation(){
        //delivering information if defeault location is constant, or is from geolocalization
        boolean isDefeaultLocationConstant=SharedPreferencesModifier.isDefeaultLocationConstant(this);
        if(isDefeaultLocationConstant) new WeatherDataSetter(this, weatherDataInitializer,true,false);
        else new WeatherDataSetter(this, weatherDataInitializer,true,true);
        UsefulFunctions.setIsFirstWeatherDownloading(false);
    }

    private void initializeLayout(){
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationDrawerToggle = new UsefulFunctions().new SmoothActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close,invalidateOptionsMenuRunnable);
        drawer.addDrawerListener(navigationDrawerToggle);
        navigationDrawerToggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        refreshMessageTextView=(TextView)findViewById(R.id.app_bar_refresh_text);
        refreshImageView=(ImageView) findViewById(R.id.app_bar_refresh_image);
        initializeAppBar();
        setGeneralWeatherLayoutSizes();
        setDetailsLayoutSizes();
        setForecastLayoutSizes();
        setSwipeRefreshLayout();
        initializeDialogs();
        setButtonsClickable();
    }


    private void initializeAppBar(){
        setCollapsingToolbarViewsHeight();
        setAppbarOnOffsetChangeListener();
    }

    private void setCollapsingToolbarViewsHeight(){
        int bottomLayoutHeight=getBottomLayoutHeight();
        toolbarExpandedHeight = getComputedExpendedToolbarHeight(bottomLayoutHeight);
        setComputedToolbarExpandedHeight(bottomLayoutHeight, toolbarExpandedHeight);
        setContentLayoutTopPadding();
    }

    private void setComputedToolbarExpandedHeight(int bottomLayoutHeight, int toolbarExpandedHeight){
        CollapsingToolbarLayout collapsingToolbarLayout=(net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar_layout);
        AppBarLayout.LayoutParams collapsingToolbarParams = (AppBarLayout.LayoutParams)collapsingToolbarLayout.getLayoutParams();
        collapsingToolbarParams.height = toolbarExpandedHeight;
        collapsingToolbarLayout.setExpandedTitleMarginBottom(bottomLayoutHeight);
    }

    private void setContentLayoutTopPadding(){
        LinearLayout contentLayout=(LinearLayout)findViewById(R.id.main_activity_content_layout);
        contentLayout.setPadding(0,toolbarExpandedHeight,0,0);
    }

    private int getBottomLayoutHeight(){
        int locationSubheaderTextSize=UsefulFunctions.getTextViewHeight(
                this,
                "",
                (int)getResources().getDimension(R.dimen.secondary_location_text_size),
                Typeface.DEFAULT,
                0,
                0,
                0,
                (int)getResources().getDimension(R.dimen.activity_very_small_margin)
        );
        int refreshMessageTextViewHeight=UsefulFunctions.getTextViewHeight(
                this,
                "",
                (int)getResources().getDimension(R.dimen.refresh_message_text_size),
                Typeface.DEFAULT,
                0,
                0,
                0,
                (int)getResources().getDimension(R.dimen.activity_very_small_margin)
        );
        int refreshMessageIconSize=(int)getResources().getDimension(R.dimen.refresh_message_icon_size);
        int refreshMessageLayoutSize;
        if(refreshMessageIconSize>refreshMessageTextViewHeight) {
            refreshMessageLayoutSize=refreshMessageIconSize;
        }
        else {
            refreshMessageLayoutSize=locationSubheaderTextSize;
        }
        int layoutHeight=locationSubheaderTextSize+refreshMessageLayoutSize;
        return layoutHeight;
    }

    private int getComputedExpendedToolbarHeight(int bottomAppbarLayoutHeigh){
        int paddingLeft=(int)getResources().getDimension(R.dimen.activity_normal_margin);
        int paddingTop=(int)getResources().getDimension(R.dimen.expended_toolbar_top_padding);
        int paddingRight=(int)getResources().getDimension(R.dimen.activity_normal_margin);
        int paddingBottom=bottomAppbarLayoutHeigh;
        int collapsingToolbarHeight=UsefulFunctions.getTextViewHeight(
                this,
                "",
                (int)getResources().getDimension(R.dimen.primary_location_text_size),
                Typeface.DEFAULT,
                paddingLeft,
                paddingTop,
                paddingRight,
                paddingBottom
        );
        return collapsingToolbarHeight;
    }

    private void setAppbarOnOffsetChangeListener(){
        AppBarLayout appBarLayout=(AppBarLayout)findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                animateOnOffsetChanged(appBarLayout,verticalOffset);
            }
        });
    }

    private void animateOnOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        float percentage = ((float)Math.abs(verticalOffset)/appBarLayout.getTotalScrollRange());
        animateTimeLayout(percentage);
        animateSecondaryLocationNameTextView(percentage);
        animateYahooLogoLayout(percentage);
        animateSeeMoreArrow(percentage);
    }

    private void animateTimeLayout(float percentage){
        final int TIME_LAYOUT_DISAPPEARANCE_TIME_MULTIPLIER =3;
        float timezoneDisappearPercentage=1f-(percentage* TIME_LAYOUT_DISAPPEARANCE_TIME_MULTIPLIER);
        RelativeLayout timezoneLayout=(RelativeLayout)findViewById(R.id.app_bar_time_layout);
        timezoneLayout.setAlpha(timezoneDisappearPercentage);
    }

    private void animateSecondaryLocationNameTextView(float percentage){
        final int SECONDARY_LOCATION_NAME_TEXT_VIEW_DISAPPEARANCE_TIME_MULTIPLIER =3;
        float secondaryLocationNameTextViewDisappearPercentage=1f-(percentage* SECONDARY_LOCATION_NAME_TEXT_VIEW_DISAPPEARANCE_TIME_MULTIPLIER);
        TextView bottomLayout=(TextView)findViewById(R.id.app_bar_secondary_location_name_text);
        bottomLayout.setAlpha(secondaryLocationNameTextViewDisappearPercentage);
    }

    private void animateYahooLogoLayout(float percentage){
        final int YAHOO_LOGO_LAYOUT_DISAPPEARANCE_TIME_MULTIPLIER =4;
        float yahooLogoLayoutDisappearPercentage=1-(percentage* YAHOO_LOGO_LAYOUT_DISAPPEARANCE_TIME_MULTIPLIER);
        LinearLayout yahooLogoLayout=(LinearLayout)findViewById(R.id.yahoo_logo_layout);
        yahooLogoLayout.setAlpha(yahooLogoLayoutDisappearPercentage);
    }

    private void animateSeeMoreArrow(float percentage){
        final int SEE_MORE_IMAGE_VIEW_DISAPPEARANCE_TIME_MULTIPLIER =3;
        float seeMoreImageViewDisappearPercentage=1-(percentage* SEE_MORE_IMAGE_VIEW_DISAPPEARANCE_TIME_MULTIPLIER);
        ImageView seeMoreImageView=(ImageView)findViewById(R.id.current_weather_layout_see_more_image);
        seeMoreImageView.setAlpha(seeMoreImageViewDisappearPercentage);
    }

    private void setGeneralWeatherLayoutSizes(){
        int screenHeight=UsefulFunctions.getScreenHeight(this);
        int statusBarHeight=UsefulFunctions.getStatusBarHeight(this);
        int generalWeatherLayoutHeight=screenHeight-toolbarExpandedHeight-statusBarHeight;
        LinearLayout generalWeatherLayout=(LinearLayout)findViewById(R.id.current_layout);
        LinearLayout.LayoutParams generalWeatherLayoutParams=(LinearLayout.LayoutParams)generalWeatherLayout.getLayoutParams();
        generalWeatherLayoutParams.height=generalWeatherLayoutHeight;
        generalWeatherLayout.setLayoutParams(generalWeatherLayoutParams);
        setCurrentTemperatureTextViewHeight(generalWeatherLayoutHeight);
    }

    private void setCurrentTemperatureTextViewHeight(final int generalWeatherLayoutHeight){
        final LinearLayout generalWeatherLayout=(LinearLayout)findViewById(R.id.current_layout);
        ViewTreeObserver observer = generalWeatherLayout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ImageView currentConditionsImageView=(ImageView)findViewById(R.id.current_conditions_image);

                int currentConditionsImageViewVerticalPadding=generalWeatherLayoutHeight/20;
                //currentConditionsImageView.setPadding(0,currentConditionsImageViewVerticalPadding,0,currentConditionsImageViewVerticalPadding);
                RelativeLayout currentTemperatureLayout=(RelativeLayout)findViewById(R.id.current_temperature_layout);
                int currentTemperatureLayoutVerticalTranslation=(int)(generalWeatherLayoutHeight*0.02f);
                currentTemperatureLayout.setTranslationY(-currentTemperatureLayoutVerticalTranslation);
                int currentTemperatureTextViewHeight=currentTemperatureLayout.getHeight();
                TextView currentTemperatureTextView=(TextView)findViewById(R.id.current_temperature_text);
                currentTemperatureTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, currentTemperatureTextViewHeight);
                TextView currentTemperatureDagreeSignTextView=(TextView)findViewById(R.id.current_temperature_dagree_sign_text);
                currentTemperatureDagreeSignTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, currentTemperatureTextViewHeight);

                TextView currentTemperatureUnitTextView=(TextView)findViewById(R.id.current_temperature_unit_text);
                int currentTemperatureUnitTextViewHeight=(int)(currentTemperatureTextViewHeight/2.5f);
                currentTemperatureUnitTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, currentTemperatureUnitTextViewHeight);
                int currentTemperatureUnitTextViewTranslationY=(int)(currentTemperatureTextViewHeight/35);
                currentTemperatureUnitTextView.setTranslationY(currentTemperatureUnitTextViewTranslationY);

                ImageView seeMoreImageView=(ImageView)findViewById(R.id.current_weather_layout_see_more_image);
                Log.d("image_size", "onGlobalLayout: "+seeMoreImageView.getWidth());


                generalWeatherLayout.getViewTreeObserver().removeOnGlobalLayoutListener(
                        this);
            }
        });


    }

    private void setDetailsLayoutSizes(){
        setAdditionalConditionsLayoutSizes();
    }

    private void setAdditionalConditionsLayoutSizes(){
        final LinearLayout additionalConditionsLayout=(LinearLayout)findViewById(R.id.additional_conditions_layout);
        ViewTreeObserver observer=additionalConditionsLayout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int iconSize=(int)getResources().getDimension(R.dimen.additional_conditions_icon_size);
                TextView textView = (TextView)findViewById(R.id.speed_text);
                int textSize=textView.getWidth();
                int iconEmptySpace=(textSize-iconSize)/2;
                LinearLayout directionLayout=(LinearLayout)findViewById(R.id.direction_layout);
                LinearLayout.LayoutParams directionLayoutParams=(LinearLayout.LayoutParams)directionLayout.getLayoutParams();
                directionLayoutParams.rightMargin=iconEmptySpace;
                Log.d("space", "onGlobalLayout: "+iconEmptySpace);
                directionLayout.setLayoutParams(directionLayoutParams);
                LinearLayout humidityLayout=(LinearLayout)findViewById(R.id.humidity_layout);
                LinearLayout.LayoutParams humidityLayoutParams=(LinearLayout.LayoutParams)humidityLayout.getLayoutParams();
                humidityLayoutParams.leftMargin=iconEmptySpace;
                humidityLayout.setLayoutParams(humidityLayoutParams);
                additionalConditionsLayout.getViewTreeObserver().removeOnGlobalLayoutListener(
                        this);
            }
        });
    }

    private void setForecastLayoutSizes(){
        final LinearLayout forecastLayout=(LinearLayout)findViewById(R.id.forecast_layout);
        ViewTreeObserver observer=forecastLayout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                LinearLayout daysLayout=(LinearLayout)findViewById(R.id.forecast_days_layout);
                int daysLayoutHeight=daysLayout.getHeight();
                LinearLayout dayLayout=(LinearLayout)findViewById(R.id.forecast_day0_layout);
                int dayLayoutHeight=dayLayout.getHeight();
                int stepperHeight=daysLayoutHeight-dayLayoutHeight;
                RelativeLayout dateLayout=(RelativeLayout)findViewById(R.id.forecast_day0_date_layout);
                int dateLayoutWidth=dateLayout.getWidth();
                int conditionIconSize=(int)getResources().getDimension(R.dimen.forecast_condition_icon_size);
                int conditionIconHorizontalMargin=(int)getResources().getDimension(R.dimen.forecast_condition_icon_horizontal_margin);
                int stepperWidth=(int)getResources().getDimension(R.dimen.forecast_stepper_size);
                int marginLeft=dateLayoutWidth+conditionIconHorizontalMargin+(conditionIconSize-stepperWidth)/2;
                View stepperView=findViewById(R.id.forecast_stepper_view);
                RelativeLayout.LayoutParams stepperViewLayoutParams=(RelativeLayout.LayoutParams)stepperView.getLayoutParams();
                stepperViewLayoutParams.height=stepperHeight;
                stepperViewLayoutParams.leftMargin=marginLeft;
                stepperView.setLayoutParams(stepperViewLayoutParams);
                forecastLayout.getViewTreeObserver().removeOnGlobalLayoutListener(
                        this);
            }
        });
    }


    @Override
    public void weatherServiceSuccess(Channel channel) {
        weatherDataInitializer = new WeatherDataInitializer(channel);
        if(downloadMode==0){
            //weather service success for geolocalization
            new WeatherDataSetter(this, weatherDataInitializer,true,true);
            geolocalizationProgressDialog.dismiss();
        }
        else{
            //weather service success for refreshing
            new WeatherDataSetter(this, weatherDataInitializer,false,false);
            swipeRefreshLayout.setRefreshing(false);
            UsefulFunctions.setViewInvisible(refreshMessageTextView);
            UsefulFunctions.showWeatherSublayouts(this);
        }
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
            swipeRefreshLayout.setRefreshing(false);
            UsefulFunctions.showWeatherSublayouts(this);
            UsefulFunctions.setViewInvisible(refreshMessageTextView);
            if(errorCode==0) {
                weatherRefreshInternetFailureDialog.show();
            }
            else if(errorCode==1){
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
        if(errorCode==0){
            geocodingInternetFailureDialog.show();
        }
        else if(errorCode==1){
            geocodingServiceFailureDialog.show();
        }
    }

    public void downloadWeatherData(String location) {
       new WeatherDataDownloader(location,this);
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

    Runnable refreshWeatherRunnable = new Runnable() {
        public void run() {onRefresh();}
    };

    Runnable downloadWeatherDataAfterGeocodingFailureRunnable = new Runnable() {
        public void run() {downloadWeatherData(geocodingLocation);}
    };

    Runnable invalidateOptionsMenuRunnable = new Runnable() {
        public void run() {invalidateOptionsMenu();}
    };

    private void initializeDialogs(){
        dialogInitializer=new DialogInitializer(this);
        yahooMainRedirectDialog =dialogInitializer.initializeYahooRedirectDialog(0,null);
        exitDialog=dialogInitializer.initializeExitDialog(1,null);
        aboutDialog=dialogInitializer.initializeAboutDialog();
        feedbackDialog=dialogInitializer.initializeFeedbackDialog();
        authorDialog=dialogInitializer.initializeAuthorDialog();
        searchDialog=dialogInitializer.initializeSearchDialog(1,null);
        noFavouritesDialog =dialogInitializer.initializeNoFavouritesDialog();
        geolocalizationMethodsDialog=dialogInitializer.initializeGeolocalizationMethodsDialog(1,startGeolocalizationRunnable);
        geolocalizationProgressDialog=dialogInitializer.initializeProgressDialog(getString(R.string.waiting_for_localization_progress_message));
        noWeatherResultsForLocation =dialogInitializer.initializeNoWeatherResultsForLocationDialog(2,startGeolocalizationRunnable,null);
        geocodingInternetFailureDialog=dialogInitializer.initializeInternetFailureDialog(1,geocodingRunnable,null);
        weatherGeolocalizationInternetFailureDialog=dialogInitializer.initializeInternetFailureDialog(1, downloadWeatherDataAfterGeocodingFailureRunnable,null);
        weatherRefreshInternetFailureDialog=dialogInitializer.initializeInternetFailureDialog(1,refreshWeatherRunnable,null);
        weatherServiceFailureDialog =dialogInitializer.initializeServiceFailureDialog(1,refreshWeatherRunnable,null);
        geocodingServiceFailureDialog=dialogInitializer.initializeServiceFailureDialog(1,startGeolocalizationRunnable,null);
        permissionDeniedDialog=dialogInitializer.initializePermissionDeniedDialog(1,startGeolocalizationRunnable,null);
        coordinatesDownloadFailureDialog =dialogInitializer.initializeGeolocalizationFailureDialog(1,startGeolocalizationRunnable,null);
    }

    private void setButtonsClickable(){
        setYahooClickable();
        setWeatherClickable();
        setSearchClickable();
        setFloatingActionButton();
        setLocationClickable();
    }

    private void setSeeMoreButtonClickable(){
        ImageView seeMoreImageView=(ImageView)findViewById(R.id.current_weather_layout_see_more_image);
        final AppBarLayout appBarLayout=(AppBarLayout)findViewById(R.id.app_bar);
        final CoordinatorLayout coordinatorLayout=(CoordinatorLayout)findViewById(R.id.main_coordinator_layout);
        seeMoreImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
                AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
                if (behavior != null) {
                    behavior.onNestedFling(coordinatorLayout, appBarLayout, null, 0, 10000, true);
                }
            }
        });
    }

    private void setYahooClickable(){
        LinearLayout yahooLayout=(LinearLayout)findViewById(R.id.yahoo_logo_layout);
        yahooLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yahooMainRedirectDialog.show();
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
                yahooWeatherLink =WeatherDataSetter.getCurrentDataFormatter().getLink();
                yahooWeatherRedirectDialog= dialogInitializer.initializeYahooRedirectDialog(1, yahooWeatherLink);
                yahooWeatherRedirectDialog.show();
            }
        });
        detailsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yahooWeatherLink =WeatherDataSetter.getCurrentDataFormatter().getLink();
                yahooWeatherRedirectDialog= dialogInitializer.initializeYahooRedirectDialog(1, yahooWeatherLink);
                yahooWeatherRedirectDialog.show();
            }
        });
        forecastLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yahooWeatherLink =WeatherDataSetter.getCurrentDataFormatter().getLink();
                yahooWeatherRedirectDialog= dialogInitializer.initializeYahooRedirectDialog(1, yahooWeatherLink);
                yahooWeatherRedirectDialog.show();
            }
        });
    }

    private void setSearchClickable(){
        ImageView searchImageView =(ImageView)findViewById(R.id.search_image);
        LinearLayout searchLayout=(LinearLayout) findViewById(R.id.search_layout);
        Picasso.with(getApplicationContext()).load(R.drawable.search_icon).transform(new UsefulFunctions().new setDrawableColor(ContextCompat.getColor(this,R.color.white))).into(searchImageView);
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
        LinearLayout locactionLayout=(LinearLayout) findViewById(R.id.bottom_layout);
        locactionLayout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mapsDialog= dialogInitializer.initializeMapsDialog(MainActivity.this);
                        mapsDialog.show();
                    }
                });
    }

    private void setFloatingActionButton(){
        favouritesFloatingActionButton =(FloatingActionButton)findViewById(R.id.main_fab);
        UsefulFunctions.setfloatingActionButtonOnClickIndicator(this,0);
        favouritesFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(floatingActionButtonOnClickIndicator ==0){
                    //if location is not in favourites
                    addToFavouritesDialog = dialogInitializer.initializeAddToFavouritesDialog();
                    addToFavouritesDialog.show();
                }
                else if(floatingActionButtonOnClickIndicator ==1){
                    //if location is in favourites
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
        int swipeRefrehLayoutOffset = UsefulFunctions.dpToPixels(56,this);
        int progressViewStart = toolbarExpandedHeight-swipeRefrehLayoutOffset;
        int progressViewEnd = (int)(progressViewStart+swipeRefrehLayoutOffset*1.5f);
        swipeRefreshLayout.setProgressViewOffset(true, progressViewStart, progressViewEnd);

        swipeRefreshLayout.setOnTouchListener(new View.OnTouchListener() {
            //dynamically set transparency depending on the pull
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
                    double alpha=UsefulFunctions.getPullTransparency(0.2,movedHeight,MainActivity.this,true);
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
        //closing drawer on back pressed
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
        //refreshing weather
        downloadMode=1;
        swipeRefreshLayout.setRefreshing(true);
        refreshMessageTextView.setTextColor(Color.argb(255, 255, 255, 255));
        refreshMessageTextView.setText(getString(R.string.refreshing_progress_message));
        UsefulFunctions.setViewVisible(refreshMessageTextView);
        UsefulFunctions.hideWeatherSublayouts(this);
        String currentLocation[]=UsefulFunctions.getCurrentLocationAddress();
        downloadWeatherData(currentLocation[0]+", "+currentLocation[1]);
    }

    private void refreshLayoutAfterUnitsPreferencesChange(){
        new WeatherDataSetter(this,weatherDataInitializer,true,false);
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
        final int id = item.getItemId();
        navigationDrawerToggle.runWhenIdle(new Runnable() {
            @Override
            public void run() {
                if(id==R.id.nav_button_geolocalization){
                    //geolocalization
                    int localizationOption=SharedPreferencesModifier.getGeolocalizationMethod(MainActivity.this);
                    if(localizationOption==-1){
                        geolocalizationMethodsDialog.show();
                    }
                    else{
                        checkGeolocalizationPermissions();
                    }
                }
                else if(id==R.id.nav_button_favourites){
                    //favourites
                    if(SharedPreferencesModifier.getFavouriteLocationsAddresses(MainActivity.this).length==0) noFavouritesDialog.show();
                    else{
                        favouritesDialog=dialogInitializer.initializeFavouritesDialog(1,null,null);
                        favouritesDialog.show();
                    }
                }
                else if (id == R.id.nav_button_settings) {
                    //settings
                    Intent intent = new Intent(MainActivity.this, Settings.class);
                    startActivity(intent);
                }
                else if (id == R.id.nav_button_about) {
                    //about
                    aboutDialog.show();
                }
                else if (id == R.id.nav_button_feedback) {
                    //send feedback
                    feedbackDialog.show();
                }
                else if(id == R.id.nav_button_author){
                    //author
                    authorDialog.show();
                }
            }
        });
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle state) {
        //saving current weather information for recreate
        super.onSaveInstanceState(state);
        WeatherDataInitializer currentWeatherDataInitializer=WeatherDataSetter.getCurrentWeatherDataInitializer();
        state.putParcelable(getString(R.string.extras_data_initializer_key), currentWeatherDataInitializer);
    }

    @Override
    protected  void onPause(){
        super.onPause();
        //stop updating current time in AppBar
        setStartTimeThread(false);
        weatherDataInitializer=WeatherDataSetter.getCurrentWeatherDataInitializer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //start updating current time in AppBar
        setStartTimeThread(true);
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

    @Override
    protected void onDestroy() {
        //killing UI thread, which updates layout every second
        WeatherDataSetter.interruptUiThread();
        super.onDestroy();
    }
}
