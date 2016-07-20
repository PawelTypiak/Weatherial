package paweltypiak.matweather;

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
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Transformation;

public class UsefulFunctions {
    private static boolean isFirst;

    public static boolean getIsFirst() {
        return isFirst;
    }

    public static void setIsFirst(boolean bool) {
        isFirst = bool;
    }

    private static int[] units={0,0,0,0,0};

    public static int[] getUnits() {
        return units;
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

    public static void setLayoutFocusable(Activity activity, LinearLayout layout){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            TypedValue outValue = new TypedValue();
            activity.getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            layout.setBackgroundResource(outValue.resourceId);
        }
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
        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
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

    public static void initializeUiThread(final Activity activity, final Runnable runnable) {
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
        uiThread.start();
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
