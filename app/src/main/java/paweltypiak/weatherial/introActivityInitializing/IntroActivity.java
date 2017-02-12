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

import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import paweltypiak.weatherial.dialogsInitializing.dialogInitializers.exitDialogInitializing.ExitDialogInitializer;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityConfigurationFragmentInitializing.OnSettingsFragmentViewCreatedListener;
import paweltypiak.weatherial.introActivityInitializing.introActivityFragmentInitializing.introActivityLocationFragmentInitializing.ShowLocationFragmentAgainListener;
import paweltypiak.weatherial.utils.SharedPreferencesModifier;

public class IntroActivity
        extends AppCompatActivity
        implements
        OnSettingsFragmentViewCreatedListener,
        ShowLocationFragmentAgainListener {

    private AlertDialog exitDialog;
    private boolean isFirstLaunch;
    private IntroActivityFragmentInsertionInitializer fragmentInsertionInitializer;
    private IntroActivityNextButtonInitializer nextButtonInitializer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        runDelayedInitializing();
    }

    public IntroActivityFragmentInsertionInitializer getFragmentInsertionInitializer() {
        return fragmentInsertionInitializer;
    }

    public IntroActivityNextButtonInitializer getNextButtonInitializer() {
        return nextButtonInitializer;
    }

    private void runDelayedInitializing(){
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                getIsFirstLaunch();
                initializeFragmentInsertion();
                initializeNextButton();
                initializeLaunchProcess();
            }
        }, 1000);
    }

    private void initializeFragmentInsertion(){
        fragmentInsertionInitializer
                =new IntroActivityFragmentInsertionInitializer(isFirstLaunch);
    }

    private void initializeNextButton(){
        nextButtonInitializer
                =new IntroActivityNextButtonInitializer(IntroActivity.this,fragmentInsertionInitializer);
    }

    private void initializeLaunchProcess(){
        new IntroActivityLaunchProcessInitializer(
                IntroActivity.this,
                isFirstLaunch,
                fragmentInsertionInitializer,
                nextButtonInitializer
        );
    }

    private void getIsFirstLaunch(){
        isFirstLaunch= SharedPreferencesModifier.getIsFirstLaunch(this);
    }

    @Override
    public void showDefaultLocationFragmentAgain(){
        nextButtonInitializer.setNextButtonVisible();
        nextButtonInitializer.setStep(3);
        fragmentInsertionInitializer.insertDefaultLocationFragment(this);
    }

    @Override
    public void fadeInConfigurationFragmentLayout(){
        if(isFirstLaunch!=true){
            LinearLayout mainFragmentLayout=(LinearLayout)findViewById(R.id.intro_activity_layout);
            mainFragmentLayout.setVisibility(View.VISIBLE);
        }
        FrameLayout configurationFragmentLayout
                =(FrameLayout)findViewById(R.id.intro_activity_main_fragment_placeholder);
        long transitionTime=200;
        configurationFragmentLayout.setAlpha(0f);
        configurationFragmentLayout.setVisibility(View.VISIBLE);
        configurationFragmentLayout.animate()
                .alpha(1f)
                .setDuration(transitionTime)
                .setListener(null);
        nextButtonInitializer.setStartButtonEnabled();
    }

    @Override
    public void onBackPressed() {
        //exit dialog availability depending on the current step
        initializeExitDialogShowing();
    }

    private void initializeExitDialogShowing(){
        if(isFirstLaunch ==true){
            if(!fragmentInsertionInitializer.isLoadingFragmentInserted()==true){
                if(exitDialog==null){
                    exitDialog= ExitDialogInitializer.getExitDialog(this,1,null);
                }
                exitDialog.show();
            }
        }
    }
}
