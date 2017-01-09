package paweltypiak.matweather.usefulClasses;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.Html;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Transformation;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import java.util.Locale;

import paweltypiak.matweather.MainActivity;
import paweltypiak.matweather.R;
import paweltypiak.matweather.layoutInitializing.appBarInitializing.appBarOnOffsetChangeListenerAdding.ToolbarTitleClickableViewSizeUpdater;
import paweltypiak.matweather.layoutInitializing.LayoutUpdating.OnWeatherDataChangeLayoutUpdater;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataParser;

public class UsefulFunctions {
    //information about first weather downloading, after application launch

    // TODO: move to dataFormatter

    public static String[] getCurrentLocationAddress(){
        String[] location=new String[2];
        location[0]= OnWeatherDataChangeLayoutUpdater.getCurrentDataFormatter().getCity();
        location[1]= OnWeatherDataChangeLayoutUpdater.getCurrentDataFormatter().getRegion()+", "+ OnWeatherDataChangeLayoutUpdater.getCurrentDataFormatter().getCountry();
        return location;
    }

    public static String[] getCurrentLocationCoordinates(){
        String[] coordinates=new String[2];
        coordinates[0]= Double.toString(OnWeatherDataChangeLayoutUpdater.getCurrentDataFormatter().getLatitude());
        coordinates[1]=Double.toString(OnWeatherDataChangeLayoutUpdater.getCurrentDataFormatter().getLongitude());
        return coordinates;
    }

    //// TODO: move to AppBarLayoutDataInitializer

    public static String[] getAppBarStrings(Activity activity){
        //get location name from AppBar
        String[] location=new String[2];
        CollapsingToolbarLayout collapsingToolbarLayout=(CollapsingToolbarLayout)activity.findViewById(R.id.collapsing_toolbar_layout);
        TextView secondaryLocationTextView=(TextView)activity.findViewById(R.id.toolbar_layout_subtitle_text);
        location[0]=collapsingToolbarLayout.getTitle().toString();
        location[1]=secondaryLocationTextView.getText().toString();
        return location;
    }

    public static void setAppBarStrings(Activity activity, String toolbarTitle, String toolbarSubtitle){
        //set custom location name in AppBar
        CollapsingToolbarLayout collapsingToolbarLayout=(CollapsingToolbarLayout)activity.findViewById(R.id.collapsing_toolbar_layout);
        String formattedToolbarTitle=getFormattedString(toolbarTitle);
        collapsingToolbarLayout.setTitle(formattedToolbarTitle);
        TextView secondaryLocationTextView=(TextView)activity.findViewById(R.id.toolbar_layout_subtitle_text);
        secondaryLocationTextView.setText(toolbarSubtitle);
        setViewGone(secondaryLocationTextView);
        if(!toolbarSubtitle.equals("")) setViewVisible(secondaryLocationTextView);
        //((MainActivity) activity).getOnAppBarStringsChangeListener().onAppBarStringsChanged(activity,toolbarTitle);
        ToolbarTitleClickableViewSizeUpdater.getOnAppBarStringsChangeListener().onAppBarStringsChanged(activity,formattedToolbarTitle);
    }

    public interface OnAppBarStringsChangeListener {
        void onAppBarStringsChanged(Activity activity,String toolbarTitle);
    }

    // TODO: usefulFunctions

    public static void initializeWebIntent(Context context, String url){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }

    public static void initializeMapsIntent(Context context,double latitude,double longitude , String label){
        String uriBegin = "geo:" + latitude + "," + longitude;
        String query = latitude + "," + longitude + "(" + label + ")";
        String encodedQuery = Uri.encode(query);
        String uriString = uriBegin + "?q=" + encodedQuery + "&z=16";
        Uri uri = Uri.parse(uriString);
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
        context.startActivity(intent);
    }

    public static void initializeEmailIntent(Context context, String address, String subject, String body, AlertDialog dialog){
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setType("text/plain");
        if(subject!=null) intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        if(body!=null)intent.putExtra(Intent.EXTRA_TEXT, body);
        intent.setData(Uri.parse("mailto:"+address));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            dialog.show();
        }
    }

    public static void copyToClipboard(Context context, String text){
        ClipboardManager myClipboard;
        ClipData myClip;
        myClipboard = (ClipboardManager)context.getSystemService(Context.CLIPBOARD_SERVICE);
        myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);
        Toast.makeText(context.getApplicationContext(), context.getString(R.string.clipboard_toast_message),Toast.LENGTH_SHORT).show();
    }

    public static StringBuilder buildStringFromStringArray(String[] stringArray){
        //build string from array of strings  for SharedPreferences
        int numberOfLocations=stringArray.length;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < numberOfLocations; i++) {
            stringBuilder.append(stringArray[i]).append("|");
        }
        return stringBuilder;
    }

    public static String getFormattedString(String string){
        //cut empty characters before and after string and set upper case
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

    // TODO: move to DialogInitializer

    public static void customizeEditText(final Activity activity,final AlertDialog dialog, final EditText editText){
        //custom edit text in dialogs with string validation and edition
        final String hint=editText.getHint().toString();
        if(editText.getText().length()!=0) {
            editText.getBackground().setColorFilter(ContextCompat.getColor(activity,R.color.transparent), PorterDuff.Mode.SRC_ATOP);
            editText.setHint("");
        }
        else {
            setDialogButtonDisabled(dialog,activity);
        }
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            String previusEditTextString;
            @Override
            public void onFocusChange(View view, boolean b) {
                if(editText.isFocused()) {
                    editText.getBackground().setColorFilter(ContextCompat.getColor(activity,R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP);
                    previusEditTextString=editText.getText().toString();
                    showKeyboard(activity);
                }
                else {
                    String editTextString=editText.getText().toString();
                    editTextString=getFormattedString(editTextString);
                    if(editTextString.length()==0) {
                        editText.getBackground().setColorFilter(ContextCompat.getColor(activity,R.color.hintLightBackgroud), PorterDuff.Mode.SRC_ATOP);
                    }
                    else{
                        editText.getBackground().setColorFilter(ContextCompat.getColor(activity,R.color.transparent), PorterDuff.Mode.SRC_ATOP);
                        editText.setText(editTextString);
                    }
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
                if(editText.getText().length()==0) {
                    editText.setHint(hint);
                    setDialogButtonDisabled(dialog,activity);
                }
                else {
                    editText.setHint("");
                    setDialogButtonEnabled(dialog,activity);
                }
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

    // TODO: usefulFunctions

    public static void showKeyboard(Activity activity){
        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);

    }
    public static void hideKeyboard(final Activity activity, final EditText editText) {
        if(editText==null)activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        else{
            InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    // TODO: delete

    public static void setViewVisible(View view){
        view.setVisibility(View.VISIBLE);
    }

    public static void setViewInvisible(View view){
        view.setVisibility(View.INVISIBLE);
    }

    public static void setViewGone(View view){
        view.setVisibility(View.GONE);
    }

    // TODO: usefulFunctions

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

    // TODO: move somewhere

    public static void setRadioButtonMargins(View view, Activity activity, int marginLeft, int marginTop, int marginRight, int marginBottom){
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

    // TODO: move to dialogInitializer

    public static void setDialogButtonDisabled(AlertDialog alertDialog, Context context){
        //disable alert dialog positive button
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
    }

    public static void setDialogButtonEnabled(AlertDialog alertDialog, Context context){
        //enable alert dialog positive button
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context,R.color.colorPrimary));
    }

    // TODO: usefulFunctions

    @SuppressWarnings("deprecation")
    public static void setLocale(Context context,int type){
        String language=null;
        if(type==0) language="en";
        else if(type==1) language="pl";
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale);

        } else {
            config.locale = locale;
        }
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }

    @SuppressWarnings("deprecation")
    public static int getLocale(Context context){
        String languageString;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            languageString=context.getResources().getConfiguration().getLocales().get(0).toString();

        } else {
            languageString=context.getResources().getConfiguration().locale.toString();
        }
        languageString=languageString.substring(0,2);
        Log.d("languagestring", languageString);
        int language;
        if(languageString.equals("pl")) language=1;
        else language=0;
        return language;
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(source);
        }
    }

    public class setDrawableColor implements Transformation {
        //change drawable color in Picasso
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

    public static Drawable getColoredDrawable(Activity activity, int drawableId, int color){
        Drawable drawable = ContextCompat.getDrawable(activity, drawableId).mutate();
        drawable.setColorFilter(new
                PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
        return drawable;
    }

    public static int getScreenHeight(Activity activity){
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenHeight = metrics.heightPixels;
        return screenHeight;
    }

    public static int getScreenWidth(Activity activity){
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;
        return screenWidth;
    }

    public static int getStatusBarHeight(Activity activity){
        int statusBarHeight = 0;
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = activity.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    public static int getTextViewHeight(Activity activity,
                                        String text,
                                        Typeface typeface,
                                        float textSize,
                                        int paddingLeft,
                                        int paddingTop,
                                        int paddingRight,
                                        int paddingBottom) {
        int textViewHeight=getTextViewSize(
                activity,
                text,
                typeface,
                textSize,
                paddingLeft,
                paddingTop,
                paddingRight,
                paddingBottom)[1];
        return textViewHeight;
    }
    public static int getTextViewWidth(Activity activity,
                                       String text,
                                       Typeface typeface,
                                       float textSize,
                                       int paddingLeft,
                                       int paddingTop,
                                       int paddingRight,
                                       int paddingBottom) {
        int textViewWidth=getTextViewSize(
                activity,
                text,
                typeface,
                textSize,
                paddingLeft,
                paddingTop,
                paddingRight,
                paddingBottom)[0];
        return textViewWidth;
    }

    public static int[] getTextViewSize(Activity activity,
                                        String text,
                                        Typeface typeface,
                                        float textSize,
                                        int paddingLeft,
                                        int paddingTop,
                                        int paddingRight,
                                        int paddingBottom){
        TextView textView = new TextView(activity);
        textView.setPadding(paddingLeft,paddingTop,paddingRight,paddingBottom);
        textView.setTypeface(typeface);
        textView.setText(text, TextView.BufferType.SPANNABLE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        int deviceWidth = displayMetrics.widthPixels;
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.AT_MOST);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        textView.measure(widthMeasureSpec, heightMeasureSpec);
        int [] textViewSize={textView.getMeasuredWidth(),textView.getMeasuredHeight()};
        return textViewSize;
    }

    public static void crossFade(Context context, final View viewIn, final View viewOut, int animationDurationType) {
        int animationDuration=0;
        if(animationDurationType==0){
            animationDuration = context.getResources().getInteger(android.R.integer.config_shortAnimTime);
        }
        else if(animationDurationType==1){
            animationDuration = context.getResources().getInteger(android.R.integer.config_mediumAnimTime);
        }
        else if(animationDurationType==2){
            animationDuration = context.getResources().getInteger(android.R.integer.config_longAnimTime);
        }

        if(viewIn!=null){
            viewIn.setAlpha(0f);
            viewIn.setVisibility(View.VISIBLE);
            viewIn.animate()
                    .alpha(1f)
                    .setDuration(animationDuration)
                    .setListener(null);
        }
        if(viewOut!=null){
            viewOut.setAlpha(1f);
            viewOut.animate()
                    .alpha(0f)
                    .setDuration(animationDuration)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            viewOut.setVisibility(View.INVISIBLE);
                            // TODO: add types of visibility
                        }
                    });
        }
    }
}
