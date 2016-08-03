package paweltypiak.matweather;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataDownloader;
import paweltypiak.matweather.weatherDataDownloading.WeatherDownloadCallback;
import paweltypiak.matweather.weatherDataProcessing.WeatherDataInitializer;
import paweltypiak.matweather.weatherDataProcessing.WeatherDataSetter;
import paweltypiak.matweather.jsonHandling.Channel;

public class DialogInitializer  {

    private static AlertDialog serviceFailureDialog;
    private static AlertDialog yahooRedirectDialog;
    private static AlertDialog yahooWeatherRedirectDialog;
    private static AlertDialog exitDialog;
    private static AlertDialog aboutDialog;
    private static AlertDialog firstLoadingDialog;
    private static AlertDialog feedbackDialog;
    private static AlertDialog authorDialog;
    private static AlertDialog noEmailApplicationDialog;
    private static AlertDialog searchDialog;
    private static AlertDialog searchProgressDialog;
    private static AlertDialog noLocalizationResultsDialog;
    private static AlertDialog localizationResultsDialog;
    private static AlertDialog emptyLocationNameDialog;
    private static AlertDialog internetFailureDialog;
    private static AlertDialog mapsDialog;
    private static AlertDialog differentLocationDialog;
    private static AlertDialog addToFavouritesDialog;
    private static AlertDialog editFavouritesDialog;
    private static AlertDialog localizationFailureDialog;
    private static AlertDialog permissionDeniedDialog;
    private static AlertDialog providerUnavailableDialog;
    private static AlertDialog duplicateDialog;
    private static EditText searchEditText;
    private static Activity activity;


    public DialogInitializer(Activity activity) {
        this.activity = activity;
    }

    //runnables to make passing methods as parameters possible
    private static Runnable showExitRunnable = new Runnable() {
        public void run() {
            if(exitDialog==null) exitDialog=initializeExitDialog(false,null);
        }
    };

    private static Runnable finishRunnable = new Runnable() {
        public void run() {
            activity.finish();
            return;
        }
    };

    private static class copyToClipboardRunnable implements Runnable {
        private String text;
        public copyToClipboardRunnable(String text) {
            this.text=text;
        }
        public void run() {
            UsefulFunctions.copyToClipboard(activity,text);
        }
    }

    private static class initializeWebIntentRunnable implements Runnable {
        private String url;
        public initializeWebIntentRunnable(String url) {
            this.url=url;
        }
        public void run() {
            UsefulFunctions.initializeWebIntent(activity,url);
        }
    }

    private static class initializeMapsIntentRunnable implements Runnable{
        private String label;
        private double longitude;
        private double latitude;

        public initializeMapsIntentRunnable(String label, double longitude, double latitude){
            this.label=label;
            this.longitude=longitude;
            this.latitude=latitude;
        }

        @Override
        public void run() {
            UsefulFunctions.initializeMapsIntent(activity, longitude, latitude,label);
        }
    }

    private static class initializeEmailIntentRunnable implements Runnable {
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

    private static class setMainLayoutRunnable implements Runnable {
        WeatherDataInitializer dataInitializer;
        WeatherDataSetter dataSetter;

        public setMainLayoutRunnable(WeatherDataInitializer dataInitializer) {
            this.dataInitializer = dataInitializer;
        }

        public void run() {
            dataSetter = new WeatherDataSetter(activity, dataInitializer);
        }
    }

    private static Runnable showDifferentLocationDialogRunnable = new Runnable() {
        public void run() {
            differentLocationDialog.show();
            UsefulFunctions.showKeyboard(activity);
        }
    };

    private static class differentLocationDialogRunnable implements  Runnable{
        private RadioButton radioButton;
        private View dialogView;
        private EditText editText;
        private String editTextString;

        public differentLocationDialogRunnable(RadioButton radioButton, View dialogView) {
            this.radioButton = radioButton;
            this.dialogView=dialogView;
        }
        @Override
        public void run() {
            UsefulFunctions.hideKeyboard(activity,null);
            editText=(EditText)dialogView.findViewById(R.id.search_edit_text);
            editTextString=editText.getText().toString();
            if(editTextString.length()==0) {
                emptyLocationNameDialog=initializeEmptyLocationNameDialog(1);
                emptyLocationNameDialog.show();
            }
            else{
                editTextString =UsefulFunctions.getFormattedString(editTextString);
                radioButton.setText(editTextString);
            }
        }
    }


    private static void initializeSearchRunnableDialogs(){
        emptyLocationNameDialog=initializeEmptyLocationNameDialog(2);
        searchProgressDialog=initializeSearchProgressDialog();
        noLocalizationResultsDialog= initializeNoLocationResultsDialog(2,showSearchDialogRunnable,null);
    }

    private static Runnable showSearchDialogRunnable = new Runnable() {
        public void run() {
            if(searchDialog==null) searchDialog=initializeSearchDialog();
            noLocalizationResultsDialog= initializeNoLocationResultsDialog(2,showSearchDialogRunnable,null);
            searchDialog.show();
            UsefulFunctions.showKeyboard(activity);
        }
    };

    private static class searchRunnable implements Runnable, WeatherDownloadCallback {
        private static String location;
        private WeatherDataDownloader downloader;
        private WeatherDataInitializer dataInitializer;
        private View dialogView;
        private EditText locationEditText;
        private boolean isReconnect;

        public searchRunnable(){
            isReconnect=true;
        }
        public searchRunnable(View dialogView){
            initializeSearchRunnableDialogs();
            locationEditText=(EditText)dialogView.findViewById(R.id.search_edit_text);
            isReconnect=false;
        }

        public void run(){
            if(isReconnect==false){
                location=locationEditText.getText().toString();
                location=UsefulFunctions.getFormattedString(location);
                UsefulFunctions.hideKeyboard(activity,locationEditText);
            }
            if(location.length()==0) emptyLocationNameDialog.show();
            else {
                searchProgressDialog.show();
                downloader=new WeatherDataDownloader(location,this);
            }
        }

        @Override
        public void ServiceSuccess(Channel channel) {
            dataInitializer=new WeatherDataInitializer(activity,channel);
            localizationResultsDialog = initializeLocationResultsDialog(2,dataInitializer,new setMainLayoutRunnable(dataInitializer),showSearchDialogRunnable,null);
            localizationResultsDialog.show();
            searchProgressDialog.dismiss();
        }

        @Override
        public void ServiceFailure(int errorCode) {
            if(errorCode==1)   {
                internetFailureDialog=initializeInternetFailureDialog(false, new searchRunnable(),null);
                internetFailureDialog.show();
            }
            else {
                noLocalizationResultsDialog.show();
            }
            searchProgressDialog.dismiss();
        }
    }

    private static AlertDialog buildDialog(Activity activity, View dialogView, int theme, String title, int iconResource, String message, boolean ifUncancelable, String positiveButtonText, final Runnable positiveButtonFunction, String neutralButtonText, final Runnable neutralButtonFunction, String negativeButtonText, final Runnable negativeButtonFunction){
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

    public static AlertDialog initializeSearchDialog(){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.search_dialog,null);
        final EditText locationEditText=(EditText)dialogView.findViewById(R.id.search_edit_text);
        searchDialog=buildDialog(
                activity,
                dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.search_dialog_title),
                R.drawable.search_blue_icon,
                null,
                false,
                activity.getString(R.string.search_dialog_positive_button),
                new searchRunnable(dialogView),
                null,
                null,
                activity.getString(R.string.search_dialog_negative_button),
                null
        );
        searchDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                UsefulFunctions.hideKeyboard(activity,null);
                Log.d("ond", "onDismiss: ");
                locationEditText.getText().clear();
            }
        });
        return searchDialog;
    }

    public static AlertDialog initializeDifferentLocationDialog(RadioButton radioButton){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.search_dialog,null);
        final EditText locationEditText=(EditText)dialogView.findViewById(R.id.search_edit_text);
        String radioButtonString=radioButton.getText().toString();
        if(!radioButtonString.equals(activity.getString(R.string.first_launch_layout_location_different))){
            locationEditText.setText(radioButtonString);
            locationEditText.setSelection(radioButtonString.length());
        }
        differentLocationDialog=buildDialog(
                activity,
                dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.different_location_dialog_title),
                R.drawable.localization_icon,
                null,
                false,
                activity.getString(R.string.different_location_dialog_positive_button),
                new differentLocationDialogRunnable(radioButton,dialogView),
                null,
                null,
                activity.getString(R.string.different_location_dialog_negative_button),
                null
        );
        differentLocationDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                UsefulFunctions.hideKeyboard(activity,null);
                locationEditText.getText().clear();
            }
        });
        return differentLocationDialog;
    }

    public static AlertDialog initializeLocalizationFailureDialog(Runnable positiveButtonRunnable, Runnable negativeButtonRunnable){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.one_line_text_dialog,null);
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.one_line_text_dialog_message_text);
        messageTextView.setText(activity.getString(R.string.first_launch_localization_failure_dialog_message));
        localizationFailureDialog=buildDialog(
                activity,
                dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.first_launch_localization_failure_dialog_title),
                R.drawable.error_icon,
                null,
                true,
                activity.getString(R.string.first_launch_localization_failure_dialog_positive_button),
                positiveButtonRunnable,
                null,
                null,
                activity.getString(R.string.first_launch_localization_failure_dialog_negative_button),
                negativeButtonRunnable
        );
        return localizationFailureDialog;
    }

    public static AlertDialog initializeProviderUnavailableDialog(int type,Runnable positiveButtonRunnable){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.one_line_text_dialog,null);
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.one_line_text_dialog_message_text);
        String message;
        String title;
        String positiveButtonString;
        if(type==1){
            message=activity.getString(R.string.gps_unavailable_dialog_message);
            title=activity.getString(R.string.gps_unavailable_dialog_title);
            positiveButtonString=activity.getString(R.string.gps_unavailable_dialog_positive_button);
        }
        else{
            message=activity.getString(R.string.network_unavailable_dialog_message);
            title=activity.getString(R.string.network_unavailable_dialog_title);
            positiveButtonString=activity.getString(R.string.network_unavailable_dialog_positive_button);
        }
        messageTextView.setText(message);
        providerUnavailableDialog=buildDialog(
                activity,
                dialogView,
                R.style.CustomDialogStyle,
                title,
                R.drawable.warning_icon,
                null,
                true,
                positiveButtonString,
                positiveButtonRunnable,
                null,
                null,
                null,
                null
        );
        return providerUnavailableDialog;
    }

    public static AlertDialog initializePermissionDeniedDialog(Runnable positiveButtonRunnable, Runnable negativeButtonRunnable){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.one_line_text_dialog,null);
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.one_line_text_dialog_message_text);
        messageTextView.setText(activity.getString(R.string.first_launch_permission_denied_dialog_message));
        permissionDeniedDialog=buildDialog(
                activity,
                dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.first_launch_permission_denied_dialog_title),
                R.drawable.error_icon,
                null,
                true,
                activity.getString(R.string.first_launch_permission_denied_dialog_positive_button),
                positiveButtonRunnable,
                null,
                null,
                activity.getString(R.string.first_launch_permission_denied_dialog_negative_button),
                negativeButtonRunnable
        );
        return permissionDeniedDialog;
    }

    public static AlertDialog initializeDuplicateDialog(){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.one_line_text_dialog,null);
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.one_line_text_dialog_message_text);
        messageTextView.setText(activity.getString(R.string.duplicate_dialog_message));
        duplicateDialog=buildDialog(
                activity,
                dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.duplicate_dialog_title),
                R.drawable.info_icon,
                null,
                false,
                activity.getString(R.string.duplicate_dialog_positive_button),
                null,
                null,
                null,
                null,
                null
        );
        return duplicateDialog;
    }

    public static AlertDialog initializeEmptyLocationNameDialog(int type){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.one_line_text_dialog,null);
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.one_line_text_dialog_message_text);
        messageTextView.setText(activity.getString(R.string.empty_location_name_dialog_message));
        Runnable positiveButtonRunnable;
        if(type==1) positiveButtonRunnable=showDifferentLocationDialogRunnable;
        else positiveButtonRunnable=showSearchDialogRunnable;
        emptyLocationNameDialog=buildDialog(
                activity,
                dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.empty_location_name_dialog_title),
                R.drawable.error_icon,
                null,
                false,
                activity.getString(R.string.empty_location_name_dialog_positive_button),
                positiveButtonRunnable,
                null,
                null,
                activity.getString(R.string.empty_location_name_dialog_negative_button),
                null
        );
        return emptyLocationNameDialog;
    }

    public static AlertDialog initializeNoLocationResultsDialog(int type, Runnable positiveButtonRunnable, Runnable negativeButtonRunnable){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.one_line_text_dialog,null);
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.one_line_text_dialog_message_text);
        messageTextView.setText(activity.getString(R.string.no_localization_results_dialog_message));
        boolean isUncancelable;
        String negativeButtonString;
        if(type==1){
            isUncancelable=true;
            negativeButtonString=activity.getString(R.string.no_localization_results_first_launch_dialog_negative_button);
        }
        else{
            isUncancelable=false;
            negativeButtonString=activity.getString(R.string.no_localization_results_dialog_negative_button);
        }
        noLocalizationResultsDialog=buildDialog(
                activity,
                dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.no_localization_results_dialog_title),
                R.drawable.error_icon,
                null,
                isUncancelable,
                activity.getString(R.string.no_localization_results_dialog_positive_button),
                positiveButtonRunnable,
                null,
                null,
                negativeButtonString,
                negativeButtonRunnable
        );
        return noLocalizationResultsDialog;
    };

    private static AlertDialog initializeNoEmailApplicationDialog(){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.one_line_text_dialog,null);
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.one_line_text_dialog_message_text);
        messageTextView.setText(activity.getString(R.string.no_email_application_dialog_message));
        noEmailApplicationDialog = buildDialog(
                activity,
                dialogView,R.style.CustomDialogStyle,
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

    public static AlertDialog initializeServiceFailureDialog(Runnable positiveButtonRunnable,Runnable negativeButtonRunnable){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.one_line_text_dialog,null);
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.one_line_text_dialog_message_text);
        messageTextView.setText(activity.getString(R.string.service_failure_dialog_message));
        if(negativeButtonRunnable==null) negativeButtonRunnable=showExitRunnable;
        serviceFailureDialog = buildDialog(
                activity,
                dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.service_failure_dialog_title),
                R.drawable.error_icon,
                null,
                true,
                activity.getString(R.string.service_failure_dialog_positive_button),
                positiveButtonRunnable,
                null,
                null,
                activity.getString(R.string.service_failure_dialog_negative_button),
                negativeButtonRunnable
        );
        return serviceFailureDialog;
    }

    public static AlertDialog initializeInternetFailureDialog(boolean isUncancelable,Runnable positiveButtonRunnable,Runnable negativeButtonRunnable){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.one_line_text_dialog,null);
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.one_line_text_dialog_message_text);
        messageTextView.setText(activity.getString(R.string.internet_failure_dialog_message));
        internetFailureDialog = buildDialog(
                activity,
                dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.internet_failure_dialog_title),
                R.drawable.error_icon,
                null,
                isUncancelable,
                activity.getString(R.string.internet_failure_dialog_positive_button),
                positiveButtonRunnable,
                null,
                null,
                activity.getString(R.string.internet_failure_dialog_negative_button),
                negativeButtonRunnable
        );
        return internetFailureDialog;
    }

    public static AlertDialog initializeExitDialog(boolean ifUncancelable, Runnable negativeButtonRunnable){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.one_line_text_dialog,null);
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.one_line_text_dialog_message_text);
        messageTextView.setText(activity.getText(R.string.exit_dialog_message));
        exitDialog = buildDialog(
                activity,
                dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.exit_dialog_title),
                R.drawable.warning_icon,
                null,
                ifUncancelable,
                activity.getString(R.string.exit_dialog_positive_button),
                finishRunnable,
                null,
                null,
                activity.getString(R.string.exit_dialog_negative_button),
                negativeButtonRunnable
        );
        return exitDialog;
    }

    public static AlertDialog initializeYahooRedirectDialog(){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.two_line_text_dialog,null);
        TextView message1TextView=(TextView)dialogView.findViewById(R.id.two_line_text_dialog_message1_text);
        message1TextView.setText(activity.getString(R.string.yahoo_redirect_dialog_message));
        TextView message2TextView=(TextView)dialogView.findViewById(R.id.two_line_text_dialog_message2_text);
        message2TextView.setText(activity.getString(R.string.yahoo_redirect_dialog_message_service_name));
        yahooRedirectDialog = buildDialog(
                activity,
                dialogView,
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

    public static AlertDialog initializeYahooWeatherRedirectDialog(Activity activity, String link){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.two_line_text_dialog,null);
        TextView message1TextView=(TextView)dialogView.findViewById(R.id.two_line_text_dialog_message1_text);
        message1TextView.setText(activity.getString(R.string.yahoo_weather_redirect_dialog_message));
        TextView message2TextView=(TextView)dialogView.findViewById(R.id.two_line_text_dialog_message2_text);
        message2TextView.setText(activity.getString(R.string.yahoo_weather_redirect_dialog_message_service_name));
        yahooWeatherRedirectDialog = buildDialog(
                activity,
                dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.yahoo_weather_redirect_dialog_title),
                R.drawable.info_icon,
                null,
                false,
                activity.getString(R.string.yahoo_weather_redirect_dialog_positive_button),
                new initializeWebIntentRunnable(link),
                null,
                null,
                activity.getString(R.string.yahoo_weather_redirect_dialog_negative_button),
                null
        );
        return yahooWeatherRedirectDialog;
    }

    public static AlertDialog initializeFeedbackDialog(){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.two_line_text_dialog,null);
        TextView message1TextView=(TextView)dialogView.findViewById(R.id.two_line_text_dialog_message1_text);
        message1TextView.setText(activity.getString(R.string.feedback_dialog_message));
        TextView message2TextView=(TextView)dialogView.findViewById(R.id.two_line_text_dialog_message2_text);
        message2TextView.setText(activity.getString(R.string.feedback_dialog_mail));
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray typedArray = activity.obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        message2TextView.setBackgroundResource(backgroundResource);
        typedArray.recycle();
        message2TextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                UsefulFunctions.copyToClipboard(activity,activity.getString(R.string.mail_address));
                return true;
            }
        });
        feedbackDialog = buildDialog(
                activity,
                dialogView,
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

    private static class addToFavouritesRunnable implements Runnable {
        private View dialogView;
        private FloatingActionButton floatingActionButton;

        public addToFavouritesRunnable(View dialogView, FloatingActionButton floatingActionButton) {
            this.dialogView = dialogView;
            this.floatingActionButton=floatingActionButton;
        }

        public void run() {
            EditText headerEditText=(EditText)dialogView.findViewById(R.id.edit_location_dialog_header_edittext);
            EditText subheaderEditText=(EditText)dialogView.findViewById(R.id.edit_location_dialog_subheader_edittext);
            String customHeaderString=headerEditText.getText().toString();
            String customSubheaderString=subheaderEditText.getText().toString();
            String customLocationString=customHeaderString+"%"+customSubheaderString;
            UsefulFunctions.saveNewFavouriteLocationName(customLocationString,activity);
            UsefulFunctions.saveNewFavouriteLocationAddress(activity);
            UsefulFunctions.saveNewFavouriteLocationCoordinates(activity);
            CheckBox checkBox=(CheckBox)dialogView.findViewById(R.id.edit_location_dialog_checkbox);
            if(checkBox.isChecked()){
                Log.d("checkbox", "checked");
                UsefulFunctions.setFirstLocation(activity,null);
            }
            UsefulFunctions.getFirstLocation(activity);
            //  floatingActionButton.setImageResource(R.drawable.edit_black_icon);
            // MainActivity.setfloatingActionButtonOnClickIndicator(2);
        }
    }

    public static AlertDialog initializeAddToFavourites(FloatingActionButton floatingActionButton){
        LayoutInflater inflater = activity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.edit_location_dialog,null);
        dialogView.setFocusableInTouchMode(true);
        dialogView.setClickable(true);
        String [] location=UsefulFunctions.getCurrentLocationStrings();
        EditText headerEditText=(EditText)dialogView.findViewById(R.id.edit_location_dialog_header_edittext);
        headerEditText.setText(location[0]);
        UsefulFunctions.customizeEditText(headerEditText,activity);
        EditText subheaderEditText=(EditText)dialogView.findViewById(R.id.edit_location_dialog_subheader_edittext);
        subheaderEditText.setText(location[1]);
        UsefulFunctions.customizeEditText(subheaderEditText,activity);
        addToFavouritesDialog = buildDialog(
                activity,
                dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.add_location_dialog_title),
                R.drawable.add_blue_icon,
                null,
                false,
                activity.getString(R.string.add_location_dialog_positive_button),
                new addToFavouritesRunnable(dialogView,floatingActionButton),
                null,
                null,
                activity.getString(R.string.add_location_dialog_negative_button),
                null
        );
        addToFavouritesDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                UsefulFunctions.hideKeyboard(activity,null);
            }
        });
        return addToFavouritesDialog;
    }

    public static AlertDialog initializeEditFavourites(){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.edit_location_dialog,null);
        CheckBox checkBox=(CheckBox)dialogView.findViewById(R.id.edit_location_dialog_checkbox);
        checkBox.setText(activity.getString(R.string.edit_location_dialog_checkbox_message));
        addToFavouritesDialog = buildDialog(
                activity,
                dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.edit_location_dialog_title),
                R.drawable.edit_blue_icon,
                null,
                false,
                activity.getString(R.string.edit_location_dialog_positive_button),
                null,
                activity.getString(R.string.edit_location_dialog_neutral_button),
                null,
                activity.getString(R.string.edit_location_dialog_negative_button),
                null
        );
        return addToFavouritesDialog;
    }

    private static AlertDialog initializeSearchProgressDialog(){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.progress_dialog,null);
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.progress_dialog_message);
        messageTextView.setText(activity.getString(R.string.search_progress_dialog_message));
        searchProgressDialog=buildDialog(
                activity,
                dialogView,
                R.style.CustomDialogStyle,
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

        return searchProgressDialog;
    }

    public static AlertDialog initializeLocationResultsDialog(int type, WeatherDataInitializer dataInitializer, Runnable positiveButtonRunnable, Runnable neutralButtonRunnable, Runnable negativeButtonRunnable) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.location_dialog, null);
        String city=dataInitializer.getCity();
        String region=dataInitializer.getRegion();
        String country=dataInitializer.getCountry();
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.location_dialog_message_text);
        messageTextView.setText(activity.getString(R.string.localization_results_dialog_message));
        TextView cityTextView=(TextView)dialogView.findViewById(R.id.location_dialog_city_text);
        cityTextView.setText(city);
        TextView regionCountryTextView=(TextView)dialogView.findViewById(R.id.location_dialog_region_county_text);
        regionCountryTextView.setText(region+", "+country);
        boolean ifUncancellable;
        String negativeButtonString;
        if(type==1){
            ifUncancellable=true;
            negativeButtonString=activity.getString(R.string.first_launch_localization_results_dialog_negative_button);
        }
        else
        {
            ifUncancellable=false;
            negativeButtonString=activity.getString(R.string.localization_results_dialog_negative_button);
        }
        localizationResultsDialog = buildDialog(
                activity,
                dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.localization_results_dialog_title),
                R.drawable.localization_icon,
                null,
                ifUncancellable,
                activity.getString(R.string.localization_results_dialog_positive_button),
                positiveButtonRunnable,
                activity.getString(R.string.localization_results_dialog_neutral_button),
                neutralButtonRunnable,
                negativeButtonString,
                negativeButtonRunnable);
        return localizationResultsDialog;
    }

    public static AlertDialog initializeMapsDialog(Activity activity, String city, String region,String country,double longitude,double latitude) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.location_dialog, null);
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.location_dialog_message_text);
        messageTextView.setText(activity.getString(R.string.maps_dialog_message));
        TextView cityTextView=(TextView)dialogView.findViewById(R.id.location_dialog_city_text);
        cityTextView.setText(city);
        TextView regionCountryTextView=(TextView)dialogView.findViewById(R.id.location_dialog_region_county_text);
        regionCountryTextView.setText(region+", "+country);
        mapsDialog = buildDialog(
                activity,
                dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.maps_dialog_title),
                R.drawable.maps_icon,
                null,
                false,
                activity.getString(R.string.maps_dialog_positive_button),
                new initializeMapsIntentRunnable(city, longitude, latitude),
                null,
                null,
                activity.getString(R.string.maps_dialog_negative_button),
                null);
        return mapsDialog;
    }

    public static AlertDialog initializeFirstLoadingDialog(){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.first_loading_dialog,null);
        ImageView iconImageView=(ImageView)dialogView.findViewById(R.id.first_loading_dialog_app_icon_image);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.app_icon_small).fit().centerInside().into(iconImageView);
        firstLoadingDialog = buildDialog(
                activity,
                dialogView,
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

    public static AlertDialog initializeAuthorDialog(){
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
        authorDialog = buildDialog(
                activity,
                dialogView,
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

    public static AlertDialog initializeAboutDialog(){
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
        Picasso.with(activity.getApplicationContext()).load(R.drawable.app_icon_small).fit().centerInside().into(iconImageView);
        //initializing dialog
        aboutDialog = buildDialog(
                activity,
                dialogView,
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

}