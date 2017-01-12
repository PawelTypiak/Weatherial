package paweltypiak.matweather.mainActivityLayoutInitializing.WeatherLayoutInitializing.swipeRefreshLayoutInitializing.onRefreshInitializing;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.widget.LinearLayout;
import paweltypiak.matweather.MainActivity;
import paweltypiak.matweather.mainActivityLayoutInitializing.MainActivityLayoutInitializer;
import paweltypiak.matweather.usefulClasses.DialogInitializer;
import paweltypiak.matweather.weatherDataDownloading.WeatherDataParser;
public class OnRefreshInitializer implements SwipeRefreshLayout.OnRefreshListener{

    private Activity activity;
    private DialogInitializer dialogInitializer;
    private MainActivityLayoutInitializer mainActivityLayoutInitializer;
    private SwipeRefreshLayout swipeRefreshLayout;
    private OnRefreshListener onRefreshListener;
    private AlertDialog onRefreshWeatherInternetFailureDialog;
    private AlertDialog onRefreshWeatherServiceFailureDialog;
    private OnRefreshLayoutAnimator layoutAnimator;


    public OnRefreshInitializer(Activity activity,
                                DialogInitializer dialogInitializer,
                                MainActivityLayoutInitializer mainActivityLayoutInitializer,
                                SwipeRefreshLayout swipeRefreshLayout,
                                LinearLayout onRefreshMessageLayout
                                ){
        this.activity=activity;
        this.dialogInitializer=dialogInitializer;
        this.swipeRefreshLayout=swipeRefreshLayout;
        this.mainActivityLayoutInitializer=mainActivityLayoutInitializer;
        swipeRefreshLayout.setOnRefreshListener(this);
        layoutAnimator=new OnRefreshLayoutAnimator(activity,onRefreshMessageLayout,mainActivityLayoutInitializer);
    }

    @Override
    public void onRefresh() {
        Log.d("refresh", "refresh ");
        //refreshing weather
        mainActivityLayoutInitializer.
                getWeatherLayoutInitializer().
                getSwipeRefreshLayoutInitializer().
                getPullListenersInitializer().
                setNestedScrollViewScrollingDisabled();
        swipeRefreshLayout.setRefreshing(true);
        layoutAnimator.fadeOutWeatherLayout();
        layoutAnimator.fadeInOnRefreshMessageLayout();
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
        layoutAnimator.fadeOutOnRefreshMessageLayout();
        mainActivityLayoutInitializer.updateLayoutOnWeatherDataChange(activity, weatherDataParser,false,false);
    }

    public void onWeatherFailureAfterRefresh(int errorCode){
        swipeRefreshLayout.setRefreshing(false);
        layoutAnimator.fadeOutOnRefreshMessageLayout();
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
            setWeatherLayoutVisibleAfterRefreshFailure();
        }
    };

    private void setWeatherLayoutVisibleAfterRefreshFailure(){
        layoutAnimator.fadeOutOnRefreshMessageLayout();
        layoutAnimator.fadeInWeatherLayout();
    }
}

