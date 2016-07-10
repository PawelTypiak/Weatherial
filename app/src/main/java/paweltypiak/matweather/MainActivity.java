package paweltypiak.matweather;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import paweltypiak.matweather.dataDownloading.DataDownloader;
import paweltypiak.matweather.dataDownloading.DownloadCallback;
import paweltypiak.matweather.dataProcessing.DataGetter;
import paweltypiak.matweather.dataProcessing.DataSetter;
import paweltypiak.matweather.jsonHandling.Channel;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DownloadCallback{

    private DataGetter getter;
    private DataSetter setter;
    private DataDownloader downloader;
    private ProgressDialog progressDialog;
    private AlertDialog failureDialog;
    private AlertDialog yahooDialog;
    private AlertDialog yahooWeatherDialog;
    private AlertDialog exitDialog;
    private RelativeLayout yahooLayout;
    private RelativeLayout refreshLayout;
    private LinearLayout weatherLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //layout init
        setDialogs();
        downloadData();//download weather data

    }

    public void downloadData(){
        progressDialog.show();
        downloader=new DataDownloader("Poznan",this);
    }

    @Override
    public void ServiceSuccess(Channel channel) {
        setLayout(); //layout initialization
        getter = new DataGetter(channel); //data 1st step formatting
        setter = new DataSetter(this,getter); //data 2nd step formatting and setting
        Log.d("success", "success");
        progressDialog.dismiss();
    }

    @Override
    public void ServiceFailure(Exception exception) {
        exception.printStackTrace();
        failureDialog.show();
    }

    private void setDialogs(){
        setProgressDialog();
        setFailureDialog();
        setYahooDialog();
        setYahooWeatherDialog();
        setExitDialog();
    }

    public void setProgressDialog(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(getString(R.string.downloading_data_dialog_title));
        progressDialog.setMessage(getString(R.string.downloading_data_dialog_message));
        progressDialog.setCancelable(false);
    }

    public void setFailureDialog(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle(getString(R.string.service_failure_dialog_title))
                .setMessage(getString(R.string.service_failure_dialog_message))
                .setCancelable(false)
                .setPositiveButton(R.string.service_failure_dialog_positive_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        downloadData();
                    }
                })
                .setNegativeButton(R.string.service_failure_dialog_negative_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
        failureDialog = alertBuilder.create();
    }

    public void setYahooDialog(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle(getString(R.string.yahoo_redirect_dialog_title))
                .setMessage(getString(R.string.yahoo_redirect_dialog_message))
                .setPositiveButton(R.string.yahoo_redirect_dialog_positive_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String url = "https://www.yahoo.com/?ilc=401";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                })
                .setNegativeButton(R.string.yahoo_redirect_dialog_negative_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        yahooDialog = alertBuilder.create();
    }

    public void setYahooWeatherDialog(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle(getString(R.string.yahoo_weather_redirect_dialog_title))
                .setMessage(getString(R.string.yahoo_weather_redirect_dialog_message))
                .setPositiveButton(R.string.yahoo_weather_redirect_dialog_positive_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String url = "https://www.yahoo.com/news/weather/poland/greater-poland/poznan-514048";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                })
                .setNegativeButton(R.string.yahoo_weather_redirect_dialog_negative_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        yahooWeatherDialog = alertBuilder.create();
    }

    public void setExitDialog(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle(getString(R.string.exit_dialog_title))
                .setMessage(getString(R.string.exit_dialog_message))
                .setPositiveButton(R.string.exit_dialog_positive_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton(R.string.exit_dialog_negative_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        exitDialog = alertBuilder.create();
    }



    public void setLayout(){
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

    public void setButtonsClickable(){
        setYahooClickable();
        setWeatherClickable();
        setRefreshClickable();
    }

    public void setYahooClickable(){
        //handling yahoo icon click
        yahooLayout=(RelativeLayout)findViewById(R.id.yahoo_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            TypedValue outValue = new TypedValue();
            this.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            yahooLayout.setBackgroundResource(outValue.resourceId);
        }
        yahooLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yahooDialog.show();
            }
        });
    }

    public void setWeatherClickable(){
        //handling weather information click
        weatherLayout=(LinearLayout) findViewById(R.id.weather_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            TypedValue outValue = new TypedValue();
            this.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            weatherLayout.setBackgroundResource(outValue.resourceId);
        }
        weatherLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yahooWeatherDialog.show();
            }
        });
    }

    public void setRefreshClickable(){
        //handling refresh button click
        refreshLayout=(RelativeLayout) findViewById(R.id.refresh_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            TypedValue outValue = new TypedValue();
            this.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            refreshLayout.setBackgroundResource(outValue.resourceId);
        }
        refreshLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadData();
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_button_settings) {
            // Handle options
        } else if (id == R.id.nav_button_about) {
            //Handle about
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
