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
package paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.appBarInitializing;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.design.widget.AppBarLayout;

import net.opacapp.multilinecollapsingtoolbar.CollapsingToolbarLayout;

import paweltypiak.weatherial.R;
import paweltypiak.weatherial.utils.UsefulFunctions;

public class AppBarLayoutDimensionsSetter {

    private int toolbarExpandedHeight;

    public AppBarLayoutDimensionsSetter(Activity activity){
        int bottomLayoutHeight= getToolbarBelowTitleLayoutHeight(activity);
        toolbarExpandedHeight = getComputedToolbarExpandedHeight(activity,bottomLayoutHeight);
        setComputedToolbarExpandedHeight(activity,bottomLayoutHeight,toolbarExpandedHeight);
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

    public int getToolbarExpandedHeight() {
        return toolbarExpandedHeight;
    }
}
