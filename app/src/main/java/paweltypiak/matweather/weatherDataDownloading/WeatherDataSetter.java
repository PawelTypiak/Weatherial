package paweltypiak.matweather.weatherDataDownloading;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import static paweltypiak.matweather.usefulClasses.UsefulFunctions.initializeUiThread;
import static paweltypiak.matweather.usefulClasses.UsefulFunctions.setfloatingActionButtonOnClickIndicator;
import paweltypiak.matweather.R;
import paweltypiak.matweather.usefulClasses.FavouritesEditor;
import paweltypiak.matweather.usefulClasses.SharedPreferencesModifier;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

public class WeatherDataSetter {

    //private String chill;
    private String direction;
    private String directionName;
    private String speed;
    private String humidity;
    private String pressure;
    //private String visibility;
    private String sunrise;
    private String sunset;
    private int code;
    private String temperature;
    private String temperatureUnit;
    private String temperatureDagreeSign;
    private int[] forecastCode;
    private String[] forecastHighTemperature;
    private String[] forecastLowTemperature;
    private String link;
    private String city;
    private String country;
    private String region;
    private String timezone;
    private double latitude;
    private double longitude;
    private String[] dayName;
    private LinearLayout contentLayout;
    private View currentDetailsDividerView;
    private View detailsSubdividerView;
    private View detailsForecastDividerView;
    private ImageView yahooImageView;
    private TextView timeTextView;
    private TextView timezoneTextView;
    private int conditionStringId;
    private int conditionDrawableId;
    private TextView conditionTextView;
    private ImageView conditionImageView;
    private TextView temperatureTextView;
    private TextView temperatureUnitTextView;
    private TextView temperatureDagreeSignTextView;
    //private ImageView sunsetSunriseLeftImageView;
    //private ImageView sunsetSunriseRightImageView;
    private TextView sunsetSunriseLeftTextView;
    private TextView sunsetSunriseRightTextView;
    private RelativeLayout sunPathLayout;
    private ImageView sunPathBackgroudImageView;
    private ImageView sunPathObjectImageView;
    private ImageView sunPathLeftCircleImageView;
    private ImageView sunPathRightCircleImageView;
    private long currentDiffMinutes;
    private long sunsetSunriseDiffMinutes;
    private ImageView directionImageView;
    private ImageView directionNorthImageView;
    private ImageView speedImageView;
    private ImageView humidityImageView;
    private ImageView pressureImageView;
   // private ImageView visibilityImageView;
    private TextView directionTextView;
    private TextView speedTextView;
    private TextView humidityTextView;
    private TextView pressureTextView;
   // private TextView visibilityTextView;
    private ImageView refreshIconImageView;
    private int[] forecastConditionsDrawableId;
    private int[] forecastConditionsStringId;
    private TextView[] forecastDayNameTextView;
    private ImageView[] forecastDayConditionsImageView;
    private TextView[] forecastDayConditionsTextView;
    private ImageView[] forecastHighTemperatureImageView;
    private ImageView[] forecastLowTemperatureImageView;
    private TextView[] forecastHighTemperatureTextView;
    private TextView[] forecastLowTemperatureTextView;
    private Activity activity;
    private double objectScale;
    private double lineScale;
    private boolean isDay;
    private int backgroundColor;
    private int textPrimaryColor;
    private int textSecondaryColor;
    private int textDisabledColor;
    private int dividerColor;
    private int iconColor;
    private int objectIconColor;
    private static WeatherDataFormatter currentDataFormatter;
    private static boolean startTimeThread;
    public static boolean newRefresh;
    private String sunsetSunriseDiffMinutesString;
    private String currentDiffMinutesString;
    private String isDayString;
    private int layoutHeight;
    private int layoutWidth;
    private static Thread uiThread;
    private static int units[];
    private boolean isGeolocalizationMode;
    private static WeatherDataInitializer currentWeatherDataInitializer;


    public WeatherDataSetter(Activity activity,
                             WeatherDataInitializer dataInitializer,
                             boolean doSetAppBar,
                             boolean isGeolocalizationMode) {
        this.activity=activity;
        this.isGeolocalizationMode=isGeolocalizationMode;
        currentWeatherDataInitializer=dataInitializer;
        currentDataFormatter=new WeatherDataFormatter(activity, dataInitializer);
        newRefresh=true;
        units= SharedPreferencesModifier.getUnits(activity);
        getData();
        setTheme();
        if(doSetAppBar==true)setAppBarLayout();
        setWeatherLayout();
        startUiThread();
    }

    private void startUiThread(){
        //start threat for updating UI
        if(UsefulFunctions.getIsFirstWeatherDownloading()==true){
            uiThread=initializeUiThread(activity,timeUpdateRunnable);
            uiThread.start();
        }
        setStartTimeThread(true);
    }

    static public void interruptUiThread(){
        uiThread.interrupt();
    }

    Runnable timeUpdateRunnable = new Runnable() {
        public void run() {
            //update UI
            if (startTimeThread == true) {
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.HOUR,getCurrentDataFormatter().getHourDifference());
                setCurrentTime(calendar);
                updateLayout(calendar);
            }
        }
    };

    private void setCurrentTime(Calendar calendar){
        //update timeTextView text every second
        String outputFormat;
        if(units[4]==0) outputFormat="H:mm:ss";
        else outputFormat="h:mm:ss a";
        timeTextView.setText(DateFormat.format(outputFormat, calendar));
    }
    private void changeTimeOfDay(){
        //change time of  day if sunrise or sunset
        isDay=!isDay;
        setTheme();
        setWeatherLayout();
    }
    private void updateLayout(Calendar calendar){
        //update sun position every minute
        String outputMinutesFormat="H:mm";
        String outputMinutesString=DateFormat.format(outputMinutesFormat, calendar).toString();
        String[] sunPositionStrings=currentDataFormatter.countSunPosition(outputMinutesString);
        if(newRefresh==true){
            assignSunPositionStrings(sunPositionStrings);
            newRefresh=false;
        }
        else{
            if(!currentDiffMinutesString.equals(sunPositionStrings[1])){
                if(!isDayString.equals(sunPositionStrings[2])){
                    Log.d("data setter", "change time of day");
                    changeTimeOfDay();
                }
                assignSunPositionStrings(sunPositionStrings);
                setSunTranslation();
            }
        }
    }

    private void assignSunPositionStrings(String[] sunPositionStrings){
        sunsetSunriseDiffMinutesString=sunPositionStrings[0];
        currentDiffMinutesString=sunPositionStrings[1];
        isDayString=sunPositionStrings[2];
        sunsetSunriseDiffMinutes=Long.parseLong(sunPositionStrings[0]);
        currentDiffMinutes=Long.parseLong(sunPositionStrings[1]);
        isDay=(Integer.parseInt(sunPositionStrings[1])) != 0;
    }

    private void setTheme(){
        //set theme for time of day - light for day, dark for night
        if(isDay==true) {
            backgroundColor= ContextCompat.getColor(activity,R.color.backgroundLight);
            textPrimaryColor=ContextCompat.getColor(activity,R.color.textPrimaryLightBackground);
            textSecondaryColor=ContextCompat.getColor(activity,R.color.textSecondaryLightBackground);
            textDisabledColor=ContextCompat.getColor(activity,R.color.textDisabledLightBackground);
            dividerColor=ContextCompat.getColor(activity,R.color.dividerLightBackground);
            iconColor=ContextCompat.getColor(activity,R.color.iconLightBackground);
            objectIconColor=ContextCompat.getColor(activity,R.color.black);
        }
        else {
            backgroundColor=ContextCompat.getColor(activity,R.color.backgroundDark);
            textPrimaryColor=ContextCompat.getColor(activity,R.color.textPrimaryDarkBackground);
            textSecondaryColor=ContextCompat.getColor(activity,R.color.textSecondaryDarkBackground);
            textDisabledColor=ContextCompat.getColor(activity,R.color.textDisabledDarkBackground);
            dividerColor=ContextCompat.getColor(activity,R.color.dividerDarkBackground);
            iconColor=ContextCompat.getColor(activity,R.color.iconDarkBackground);
            objectIconColor=ContextCompat.getColor(activity,R.color.white);
        }
    }



    private void setAppBarLayout(){
        getAppBarResources();


        if(FavouritesEditor.isAddressEqual(activity)){
            FavouritesEditor.setLayoutForFavourites(activity);
            Log.d("data setter", "location in favourites");
        }
        else{
            UsefulFunctions.setAppBarStrings(activity,city,region+", "+country);
            setfloatingActionButtonOnClickIndicator(activity,0);
            if(isGeolocalizationMode==true){
                UsefulFunctions.checkNavigationDrawerMenuItem(activity,0);
            }
            else {
                UsefulFunctions.uncheckAllNavigationDrawerMenuItems(activity);
                Log.d("data setter", "location found by searching, not in favourites");
            }
        }
        UsefulFunctions.setViewGone(timezoneTextView);
        timezoneTextView.setText(timezone);
        UsefulFunctions.setViewVisible(timezoneTextView);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.arrow).transform(new UsefulFunctions().new setDrawableColor(ContextCompat.getColor(activity,R.color.textPrimaryDarkBackground))).rotate(180).fit().centerInside().into(refreshIconImageView);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.yahoo_logo).fit().centerInside().into(yahooImageView);
    }



    private void setWeatherLayout(){
        setCurrentLayout();
        setDetailsLayout();
        setForecastLayout();
    }

    private void setCurrentLayout(){
        getCurrentResources();
        contentLayout.setBackgroundColor(backgroundColor);
        conditionTextView.setText(conditionStringId);
        conditionTextView.setTextColor(textPrimaryColor);
        temperatureTextView.setText(temperature);
        temperatureTextView.setTextColor(textPrimaryColor);
        temperatureDagreeSignTextView.setTextColor(textPrimaryColor);
        temperatureUnitTextView.setText(temperatureUnit);
        temperatureUnitTextView.setTextColor(textDisabledColor);
        Picasso.with(activity.getApplicationContext()).load(conditionDrawableId).into(conditionImageView);
        Drawable arrowIconDrawable = UsefulFunctions.getColoredDrawable(activity,R.drawable.arrow_no_padding_icon,dividerColor);
        ImageView seeMoreImageView=(ImageView)activity.findViewById(R.id.current_weather_layout_see_more_image);
        seeMoreImageView.setRotation(180);
        seeMoreImageView.setImageDrawable(arrowIconDrawable);

        currentDetailsDividerView.setBackgroundColor(dividerColor);
    }

    private void setSunPathLayout(){
        //set layout for sun path
        ViewTreeObserver treeObserver = sunPathLayout.getViewTreeObserver();
        treeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                objectScale=0.5;
                lineScale=0.07;
                layoutHeight = sunPathLayout.getMeasuredHeight();
                layoutWidth = sunPathLayout.getMeasuredWidth();
                sunPathLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                if(isDay ==true){
                    Picasso.with(activity.getApplicationContext()).load(R.drawable.sun_icon).resize((int) (layoutHeight * objectScale), (int) (layoutHeight * objectScale)).transform(new UsefulFunctions().new setDrawableColor(objectIconColor)).into(sunPathObjectImageView);
                }
                else {
                    Picasso.with(activity.getApplicationContext()).load(R.drawable.moon_icon).resize((int) (layoutHeight * objectScale), (int) (layoutHeight * objectScale)).transform(new UsefulFunctions().new setDrawableColor(objectIconColor)).into(sunPathObjectImageView);
                }
                Picasso.with(activity.getApplicationContext()).load(R.drawable.weather_line).transform(new UsefulFunctions().new setDrawableColor(dividerColor)).resize((int) (layoutWidth - layoutHeight * objectScale), (int) (layoutHeight)).into(sunPathBackgroudImageView);
                Picasso.with(activity.getApplicationContext()).load(R.drawable.weather_small_circle).transform(new UsefulFunctions().new setDrawableColor(iconColor)).resize((int) (layoutHeight * lineScale), (int) (layoutHeight * lineScale)).into(sunPathLeftCircleImageView);
                Picasso.with(activity.getApplicationContext()).load(R.drawable.weather_small_circle).transform(new UsefulFunctions().new setDrawableColor(iconColor)).resize((int) (layoutHeight * lineScale), (int) (layoutHeight * lineScale)).into(sunPathRightCircleImageView);
                int circleTranslation = (int)(layoutHeight * objectScale/2);
                sunPathRightCircleImageView.setTranslationX(-circleTranslation);
                sunPathLeftCircleImageView.setTranslationX(circleTranslation);
                setSunTranslation();
                return true;
            }
        });
    }

    private void setSunTranslation(){
        //translate sun on sun line
        int imageTranslation = (int)((currentDiffMinutes *(layoutWidth-(layoutHeight * objectScale))/sunsetSunriseDiffMinutes));
        sunPathObjectImageView.setTranslationX(imageTranslation);
    }

    private void setDetailsLayout(){
        getDetailsResources();
        if(isDay ==true){
            sunsetSunriseLeftTextView.setText(sunrise);
            sunsetSunriseRightTextView.setText(sunset);
            /*Picasso.with(activity.getApplicationContext()).load(R.drawable.sunrise_icon).transform(new UsefulFunctions().new setDrawableColor(iconColor)).fit().centerInside().into(sunsetSunriseLeftImageView);
            Picasso.with(activity.getApplicationContext()).load(R.drawable.sunset_icon).transform(new UsefulFunctions().new setDrawableColor(iconColor)).fit().centerInside().into(sunsetSunriseRightImageView);*/
        }
        else {
            sunsetSunriseLeftTextView.setText(sunset);
            sunsetSunriseRightTextView.setText(sunrise);
            /*Picasso.with(activity.getApplicationContext()).load(R.drawable.sunrise_icon).transform(new UsefulFunctions().new setDrawableColor(iconColor)).fit().centerInside().into(sunsetSunriseRightImageView);
            Picasso.with(activity.getApplicationContext()).load(R.drawable.sunset_icon).fit().transform(new UsefulFunctions().new setDrawableColor(iconColor)).centerInside().into(sunsetSunriseLeftImageView);*/
        }
        sunsetSunriseLeftTextView.setTextColor(textPrimaryColor);
        sunsetSunriseRightTextView.setTextColor(textPrimaryColor);
        setSunPathLayout();
        directionTextView.setText(directionName);
        directionTextView.setTextColor(textPrimaryColor);
        speedTextView.setText(speed);
        speedTextView.setTextColor(textPrimaryColor);
        humidityTextView.setText(humidity);
        humidityTextView.setTextColor(textPrimaryColor);
        pressureTextView.setText(pressure);
        pressureTextView.setTextColor(textPrimaryColor);
        //visibilityTextView.setText(visibility);
        //visibilityTextView.setTextColor(textPrimaryColor);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.direction_icon).transform(new UsefulFunctions().new setDrawableColor(backgroundColor)).rotate(Float.parseFloat(direction)).fit().centerInside().into(directionImageView);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.north_letter_icon).fit().transform(new UsefulFunctions().new setDrawableColor(iconColor)).centerInside().into(directionNorthImageView);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.speed_icon).fit().transform(new UsefulFunctions().new setDrawableColor(iconColor)).centerInside().into(speedImageView);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.humidity_icon).fit().transform(new UsefulFunctions().new setDrawableColor(iconColor)).centerInside().into(humidityImageView);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.pressure_icon).fit().transform(new UsefulFunctions().new setDrawableColor(iconColor)).centerInside().into(pressureImageView);
        //Picasso.with(activity.getApplicationContext()).load(R.drawable.visibility_icon).fit().transform(new UsefulFunctions().new setDrawableColor(iconColor)).centerInside().into(visibilityImageView);
        detailsSubdividerView.setBackgroundColor(dividerColor);
        detailsForecastDividerView.setBackgroundColor(dividerColor);


       /*final LinearLayout lin=(LinearLayout)activity.findViewById(R.id.additional_conditions_layout);
        ViewTreeObserver observer = lin.getViewTreeObserver();
        observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                View view_small=activity.findViewById(R.id.test_view_small);
                Log.d("width", "onGlobalLayout small: "+view_small.getWidth());
                View view_big=activity.findViewById(R.id.test_view_big);
                Log.d("width", "onGlobalLayout big: "+view_big.getWidth());
                lin.getViewTreeObserver().removeOnGlobalLayoutListener(
                        this);
            }
        });*/


    }

    private void setForecastLayout() {
        getForecastResouces();
        dayName = new String[5];
        for (int i = 0; i < 5; i++) {
            CharSequence dayFormat;
            Calendar calendar = Calendar.getInstance();
            if(i!=0){
                calendar.add(Calendar.DATE, i);
            }
            dayFormat = DateFormat.format("EEEE", calendar);
            dayName[i] = new String(dayFormat.toString().substring(0, 1).toUpperCase() + dayFormat.toString().substring(1));
            forecastDayNameTextView[i].setText(dayName[i]);
            forecastDayNameTextView[i].setTextColor(textPrimaryColor);
            forecastDayConditionsTextView[i].setText(forecastConditionsStringId[i]);
            forecastDayConditionsTextView[i].setTextColor(textPrimaryColor);
            forecastHighTemperatureTextView[i].setText(forecastHighTemperature[i]);
            forecastHighTemperatureTextView[i].setTextColor(textPrimaryColor);
            forecastLowTemperatureTextView[i].setText(forecastLowTemperature[i]);
            forecastLowTemperatureTextView[i].setTextColor(textPrimaryColor);
            Picasso.with(activity.getApplicationContext()).load(forecastConditionsDrawableId[i]).into(forecastDayConditionsImageView[i]);
            Picasso.with(activity.getApplicationContext()).load(R.drawable.arrow).transform(new UsefulFunctions().new setDrawableColor(iconColor)).into(forecastHighTemperatureImageView[i]);
            Picasso.with(activity.getApplicationContext()).load(R.drawable.arrow).transform(new UsefulFunctions().new setDrawableColor(iconColor)).rotate(180f).into(forecastLowTemperatureImageView[i]);
        }
    }

    private void getData(){
        link=currentDataFormatter.getLink();
        city=currentDataFormatter.getCity();
        region=currentDataFormatter.getRegion();
        country=currentDataFormatter.getCountry();
        latitude=currentDataFormatter.getLatitude();
        longitude=currentDataFormatter.getLongitude();
        //chill = currentDataFormatter.getChill();
        direction= currentDataFormatter.getDirection();
        speed= currentDataFormatter.getSpeed();
        humidity= currentDataFormatter.getHumidity();
        pressure= currentDataFormatter.getPressure();
        //visibility= currentDataFormatter.getVisibility();
        sunrise= currentDataFormatter.getSunrise();
        sunset= currentDataFormatter.getSunset();
        code= currentDataFormatter.getCode();
        temperature= currentDataFormatter.getTemperature();
        temperatureUnit=currentDataFormatter.getTemperatureUnit();
        forecastCode = currentDataFormatter.getForecastCode();
        forecastHighTemperature = currentDataFormatter.getForecastHighTemperature();
        forecastLowTemperature = currentDataFormatter.getForecastLowTemperature();
        directionName=currentDataFormatter.getDirectionName();
        timezone=currentDataFormatter.getTimezone();
        isDay=currentDataFormatter.getDay();
        currentDiffMinutes=currentDataFormatter.getCurrentDiffMinutes();
        sunsetSunriseDiffMinutes=currentDataFormatter.getSunsetSunriseDiffMinutes();
        Log.d("formatted_data", "link: "+link);
        Log.d("formatted_data", "city: "+city);
        Log.d("formatted_data", "region: "+region);
        Log.d("formatted_data", "country: "+country);
        Log.d("formatted_data", "latitude: "+latitude);
        Log.d("formatted_data", "longitude: "+longitude);
        //Log.d("formatted_data", "chill: "+chill);
        Log.d("formatted_data", "direction: "+direction);
        Log.d("formatted_data", "speed: "+speed);
        Log.d("formatted_data", "humidity: "+humidity);
        Log.d("formatted_data", "pressure: "+pressure);
        //Log.d("formatted_data", "visibility: "+visibility);
        Log.d("formatted_data", "sunrise: "+sunrise);
        Log.d("formatted_data", "sunset: "+sunset);
        Log.d("formatted_data", "code: "+code);
        Log.d("formatted_data", "temperature: "+temperature);
        for(int i=0;i<5;i++){
            Log.d("formatted_data", "day "+i+":");
            Log.d("formatted_data", "forecastCode: "+forecastCode[i]);
            Log.d("formatted_data", "forecastHigh: "+forecastHighTemperature[i]);
            Log.d("formatted_data", "forecastLow: "+forecastLowTemperature[i]);
        }
        Log.d("formatted_data", "directionName: "+directionName);
        Log.d("formatted_data", "timezone: "+timezone);
        Log.d("formatted_data", "isDay: "+isDay);
        Log.d("formatted_data", "currentDiffMinutes: "+currentDiffMinutes);
        Log.d("formatted_data", "sunsetSunriseDiffMinutes: "+sunsetSunriseDiffMinutes);
    }

    private void getAppBarResources(){
        contentLayout =(LinearLayout)activity.findViewById(R.id.main_activity_content_layout);
        timeTextView=(TextView)activity.findViewById(R.id.app_bar_time_text);
        timezoneTextView =(TextView)activity.findViewById(R.id.app_bar_timezone_text);
        refreshIconImageView=(ImageView)activity.findViewById(R.id.app_bar_refresh_image);
        yahooImageView=(ImageView)activity.findViewById(R.id.yahoo_image);
    }

    private void getCurrentResources(){
        if(contentLayout==null) contentLayout =(LinearLayout)activity.findViewById(R.id.main_activity_content_layout);
        conditionStringId=activity.getResources().getIdentifier("condition_" + code, "string", activity.getPackageName());
        conditionDrawableId=activity.getResources().getIdentifier("drawable/conditions_icon_" + code, null, activity.getPackageName());
        conditionTextView =(TextView)activity.findViewById(R.id.current_conditions_text);
        conditionImageView =(ImageView)activity.findViewById(R.id.current_conditions_image);
        temperatureTextView =(TextView)activity.findViewById(R.id.current_temperature_text);
        temperatureUnitTextView=(TextView)activity.findViewById(R.id.current_temperature_unit_text);
        temperatureDagreeSignTextView=(TextView)activity.findViewById(R.id.current_temperature_dagree_sign_text);
        currentDetailsDividerView =activity.findViewById(R.id.current_details_divider);
    }

    private void getDetailsResources(){
        /*sunsetSunriseLeftImageView =(ImageView)activity.findViewById(R.id.sunrise_sunset_left_image);
        sunsetSunriseRightImageView =(ImageView)activity.findViewById(R.id.sunrise_sunset_right_image);*/
        sunsetSunriseLeftTextView =(TextView)activity.findViewById(R.id.sunrise_sunset_left_text);
        sunsetSunriseRightTextView =(TextView)activity.findViewById(R.id.sunrise_sunset_right_text);
        sunPathLayout =(RelativeLayout)activity.findViewById(R.id.sun_path_layout);
        sunPathBackgroudImageView =(ImageView)activity.findViewById(R.id.sun_path_backgroud_image);
        sunPathObjectImageView =(ImageView)activity.findViewById(R.id.sun_path_object_image);
        sunPathLeftCircleImageView =(ImageView)activity.findViewById(R.id.sun_path_left_image);
        sunPathRightCircleImageView =(ImageView)activity.findViewById(R.id.sun_path_right_image);
        directionImageView =(ImageView)activity.findViewById(R.id.direction_image);
        directionNorthImageView =(ImageView)activity.findViewById(R.id.direction_north_icon_image);
        speedImageView =(ImageView)activity.findViewById(R.id.speed_image);
        humidityImageView =(ImageView)activity.findViewById(R.id.humidity_image);
        pressureImageView =(ImageView)activity.findViewById(R.id.pressure_image);
        //visibilityImageView =(ImageView)activity.findViewById(R.id.visibility_image);
        directionTextView =(TextView)activity.findViewById(R.id.direction_text);
        speedTextView =(TextView)activity.findViewById(R.id.speed_text);
        humidityTextView =(TextView)activity.findViewById(R.id.humidity_text);
        pressureTextView=(TextView)activity.findViewById(R.id.pressure_text);
        //visibilityTextView =(TextView)activity.findViewById(R.id.visibility_text);
        detailsSubdividerView=activity.findViewById(R.id.details_subdivider);
        detailsForecastDividerView =activity.findViewById(R.id.details_forecast_divider);
    }

    private void getForecastResouces(){
        forecastConditionsDrawableId =new int [5];
        forecastConditionsStringId=new int[5];
        forecastDayNameTextView=new TextView[5];
        forecastDayConditionsImageView =new ImageView[5];
        forecastDayConditionsTextView=new TextView[5];
        forecastHighTemperatureTextView=new TextView[5];
        forecastLowTemperatureTextView=new TextView[5];
        forecastHighTemperatureImageView=new ImageView[5];
        forecastLowTemperatureImageView=new ImageView[5];
        for(int i=0;i<5;i++){
            forecastConditionsDrawableId[i]=activity.getResources().getIdentifier("drawable/forecast_conditions_icon_" + forecastCode[i], null, activity.getPackageName());
            forecastConditionsStringId[i]=activity.getResources().getIdentifier("condition_" + forecastCode[i], "string",activity.getPackageName());
            forecastDayNameTextView[i] =(TextView)activity.findViewById(activity.getResources().getIdentifier("forecast_day"+(i)+"_name_text", "id", activity.getPackageName()));
            forecastDayConditionsImageView[i] =(ImageView)activity.findViewById(activity.getResources().getIdentifier("forecast_day"+(i)+"_conditions_image", "id",activity.getPackageName()));
            forecastDayConditionsTextView[i]=(TextView)activity.findViewById(activity.getResources().getIdentifier("forecast_day"+(i)+"_conditions_text", "id",activity.getPackageName()));
            forecastHighTemperatureTextView[i]=(TextView)activity.findViewById(activity.getResources().getIdentifier("forecast_day"+(i)+"_high_temperature_text", "id",activity.getPackageName()));
            forecastLowTemperatureTextView[i]=(TextView)activity.findViewById(activity.getResources().getIdentifier("forecast_day"+(i)+"_low_temperature_text","id", activity.getPackageName()));
            forecastHighTemperatureImageView[i]=(ImageView)activity.findViewById(activity.getResources().getIdentifier("forecast_day"+(i)+"_high_temperature_image","id", activity.getPackageName()));
            forecastLowTemperatureImageView[i]=(ImageView)activity.findViewById(activity.getResources().getIdentifier("forecast_day"+(i)+"_low_temperature_image","id", activity.getPackageName()));
        }
    }

    public static WeatherDataFormatter getCurrentDataFormatter() {return currentDataFormatter;}

    public static void setStartTimeThread(boolean startTimeThread) {
        WeatherDataSetter.startTimeThread = startTimeThread;}

    public static WeatherDataInitializer getCurrentWeatherDataInitializer() {
        return currentWeatherDataInitializer;
    }
}
