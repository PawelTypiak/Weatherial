/*******************************************************************************
 * Copyright 2017 Paweł Typiak
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.layoutUpdating;

import android.app.Activity;
import android.os.Build;
import android.text.format.DateFormat;
import java.util.Calendar;
import paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.MainActivityLayoutInitializer;
import paweltypiak.weatherial.utils.SharedPreferencesModifier;
import paweltypiak.weatherial.dataDownloading.weatherDataDownloading.WeatherDataFormatter;

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
        String outputFormat;
        int[] units= SharedPreferencesModifier.getUnits(activity);
        if(units[3]==0) {
            if(Build.VERSION.SDK_INT >= 18) {
                outputFormat="HH:mm:ss";

            } else {
                outputFormat="kk:mm:ss";
            }
        }
        else {
            outputFormat="hh:mm:ss a";
        }
        return outputFormat;
    }

    private void updateWeatherLayout(Calendar calendar){
        String outputMinutesFormat;
        if(Build.VERSION.SDK_INT >= 18) {
            outputMinutesFormat="H:mm";

        } else {
            outputMinutesFormat="k:mm";
        }
        String outputMinutesString=DateFormat.format(outputMinutesFormat, calendar).toString();
        String[] sunPositionStrings=weatherDataFormatter.countSunPosition(outputMinutesString);
        if(newWeatherUpdate==true){
            updateSunPositionData(sunPositionStrings);
            newWeatherUpdate =false;
        }
        else{
            if(!currentDiffMinutesString.equals(sunPositionStrings[1])){
                if(!isDayString.equals(sunPositionStrings[2])){
                    changeTimeOfDay();
                }
                updateSunPositionData(sunPositionStrings);
                updateSunPathProgress();
            }
        }
    }

    private void updateSunPositionData(String[] sunPositionStrings){
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
