package paweltypiak.matweather;

import android.app.AlertDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

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
    private AlertDialog failureDialog;
    private AlertDialog yahooRedirectDialog;
    private AlertDialog yahooWeatherRedirectDialog;
    private AlertDialog exitDialog;
    private AlertDialog aboutDialog;
    private AlertDialog firstLoadingDialog;
    private AlertDialog feedbackDialog;
    private AlertDialog authorDialog;
    private AlertDialog noEmailApplicationDialog;
    private boolean isFirst;
    private DialogInitializer dialogInitializer;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView favouritesImageView;
    private int temperature;
    private int time;
    private int pressure;
    private int distance;
    private int speed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFirst=true;
        setContentView(R.layout.activity_main);

        setDialogs();
        downloadData();//download weather data
    }


    public void downloadData(){
        if(isFirst==true) firstLoadingDialog.show();    //dialog at the beginning
        else swipeRefreshLayout.setRefreshing(true);    //dialog when refresh
        downloader=new DataDownloader("zamosc",this);   //downloading weather data for Poznan
    }

    @Override
    public void ServiceSuccess(Channel channel) {
        Log.d("successisfirst", "successisfirst "+ isFirst);
        if(isFirst==true) {
            initializeLayout(); //layout initialization
            getter = new DataInitializer(this,channel,units(0,0,0,0,0)); //initializing weather data from JSON
            setter = new DataSetter(this,getter); //data formatting and weather layout setting
            firstLoadingDialog.dismiss();
        }
        else {
            getter = new DataInitializer(this,channel,units(0,0,0,0,0)); //initializing weather data from JSON
            setter = new DataSetter(this,getter); //data formatting and weather layout setting
            swipeRefreshLayout.setRefreshing(false);
        }
        isFirst=false;  //first loading done
    }

    @Override
    public void ServiceFailure(Exception exception) {
        Log.d("failure", "failure ");
        Log.d("failureisfirst", "failureisfirst "+ isFirst);
        //failure handling
        if(isFirst==true) {

            firstLoadingDialog.dismiss();
        }
        else swipeRefreshLayout.setRefreshing(false);
        exception.printStackTrace();
        failureDialog.show();
        isFirst=false;
    }

    private int[] units(int time, int temperature, int speed, int distance, int pressure){
        int [] units={time, temperature, speed, distance, pressure};
        return units;
    }

    private void setSwipeRefreshLayout(){
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
    }

    private void setDialogs(){
        dialogInitializer=new DialogInitializer(this,downloadDataRunnable);
        refreshDialog=dialogInitializer.getRefreshDialog();
        firstLoadingDialog=dialogInitializer.getFirstLoadingDialog();
        failureDialog=dialogInitializer.getFailureDialog();
        yahooRedirectDialog=dialogInitializer.getYahooRedirectDialog();
        yahooWeatherRedirectDialog=dialogInitializer.getYahooWeatherRedirectDialog();
        exitDialog=dialogInitializer.getExitDialog();
        noEmailApplicationDialog=dialogInitializer.getNoEmailApplicationDialog();
        aboutDialog=dialogInitializer.getAboutDialog();
        feedbackDialog=dialogInitializer.getFeedbackDialog();
        authorDialog=dialogInitializer.getAuthorDialog();
    }

    Runnable downloadDataRunnable = new Runnable() {
        public void run() {
            downloadData();
        }
    };

    private void initializeLayout(){
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
        setButtonsClickable();
    }

    private void setButtonsClickable(){
        setYahooClickable();
        setWeatherClickable();
        setFavouritesClickable();
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

    private void setFavouritesClickable(){
        //handling favourites button click

        favouritesImageView=(ImageView)findViewById(R.id.favourites_image);
        LinearLayout refreshLayout=(LinearLayout) findViewById(R.id.favourites_layout);
        Picasso.with(getApplicationContext()).load(R.drawable.favourites_empty_icon).transform(new UsefulFunctions().new setDrawableColor(getResources().getColor(R.color.white))).into(favouritesImageView);

        refreshLayout.setOnClickListener(new View.OnClickListener() {
            boolean mark=false;

            @Override
            public void onClick(View v) {
                if(mark==false){
                    Picasso.with(getApplicationContext()).load(R.drawable.favourites_full_icon).transform(new UsefulFunctions().new setDrawableColor(getResources().getColor(R.color.white))).into(favouritesImageView);
                    mark=true;
                }
                else {
                    Picasso.with(getApplicationContext()).load(R.drawable.favourites_empty_icon).transform(new UsefulFunctions().new setDrawableColor(getResources().getColor(R.color.white))).into(favouritesImageView);
                    mark=false;
                }

            }
        });
    }

    private void setFloatingActionButton(){
        FloatingActionButton floatingActionButton=(FloatingActionButton)findViewById(R.id.main_fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
        downloadData();
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
