package paweltypiak.matweather.mainActivityInitializing.mainActivityLayoutInitializing.weatherLayoutInitializing.swipeRefreshLayoutInitializing;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import paweltypiak.matweather.R;
import paweltypiak.matweather.mainActivityInitializing.mainActivityLayoutInitializing.MainActivityLayoutInitializer;
import paweltypiak.matweather.mainActivityInitializing.mainActivityLayoutInitializing.weatherLayoutInitializing.WeatherLayoutInitializer;

public class SwipeRefreshLayoutInitializer {

    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout onRefreshMessageLayout;
    private OnRefreshInitializer onRefreshInitializer;

    public SwipeRefreshLayoutInitializer (Activity activity, MainActivityLayoutInitializer mainActivityLayoutInitializer,
                                          WeatherLayoutInitializer weatherLayoutInitializer){
        swipeRefreshLayout = (SwipeRefreshLayout) activity.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        setSwipeRefreshLayoutOffset(activity,mainActivityLayoutInitializer);
        initializeOnRefreshMessage(activity);
        initializeOnSwipeRefreshPullListeners(activity,weatherLayoutInitializer);
        initializeOnRefresh(activity,mainActivityLayoutInitializer,weatherLayoutInitializer);
    }

    private void setSwipeRefreshLayoutOffset(Activity activity, MainActivityLayoutInitializer mainActivityLayoutInitializer){
        int swipeRefrehLayoutOffset = (int)activity.getResources().getDimension(R.dimen.swipe_refresh_layout_offset);
        int toolbarExpandedHeight=
                mainActivityLayoutInitializer.
                getAppBarLayoutInitializer().
                getAppBarLayoutDimensionsSetter().
                getToolbarExpandedHeight();
        int progressViewStart = toolbarExpandedHeight-swipeRefrehLayoutOffset;
        float pullRange=swipeRefrehLayoutOffset*1.5f;
        int progressViewEnd = (int)(progressViewStart+pullRange);
        swipeRefreshLayout.setProgressViewOffset(true, progressViewStart, progressViewEnd);
    }

    private void initializeOnRefreshMessage(Activity activity){
        onRefreshMessageLayout =(LinearLayout)activity.findViewById(R.id.main_content_layout_on_refresh_message_layout);
        float onRefreshMessageLayoutTopMargin=activity.getResources().getDimension(R.dimen.on_refresh_message_layout_top_margin);
        int swipeRefrehLayoutOffset = (int)activity.getResources().getDimension(R.dimen.swipe_refresh_layout_offset);
        RelativeLayout.LayoutParams onRefreshMessageLayoutParams=(RelativeLayout.LayoutParams)onRefreshMessageLayout.getLayoutParams();
        onRefreshMessageLayoutParams.topMargin=(int)(swipeRefrehLayoutOffset+onRefreshMessageLayoutTopMargin);
        onRefreshMessageLayout.setLayoutParams(onRefreshMessageLayoutParams);
    }

    private void initializeOnSwipeRefreshPullListeners(Activity activity, WeatherLayoutInitializer weatherLayoutInitializer){
        new OnSwipeRefreshLayoutPullListenersInitializer(activity,weatherLayoutInitializer,swipeRefreshLayout);
    }

    private void initializeOnRefresh(Activity activity,MainActivityLayoutInitializer mainActivityLayoutInitializer,
                                     WeatherLayoutInitializer weatherLayoutInitializer){
        onRefreshInitializer=new OnRefreshInitializer(
                activity,
                mainActivityLayoutInitializer,
                weatherLayoutInitializer,
                swipeRefreshLayout,
                onRefreshMessageLayout
                );
    }

    public OnRefreshInitializer getOnRefreshInitializer() {
        return onRefreshInitializer;
    }
}
