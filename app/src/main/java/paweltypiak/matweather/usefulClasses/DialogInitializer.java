package paweltypiak.matweather.usefulClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;
import paweltypiak.matweather.R;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataDownloader;
import paweltypiak.matweather.weatherDataDownloading.WeatherDownloadCallback;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataInitializer;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataSetter;
import paweltypiak.matweather.jsonHandling.Channel;

public class DialogInitializer  {
    private AlertDialog serviceFailureDialog;
    private AlertDialog yahooRedirectDialog;
    private AlertDialog yahooWeatherRedirectDialog;
    private AlertDialog exitDialog;
    private AlertDialog aboutDialog;
    private AlertDialog feedbackDialog;
    private AlertDialog authorDialog;
    private AlertDialog noEmailApplicationDialog;
    private AlertDialog searchDialog;
    private AlertDialog progressDialog;
    private AlertDialog noWeatherResultsForLocation;
    private AlertDialog localizationResultsDialog;
    private AlertDialog noDifferentLocationChoosenDialog;
    private AlertDialog internetFailureDialog;
    private AlertDialog mapsDialog;
    private AlertDialog addToFavouritesDialog;
    private AlertDialog editFavouritesDialog;
    private AlertDialog localizationFailureDialog;
    private AlertDialog permissionDeniedDialog;
    private AlertDialog providerUnavailableDialog;
    private AlertDialog favouritesDialog;
    private AlertDialog emptyLocationListDialog;
    private AlertDialog localizationOptionsDialog;
    private Activity activity;

    public DialogInitializer(Activity activity) {
        this.activity = activity;
    }

    private Runnable finishRunnable = new Runnable() {
        public void run() {
            activity.finish();
            return;
        }
    };

    private class copyToClipboardRunnable implements Runnable {
        private String text;
        public copyToClipboardRunnable(String text) {
            this.text=text;
        }
        public void run() {
            UsefulFunctions.copyToClipboard(activity,text);
        }
    }

    private class initializeWebIntentRunnable implements Runnable {
        private String url;
        public initializeWebIntentRunnable(String url) {
            this.url=url;
        }
        public void run() {
            UsefulFunctions.initializeWebIntent(activity,url);
        }
    }

    private class initializeMapsIntentRunnable implements Runnable{
        private String label;
        private double longitude;
        private double latitude;

        public initializeMapsIntentRunnable(String label, double latitude, double longitude){
            this.label=label;
            this.longitude=longitude;
            this.latitude=latitude;
        }

        @Override
        public void run() {
            UsefulFunctions.initializeMapsIntent(activity, latitude, longitude,label);
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
        WeatherDataInitializer dataInitializer;
        WeatherDataSetter dataSetter;

        public setMainLayoutRunnable(WeatherDataInitializer dataInitializer) {
            this.dataInitializer = dataInitializer;
        }

        public void run() {
            dataSetter = new WeatherDataSetter(activity, dataInitializer,true,false);
        }
    }

    private Runnable showDifferentLocationDialogRunnable = new Runnable() {
        public void run() {
            searchDialog.show();
            UsefulFunctions.showKeyboard(activity);
        }
    };

    private class differentLocationDialogRunnable implements  Runnable{
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
            editTextString =UsefulFunctions.getFormattedString(editTextString);
            radioButton.setText(editTextString);
        }
    }

    private Runnable showSearchDialogRunnable = new Runnable() {
        public void run() {
            searchDialog=initializeSearchDialog(1,null);
            searchDialog.show();
            UsefulFunctions.showKeyboard(activity);
        }
    };

    private class searchRunnable implements Runnable, WeatherDownloadCallback {
        private String location;
        private WeatherDataDownloader downloader;
        private WeatherDataInitializer dataInitializer;
        private EditText locationEditText;
        private boolean isReconnect;

        public searchRunnable(String location){
            this.location=location;
            isReconnect=true;
        }
        public searchRunnable(View dialogView){
            locationEditText=(EditText)dialogView.findViewById(R.id.search_edit_text);
            isReconnect=false;
        }

        public void run(){
            progressDialog = initializeProgressDialog(activity.getString(R.string.searching_location_progress_message));
            noWeatherResultsForLocation = initializeNoWeatherResultsForLocationDialog(1,showSearchDialogRunnable,null);
            progressDialog.show();
            if(isReconnect==false){
                location=locationEditText.getText().toString();
                location=UsefulFunctions.getFormattedString(location);
                UsefulFunctions.hideKeyboard(activity,locationEditText);
            }
            downloader=new WeatherDataDownloader(location,this);
        }

        @Override
        public void weatherServiceSuccess(Channel channel) {
            dataInitializer=new WeatherDataInitializer(channel);
            localizationResultsDialog = initializeWeatherResultsForLocationDialog(1,dataInitializer,new setMainLayoutRunnable(dataInitializer),showSearchDialogRunnable,null);
            localizationResultsDialog.show();
            progressDialog.dismiss();
        }

        @Override
        public void weatherServiceFailure(int errorCode) {
            if(errorCode==1)   {
                internetFailureDialog=initializeInternetFailureDialog(1, new searchRunnable(location),null);
                internetFailureDialog.show();
            }
            else {
                noWeatherResultsForLocation.show();
            }
            progressDialog.dismiss();
        }
    }

    private class addToFavouritesRunnable implements Runnable {
        private View dialogView;

        public addToFavouritesRunnable(View dialogView) {
            this.dialogView = dialogView;
        }

        public void run() {
            EditText headerEditText=(EditText)dialogView.findViewById(R.id.edit_location_dialog_header_edittext);
            EditText subheaderEditText=(EditText)dialogView.findViewById(R.id.edit_location_dialog_subheader_edittext);
            String customHeaderString=headerEditText.getText().toString();
            String customSubheaderString=subheaderEditText.getText().toString();
            FavouritesEditor.saveNewFavouritesItem(activity,customHeaderString,customSubheaderString,null,null);
            CheckBox checkBox=(CheckBox)dialogView.findViewById(R.id.edit_location_dialog_checkbox);
            if(checkBox.isChecked()){
                Log.d("checkbox", "checked");
                String currentLocationHeaderString=UsefulFunctions.getCurrentLocationAddress()[0];
                String currentLocationSubheaderString=UsefulFunctions.getCurrentLocationAddress()[1];
                String currentLocationName=currentLocationHeaderString+", "+currentLocationSubheaderString;
                SharedPreferencesModifier.setDefeaultLocationConstant(activity,currentLocationName);
            }
            FavouritesEditor.setLayoutForFavourites(activity);
        }
    }

    private Runnable deleteFromFavouritesRunnable = new Runnable() {
        public void run() {
            FavouritesEditor.deleteFavouritesItem(activity);
            UsefulFunctions.setfloatingActionButtonOnClickIndicator(activity,0);
            UsefulFunctions.uncheckNavigationDrawerMenuItem(activity,1);
            String currentLocationHeaderString=UsefulFunctions.getCurrentLocationAddress()[0];
            String currentLocationSubheaderString=UsefulFunctions.getCurrentLocationAddress()[1];
            UsefulFunctions.setAppBarStrings(activity,currentLocationHeaderString,currentLocationSubheaderString);
            if(FavouritesEditor.isDefeaultLocationEqual(activity,null)) {
                SharedPreferencesModifier.setDefeaultLocationGeolocalization(activity);
            }
        }
    };

    private class saveFavouritesChangesRunnable implements Runnable {
        private View dialogView;

        public saveFavouritesChangesRunnable(View dialogView) {
            this.dialogView = dialogView;
        }

        public void run() {
            EditText headerEditText=(EditText)dialogView.findViewById(R.id.edit_location_dialog_header_edittext);
            EditText subheaderEditText=(EditText)dialogView.findViewById(R.id.edit_location_dialog_subheader_edittext);
            String customHeaderString=headerEditText.getText().toString();
            customHeaderString=UsefulFunctions.getFormattedString(customHeaderString);
            String customSubheaderString=subheaderEditText.getText().toString();
            customSubheaderString=UsefulFunctions.getFormattedString(customSubheaderString);
            FavouritesEditor.editFavouriteLocationName(activity,customHeaderString,customSubheaderString);
            UsefulFunctions.setAppBarStrings(activity,customHeaderString,customSubheaderString);
            CheckBox checkBox=(CheckBox)dialogView.findViewById(R.id.edit_location_dialog_checkbox);
            if(checkBox.isChecked()){
                Log.d("checkbox", "checked");
                String currentLocationHeaderString=UsefulFunctions.getCurrentLocationAddress()[0];
                String currentLocationSubheaderString=UsefulFunctions.getCurrentLocationAddress()[1];
                String currentLocationAddress=currentLocationHeaderString+", "+currentLocationSubheaderString;
                SharedPreferencesModifier.setDefeaultLocationConstant(activity,currentLocationAddress);
            }
            else{
                if(FavouritesEditor.isDefeaultLocationEqual(activity,null)) {
                    SharedPreferencesModifier.setDefeaultLocationGeolocalization(activity);
                }
            }
        }
    }

    private class favouritesDialogRunnable implements Runnable,WeatherDownloadCallback {
        WeatherDataInitializer dataInitializer;
        public favouritesDialogRunnable() {}

        public void run() {
            String address= FavouritesEditor.getChoosenFavouriteLocationAddress(activity);
            Log.d("choosen adress", address);
            new WeatherDataDownloader(address,this);
            initializeProgressDialog(activity.getString(R.string.downloading_weather_data_progress_message));
            progressDialog.show();
        }

        @Override
        public void weatherServiceSuccess(Channel channel) {
            dataInitializer=new WeatherDataInitializer(channel);
            new WeatherDataSetter(activity,dataInitializer,true,false);
            progressDialog.dismiss();
        }

        @Override
        public void weatherServiceFailure(int errorCode) {
            if(errorCode==1)   {
                internetFailureDialog=initializeInternetFailureDialog(1, new favouritesDialogRunnable(),null);
                internetFailureDialog.show();
            }
            else {
                serviceFailureDialog=initializeServiceFailureDialog(1,new favouritesDialogRunnable(),null);
                serviceFailureDialog.show();
            }
            progressDialog.dismiss();
        }
    }

    private AlertDialog buildDialog(Activity activity, View dialogView, int theme, String title, int iconResource, String message, boolean ifUncancelable, String positiveButtonText, final Runnable positiveButtonFunction, String neutralButtonText, final Runnable neutralButtonFunction, String negativeButtonText, final Runnable negativeButtonFunction){
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

    public AlertDialog initializeSearchDialog(int type,RadioButton radioButton){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.search_dialog,null);
        dialogView.setFocusableInTouchMode(true);
        dialogView.setClickable(true);
        final EditText locationEditText=(EditText)dialogView.findViewById(R.id.search_edit_text);
        locationEditText.requestFocus();
        String title=null;
        int icon=0;
        String positiveButtonString=null;
        Runnable positiveButtonRunnable=null;
        if(type==0){
            String radioButtonString=radioButton.getText().toString();
            if(!radioButtonString.equals(activity.getString(R.string.first_launch_defeault_location_different))){
                locationEditText.setText(radioButtonString);
                locationEditText.setSelection(radioButtonString.length());
            }
            title=activity.getString(R.string.search_dialog_title_type_0);
            icon=R.drawable.localization_icon;
            positiveButtonString=activity.getString(R.string.search_dialog_positive_button_type_0);
            positiveButtonRunnable=new differentLocationDialogRunnable(radioButton,dialogView);
        }
        else if(type==1){
            title=activity.getString(R.string.search_dialog_title_type_1);
            icon=R.drawable.search_icon;
            positiveButtonString=activity.getString(R.string.search_dialog_positive_button_type_1);
            positiveButtonRunnable=new searchRunnable(dialogView);
        }
        searchDialog=buildDialog(
                activity,
                dialogView,
                R.style.CustomDialogStyle,
                title,
                icon,
                null,
                false,
                positiveButtonString,
                positiveButtonRunnable ,
                null,
                null,
                activity.getString(R.string.search_dialog_negative_button),
                null
        );
        searchDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                UsefulFunctions.customizeEditText(activity,searchDialog,locationEditText);
            }
        });
        searchDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                UsefulFunctions.hideKeyboard(activity,null);
                locationEditText.getText().clear();
            }
        });
        return searchDialog;
    }

    public AlertDialog initializeGeolocalizationFailureDialog(int type, Runnable positiveButtonRunnable, Runnable negativeButtonRunnable){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.one_line_text_dialog,null);
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.one_line_text_dialog_message_text);
        messageTextView.setText(activity.getString(R.string.geolocalization_failure_dialog_message));
        boolean isUncancelable=false;
        String positiveButtonString=null;
        String negativeButtonString=null;
        if(type==0){
            isUncancelable=true;
            positiveButtonString=activity.getString(R.string.geolocalization_failure_dialog_positive_button_type_0);
            negativeButtonString=activity.getString(R.string.geolocalization_failure_dialog_negative_button_type_0);
        }
        else if(type==1){
            isUncancelable=false;
            positiveButtonString=activity.getString(R.string.geolocalization_failure_dialog_positive_button_type_1);
            negativeButtonString=activity.getString(R.string.geolocalization_failure_dialog_negative_button_type_1);
        }
        localizationFailureDialog=buildDialog(
                activity,
                dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.failure_dialog_title),
                R.drawable.error_icon,
                null,
                isUncancelable,
                positiveButtonString,
                positiveButtonRunnable,
                null,
                null,
                negativeButtonString,
                negativeButtonRunnable
        );
        return localizationFailureDialog;
    }

    public AlertDialog initializeProviderUnavailableDialog(int type,Runnable positiveButtonRunnable){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.one_line_text_dialog,null);
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.one_line_text_dialog_message_text);
        String message=null;
        String title=null;
        String positiveButtonString=null;
        if(type==0){
            message=activity.getString(R.string.gps_unavailable_dialog_message);
            title=activity.getString(R.string.gps_unavailable_dialog_title);
            positiveButtonString=activity.getString(R.string.gps_unavailable_dialog_positive_button);
        }
        else if(type==1){
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

    public AlertDialog initializePermissionDeniedDialog(int type,Runnable positiveButtonRunnable, Runnable negativeButtonRunnable){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.one_line_text_dialog,null);
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.one_line_text_dialog_message_text);
        messageTextView.setText(activity.getString(R.string.permission_denied_dialog_message));
        boolean isUncancelable=false;
        String positiveButtonString=null;
        String negativeButtonString=null;
        if(type==0){
            isUncancelable=true;
            positiveButtonString=activity.getString(R.string.permission_denied_dialog_positive_button_type_0);
            negativeButtonString=activity.getString(R.string.permission_denied_dialog_negative_button_type_0);
        }
        else if(type==1){
            isUncancelable=false;
            positiveButtonString=activity.getString(R.string.permission_denied_dialog_positive_button_type_1);
            negativeButtonString=activity.getString(R.string.permission_denied_dialog_negative_button_type_1);
        }
        permissionDeniedDialog=buildDialog(
                activity,
                dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.permission_denied_dialog_title),
                R.drawable.error_icon,
                null,
                isUncancelable,
                positiveButtonString,
                positiveButtonRunnable,
                null,
                null,
                negativeButtonString,
                negativeButtonRunnable
        );
        return permissionDeniedDialog;
    }

    public AlertDialog initializeNoDifferentLocationChoosenDialog(){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.one_line_text_dialog,null);
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.one_line_text_dialog_message_text);
        messageTextView.setText(activity.getString(R.string.no_different_location_choosen_dialog_message));
        noDifferentLocationChoosenDialog =buildDialog(
                activity,
                dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.failure_dialog_title),
                R.drawable.warning_icon,
                null,
                false,
                activity.getString(R.string.no_different_location_choosen_dialog_positive_button),
                showDifferentLocationDialogRunnable,
                null,
                null,
                activity.getString(R.string.no_different_location_choosen_dialog_negative_button),
                null
        );
        return noDifferentLocationChoosenDialog;
    }

    public AlertDialog initializeNoWeatherResultsForLocationDialog(int type, Runnable positiveButtonRunnable, Runnable negativeButtonRunnable){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.one_line_text_dialog,null);
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.one_line_text_dialog_message_text);
        messageTextView.setText(activity.getString(R.string.no_weather_results_for_location_dialog_message));
        boolean isUncancelable=false;
        String negativeButtonString=null;
        String positiveButtonString=null;
        if(type==0){
            isUncancelable=true;
            negativeButtonString=activity.getString(R.string.no_weather_results_for_location_dialog_negative_button_type_0);
            positiveButtonString=activity.getString(R.string.no_weather_results_for_location_dialog_positive_button_type_0_1);
        }
        else if(type==1){
            isUncancelable=false;
            negativeButtonString=activity.getString(R.string.no_weather_results_for_location_dialog_negative_button_type_1_2);
            positiveButtonString=activity.getString(R.string.no_weather_results_for_location_dialog_positive_button_type_0_1);
        }
        else if(type==2){
            isUncancelable=false;
            negativeButtonString=activity.getString(R.string.no_weather_results_for_location_dialog_negative_button_type_1_2);
            positiveButtonString=activity.getString(R.string.no_weather_results_for_location_dialog_positive_button_type_2);
        }
        noWeatherResultsForLocation =buildDialog(
                activity,
                dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.failure_dialog_title),
                R.drawable.error_icon,
                null,
                isUncancelable,
                positiveButtonString,
                positiveButtonRunnable,
                null,
                null,
                negativeButtonString,
                negativeButtonRunnable
        );
        return noWeatherResultsForLocation;
    };

    private AlertDialog initializeNoEmailApplicationDialog(){
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

    public AlertDialog initializeServiceFailureDialog(int type,Runnable positiveButtonRunnable,Runnable negativeButtonRunnable){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.one_line_text_dialog,null);
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.one_line_text_dialog_message_text);
        messageTextView.setText(activity.getString(R.string.service_failure_dialog_message));
        String negativeButtonString=null;
        boolean isUncancelable=false;
        if(type==0){
            negativeButtonString=activity.getString(R.string.service_failure_dialog_negative_button_type_0);
            isUncancelable=true;
        }
        else if(type==1){
            negativeButtonString=activity.getString(R.string.service_failure_dialog_negative_button_type_1);
            isUncancelable=false;
        }
        serviceFailureDialog = buildDialog(
                activity,
                dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.failure_dialog_title),
                R.drawable.error_icon,
                null,
                isUncancelable,
                activity.getString(R.string.service_failure_dialog_positive_button),
                positiveButtonRunnable,
                null,
                null,
                negativeButtonString,
                negativeButtonRunnable
        );
        return serviceFailureDialog;
    }

    public AlertDialog initializeNoFavouritesDialog(){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.one_line_text_dialog,null);
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.one_line_text_dialog_message_text);
        messageTextView.setText(activity.getString(R.string.no_favourites_dialog_message));
        emptyLocationListDialog = buildDialog(
                activity,
                dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.no_favourites_dialog_title),
                R.drawable.info_icon,
                null,
                true,
                activity.getString(R.string.no_favourites_dialog_positive_button),
                null,
                null,
                null,
                null,
                null
        );
        return emptyLocationListDialog;
    }

    public AlertDialog initializeInternetFailureDialog(int type,Runnable positiveButtonRunnable,Runnable negativeButtonRunnable){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.one_line_text_dialog,null);
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.one_line_text_dialog_message_text);
        messageTextView.setText(activity.getString(R.string.internet_failure_dialog_message));
        boolean isUncancelable=false;
        String negativeButtonString=null;
        if(type==0){
            isUncancelable=true;
            negativeButtonString=activity.getString(R.string.internet_failure_dialog_negative_button_type_0);
        }
        else if(type==1){
            isUncancelable=false;
            negativeButtonString=activity.getString(R.string.internet_failure_dialog_negative_button_type_1);
        }
        internetFailureDialog = buildDialog(
                activity,
                dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.failure_dialog_title),
                R.drawable.error_icon,
                null,
                isUncancelable,
                activity.getString(R.string.internet_failure_dialog_positive_button),
                positiveButtonRunnable,
                null,
                null,
                negativeButtonString,
                negativeButtonRunnable
        );
        return internetFailureDialog;
    }

    public AlertDialog initializeExitDialog(int type, Runnable negativeButtonRunnable){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.one_line_text_dialog,null);
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.one_line_text_dialog_message_text);
        messageTextView.setText(activity.getText(R.string.exit_dialog_message));
        boolean isUncancelable=false;
        if(type==0) isUncancelable=true;
        else if(type==1) isUncancelable=false;
        exitDialog = buildDialog(
                activity,
                dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.exit_dialog_title),
                R.drawable.warning_icon,
                null,
                isUncancelable,
                activity.getString(R.string.exit_dialog_positive_button),
                finishRunnable,
                null,
                null,
                activity.getString(R.string.exit_dialog_negative_button),
                negativeButtonRunnable
        );
        return exitDialog;
    }

    public AlertDialog initializeYahooRedirectDialog(){
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

    public AlertDialog initializeYahooWeatherRedirectDialog(Activity activity, String link){
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

    public AlertDialog initializeFeedbackDialog(){
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

    public AlertDialog initializeAddToFavouritesDialog(){
        LayoutInflater inflater = activity.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.edit_location_dialog,null);
        dialogView.setFocusableInTouchMode(true);
        dialogView.setClickable(true);
        String [] location=UsefulFunctions.getAppBarStrings(activity);
        final EditText headerEditText=(EditText)dialogView.findViewById(R.id.edit_location_dialog_header_edittext);
        headerEditText.setText(location[0]);
        final EditText subheaderEditText=(EditText)dialogView.findViewById(R.id.edit_location_dialog_subheader_edittext);
        subheaderEditText.setText(location[1]);
        addToFavouritesDialog = buildDialog(
                activity,
                dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.add_location_dialog_title),
                R.drawable.add_blue_icon,
                null,
                false,
                activity.getString(R.string.add_location_dialog_positive_button),
                new addToFavouritesRunnable(dialogView),
                null,
                null,
                activity.getString(R.string.add_location_dialog_negative_button),
                null
        );
        addToFavouritesDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                UsefulFunctions.customizeEditText(activity,addToFavouritesDialog,headerEditText);
                UsefulFunctions.customizeEditText(activity,addToFavouritesDialog,subheaderEditText);
            }
        });
        addToFavouritesDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                UsefulFunctions.hideKeyboard(activity,null);
            }
        });
        return addToFavouritesDialog;
    }

    public AlertDialog initializeEditFavouritesDialog(){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.edit_location_dialog,null);
        dialogView.setFocusableInTouchMode(true);
        dialogView.setClickable(true);
        String [] location=UsefulFunctions.getAppBarStrings(activity);
        final EditText headerEditText=(EditText)dialogView.findViewById(R.id.edit_location_dialog_header_edittext);
        headerEditText.setText(location[0]);
        final EditText subheaderEditText=(EditText)dialogView.findViewById(R.id.edit_location_dialog_subheader_edittext);
        subheaderEditText.setText(location[1]);
        CheckBox checkBox=(CheckBox)dialogView.findViewById(R.id.edit_location_dialog_checkbox);
        checkBox.setChecked(FavouritesEditor.isDefeaultLocationEqual(activity,null));
        editFavouritesDialog = buildDialog(
                activity,
                dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.edit_location_dialog_title),
                R.drawable.edit_blue_icon,
                null,
                false,
                activity.getString(R.string.edit_location_dialog_positive_button),
                new saveFavouritesChangesRunnable(dialogView),
                activity.getString(R.string.edit_location_dialog_neutral_button),
                deleteFromFavouritesRunnable,
                activity.getString(R.string.edit_location_dialog_negative_button),
                null
        );
        editFavouritesDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                UsefulFunctions.customizeEditText(activity,editFavouritesDialog,headerEditText);
                UsefulFunctions.customizeEditText(activity,editFavouritesDialog,subheaderEditText);
            }
        });
        editFavouritesDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                Log.d("dismiss", "onDismiss: ");
                UsefulFunctions.hideKeyboard(activity,null);
            }
        });
        return editFavouritesDialog;
    }

    public AlertDialog initializeProgressDialog(String message){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.progress_dialog,null);
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.progress_dialog_message_text);
        messageTextView.setText(message);
        progressDialog =buildDialog(
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

        return progressDialog;
    }

    public AlertDialog initializeWeatherResultsForLocationDialog(int type, WeatherDataInitializer dataInitializer, Runnable positiveButtonRunnable, Runnable neutralButtonRunnable, Runnable negativeButtonRunnable) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.location_dialog, null);
        String city=dataInitializer.getCity();
        String region=dataInitializer.getRegion();
        String country=dataInitializer.getCountry();
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.location_dialog_message_text);
        messageTextView.setText(activity.getString(R.string.weather_results_for_location_dialog_message));
        TextView cityTextView=(TextView)dialogView.findViewById(R.id.location_dialog_city_text);
        cityTextView.setText(city);
        TextView regionCountryTextView=(TextView)dialogView.findViewById(R.id.location_dialog_region_county_text);
        regionCountryTextView.setText(region+", "+country);
        boolean ifUncancellable=false;
        String negativeButtonString=null;
        if(type==0){
            ifUncancellable=true;
            negativeButtonString=activity.getString(R.string.first_launch_localization_results_dialog_negative_button_type_0);
        }
        else if(type==1){
            ifUncancellable=false;
            negativeButtonString=activity.getString(R.string.weather_results_for_location_dialog_negative_button_type_1);
        }
        localizationResultsDialog = buildDialog(
                activity,
                dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.weather_results_for_location_dialog_title),
                R.drawable.localization_icon,
                null,
                ifUncancellable,
                activity.getString(R.string.weather_results_for_location_dialog_positive_button),
                positiveButtonRunnable,
                activity.getString(R.string.weather_results_for_location_dialog_neutral_button),
                neutralButtonRunnable,
                negativeButtonString,
                negativeButtonRunnable);
        return localizationResultsDialog;
    }

    public AlertDialog initializeMapsDialog(Activity activity) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.location_dialog, null);
        TextView messageTextView=(TextView)dialogView.findViewById(R.id.location_dialog_message_text);
        messageTextView.setText(activity.getString(R.string.maps_dialog_message));
        TextView cityTextView=(TextView)dialogView.findViewById(R.id.location_dialog_city_text);
        String [] locationName=UsefulFunctions.getAppBarStrings(activity);
        cityTextView.setText(locationName[0]);
        TextView regionCountryTextView=(TextView)dialogView.findViewById(R.id.location_dialog_region_county_text);
        regionCountryTextView.setText(locationName[1]);
        double [] locationCoordinates={Double.valueOf(UsefulFunctions.getCurrentLocationCoordinates()[0]),
                Double.valueOf(UsefulFunctions.getCurrentLocationCoordinates()[1])};
        mapsDialog = buildDialog(
                activity,
                dialogView,
                R.style.CustomDialogStyle,
                activity.getString(R.string.maps_dialog_title),
                R.drawable.maps_icon,
                null,
                false,
                activity.getString(R.string.maps_dialog_positive_button),
                new initializeMapsIntentRunnable(locationName[0],locationCoordinates[0], locationCoordinates[1]),
                null,
                null,
                activity.getString(R.string.maps_dialog_negative_button),
                null);
        return mapsDialog;
    }

    public AlertDialog initializeGeolocalizationMethodsDialog(Runnable positiveButtonRunnable){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.localization_options_dialog,null);
        RadioGroup radioGroup=(RadioGroup)dialogView.findViewById(R.id.localization_options_dialog_radio_group);
        RadioButton gpsRadioButton=new RadioButton(activity);
        gpsRadioButton.setText(activity.getString(R.string.geolocalization_method_gps));
        gpsRadioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,activity.getResources().getDimensionPixelSize(R.dimen.dialog_text_size));
        gpsRadioButton.setTextColor(activity.getResources().getColor(R.color.textSecondaryLightBackground));
        gpsRadioButton.setId(R.id.geolocalization_method_dialog_gps_radio_button_id);
        UsefulFunctions.setRadioButtonMargins(gpsRadioButton,activity,0,0,0,16);
        radioGroup.addView(gpsRadioButton);
        RadioButton networkRadioButton=new RadioButton(activity);
        networkRadioButton.setText(activity.getString(R.string.geolocalization_method_network));
        networkRadioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,activity.getResources().getDimensionPixelSize(R.dimen.dialog_text_size));
        networkRadioButton.setTextColor(activity.getResources().getColor(R.color.textSecondaryLightBackground));
        networkRadioButton.setId(R.id.geolocalization_method_dialog_network_radio_button_id);
        radioGroup.addView(networkRadioButton);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                UsefulFunctions.setDialogButtonEnabled(localizationOptionsDialog,activity);
                if (i == R.id.geolocalization_method_dialog_gps_radio_button_id) {
                    Log.d("localization_option", "gps");
                    SharedPreferencesModifier.setGeolocalizationMethod(activity,0);
                } else if (i == R.id.geolocalization_method_dialog_network_radio_button_id) {
                    Log.d("localization_option", "network");
                    SharedPreferencesModifier.setGeolocalizationMethod(activity,1);
                }
            }
        });
        localizationOptionsDialog = buildDialog(
                activity,
                dialogView,
                R.style.CustomLoadingDialogStyle,
                activity.getString(R.string.geolocalization_methods_dialog_title),
                R.drawable.localization_icon,
                null,
                true,
                activity.getString(R.string.geolocalization_methods_dialog_positive_button),
                positiveButtonRunnable,
                null,
                null,
                null,
                null
        );
        localizationOptionsDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                UsefulFunctions.setDialogButtonDisabled(localizationOptionsDialog,activity);
            }
        });
        return localizationOptionsDialog;
    }

    public AlertDialog initializeAuthorDialog(){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.author_dialog,null);
        ImageView emailImageView=(ImageView)dialogView.findViewById(R.id.author_dialog_mail_image);
        ImageView githubImageView=(ImageView)dialogView.findViewById(R.id.author_dialog_github_image);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.email_icon).transform(new UsefulFunctions().new setDrawableColor(activity.getResources().getColor(R.color.white))).fit().centerInside().into(emailImageView);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.github_icon).transform(new UsefulFunctions().new setDrawableColor(activity.getResources().getColor(R.color.white))).fit().centerInside().into(githubImageView);
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

    public AlertDialog initializeAboutDialog(){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.about_dialog,null);
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
        ImageView iconImageView=(ImageView)dialogView.findViewById(R.id.about_dialog_app_icon_image);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.app_icon_small).fit().centerInside().into(iconImageView);
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

    public AlertDialog initializeFavouritesDialog(final int type, Runnable positiveButtonRunnable, Runnable negativeButtonRunnable){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.radiogroup_dialog,null);
        final RadioGroup radioGroup = (RadioGroup) dialogView.findViewById(R.id.radiogroup_dialog_radiogroup);
        final List<String> favouritesList = FavouritesEditor.getFavouriteLocationsNamesDialogList(activity);
        String locationName=null;
        int size=favouritesList.size();
        if(type==0) {
            size=++size;
        }
        for(int i=0;i<size;i++){
            RadioButton radioButton=new RadioButton(activity);
            if(i!=size-1){
                UsefulFunctions.setRadioButtonMargins(radioButton,activity,0,0,0,16);
                if(type==0) locationName=favouritesList.get(i);
            }
            else{
                if(type==0) locationName=activity.getString(R.string.favourites_dialog_geolocalization_option);
            }
            if(type==1) locationName=favouritesList.get(i);
            radioButton.setId(i);
            radioButton.setText(Html.fromHtml(locationName));
            radioButton.setTextSize(TypedValue.COMPLEX_UNIT_PX,activity.getResources().getDimensionPixelSize(R.dimen.locations_list_text_size));
            radioButton.setSingleLine();
            radioButton.setEllipsize(TextUtils.TruncateAt.END);
            radioButton.setTextColor(activity.getResources().getColor(R.color.textSecondaryLightBackground));
            radioGroup.addView(radioButton);
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                UsefulFunctions.setDialogButtonEnabled(favouritesDialog,activity);
                FavouritesEditor.setChoosenFavouriteLocationID(i);
                Log.d("wybrane id", ""+i);
            }
        });
        String title=null;
        String negativeButtonString=null;
        boolean isUncancelable=false;
        int icon=0;
        if(type==0) {
            title=activity.getString(R.string.favourites_dialog_first_launch_title);
            negativeButtonString=activity.getString(R.string.favourites_dialog_negative_button_type_0);
            isUncancelable=true;
            icon=R.drawable.localization_icon;
        }
        else if(type==1){
            positiveButtonRunnable=new favouritesDialogRunnable();
            negativeButtonString=activity.getString(R.string.favourites_dialog_negative_button_type_1);
            title=activity.getString(R.string.favourites_dialog_title);
            isUncancelable=false;
            icon=R.drawable.favourites_icon;
        }
        favouritesDialog = buildDialog(
                activity,
                dialogView,
                R.style.CustomDialogStyle,
                title,
                icon,
                null,
                isUncancelable,
                activity.getString(R.string.favourites_dialog_positive_button),
                positiveButtonRunnable,
                null,
                null,
                negativeButtonString,
                negativeButtonRunnable);
        favouritesDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialogInterface) {
                UsefulFunctions.setDialogButtonDisabled(favouritesDialog,activity);
            }
        });
        return favouritesDialog;
    }
}