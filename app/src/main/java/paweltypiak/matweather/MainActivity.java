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

import paweltypiak.matweather.weatherDataDownloading.WeatherDataDownloader;
import paweltypiak.matweather.weatherDataDownloading.WeatherDownloadCallback;
import paweltypiak.matweather.weatherDataProcessing.WeatherDataInitializer;
import paweltypiak.matweather.weatherDataProcessing.WeatherDataSetter;

import static paweltypiak.matweather.weatherDataProcessing.WeatherDataSetter.getTimeThreadStartedFlag;
import static paweltypiak.matweather.weatherDataProcessing.WeatherDataSetter.setStartTimeThread;
import paweltypiak.matweather.jsonHandling.Channel;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, WeatherDownloadCallback, SwipeRefreshLayout.OnRefreshListener{

    private WeatherDataInitializer getter;
    private WeatherDataSetter setter;
    private WeatherDataDownloader downloader;
    private AlertDialog refreshDialog;
    private AlertDialog serviceFailureDialog;
    private AlertDialog internetFailureDialog;
    private AlertDialog yahooRedirectDialog;
    private AlertDialog yahooWeatherRedirectDialog;
    private AlertDialog exitDialog;
    private AlertDialog aboutDialog;
    private AlertDialog firstLoadingDialog;
    private AlertDialog feedbackDialog;
    private AlertDialog authorDialog;
    private AlertDialog noEmailApplicationDialog;
    private AlertDialog searchDialog;
    private AlertDialog mapsDialog;
    private AlertDialog addToFavouritesDialog;
    private AlertDialog editFavouritesDialog;
    private DialogInitializer dialogInitializer;
    private SwipeRefreshLayout swipeRefreshLayout;
    private String location;
    private CoordinatorLayout mainLayout;
    private LinearLayout weatherLayout;
    private TextView refreshMessageTextView;
    private ImageView refreshImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadExtras();
        UsefulFunctions.setIsFirst(true);
        new UsefulFunctions().new SharedPreferencesReader(MainActivity.this);
        initializeLayout(); //layout initialization
        loadFirstLocation();
    }

    public void downloadData(String location) {
        downloader=new WeatherDataDownloader(location,this);
    }
    private void loadExtras(){
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            getter=extras.getParcelable(getString(R.string.extras_data_initializer_key));
        }
    }

    private void loadFirstLocation(){
        setter = new WeatherDataSetter(this,getter); //data formatting and weather layout setting
        UsefulFunctions.setViewVisible(mainLayout);
        UsefulFunctions.setIsFirst(false);
    }

    @Override
    public void ServiceSuccess(Channel channel) {
        getter = new WeatherDataInitializer(this,channel); //initializing weather data from JSON
        setter = new WeatherDataSetter(this,getter); //data formatting and weather layout setting
        swipeRefreshLayout.setRefreshing(false);
        UsefulFunctions.setViewInvisible(refreshMessageTextView);
        UsefulFunctions.setViewVisible(mainLayout);
        UsefulFunctions.setViewVisible(weatherLayout);
    }
    @Override
    public void ServiceFailure(int errorCode) {
        //failure handling
        swipeRefreshLayout.setRefreshing(false);
        UsefulFunctions.setViewInvisible(refreshMessageTextView);
        if(errorCode==1) {
            internetFailureDialog=dialogInitializer.initializeInternetFailureDialog(false,refreshRunnable,null);
            internetFailureDialog.show();
        }
        else serviceFailureDialog.show();
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

    private void setDialogs(){
        dialogInitializer=new DialogInitializer(this);
        firstLoadingDialog=dialogInitializer.initializeFirstLoadingDialog();
        serviceFailureDialog =dialogInitializer.initializeServiceFailureDialog(downloadDataRunnable,null);
        yahooRedirectDialog=dialogInitializer.initializeYahooRedirectDialog();
        exitDialog=dialogInitializer.initializeExitDialog(false,null);
        aboutDialog=dialogInitializer.initializeAboutDialog();
        feedbackDialog=dialogInitializer.initializeFeedbackDialog();
        authorDialog=dialogInitializer.initializeAuthorDialog();
        searchDialog=dialogInitializer.initializeSearchDialog();
    }

    Runnable downloadDataRunnable = new Runnable() {
        public void run() {
            downloadData(location);
        }
    };
    Runnable refreshRunnable = new Runnable() {
        public void run() {
            onRefresh();
        }
    };

    private void initializeLayout(){
        setContentView(R.layout.activity_main);
        mainLayout=(CoordinatorLayout)findViewById(R.id.main_coordinator_layout);
        UsefulFunctions.setViewInvisible(mainLayout);
        //toolbar init
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //3-stripe toggle init
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //navigation drawer init
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
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
        //handling yahoo icon click
        LinearLayout yahooLayout=(LinearLayout)findViewById(R.id.yahoo_layout);
        yahooLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yahooRedirectDialog.show();
            }
        });
    }

    private void setWeatherClickable(){
        //handling weather information click
        LinearLayout currentLayout=(LinearLayout)findViewById(R.id.current_layout);
        LinearLayout detailsLayout=(LinearLayout)findViewById(R.id.details_layout);
        LinearLayout forecastLayout=(LinearLayout)findViewById(R.id.forecast_layout);
        currentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yahooWeatherRedirectDialog= WeatherDataSetter.getYahooWeatherRedirectDialog();
                yahooWeatherRedirectDialog.show();           }
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
        //handling search button click
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
        final FloatingActionButton floatingActionButton=(FloatingActionButton)findViewById(R.id.main_fab);
        floatingActionButton.setImageResource(R.drawable.add_black_icon);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            int mark=1;
            @Override
            public void onClick(View v) {
                if(mark==1){
                    floatingActionButton.setImageResource(R.drawable.edit_black_icon);
                    editFavouritesDialog=dialogInitializer.initializeEditFavourites();
                    editFavouritesDialog.show();
                    mark=2;
                }
                else {
                    floatingActionButton.setImageResource(R.drawable.add_black_icon);
                    addToFavouritesDialog=dialogInitializer.initializeAddToFavourites();
                    addToFavouritesDialog.show();
                    mark=1;
                }
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
        swipeRefreshLayout.setRefreshing(true);    //dialog when refresh
        refreshMessageTextView.setTextColor(Color.argb(255, 255, 255, 255));
        refreshMessageTextView.setText(getString(R.string.refresh_message_refreshing));
        UsefulFunctions.setViewVisible(refreshMessageTextView);
        UsefulFunctions.setViewInvisible(weatherLayout);
        downloadData(WeatherDataSetter.getCurrentDataFormatter().getCity());
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent e) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            //opening navigation drawer on back press
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_button_settings) {
            // Handle options
        } else if (id == R.id.nav_button_about) {
            //Handle about
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
            WeatherDataSetter.newRefresh=true;
        }
        super.onResume();
    }
    @Override
    protected void onDestroy() {
        WeatherDataSetter.interruptUiThread();
        super.onDestroy();
    }
}
