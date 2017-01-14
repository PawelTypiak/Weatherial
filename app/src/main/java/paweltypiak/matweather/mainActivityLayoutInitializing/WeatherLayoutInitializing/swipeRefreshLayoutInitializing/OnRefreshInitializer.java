package paweltypiak.matweather.mainActivityLayoutInitializing.WeatherLayoutInitializing.swipeRefreshLayoutInitializing;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.AlertDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import paweltypiak.matweather.MainActivity;
import paweltypiak.matweather.mainActivityLayoutInitializing.MainActivityLayoutInitializer;
import paweltypiak.matweather.mainActivityLayoutInitializing.WeatherLayoutInitializing.WeatherLayoutInitializer;
import paweltypiak.matweather.usefulClasses.DialogInitializer;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataParser;
public class OnRefreshInitializer implements SwipeRefreshLayout.OnRefreshListener{

    private Activity activity;
    private DialogInitializer dialogInitializer;
    private MainActivityLayoutInitializer mainActivityLayoutInitializer;
    private WeatherLayoutInitializer weatherLayoutInitializer;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout onRefreshMessageLayout;
    private OnRefreshListener onRefreshListener;
    private AlertDialog onRefreshWeatherInternetFailureDialog;
    private AlertDialog onRefreshWeatherServiceFailureDialog;

    public OnRefreshInitializer(Activity activity,
                                DialogInitializer dialogInitializer,
                                MainActivityLayoutInitializer mainActivityLayoutInitializer,
                                WeatherLayoutInitializer weatherLayoutInitializer,
                                SwipeRefreshLayout swipeRefreshLayout,
                                LinearLayout onRefreshMessageLayout
                                ){
        this.activity=activity;
        this.dialogInitializer=dialogInitializer;
        this.mainActivityLayoutInitializer=mainActivityLayoutInitializer;
        this.weatherLayoutInitializer=weatherLayoutInitializer;
        this.swipeRefreshLayout=swipeRefreshLayout;
        this.onRefreshMessageLayout=onRefreshMessageLayout;
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        Log.d("refresh", "refresh ");
        //refreshing weather
        swipeRefreshLayout.setRefreshing(true);
        fadeOutWeatherLayout();
        fadeInOnRefreshMessageLayout();
        callOnRefreshListener();
    }

    private void callOnRefreshListener(){
        if(onRefreshListener==null){
            onRefreshListener=((MainActivity)activity).getOnRefreshListener();
        }
        onRefreshListener.downloadWeatherDataOnRefresh();
    }

    public interface OnRefreshListener {
        void downloadWeatherDataOnRefresh();
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
            onRefreshWeatherInternetFailureDialog =dialogInitializer.initializeInternetFailureDialog(1,refreshWeatherRunnable,setWeatherLayoutVisibleAfterRefreshFailureRunnable);
        }
        onRefreshWeatherInternetFailureDialog.show();
    }

    private void showOnRefreshWeatherServiceFailureDialog(){
        if(onRefreshWeatherServiceFailureDialog==null){
            onRefreshWeatherServiceFailureDialog =dialogInitializer.initializeServiceFailureDialog(1,refreshWeatherRunnable,setWeatherLayoutVisibleAfterRefreshFailureRunnable);
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

