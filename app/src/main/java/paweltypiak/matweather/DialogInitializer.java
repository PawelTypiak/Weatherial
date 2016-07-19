package paweltypiak.matweather;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import paweltypiak.matweather.dataDownloading.DataDownloader;
import paweltypiak.matweather.dataDownloading.DownloadCallback;
import paweltypiak.matweather.dataProcessing.DataInitializer;
import paweltypiak.matweather.dataProcessing.DataSetter;
import paweltypiak.matweather.jsonHandling.Channel;

public class DialogInitializer  {

    private AlertDialog refreshDialog;
    private AlertDialog serviceFailureDialog;
    private AlertDialog yahooRedirectDialog;
    private AlertDialog yahooWeatherRedirectDialog;
    private AlertDialog exitDialog;
    private AlertDialog aboutDialog;
    private AlertDialog firstLoadingDialog;
    private AlertDialog feedbackDialog;
    private AlertDialog authorDialog;
    private AlertDialog noEmailApplicationDialog;
    private AlertDialog searchDialog;
    private AlertDialog searchProgressDialog;
    private AlertDialog noLocalizationResultsDialog;
    private AlertDialog loalizationResultsDialog;
    private AlertDialog emptyLocationNameDialog;
    private AlertDialog internetFailureDialog;
    private AlertDialog mapsDialog;
    private AlertDialog addToFavouritesDialog;
    private AlertDialog deleteFromFavouritesDialog;
    private Activity activity;
    private int[] units;


   public DialogInitializer(Activity activity, Runnable reloadRunnable, int[] units){

       this.units=units;
       this.activity=activity;
       this.refreshDialog=initializeRefreshDialog();
       this.firstLoadingDialog=initializeFirstLoadingDialog();
       this.serviceFailureDialog = initializeServiceFailureDialog(reloadRunnable);
       this.internetFailureDialog=initializeInternetFailureDialog(reloadRunnable);
       this.yahooRedirectDialog=initializeYahooRedirectDialog();
       this.yahooWeatherRedirectDialog=initializeYahooWeatherRedirectDialog();
       this.exitDialog=initializeExitDialog();
       this.noEmailApplicationDialog=initializeNoEmailApplicationDialog();
       this.aboutDialog=initializeAboutDialog();
       this.feedbackDialog=initializeFeedbackDialog();
       this.authorDialog=initializeAuthorDialog();
       this.searchDialog=initializeSearchDialog();
       this.searchProgressDialog=initializeSearchProgressDialog();
       this.noLocalizationResultsDialog=initializeNoLocalizationResultsDialog();
       this.emptyLocationNameDialog=initializeEmptyLocationNameDialog();


    }

    public DialogInitializer(Activity activity){

        this.activity=activity;
        this.refreshDialog=initializeRefreshDialog();
        this.firstLoadingDialog=initializeFirstLoadingDialog();
        this.yahooRedirectDialog=initializeYahooRedirectDialog();
        this.yahooWeatherRedirectDialog=initializeYahooWeatherRedirectDialog();
        this.exitDialog=initializeExitDialog();
        this.noEmailApplicationDialog=initializeNoEmailApplicationDialog();
        this.aboutDialog=initializeAboutDialog();
        this.feedbackDialog=initializeFeedbackDialog();
        this.authorDialog=initializeAuthorDialog();
        this.searchDialog=initializeSearchDialog();
        this.searchProgressDialog=initializeSearchProgressDialog();
        this.noLocalizationResultsDialog=initializeNoLocalizationResultsDialog();
        this.emptyLocationNameDialog=initializeEmptyLocationNameDialog();
    }

    //runnables to make passing methods as parameters possible
    private Runnable finishRunnable = new Runnable() {
        public void run() {
            activity.finish();
        }
    };

    private class showDialogRunnable implements Runnable {
        AlertDialog dialog;
        boolean ifShowKeyboard;
        public showDialogRunnable(AlertDialog dialog, boolean ifShowKeyboard) {
            this.dialog=dialog;
            this.ifShowKeyboard=ifShowKeyboard;
        }
        public void run() {
            dialog.show();
            if(ifShowKeyboard==true) UsefulFunctions.showKeyboard(activity);
        }
    }

    private class copyToClipboardRunnable implements Runnable {
        String text;
        public copyToClipboardRunnable(String text) {
            this.text=text;
        }
        public void run() {
            UsefulFunctions.copyToClipboard(activity,text);
        }
    }

    private class initializeWebIntentRunnable implements Runnable {
        String url;
        public initializeWebIntentRunnable(String url) {
            this.url=url;
        }
        public void run() {
            UsefulFunctions.initializeWebIntent(activity,url);
        }
    }

    private class initializeEmailIntentRunnable implements Runnable {
        String address;
        String subject;
        String body;
        AlertDialog dialog;
        public initializeEmailIntentRunnable(String address, String subject, String body, AlertDialog dialog) {
            this.dialog=dialog;
            this.address=address;
            this.subject=subject;
            this.body=body;
        }
        public void run() {
            UsefulFunctions.initializeEmailIntent(activity,address,subject,body,dialog);
        }
    }

    private class setMainLayoutRunnable implements Runnable {
        DataInitializer dataInitializer;
        DataSetter dataSetter;
        public setMainLayoutRunnable(DataInitializer dataInitializer) {
            this.dataInitializer=dataInitializer;
        }
        public void run() {
            dataSetter=new DataSetter(activity,dataInitializer);
        }
    }




    private class searchRunnable implements Runnable, DownloadCallback{
        private String location;
        private DataDownloader downloader;
        private DownloadCallback downloadCallback;
        private DataInitializer dataInitializer;
        private Activity activity;
        private int []units;
        private View dialogView;
        private EditText locationEditText;
        private String city;
        private String region;
        private String country;


        public searchRunnable(View dialogView, Activity activity, int[] units){
            this.dialogView=dialogView;
            this.units=units;
            this.activity=activity;
        }

        public void run(){

            locationEditText=(EditText)dialogView.findViewById(R.id.search_edit_text);
            location=locationEditText.getText().toString();
            UsefulFunctions.hideKeyboard(activity,locationEditText);
            if(location.length()==0) emptyLocationNameDialog.show();
            else {
                locationEditText.getText().clear();
                searchProgressDialog.show();
                downloader=new DataDownloader(location,this);
            }
            //UsefulFunctions.hideKeyboard(activity);
        }

        @Override
        public void ServiceSuccess(Channel channel) {
            dataInitializer=new DataInitializer(activity,channel,units);

            loalizationResultsDialog=initializeLocalizationResultsDialog(dataInitializer);
            loalizationResultsDialog.show();
            searchProgressDialog.dismiss();
        }

        @Override
        public void ServiceFailure(int errorCode) {
            noLocalizationResultsDialog.show();
            searchProgressDialog.dismiss();
        }
    }

    private AlertDialog buildDialog(View dialogView, int theme, String title, int iconResource, String message, boolean ifUncancelable, String positiveButtonText, final Runnable positiveButtonFunction, String neutralButtonText, final Runnable neutralButtonFunction, String negativeButtonText, final Runnable negativeButtonFunction){
        //custom dialog builder
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity, theme);
        if(dialogView!=null)alertBuilder.setView(dialogView);
        if(title!=null) alertBuilder.setTitle(title);
        if(iconResource!=0)  alertBuilder.setIcon(iconResource);
        if(message!=null) alertBuilder.setMessage(message);
        if(ifUncancelable==true) alertBuilder.setCancelable(false);
        if(positiveButtonText!=null) {
            alertBuilder.setPositiveButton(positiveButtonText, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if(positiveButtonFunction!=null) positiveButtonFunction.run();
                }
            });
        }
        if(neutralButtonText!=null) {
            alertBuilder.setNeutralButton(neutralButtonText, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    if(neutralButtonFunction!=null) neutralButtonFunction.run();
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

    private AlertDialog initializeSearchDialog(){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.search_dialog,null);
        final EditText locationEditText=(EditText)dialogView.findViewById(R.id.search_edit_text);
        /*InputMethodManager imm = (InputMethodManager)activity.getSystemService(activity.INPUT_METHOD_SERVICE);
        imm.showSoftInput(locationEditText, InputMethodManager.SHOW_IMPLICIT);*/

        AlertDialog searchDialog=buildDialog(
                dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.search_dialog_title),
                R.drawable.search_blue_icon,
                null,
                false,
                activity.getString(R.string.search_dialog_positive_button),
                new searchRunnable(dialogView,activity,units),
                null,
                null,
                activity.getString(R.string.search_dialog_negative_button),
                null
        );
        searchDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                UsefulFunctions.hideKeyboard(activity,locationEditText);
            }
        });
        return searchDialog;
    }

    private AlertDialog initializeEmptyLocationNameDialog(){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.empty_location_name_dialog,null);
        AlertDialog emptyLocationNameDialog=buildDialog(
                dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.empty_location_name_dialog_title),
                R.drawable.error_icon,
                null,
                false,
                activity.getString(R.string.empty_location_name_dialog_positive_button),
                new showDialogRunnable(searchDialog, true),
                null,
                null,
                activity.getString(R.string.empty_location_name_dialog_negative_button),
                null
        );
        return emptyLocationNameDialog;
    }

    private AlertDialog initializeSearchProgressDialog(){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.refresh_dialog,null);
        AlertDialog searchProgressDialog=buildDialog(
                dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.search_progress_dialog_message),
                0,
                null,
                true,
                null,
                null,
                null,
                null,
                null,
                null
        );

        return searchProgressDialog;
    }

    private AlertDialog initializeNoLocalizationResultsDialog(){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.no_localization_results_layout,null);
        AlertDialog noLocalizationResultsDialog=buildDialog(
                dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.no_localization_results_dialog_title),
                R.drawable.error_icon,
                null,
                false,
                activity.getString(R.string.no_localization_results_dialog_positive_button),
                new showDialogRunnable(searchDialog, true),
                null,
                null,
                activity.getString(R.string.no_localization_results_dialog_negative_button),
                null
        );
        return noLocalizationResultsDialog;
    };

    private AlertDialog initializeLocalizationResultsDialog(DataInitializer dataInitializer) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.localization_results_dialog, null);
        String city="<b>"+dataInitializer.getCity()+"</b>";
        String region=dataInitializer.getRegion();
        String country=dataInitializer.getCountry();
        TextView cityTextView=(TextView)dialogView.findViewById(R.id.localization_results_dialog_city_text);
        cityTextView.setText((Html.fromHtml(city)));
        TextView regionCountryTextView=(TextView)dialogView.findViewById(R.id.localization_results_dialog_region_county_text);
        regionCountryTextView.setText(region+", "+country);
        AlertDialog localizationResultsDialog = buildDialog(
                dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.localization_results_dialog_title),
                R.drawable.localization_icon,
                null,
                false,
                activity.getString(R.string.localization_results_dialog_positive_button),
                new setMainLayoutRunnable(dataInitializer),
                activity.getString(R.string.localization_results_dialog_neutral_button),
                new showDialogRunnable(searchDialog,true),
                activity.getString(R.string.localization_results_dialog_negative_button),
                null);
        return localizationResultsDialog;
    }

    private AlertDialog initializeRefreshDialog(){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.refresh_dialog,null);
        AlertDialog refreshDialog = buildDialog(dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.refresh_dialog_title),
                0,
                null,
                true,
                null,
                null,
                null,
                null,
                null,
                null);

        return refreshDialog;
    }

    private AlertDialog initializeNoEmailApplicationDialog(){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.no_email_application_dialog,null);
        AlertDialog noEmailApplicationDialog = buildDialog(dialogView,R.style.CustomDialogStyle,
                activity.getString(R.string.no_email_application_dialog_title),
                R.drawable.error_icon,
                null,
                false,
                activity.getString(R.string.no_email_application_dialog_positive_button),
                new copyToClipboardRunnable(activity.getString(R.string.mail_address)),
                null,
                null,
                activity.getString( R.string.no_email_application_dialog_negative_button),
                null);
        return noEmailApplicationDialog;
    }

    private AlertDialog initializeFirstLoadingDialog(){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.first_loading_dialog,null);
        ImageView iconImageView=(ImageView)dialogView.findViewById(R.id.first_loading_dialog_app_icon_image);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.app_icon).fit().centerInside().into(iconImageView);
        AlertDialog firstLoadingDialog = buildDialog(dialogView,
                R.style.CustomLoadingDialogStyle,
                null,
                0,
                null,
                true,
                null,
                null,
                null,
                null,
                null,
                null
        );
        return firstLoadingDialog;
    }

    private AlertDialog initializeServiceFailureDialog(Runnable runnable){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.service_failure_dialog,null);
        AlertDialog serviceFailureDialog = buildDialog(dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.service_failure_dialog_title),
                R.drawable.error_icon,
                null,
                true,
                activity.getString(R.string.service_failure_dialog_positive_button),
                runnable,
                null,
                null,
                activity.getString(R.string.service_failure_dialog_negative_button),
                finishRunnable
        );
        return serviceFailureDialog;
    }

    private AlertDialog initializeInternetFailureDialog(Runnable runnable){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.internet_failure_dialog,null);
        AlertDialog internetFailureDialog = buildDialog(dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.internet_failure_dialog_title),
                R.drawable.error_icon,
                null,
                true,
                activity.getString(R.string.internet_failure_dialog_positive_button),
                runnable,
                null,
                null,
                activity.getString(R.string.internet_failure_dialog_negative_button),
                finishRunnable
        );
        return internetFailureDialog;
    }

    private AlertDialog initializeYahooRedirectDialog(){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.yahoo_redirect_dialog,null);
        AlertDialog yahooRedirectDialog = buildDialog(dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.yahoo_redirect_dialog_title),
                R.drawable.info_icon,
                null,
                false,
                activity.getString(R.string.yahoo_redirect_dialog_positive_button),
                new initializeWebIntentRunnable(activity.getString(R.string.yahoo_address)),
                null,
                null,
                activity.getString(R.string.yahoo_redirect_dialog_negative_button),
                null
        );
        return  yahooRedirectDialog;
    }

    private AlertDialog initializeYahooWeatherRedirectDialog(){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.yahoo_weather_redirect_dialog,null);
        AlertDialog yahooWeatherRedirectDialog = buildDialog(dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.yahoo_weather_redirect_dialog_title),
                R.drawable.info_icon,
                null,
                false,
                activity.getString(R.string.yahoo_weather_redirect_dialog_positive_button),
                new initializeWebIntentRunnable(activity.getString(R.string.yahoo_weather_address)),
                null,
                null,
                activity.getString(R.string.yahoo_weather_redirect_dialog_negative_button),
                null
        );
        return yahooWeatherRedirectDialog;
    }

    private AlertDialog initializeExitDialog(){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.exit_dialog,null);
        AlertDialog exitDialog = buildDialog(dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.exit_dialog_title),
                R.drawable.warning_icon,
                null,
                false,
                activity.getString(R.string.exit_dialog_positive_button),
                finishRunnable,
                null,
                null,
                activity.getString(R.string.exit_dialog_negative_button),
                null
        );
        return exitDialog;
    }

    private AlertDialog initializeAuthorDialog(){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.author_dialog,null);
        //setting images
        ImageView emailImageView=(ImageView)dialogView.findViewById(R.id.author_dialog_mail_image);
        ImageView githubImageView=(ImageView)dialogView.findViewById(R.id.author_dialog_github_image);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.email_icon).transform(new UsefulFunctions().new setDrawableColor(activity.getResources().getColor(R.color.white))).fit().centerInside().into(emailImageView);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.github_icon).transform(new UsefulFunctions().new setDrawableColor(activity.getResources().getColor(R.color.white))).fit().centerInside().into(githubImageView);
        //making links clickable and focusable
        RelativeLayout mailLayout=(RelativeLayout)dialogView.findViewById(R.id.author_dialog_mail_button_layout);
        RelativeLayout githubLayout=(RelativeLayout)dialogView.findViewById(R.id.author_dialog_github_button_layout);
        mailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UsefulFunctions.initializeEmailIntent(activity,activity.getString(R.string.mail_address),null,null,initializeNoEmailApplicationDialog());
            }
        });
        githubLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UsefulFunctions.initializeWebIntent(activity,activity.getString(R.string.github_address));
            }
        });
        AlertDialog authorDialog = buildDialog(dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.author_dialog_title),
                R.drawable.author_icon,
                null,
                false,
                null,
                null,
                null,
                null,
                activity.getString(R.string.author_dialog_negative_button),
                null);
        return authorDialog;
    }

    private AlertDialog initializeFeedbackDialog(){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.feedback_dialog,null);
        //making mail address clickable and focusable
        LinearLayout textLayout=(LinearLayout)dialogView.findViewById(R.id.feedback_dialog_mail_layout);
        UsefulFunctions.setLayoutFocusable(activity,textLayout);
        //copy to alipboadrd after press
        textLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                UsefulFunctions.copyToClipboard(activity,activity.getString(R.string.mail_address));
                return false;
            }
        });
        AlertDialog feedbackDialog = buildDialog(dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.feedback_dialog_title),
                R.drawable.feedback_icon,
                null,
                false,
                activity.getString(R.string.feedback_dialog_positive_button),
                new initializeEmailIntentRunnable(activity.getString(R.string.mail_address),activity.getString(R.string.clipboard_mail_feedback_title),null,initializeNoEmailApplicationDialog()),
                null,
                null,
                activity.getString(R.string.feedback_dialog_negative_button),
                null
        );
        return feedbackDialog;
    }



    private AlertDialog initializeAboutDialog(){
        LayoutInflater inflater = activity.getLayoutInflater();
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
        aboutDesctiptionPart1.setLinkTextColor( ContextCompat.getColor(activity,R.color.textSecondaryLightBackground));
        aboutDesctiptionPart2.setLinkTextColor( ContextCompat.getColor(activity,R.color.textSecondaryLightBackground));
        aboutDesctiptionPart3.setLinkTextColor( ContextCompat.getColor(activity,R.color.textSecondaryLightBackground));
        aboutDesctiptionPart4.setLinkTextColor( ContextCompat.getColor(activity,R.color.textSecondaryLightBackground));
        //setting app icon
        ImageView iconImageView=(ImageView)dialogView.findViewById(R.id.about_dialog_app_icon_image);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.app_icon).fit().centerInside().into(iconImageView);
        //initializing dialog
        AlertDialog aboutDialog = buildDialog(dialogView,
                R.style.CustomDialogStyle,
                null,
                0,
                null,
                false,
                null,
                null,
                null,
                null,

                activity.getString(R.string.about_dialog_negative_button),
                null);
        return aboutDialog;
    }

    public AlertDialog getRefreshDialog() {return refreshDialog;}
    public AlertDialog getSearchProgressDialog() {return searchProgressDialog;}
    public AlertDialog getInternetFailureDialog() {return internetFailureDialog;}
    public AlertDialog getServiceFailureDialog() {return serviceFailureDialog;}
    public AlertDialog getYahooRedirectDialog() {return yahooRedirectDialog;}
    public AlertDialog getYahooWeatherRedirectDialog() {return yahooWeatherRedirectDialog;}
    public AlertDialog getExitDialog() {return exitDialog;}
    public AlertDialog getAboutDialog() {return aboutDialog;}
    public AlertDialog getFirstLoadingDialog() {return firstLoadingDialog;}
    public AlertDialog getFeedbackDialog() {return feedbackDialog;}
    public AlertDialog getAuthorDialog() {return authorDialog;}
    public AlertDialog getNoEmailApplicationDialog() {return noEmailApplicationDialog;}
    public AlertDialog getSearchDialog() {return searchDialog;}
}
