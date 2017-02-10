package paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.appBarInitializing.appBarOnOffsetChangeListenerAdding;

import android.app.Activity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import paweltypiak.weatherial.R;

class ToolbarViewsTransparencyUpdater {

    private RelativeLayout timezoneLayout;
    private TextView bottomLayout;
    private LinearLayout yahooLogoLayout;

    ToolbarViewsTransparencyUpdater(Activity activity){
        findViews(activity);
    }

    private void findViews(Activity activity){
        timezoneLayout=(RelativeLayout)activity.findViewById(R.id.toolbar_layout_time_layout);
        bottomLayout=(TextView)activity.findViewById(R.id.toolbar_layout_subtitle_text);
        yahooLogoLayout=(LinearLayout)activity.findViewById(R.id.toolbar_layout_yahoo_logo_layout);
    }

    void updateViewsTransparencyOnAppBarOffsetChanged(float scrollPercentage) {
        animateTimeLayout(scrollPercentage);
        animateSecondaryLocationNameTextView(scrollPercentage);
        animateYahooLogoLayout(scrollPercentage);
    }

    private void animateTimeLayout(float percentage){
        final int TIME_LAYOUT_DISAPPEARANCE_TIME_MULTIPLIER =3;
        float timezoneDisappearPercentage=1f-(percentage* TIME_LAYOUT_DISAPPEARANCE_TIME_MULTIPLIER);
        timezoneLayout.setAlpha(timezoneDisappearPercentage);
    }

    private void animateSecondaryLocationNameTextView(float percentage){
        final int SECONDARY_LOCATION_NAME_TEXT_VIEW_DISAPPEARANCE_TIME_MULTIPLIER =3;
        float secondaryLocationNameTextViewDisappearPercentage=1f-(percentage* SECONDARY_LOCATION_NAME_TEXT_VIEW_DISAPPEARANCE_TIME_MULTIPLIER);
        bottomLayout.setAlpha(secondaryLocationNameTextViewDisappearPercentage);
    }

    private void animateYahooLogoLayout(float percentage){
        final int YAHOO_LOGO_LAYOUT_DISAPPEARANCE_TIME_MULTIPLIER =4;
        float yahooLogoLayoutDisappearPercentage=1-(percentage* YAHOO_LOGO_LAYOUT_DISAPPEARANCE_TIME_MULTIPLIER);
        yahooLogoLayout.setAlpha(yahooLogoLayoutDisappearPercentage);
    }
}
