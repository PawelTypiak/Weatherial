package paweltypiak.matweather.infoActivityInitializing;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.Space;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import me.henrytao.smoothappbarlayout.SmoothAppBarLayout;
import paweltypiak.matweather.R;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

public class InfoActivityNestedScrollViewInitializer {

    public InfoActivityNestedScrollViewInitializer(Activity activity){
        setTopPaddingSpace(activity);
        initializeApplicationSourceSection(activity);
        initializeApiSection(activity);
        initializeLibrariesSection(activity);
    }

    private void setTopPaddingSpace(final Activity activity){
        final SmoothAppBarLayout appBarLayout
                =(SmoothAppBarLayout)activity.findViewById(R.id.info_activity_app_bar);
        ViewTreeObserver observer = appBarLayout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int appBarHeight=appBarLayout.getHeight();
                Space topPaddingSpace
                        =(Space)activity.findViewById(R.id.info_activity_nested_scrollview_top_padding_space);
                LinearLayout.LayoutParams topPaddingSpaceParams
                        = (LinearLayout.LayoutParams)topPaddingSpace.getLayoutParams();
                topPaddingSpaceParams.height = appBarHeight;
                topPaddingSpace.setLayoutParams(topPaddingSpaceParams);
            }
        });
    }

    private void initializeApplicationSourceSection(Activity activity){
        initializeRepositoryLayout(activity);
        initializeLicenseLayout(activity);
    }

    private void initializeRepositoryLayout(Activity activity){
        LinearLayout repositoryLayout
                =(LinearLayout)activity.findViewById(R.id.info_activity_application_source_repository_web_layout);
        setRepositoryLayoutOnClickListener(activity,repositoryLayout);
        setRepositoryLayoutOnLongClickListener(activity,repositoryLayout);
        setRepositoryLayoutWebIcon(activity);
    }

    private void setRepositoryLayoutOnClickListener(final Activity activity,
                                                    LinearLayout repositoryLayout){
        repositoryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeWebIntent(activity,activity.getString(R.string.info_activity_repository_address));
            }
        });
    }

    private void setRepositoryLayoutOnLongClickListener(final Activity activity,
                                                        LinearLayout repositoryApiLayout){
        repositoryApiLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                copyAddressToClipboard(activity,activity.getString(R.string.info_activity_repository_address));
                return true;
            }
        });
    }

    private void setRepositoryLayoutWebIcon(Activity activity){
        ImageView repositoryImageView=(ImageView)activity.findViewById(R.id.info_activity_application_source_repository_web_icon_image);
        setWebIcon(activity,repositoryImageView);
    }

    private void initializeLicenseLayout(Activity activity){
        setLicenseLayoutListeners(activity);
        setLicenseLayoutWebIcon(activity);
    }

    private void setLicenseLayoutListeners(Activity activity){
        LinearLayout licenseLayout
                =(LinearLayout)activity.findViewById(R.id.info_activity_application_source_license_web_layout);
        setLicenseLayoutOnClickListener(activity,licenseLayout);
        setLicenseLayoutOnLongClickListener(activity,licenseLayout);
    }

    private void setLicenseLayoutOnClickListener(final Activity activity,
                                                 LinearLayout licenseLayout){
        licenseLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeWebIntent(activity,activity.getString(R.string.info_activity_apache_license_address));
            }
        });
    }

    private void setLicenseLayoutOnLongClickListener(final Activity activity,
                                                     LinearLayout licenseLayout){
        licenseLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                copyAddressToClipboard(activity,activity.getString(R.string.info_activity_apache_license_address));
                return true;
            }
        });
    }

    private void setLicenseLayoutWebIcon(Activity activity){
        ImageView repositoryImageView
                =(ImageView)activity.findViewById(R.id.info_activity_application_source_license_web_icon_image);
        setWebIcon(activity,repositoryImageView);
    }

    private void initializeApiSection(Activity activity){
        initializeWeatherApiLayout(activity);
        initializeGeocodingApiLayout(activity);
    }

    private void initializeWeatherApiLayout(Activity activity){
        LinearLayout weatherApiLayout
                =(LinearLayout)activity.findViewById(R.id.info_activity_api_weather_layout);
        setWeatherApiLayoutOnClickListener(activity,weatherApiLayout);
        setWeatherApiLayoutOnLongClickListener(activity,weatherApiLayout);
        setWeathterApiLayoutWebIcon(activity);
    }

    private void setWeatherApiLayoutOnClickListener(final Activity activity,
                                                    LinearLayout weatherApiLayout){
        weatherApiLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeWebIntent(activity,activity.getString(R.string.info_activity_api_weather_address));
            }
        });
    }

    private void setWeatherApiLayoutOnLongClickListener(final Activity activity,
                                                        LinearLayout weatherApiLayout){
        weatherApiLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                copyAddressToClipboard(activity,activity.getString(R.string.info_activity_api_weather_address));
                return true;
            }
        });
    }

    private void setWeathterApiLayoutWebIcon(Activity activity){
        ImageView weatherApiImageView
                =(ImageView)activity.findViewById(R.id.info_activity_api_weather_web_icon_image);
        setWebIcon(activity,weatherApiImageView);
    }

    private void initializeGeocodingApiLayout(Activity activity){
        LinearLayout geocodingApiLayout
                =(LinearLayout)activity.findViewById(R.id.info_activity_api_geocoding_layout);
        setGeocodingApiLayoutOnClickListener(activity,geocodingApiLayout);
        setGeocodingApiLayoutOnLongClickListener(activity,geocodingApiLayout);
        setGeocodingApiLayoutWebIcon(activity);
    }

    private void setGeocodingApiLayoutOnClickListener(final Activity activity,
                                                      LinearLayout geocodingApiLayout){
        geocodingApiLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeWebIntent(activity,activity.getString(R.string.info_activity_api_geocoding_address));
            }
        });
    }

    private void setGeocodingApiLayoutOnLongClickListener(final Activity activity,
                                                          LinearLayout geocodingApiLayout){
        geocodingApiLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                copyAddressToClipboard(activity,activity.getString(R.string.info_activity_api_geocoding_address));
                return true;
            }
        });
    }

    private void setGeocodingApiLayoutWebIcon(Activity activity){
        ImageView geocodingApiImageView
                =(ImageView)activity.findViewById(R.id.info_activity_api_geocoding_web_icon_image);
        setWebIcon(activity,geocodingApiImageView);
    }

    private void setWebIcon(Activity activity,ImageView imageView){
        Drawable webIconDrawable
                = UsefulFunctions.getColoredDrawable(activity,R.drawable.author_icon,R.color.colorPrimary);
        imageView.setImageDrawable(webIconDrawable);
    }

    private void initializeLibrariesSection(Activity activity){
        initializeExternalLibrariesLicenseHiperlink(activity);
        initializeExternalLibrariesHiperlinks(activity);
    }

    private void initializeExternalLibrariesLicenseHiperlink(final Activity activity){
        TextView licenseTextView
                =(TextView)activity.findViewById(R.id.info_activity_external_libraries_license_text);
        setExternalLibrariesLicesceTextViewOnClickListener(activity,licenseTextView);
        setExternalLibrariesLicenseTextViewHighlight(activity,licenseTextView);
    }

    private void setExternalLibrariesLicesceTextViewOnClickListener(final Activity activity,
                                                                    TextView licenseTextView){
        licenseTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String webAddress=activity.getString(R.string.info_activity_apache_license_address);
                initializeWebIntent(activity,webAddress);
            }
        });
    }

    private void setExternalLibrariesLicenseTextViewHighlight(Activity activity,
                                                              TextView licenseTextView){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setRippleHighlight(activity,licenseTextView);
        }
        else{
            setStaticHighlight(activity,licenseTextView);
        }
    }

    private void setRippleHighlight(Activity activity,TextView textView){
        int[] attrs = new int[]{R.attr.selectableItemBackground};
        TypedArray typedArray = activity.obtainStyledAttributes(attrs);
        int backgroundResource = typedArray.getResourceId(0, 0);
        textView.setBackgroundResource(backgroundResource);
        typedArray.recycle();
    }

    private void setStaticHighlight(Activity activity,TextView textView){
        StateListDrawable states = new StateListDrawable();
        states.addState(
                new int[] {android.R.attr.state_pressed},
                ContextCompat.getDrawable(activity,R.drawable.rounded_button_background_pressed));
        states.addState(
                new int[] { },
                ContextCompat.getDrawable(activity,R.drawable.rounded_button_background_normal));
        textView.setBackground(states);
    }

    private void initializeExternalLibrariesHiperlinks(final Activity activity){
        setPicassoLibraryLayoutOnClickListener(activity);
        setSmoothAppBarLayoutLibraryLayoutOnClickListener(activity);
        setCollapsingToolbarLibraryLayoutOnClickListener(activity);
    }

    private void setPicassoLibraryLayoutOnClickListener(final Activity activity){
        LinearLayout picassoLibraryLayout
                =(LinearLayout)activity.findViewById(R.id.info_activity_external_libraries_picasso_library_layout);
        picassoLibraryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeWebIntent(activity,activity.getString(R.string.info_activity_picasso_library_address));
            }
        });
    }

    private void setSmoothAppBarLayoutLibraryLayoutOnClickListener(final Activity activity){
        LinearLayout smoothAppBarLayoutLibraryLayout
                =(LinearLayout)activity.findViewById(R.id.info_activity_external_libraries_smooth_app_bar_layout_library_layout);
        smoothAppBarLayoutLibraryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeWebIntent(activity,activity.getString(R.string.info_activity_smooth_app_bar_layout_library_address));
            }
        });
    }

    private void setCollapsingToolbarLibraryLayoutOnClickListener(final Activity activity){
        LinearLayout collapsingToolbarLibraryLayout
                =(LinearLayout)activity.findViewById(R.id.info_activity_external_libraries_multiline_collapsingtoolbar_library_layout);
        collapsingToolbarLibraryLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeWebIntent(activity,activity.getString(R.string.info_activity_multiline_collapsingtoolbar_library_address));
            }
        });
    }

    private void initializeWebIntent(Activity activity,String url){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        activity.startActivity(intent);
    }

    public void copyAddressToClipboard(Activity activity,String copiedText){
        ClipboardManager myClipboard;
        ClipData myClip;
        myClipboard = (ClipboardManager)activity.getSystemService(Context.CLIPBOARD_SERVICE);
        myClip = ClipData.newPlainText("text", copiedText);
        myClipboard.setPrimaryClip(myClip);
        Toast.makeText(
                activity,
                activity.getString(R.string.info_activity_clipboard_toast_message),
                Toast.LENGTH_SHORT).show();
    }
}
