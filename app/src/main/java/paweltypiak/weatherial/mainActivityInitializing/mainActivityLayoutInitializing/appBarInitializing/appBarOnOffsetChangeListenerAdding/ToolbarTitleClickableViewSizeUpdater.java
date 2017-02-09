package paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.appBarInitializing.appBarOnOffsetChangeListenerAdding;

import android.app.Activity;
import android.graphics.Typeface;
import android.view.View;
import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.appBarInitializing.AppBarLayoutDataInitializer;
import paweltypiak.weatherial.usefulClasses.UsefulFunctions;

public class ToolbarTitleClickableViewSizeUpdater implements  AppBarLayoutDataInitializer.OnAppBarStringsChangeListener {

    private View clickableView;
    private int toolbarExpandedTitleTextViewMaxWidth;
    private int toolbarCollapsedTitleTextViewMaxWidth;
    private int toolbarTitleClickableViewHorizontalPadding;
    private int toolbarExpandedTitleTopMargin;
    private int toolbarExpandedTitleTextSize;
    private int toolbarCollapsedTitleTextSize;
    private int toolbarExpandedTitleTextViewHeight;
    private int toolbarExpandedTitleTextViewWidth;
    private int toolbarTitleTextViewHeightDifference;
    private int toolbarTitleTextViewWidthDifference;
    private Typeface robotoMediumTypeface;
    private float lastScrollPrecentage;

    public ToolbarTitleClickableViewSizeUpdater(Activity activity){
        findClickableView(activity);
        initializeToolbarTitleDimensions(activity);
        initializeAppBarTitleTypeFace(activity);
    }

    private void findClickableView(Activity activity){
        clickableView=activity.findViewById(R.id.toolbar_layout_title_clickable_view);
    }

    private void initializeToolbarTitleDimensions(Activity activity){
        toolbarExpandedTitleTopMargin=(int)activity.getResources().getDimension(R.dimen.toolbar_expanded_title_top_padding);
        toolbarTitleClickableViewHorizontalPadding=(int) activity.getResources().getDimension(R.dimen.toolbar_clickable_view_horizontal_padding);
        toolbarExpandedTitleTextSize=(int) activity.getResources().getDimension(R.dimen.toolbar_expanded_title_text_size);
        toolbarCollapsedTitleTextSize=(int) activity.getResources().getDimension(R.dimen.toolbar_collapsed_title_text_size);
        toolbarExpandedTitleTextViewMaxWidth = getToolbarExpandedTitleTextViewMaxWidth(activity);
        toolbarCollapsedTitleTextViewMaxWidth = getToolbarCollapsedTitleTextViewMaxWidth(activity);
        toolbarExpandedTitleTextViewHeight=getToolbarExpandedTitleTextViewHeight(activity);
        toolbarTitleTextViewHeightDifference = getToolbarTitleTextViewHeightDifference(activity);
    }

    private int getToolbarExpandedTitleTextViewMaxWidth(Activity activity){
        int screenWidth=UsefulFunctions.getScreenWidth(activity);
        int activityNormalMargin=(int) activity.getResources().getDimension(R.dimen.activity_normal_margin);
        int toolbarExpandedTitleTextViewMaxWidth=screenWidth-2*activityNormalMargin;
        return toolbarExpandedTitleTextViewMaxWidth;
    }

    private int getToolbarCollapsedTitleTextViewMaxWidth(Activity activity){
        int screenWidth=UsefulFunctions.getScreenWidth(activity);
        int activityHugeMargin=(int) activity.getResources().getDimension(R.dimen.activity_huge_margin);
        int toolbarCollapsedTitleTextViewMaxWidth=screenWidth-2*activityHugeMargin;
        return toolbarCollapsedTitleTextViewMaxWidth;
    }

    private int getToolbarExpandedTitleTextViewHeight(Activity activity){
        int toolbarExpandedTitleTextViewHeight =UsefulFunctions.getTextViewHeight(
                activity,
                "",
                robotoMediumTypeface,
                toolbarExpandedTitleTextSize,
                toolbarTitleClickableViewHorizontalPadding,
                0,
                toolbarTitleClickableViewHorizontalPadding,
                0
        );
        return toolbarExpandedTitleTextViewHeight;
    }

    private int getToolbarTitleTextViewHeightDifference(Activity activity){
        int verticalDifference= toolbarExpandedTitleTextViewHeight-getToolbarCollapsedTitleTextViewHeight(activity);
        return verticalDifference;
    }

    private int getToolbarCollapsedTitleTextViewHeight(Activity activity){
        int toolbarCollapsedTitleTextViewHeight=(int)activity.getResources().getDimension(R.dimen.toolbar_collapsed_height);
        return toolbarCollapsedTitleTextViewHeight;
    }

    private void initializeAppBarTitleTypeFace(Activity activity){
        robotoMediumTypeface=Typeface.createFromAsset(activity.getAssets(), "Roboto-Medium.ttf");
    }

    //// onAppBarOffsetChange

    public void updateToolbarTitleSize(float scrollPercentage){
        lastScrollPrecentage=scrollPercentage;
        int clickableViewTopMargin= getToolbarTitleClickableViewTopMargin(scrollPercentage);
        int clickableViewHeight= getToolbarTitleClickableViewHeight(scrollPercentage);
        int clickableViewWidth= getToolbarTitleClickableViewWidth(scrollPercentage);
        if(clickableViewHeight!=0&&clickableViewWidth!=0){
            setClickableViewLayoutParams(clickableViewTopMargin,clickableViewHeight,clickableViewWidth);
        }
    }

    private int getToolbarTitleClickableViewTopMargin(float percentage){
        int clickableViewTopMargin=(int)(toolbarExpandedTitleTopMargin*(1-percentage));
        return clickableViewTopMargin;
    }

    private int getToolbarTitleClickableViewHeight(float percentage){
        int clickableViewHeight=toolbarExpandedTitleTextViewHeight-(int)(toolbarTitleTextViewHeightDifference*percentage);
        return clickableViewHeight;
    }

    private int getToolbarTitleClickableViewWidth(float percentage){
        int clickableViewWidth=toolbarExpandedTitleTextViewWidth-(int)(toolbarTitleTextViewWidthDifference*percentage);
        return clickableViewWidth;
    }

    private void setClickableViewLayoutParams(int clickableViewTopMargin, int clickableViewHeight, int clickableViewWidth){
        CollapsingToolbarLayout.LayoutParams clickableViewParams=(CollapsingToolbarLayout.LayoutParams)clickableView.getLayoutParams();
        clickableViewParams.height=clickableViewHeight;
        clickableViewParams.width=clickableViewWidth;
        clickableViewParams.topMargin=clickableViewTopMargin;
        clickableView.setLayoutParams(clickableViewParams);
    }

    public AppBarLayoutDataInitializer.OnAppBarStringsChangeListener getOnAppBarStringsChangeListener(){
        return this;
    }

    @Override
    public void onAppBarStringsChanged(Activity activity,String toolbarTitle){
        updateToolbarTitleWidth(activity,toolbarTitle);
        updateToolbarTitleSize(lastScrollPrecentage);
    }

    private void updateToolbarTitleWidth(Activity activity, String toolbarTitle){
        toolbarExpandedTitleTextViewWidth=getToolbarExpandedTitleTextViewWidth(activity,toolbarTitle);
        toolbarTitleTextViewWidthDifference=getToolbarTitleTextViewWidthDifference(activity, toolbarTitle);
    }

    private int getToolbarExpandedTitleTextViewWidth(Activity activity, String collapsingToolbarTitle){
        int toolbarExpandedTitleTextViewSize =UsefulFunctions.getTextViewWidth(
                activity,
                collapsingToolbarTitle,
                robotoMediumTypeface,
                toolbarExpandedTitleTextSize,
                toolbarTitleClickableViewHorizontalPadding,
                0,
                toolbarTitleClickableViewHorizontalPadding,
                0
        );
        if(toolbarExpandedTitleTextViewSize> toolbarExpandedTitleTextViewMaxWidth){
            toolbarExpandedTitleTextViewSize= toolbarExpandedTitleTextViewMaxWidth;
        }
        return toolbarExpandedTitleTextViewSize;
    }

    private int getToolbarTitleTextViewWidthDifference(Activity activity,String collapsingToolbarTitle){
        int horizontalDifference= toolbarExpandedTitleTextViewWidth-getToolbarCollapsedTitleTextViewWidth(activity,collapsingToolbarTitle);
        return horizontalDifference;
    }

    private int getToolbarCollapsedTitleTextViewWidth(Activity activity, String collapsingToolbarTitle){
        int toolbarCollapsedTitleTextViewWidth=UsefulFunctions.getTextViewWidth(
                activity,
                collapsingToolbarTitle,
                robotoMediumTypeface,
                toolbarCollapsedTitleTextSize,
                toolbarTitleClickableViewHorizontalPadding,
                0,
                toolbarTitleClickableViewHorizontalPadding,
                0
        );
        if(toolbarCollapsedTitleTextViewWidth > toolbarCollapsedTitleTextViewMaxWidth){
            toolbarCollapsedTitleTextViewWidth = toolbarCollapsedTitleTextViewMaxWidth;
        }
        return toolbarCollapsedTitleTextViewWidth;
    }
}
