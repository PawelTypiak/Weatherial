package paweltypiak.matweather;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
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
import paweltypiak.matweather.customViews.LockableSmoothNestedScrollView;
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
        GeocodingCallback,
        UsefulFunctions.OnAppBarStringsChangeListener {

    private WeatherDataInitializer weatherDataInitializer;;
    private AlertDialog geocodingInternetFailureDialog;
    private AlertDialog weatherGeolocalizationInternetFailureDialog;
    private AlertDialog onRefreshWeatherInternetFailureDialog;
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
    private AlertDialog onRefreshWeatherServiceFailureDialog;
    private DialogInitializer dialogInitializer;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String geocodingLocation;
    private LinearLayout onRefreshMessageLayout;
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
    private static boolean isTransparent=false;
    private int expandedToolbarTitleHeight;
    private int expandedToolbarTitleWidth;
    private int collapsedToolbarTitleHeight;
    private int collapsedToolbarTitleWidth;
    private LockableSmoothNestedScrollView nestedScrollView;
    private float previousVerticalOffset=-1;
    private boolean isFingerOnScreen;
    private boolean isToolbarExpanded =true;
    private float movedHeight;
    private float startHeight;

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
        if(isDefeaultLocationConstant) UsefulFunctions.updateLayoutData(this,weatherDataInitializer,true,false);
        else UsefulFunctions.updateLayoutData(this,weatherDataInitializer,true,true);
        UsefulFunctions.setIsFirstWeatherDownloading(false);
    }

    private void initializeLayout(){
        setContentView(R.layout.activity_main);
        initializeAppBar();
        setWeatherGeneralInfoLayoutSizes();
        setWeatherDetailsLayoutSizes();
        setWeatherForecastLayoutSizes();
        initializeDialogs();
        setButtonsClickable();
    }

    private void initializeAppBar(){
        setCollapsingToolbarViewsHeight();
        setAppbarButtonsClickable();
        setSwipeRefreshLayout();
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
        RelativeLayout contentLayout=(RelativeLayout)findViewById(R.id.main_content_layout);
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
        int paddingTop=(int)getResources().getDimension(R.dimen.expanded_toolbar_top_padding);
        int paddingRight=(int)getResources().getDimension(R.dimen.activity_normal_margin);
        int paddingBottom=bottomAppbarLayoutHeigh;
        int collapsingToolbarHeight=UsefulFunctions.getTextViewHeight(
                this,
                "",
                (int)getResources().getDimension(R.dimen.collapsing_toolbar_title_expanded_size),
                Typeface.DEFAULT,
                paddingLeft,
                paddingTop,
                paddingRight,
                paddingBottom
        );
        return collapsingToolbarHeight;
    }

    private void setAppbarButtonsClickable(){
        setNavigationDrawerToggle();
        setSearchButton();
        setLocationButton();
        setYahooLogoButton();
        setFloatingActionButton();
    }

    private void setNavigationDrawerToggle(){
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationDrawerToggle = new UsefulFunctions().new SmoothActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close,invalidateOptionsMenuRunnable);
        drawer.addDrawerListener(navigationDrawerToggle);
        navigationDrawerToggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void setSearchButton(){
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

    private void setLocationButton(){
        View clickableView=findViewById(R.id.toolbar_title_clickable_view);
        clickableView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mapsDialog= dialogInitializer.initializeMapsDialog(MainActivity.this);
                        mapsDialog.show();
                    }
                });
    }

    private void setYahooLogoButton(){
        LinearLayout yahooLayout=(LinearLayout)findViewById(R.id.yahoo_logo_layout);
        yahooLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yahooMainRedirectDialog.show();
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
        setSwipeRefreshLayoutOffset();
        initializeOnRefreshMessage();
        addOnSwipeRefreshPullListeners();
    }

    private void setSwipeRefreshLayoutOffset(){
        int swipeRefrehLayoutOffset = (int)getResources().getDimension(R.dimen.swipe_refresh_layout_offset);
        int progressViewStart = toolbarExpandedHeight-swipeRefrehLayoutOffset;
        float pullRange=swipeRefrehLayoutOffset*1.5f;
        int progressViewEnd = (int)(progressViewStart+pullRange);
        swipeRefreshLayout.setProgressViewOffset(true, progressViewStart, progressViewEnd);
    }

    private void initializeOnRefreshMessage(){
        onRefreshMessageLayout =(LinearLayout)findViewById(R.id.on_refresh_message_layout);
        float onRefreshMessageLayoutTopMargin=getResources().getDimension(R.dimen.on_refresh_message_layout_top_margin);
        int swipeRefrehLayoutOffset = (int)getResources().getDimension(R.dimen.swipe_refresh_layout_offset);
        RelativeLayout.LayoutParams onRefreshMessageLayoutParams=(RelativeLayout.LayoutParams)onRefreshMessageLayout.getLayoutParams();
        onRefreshMessageLayoutParams.topMargin=(int)(swipeRefrehLayoutOffset+onRefreshMessageLayoutTopMargin);
        onRefreshMessageLayout.setLayoutParams(onRefreshMessageLayoutParams);
    }

    private void addOnSwipeRefreshPullListeners(){
        nestedScrollView=(LockableSmoothNestedScrollView) findViewById(R.id.nested_scroll_view);
        addNestedScrollViewOnScrollListener(nestedScrollView);
        addNestedScrollViewOnTouchListener(nestedScrollView);
    }

    private void addNestedScrollViewOnTouchListener(LockableSmoothNestedScrollView nestedScrollView){
        nestedScrollView.setOnTouchListener(new View.OnTouchListener() {
            //dynamically set transparency depending on the pull
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    if(isFingerOnScreen ==false){
                        startHeight = event.getRawY();
                        isFingerOnScreen =true;

                    }
                    setWeatherLayoutTransparencyOnSwipeRefreshLayoutPull(event);
                }
                if(event.getAction()==MotionEvent.ACTION_UP){
                    refreshSwipeRefreshLayoutAvailabilityAfterActionUp();
                    setWeatherLayoutNonTransparentOnActionUp();
                    movedHeight=0;
                    isFingerOnScreen =false;
                }
                return false;
            }
        });
    }

    private void addNestedScrollViewOnScrollListener(LockableSmoothNestedScrollView nestedScrollView){
        nestedScrollView.addOnScrollListener(new android.support.v4.widget.NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(android.support.v4.widget.NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                detectExpandedToolbar(scrollY);
                refreshSwipeRefreshLayoutAvailabilityAfterFling();
            }
        });
    }

    private void refreshSwipeRefreshLayoutAvailabilityAfterFling(){
        if(isFingerOnScreen==false){
            if(isToolbarExpanded ==true){
                swipeRefreshLayout.setEnabled(true);
            }
        }
    }

    private void detectExpandedToolbar(int scrollY){
        if(scrollY==0){
            isToolbarExpanded =true;
        }
        else{
            isToolbarExpanded =false;
        }
    }

    private void refreshSwipeRefreshLayoutAvailabilityAfterActionUp(){
        if(isToolbarExpanded ==true){
            swipeRefreshLayout.setEnabled(true);
        }
        else{
            swipeRefreshLayout.setEnabled(false);
        }
    }

    private void setWeatherLayoutTransparencyOnSwipeRefreshLayoutPull(MotionEvent event){
        movedHeight = event.getRawY() - startHeight;
        if(isSwipeRefreshLayoutPulled(movedHeight)==true){
            LinearLayout weatherLayout=(LinearLayout)findViewById(R.id.weather_layout);
            float transparencyRange=getResources().getDimension(R.dimen.swipe_refresh_layout_offset)*1.5f;
            float transparencyPercentage=movedHeight/transparencyRange;
            float currentAlpha=1-transparencyPercentage;
            if(currentAlpha<0.25){
                currentAlpha=0.25f;
            }
            weatherLayout.setAlpha(currentAlpha);
        }
    }

    private void setWeatherLayoutNonTransparentOnActionUp(){
        float swipeRefrehLayoutOffset=getResources().getDimension(R.dimen.swipe_refresh_layout_offset);
        float fadeOutOffset=swipeRefrehLayoutOffset+UsefulFunctions.dpToPixels(1,this);
        if(movedHeight<=fadeOutOffset){
            LinearLayout weatherLayout=(LinearLayout)findViewById(R.id.weather_layout);
            float alpha=weatherLayout.getAlpha();
            long transitionTime=100;
            if(alpha!=1){
                weatherLayout.animate()
                        .alpha(1f)
                        .setDuration(transitionTime)
                        .setListener(null);
            }
        }
    }

    private boolean isSwipeRefreshLayoutPulled(float movedHeight){
        if(swipeRefreshLayout.isEnabled()==true && movedHeight>0){
            return true;
        }
        else return false;
    }

    private void setAppbarOnOffsetChangeListener(){
        AppBarLayout appBarLayout=(AppBarLayout)findViewById(R.id.app_bar);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float percentage = ((float)Math.abs(verticalOffset)/appBarLayout.getTotalScrollRange());
                changeToolbarTitleClickableViewSizeOnOffsetChanged(percentage);
                animateOnOffsetChanged(percentage,verticalOffset);
            }
        });
    }

    private void changeToolbarTitleClickableViewSizeOnOffsetChanged(float percentage){
        if(isVerticalOffsetChanged(percentage)==true){
            int clickableViewHeight= getToolbarTitleClickableViewHeightOnOffsetChanged(percentage);
            int clickableViewWidth= getToolbarTitleClickableViewWidthOnOffsetChanged(percentage);
            int clickableViewTopMargin=getToolbarTitleClickableViewTopMarginOnOffsetChanged(percentage);
            if(clickableViewHeight!=0&&clickableViewWidth!=0){
                View clickableView=findViewById(R.id.toolbar_title_clickable_view);
                CollapsingToolbarLayout.LayoutParams clickableViewParams=(CollapsingToolbarLayout.LayoutParams)clickableView.getLayoutParams();
                clickableViewParams.height=clickableViewHeight;
                clickableViewParams.width=clickableViewWidth;
                clickableViewParams.topMargin=clickableViewTopMargin;
                clickableView.setLayoutParams(clickableViewParams);
            }
        }
    }

    private boolean isVerticalOffsetChanged(float verticalOffset){
        if(verticalOffset!=previousVerticalOffset){
            previousVerticalOffset=verticalOffset;
            return true;
        }
        else{
            return false;
        }
    }

    private int getToolbarTitleClickableViewTopMarginOnOffsetChanged(float percentage){
        int expandedTitleMargin=(int)getResources().getDimension(R.dimen.expanded_toolbar_top_padding);
        int clickableViewTopMargin=(int)(expandedTitleMargin*(1-percentage));
        return clickableViewTopMargin;
    }

    private int getToolbarTitleClickableViewHeightOnOffsetChanged(float percentage){
        int verticalDifference= expandedToolbarTitleHeight -collapsedToolbarTitleHeight;
        int clickableViewHeight=expandedToolbarTitleHeight-(int)(verticalDifference*percentage);
        return clickableViewHeight;
    }

    private int getToolbarTitleClickableViewWidthOnOffsetChanged(float percentage){
        int horizontalDifference= expandedToolbarTitleWidth -collapsedToolbarTitleWidth;
        int clickableViewWidth=expandedToolbarTitleWidth-(int)(horizontalDifference*percentage);
        return clickableViewWidth;
    }

    public UsefulFunctions.OnAppBarStringsChangeListener getOnAppBarStringsChangeListener(){
        return this;
    }

    @Override
    public void onAppBarStringsChanged(){
        CollapsingToolbarLayout collapsingToolbarLayout=(net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout)findViewById(R.id.collapsing_toolbar_layout);
        getToolbarTitleSize(collapsingToolbarLayout);
    }

    private void getToolbarTitleSize(CollapsingToolbarLayout collapsingToolbarLayout){
        String collapsingToolbarTitle=collapsingToolbarLayout.getTitle().toString();
        int toolbarTitleClickableViewHorizontalPadding=(int)getResources().getDimension(R.dimen.toolbar_clickable_view_horizontal_padding);
        getExpandedToolbarTitleWidth(collapsingToolbarTitle,toolbarTitleClickableViewHorizontalPadding);
        getCollapsedToolbarTitleWidth(collapsingToolbarTitle,toolbarTitleClickableViewHorizontalPadding);
        if(expandedToolbarTitleHeight==0){
            getExpandedToolbarTitleHeight(toolbarTitleClickableViewHorizontalPadding);
            getCollapsedToolbarTitleHeight();
        }
    }

    private void getExpandedToolbarTitleWidth(String collapsingToolbarTitle,int primaryLocationClickableViewPadding){
        expandedToolbarTitleWidth =UsefulFunctions.getTextViewWidth(
                this,
                collapsingToolbarTitle,
                (int)getResources().getDimension(R.dimen.collapsing_toolbar_title_expanded_size),
                Typeface.createFromAsset(getAssets(), "Roboto-Medium.ttf"),
                primaryLocationClickableViewPadding,
                0,
                primaryLocationClickableViewPadding,
                0
        );
        int expandedToolbarTitleMaxWidth=getExpandedToolbarTitleMaxWidth();
        if(expandedToolbarTitleWidth>expandedToolbarTitleMaxWidth){
            expandedToolbarTitleWidth=expandedToolbarTitleMaxWidth;
        }
    }

    private void getCollapsedToolbarTitleWidth(String collapsingToolbarTitle,int primaryLocationClickableViewPadding){
        collapsedToolbarTitleWidth=UsefulFunctions.getTextViewWidth(
                this,
                collapsingToolbarTitle,
                (int)getResources().getDimension(R.dimen.collapsing_toolbar_title_collapsed_size),
                Typeface.createFromAsset(getAssets(), "Roboto-Medium.ttf"),
                primaryLocationClickableViewPadding,
                0,
                primaryLocationClickableViewPadding,
                0
        );
        int collapsedToolbarTitleMaxWidth=getCollapsedToolbarTitleMaxWidth();
        if(collapsedToolbarTitleWidth>collapsedToolbarTitleMaxWidth){
            collapsedToolbarTitleWidth=collapsedToolbarTitleMaxWidth;
        }
    }

    private void getExpandedToolbarTitleHeight(int primaryLocationClickableViewPadding){
        expandedToolbarTitleHeight =UsefulFunctions.getTextViewHeight(
                this,
                "",
                (int)getResources().getDimension(R.dimen.collapsing_toolbar_title_expanded_size),
                Typeface.createFromAsset(getAssets(), "Roboto-Medium.ttf"),
                primaryLocationClickableViewPadding,
                0,
                primaryLocationClickableViewPadding,
                0
        );
    }

    private void getCollapsedToolbarTitleHeight(){
        collapsedToolbarTitleHeight=(int)getResources().getDimension(R.dimen.toolbar_height);
    }

    private int getExpandedToolbarTitleMaxWidth(){
        int normalMargin=(int)getResources().getDimension(R.dimen.activity_normal_margin);
        int screenWidth=UsefulFunctions.getScreenWidth(this);
        int expandedToolbarTitleMaxWidth=screenWidth-2*normalMargin;
        return expandedToolbarTitleMaxWidth;
    }

    private int getCollapsedToolbarTitleMaxWidth(){
        int hugeMargin=(int)getResources().getDimension(R.dimen.activity_huge_margin);
        int screenWidth=UsefulFunctions.getScreenWidth(this);
        int collapsedToolbarTitleMaxWidth=screenWidth-2*hugeMargin;
        return collapsedToolbarTitleMaxWidth;
    }

    private void animateOnOffsetChanged(float percentage, int verticalOffset) {
        animateTimeLayout(percentage);
        animateSecondaryLocationNameTextView(percentage);
        animateYahooLogoLayout(percentage);
        animateSeeMoreArrow(-verticalOffset);
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

    private void animateSeeMoreArrow(int verticalOffset){
        ImageView seeMoreImageView=(ImageView)findViewById(R.id.current_weather_layout_see_more_image);
        int normalMargin=(int)this.getResources().getDimension(R.dimen.activity_normal_margin);
        if(verticalOffset>normalMargin && isTransparent==false ){
            isTransparent=true;
            UsefulFunctions.crossFade(this,null,seeMoreImageView,1);
        }
        else if(verticalOffset<=normalMargin && isTransparent==true){
            isTransparent=false;
            UsefulFunctions.crossFade(this,seeMoreImageView,null,1);
        }
    }

    private void setWeatherGeneralInfoLayoutSizes(){
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
                generalWeatherLayout.getViewTreeObserver().removeOnGlobalLayoutListener(
                        this);
            }
        });
    }

    private void setWeatherDetailsLayoutSizes(){
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

    private void setWeatherForecastLayoutSizes(){
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

    private void onWeatherServiceSuccessAfrerGeolocalization(){
        UsefulFunctions.updateLayoutData(this,weatherDataInitializer,true,true);
        geolocalizationProgressDialog.dismiss();
    }

    @Override
    public void weatherServiceSuccess(Channel channel) {
        weatherDataInitializer = new WeatherDataInitializer(channel);
        if(downloadMode==0){
            //weather service success for geolocalization
            onWeatherServiceSuccessAfrerGeolocalization();
        }
        else{
            onWeatherSuccessAfterRefresh();
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
            fadeOutOnRefreshMessageLayout();
            if(errorCode==0) {
                onRefreshWeatherInternetFailureDialog.show();
            }
            else if(errorCode==1){
                onRefreshWeatherServiceFailureDialog.show();
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
        onRefreshWeatherInternetFailureDialog =dialogInitializer.initializeInternetFailureDialog(1,refreshWeatherRunnable,setWeatherLayoutVisibleAfterRefreshFailureRunnable);
        onRefreshWeatherServiceFailureDialog =dialogInitializer.initializeServiceFailureDialog(1,refreshWeatherRunnable,setWeatherLayoutVisibleAfterRefreshFailureRunnable);
        geocodingServiceFailureDialog=dialogInitializer.initializeServiceFailureDialog(1,startGeolocalizationRunnable,null);
        permissionDeniedDialog=dialogInitializer.initializePermissionDeniedDialog(1,startGeolocalizationRunnable,null);
        coordinatesDownloadFailureDialog =dialogInitializer.initializeGeolocalizationFailureDialog(1,startGeolocalizationRunnable,null);
    }

    private void setButtonsClickable(){

        setWeatherClickable();
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
        fadeOutWeatherLayout();
        fadeInOnRefreshMessageLayout();
    }

    private void onWeatherSuccessAfterRefresh(){
        fadeOutOnRefreshMessageLayout();
        swipeRefreshLayout.setRefreshing(false);
        UsefulFunctions.updateLayoutData(this,weatherDataInitializer,false,false);
    }

    private void setWeatherLayoutVisibleAfterRefreshFailure(){
        fadeOutOnRefreshMessageLayout();
        fadeInWeatherLayout();
    }

    Runnable setWeatherLayoutVisibleAfterRefreshFailureRunnable = new Runnable() {
        public void run() {
            setWeatherLayoutVisibleAfterRefreshFailure();
        }
    };

    private void fadeInWeatherLayout(){
        final LinearLayout weatherLayout=(LinearLayout)findViewById(R.id.weather_layout);
        long transitionTime=200;
        weatherLayout.setAlpha(0f);
        weatherLayout.setVisibility(View.VISIBLE);
        weatherLayout.animate()
                .alpha(1f)
                .setDuration(transitionTime)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        UsefulFunctions.setNestedScrollViewScrollingEnabled(MainActivity.this);
                    }
                });
    }

    private void fadeOutWeatherLayout(){
        final LinearLayout weatherLayout=(LinearLayout)findViewById(R.id.weather_layout);
        long transitionTime=100;
        if(weatherLayout.getVisibility()==View.VISIBLE){
            weatherLayout.animate()
                    .alpha(0f)
                    .setDuration(transitionTime)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            weatherLayout.setVisibility(View.INVISIBLE);
                            UsefulFunctions.setNestedScrollViewScrollingDisabled(MainActivity.this);
                        }
                    });
        }
    }

    private void fadeInOnRefreshMessageLayout(){
        onRefreshMessageLayout.setAlpha(0f);
        onRefreshMessageLayout.setVisibility(View.VISIBLE);
        long transitionTime=100;
        onRefreshMessageLayout.animate()
                .alpha(1f)
                .setDuration(transitionTime)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        refreshWeatherData();
                    }
                });
    }

    private void fadeOutOnRefreshMessageLayout(){
        long transitionTime=100;
        onRefreshMessageLayout.animate()
                .alpha(0f)
                .setDuration(transitionTime)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        onRefreshMessageLayout.setVisibility(View.GONE);
                    }
                });
    }

    private void refreshWeatherData(){
        String currentLocation[]=UsefulFunctions.getCurrentLocationAddress();
        downloadWeatherData(currentLocation[0]+", "+currentLocation[1]);
    }

    private void refreshLayoutAfterUnitsPreferencesChange(){
        UsefulFunctions.updateLayoutData(this,weatherDataInitializer,true,false);
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
