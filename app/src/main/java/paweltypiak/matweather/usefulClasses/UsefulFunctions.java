package paweltypiak.matweather.usefulClasses;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.Spanned;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.squareup.picasso.Transformation;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Locale;

import paweltypiak.matweather.R;

public class UsefulFunctions {

    //// TODO: split to different classes

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

    //// TODO: delete
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

    public static void setTaskDescription(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String title = activity.getString(R.string.app_name);
            Bitmap icon = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.app_icon);
            int color = ContextCompat.getColor(activity,R.color.colorPrimaryDark);
            try {
                Class<?> taskDescriptionClass = Class.forName("android.app.ActivityManager$TaskDescription");
                Constructor<?> taskDescriptionConstructor = taskDescriptionClass.getConstructor(String.class, Bitmap.class, int.class);
                Object taskDescription = taskDescriptionConstructor.newInstance(title, icon, color);
                Method method = ((Object) activity).getClass().getMethod("setTaskDescription", taskDescriptionClass);
                method.invoke(activity, taskDescription);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
