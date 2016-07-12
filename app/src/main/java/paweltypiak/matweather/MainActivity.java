package paweltypiak.matweather;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.method.LinkMovementMethod;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import paweltypiak.matweather.dataDownloading.DataDownloader;
import paweltypiak.matweather.dataDownloading.DownloadCallback;
import paweltypiak.matweather.dataProcessing.DataInitializer;
import paweltypiak.matweather.dataProcessing.DataSetter;
import paweltypiak.matweather.jsonHandling.Channel;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DownloadCallback{

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isFirst=true;
        setDialogs();
        downloadData();//download weather data
    }

    public void downloadData(){
        if(isFirst==true) firstLoadingDialog.show();    //dialog at the beginning
        else refreshDialog.show();     //dialog when refresh
        downloader=new DataDownloader("Poznan",this);   //downloading weather data for Poznan
    }

    @Override
    public void ServiceSuccess(Channel channel) {
        // success handling
        if(isFirst==true) setContentView(R.layout.activity_main); //layout init
        initializeLayout(); //layout initialization
        getter = new DataInitializer(channel); //initializing weather data from JSON
        setter = new DataSetter(this,getter); //data formatting and weather layout setting
        //progress dialogs dismiss
        if(isFirst==true) firstLoadingDialog.dismiss();
        else refreshDialog.dismiss();
        isFirst=false;  //first loading done
    }

    @Override
    public void ServiceFailure(Exception exception) {
        //failure handling
        exception.printStackTrace();
        failureDialog.show();
    }

    private void setDialogs(){
        initializeRefreshDialog();
        initializeFirstLoadingDialog();
        initializeFailureDialog();
        initializeYahooRedirectDialog();
        initializeYahooWeatherRedirectDialog();
        initializeExitDialog();
        initializeAboutDialog();
        initializeFeedbackDialog();
        initializeNoEmailApplicationDialog();
        initializeAuthorDialog();
    }

    public void initializeRefreshDialog(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.CustomDialogStyle);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.refresh_dialog,null);
        alertBuilder.setView(dialogView);
        alertBuilder.setTitle(getString(R.string.refresh_dialog_title))
                .setCancelable(false);
        refreshDialog = alertBuilder.create();
    }

    public void initializeNoEmailApplicationDialog(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.CustomDialogStyle);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.no_email_application_dialog,null);
        alertBuilder.setView(dialogView);
        alertBuilder.setTitle(getString(R.string.no_email_application_dialog_title))
                .setIcon(R.drawable.error_icon)
                .setPositiveButton(R.string.no_email_application_dialog_positive_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       copyToClipboard(getString(R.string.mail_address));
                    }
                })
                .setNegativeButton(R.string.no_email_application_dialog_negative_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        noEmailApplicationDialog = alertBuilder.create();
    }

    public void initializeFirstLoadingDialog(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.CustomLoadingDialogStyle);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.first_loading_dialog,null);
        ImageView iconImageView=(ImageView)dialogView.findViewById(R.id.first_loading_dialog_app_icon_image);
        Picasso.with(getApplicationContext()).load(R.drawable.app_icon).fit().centerInside().into(iconImageView);
        alertBuilder.setCancelable(false);
        alertBuilder.setView(dialogView);
        firstLoadingDialog = alertBuilder.create();
    }

    public void initializeFailureDialog(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.CustomDialogStyle);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.failure_dialog,null);
        alertBuilder.setView(dialogView);
        alertBuilder.setTitle(getString(R.string.service_failure_dialog_title))
                .setIcon(R.drawable.error_icon)
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

    public void initializeYahooRedirectDialog(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.CustomDialogStyle);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.yahoo_redirect_dialog,null);
        alertBuilder.setView(dialogView);
        alertBuilder.setTitle(getString(R.string.yahoo_redirect_dialog_title))
                .setIcon(R.drawable.info_icon)
                .setPositiveButton(R.string.yahoo_redirect_dialog_positive_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        initializeWebIntent(getString(R.string.yahoo_address));
                    }
                })
                .setNegativeButton(R.string.yahoo_redirect_dialog_negative_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        yahooRedirectDialog = alertBuilder.create();
    }

    public void initializeYahooWeatherRedirectDialog(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.CustomDialogStyle);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.yahoo_weather_redirect_dialog,null);
        alertBuilder.setView(dialogView);
        alertBuilder.setTitle(getString(R.string.yahoo_weather_redirect_dialog_title))
                .setIcon(R.drawable.info_icon)
                .setPositiveButton(R.string.yahoo_weather_redirect_dialog_positive_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        initializeWebIntent(getString(R.string.yahoo_weather_address));
                    }
                })
                .setNegativeButton(R.string.yahoo_weather_redirect_dialog_negative_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        yahooWeatherRedirectDialog = alertBuilder.create();
    }

    public void initializeExitDialog(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.CustomDialogStyle);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.exit_dialog,null);
        alertBuilder.setView(dialogView);
        alertBuilder.setTitle(getString(R.string.exit_dialog_title))
                .setIcon(R.drawable.warning_icon)
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

    public void initializeAuthorDialog(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.CustomDialogStyle);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.author_dialog,null);
        ImageView emailImageView=(ImageView)dialogView.findViewById(R.id.author_dialog_mail_image);
        ImageView githubImageView=(ImageView)dialogView.findViewById(R.id.author_dialog_github_image);
        LinearLayout mailLayout=(LinearLayout)dialogView.findViewById(R.id.author_dialog_mail_layout);
        LinearLayout githubLayout=(LinearLayout)dialogView.findViewById(R.id.author_dialog_github_layout);
        Picasso.with(getApplicationContext()).load(R.drawable.email_icon).transform(new ColorTransformation(getResources().getColor(R.color.white))).fit().centerInside().into(emailImageView);
        Picasso.with(getApplicationContext()).load(R.drawable.github_icon).transform(new ColorTransformation(getResources().getColor(R.color.white))).fit().centerInside().into(githubImageView);
        setLayoutFocusable(mailLayout);
        setLayoutFocusable(githubLayout);
        mailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeEmailIntent(getString(R.string.mail_address),null,null);
            }
        });
        githubLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeWebIntent(getString(R.string.github_address));
            }
        });
        alertBuilder.setView(dialogView);
        alertBuilder.setTitle(getString(R.string.author_dialog_title))
                .setIcon(R.drawable.author_icon)
                .setNegativeButton(R.string.author_dialog_negative_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        authorDialog = alertBuilder.create();
    }

    public void initializeFeedbackDialog(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, R.style.CustomDialogStyle);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.feedback_dialog,null);

        LinearLayout textLayout=(LinearLayout)dialogView.findViewById(R.id.feedback_dialog_mail_layout);
        setLayoutFocusable(textLayout);
        textLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                copyToClipboard(getString(R.string.mail_address));
                return false;
            }
        });
        alertBuilder.setView(dialogView);
        alertBuilder.setTitle(getString(R.string.feedback_dialog_title))
                .setIcon(R.drawable.feedback_icon)
                .setPositiveButton(R.string.feedback_dialog_positive_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        initializeEmailIntent(getString(R.string.mail_address),getString(R.string.clipboard_mail_feedback_title),null);
                    }
                })
                .setNegativeButton(R.string.feedback_dialog_negative_button, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });
        feedbackDialog = alertBuilder.create();
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
        ImageView iconImageView=(ImageView)dialogView.findViewById(R.id.about_dialog_app_icon_image);
        Picasso.with(getApplicationContext()).load(R.drawable.app_icon).fit().centerInside().into(iconImageView);
        alertBuilder.setView(dialogView);
        alertBuilder
                .setNegativeButton(R.string.about_dialog_negative_button, new DialogInterface.OnClickListener() {
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

    public void initializeWebIntent(String url){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    public void initializeEmailIntent(String address, String subject, String body){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/plain");
        if(subject!=null) intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if(body!=null)intent.putExtra(Intent.EXTRA_TEXT, body);
        intent.setData(Uri.parse("mailto:"+address));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            noEmailApplicationDialog.show();
        }
    }

    public void copyToClipboard(String text){
        ClipboardManager myClipboard;
        ClipData myClip;
        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);
        Toast.makeText(getApplicationContext(), getString(R.string.clipboard_toast_message),Toast.LENGTH_SHORT).show();
    }

    public void setLayoutFocusable(LinearLayout layout){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            TypedValue outValue = new TypedValue();
            this.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            layout.setBackgroundResource(outValue.resourceId);
        }
    }

    public void setButtonsClickable(){
        setYahooClickable();
        setWeatherClickable();
        setRefreshClickable();
    }

    public void setYahooClickable(){
        //handling yahoo icon click
        LinearLayout yahooLayout=(LinearLayout)findViewById(R.id.yahoo_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            TypedValue outValue = new TypedValue();
            this.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            yahooLayout.setBackgroundResource(outValue.resourceId);
        }
        yahooLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yahooRedirectDialog.show();
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

    public void setRefreshClickable(){
        //handling refresh button click
        LinearLayout refreshLayout=(LinearLayout) findViewById(R.id.refresh_layout);
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
