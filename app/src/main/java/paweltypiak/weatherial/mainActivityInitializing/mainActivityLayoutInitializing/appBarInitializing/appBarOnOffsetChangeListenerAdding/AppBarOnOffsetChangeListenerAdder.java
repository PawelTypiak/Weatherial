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
package paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.appBarInitializing.appBarOnOffsetChangeListenerAdding;

import android.app.Activity;
import android.support.design.widget.AppBarLayout;

import paweltypiak.weatherial.R;

public class AppBarOnOffsetChangeListenerAdder {

    private int previousVerticalOffset;
    private ToolbarTitleClickableViewSizeUpdater toolbarTitleClickableViewSizeUpdater;

    public AppBarOnOffsetChangeListenerAdder(Activity activity){
        addAppBarOnOffsetChangeListener(activity);
    }

    private void addAppBarOnOffsetChangeListener(final Activity activity){
        AppBarLayout appBarLayout=(AppBarLayout)activity.findViewById(R.id.app_bar_layout);
        toolbarTitleClickableViewSizeUpdater
                =new ToolbarTitleClickableViewSizeUpdater(activity);
        final ToolbarViewsTransparencyUpdater appBarViewsTransparencyUpdater
                =new ToolbarViewsTransparencyUpdater(activity);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if(isVerticalOffsetChanged(verticalOffset)==true){
                    float scrollPercentage=getScrollPercentage(appBarLayout,verticalOffset);
                    toolbarTitleClickableViewSizeUpdater.updateToolbarTitleSize(scrollPercentage);
                    appBarViewsTransparencyUpdater.updateViewsTransparencyOnAppBarOffsetChanged(scrollPercentage);
                }
            }
        });
    }

    private float getScrollPercentage(AppBarLayout appBarLayout,int verticalOffset){
        int totalScrollRange=appBarLayout.getTotalScrollRange();
        float scrollPercentage = ((float)Math.abs(verticalOffset)/ totalScrollRange);
        return scrollPercentage;
    }

    private boolean isVerticalOffsetChanged(int verticalOffset){
        if(verticalOffset!=previousVerticalOffset){
            previousVerticalOffset=verticalOffset;
            return true;
        }
        else{
            return false;
        }
    }

    public ToolbarTitleClickableViewSizeUpdater getToolbarTitleClickableViewSizeUpdater() {
        return toolbarTitleClickableViewSizeUpdater;
    }
}
