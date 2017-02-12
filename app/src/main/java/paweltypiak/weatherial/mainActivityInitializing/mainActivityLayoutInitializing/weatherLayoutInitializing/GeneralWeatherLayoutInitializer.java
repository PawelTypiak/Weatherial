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
package paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.weatherLayoutInitializing;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.customViews.LockableSmoothNestedScrollView;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.yahooRedirectDialogInitializing.YahooRedirectDialogInitializer;
import paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.MainActivityLayoutInitializer;

public class GeneralWeatherLayoutInitializer {

    private LinearLayout weatherLayout;
    private LockableSmoothNestedScrollView nestedScrollView;

    public GeneralWeatherLayoutInitializer(Activity activity, MainActivityLayoutInitializer mainActivityLayoutInitializer){
        findViews(activity);
        setWeatherLayoutTopPadding(activity,mainActivityLayoutInitializer);
        setWeatherLayoutOnClickListener(activity,mainActivityLayoutInitializer);
    }

    private void findViews(Activity activity){
        nestedScrollView=(LockableSmoothNestedScrollView)activity.findViewById(R.id.nested_scroll_view);
        weatherLayout=(LinearLayout)activity.findViewById(R.id.weather_inner_layout);
        Log.d("nested", "findViews: ");
    }

    private void setWeatherLayoutTopPadding(Activity activity,  MainActivityLayoutInitializer mainActivityLayoutInitializer){
        int toolbarExpandedHeight=mainActivityLayoutInitializer.
                getAppBarLayoutInitializer().
                getAppBarLayoutDimensionsSetter().
                getToolbarExpandedHeight();
        RelativeLayout weatherLayout=(RelativeLayout)activity.findViewById(R.id.weather_layout);
        weatherLayout.setPadding(0,toolbarExpandedHeight,0,0);
    }

    private void setWeatherLayoutOnClickListener(final Activity activity,
                                                 final MainActivityLayoutInitializer mainActivityLayoutInitializer) {
        weatherLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String yahooWeatherLink = mainActivityLayoutInitializer.getOnWeatherDataChangeLayoutUpdater().getCurrentDataFormatter().getLink();
                AlertDialog yahooWeatherRedirectDialog= YahooRedirectDialogInitializer.getYahooRedirectDialog(
                        activity,
                        1,
                        yahooWeatherLink);
                yahooWeatherRedirectDialog.show();
            }
        });
    }

    public void fadeInWeatherLayout(final Runnable runnable){
        long transitionTime=200;
        weatherLayout.setVisibility(View.VISIBLE);
        weatherLayout.animate()
                .alpha(1f)
                .setDuration(transitionTime)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        setNestedScrollViewEnabled();
                        if(runnable!=null){
                            runnable.run();
                        }
                    }
                });
    }

    public void fadeOutWeatherLayout(final Runnable runnable){
        setNestedScrollViewDisabled();
        long transitionTime=200;
        if(weatherLayout.getVisibility()==View.VISIBLE){
            weatherLayout.animate()
                    .alpha(0f)
                    .setDuration(transitionTime)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            weatherLayout.setVisibility(View.INVISIBLE);
                            if(runnable!=null){
                                runnable.run();
                            }
                        }
                    });
        }
    }

    public LinearLayout getWeatherLayout() {
        return weatherLayout;
    }

    public LockableSmoothNestedScrollView getNestedScrollView() {
        return nestedScrollView;
    }

    private void setNestedScrollViewEnabled(){
        nestedScrollView.setScrollingEnabled(true);
    }

    private void setNestedScrollViewDisabled(){
        nestedScrollView.setScrollingEnabled(false);
    }
}
