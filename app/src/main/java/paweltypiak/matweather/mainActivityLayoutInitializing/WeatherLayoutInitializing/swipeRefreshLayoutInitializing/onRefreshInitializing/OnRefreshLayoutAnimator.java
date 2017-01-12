package paweltypiak.matweather.mainActivityLayoutInitializing.WeatherLayoutInitializing.swipeRefreshLayoutInitializing.onRefreshInitializing;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.LinearLayout;

import paweltypiak.matweather.R;
import paweltypiak.matweather.mainActivityLayoutInitializing.MainActivityLayoutInitializer;
import paweltypiak.matweather.mainActivityLayoutInitializing.WeatherLayoutInitializing.swipeRefreshLayoutInitializing.OnSwipeRefreshLayoutPullListenersInitializer;
import paweltypiak.matweather.mainActivityLayoutInitializing.WeatherLayoutInitializing.swipeRefreshLayoutInitializing.SwipeRefreshLayoutInitializer;

public class OnRefreshLayoutAnimator {

    private MainActivityLayoutInitializer mainActivityLayoutInitializer;
    private LinearLayout weatherLayout;
    private LinearLayout onRefreshMessageLayout;

    public OnRefreshLayoutAnimator(Activity activity,
                                   LinearLayout onRefreshMessageLayout,
                                   MainActivityLayoutInitializer mainActivityLayoutInitializer){
        weatherLayout=(LinearLayout)activity.findViewById(R.id.weather_inner_layout);
        this.onRefreshMessageLayout=onRefreshMessageLayout;
        this.mainActivityLayoutInitializer=mainActivityLayoutInitializer;
    }


    public void fadeInWeatherLayout(){
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
                    }
                });
    }

    public void fadeOutWeatherLayout(){
        long transitionTime=100;
        if(weatherLayout.getVisibility()==View.VISIBLE){
            weatherLayout.animate()
                    .alpha(0f)
                    .setDuration(transitionTime)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            weatherLayout.setVisibility(View.INVISIBLE);
                        }
                    });
        }
    }

    public void fadeInOnRefreshMessageLayout(){
        onRefreshMessageLayout.setVisibility(View.VISIBLE);
        long transitionTime=100;
        onRefreshMessageLayout.animate()
                .alpha(1f)
                .setDuration(transitionTime)
                .setListener(null);
    }

    public void fadeOutOnRefreshMessageLayout(){
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
}


