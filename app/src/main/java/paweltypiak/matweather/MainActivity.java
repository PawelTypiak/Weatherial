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
        //initializing all dialogs
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

    //runnables to make passing methods as parameters possible
    Runnable finishRunnable = new Runnable() {
        public void run() {
            finish();
        }
    };

    Runnable downloadDataRunnable = new Runnable() {
        public void run() {
            downloadData();
        }
    };

    public class copyToClipboardRunnable implements Runnable {
        String text;
        public copyToClipboardRunnable(String text) {
            this.text=text;
        }
        public void run() {
            copyToClipboard(text);
        }
    }

    public class initializeWebIntentRunnable implements Runnable {
        String url;
        public initializeWebIntentRunnable(String url) {
            this.url=url;
        }
        public void run() {
            initializeWebIntent(url);
        }
    }

    public class initializeEmailIntentRunnable implements Runnable {
        String address;
        String subject;
        String body;
        public initializeEmailIntentRunnable(String address, String subject, String body) {
            this.address=address;
            this.subject=subject;
            this.body=body;
        }
        public void run() {
            initializeEmailIntent(address,subject,body);
        }
    }

    private AlertDialog dialogBuilder(View dialogView, int theme, String title, int iconResource, String message, boolean ifUncancelable, String positiveButtonText, final Runnable positiveButtonFunction, String negativeButtonText,  final Runnable negativeButtonFunction){
       //custom dialog builder
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, theme);
        if(dialogView!=null)alertBuilder.setView(dialogView);
        if(title!=null) alertBuilder.setTitle(title);
        if(iconResource!=0)  alertBuilder.setIcon(iconResource);
        if(message!=null) alertBuilder.setMessage(message);
        if(ifUncancelable==true) alertBuilder.setCancelable(true);
        if(positiveButtonText!=null) {
            alertBuilder.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if(positiveButtonFunction!=null) positiveButtonFunction.run();
                }
            });
        }
        if(negativeButtonText!=null) {
            alertBuilder.setNegativeButton(negativeButtonText, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if(negativeButtonFunction!=null) negativeButtonFunction.run();
                }
            });
        }
        AlertDialog dialog = alertBuilder.create();
        return dialog;
    }

    private void initializeRefreshDialog(){
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.refresh_dialog,null);
        refreshDialog = dialogBuilder(dialogView,
                R.style.CustomDialogStyle,
                getString(R.string.refresh_dialog_title),
                0,
                null,
                true,
                null,
                null,
                null,
                null);
    }

    private void initializeNoEmailApplicationDialog(){
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.no_email_application_dialog,null);
        noEmailApplicationDialog = dialogBuilder(dialogView,R.style.CustomDialogStyle,
                getString(R.string.no_email_application_dialog_title),
                R.drawable.error_icon,
                null,
                false,
                getString(R.string.no_email_application_dialog_positive_button),
                new copyToClipboardRunnable(getString(R.string.mail_address)),
                getString( R.string.no_email_application_dialog_negative_button),
                null);
    }

    private void initializeFirstLoadingDialog(){
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.first_loading_dialog,null);
        ImageView iconImageView=(ImageView)dialogView.findViewById(R.id.first_loading_dialog_app_icon_image);
        Picasso.with(getApplicationContext()).load(R.drawable.app_icon).fit().centerInside().into(iconImageView);
        firstLoadingDialog = dialogBuilder(dialogView,
                R.style.CustomLoadingDialogStyle,
                null,
                0,
                null,
                true,null,
                null,
                null,
                null
                );
    }

    private void initializeFailureDialog(){
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.failure_dialog,null);
        failureDialog = dialogBuilder(dialogView,
                R.style.CustomDialogStyle,
                getString(R.string.service_failure_dialog_title),
                R.drawable.error_icon,
                null,
                true,
                getString(R.string.service_failure_dialog_positive_button),
                downloadDataRunnable,
                getString(R.string.service_failure_dialog_negative_button),
                finishRunnable
                );
    }

    private void initializeYahooRedirectDialog(){
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.yahoo_redirect_dialog,null);
        yahooRedirectDialog = dialogBuilder(dialogView,
                R.style.CustomDialogStyle,
                getString(R.string.yahoo_redirect_dialog_title),
                R.drawable.info_icon,
                null,
                false,
                getString(R.string.yahoo_redirect_dialog_positive_button),
                new initializeWebIntentRunnable(getString(R.string.yahoo_address)),
                getString(R.string.yahoo_redirect_dialog_negative_button),
                null
                );
    }

    private void initializeYahooWeatherRedirectDialog(){
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.yahoo_weather_redirect_dialog,null);
        yahooWeatherRedirectDialog = dialogBuilder(dialogView,
                R.style.CustomDialogStyle,
                getString(R.string.yahoo_weather_redirect_dialog_title),
                R.drawable.info_icon,
                null,
                false,
                getString(R.string.yahoo_weather_redirect_dialog_positive_button),
                new initializeWebIntentRunnable(getString(R.string.yahoo_weather_address)),
                getString(R.string.yahoo_weather_redirect_dialog_negative_button),
                null
                );
    }

    private void initializeExitDialog(){
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.exit_dialog,null);
        exitDialog = dialogBuilder(dialogView,
                R.style.CustomDialogStyle,
                getString(R.string.exit_dialog_title),
                R.drawable.warning_icon,
                null,
                false,
                getString(R.string.exit_dialog_positive_button),
                finishRunnable,
                getString(R.string.exit_dialog_negative_button),
                null
        );
    }

    private void initializeAuthorDialog(){
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.author_dialog,null);
        //setting images
        ImageView emailImageView=(ImageView)dialogView.findViewById(R.id.author_dialog_mail_image);
        ImageView githubImageView=(ImageView)dialogView.findViewById(R.id.author_dialog_github_image);
        Picasso.with(getApplicationContext()).load(R.drawable.email_icon).transform(new ColorTransformation(getResources().getColor(R.color.white))).fit().centerInside().into(emailImageView);
        Picasso.with(getApplicationContext()).load(R.drawable.github_icon).transform(new ColorTransformation(getResources().getColor(R.color.white))).fit().centerInside().into(githubImageView);
        //making links clickable and focusable
        LinearLayout mailLayout=(LinearLayout)dialogView.findViewById(R.id.author_dialog_mail_layout);
        LinearLayout githubLayout=(LinearLayout)dialogView.findViewById(R.id.author_dialog_github_layout);
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
        authorDialog = dialogBuilder(dialogView,
                R.style.CustomDialogStyle,
                getString(R.string.author_dialog_title),
                R.drawable.author_icon,
                null,
                false,
                null,
                null,
                getString(R.string.author_dialog_negative_button),
                null);
    }

    private void initializeFeedbackDialog(){
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.feedback_dialog,null);
        //making mail address clickable and focusable
        LinearLayout textLayout=(LinearLayout)dialogView.findViewById(R.id.feedback_dialog_mail_layout);
        setLayoutFocusable(textLayout);
        //copy to alipboadrd after press
        textLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                copyToClipboard(getString(R.string.mail_address));
                return false;
            }
        });
        feedbackDialog = dialogBuilder(dialogView,
                R.style.CustomDialogStyle,
                getString(R.string.feedback_dialog_title),
                R.drawable.feedback_icon,
                null,
                false,
                getString(R.string.feedback_dialog_positive_button),
                new initializeEmailIntentRunnable(getString(R.string.mail_address),getString(R.string.clipboard_mail_feedback_title),null),
                getString(R.string.feedback_dialog_negative_button),
                null
                );
    }

    private void initializeAboutDialog(){
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.about_dialog,null);
        //initializing clickable hyperlinks
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
        //setting app icon
        ImageView iconImageView=(ImageView)dialogView.findViewById(R.id.about_dialog_app_icon_image);
        Picasso.with(getApplicationContext()).load(R.drawable.app_icon).fit().centerInside().into(iconImageView);
        //initializing dialog
        aboutDialog = dialogBuilder(dialogView,
                R.style.CustomDialogStyle,
                null,
                0,
                null,
                false,
                null,
                null,
                getString(R.string.about_dialog_negative_button),
                null);
    }

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

    private void initializeWebIntent(String url){
        //initialize web intent
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
    }

    private void initializeEmailIntent(String address, String subject, String body){
        //initialize email intent
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

    private void copyToClipboard(String text){
        //initialize copy to clipboard
        ClipboardManager myClipboard;
        ClipData myClip;
        myClipboard = (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
        myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);
        Toast.makeText(getApplicationContext(), getString(R.string.clipboard_toast_message),Toast.LENGTH_SHORT).show();
    }

    private void setLayoutFocusable(LinearLayout layout){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            TypedValue outValue = new TypedValue();
            this.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            layout.setBackgroundResource(outValue.resourceId);
        }
    }

    private void setButtonsClickable(){
        setYahooClickable();
        setWeatherClickable();
        setRefreshClickable();
    }

    private void setYahooClickable(){
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

    private void setRefreshClickable(){
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
