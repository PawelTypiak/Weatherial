package paweltypiak.matweather;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
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
    private AlertDialog aboutDialog;
    private AlertDialog firstLoadingDialog;
    private boolean isFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFirst=true;
        setDialogs();
        downloadData();//download weather data
    }

    public void downloadData(){
        if(isFirst==true) firstLoadingDialog.show();
        else progressDialog.show();
        downloader=new DataDownloader("Poznan",this);
    }

    @Override
    public void ServiceSuccess(Channel channel) {
        if(isFirst==true) setContentView(R.layout.activity_main); //layout init
        initializeLayout(); //layout initialization
        getter = new DataGetter(channel); //data 1st step formatting
        setter = new DataSetter(this,getter); //data 2nd step formatting and setting
        Log.d("success", "success");
        if(isFirst==true) firstLoadingDialog.dismiss();
        else progressDialog.dismiss();
        isFirst=false;
    }

    @Override
    public void ServiceFailure(Exception exception) {
        exception.printStackTrace();
        failureDialog.show();
    }

    private void setDialogs(){
        initializeProgressDialog();
        initializeFirstLoadingDialog();
        initializeFailureDialog();
        initializeYahooDialog();
        initializeYahooWeatherDialog();
        initializeExitDialog();
        initializeAboutDialog();
    }

    public void initializeProgressDialog(){
        progressDialog = new ProgressDialog(this, R.style.CustomDialogStyle);
        progressDialog.setTitle(getString(R.string.downloading_data_dialog_title));
        progressDialog.setMessage(getString(R.string.downloading_data_dialog_message));
        progressDialog.setCancelable(false);
    }

    public void initializeFirstLoadingDialog(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.CustomLoadingDialogStyle);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.loading_dialog,null);
        alertBuilder.setCancelable(false);
        alertBuilder.setView(dialogView);
        firstLoadingDialog = alertBuilder.create();
    }

    public void initializeFailureDialog(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.CustomDialogStyle);
        alertBuilder.setTitle(getString(R.string.service_failure_dialog_title))
                .setIcon(R.drawable.error_icon)
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

    public void initializeYahooDialog(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.CustomDialogStyle);
        alertBuilder.setTitle(getString(R.string.yahoo_redirect_dialog_title))
                .setIcon(R.drawable.info_icon)
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

    public void initializeYahooWeatherDialog(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.CustomDialogStyle);
        alertBuilder.setTitle(getString(R.string.yahoo_weather_redirect_dialog_title))
                .setIcon(R.drawable.info_icon)
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

    public void initializeExitDialog(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.CustomDialogStyle);
        alertBuilder.setTitle(getString(R.string.exit_dialog_title))
                .setIcon(R.drawable.warning_icon)
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

    public void initializeAboutDialog(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.CustomDialogStyle);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.about_dialog,null);
        TextView aboutDesctiptionPart1=(TextView)dialogView.findViewById(R.id.about_dialog_text_part1);
        TextView aboutDesctiptionPart2=(TextView)dialogView.findViewById(R.id.about_dialog_text_part2);
        TextView aboutDesctiptionPart3=(TextView)dialogView.findViewById(R.id.about_dialog_text_part3);
        TextView aboutDesctiptionPart4=(TextView)dialogView.findViewById(R.id.about_dialog_text_part4);
        aboutDesctiptionPart1.setMovementMethod(LinkMovementMethod.getInstance());
        aboutDesctiptionPart2.setMovementMethod(LinkMovementMethod.getInstance());
        aboutDesctiptionPart3.setMovementMethod(LinkMovementMethod.getInstance());
        aboutDesctiptionPart4.setMovementMethod(LinkMovementMethod.getInstance());
        aboutDesctiptionPart1.setLinkTextColor( ContextCompat.getColor(this,R.color.textSecondaryDark));
        aboutDesctiptionPart2.setLinkTextColor( ContextCompat.getColor(this,R.color.textSecondaryDark));
        aboutDesctiptionPart3.setLinkTextColor( ContextCompat.getColor(this,R.color.textSecondaryDark));
        aboutDesctiptionPart4.setLinkTextColor( ContextCompat.getColor(this,R.color.textSecondaryDark));
        alertBuilder.setView(dialogView);
        alertBuilder
                .setPositiveButton(R.string.about_dialog_positive_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        aboutDialog = alertBuilder.create();
    }

    public void initializeLayout(){
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
        RelativeLayout yahooLayout=(RelativeLayout)findViewById(R.id.yahoo_layout);
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
        LinearLayout currentLayout=(LinearLayout)findViewById(R.id.current_layout);
        LinearLayout detailsLayout=(LinearLayout)findViewById(R.id.details_layout);
        LinearLayout forecastLayout=(LinearLayout)findViewById(R.id.forecast_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            TypedValue outValue = new TypedValue();
            this.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            currentLayout.setBackgroundResource(outValue.resourceId);
            detailsLayout.setBackgroundResource(outValue.resourceId);
            forecastLayout.setBackgroundResource(outValue.resourceId);
        }
        currentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yahooWeatherDialog.show();
            }
        });
        detailsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yahooWeatherDialog.show();
            }
        });
        forecastLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yahooWeatherDialog.show();
            }
        });
    }

    public void setRefreshClickable(){
        //handling refresh button click
        RelativeLayout refreshLayout=(RelativeLayout) findViewById(R.id.refresh_layout);
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
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_button_settings) {
            // Handle options
        } else if (id == R.id.nav_button_about) {
            //Handle about
            aboutDialog.show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
