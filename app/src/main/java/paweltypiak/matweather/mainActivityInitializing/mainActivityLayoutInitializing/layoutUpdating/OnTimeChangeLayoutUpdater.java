package paweltypiak.matweather.mainActivityInitializing.mainActivityLayoutInitializing.layoutUpdating;

import android.app.Activity;
import android.text.format.DateFormat;
import android.util.Log;
import java.util.Calendar;
import paweltypiak.matweather.mainActivityInitializing.mainActivityLayoutInitializing.MainActivityLayoutInitializer;
import paweltypiak.matweather.usefulClasses.SharedPreferencesModifier;
import paweltypiak.matweather.dataDownloading.weatherDataDownloading.WeatherDataFormatter;

public class OnTimeChangeLayoutUpdater {

    private Thread uiThread;
    private boolean isTimeThreadWorking;
    private Activity activity;
    private MainActivityLayoutInitializer mainActivityLayoutInitializer;
    private long currentDiffMinutes;
    private long sunsetSunriseDiffMinutes;
    private boolean isDay;
    private String currentDiffMinutesString;
    private String isDayString;
    private WeatherDataFormatter weatherDataFormatter;
    private boolean newWeatherUpdate;

    public OnTimeChangeLayoutUpdater(Activity activity,MainActivityLayoutInitializer mainActivityLayoutInitializer){
        initializeUiThread(activity);
        this.activity=activity;
        this.mainActivityLayoutInitializer = mainActivityLayoutInitializer;
    }

    public void onWeatherDataChangeUpdate(WeatherDataFormatter weatherDataFormatter){
        this.weatherDataFormatter=weatherDataFormatter;
        isTimeThreadWorking =true;
        newWeatherUpdate =true;
    }

    private void initializeUiThread(final Activity activity) {
        //initialize new thread for updating UI every second
        isTimeThreadWorking =false;
        uiThread = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (isTimeThreadWorking == true) {
                                    onTimeUpdate();
                                }
                            }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        uiThread.start();
        Log.d("uithread", "start");
    }

    private void onTimeUpdate(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR,weatherDataFormatter.getHourDifference());
        initializeLayoutUpdateOnTimeChange(calendar);
    }

    private void initializeLayoutUpdateOnTimeChange(Calendar calendar){
        //update sun position every minute
        updateToolbarTimeTextView(calendar);
        updateWeatherLayout(calendar);
    }

    private void updateToolbarTimeTextView(Calendar calendar){
        //update timeTextView text every second
        String outputFormat=getTimeOutputFormat();
        mainActivityLayoutInitializer.getAppBarLayoutInitializer().
                getAppBarLayoutDataInitializer().
                updateCurrentTime(DateFormat.format(outputFormat, calendar));
    }

    private String getTimeOutputFormat(){
        // TODO: unit in weatherInitializer, passing only calendar
        String outputFormat;
        int[] units= SharedPreferencesModifier.getUnits(activity);
        if(units[3]==0) outputFormat="HH:mm:ss";
        else outputFormat="hh:mm:ss a";
        return outputFormat;
    }

    private void updateWeatherLayout(Calendar calendar){
        String outputMinutesFormat="H:mm";
        String outputMinutesString=DateFormat.format(outputMinutesFormat, calendar).toString();
        String[] sunPositionStrings=weatherDataFormatter.countSunPosition(outputMinutesString);
        if(newWeatherUpdate==true){
            updateSunPostionData(sunPositionStrings);
            newWeatherUpdate =false;
        }
        else{
            if(!currentDiffMinutesString.equals(sunPositionStrings[1])){
                if(!isDayString.equals(sunPositionStrings[2])){
                    Log.d("data setter", "change time of day");
                    changeTimeOfDay();
                }
                updateSunPostionData(sunPositionStrings);
                updateSunPathProgress();
            }
        }
    }

    private void updateSunPostionData(String[] sunPositionStrings){
        currentDiffMinutesString=sunPositionStrings[1];
        isDayString=sunPositionStrings[2];
        sunsetSunriseDiffMinutes=Long.parseLong(sunPositionStrings[0]);
        currentDiffMinutes=Long.parseLong(sunPositionStrings[1]);
        isDay=(Integer.parseInt(sunPositionStrings[1])) != 0;
    }

    private void changeTimeOfDay(){
        //change time of  day if sunrise or sunset
        isDay=!isDay;
        updateWeatherLayoutTheme();
    }

    private void updateWeatherLayoutTheme(){
        mainActivityLayoutInitializer.getWeatherLayoutInitializer().updateWeatherLayoutTheme(isDay);
    }

    private void updateSunPathProgress(){
        mainActivityLayoutInitializer.
                getWeatherLayoutInitializer().
                getWeatherDetailsLayoutInitializer().
                updateSunPathProgress(activity,currentDiffMinutes,sunsetSunriseDiffMinutes);
    }

    public void resumeUiThread(){
        //start threat for updating UI
        isTimeThreadWorking =true;
    }

    public void pauseUiThread(){
        isTimeThreadWorking =false;
    }

    public void interruptUiThread(){
        uiThread.interrupt();
    }
}
