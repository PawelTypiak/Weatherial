package paweltypiak.matweather.mainActivityLayoutInitializing.WeatherLayoutInitializing;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import paweltypiak.matweather.R;
import paweltypiak.matweather.customViews.LockableSmoothNestedScrollView;
import paweltypiak.matweather.mainActivityLayoutInitializing.MainActivityLayoutInitializer;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataFormatter;

public class WeatherBasicInfoLayoutInitializer {

    private CoordinatorLayout mainLayout;
    private LinearLayout weatherInnerLayout;
    private TextView onRefreshMessageTextView;
    private TextView conditionTextView;
    private ImageView conditionImageView;
    private TextView temperatureTextView;
    private TextView temperatureUnitTextView;
    private TextView temperatureDagreeSignTextView;
    private ImageView seeMoreImageView;
    private View currentDetailsDividerView;

    public WeatherBasicInfoLayoutInitializer(Activity activity,
                                             MainActivityLayoutInitializer mainActivityLayoutInitializer){
        findLayoutViews(activity);
        rotateSeeMoreImageView();
        setWeatherBasicInfoLayoutSizes(activity,mainActivityLayoutInitializer);
       // initializeSeeMoreArrowAnimation(activity);
    }

    private void findLayoutViews(Activity activity){
        mainLayout =(CoordinatorLayout) activity.findViewById(R.id.coordinator_layout);
        weatherInnerLayout=(LinearLayout)activity.findViewById(R.id.weather_inner_layout);
        onRefreshMessageTextView=(TextView)activity.findViewById(R.id.main_content_layout_on_refresh_message_text);
        conditionTextView =(TextView)activity.findViewById(R.id.weather_basic_info_layout_conditions_text);
        conditionImageView =(ImageView)activity.findViewById(R.id.weather_basic_info_layout_conditions_image);
        temperatureTextView =(TextView)activity.findViewById(R.id.weather_basic_info_layout_temperature_text);
        temperatureUnitTextView=(TextView)activity.findViewById(R.id.weather_basic_info_temperature_unit_text);
        temperatureDagreeSignTextView=(TextView)activity.findViewById(R.id.weather_basic_info_layout_temperature_dagree_sign_text);
        currentDetailsDividerView =activity.findViewById(R.id.weather_basic_info_layout_bottom_divider_divider);
        seeMoreImageView=(ImageView)activity.findViewById(R.id.weather_basic_info_layout_see_more_image);
    }

    private void rotateSeeMoreImageView(){
        seeMoreImageView.setRotation(180);
    }

    private void setWeatherBasicInfoLayoutSizes(Activity activity,
                                                MainActivityLayoutInitializer mainActivityLayoutInitializer){
        int screenHeight=UsefulFunctions.getScreenHeight(activity);
        int statusBarHeight=UsefulFunctions.getStatusBarHeight(activity);
        int toolbarExpandedHeight= mainActivityLayoutInitializer.
                getAppBarLayoutInitializer().
                getAppBarLayoutDimensionsSetter().
                getToolbarExpandedHeight();
        int basicWeatherLayoutHeight=screenHeight-toolbarExpandedHeight-statusBarHeight;
        LinearLayout basicWeatherLayout=(LinearLayout)activity.findViewById(R.id.weather_basic_info_layout);
        LinearLayout.LayoutParams basicWeatherLayoutParams=(LinearLayout.LayoutParams)basicWeatherLayout.getLayoutParams();
        basicWeatherLayoutParams.height=basicWeatherLayoutHeight;
        basicWeatherLayout.setLayoutParams(basicWeatherLayoutParams);
        setCurrentTemperatureTextViewHeight(activity,basicWeatherLayoutHeight);
    }

    private void setCurrentTemperatureTextViewHeight(final Activity activity,
                                                     final int basicWeatherLayoutHeight){
        final LinearLayout basicWeatherLayout=(LinearLayout)activity.findViewById(R.id.weather_basic_info_layout);
        ViewTreeObserver observer = basicWeatherLayout.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                RelativeLayout currentTemperatureLayout=(RelativeLayout)activity.findViewById(R.id.weather_basic_info_layout_temperature_layout);
                int currentTemperatureLayoutVerticalTranslation=(int)(basicWeatherLayoutHeight*0.02f);
                currentTemperatureLayout.setTranslationY(-currentTemperatureLayoutVerticalTranslation);
                int currentTemperatureTextViewHeight=currentTemperatureLayout.getHeight();
                TextView currentTemperatureTextView=(TextView)activity.findViewById(R.id.weather_basic_info_layout_temperature_text);
                currentTemperatureTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, currentTemperatureTextViewHeight);
                TextView currentTemperatureDagreeSignTextView=(TextView)activity.findViewById(R.id.weather_basic_info_layout_temperature_dagree_sign_text);
                currentTemperatureDagreeSignTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, currentTemperatureTextViewHeight);
                TextView currentTemperatureUnitTextView=(TextView)activity.findViewById(R.id.weather_basic_info_temperature_unit_text);
                int currentTemperatureUnitTextViewHeight=(int)(currentTemperatureTextViewHeight/2.5f);
                currentTemperatureUnitTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, currentTemperatureUnitTextViewHeight);
                int currentTemperatureUnitTextViewTranslationY=(int)(currentTemperatureTextViewHeight/35);
                currentTemperatureUnitTextView.setTranslationY(currentTemperatureUnitTextViewTranslationY);
                basicWeatherLayout.getViewTreeObserver().removeOnGlobalLayoutListener(
                        this);
            }
        });
    }

    private void initializeSeeMoreArrowAnimation(final Activity activity){
        LockableSmoothNestedScrollView nestedScrollView
                =(LockableSmoothNestedScrollView)activity.findViewById(R.id.nested_scroll_view);
        nestedScrollView.addOnScrollListener(new android.support.v4.widget.NestedScrollView.OnScrollChangeListener() {
            boolean isTransparent=true;
            int normalMargin=(int)activity.getResources().getDimension(R.dimen.activity_normal_margin);
            @Override
            public void onScrollChange(android.support.v4.widget.NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY!=oldScrollY){
                    if(scrollY>normalMargin && isTransparent==false ){
                        isTransparent=true;
                        UsefulFunctions.crossFade(activity,null,seeMoreImageView,1);
                    }
                    else if(scrollY<=normalMargin && isTransparent==true){
                        isTransparent=false;
                        UsefulFunctions.crossFade(activity,seeMoreImageView,null,1);
                    }
                }
            }
        });
    }

    public void updateWeatherBasicInfoLayoutData(Activity activity,
                                                 WeatherDataFormatter weatherDataFormatter,
                                                 WeatherLayoutInitializer weatherLayoutInitializer){
        setConditionData(activity,weatherDataFormatter,weatherLayoutInitializer);
        setTemperatureData(weatherDataFormatter);
    }

    private void setTemperatureData(WeatherDataFormatter weatherDataFormatter){
        String temperature=weatherDataFormatter.getTemperature();
        temperatureTextView.setText(temperature);
        String temperatureUnit=weatherDataFormatter.getTemperatureUnit();
        temperatureUnitTextView.setText(temperatureUnit);
    }

    private void setConditionData(Activity activity,
                                  WeatherDataFormatter weatherDataFormatter,
                                  final WeatherLayoutInitializer weatherLayoutInitializer){
        int code=weatherDataFormatter.getCode();
        int conditionStringId=activity.getResources().getIdentifier("condition_" + code, "string", activity.getPackageName());
        int conditionDrawableId=activity.getResources().getIdentifier("drawable/conditions_icon_" + code, null, activity.getPackageName());
        conditionTextView.setText(conditionStringId);
        Picasso.with(activity.getApplicationContext()).load(conditionDrawableId).into(conditionImageView, new Callback() {
            @Override
            public void onSuccess() {
                //fadeInWeatherLayout();
                fadeInWeatherLayout(weatherLayoutInitializer);
                Log.d("success", "onSuccess: ");
            }

            @Override
            public void onError() {

            }
        });
    }

    private void fadeInWeatherLayout(final WeatherLayoutInitializer weatherLayoutInitializer){
        long transitionTime=200;
        weatherInnerLayout.setAlpha(0f);
        weatherInnerLayout.setVisibility(View.VISIBLE);
        weatherInnerLayout.animate()
                .alpha(1f)
                .setDuration(transitionTime)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animator) {
                        weatherLayoutInitializer.
                        getSwipeRefreshLayoutInitializer().
                                getPullListenersInitializer().
                                setNestedScrollViewScrollingEnabled();
                    }
                });
    }

    public void updateWeatherBasicInfoLayoutTheme(Activity activity,
                                                  WeatherLayoutThemeColorsUpdater themeColorsUpdater){
        int backgroundColor=themeColorsUpdater.getBackgroundColor();
        mainLayout.setBackgroundColor(backgroundColor);
        int textPrimaryColor=themeColorsUpdater.getTextPrimaryColor();
        onRefreshMessageTextView.setTextColor(textPrimaryColor);
        conditionTextView.setTextColor(textPrimaryColor);
        temperatureTextView.setTextColor(textPrimaryColor);
        temperatureDagreeSignTextView.setTextColor(textPrimaryColor);
        int textDisabledColor=themeColorsUpdater.getTextDisabledColor();
        temperatureUnitTextView.setTextColor(textDisabledColor);
        int dividerColor=themeColorsUpdater.getDividerColor();
        Drawable arrowIconDrawable = UsefulFunctions.getColoredDrawable(activity,R.drawable.empty_arrow_icon,dividerColor);
        seeMoreImageView.setImageDrawable(arrowIconDrawable);
        currentDetailsDividerView.setBackgroundColor(dividerColor);
    }
}
