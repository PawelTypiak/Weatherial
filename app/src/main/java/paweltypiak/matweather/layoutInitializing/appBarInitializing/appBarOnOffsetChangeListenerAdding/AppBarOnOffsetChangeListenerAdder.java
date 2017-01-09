package paweltypiak.matweather.layoutInitializing.appBarInitializing.appBarOnOffsetChangeListenerAdding;

import android.app.Activity;
import android.support.design.widget.AppBarLayout;

import paweltypiak.matweather.R;

public class AppBarOnOffsetChangeListenerAdder {

    private int previousVerticalOffset;

    public AppBarOnOffsetChangeListenerAdder(Activity activity){
        addAppBarOnOffsetChangeListener(activity);
    }

    private void addAppBarOnOffsetChangeListener(final Activity activity){
        AppBarLayout appBarLayout=(AppBarLayout)activity.findViewById(R.id.app_bar_layout);
        final ToolbarTitleClickableViewSizeUpdater toolbarTitleClickableViewSizeUpdater
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
}
