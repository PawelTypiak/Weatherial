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

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.MainActivityLayoutInitializer;
import paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.weatherLayoutInitializing.WeatherLayoutInitializer;

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
        int swipeRefreshLayoutOffset = (int)activity.getResources().getDimension(R.dimen.swipe_refresh_layout_offset);
        int toolbarExpandedHeight=
                mainActivityLayoutInitializer.
                getAppBarLayoutInitializer().
                getAppBarLayoutDimensionsSetter().
                getToolbarExpandedHeight();
        int progressViewStart = toolbarExpandedHeight-swipeRefreshLayoutOffset;
        float pullRange=swipeRefreshLayoutOffset*1.5f;
        int progressViewEnd = (int)(progressViewStart+pullRange);
        swipeRefreshLayout.setProgressViewOffset(true, progressViewStart, progressViewEnd);
    }

    private void initializeOnRefreshMessage(Activity activity){
        onRefreshMessageLayout =(LinearLayout)activity.findViewById(R.id.main_content_layout_on_refresh_message_layout);
        float onRefreshMessageLayoutTopMargin=activity.getResources().getDimension(R.dimen.on_refresh_message_layout_top_margin);
        int swipeRefreshLayoutOffset = (int)activity.getResources().getDimension(R.dimen.swipe_refresh_layout_offset);
        RelativeLayout.LayoutParams onRefreshMessageLayoutParams=(RelativeLayout.LayoutParams)onRefreshMessageLayout.getLayoutParams();
        onRefreshMessageLayoutParams.topMargin=(int)(swipeRefreshLayoutOffset+onRefreshMessageLayoutTopMargin);
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
