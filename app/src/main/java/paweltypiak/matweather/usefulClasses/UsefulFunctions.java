package paweltypiak.matweather.usefulClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Transformation;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;

import paweltypiak.matweather.MainActivity;
import paweltypiak.matweather.R;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataSetter;

public class UsefulFunctions {
    private static boolean isFirstWeatherDownloading;
    public static boolean getIsFirstWeatherDownloading() {
        return isFirstWeatherDownloading;
    }
    public static void setIsFirstWeatherDownloading(boolean bool) {
        isFirstWeatherDownloading = bool;
    }

    public static class FavouritesMaker {
        private static int choosenLocationId;
        private static Activity activity;
        private static String[] favouritesHeaderNames;
        private static String[] favouritesSubheaderNames;

        public FavouritesMaker(Activity activity){
            this.activity=activity;
            getFavouriteLocationsNamesList();
        }

        public static void setChoosenLocationID(int id){
            choosenLocationId=id;
        }

        public static String getChoosenFavouriteLocationAddress(){
            String[] addresses=SharedPreferencesModifier.getFavouriteLocationsAddresses(activity);
            Log.d("adres", addresses[choosenLocationId]);
            return addresses[choosenLocationId];
        }

        private static String makeLocationsDialogName(String header, String subheader){
            String name="<b>"+header+"</b>";
            if(!subheader.equals(""))name=name+", "+subheader;
            return name;
        }

        public static void setAppBarForChoosenFavouriteLocation(){
            setAppBarStrings(activity,favouritesHeaderNames[choosenLocationId],favouritesSubheaderNames[choosenLocationId]);
        }

        private static void getFavouriteLocationsNamesList(){
            String[] favourites=SharedPreferencesModifier.getFavouriteLocationsNames(activity);
            int favouritesSize=favourites.length;
            favouritesHeaderNames=new String[favouritesSize];
            favouritesSubheaderNames=new String[favouritesSize];
            for(int i=0;i<favouritesSize;i++){
                StringTokenizer stringTokenizer = new StringTokenizer(favourites[i], "%");
                String locationPartName[]={"",""};
                int tokenizerSize=stringTokenizer.countTokens();
                Log.d("tokinizersize", ""+stringTokenizer.countTokens());
                for (int j = 0; j < tokenizerSize; j++) {
                    locationPartName[j] = stringTokenizer.nextToken();
                    Log.d("partname", ""+locationPartName[j]);
                }
                favouritesHeaderNames[i]=locationPartName[0];
                favouritesSubheaderNames[i]=locationPartName[1];
            }
        }

        public static List<String> getFavouriteLocationsNamesDialogList(){
            int size=favouritesHeaderNames.length;
            String locationNames[]=new String[size];
            for(int i=0;i<size;i++){
                locationNames[i]=makeLocationsDialogName(favouritesHeaderNames[i],favouritesSubheaderNames[i]);
            }
            List<String> favouriteLocationsNamesDialogList=Arrays.asList(locationNames);
            return favouriteLocationsNamesDialogList;
        }
    }

    public static boolean areCoordinatesEqual(Activity activity){
        String favouritesCoordinates[]= SharedPreferencesModifier.getFavouriteLocationsCoordinates(activity);
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

    public static void setfloatingActionButtonOnClickIndicator(Activity activity,int  fabIndicator) {
        FloatingActionButton floatingActionButton=(FloatingActionButton)activity.findViewById(R.id.main_fab);
        MainActivity.setFloatingActionButtonOnClickIndicator(fabIndicator);
        if(fabIndicator==1) floatingActionButton.setImageResource(R.drawable.add_black_icon);
        else floatingActionButton.setImageResource(R.drawable.edit_black_icon);
    }

    public static String[] getCurrentLocationAddress(){
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
        setViewGone(primaryLocationTextView);
        setViewVisible(primaryLocationTextView);
        secondaryLocationTextView.setText(secondaryText);
        setViewGone(secondaryLocationTextView);
        if(!secondaryText.equals("")) setViewVisible(secondaryLocationTextView);

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

    public static void uncheckNavigationDrawerMenuItems(Activity activity){
        NavigationView navigationView = (NavigationView)activity. findViewById(R.id.nav_view);
        MenuItem geolocalizationItem=navigationView.getMenu().findItem(R.id.nav_button_geolocalization);;
        MenuItem favouritesItem=navigationView.getMenu().findItem(R.id.nav_button_favourites);
        favouritesItem.setChecked(false);
        geolocalizationItem.setChecked(false);
        Log.d("unchecked", "unchecked ");

    }

    public static void checkNavigationDrawerMenuItem(Activity activity, int itemId){
        NavigationView navigationView = (NavigationView)activity. findViewById(R.id.nav_view);
        MenuItem geolocalizationItem=navigationView.getMenu().findItem(R.id.nav_button_geolocalization);;
        MenuItem favouritesItem=navigationView.getMenu().findItem(R.id.nav_button_favourites);
        if(itemId==0) {
            geolocalizationItem.setCheckable(true);
            geolocalizationItem.setChecked(true);
            favouritesItem.setChecked(false);
        }
        else {
            favouritesItem.setCheckable(true);
            favouritesItem.setChecked(true);
            geolocalizationItem.setChecked(false);
        }
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
        double alpha;
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

    public static void setRadiogroupMargins(View view, Activity activity, int marginLeft, int marginTop, int marginRight, int marginBottom){
        int marginLeftPixels=dpToPixels(marginLeft,activity);
        int marginTopPixels=dpToPixels(marginTop,activity);
        int marginRightPixels=dpToPixels(marginRight,activity);
        int marginBottomPixels=dpToPixels(marginBottom,activity);
        LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.WRAP_CONTENT,
                RadioGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(marginLeftPixels, marginTopPixels, marginRightPixels, marginBottomPixels);
        view.setLayoutParams(layoutParams);
    }

    public static void setPadding(View view, Activity activity, int paddingLeft, int paddingTop, int paddingRight, int paddingBottom){
        int paddingLeftPixels=dpToPixels(paddingLeft,activity);
        int paddingTopPixels=dpToPixels(paddingTop,activity);
        int paddingRightPixels=dpToPixels(paddingRight,activity);
        int paddingBottomPixels=dpToPixels(paddingBottom,activity);

        view.setPadding(paddingLeftPixels,paddingTopPixels,paddingRightPixels,paddingBottomPixels);
    }

    public static void setDialogButtonDisabled(AlertDialog alertDialog, Activity activity){
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(activity.getResources().getColor(R.color.textDisabledLightBackground));
    }

    public static void setDialogButtonEnabled(AlertDialog alertDialog, Activity activity){
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(activity.getResources().getColor(R.color.colorPrimary));
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