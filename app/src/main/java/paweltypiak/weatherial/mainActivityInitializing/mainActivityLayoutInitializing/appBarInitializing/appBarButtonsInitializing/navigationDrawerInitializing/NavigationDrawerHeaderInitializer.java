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
package paweltypiak.weatherial.mainActivityInitializing.mainActivityLayoutInitializing.appBarInitializing.appBarButtonsInitializing.navigationDrawerInitializing;

import android.app.Activity;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.utils.UsefulFunctions;

class NavigationDrawerHeaderInitializer {

    NavigationDrawerHeaderInitializer(Activity activity, NavigationView navigationView){
        View headerView=navigationView.getHeaderView(0);
        ImageView logoImageView=(ImageView)headerView.findViewById(R.id.navigation_drawer_header_logo_image);
        setLogoImageViewHeight(activity,logoImageView);
        setLogoDrawable(activity,logoImageView);
    }

    private void setLogoImageViewHeight(Activity activity,ImageView logoImageView){
        LinearLayout.LayoutParams logoImageViewLayoutParams
                =(LinearLayout.LayoutParams)logoImageView.getLayoutParams();
        final float logoHeightMultiplier=0.3f;
        int imageViewHeight= (int)(UsefulFunctions.getScreenHeight(activity)*logoHeightMultiplier);
        logoImageViewLayoutParams.height=imageViewHeight;
        logoImageView.setLayoutParams(logoImageViewLayoutParams);
    }

    private void setLogoDrawable(Activity activity, ImageView logoImageView){
        logoImageView.setImageDrawable(ContextCompat.getDrawable(activity,R.drawable.logo_navigation_drawer));
    }
}
