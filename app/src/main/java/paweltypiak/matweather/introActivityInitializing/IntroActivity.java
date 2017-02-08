package paweltypiak.matweather.introActivityInitializing;

import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import paweltypiak.matweather.dialogsInitializing.dialogInitializers.exitDialogInitializing.ExitDialogInitializer;
import paweltypiak.matweather.R;
import paweltypiak.matweather.introActivityInitializing.introActivityFragmentInitializing.introActivityConfigurationFragmentInitializing.OnSettingsFragmentViewCreatedListener;
import paweltypiak.matweather.introActivityInitializing.introActivityFragmentInitializing.introActivityLocationFragmentInitializing.ShowLocationFragmentAgainListener;
import paweltypiak.matweather.usefulClasses.SharedPreferencesModifier;

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
    public void showLocationFragmentAgain(){
        nextButtonInitializer.setNextButtonVisible();
        nextButtonInitializer.setStep(3);
        //fragmentInsertionInitializer.setNestedConfigurationFragment(new IntroActivityDefaultLocationFragment(),"LocationFragment");
        fragmentInsertionInitializer.
                insertDefaultLocationFragment(this);
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
        //exit dialog availiblity depending on the current step
        initializeExitDialogShowing();
    }

    private void initializeExitDialogShowing(){
        if(isFirstLaunch ==true){
            if(exitDialog==null){
                exitDialog= ExitDialogInitializer.getExitDialog(this,1,null);
            }
            int step=nextButtonInitializer.getStep();
            if(step>3){
                int locationOption= getSelectedDefaultLocationOption();
                if(locationOption==1&&step==4) exitDialog.show();
            }
            else {
                exitDialog.show();
            }
        }
    }

    private int getSelectedDefaultLocationOption(){
        return fragmentInsertionInitializer.
                getConfigurationFragment().getSelectedDefaultLocationOption();
    }
}
