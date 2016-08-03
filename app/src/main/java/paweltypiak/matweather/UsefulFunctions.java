package paweltypiak.matweather;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Transformation;

import java.util.StringTokenizer;

import paweltypiak.matweather.weatherDataProcessing.WeatherDataSetter;

public class UsefulFunctions {
    private static boolean isFirstRefresh;
    public static boolean getIsFirstRefresh() {
        return isFirstRefresh;
    }
    public static void setIsFirstRefresh(boolean bool) {
        isFirstRefresh = bool;
    }
    private static SharedPreferences sharedPreferences;

    public static SharedPreferences getSharedPreferences(Activity activity){
        if(sharedPreferences==null) {
            sharedPreferences = activity.getSharedPreferences(
                    activity.getString(R.string.shared_preferences_key_name), activity.MODE_PRIVATE);
        }
        return sharedPreferences;
    }

    public static boolean getIsFirstLaunch(Activity activity){
        boolean isFirstLaunch=getSharedPreferences(activity).getBoolean(activity.getString(R.string.shared_preferences_is_first_launch_key),true);
        Log.d("isFirst", ""+isFirstLaunch);
        return isFirstLaunch;
    }

    public static void setIsFirstLaunch(Activity activity){
        getSharedPreferences(activity).edit().putBoolean(activity.getString(R.string.shared_preferences_is_first_launch_key),true).commit();
    }

    public static int getLanguage(Activity activity){
        int language=getSharedPreferences(activity).getInt(activity.getString(R.string.shared_preferences_language_key), 0);
        return language;
    }

    public static void setLanguage(Activity activity, int option){
        getSharedPreferences(activity).edit().putInt(activity.getString(R.string.shared_preferences_language_key), option).commit();
    }

    public static int[] getUnits(Activity activity){
        String unitsString = getSharedPreferences(activity).getString(activity.getString(R.string.shared_preferences_units_key), "0,0,0,0,0,");
        Log.d("units_useful_func", unitsString);
        int [] units=new int [5];
        StringTokenizer stringTokenizer = new StringTokenizer(unitsString, ",");
        for (int i = 0; i < units.length; i++) {
            units[i] = Integer.parseInt(stringTokenizer.nextToken());
            Log.d("units_static", ""+ units[i]);
        }
        return units;
    }

    public static void setUnits(Activity activity, int []unitsArray){
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < unitsArray.length; i++) {
            stringBuilder.append(unitsArray[i]).append(",");
        }
        getSharedPreferences(activity).edit().putString(activity.getString(R.string.shared_preferences_units_key), stringBuilder.toString()).commit();
    }

    public static int getLocalizationOptionKey(Activity activity){
        int localizationOptionKey=getSharedPreferences(activity).getInt(activity.getString(R.string.shared_preferences_localization_option_key),0);
        Log.d("localizationOption", ""+localizationOptionKey);
        return localizationOptionKey;
    }

    public static void setLocalizationOptionKey(Activity activity,int option){
        getSharedPreferences(activity).edit().putInt(activity.getString(R.string.shared_preferences_localization_option_key),option);
    }

    public static String getFirstLocation(Activity activity){
        String firstLocation=getSharedPreferences(activity).getString(activity.getString(R.string.shared_preferences_first_location_key),null);
        Log.d("FirstLocation", ""+firstLocation);
        return firstLocation;
    }

    public static void setFirstLocation(Activity activity, String locationName){
        if(locationName==null){
            String currentLocationHeaderString=UsefulFunctions.getCurrentLocationStrings()[0];
            String currentLocationSubheaderString=UsefulFunctions.getCurrentLocationStrings()[1];
            locationName=currentLocationHeaderString+", "+currentLocationSubheaderString;
        }
        getSharedPreferences(activity).edit().putString(activity.getString(R.string.shared_preferences_first_location_key), locationName).commit();
    }

    public static void resetFirstLocation(Activity activity){
        getSharedPreferences(activity).edit().putString(activity.getString(R.string.shared_preferences_first_location_key), null).commit();
    }

    public static String[] getFavouriteLocationsNames(Activity activity){
        String favouritesString = getSharedPreferences(activity).getString(activity.getString(R.string.shared_preferences_favourite_locations_names_key), "");
        Log.d("fav_names_string", favouritesString);
        StringTokenizer stringTokenizer = new StringTokenizer(favouritesString, "|");
        int numberOfLocations=stringTokenizer.countTokens();
        String favourites[]=new String[numberOfLocations];
        for (int i = 0; i < numberOfLocations; i++) {
            favourites[i] = stringTokenizer.nextToken();
            Log.d("favourite name", ""+ favourites[i]);
        }
        return favourites;
    }

    public static void saveNewFavouriteLocationName(String location, Activity activity){
        String favourites[]= getFavouriteLocationsNames(activity);
        StringBuilder stringBuilder=buildStringFromStringArray(favourites);
        stringBuilder.append(location).append("|");
        String favouritesNamesString=stringBuilder.toString();
        Log.d("string_names_save", ""+favouritesNamesString);
        getSharedPreferences(activity).edit().putString(activity.getString(R.string.shared_preferences_favourite_locations_names_key), favouritesNamesString).commit();
    }

    public static String[] getFavouriteLocationsAddresses(Activity activity){
        String favouritesString = getSharedPreferences(activity).getString(activity.getString(R.string.shared_preferences_favourite_locations_addresses_key), "");
        Log.d("fav_addresses_string", favouritesString);
        StringTokenizer stringTokenizer = new StringTokenizer(favouritesString, "|");
        int numberOfLocations=stringTokenizer.countTokens();
        String favourites[]=new String[numberOfLocations];
        for (int i = 0; i < numberOfLocations; i++) {
            favourites[i] = stringTokenizer.nextToken();
            Log.d("favourite address", ""+ favourites[i]);
        }
        return favourites;
    }

    public static void saveNewFavouriteLocationAddress(Activity activity){
        String currentLocationHeaderString=UsefulFunctions.getCurrentLocationStrings()[0];
        String currentLocationSubheaderString=UsefulFunctions.getCurrentLocationStrings()[1];
        String currentLocationNameString=currentLocationHeaderString+", "+currentLocationSubheaderString;
        String favourites[]= getFavouriteLocationsAddresses(activity);
        StringBuilder stringBuilder=buildStringFromStringArray(favourites);
        stringBuilder.append(currentLocationNameString).append("|");
        String favouritesAddressesString=stringBuilder.toString();
        Log.d("string_Address_save", ""+favouritesAddressesString);
        getSharedPreferences(activity).edit().putString(activity.getString(R.string.shared_preferences_favourite_locations_addresses_key), favouritesAddressesString).commit();
    }

    public static String[] getFavouriteLocationsCoordinates(Activity activity){
        String favouritesString = getSharedPreferences(activity).getString(activity.getString(R.string.shared_preferences_favourite_locations_coordinates_key), "");
        Log.d("fav_coordinates_string", favouritesString);
        StringTokenizer stringTokenizer = new StringTokenizer(favouritesString, "|");
        int numberOfLocations=stringTokenizer.countTokens();
        String favourites[]=new String[numberOfLocations];
        for (int i = 0; i < numberOfLocations; i++) {
            favourites[i] = stringTokenizer.nextToken();
            Log.d("favourite coordinates", ""+ favourites[i]);
        }
        return favourites;
    }

    public static void saveNewFavouriteLocationCoordinates(Activity activity){
        String currentLocationLatitude=UsefulFunctions.getCurrentLocationCoordinates()[0];
        String currentLocationLongitude=UsefulFunctions.getCurrentLocationCoordinates()[1];
        String currentLocationCoordinatesString=currentLocationLatitude+"%"+currentLocationLongitude;
        String favourites[]= getFavouriteLocationsCoordinates(activity);
        StringBuilder stringBuilder=buildStringFromStringArray(favourites);
        stringBuilder.append(currentLocationCoordinatesString).append("|");
        String favouritesCoordinateString=stringBuilder.toString();
        Log.d("string_Coordinate_save", ""+favouritesCoordinateString);
        getSharedPreferences(activity).edit().putString(activity.getString(R.string.shared_preferences_favourite_locations_coordinates_key), favouritesCoordinateString).commit();
    }

    private static StringBuilder buildStringFromStringArray(String[] stringArray){
        int numberOfLocations=stringArray.length;
        Log.d("favourites_number", ""+numberOfLocations);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < numberOfLocations; i++) {
            stringBuilder.append(stringArray[i]).append("|");
        }
        return stringBuilder;
    }



    public static boolean areCoordinatesEqual(Activity activity){
        String favouritesCoordinates[]= getFavouriteLocationsCoordinates(activity);
        boolean isEqual=false;
        String coordinates[]=new String [2];
        for(int i=0;i<favouritesCoordinates.length;i++){
            StringTokenizer stringTokenizer = new StringTokenizer(favouritesCoordinates[i], "%");
            for (int j = 0; j < 2; j++) {
                coordinates[j] = stringTokenizer.nextToken();
            }
            if(coordinates[0].equals(getCurrentLocationCoordinates()[0])
                    && coordinates[1].equals(getCurrentLocationCoordinates()[1])) isEqual=true;
        }
        return isEqual;
    }

    public static void initializeWebIntent(Activity activity, String url){
        //initialize web intent
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        activity.startActivity(intent);
    }

    public static void initializeMapsIntent(Activity activity,double longitude, double latitude, String label){
        String uriBegin = "geo:" + latitude + "," + longitude;
        String query = latitude + "," + longitude + "(" + label + ")";
        String encodedQuery = Uri.encode(query);
        String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
        Uri uri = Uri.parse(uriString);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
        activity.startActivity(intent);
    }

    public static void initializeEmailIntent(Activity activity, String address, String subject, String body, AlertDialog dialog){
        //initialize email intent
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/plain");
        if(subject!=null) intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if(body!=null)intent.putExtra(Intent.EXTRA_TEXT, body);
        intent.setData(Uri.parse("mailto:"+address));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            activity.startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            dialog.show();
        }
    }

    public static void copyToClipboard(Activity activity, String text){
        //initialize copy to clipboard
        ClipboardManager myClipboard;
        ClipData myClip;
        myClipboard = (ClipboardManager)activity.getSystemService(Context.CLIPBOARD_SERVICE);
        myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);
        Toast.makeText(activity.getApplicationContext(), activity.getString(R.string.clipboard_toast_message),Toast.LENGTH_SHORT).show();
    }

    public static String getFormattedString(String string){
        if(string.length()!=0){
            string=getStringWithUpperCase(string);
            string=getStringWithoutLastSpace(string);
        }
        return string;
    }

    private static String getStringWithoutLastSpace(String string){
        if(string.length()!=0){
            while(string.substring(string.length()-1).equals(" ")){
                string=string.substring(0,string.length()-1);
            }
        }
        return string;
    }

    private static String getStringWithoutFirstSpace(String string){
        while(string.substring(0,1).equals(" ")){
            string=string.substring(1,string.length());
            if(string.length()==0) break;
        }
        return string;
    }

    private static String getStringWithUpperCase(String string){
        string=getStringWithoutFirstSpace(string);
        if(string.length()!=0) string=string.substring(0, 1).toUpperCase() + string.substring(1);
        return string;
    }

    public static void customizeEditText(final EditText editText, final Activity activity){
        final String hint=editText.getHint().toString();
        editText.setHint("");
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            String previusEditTextString;
            @Override
            public void onFocusChange(View view, boolean b) {
                Log.d("onfocus", "onFocusChange: ");
                if(editText.isFocused()) {
                    previusEditTextString=editText.getText().toString();
                    showKeyboard(activity);
                }
                else {
                    String editTextString=editText.getText().toString();
                    editTextString=getFormattedString(editTextString);
                    if(editTextString.length()==0) editText.setText(previusEditTextString);
                    else editText.setText(editTextString);
                    hideKeyboard(activity,editText);
                }
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(editText.getText().length()==0) editText.setHint(hint);
                else editText.setHint("");
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        && keyEvent.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    editText.clearFocus();
                    return false;
                }
                return false;
            }
        });
    }



    public static String[] getCurrentLocationStrings(){
        String[] location=new String[2];
        location[0]= WeatherDataSetter.getCurrentDataFormatter().getCity();
        location[1]=WeatherDataSetter.getCurrentDataFormatter().getRegion()+", "+WeatherDataSetter.getCurrentDataFormatter().getCountry();

        return location;
    }

    public static String[] getCurrentLocationCoordinates(){
        String[] coordinates=new String[2];
        coordinates[0]= Double.toString(WeatherDataSetter.getCurrentDataFormatter().getLatitude());
        coordinates[1]=Double.toString(WeatherDataSetter.getCurrentDataFormatter().getLongitude());

        return coordinates;
    }

    public static String[] getAppBarStrings(Activity activity){
        String[] location=new String[2];
        TextView primaryLocationTextView=(TextView)activity.findViewById(R.id.app_bar_primary_location_name_text);
        TextView secondaryLocationTextView=(TextView)activity.findViewById(R.id.app_bar_secondary_location_name_text);

        location[0]=primaryLocationTextView.getText().toString();
        location[1]=secondaryLocationTextView.getText().toString();

        return location;
    }

    public static void setAppBarStrings(Activity activity, String primaryText, String secondaryText){
        TextView primaryLocationTextView=(TextView)activity.findViewById(R.id.app_bar_primary_location_name_text);
        TextView secondaryLocationTextView=(TextView)activity.findViewById(R.id.app_bar_secondary_location_name_text);

        primaryLocationTextView.setText(primaryText);
        secondaryLocationTextView.setText(secondaryText);

        int visibility = primaryLocationTextView.getVisibility();
        primaryLocationTextView.setVisibility(View.GONE);
        primaryLocationTextView.setVisibility(visibility);
        secondaryLocationTextView.setVisibility(View.GONE);
        secondaryLocationTextView.setVisibility(visibility);
    }

    public static void showKeyboard(Activity activity){
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

    }
    public static void hideKeyboard(Activity activity, EditText editText) {
        if(editText==null)activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        else{
            InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }


    public static void setViewVisible(View view){
        view.setVisibility(View.VISIBLE);
    }
    public static void setViewInvisible(View view){
        view.setVisibility(View.INVISIBLE);
    }
    public static void setViewGone(View view){
        view.setVisibility(View.GONE);
    }

    public static double getPullOpacity(double screenPercentage,float movedDistance, Context context, boolean isVertical){
        int screenWidth=getScreenResolution(context)[0];
        int screenHeight=getScreenResolution(context)[1];
        double alpha=0;
        if(movedDistance<0) movedDistance=0;
        if(isVertical==true){alpha=(movedDistance*255)/(screenPercentage*screenHeight);}
        else {alpha=(movedDistance*255)/(screenPercentage*screenWidth);}
        if(alpha>255) alpha=255;
        else if(alpha<100) alpha=100;
        return alpha;
    }

    public static Thread initializeUiThread(final Activity activity, final Runnable runnable) {
        Thread uiThread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                runnable.run();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        Log.d("uithread", "start ");
        return uiThread;
    }

    public static int[] getScreenResolution(Context context)
    {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        int resolution[]={width,height};
        return resolution;
    }

    public static int pixelsToDp(int pixels,Activity activity){
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float logicalDensity = metrics.density;
        int dp = (int) Math.ceil(pixels / logicalDensity);
        return dp;
    }

    public static int dpToPixels(int dp,Activity activity){
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float logicalDensity = metrics.density;
        int pixels = (int) Math.ceil(dp * logicalDensity);
        return pixels;
    }

    public static void setPadding(View view, Activity activity, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom){
        int paddingLeftPixels=dpToPixels(paddingLeft,activity);
        int paddingTopPixels=dpToPixels(paddingTop,activity);
        int paddingRightPixels=dpToPixels(paddingRight,activity);
        int paddingBottomPixels=dpToPixels(paddingBottom,activity);

        view.setPadding(paddingLeftPixels,paddingTopPixels,paddingRightPixels,paddingBottomPixels);
    }

    public class setDrawableColor implements Transformation {

        private int color = 0;

        public setDrawableColor(int color ) {
            this.color = color;
        }

        @Override
        public Bitmap transform(Bitmap source) {
            if( color == 0 ) {
                return source;
            }
            BitmapDrawable drawable = new BitmapDrawable(Resources.getSystem(), source );
            Bitmap result = Bitmap.createBitmap( drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888 );
            Canvas canvas = new Canvas( result );
            drawable.setBounds( 0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight() );
            drawable.setColorFilter( color, PorterDuff.Mode.SRC_IN );
            drawable.draw(canvas);
            drawable.setColorFilter(null);
            drawable.setCallback(null);
            if( result != source ) {
                source.recycle();
            }
            return result;
        }

        @Override
        public String key() {
            return "DrawableColor:" + color;
        }
    }
}
