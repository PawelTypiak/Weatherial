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
