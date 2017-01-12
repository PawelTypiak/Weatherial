package paweltypiak.matweather.mainActivityLayoutInitializing.appBarInitializing;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.design.widget.AppBarLayout;
import android.widget.RelativeLayout;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import paweltypiak.matweather.R;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

public class AppBarLayoutDimensionsSetter {

    private int toolbarExpandedHeight;

    public AppBarLayoutDimensionsSetter(Activity activity){
        int bottomLayoutHeight= getToolbarBelowTitleLayoutHeight(activity);
        toolbarExpandedHeight = getComputedToolbarExpandedHeight(activity,bottomLayoutHeight);
        setComputedToolbarExpandedHeight(activity,bottomLayoutHeight,toolbarExpandedHeight);
        setWeatherLayoutTopPadding(activity,toolbarExpandedHeight);
    }

    private int getToolbarBelowTitleLayoutHeight(Activity activity){
        int subtitleTextHeight= UsefulFunctions.getTextViewHeight(
                activity,
                "",
                Typeface.DEFAULT,
                (int)activity.getResources().getDimension(R.dimen.toolbar_subtitle_text_size),
                0,
                0,
                0,
                (int)activity.getResources().getDimension(R.dimen.toolbar_subtitle_bottom_margin)
        );
        int yahooLogoImageViewHeight=(int)activity.getResources().getDimension(R.dimen.yahoo_logo_image_height);
        int yahooLogoLayoutVerticalPadding=(int)activity.getResources().getDimension(R.dimen.yahoo_logo_padding);
        int yahooLogoHeight=yahooLogoImageViewHeight+2*yahooLogoLayoutVerticalPadding;
        int layoutHeight=subtitleTextHeight+yahooLogoHeight;
        return layoutHeight;
    }

    private int getComputedToolbarExpandedHeight(Activity activity,int toolbarBelowTitleLayoutHeight){
        String text="";
        Typeface typeface=Typeface.DEFAULT;
        float toolbarExpandedTitleTextSize=activity.getResources().getDimension(R.dimen.toolbar_expanded_title_text_size);
        int activityNormalMargin=(int)activity.getResources().getDimension(R.dimen.activity_normal_margin);
        int toolbarExpandedTopPadding=(int)activity.getResources().getDimension(R.dimen.toolbar_expanded_title_top_padding);
        int toolbarExpandedHeight=UsefulFunctions.getTextViewHeight(
                activity,
                text,
                typeface,
                toolbarExpandedTitleTextSize,
                activityNormalMargin,
                toolbarExpandedTopPadding,
                activityNormalMargin,
                toolbarBelowTitleLayoutHeight
        );
        return toolbarExpandedHeight;
    }

    private void setComputedToolbarExpandedHeight(Activity activity, int bottomLayoutHeight, int toolbarExpandedHeight){
        CollapsingToolbarLayout collapsingToolbarLayout=(net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout)activity.findViewById(R.id.collapsing_toolbar_layout);
        AppBarLayout.LayoutParams collapsingToolbarParams = (AppBarLayout.LayoutParams)collapsingToolbarLayout.getLayoutParams();
        collapsingToolbarParams.height = toolbarExpandedHeight;
        collapsingToolbarLayout.setExpandedTitleMarginBottom(bottomLayoutHeight);
    }

    private void setWeatherLayoutTopPadding(Activity activity, int toolbarExpandedHeight){
        //// TODO: move to weatherlayoutinitializer
        RelativeLayout weatherLayout=(RelativeLayout)activity.findViewById(R.id.weather_layout);
        weatherLayout.setPadding(0,toolbarExpandedHeight,0,0);
    }

    public int getToolbarExpandedHeight() {
        return toolbarExpandedHeight;
    }
}
