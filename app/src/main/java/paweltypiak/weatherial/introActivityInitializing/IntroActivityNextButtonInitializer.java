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
package paweltypiak.weatherial.introActivityInitializing;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import paweltypiak.weatherial.R;

public class IntroActivityNextButtonInitializer {

    private CardView nextButton;
    private TextView nextButtonTextView;
    private int step;
    private IntroActivityFragmentInsertionInitializer fragmentInsertionInitializer;

    IntroActivityNextButtonInitializer(Activity activity,
                                              IntroActivityFragmentInsertionInitializer fragmentInsertionInitializer){
        findNextButton(activity);
        this.fragmentInsertionInitializer=fragmentInsertionInitializer;
    }

    private void findNextButton(Activity activity){
        nextButton = (CardView) activity.findViewById(R.id.intro_activity_button_cardView);
    }

    void setStartButton(Activity activity,boolean isFirstLaunch){
        if(isFirstLaunch ==true){
            //next button usable when first launch
            initializeStartButton(activity);
        }
        else{
            //next button invisible when next launch
            nextButton.setVisibility(View.INVISIBLE);
        }
    }

    private void initializeStartButton(final Activity activity){
        setNextButtonText(activity,activity.getResources().getString(R.string.intro_activity_next_button_start_text));
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNextStep(activity);
            }
        });
    }

    private void setNextStep(Activity activity){
        if(step==0){
            initializeConfigurationFragment(activity);
            step=1;
        }
        else if (step == 1) {
            insertUnitsFragment(activity);
            step = 2;
        }
        else if(step==2){
            insertDefaultLocationFragment(activity);
            step=3;
        }
        else if(step==3){
            initializeLoadingLocation();
            step=4;
        }
        else if(step==4){
            initializeLoadingLocation();
            step=5;
        }
    }

    private void initializeConfigurationFragment(final Activity activity){
        setStartButtonDisabled();
        setNextButtonText(activity,activity.getString(R.string.intro_activity_next_button_continue_text));
        final FrameLayout fragmentPlaceholderLayout
                =(FrameLayout)activity.findViewById(R.id.intro_activity_main_fragment_placeholder);
        long transitionTime=200;
        fragmentPlaceholderLayout.animate()
                .alpha(0f)
                .setDuration(transitionTime)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        fragmentInsertionInitializer.insertConfigurationFragment(activity);
                    }
                });
    }

    private void setNextButtonText(Activity activity, String text){
        if(nextButtonTextView==null){
            nextButtonTextView=(TextView)activity.findViewById(R.id.intro_activity_button_text);
        }
        nextButtonTextView.setText(text);
    }

    private void insertUnitsFragment(Activity activity){
        fragmentInsertionInitializer.insertUnitsFragment(activity);
    }

    private void insertDefaultLocationFragment(Activity activity){
        fragmentInsertionInitializer.insertDefaultLocationFragment(activity);
    }

    private void initializeLoadingLocation(){
        fragmentInsertionInitializer.
                getConfigurationFragment().
                initializeLoadingLocation();
    }

    void setNextButtonVisible(){
        nextButton.setVisibility(View.VISIBLE);
    }

    public void setNextButtonInvisible(){
        nextButton.setVisibility(View.INVISIBLE);
    }

    private void setStartButtonDisabled(){
        nextButton.setClickable(false);
    }

    void setStartButtonEnabled(){
        nextButton.setClickable(true);
    }

    void setStep(int step) {
        this.step = step;
    }
}
