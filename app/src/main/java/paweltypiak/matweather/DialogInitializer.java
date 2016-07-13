package paweltypiak.matweather;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DialogInitializer {
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
    private Activity activity;

   public DialogInitializer(Activity activity, Runnable reloadRunnable){

       this.activity=activity;
       this.refreshDialog=initializeRefreshDialog();
       this.firstLoadingDialog=initializeFirstLoadingDialog();
       this.failureDialog=initializeFailureDialog(reloadRunnable);
       this.yahooRedirectDialog=initializeYahooRedirectDialog();
       this.yahooWeatherRedirectDialog=initializeYahooWeatherRedirectDialog();
       this.exitDialog=initializeExitDialog();
       this.noEmailApplicationDialog=initializeNoEmailApplicationDialog();
       this.aboutDialog=initializeAboutDialog();
       this.feedbackDialog=initializeFeedbackDialog();
       this.authorDialog=initializeAuthorDialog();
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
    }

    //runnables to make passing methods as parameters possible
    private Runnable finishRunnable = new Runnable() {
        public void run() {
            activity.finish();
        }
    };

    private class copyToClipboardRunnable implements Runnable {
        String text;
        public copyToClipboardRunnable(String text) {
            this.text=text;
        }
        public void run() {
            UsableFunctions.copyToClipboard(activity,text);
        }
    }

    private class initializeWebIntentRunnable implements Runnable {
        String url;
        public initializeWebIntentRunnable(String url) {
            this.url=url;
        }
        public void run() {
            UsableFunctions.initializeWebIntent(activity,url);
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
            UsableFunctions.initializeEmailIntent(activity,address,subject,body,dialog);
        }
    }

    private AlertDialog buildDialog(View dialogView, int theme, String title, int iconResource, String message, boolean ifUncancelable, String positiveButtonText, final Runnable positiveButtonFunction, String negativeButtonText, final Runnable negativeButtonFunction){
        //custom dialog builder
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(activity, theme);
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
                true,null,
                null,
                null,
                null
        );
        return firstLoadingDialog;
    }

    private AlertDialog initializeFailureDialog(Runnable runnable){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.failure_dialog,null);
        AlertDialog failureDialog = buildDialog(dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.service_failure_dialog_title),
                R.drawable.error_icon,
                null,
                true,
                activity.getString(R.string.service_failure_dialog_positive_button),
                runnable,
                activity.getString(R.string.service_failure_dialog_negative_button),
                finishRunnable
        );
        return failureDialog;
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
                activity.getString(R.string.yahoo_redirect_dialog_negative_button),
                null
        );
        return  initializeYahooWeatherRedirectDialog();
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
        Picasso.with(activity.getApplicationContext()).load(R.drawable.email_icon).transform(new UsableFunctions().new setDrawableColor(activity.getResources().getColor(R.color.white))).fit().centerInside().into(emailImageView);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.github_icon).transform(new UsableFunctions().new setDrawableColor(activity.getResources().getColor(R.color.white))).fit().centerInside().into(githubImageView);
        //making links clickable and focusable
        LinearLayout mailLayout=(LinearLayout)dialogView.findViewById(R.id.author_dialog_mail_layout);
        LinearLayout githubLayout=(LinearLayout)dialogView.findViewById(R.id.author_dialog_github_layout);
        UsableFunctions.setLayoutFocusable(activity,mailLayout);
        UsableFunctions.setLayoutFocusable(activity,githubLayout);
        mailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UsableFunctions.initializeEmailIntent(activity,activity.getString(R.string.mail_address),null,null,initializeNoEmailApplicationDialog());
            }
        });
        githubLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UsableFunctions.initializeWebIntent(activity,activity.getString(R.string.github_address));
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
                activity.getString(R.string.author_dialog_negative_button),
                null);
        return authorDialog;
    }

    private AlertDialog initializeFeedbackDialog(){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.feedback_dialog,null);
        //making mail address clickable and focusable
        LinearLayout textLayout=(LinearLayout)dialogView.findViewById(R.id.feedback_dialog_mail_layout);
        UsableFunctions.setLayoutFocusable(activity,textLayout);
        //copy to alipboadrd after press
        textLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                UsableFunctions.copyToClipboard(activity,activity.getString(R.string.mail_address));
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
        aboutDesctiptionPart1.setLinkTextColor( ContextCompat.getColor(activity,R.color.textSecondaryDark));
        aboutDesctiptionPart2.setLinkTextColor( ContextCompat.getColor(activity,R.color.textSecondaryDark));
        aboutDesctiptionPart3.setLinkTextColor( ContextCompat.getColor(activity,R.color.textSecondaryDark));
        aboutDesctiptionPart4.setLinkTextColor( ContextCompat.getColor(activity,R.color.textSecondaryDark));
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
                activity.getString(R.string.about_dialog_negative_button),
                null);
        return aboutDialog;
    }

    public AlertDialog getRefreshDialog() {
        return refreshDialog;
    }

    public AlertDialog getFailureDialog() {
        return failureDialog;
    }

    public AlertDialog getYahooRedirectDialog() {
        return yahooRedirectDialog;
    }

    public AlertDialog getYahooWeatherRedirectDialog() {
        return yahooWeatherRedirectDialog;
    }

    public AlertDialog getExitDialog() {
        return exitDialog;
    }

    public AlertDialog getAboutDialog() {
        return aboutDialog;
    }

    public AlertDialog getFirstLoadingDialog() {
        return firstLoadingDialog;
    }

    public AlertDialog getFeedbackDialog() {
        return feedbackDialog;
    }

    public AlertDialog getAuthorDialog() {
        return authorDialog;
    }

    public AlertDialog getNoEmailApplicationDialog() {
        return noEmailApplicationDialog;
    }
}
