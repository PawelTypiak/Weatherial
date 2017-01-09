package paweltypiak.matweather.layoutInitializing.WeatherLayoutInitializing.swipeRefreshLayoutInitializing;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import paweltypiak.matweather.R;
import paweltypiak.matweather.layoutInitializing.WeatherLayoutInitializing.swipeRefreshLayoutInitializing.onRefreshInitializing.OnRefreshInitializer;
import paweltypiak.matweather.layoutInitializing.appBarInitializing.AppBarLayoutDimensionsSetter;
import paweltypiak.matweather.layoutInitializing.appBarInitializing.AppBarLayoutInitializer;
import paweltypiak.matweather.usefulClasses.DialogInitializer;

public class SwipeRefreshLayoutInitializer {

    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout onRefreshMessageLayout;
    private OnSwipeRefreshLayoutPullListenersInitializer pullListenersInitializer;
    private OnRefreshInitializer onRefreshInitializer;

    public SwipeRefreshLayoutInitializer (Activity activity, DialogInitializer dialogInitializer, AppBarLayoutInitializer appBarLayoutInitializer){
        swipeRefreshLayout = (SwipeRefreshLayout) activity.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        setSwipeRefreshLayoutOffset(activity,appBarLayoutInitializer);
        initializeOnRefreshMessage(activity);
        initializeOnSwipeRefreshPullListeners(activity);
        initializeOnRefresh(activity,dialogInitializer);
    }

    private void setSwipeRefreshLayoutOffset(Activity activity,AppBarLayoutInitializer appBarLayoutInitializer){
        int swipeRefrehLayoutOffset = (int)activity.getResources().getDimension(R.dimen.swipe_refresh_layout_offset);
        int toolbarExpandedHeight= appBarLayoutInitializer.getAppBarLayoutDimensionsSetter().getToolbarExpandedHeight();
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

    private void initializeOnSwipeRefreshPullListeners(Activity activity){
        pullListenersInitializer=new OnSwipeRefreshLayoutPullListenersInitializer(activity,swipeRefreshLayout);
    }

    private void initializeOnRefresh(Activity activity,DialogInitializer dialogInitializer){
        onRefreshInitializer=new OnRefreshInitializer(
                activity,
                dialogInitializer,
                swipeRefreshLayout,
                onRefreshMessageLayout,
                pullListenersInitializer);
    }

    public OnSwipeRefreshLayoutPullListenersInitializer getPullListenersInitializer() {
        return pullListenersInitializer;
    }

    public OnRefreshInitializer getOnRefreshInitializer() {
        return onRefreshInitializer;
    }
}
