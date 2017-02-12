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
package paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.weatherLayoutInitializing.swipeRefreshLayoutInitializing;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.LinearLayout;
import paweltypiak.weatherial.MainActivity;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.InternetFailureDialogInitializer;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.ServiceFailureDialogInitializer;
import paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.layoutUpdating.OnWeatherDataChangeLayoutUpdater;
import paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.MainActivityLayoutInitializer;
import paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.weatherLayoutInitializing.WeatherLayoutInitializer;
import paweltypiak.weatherial.dataDownloading.weatherDataDownloading.WeatherDataParser;

public class OnRefreshInitializer implements SwipeRefreshLayout.OnRefreshListener{

    private Activity activity;
    private MainActivityLayoutInitializer mainActivityLayoutInitializer;
    private WeatherLayoutInitializer weatherLayoutInitializer;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout onRefreshMessageLayout;
    private AlertDialog onRefreshWeatherInternetFailureDialog;
    private AlertDialog onRefreshWeatherServiceFailureDialog;

    public OnRefreshInitializer(Activity activity,
                                MainActivityLayoutInitializer mainActivityLayoutInitializer,
                                WeatherLayoutInitializer weatherLayoutInitializer,
                                SwipeRefreshLayout swipeRefreshLayout,
                                LinearLayout onRefreshMessageLayout
                                ){
        this.activity=activity;
        this.mainActivityLayoutInitializer=mainActivityLayoutInitializer;
        this.weatherLayoutInitializer=weatherLayoutInitializer;
        this.swipeRefreshLayout=swipeRefreshLayout;
        this.onRefreshMessageLayout=onRefreshMessageLayout;
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        //refreshing weather
        swipeRefreshLayout.setRefreshing(true);
        fadeOutWeatherLayout();
        fadeInOnRefreshMessageLayout();
        downloadWeatherDataOnRefresh();
    }

    private void downloadWeatherDataOnRefresh(){
        ((MainActivity)activity).
                getMainActivityDataDownloader().
                getWeatherDataDownloader().
                initializeWeatherDataDownloading(1,getCurrentLocationAddress());
    }

    private String getCurrentLocationAddress(){
        String city= OnWeatherDataChangeLayoutUpdater.getCurrentWeatherDataParser().getCity();
        String region=OnWeatherDataChangeLayoutUpdater.getCurrentWeatherDataParser().getRegion();
        String country=OnWeatherDataChangeLayoutUpdater.getCurrentWeatherDataParser().getCountry();
        String currentLocationAddressString=city+", "+region+", "+ country;
        return currentLocationAddressString;
    }

    public void onWeatherSuccessAfterRefresh(Activity activity,WeatherDataParser weatherDataParser){
        swipeRefreshLayout.setRefreshing(false);
        fadeOutOnRefreshMessageLayout();
        mainActivityLayoutInitializer.updateLayoutOnWeatherDataChange(activity, weatherDataParser,false,false);
    }

    public void onWeatherFailureAfterRefresh(int errorCode){
        swipeRefreshLayout.setRefreshing(false);
        fadeOutOnRefreshMessageLayout();
        if(errorCode==0) {
            showOnRefreshWeatherInternetFailureDialog();
        }
        else if(errorCode==1){
            showOnRefreshWeatherServiceFailureDialog();
        }
    }

    private void showOnRefreshWeatherInternetFailureDialog(){
        if(onRefreshWeatherInternetFailureDialog==null){
            onRefreshWeatherInternetFailureDialog
                    =InternetFailureDialogInitializer.getInternetFailureDialog(activity,
                    1,
                    refreshWeatherRunnable,
                    setWeatherLayoutVisibleAfterRefreshFailureRunnable);
        }
        onRefreshWeatherInternetFailureDialog.show();
    }

    private void showOnRefreshWeatherServiceFailureDialog(){
        if(onRefreshWeatherServiceFailureDialog==null){
            onRefreshWeatherServiceFailureDialog=ServiceFailureDialogInitializer.getServiceFailureDialog(activity,
                    1,
                    refreshWeatherRunnable,
                    setWeatherLayoutVisibleAfterRefreshFailureRunnable);
        }
        onRefreshWeatherServiceFailureDialog.show();
    }

    private Runnable refreshWeatherRunnable = new Runnable() {
        public void run() {
            onRefresh();
        }
    };

    private Runnable setWeatherLayoutVisibleAfterRefreshFailureRunnable = new Runnable() {
        public void run() {
            fadeOutOnRefreshMessageLayout();
            fadeInWeatherLayout();
        }
    };

    private void fadeInOnRefreshMessageLayout(){
        onRefreshMessageLayout.setVisibility(View.VISIBLE);
        long transitionTime=100;
        onRefreshMessageLayout.animate()
                .alpha(1f)
                .setDuration(transitionTime)
                .setListener(null);
    }

    private void fadeOutOnRefreshMessageLayout(){
        long transitionTime=100;
        onRefreshMessageLayout.animate()
                .alpha(0f)
                .setDuration(transitionTime)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        onRefreshMessageLayout.setVisibility(View.GONE);
                    }
                });
    }

    private void fadeInWeatherLayout(){
        weatherLayoutInitializer.getGeneralWeatherLayoutInitializer().fadeInWeatherLayout(null);
    }

    private void fadeOutWeatherLayout(){
        weatherLayoutInitializer.getGeneralWeatherLayoutInitializer().fadeOutWeatherLayout(null);
    }
}

