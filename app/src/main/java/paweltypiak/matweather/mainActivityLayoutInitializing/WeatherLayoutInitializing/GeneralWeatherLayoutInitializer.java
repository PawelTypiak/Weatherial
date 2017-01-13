package paweltypiak.matweather.mainActivityLayoutInitializing.WeatherLayoutInitializing;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import paweltypiak.matweather.R;
import paweltypiak.matweather.mainActivityLayoutInitializing.LayoutUpdating.OnWeatherDataChangeLayoutUpdater;
import paweltypiak.matweather.mainActivityLayoutInitializing.MainActivityLayoutInitializer;
import paweltypiak.matweather.usefulClasses.DialogInitializer;

public class GeneralWeatherLayoutInitializer {

    private LinearLayout weatherLayout;
    private MainActivityLayoutInitializer mainActivityLayoutInitializer;

    public GeneralWeatherLayoutInitializer(Activity activity, DialogInitializer dialogInitializer, MainActivityLayoutInitializer mainActivityLayoutInitializer){
        this.mainActivityLayoutInitializer=mainActivityLayoutInitializer;
        setWeatherLayoutTopPadding(activity,mainActivityLayoutInitializer);
        setWeatherLayoutOnClickListener(activity,dialogInitializer);
        findWeatherLayoutView(activity);
    }

    private void setWeatherLayoutTopPadding(Activity activity,  MainActivityLayoutInitializer mainActivityLayoutInitializer){
        int toolbarExpandedHeight=mainActivityLayoutInitializer.
                getAppBarLayoutInitializer().
                getAppBarLayoutDimensionsSetter().
                getToolbarExpandedHeight();
        RelativeLayout weatherLayout=(RelativeLayout)activity.findViewById(R.id.weather_layout);
        weatherLayout.setPadding(0,toolbarExpandedHeight,0,0);
    }

    private void setWeatherLayoutOnClickListener(Activity activity,final DialogInitializer dialogInitializer) {
        LinearLayout weatherLayout = (LinearLayout) activity.findViewById(R.id.weather_inner_layout);
        weatherLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String yahooWeatherLink = OnWeatherDataChangeLayoutUpdater.getCurrentDataFormatter().getLink();
                AlertDialog yahooWeatherRedirectDialog = dialogInitializer.initializeYahooRedirectDialog(1,
                        yahooWeatherLink);
                yahooWeatherRedirectDialog.show();
            }
        });
    }

    private void findWeatherLayoutView(Activity activity){
        weatherLayout=(LinearLayout)activity.findViewById(R.id.weather_inner_layout);
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
                        mainActivityLayoutInitializer.
                                getWeatherLayoutInitializer().
                                getSwipeRefreshLayoutInitializer().
                                getPullListenersInitializer().
                                setNestedScrollViewScrollingEnabled();
                        if(runnable!=null){
                            runnable.run();
                        }
                    }
                });
    }

    public void fadeOutWeatherLayout(final Runnable runnable){
        mainActivityLayoutInitializer.
                getWeatherLayoutInitializer().
                getSwipeRefreshLayoutInitializer().
                getPullListenersInitializer().
                setNestedScrollViewScrollingDisabled();
        long transitionTime=100;
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
}
