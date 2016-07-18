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
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import paweltypiak.matweather.dataDownloading.DataDownloader;
import paweltypiak.matweather.dataDownloading.DownloadCallback;
import paweltypiak.matweather.dataProcessing.DataInitializer;
import paweltypiak.matweather.dataProcessing.DataSetter;
import paweltypiak.matweather.jsonHandling.Channel;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DownloadCallback, SwipeRefreshLayout.OnRefreshListener{

    private DataInitializer getter;
    private DataSetter setter;
    private DataDownloader downloader;
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
    private boolean isFirst;
    private DialogInitializer dialogInitializer;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView searchImageView;
    private int temperature;
    private int time;
    private int pressure;
    private int distance;
    private int speed;
    private String localization;
    private CoordinatorLayout mainLayout;
    private LinearLayout weatherLayout;
    private TextView refreshMessageTextView;
    private ImageView refreshImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFirst=true;
        setContentView(R.layout.activity_main);
        initializeLayout(); //layout initialization
        setSwipeRefreshLayout();
        setFloatingActionButton();
        setDialogs();
        localization ="zamosc";
        downloadData(localization);//download weather data
    }


    public void downloadData(String localization){
        if(isFirst==true) {
            firstLoadingDialog.show();    //dialog at the beginning
        }
        downloader=new DataDownloader(localization,this);   //downloading weather data for Poznan
    }

    @Override
    public void ServiceSuccess(Channel channel) {
        Log.d("successisfirst", "successisfirst "+ isFirst);
        if(isFirst==true) {
            getter = new DataInitializer(this,channel, unitInitializer(0,0,0,0,0)); //initializing weather data from JSON
            setter = new DataSetter(this,getter); //data formatting and weather layout setting
            UsefulFunctions.setViewVisible(mainLayout);
            firstLoadingDialog.dismiss();
        }
        else {
            getter = new DataInitializer(this,channel, unitInitializer(0,0,0,0,0)); //initializing weather data from JSON
            setter = new DataSetter(this,getter); //data formatting and weather layout setting
            swipeRefreshLayout.setRefreshing(false);
            UsefulFunctions.setViewInvisible(refreshMessageTextView);
            UsefulFunctions.setViewVisible(weatherLayout);
        }
        isFirst=false;  //first loading done
    }

    @Override
    public void ServiceFailure(int errorCode) {
        //failure handling
        if(isFirst==true) {

            firstLoadingDialog.dismiss();
        }
        else {
            swipeRefreshLayout.setRefreshing(false);
            UsefulFunctions.setViewInvisible(refreshMessageTextView);
        }
        if(errorCode==1) internetFailureDialog.show();
        else serviceFailureDialog.show();
        isFirst=false;
    }

    private int[] unitInitializer(int time, int temperature, int speed, int distance, int pressure){
        int [] units={time, temperature, speed, distance, pressure};
        return units;
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

                    int alpha=UsefulFunctions.getPullOpacity(0.15,movedHeight,MainActivity.this,true);
                    refreshMessageTextView.setTextColor(Color.argb(alpha, 255, 255, 255));
                    refreshImageView.setAlpha(alpha);
                }
                if(event.getAction()==MotionEvent.ACTION_UP){
                    isTouched=false;
                    UsefulFunctions.setViewInvisible(refreshImageView);
                    UsefulFunctions.setViewInvisible(refreshMessageTextView);
                }
                return false;
            }
        });
    }

    private void setDialogs(){
        dialogInitializer=new DialogInitializer(this,downloadDataRunnable,unitInitializer(0,0,0,0,0));
        refreshDialog=dialogInitializer.getRefreshDialog();
        firstLoadingDialog=dialogInitializer.getFirstLoadingDialog();
        serviceFailureDialog =dialogInitializer.getServiceFailureDialog();
        internetFailureDialog=dialogInitializer.getInternetFailureDialog();
        yahooRedirectDialog=dialogInitializer.getYahooRedirectDialog();
        yahooWeatherRedirectDialog=dialogInitializer.getYahooWeatherRedirectDialog();
        exitDialog=dialogInitializer.getExitDialog();
        noEmailApplicationDialog=dialogInitializer.getNoEmailApplicationDialog();
        aboutDialog=dialogInitializer.getAboutDialog();
        feedbackDialog=dialogInitializer.getFeedbackDialog();
        authorDialog=dialogInitializer.getAuthorDialog();
        searchDialog=dialogInitializer.getSearchDialog();
    }

    Runnable downloadDataRunnable = new Runnable() {
        public void run() {
            downloadData(localization);
        }
    };

    private void initializeLayout(){
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
        setButtonsClickable();
    }

    private void setButtonsClickable(){
        setYahooClickable();
        setWeatherClickable();
        setSearchClickable();
    }

    private void setYahooClickable(){
        //handling yahoo icon click
        LinearLayout yahooLayout=(LinearLayout)findViewById(R.id.yahoo_layout);
        UsefulFunctions.setLayoutFocusable(this,yahooLayout);
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
                yahooWeatherRedirectDialog.show();
            }
        });
        detailsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yahooWeatherRedirectDialog.show();
            }
        });
        forecastLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yahooWeatherRedirectDialog.show();
            }
        });
    }

    private void setSearchClickable(){
        //handling search button click
        searchImageView =(ImageView)findViewById(R.id.search_image);
        LinearLayout searchLayout=(LinearLayout) findViewById(R.id.search_layout);
        //UsefulFunctions.setLayoutFocusable(this,searchLayout);
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

    private void setFloatingActionButton(){
        final FloatingActionButton floatingActionButton=(FloatingActionButton)findViewById(R.id.main_fab);
        floatingActionButton.setImageResource(R.drawable.add_black_icon);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            boolean mark=false;

            @Override
            public void onClick(View v) {
                if(mark==false){
                    floatingActionButton.setImageResource(R.drawable.add_black_icon);
                    mark=true;
                }
                else {
                    floatingActionButton.setImageResource(R.drawable.remove_black_icon);
                    mark=false;
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
        UsefulFunctions.setViewVisible(refreshMessageTextView);
        refreshMessageTextView.setText(getString(R.string.refresh_message_refreshing));
        UsefulFunctions.setViewInvisible(weatherLayout);
        downloadData(localization);
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
}
