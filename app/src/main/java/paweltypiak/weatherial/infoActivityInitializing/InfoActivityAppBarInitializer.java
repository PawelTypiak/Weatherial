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
package paweltypiak.weatherial.infoActivityInitializing;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.squareup.picasso.Picasso;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.rateDialogInitializing.RateDialogInitializer;
import paweltypiak.weatherial.utils.UsefulFunctions;

class InfoActivityAppBarInitializer {

    private AlertDialog rateDialog;

    InfoActivityAppBarInitializer(Activity activity){
        setLogo(activity);
        initializeBackButton(activity);
        initializeRateButton(activity);
    }

    private void setLogo(Activity activity){
        ImageView logoImageView=(ImageView)activity.findViewById(R.id.info_activity_logo_image);
        setLogoImageViewsHeight(activity,logoImageView);
        loadLogoImageView(activity,logoImageView);
    }

    private void setLogoImageViewsHeight(Activity activity,ImageView logoImageView){
        float logoHeightMultiplier =0.25f;
        int logoImageViewHeight
                = (int)(UsefulFunctions.getScreenHeight(activity) * logoHeightMultiplier);
        RelativeLayout.LayoutParams logoImageViewLayoutParams
                = (RelativeLayout.LayoutParams)logoImageView.getLayoutParams();
        logoImageViewLayoutParams.height = logoImageViewHeight;
        logoImageView.setLayoutParams(logoImageViewLayoutParams);
    }

    private void loadLogoImageView(Activity activity,ImageView logoImageView){
        Picasso.with(activity).load(R.drawable.logo_info_activity).fit().centerInside().noFade().into(logoImageView);
    }

    private void initializeBackButton(final Activity activity){
        ImageView backButtonImageView=(ImageView)activity.findViewById(R.id.info_activity_back_button_image);
        backButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.onBackPressed();
            }
        });
    }

    private void initializeRateButton(final Activity activity){
        ImageView rateButtonImageView=(ImageView)activity.findViewById(R.id.info_activity_rate_button_image);
        rateButtonImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rateDialog==null){
                    rateDialog= RateDialogInitializer.getRateDialog(activity);
                }
                rateDialog.show();
            }
        });
    }
}
