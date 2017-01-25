package paweltypiak.matweather.mainActivityLayoutInitializing.WeatherLayoutInitializing;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import paweltypiak.matweather.R;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataFormatter;

public class WeatherDetailsLayoutInitializer {

    private ImageView directionImageView;
    private ImageView directionNorthImageView;
    private ImageView speedImageView;
    private ImageView humidityImageView;
    private ImageView pressureImageView;
    private TextView directionTextView;
    private TextView speedTextView;
    private TextView humidityTextView;
    private TextView pressureTextView;
    private View detailsSubdividerView;
    private LinearLayout sunPathLayout;
    private RelativeLayout sunPathTimeLayout;
    private TextView sunPathLeftTimeTextView;
    private TextView sunPathRightTimeTextView;
    private RelativeLayout sunPathArrowLayout;
    private ImageView sunPathLeftArrowImageView;
    private ImageView sunPathRightArrowImageView;
    private LinearLayout sunPathProgressBarLayout;
    private ImageView sunPathProgressBarLeftBoundaryImageView;
    private ImageView sunPathProgressBarRightBoundaryImageView;
    private View sunPathProgressBarProgressView;
    private View sunPathProgressBarBackgroundView;
    private RelativeLayout sunPathProgressIconLayout;
    private ImageView sunPathProgressIconImageView;
    private View detailsForecastDividerView;
    private String sunrise;
    private String sunset;
    private int sunPathTextViewWidth;
    private int sunPathLayoutWidth;

    public WeatherDetailsLayoutInitializer(Activity activity){
        findWeatherDetailsLayoutViews(activity);
        setWeatherDetailsLayoutSizes(activity);
    }

    private void findWeatherDetailsLayoutViews(Activity activity){
        sunPathLayout =(LinearLayout)activity.findViewById(R.id.weather_details_layout_sun_path_layout);
        sunPathTimeLayout=(RelativeLayout)activity.findViewById(R.id.weather_details_layout_sun_path_time_layout);
        sunPathLeftTimeTextView =(TextView)activity.findViewById(R.id.weather_details_layout_sun_path_left_time_text);
        sunPathRightTimeTextView =(TextView)activity.findViewById(R.id.weather_details_layout_sun_path_right_time_text);
        sunPathArrowLayout=(RelativeLayout)activity.findViewById(R.id.weather_details_layout_sun_path_arrow_layout);
        sunPathLeftArrowImageView =(ImageView)activity.findViewById(R.id.weather_details_layout_sun_path_left_arrow_image);
        sunPathRightArrowImageView =(ImageView)activity.findViewById(R.id.weather_details_layout_sun_path_right_arrow_image);
        sunPathProgressBarLayout=(LinearLayout)activity.findViewById(R.id.weather_details_layout_sun_path_progress_bar_layout);
        sunPathProgressBarLeftBoundaryImageView=(ImageView)activity.findViewById(R.id.weather_details_layout_sun_path_progress_bar_left_boundary_image);
        sunPathProgressBarRightBoundaryImageView=(ImageView)activity.findViewById(R.id.weather_details_layout_sun_path_progress_bar_right_boundary_image);
        sunPathProgressBarProgressView=activity.findViewById(R.id.weather_details_layout_sun_path_progress_bar_progress_view);
        sunPathProgressBarBackgroundView=activity.findViewById(R.id.weather_details_layout_sun_path_progress_bar_background_view);
        sunPathProgressIconLayout=(RelativeLayout) activity.findViewById(R.id.weather_details_layout_sun_path_progress_icon_layout);
        sunPathProgressIconImageView=(ImageView)activity.findViewById(R.id.weather_details_layout_sun_path_progress_icon_image);
        directionImageView =(ImageView)activity.findViewById(R.id.weather_details_layout_direction_image);
        directionNorthImageView =(ImageView)activity.findViewById(R.id.weather_details_layout_direction_north_icon_image);
        directionTextView =(TextView)activity.findViewById(R.id.weather_details_layout_direction_text);
        speedImageView =(ImageView)activity.findViewById(R.id.weather_details_layout_speed_image);
        speedTextView =(TextView)activity.findViewById(R.id.weather_details_layout_speed_text);
        humidityImageView =(ImageView)activity.findViewById(R.id.weather_details_layout_humidity_image);
        humidityTextView =(TextView)activity.findViewById(R.id.weather_details_layout_humidity_text);
        pressureImageView =(ImageView)activity.findViewById(R.id.weather_details_layout_pressure_image);
        pressureTextView=(TextView)activity.findViewById(R.id.weather_details_layout_pressure_text);
        detailsSubdividerView=activity.findViewById(R.id.weather_details_layout_subdivider);
        detailsForecastDividerView =activity.findViewById(R.id.weather_details_layout_bottom_divider);
    }

    private void setWeatherDetailsLayoutSizes(Activity activity){

        setAdditionalConditionsLayoutSizes(activity);
        //// TODO: move setInitialSunPathLayoutDimensions here
    }

    private void setAdditionalConditionsLayoutSizes(final Activity activity){
        final LinearLayout additionalConditionsLayout=(LinearLayout)activity.findViewById(R.id.weather_details_layout_additional_conditions_layout);
        ViewTreeObserver observer=additionalConditionsLayout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int iconSize=(int)activity.getResources().getDimension(R.dimen.additional_conditions_icons_size);
                TextView textView = (TextView)activity.findViewById(R.id.weather_details_layout_speed_text);
                int textSize=textView.getWidth();
                int iconEmptySpace=(textSize-iconSize)/2;
                LinearLayout directionLayout=(LinearLayout)activity.findViewById(R.id.weather_details_layout_additional_conditions_layout_direction_layout);
                LinearLayout.LayoutParams directionLayoutParams=(LinearLayout.LayoutParams)directionLayout.getLayoutParams();
                directionLayoutParams.rightMargin=iconEmptySpace;
                Log.d("space", "onGlobalLayout: "+iconEmptySpace);
                directionLayout.setLayoutParams(directionLayoutParams);
                LinearLayout humidityLayout=(LinearLayout)activity.findViewById(R.id.weather_details_layout_humidity_layout);
                LinearLayout.LayoutParams humidityLayoutParams=(LinearLayout.LayoutParams)humidityLayout.getLayoutParams();
                humidityLayoutParams.leftMargin=iconEmptySpace;
                humidityLayout.setLayoutParams(humidityLayoutParams);
                additionalConditionsLayout.getViewTreeObserver().removeOnGlobalLayoutListener(
                        this);
            }
        });
    }

    public void updateWeatherDetailsLayoutData(Activity activity,WeatherDataFormatter weatherDataFormatter){
        setAdditionalConditionsData(weatherDataFormatter);
        setSunPathData(activity,weatherDataFormatter);
    }

    private void setAdditionalConditionsData(WeatherDataFormatter weatherDataFormatter){
        directionImageView.setRotation(Float.parseFloat(weatherDataFormatter.getDirection()));
        directionTextView.setText(weatherDataFormatter.getDirectionName());
        speedTextView.setText(weatherDataFormatter.getSpeed());
        humidityTextView.setText(weatherDataFormatter.getHumidity());
        pressureTextView.setText(weatherDataFormatter.getPressure());

    }

    private void setSunPathData(Activity activity,WeatherDataFormatter weatherDataFormatter){
        sunrise=weatherDataFormatter.getSunrise();
        sunset=weatherDataFormatter.getSunset();
        long currentDiffMinutes=weatherDataFormatter.getCurrentDiffMinutes();
        long sunsetSunriseDiffMinutes=weatherDataFormatter.getSunsetSunriseDiffMinutes();
        setInitialSunPathLayoutDimensions(activity, currentDiffMinutes,sunsetSunriseDiffMinutes);
    }

    public void updateWeatherDetailsLayoutTheme(Activity activity,WeatherLayoutThemeColorsUpdater themeColorsUpdater, boolean isDay){
        updateAdditionalConditionsLayoutTheme(activity,themeColorsUpdater);
        updateSunPathLayoutTheme(activity,themeColorsUpdater,isDay);
    }

    private void updateAdditionalConditionsLayoutTheme(Activity activity,WeatherLayoutThemeColorsUpdater themeColorsUpdater){
        int backgroundColor=themeColorsUpdater.getBackgroundColor();
        int iconColor=themeColorsUpdater.getIconColor();
        int textPrimaryColor=themeColorsUpdater.getTextPrimaryColor();
        int dividerColor=themeColorsUpdater.getDividerColor();

        Drawable directionIconDrawable= UsefulFunctions.getColoredDrawable(activity,R.drawable.direction_icon,backgroundColor);
        directionImageView.setImageDrawable(directionIconDrawable);
        Drawable northLetterIconDrawable=UsefulFunctions.getColoredDrawable(activity,R.drawable.north_letter_icon,iconColor);
        directionNorthImageView.setImageDrawable(northLetterIconDrawable);
        directionTextView.setTextColor(textPrimaryColor);
        Drawable speedIconDrawable=UsefulFunctions.getColoredDrawable(activity,R.drawable.speed_icon,iconColor);
        speedImageView.setImageDrawable(speedIconDrawable);
        speedTextView.setTextColor(textPrimaryColor);
        Drawable humidityIconDrawable=UsefulFunctions.getColoredDrawable(activity,R.drawable.humidity_icon,iconColor);
        humidityImageView.setImageDrawable(humidityIconDrawable);
        humidityTextView.setTextColor(textPrimaryColor);
        Drawable pressureIconDrawable=UsefulFunctions.getColoredDrawable(activity,R.drawable.pressure_icon,iconColor);
        pressureImageView.setImageDrawable(pressureIconDrawable);
        pressureTextView.setTextColor(textPrimaryColor);
        detailsSubdividerView.setBackgroundColor(dividerColor);
        detailsForecastDividerView.setBackgroundColor(dividerColor);
    }

    private void updateSunPathLayoutTheme(Activity activity,WeatherLayoutThemeColorsUpdater themeColorsUpdater,boolean isDay){
        setSunPathLayoutChildOrder(activity,isDay);
        if(isDay ==true){
            sunPathLeftTimeTextView.setText(sunrise);
            sunPathRightTimeTextView.setText(sunset);
        }
        else {
            sunPathLeftTimeTextView.setText(sunset);
            sunPathRightTimeTextView.setText(sunrise);

        }
        int iconColor=themeColorsUpdater.getIconColor();
        int textPrimaryColor=themeColorsUpdater.getTextPrimaryColor();
        int dividerColor=themeColorsUpdater.getDividerColor();
        Drawable fullArrowIconDrawable = UsefulFunctions.getColoredDrawable(activity,R.drawable.full_arrow_icon,iconColor);
        sunPathLeftArrowImageView.setImageDrawable(fullArrowIconDrawable);
        sunPathRightArrowImageView.setImageDrawable(fullArrowIconDrawable);
        sunPathLeftTimeTextView.setTextColor(textPrimaryColor);
        sunPathRightTimeTextView.setTextColor(textPrimaryColor);
        Drawable sunIconDrawable = UsefulFunctions.getColoredDrawable(activity,R.drawable.sun_icon,textPrimaryColor);
        sunPathProgressIconImageView.setImageDrawable(sunIconDrawable);
        Drawable boundaryDrawable = UsefulFunctions.getColoredDrawable(activity,R.drawable.sun_path_progress_bar_boundary,iconColor);
        sunPathProgressBarLeftBoundaryImageView.setImageDrawable(boundaryDrawable);
        sunPathProgressBarRightBoundaryImageView.setImageDrawable(boundaryDrawable);
        sunPathProgressBarProgressView.setBackgroundColor(iconColor);
        sunPathProgressBarBackgroundView.setBackgroundColor(dividerColor);
    }

    private void setSunPathLayoutChildOrder(Activity activity, boolean isDay){
        addSunPathLayoutViews(isDay);
        rotateSunPathArrowLayout(isDay);
        setVerticalMargins(activity,isDay);
    }

    private void addSunPathLayoutViews(boolean isDay){
        sunPathLayout.removeAllViews();
        if(isDay==true){
            sunPathLayout.addView(sunPathProgressIconLayout);
            sunPathLayout.addView(sunPathProgressBarLayout);
            sunPathLayout.addView(sunPathArrowLayout);
            sunPathLayout.addView(sunPathTimeLayout);
        }
        else{
            sunPathLayout.addView(sunPathTimeLayout);
            sunPathLayout.addView(sunPathArrowLayout);
            sunPathLayout.addView(sunPathProgressBarLayout);
            sunPathLayout.addView(sunPathProgressIconLayout);
        }
    }

    private void rotateSunPathArrowLayout(boolean isDay){
        if(isDay==true){
            sunPathArrowLayout.setRotationY(0);
        }
        else{
            sunPathArrowLayout.setRotationY(180);
        }
    }

    private void setVerticalMargins(Activity activity, boolean isDay){
        LinearLayout.LayoutParams progressBarLayoutParams= (LinearLayout.LayoutParams)sunPathProgressBarLayout.getLayoutParams();
        LinearLayout.LayoutParams arrowLayoutParams= (LinearLayout.LayoutParams)sunPathArrowLayout.getLayoutParams();
        LinearLayout.LayoutParams timeLayoutParams=(LinearLayout.LayoutParams)sunPathTimeLayout.getLayoutParams();
        int progressBarLayoutProgressIconLayoutMargin=(int)activity.getResources().getDimension(R.dimen.sun_path_progress_bar_progress_icon_margin);
        int arrowLayoutProgressBarLayoutMargin=(int)activity.getResources().getDimension(R.dimen.sun_path_arrow_progress_bar_margin);
        int timeLayoutArrowLayoutMargin=(int)activity.getResources().getDimension(R.dimen.sun_path_time_arrow_margin);
        if(isDay==true){
            progressBarLayoutParams.setMargins(0,progressBarLayoutProgressIconLayoutMargin,0,0);
            arrowLayoutParams.setMargins(0,arrowLayoutProgressBarLayoutMargin,0,0);
            timeLayoutParams.setMargins(0,timeLayoutArrowLayoutMargin,0,0);
        }
        else{
            progressBarLayoutParams.setMargins(0,0,0,progressBarLayoutProgressIconLayoutMargin);
            arrowLayoutParams.setMargins(0,0,0,arrowLayoutProgressBarLayoutMargin);
            timeLayoutParams.setMargins(0,0,0,timeLayoutArrowLayoutMargin);
        }
        sunPathProgressBarLayout.setLayoutParams(progressBarLayoutParams);
        sunPathArrowLayout.setLayoutParams(arrowLayoutParams);
        sunPathTimeLayout.setLayoutParams(timeLayoutParams);
    }

    private void setInitialSunPathLayoutDimensions(final Activity activity, final long currentDiffMinutes, final long sunsetSunriseDiffMinutes){
        //set layout for sun path
        ViewTreeObserver treeObserver = sunPathLayout.getViewTreeObserver();
        treeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                sunPathLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                sunPathTextViewWidth=sunPathLeftTimeTextView.getWidth();
                sunPathLayoutWidth=sunPathLayout.getWidth();
                setSunPathLayoutDimensions(activity);
                updateSunPathProgress(activity,currentDiffMinutes,sunsetSunriseDiffMinutes);
            }
        });
    }

    private void setSunPathLayoutDimensions(Activity activity){
        setSunPathArrowLayoutDimensions(activity);
        setSunPathProgressBarDimensions(activity);
        setSunPathImageLayoutDimensions(activity);
    }

    private void setSunPathArrowLayoutDimensions(Activity activity){
        int arrowImageViewSize=(int)activity.getResources().getDimension(R.dimen.sun_path_arrow_icon_size);
        int arrowLayoutMargin=(sunPathTextViewWidth-arrowImageViewSize)/2;
        LinearLayout.LayoutParams arrowLayoutParams=(LinearLayout.LayoutParams)sunPathArrowLayout.getLayoutParams();
        arrowLayoutParams.leftMargin=arrowLayoutMargin;
        arrowLayoutParams.rightMargin=arrowLayoutMargin;
        sunPathArrowLayout.setLayoutParams(arrowLayoutParams);
    }

    private void setSunPathProgressBarDimensions(Activity activity){
        int progressBoundarySize=(int)activity.getResources().getDimension(R.dimen.sun_path_progress_bar_boundary_size);
        int progressBarLayoutMargin=(sunPathTextViewWidth-progressBoundarySize)/2;
        LinearLayout.LayoutParams progressLayoutParams=(LinearLayout.LayoutParams)sunPathProgressBarLayout.getLayoutParams();
        progressLayoutParams.leftMargin=progressBarLayoutMargin;
        progressLayoutParams.rightMargin=progressBarLayoutMargin;
        sunPathProgressBarLayout.setLayoutParams(progressLayoutParams);
    }

    private void setSunPathImageLayoutDimensions(Activity activity){
        int progressIconSize=(int)activity.getResources().getDimension(R.dimen.sun_path_progress_icon_size);
        int progressIconLayoutMargin=(sunPathTextViewWidth-progressIconSize)/2;
        LinearLayout.LayoutParams progressIconLayoutParams= (LinearLayout.LayoutParams)sunPathProgressIconLayout.getLayoutParams();
        progressIconLayoutParams.leftMargin=progressIconLayoutMargin;
        progressIconLayoutParams.rightMargin=progressIconLayoutMargin;
        sunPathProgressIconLayout.setLayoutParams(progressIconLayoutParams);
    }

    public void updateSunPathProgress(Activity activity, long currentDiffMinutes, long sunsetSunriseDiffMinutes){
        int progressBoundarySize=(int)activity.getResources().getDimension(R.dimen.sun_path_progress_bar_boundary_size);
        int sunPathLength= sunPathLayoutWidth-sunPathTextViewWidth;
        int iconProgress=(int)(currentDiffMinutes*sunPathLength/sunsetSunriseDiffMinutes);
        int barProgress=iconProgress-progressBoundarySize/2;
        LinearLayout.LayoutParams progressViewParams=(LinearLayout.LayoutParams)sunPathProgressBarProgressView.getLayoutParams();
        progressViewParams.width=barProgress;
        sunPathProgressBarProgressView.setLayoutParams(progressViewParams);
        sunPathProgressIconImageView.setTranslationX(iconProgress);
    }
}
