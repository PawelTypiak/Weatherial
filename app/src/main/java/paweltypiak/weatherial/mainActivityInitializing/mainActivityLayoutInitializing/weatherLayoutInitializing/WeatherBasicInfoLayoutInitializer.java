package paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.weatherLayoutInitializing;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.design.widget.CoordinatorLayout;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.customViews.LockableSmoothNestedScrollView;
import paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.MainActivityLayoutInitializer;
import paweltypiak.weatherial.usefulClasses.UsefulFunctions;
import paweltypiak.weatherial.dataDownloading.weatherDataDownloading.WeatherDataFormatter;

class WeatherBasicInfoLayoutInitializer {

    private CoordinatorLayout mainLayout;
    private TextView onRefreshMessageTextView;
    private TextView conditionTextView;
    private ImageView conditionImageView;
    private TextView temperatureTextView;
    private TextView temperatureUnitTextView;
    private TextView temperatureDegreeSignTextView;
    private ImageView seeMoreImageView;
    private View currentDetailsDividerView;

    WeatherBasicInfoLayoutInitializer(Activity activity,
                                             MainActivityLayoutInitializer mainActivityLayoutInitializer,
                                             WeatherLayoutInitializer weatherLayoutInitializer){
        findLayoutViews(activity);
        rotateSeeMoreImageView();
        setWeatherBasicInfoLayoutSizes(activity,mainActivityLayoutInitializer);
        initializeSeeMoreArrowAnimation(activity,weatherLayoutInitializer);
    }

    private void findLayoutViews(Activity activity){
        mainLayout =(CoordinatorLayout) activity.findViewById(R.id.coordinator_layout);
        onRefreshMessageTextView=(TextView)activity.findViewById(R.id.main_content_layout_on_refresh_message_text);
        conditionTextView =(TextView)activity.findViewById(R.id.weather_basic_info_layout_conditions_text);
        conditionImageView =(ImageView)activity.findViewById(R.id.weather_basic_info_layout_conditions_image);
        temperatureTextView =(TextView)activity.findViewById(R.id.weather_basic_info_layout_temperature_text);
        temperatureUnitTextView=(TextView)activity.findViewById(R.id.weather_basic_info_temperature_unit_text);
        temperatureDegreeSignTextView =(TextView)activity.findViewById(R.id.weather_basic_info_layout_temperature_dagree_sign_text);
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
                TextView currentTemperatureDegreeSignTextView=(TextView)activity.findViewById(R.id.weather_basic_info_layout_temperature_dagree_sign_text);
                currentTemperatureDegreeSignTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, currentTemperatureTextViewHeight);
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

    private void initializeSeeMoreArrowAnimation(final Activity activity, final WeatherLayoutInitializer weatherLayoutInitializer){
        LockableSmoothNestedScrollView nestedScrollView
                =weatherLayoutInitializer.getGeneralWeatherLayoutInitializer().getNestedScrollView();
        nestedScrollView.addOnScrollListener(new android.support.v4.widget.NestedScrollView.OnScrollChangeListener() {
            boolean isTransparent=true;
            int normalMargin=(int)activity.getResources().getDimension(R.dimen.activity_normal_margin);
            @Override
            public void onScrollChange(android.support.v4.widget.NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY!=oldScrollY){
                    if(scrollY>normalMargin && isTransparent==false ){
                        isTransparent=true;
                        crossFade(activity,null,seeMoreImageView);
                    }
                    else if(scrollY<=normalMargin && isTransparent==true){
                        isTransparent=false;
                        crossFade(activity,seeMoreImageView,null);
                    }
                }
            }
        });
    }

    private static void crossFade(Context context, final View viewIn, final View viewOut) {
        int animationDuration = context.getResources().getInteger(android.R.integer.config_mediumAnimTime);
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
                        }
                    });
        }
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
                weatherLayoutInitializer.getGeneralWeatherLayoutInitializer().fadeInWeatherLayout(null);
            }

            @Override
            public void onError() {

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
        temperatureDegreeSignTextView.setTextColor(textPrimaryColor);
        int textDisabledColor=themeColorsUpdater.getTextDisabledColor();
        temperatureUnitTextView.setTextColor(textDisabledColor);
        int dividerColor=themeColorsUpdater.getDividerColor();
        Drawable arrowIconDrawable = UsefulFunctions.getColoredDrawable(activity,R.drawable.empty_arrow_icon,dividerColor);
        seeMoreImageView.setImageDrawable(arrowIconDrawable);
        currentDetailsDividerView.setBackgroundColor(dividerColor);
    }


}
