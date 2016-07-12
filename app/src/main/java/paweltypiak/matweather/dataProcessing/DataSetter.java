package paweltypiak.matweather.dataProcessing;

import android.app.Activity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import paweltypiak.matweather.ColorTransformation;
import paweltypiak.matweather.R;

public class DataSetter {
    private int chill;
    private String direction;
    private String directionName;
    private int speed;
    private int humidity;
    private String pressure;
    private String visibility;
    private String sunrise;
    private String sunset;
    private int code;
    private int temperature;
    private int[] forecastCode;
    private int[] forecastHighTemperature;
    private int[] forecastLowTemperature;
    private String dayName1;
    private String dayName2;
    private String dayName3;
    private String dayName4;
    private Calendar calendar;
    private CharSequence dayFormat;
    private CharSequence dateFormat;
    private CharSequence hourFormat;
    private Date now;
    private Date sunriseHour;
    private Date sunsetHour;
    private Date beforeMidnight;
    private Date afterMidnight;
    private int layoutWidth;
    private int layoutHeight;
    private int imageTranslation;
    private int circleTranslation;
    private ImageView yahooImageView;
    private ImageView refreshImageView;
    private TextView titleTextView;
    private TextView dateTextView;
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
    private int forecastDay1Drawable;
    private int forecastDay2Drawable;
    private int forecastDay3Drawable;
    private int forecastDay4Drawable;
    private TextView forecastDayName1TextView;
    private TextView forecastDayName2TextView;
    private TextView forecastDayName3TextView;
    private TextView forecastDayName4TextView;
    private ImageView forecastDayName1ImageView;
    private ImageView forecastDayName2ImageView;
    private ImageView forecastDayName3ImageView;
    private ImageView forecastDayName4ImageView;
    private ImageView forecastHighTemperatureDay1ImageView;
    private ImageView forecastHighTemperatureDay2ImageView;
    private ImageView forecastHighTemperatureDay3ImageView;
    private ImageView forecastHighTemperatureDay4ImageView;
    private ImageView forecastLowTemperatureDay1ImageView;
    private ImageView forecastLowTemperatureDay2ImageView;
    private ImageView forecastLowTemperatureDay3ImageView;
    private ImageView forecastLowTemperatureDay4ImageView;
    private TextView forecastHighTemperatureDay1TextView;
    private TextView forecastHighTemperatureDay2TextView;
    private TextView forecastHighTemperatureDay3TextView;
    private TextView forecastHighTemperatureDay4TextView;
    private TextView forecastLowTemperatureDay1TextView;
    private TextView forecastLowTemperatureDay2TextView;
    private TextView forecastLowTemperatureDay3TextView;
    private TextView forecastLowTemperatureDay4TextView;
    private Activity activity;
    private DataInitializer getter;

    public DataSetter(Activity activity, DataInitializer getter) {
        this.getter=getter;
        this.activity=activity;
        getData();
        setAppBarLayout();
        setCurrentLayout();
        setDetailsLayout();
        setForecastLayout();
    }

    private void setAppBarLayout(){
        getAppBarResources();
        calendar = Calendar.getInstance();
        dayFormat = DateFormat.format("EEEE", calendar);
        dateFormat = DateFormat.format("d MMMM", calendar);
        String dayName=new String(dayFormat.toString().substring(0,1).toUpperCase()+ dayFormat.toString().substring(1));
        titleTextView.setText(R.string.weather_title);
        dateTextView.setText(dayName + ", " + dateFormat);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.refresh).fit().centerInside().into(refreshImageView);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.yahoo_logo).fit().centerInside().into(yahooImageView);
    }

    private void setCurrentLayout(){
        getCurrentResources();
        conditionTextView.setText(conditionStringId);
        Picasso.with(activity.getApplicationContext()).load(conditionDrawableId).into(conditionImageView);
        temperatureTextView.setText(temperature + "\u00B0");


        Picasso.with(activity.getApplicationContext()).load(R.drawable.arrow).into(highTemperatureImageView);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.arrow).rotate(180f).into(lowTemperatureImageView);
        highTemperatureTextView.setText(forecastHighTemperature[0] + "\u00B0");
        lowTemperatureTextView.setText(forecastLowTemperature[0] + "\u00B0");
        chillTitleTextView.setText(activity.getResources().getString(R.string.weather_chill) + ": ");
        chillTextView.setText(chill + "\u00B0");
    }

    private void setSunPathLayout(final double scale, final boolean isDay, final long currentDiffMinutes, final long sunsetSunriseDiffMinutes){
        ViewTreeObserver treeObserver = sunPathLayout.getViewTreeObserver();
        treeObserver.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            public boolean onPreDraw() {
                sunPathLayout.getViewTreeObserver().removeOnPreDrawListener(this);
                layoutHeight = sunPathLayout.getMeasuredHeight();
                layoutWidth = sunPathLayout.getMeasuredWidth();
                if(isDay==true)Picasso.with(activity.getApplicationContext()).load(R.drawable.sun_icon).resize((int) (layoutHeight * scale), (int) (layoutHeight * scale)).into(sunPathObjectImageView);
                else Picasso.with(activity.getApplicationContext()).load(R.drawable.moon_icon).resize((int) (layoutHeight * scale), (int) (layoutHeight * scale)).into(sunPathObjectImageView);
                Picasso.with(activity.getApplicationContext()).load(R.drawable.weather_line).transform(new ColorTransformation(activity.getResources().getColor(R.color.divider))).resize((int) (layoutWidth - layoutHeight * scale), (int) (layoutHeight)).into(sunPathBackgroudImageView);
                Picasso.with(activity.getApplicationContext()).load(R.drawable.weather_small_circle).resize((int) (layoutHeight * scale*0.15), (int) (layoutHeight * scale*0.15)).into(sunPathLeftCircleImageView);
                Picasso.with(activity.getApplicationContext()).load(R.drawable.weather_small_circle).resize((int) (layoutHeight * scale * 0.15), (int) (layoutHeight * scale * 0.15)).into(sunPathRightCircleImageView);
                imageTranslation = (int)((currentDiffMinutes *(layoutWidth-(layoutHeight * scale))/sunsetSunriseDiffMinutes));
                circleTranslation = (int)(layoutHeight * scale/2);
                Log.d("przesuniecie", "przesuniecie: " + imageTranslation);
                Log.d("przesuniecie", "szerokosc: " + layoutWidth);
                sunPathObjectImageView.setTranslationX(imageTranslation);
                sunPathRightCircleImageView.setTranslationX(-circleTranslation);
                sunPathLeftCircleImageView.setTranslationX(circleTranslation);
                return true;
            }
        });
    }
    private void setDetailsLayout(){
        getDetailsResources();
        SimpleDateFormat outputFormat= new SimpleDateFormat("HH:mm");
        final double scale=0.5;
        try{
            sunriseHour= outputFormat.parse(sunrise);
            sunsetHour= outputFormat.parse(sunset);
            hourFormat=DateFormat.format("HH:mm", calendar);
            String hourString=hourFormat.toString();
            now=outputFormat.parse(hourString);
            Log.d("hour", "setDetailsLayout: "+hourString);
        }catch(ParseException pe){
            pe.printStackTrace();
        }
        if((now.after(sunriseHour)&&now.before(sunsetHour))||now.equals(sunriseHour)||now.equals(sunsetHour)){
            Picasso.with(activity.getApplicationContext()).load(R.drawable.sunrise_icon).fit().centerInside().into(sunsetSunriseLeftImageView);
            Picasso.with(activity.getApplicationContext()).load(R.drawable.sunset_icon).fit().centerInside().into(sunsetSunriseRightImageView);
            sunsetSunriseLeftTextView.setText(sunrise);
            sunsetSunriseRightTextView.setText(sunset);
            long sunsetSunriseDifference = Math.abs(sunsetHour.getTime() - sunriseHour.getTime());
            long currentDifference= Math.abs(now.getTime()-sunriseHour.getTime());
            final long sunsetSunriseDiffMinutes = sunsetSunriseDifference / (60 * 1000);
            final long currentDiffMinutes = currentDifference / (60 * 1000);
            Log.d("difference", "roznica: " + currentDiffMinutes);
            setSunPathLayout(scale,true,currentDiffMinutes,sunsetSunriseDiffMinutes);
        }
        else {
            Picasso.with(activity.getApplicationContext()).load(R.drawable.sunrise_icon).fit().centerInside().into(sunsetSunriseRightImageView);
            Picasso.with(activity.getApplicationContext()).load(R.drawable.sunset_icon).fit().centerInside().into(sunsetSunriseLeftImageView);
            sunsetSunriseLeftTextView.setText(sunset);
            sunsetSunriseRightTextView.setText(sunrise);
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
                Log.d("difference", "sunrise_current_difference roznica: " + sunsetSunriseDifference);
                final long sunsetSunriseDiffMinutes = sunsetSunriseDifference / (60 * 1000);
                final long currentDiffMinutes = currentDifference / (60 * 1000);
                Log.d("difference", "roznica: sunrise_current_diffMinutes " + currentDiffMinutes);
                setSunPathLayout(scale,false,currentDiffMinutes,sunsetSunriseDiffMinutes);
            }
            else {
                long currentDifference= Math.abs(now.getTime() - sunsetHour.getTime());
                Log.d("difference", "sunrise_current_difference roznica: " + sunsetSunriseDifference);
                final long sunsetSunriseDiffMinutes = sunsetSunriseDifference / (60 * 1000);
                final long currentDiffMinutes = currentDifference / (60 * 1000);
                Log.d("difference", "roznica: sunrise_current_diffMinutes " + currentDiffMinutes);
                setSunPathLayout(scale,false,currentDiffMinutes,sunsetSunriseDiffMinutes);
            }
        }
        Picasso.with(activity.getApplicationContext()).load(R.drawable.direction_icon).rotate(Float.parseFloat(direction)).fit().centerInside().into(directionImageView);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.north_letter_icon).fit().centerInside().into(directionNorthImageView);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.speed_icon).fit().centerInside().into(speedImageView);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.humidity_icon).fit().centerInside().into(humidityImageView);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.pressure_icon).fit().centerInside().into(pressureImageView);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.visibility_icon).fit().centerInside().into(visibilityImageView);
        directionTextView.setText(directionName);
        speedTextView.setText(speed + " km/h");
        humidityTextView.setText(humidity + " %");
        pressureTextView.setText(pressure+" hPa");
        visibilityTextView.setText(visibility+" km");
    }

    private void setForecastLayout(){
        getForecastResouces();
        dayName1 =activity.getResources().getString(R.string.weather_tomorrow_name);
        calendar.add(Calendar.DATE,2);
        dayFormat = DateFormat.format("EEEE", calendar);
        dayName2 =new String(dayFormat.toString().substring(0,1).toUpperCase()+ dayFormat.toString().substring(1));
        calendar.add(Calendar.DATE,1);
        dayFormat = DateFormat.format("EEEE", calendar);
        dayName3 =new String(dayFormat.toString().substring(0,1).toUpperCase()+ dayFormat.toString().substring(1));
        calendar.add(Calendar.DATE,1);
        dayFormat = DateFormat.format("EEEE", calendar);
        dayName4 =new String(dayFormat.toString().substring(0,1).toUpperCase()+ dayFormat.toString().substring(1));
        forecastDayName1TextView.setText(dayName1);
        forecastDayName2TextView.setText(dayName2);
        forecastDayName3TextView.setText(dayName3);
        forecastDayName4TextView.setText(dayName4);
        forecastHighTemperatureDay1TextView.setText(forecastHighTemperature[1] + "\u00B0");
        forecastHighTemperatureDay2TextView.setText(forecastHighTemperature[2] + "\u00B0");
        forecastHighTemperatureDay3TextView.setText(forecastHighTemperature[3] + "\u00B0");
        forecastHighTemperatureDay4TextView.setText(forecastHighTemperature[4] + "\u00B0");
        forecastLowTemperatureDay1TextView.setText(forecastLowTemperature[1] + "\u00B0");
        forecastLowTemperatureDay2TextView.setText(forecastLowTemperature[2] + "\u00B0");
        forecastLowTemperatureDay3TextView.setText(forecastLowTemperature[3] + "\u00B0");
        forecastLowTemperatureDay4TextView.setText(forecastLowTemperature[4] + "\u00B0");
        Picasso.with(activity.getApplicationContext()).load(forecastDay1Drawable).into(forecastDayName1ImageView);
        Picasso.with(activity.getApplicationContext()).load(forecastDay2Drawable).into(forecastDayName2ImageView);
        Picasso.with(activity.getApplicationContext()).load(forecastDay3Drawable).into(forecastDayName3ImageView);
        Picasso.with(activity.getApplicationContext()).load(forecastDay4Drawable).into(forecastDayName4ImageView);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.arrow).rotate(180f).into(forecastLowTemperatureDay1ImageView);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.arrow).rotate(180f).into(forecastLowTemperatureDay2ImageView);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.arrow).rotate(180f).into(forecastLowTemperatureDay3ImageView);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.arrow).rotate(180f).into(forecastLowTemperatureDay4ImageView);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.arrow).into(forecastHighTemperatureDay1ImageView);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.arrow).into(forecastHighTemperatureDay2ImageView);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.arrow).into(forecastHighTemperatureDay3ImageView);
        Picasso.with(activity.getApplicationContext()).load(R.drawable.arrow).into(forecastHighTemperatureDay4ImageView);
    }

    void getData(){
            chill = getter.getChill();
            direction=getter.getDirection();
            directionName =getter.getDirection_name();
            speed=getter.getSpeed();
            humidity=getter.getHumidity();
            pressure=getter.getPressure();
            visibility=getter.getVisibility();
            sunrise=getter.getSunrise();
            sunset=getter.getSunset();
            code=getter.getCode();
            temperature=getter.getTemperature();
            forecastCode =getter.getForecast_code();
            forecastHighTemperature =getter.getForecast_high();
            forecastLowTemperature =getter.getForecast_low();
    }

    private void getAppBarResources(){
        titleTextView =(TextView)activity.findViewById(R.id.app_bar_title_text);
        dateTextView =(TextView)activity.findViewById(R.id.app_bar_date_text);
        refreshImageView=(ImageView)activity.findViewById(R.id.refresh_image);
        yahooImageView=(ImageView)activity.findViewById(R.id.yahoo_image);
    }

    void getCurrentResources(){
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
    }

    void getDetailsResources(){
        sunsetSunriseLeftImageView =(ImageView)activity.findViewById(R.id.sunrise_sunset_left_image);
        sunsetSunriseRightImageView =(ImageView)activity.findViewById(R.id.sunrise_sunset_right_image);
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
        visibilityImageView =(ImageView)activity.findViewById(R.id.visibility_image);
        directionTextView =(TextView)activity.findViewById(R.id.direction_text);
        speedTextView =(TextView)activity.findViewById(R.id.speed_text);
        humidityTextView =(TextView)activity.findViewById(R.id.humidity_text);
        pressureTextView=(TextView)activity.findViewById(R.id.pressure_text);
        visibilityTextView =(TextView)activity.findViewById(R.id.visibility_text);
    }

    void getForecastResouces(){
        forecastDay1Drawable=activity.getResources().getIdentifier("drawable/icon_" + forecastCode[1], null, activity.getPackageName());
        forecastDay2Drawable=activity.getResources().getIdentifier("drawable/icon_" + forecastCode[2], null, activity.getPackageName());
        forecastDay3Drawable=activity.getResources().getIdentifier("drawable/icon_" + forecastCode[3], null, activity.getPackageName());
        forecastDay4Drawable=activity.getResources().getIdentifier("drawable/icon_" + forecastCode[4], null, activity.getPackageName());
        forecastDayName1TextView =(TextView)activity.findViewById(R.id.forecast_day1_name);
        forecastDayName2TextView =(TextView)activity.findViewById(R.id.forecast_day2_name_text);
        forecastDayName3TextView =(TextView)activity.findViewById(R.id.forecast_day3_name_text);
        forecastDayName4TextView =(TextView)activity.findViewById(R.id.forecast_day4_name_text);
        forecastDayName1ImageView =(ImageView)activity.findViewById(R.id.forecast_day1_conditions_image);
        forecastDayName2ImageView =(ImageView)activity.findViewById(R.id.forecast_day2_conditions_image);
        forecastDayName3ImageView =(ImageView)activity.findViewById(R.id.forecast_day3_coditions_image);
        forecastDayName4ImageView =(ImageView)activity.findViewById(R.id.forecast_day4_conditions_image);
        forecastHighTemperatureDay1ImageView =(ImageView)activity.findViewById(R.id.forecast_day1_high_temperature_image);
        forecastHighTemperatureDay2ImageView =(ImageView)activity.findViewById(R.id.forecast_day2_high_temperature_image);
        forecastHighTemperatureDay3ImageView =(ImageView)activity.findViewById(R.id.forecast_day3_high_temperature_image);
        forecastHighTemperatureDay4ImageView =(ImageView)activity.findViewById(R.id.forecast_day4_high_temperature_image);
        forecastLowTemperatureDay1ImageView =(ImageView)activity.findViewById(R.id.forecast_day1_low_temperature_image);
        forecastLowTemperatureDay2ImageView =(ImageView)activity.findViewById(R.id.forecast_day2_low_temperature_image);
        forecastLowTemperatureDay3ImageView =(ImageView)activity.findViewById(R.id.forecast_day3_low_temperature_image);
        forecastLowTemperatureDay4ImageView =(ImageView)activity.findViewById(R.id.forecast_day4_low_temperature_image);
        forecastHighTemperatureDay1TextView =(TextView)activity.findViewById(R.id.forecast_day1_high_temperature_text);
        forecastHighTemperatureDay2TextView =(TextView)activity.findViewById(R.id.forecast_day2_high_temperature_text);
        forecastHighTemperatureDay3TextView =(TextView)activity.findViewById(R.id.forecast_day3_high_temperature_text);
        forecastHighTemperatureDay4TextView =(TextView)activity.findViewById(R.id.forecast_day4_high_temperature_text);
        forecastLowTemperatureDay1TextView =(TextView)activity.findViewById(R.id.forecast_day1_low_temperature_text);
        forecastLowTemperatureDay2TextView =(TextView)activity.findViewById(R.id.forecast_day2_low_temperature_text);
        forecastLowTemperatureDay3TextView =(TextView)activity.findViewById(R.id.forecast_day3_low_temperature_text);
        forecastLowTemperatureDay4TextView =(TextView)activity.findViewById(R.id.forecast_day4_low_temperature_text);
    }
}
