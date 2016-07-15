package paweltypiak.matweather.dataProcessing;

import android.app.Activity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import paweltypiak.matweather.R;
import paweltypiak.matweather.UsableFunctions;

public class DataSetter {
    private String chill;
    private String direction;
    private String directionName;
    private String speed;
    private String humidity;
    private String pressure;
    private String visibility;
    private String sunrise;
    private String sunset;
    private int layoutWidth;
    private int layoutHeight;
    private int imageTranslation;
    private int circleTranslation;
    private int code;
    private String temperature;
    private int[] forecastCode;
    private String[] forecastHighTemperature;
    private String[] forecastLowTemperature;
    private String link;
    private String city;
    private String country;
    private String region;
    private String lastBuildDate;
    private double latitude;
    private double longitude;
    private String[] dayName;
    private Date now;
    private LinearLayout weatherLayout;
    private View currentDetailsDividerView;
    private View detailsForecastDividerView;
    private ImageView yahooImageView;
    private TextView primaryLocationTextView;
    private TextView secondaryLocationTextView;
    private TextView timeTextView;
    private int conditionStringId;
    private int conditionDrawableId;
    private TextView conditionTextView;
    private ImageView conditionImageView;
    private TextView temperatureTextView;
    private TextView chillTextView;
    private TextView chillTitleTextView;
    private TextView highTemperatureTextView;
    private TextView lowTemperatureTextView;
    private ImageView highTemperatureImageView;
    private ImageView lowTemperatureImageView;
    private ImageView sunsetSunriseLeftImageView;
    private ImageView sunsetSunriseRightImageView;
    private TextView sunsetSunriseLeftTextView;
    private TextView sunsetSunriseRightTextView;
    private RelativeLayout sunPathLayout;
    private ImageView sunPathBackgroudImageView;
    private ImageView sunPathObjectImageView;
    private ImageView sunPathObjectBackgoundImageView;
    private ImageView sunPathLeftCircleImageView;
    private ImageView sunPathRightCircleImageView;
    private ImageView directionImageView;
    private ImageView directionNorthImageView;
    private ImageView speedImageView;
    private ImageView humidityImageView;
    private ImageView pressureImageView;
    private ImageView visibilityImageView;
    private TextView directionTextView;
    private TextView speedTextView;
    private TextView humidityTextView;
    private TextView pressureTextView;
    private TextView visibilityTextView;
    private int[] forecastDrawable;
    private TextView[] forecastDayNameTextView;
    private ImageView[] forecastDayConditionsImageView;
    private ImageView[] forecastHighTemperatureImageView;
    private ImageView[] forecastLowTemperatureImageView;
    private TextView[] forecastHighTemperatureTextView;
    private TextView[] forecastLowTemperatureTextView;
    private Activity activity;
    private DataInitializer dataInitializer;
    private SunPositionCounter sunPositionCounter;
    private boolean isDay;
    private int backgroundColor;
    private int textPrimaryColor;
    private int textSecondaryColor;
    private int dividerColor;
    private int iconColor;
    private int objectIconColor;
    private int dialogColor;

    public DataSetter(Activity activity, DataInitializer dataInitializer) {
        this.dataInitializer =dataInitializer;
        this.activity=activity;
        getData();
        sunPositionCounter=new SunPositionCounter();
        isDay =sunPositionCounter.getDay();
        setTheme();
        setAppBarLayout();
        setCurrentLayout();
        setDetailsLayout();
        setForecastLayout();
    }

    private void setTheme(){
        if(isDay==true) {
            backgroundColor=activity.getResources().getColor(R.color.backgroundLight);
            textPrimaryColor=activity.getResources().getColor(R.color.textPrimaryLightBackground);
            textSecondaryColor=activity.getResources().getColor(R.color.textSecondaryLightBackground);
            dividerColor=activity.getResources().getColor(R.color.dividerLightBackground);
            iconColor=activity.getResources().getColor(R.color.iconLightBackground);
            objectIconColor=activity.getResources().getColor(R.color.black);
            dialogColor=activity.getResources().getColor(R.color.dividerLightBackground);
        }
        else {
            backgroundColor=activity.getResources().getColor(R.color.backgroundDark);
            textPrimaryColor=activity.getResources().getColor(R.color.textPrimaryDarkBackground);
            textSecondaryColor=activity.getResources().getColor(R.color.textSecondaryDarkBackground);
            dividerColor=activity.getResources().getColor(R.color.dividerDarkBackground);
            iconColor=activity.getResources().getColor(R.color.iconDarkBackground);
            objectIconColor=activity.getResources().getColor(R.color.white);
            dialogColor=activity.getResources().getColor(R.color.dividerDarkBackground);
        }
    }



    private void setAppBarLayout(){
        getAppBarResources();
       /* String time=lastBuildDate.substring(0,8);
        SimpleDateFormat inputTimeFormat = new SimpleDateFormat("hh:mm a");
        SimpleDateFormat outputTimeFormat= new SimpleDateFormat("HH:mm");
        Date date;
        try{
            date= inputTimeFormat.parse(time);
            time = outputTimeFormat.format(date);

        }catch(ParseException pe){
            pe.printStackTrace();
        }*/

        primaryLocationTextView.setText(city);
        secondaryLocationTextView.setText(region+", "+country);
        timeTextView.setText(lastBuildDate);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.yahoo_logo).fit().centerInside().into(yahooImageView);
    }

    private void setCurrentLayout(){
        getCurrentResources();
        weatherLayout.setBackgroundColor(backgroundColor);
        conditionTextView.setText(conditionStringId);
        conditionTextView.setTextColor(textPrimaryColor);
        Picasso.with(activity.getApplicationContext()).load(conditionDrawableId).into(conditionImageView);
        temperatureTextView.setText(temperature);
        temperatureTextView.setTextColor(textPrimaryColor);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.arrow).transform(new UsableFunctions().new setDrawableColor(iconColor)).into(highTemperatureImageView);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.arrow).transform(new UsableFunctions().new setDrawableColor(iconColor)).rotate(180f).into(lowTemperatureImageView);
        highTemperatureTextView.setText(forecastHighTemperature[0]);
        highTemperatureTextView.setTextColor(textPrimaryColor);
        lowTemperatureTextView.setText(forecastLowTemperature[0]);
        lowTemperatureTextView.setTextColor(textPrimaryColor);
        chillTitleTextView.setText(activity.getResources().getString(R.string.weather_chill) + ": ");
        chillTitleTextView.setTextColor(iconColor);
        chillTextView.setText(chill);
        chillTextView.setTextColor(textPrimaryColor);
        currentDetailsDividerView.setBackgroundColor(dividerColor);
    }

    private void setDetailsLayout(){
        getDetailsResources();
        final double objectScale=0.5;
        final double lineScale=0.07;
        if(isDay ==true){
            Picasso.with(activity.getApplicationContext()).load(R.drawable.sunrise_icon).transform(new UsableFunctions().new setDrawableColor(iconColor)).fit().centerInside().into(sunsetSunriseLeftImageView);
            Picasso.with(activity.getApplicationContext()).load(R.drawable.sunset_icon).transform(new UsableFunctions().new setDrawableColor(iconColor)).fit().centerInside().into(sunsetSunriseRightImageView);
            sunsetSunriseLeftTextView.setText(sunrise);
            sunsetSunriseRightTextView.setText(sunset);
        }
        else {
            Picasso.with(activity.getApplicationContext()).load(R.drawable.sunrise_icon).transform(new UsableFunctions().new setDrawableColor(iconColor)).fit().centerInside().into(sunsetSunriseRightImageView);
            Picasso.with(activity.getApplicationContext()).load(R.drawable.sunset_icon).fit().transform(new UsableFunctions().new setDrawableColor(iconColor)).centerInside().into(sunsetSunriseLeftImageView);
            sunsetSunriseLeftTextView.setText(sunset);
            sunsetSunriseRightTextView.setText(sunrise);
        }
        sunsetSunriseLeftTextView.setTextColor(textPrimaryColor);
        sunsetSunriseRightTextView.setTextColor(textPrimaryColor);
        ViewTreeObserver treeObserver = sunPathLayout.getViewTreeObserver();
        treeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                sunPathLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                layoutHeight = sunPathLayout.getMeasuredHeight();
                Log.d("height", "height "+layoutHeight);
                layoutWidth = sunPathLayout.getMeasuredWidth();
                if(isDay==true){
                    Picasso.with(activity.getApplicationContext()).load(R.drawable.sun_icon).resize((int) (layoutHeight * objectScale), (int) (layoutHeight * objectScale)).transform(new UsableFunctions().new setDrawableColor(objectIconColor)).into(sunPathObjectImageView);
                }
                else Picasso.with(activity.getApplicationContext()).load(R.drawable.moon_icon).resize((int) (layoutHeight * objectScale), (int) (layoutHeight * objectScale)).transform(new UsableFunctions().new setDrawableColor(objectIconColor)).into(sunPathObjectImageView);
                Picasso.with(activity.getApplicationContext()).load(R.drawable.weather_line).transform(new UsableFunctions().new setDrawableColor(dividerColor)).resize((int) (layoutWidth - layoutHeight * objectScale), (int) (layoutHeight)).into(sunPathBackgroudImageView);
                Picasso.with(activity.getApplicationContext()).load(R.drawable.weather_small_circle).transform(new UsableFunctions().new setDrawableColor(iconColor)).resize((int) (layoutHeight * lineScale), (int) (layoutHeight * objectScale*0.15)).into(sunPathLeftCircleImageView);
                Picasso.with(activity.getApplicationContext()).load(R.drawable.weather_small_circle).transform(new UsableFunctions().new setDrawableColor(iconColor)).resize((int) (layoutHeight * lineScale), (int) (layoutHeight * objectScale * 0.15)).into(sunPathRightCircleImageView);
                imageTranslation = (int)((sunPositionCounter.getCurrentDiffMinutes() *(layoutWidth-(layoutHeight * objectScale))/sunPositionCounter.getSunsetSunriseDiffMinutes()));
                circleTranslation = (int)(layoutHeight * objectScale/2);
                Log.d("przesuniecie", "przesuniecie: " + imageTranslation);
                Log.d("przesuniecie", "szerokosc: " + layoutWidth);
                sunPathObjectImageView.setTranslationX(imageTranslation);
                sunPathRightCircleImageView.setTranslationX(-circleTranslation);
                sunPathLeftCircleImageView.setTranslationX(circleTranslation);
                return true;
            }
        });
        Picasso.with(activity.getApplicationContext()).load(R.drawable.direction_icon).transform(new UsableFunctions().new setDrawableColor(iconColor)).rotate(Float.parseFloat(direction)).fit().centerInside().into(directionImageView);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.north_letter_icon).fit().centerInside().into(directionNorthImageView);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.speed_icon).fit().transform(new UsableFunctions().new setDrawableColor(iconColor)).centerInside().into(speedImageView);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.humidity_icon).fit().transform(new UsableFunctions().new setDrawableColor(iconColor)).centerInside().into(humidityImageView);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.pressure_icon).fit().transform(new UsableFunctions().new setDrawableColor(iconColor)).centerInside().into(pressureImageView);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.visibility_icon).fit().transform(new UsableFunctions().new setDrawableColor(iconColor)).centerInside().into(visibilityImageView);
        directionTextView.setText(directionName);
        directionTextView.setTextColor(textPrimaryColor);
        speedTextView.setText(speed);
        speedTextView.setTextColor(textPrimaryColor);
        humidityTextView.setText(humidity);
        humidityTextView.setTextColor(textPrimaryColor);
        pressureTextView.setText(pressure);
        pressureTextView.setTextColor(textPrimaryColor);
        visibilityTextView.setText(visibility);
        visibilityTextView.setTextColor(textPrimaryColor);
        detailsForecastDividerView.setBackgroundColor(dividerColor);
    }

    public class SunPositionCounter {
        private boolean isDay;
        private long currentDiffMinutes;
        private long sunsetSunriseDiffMinutes;
        private Date sunriseHour;
        private Date sunsetHour;
        private Date beforeMidnight;
        private Date afterMidnight;

        public SunPositionCounter(){
            countSunPosition();
        }

        private void countSunPosition(){
            SimpleDateFormat outputFormat= new SimpleDateFormat("HH:mm");
            Calendar calendar = Calendar.getInstance();
            try{
                sunriseHour= outputFormat.parse(sunrise);
                sunsetHour= outputFormat.parse(sunset);
                CharSequence hourFormat=DateFormat.format("HH:mm", calendar);
                String hourString=hourFormat.toString();
                now=outputFormat.parse(hourString);
                Log.d("hour", "setDetailsLayout: "+hourString);
            }catch(ParseException pe){
                pe.printStackTrace();
            }
            if((now.after(sunriseHour)&&now.before(sunsetHour))||now.equals(sunriseHour)||now.equals(sunsetHour)){
                isDay =true;
                long sunsetSunriseDifference = Math.abs(sunsetHour.getTime() - sunriseHour.getTime());
                long currentDifference= Math.abs(now.getTime()-sunriseHour.getTime());
                sunsetSunriseDiffMinutes = sunsetSunriseDifference / (60 * 1000);
                currentDiffMinutes = currentDifference / (60 * 1000);
            }
            else {
                isDay=false;
                try{
                    beforeMidnight=outputFormat.parse("23:59");
                    afterMidnight=outputFormat.parse("00:00");
                }catch(ParseException pe){
                    pe.printStackTrace();
                }
                long twentyFourHours = Math.abs(beforeMidnight.getTime()-afterMidnight.getTime());
                long sunsetSunriseDifference=twentyFourHours-Math.abs(sunsetHour.getTime() - sunriseHour.getTime());
                if(now.before(sunriseHour)){
                    long currentDifference= sunsetSunriseDifference-Math.abs(now.getTime() - sunriseHour.getTime());
                    sunsetSunriseDiffMinutes = sunsetSunriseDifference / (60 * 1000);
                    currentDiffMinutes = currentDifference / (60 * 1000);

                }
                else {
                    long currentDifference= Math.abs(now.getTime() - sunsetHour.getTime());
                    sunsetSunriseDiffMinutes = sunsetSunriseDifference / (60 * 1000);
                    currentDiffMinutes = currentDifference / (60 * 1000);
                }
            }
        }

        public boolean getDay() {
            return isDay;
        }
        public long getCurrentDiffMinutes() {
            return currentDiffMinutes;
        }
        public long getSunsetSunriseDiffMinutes() {
            return sunsetSunriseDiffMinutes;
        }
    }

    private void setForecastLayout() {
        getForecastResouces();
        dayName = new String[4];
        for (int i = 0; i < 4; i++) {
            CharSequence dayFormat;
            Calendar calendar = Calendar.getInstance();
            if (i == 0)
                dayName[i] = activity.getResources().getString(R.string.weather_tomorrow_name);
            calendar.add(Calendar.DATE, i + 1);
            dayFormat = DateFormat.format("EEEE", calendar);
            dayName[i] = new String(dayFormat.toString().substring(0, 1).toUpperCase() + dayFormat.toString().substring(1));
            forecastDayNameTextView[i].setText(dayName[i]);
            forecastDayNameTextView[i].setTextColor(textPrimaryColor);
            forecastHighTemperatureTextView[i].setText(forecastHighTemperature[i]);
            forecastHighTemperatureTextView[i].setTextColor(textPrimaryColor);
            forecastLowTemperatureTextView[i].setText(forecastLowTemperature[i]);
            forecastLowTemperatureTextView[i].setTextColor(textPrimaryColor);
            Picasso.with(activity.getApplicationContext()).load(forecastDrawable[i]).into(forecastDayConditionsImageView[i]);
            Picasso.with(activity.getApplicationContext()).load(R.drawable.arrow).transform(new UsableFunctions().new setDrawableColor(iconColor)).into(forecastHighTemperatureImageView[i]);
            Picasso.with(activity.getApplicationContext()).load(R.drawable.arrow).transform(new UsableFunctions().new setDrawableColor(iconColor)).rotate(180f).into(forecastLowTemperatureImageView[i]);
        }
    }

    private void getData(){
        link=dataInitializer.getLink();
        city=dataInitializer.getCity();
        country=dataInitializer.getCountry();
        region=dataInitializer.getRegion();
        lastBuildDate=dataInitializer.getLastBuildDate();
        latitude=dataInitializer.getLatitude();
        longitude=dataInitializer.getLongitude();
        chill = dataInitializer.getChill();
        direction= dataInitializer.getDirection();
        directionName = dataInitializer.getDirection_name();
        speed= dataInitializer.getSpeed();
        humidity= dataInitializer.getHumidity();
        pressure= dataInitializer.getPressure();
        visibility= dataInitializer.getVisibility();
        sunrise= dataInitializer.getSunrise();
        sunset= dataInitializer.getSunset();
        code= dataInitializer.getCode();
        temperature= dataInitializer.getTemperature();
        forecastCode = dataInitializer.getForecast_code();
        forecastHighTemperature = dataInitializer.getForecast_high();
        forecastLowTemperature = dataInitializer.getForecast_low();
    }

    private void getAppBarResources(){
        primaryLocationTextView =(TextView)activity.findViewById(R.id.app_bar_primary_location_name_text);
        secondaryLocationTextView =(TextView)activity.findViewById(R.id.app_bar_secondary_location_name_text);
        timeTextView=(TextView)activity.findViewById(R.id.app_bar_time_text);
        yahooImageView=(ImageView)activity.findViewById(R.id.yahoo_image);
    }

    private void getCurrentResources(){
        weatherLayout=(LinearLayout)activity.findViewById(R.id.weather_layout);
        conditionStringId=activity.getResources().getIdentifier("condition_" + code, "string", activity.getPackageName());
        conditionDrawableId=activity.getResources().getIdentifier("drawable/icon_" + code, null, activity.getPackageName());
        conditionTextView =(TextView)activity.findViewById(R.id.current_conditions_text);
        conditionImageView =(ImageView)activity.findViewById(R.id.current_conditions_image);
        temperatureTextView =(TextView)activity.findViewById(R.id.current_temperature_text);
        chillTextView =(TextView)activity.findViewById(R.id.chill_text);
        chillTitleTextView =(TextView)activity.findViewById(R.id.chill_title_text);
        highTemperatureTextView =(TextView)activity.findViewById(R.id.high_temperature_text);
        lowTemperatureTextView =(TextView)activity.findViewById(R.id.low_temperature_text);
        highTemperatureImageView =(ImageView)activity.findViewById(R.id.high_temperature_image);
        lowTemperatureImageView =(ImageView)activity.findViewById(R.id.low_temperature_image);
        currentDetailsDividerView =activity.findViewById(R.id.current_details_divider);
    }

    private void getDetailsResources(){
        sunsetSunriseLeftImageView =(ImageView)activity.findViewById(R.id.sunrise_sunset_left_image);
        sunsetSunriseRightImageView =(ImageView)activity.findViewById(R.id.sunrise_sunset_right_image);
        sunsetSunriseLeftTextView =(TextView)activity.findViewById(R.id.sunrise_sunset_left_text);
        sunsetSunriseRightTextView =(TextView)activity.findViewById(R.id.sunrise_sunset_right_text);
        sunPathLayout =(RelativeLayout)activity.findViewById(R.id.sun_path_layout);
        sunPathBackgroudImageView =(ImageView)activity.findViewById(R.id.sun_path_backgroud_image);
        sunPathObjectImageView =(ImageView)activity.findViewById(R.id.sun_path_object_image);
        sunPathObjectBackgoundImageView =(ImageView)activity.findViewById(R.id.sun_path_object_background_image);
        sunPathLeftCircleImageView =(ImageView)activity.findViewById(R.id.sun_path_left_image);
        sunPathRightCircleImageView =(ImageView)activity.findViewById(R.id.sun_path_right_image);
        directionImageView =(ImageView)activity.findViewById(R.id.direction_image);
        directionNorthImageView =(ImageView)activity.findViewById(R.id.direction_north_icon_image);
        speedImageView =(ImageView)activity.findViewById(R.id.speed_image);
        humidityImageView =(ImageView)activity.findViewById(R.id.humidity_image);
        pressureImageView =(ImageView)activity.findViewById(R.id.pressure_image);
        visibilityImageView =(ImageView)activity.findViewById(R.id.visibility_image);
        directionTextView =(TextView)activity.findViewById(R.id.direction_text);
        speedTextView =(TextView)activity.findViewById(R.id.speed_text);
        humidityTextView =(TextView)activity.findViewById(R.id.humidity_text);
        pressureTextView=(TextView)activity.findViewById(R.id.pressure_text);
        visibilityTextView =(TextView)activity.findViewById(R.id.visibility_text);
        detailsForecastDividerView =activity.findViewById(R.id.details_forecast_divider);
    }

    private void getForecastResouces(){
        forecastDrawable=new int [4];
        forecastDayNameTextView=new TextView[4];
        forecastDayConditionsImageView =new ImageView[4];
        forecastHighTemperatureTextView=new TextView[4];
        forecastLowTemperatureTextView=new TextView[4];
        forecastHighTemperatureImageView=new ImageView[4];
        forecastLowTemperatureImageView=new ImageView[4];
        for(int i=0;i<4;i++){
            forecastDrawable[i]=activity.getResources().getIdentifier("drawable/icon_" + forecastCode[i+1], null, activity.getPackageName());
            forecastDayNameTextView[i] =(TextView)activity.findViewById(activity.getResources().getIdentifier("forecast_day"+(i+1)+"_name_text", "id", activity.getPackageName()));
            forecastDayConditionsImageView[i] =(ImageView)activity.findViewById(activity.getResources().getIdentifier("forecast_day"+(i+1)+"_conditions_image", "id",activity.getPackageName()));
            forecastHighTemperatureTextView[i]=(TextView)activity.findViewById(activity.getResources().getIdentifier("forecast_day"+(i+1)+"_high_temperature_text", "id",activity.getPackageName()));
            forecastLowTemperatureTextView[i]=(TextView)activity.findViewById(activity.getResources().getIdentifier("forecast_day"+(i+1)+"_low_temperature_text","id", activity.getPackageName()));
            forecastHighTemperatureImageView[i]=(ImageView)activity.findViewById(activity.getResources().getIdentifier("forecast_day"+(i+1)+"_high_temperature_image","id", activity.getPackageName()));
            forecastLowTemperatureImageView[i]=(ImageView)activity.findViewById(activity.getResources().getIdentifier("forecast_day"+(i+1)+"_low_temperature_image","id", activity.getPackageName()));
        }
    }
}
