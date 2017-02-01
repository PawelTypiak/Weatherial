package paweltypiak.matweather.introActivityInitializing;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AlertDialog;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import paweltypiak.matweather.dialogsInitializing.dialogInitializers.exitDialogInitializing.ExitDialogInitializer;
import paweltypiak.matweather.R;
import paweltypiak.matweather.introActivityInitializing.introActivityLocationFragmentInitializing.IntroActivityLocationFragment;
import paweltypiak.matweather.introActivityInitializing.introActivityLocationFragmentInitializing.ShowLocationFragmentAgainListener;
import paweltypiak.matweather.usefulClasses.SharedPreferencesModifier;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

public class IntroActivity
        extends AppCompatActivity
        implements ShowLocationFragmentAgainListener {

    private CardView nextCardViewButton;
    private AlertDialog exitDialog;
    private FragmentTransaction fragmentTransaction;
    private IntroActivityConfigurationFragment configurationFragment;
    private boolean isFirstLaunch;
    int step=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        initializeLaunchProcess();
    }

    private void initializeLaunchProcess(){
        isFirstLaunch= SharedPreferencesModifier.getIsFirstLaunch(this);
        if(isFirstLaunch){
            initializeFirstLaunch();
        }
        else{
            initializeNextLaunch();
        }
    }

    private void initializeFirstLaunch(){
        initializeStartFragment();
        setStartButton();
    }

    private void initializeStartFragment(){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        IntroActivityStartFragment startFragment=new IntroActivityStartFragment();
        fragmentTransaction.replace(R.id.intro_activity_main_fragment_placeholder, startFragment, "StartFragment");
        fragmentTransaction.commit();
    }

    private void initializeNextLaunch(){
        //every next launch of application
        Log.d("launch", "next launch");
        setLanguageVersion();
        initializeConfigurationFragment(isFirstLaunch);
        setStartButton();
    }

    private void setLanguageVersion(){
        //set saved language version
        int languageVersion=SharedPreferencesModifier.getLanguageVersion(this);
        if(languageVersion!=-1) UsefulFunctions.setLocale(this,languageVersion);
    }

    private void initializeConfigurationFragment(boolean isFirstLaunch){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        configurationFragment= IntroActivityConfigurationFragment.newInstance(isFirstLaunch,this);
        fragmentTransaction.replace(R.id.intro_activity_main_fragment_placeholder, configurationFragment, "ConfigurationFragment");
        fragmentTransaction.commit();
    }

    private void setStartButton(){
        nextCardViewButton = (CardView) findViewById(R.id.intro_activity_button_cardView);
        if(isFirstLaunch ==true){
            //next button usable when first launch
            setButtonIcon();
            setStartButtonOnClickListener(nextCardViewButton);
        }
        else{
            //next button invisible when next launch
            UsefulFunctions.setViewInvisible(nextCardViewButton);
        }
    }

    private void setStartButtonOnClickListener(final CardView startButtonCardView){
        startButtonCardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setButtonText();
                if(step==0){
                    Log.d("step", ""+step);
                    initializeConfigurationFragment(isFirstLaunch);
                    step=1;
                }
                else if (step == 1) {
                    Log.d("step", "" + step);
                    setNestedConfigurationFragment(new IntroActivityUnitsFragment(), "UnitsFragment");
                    step = 2;
                }
                else if(step==2){
                    Log.d("step", ""+step);
                    setNestedConfigurationFragment(new IntroActivityLocationFragment(),"LocationFragment");
                    step=3;
                }
                else if(step==3){
                    Log.d("step", ""+step);
                    configurationFragment.initializeLoadingLocation(startButtonCardView);
                    step=4;
                }
                else if(step==4){
                    Log.d("step", ""+step);
                    configurationFragment.initializeLoadingLocation(startButtonCardView);
                    step=5;
                }
            }
        });
    }

    private void setButtonIcon(){
        Drawable nextArrowIconDrawable=UsefulFunctions.getColoredDrawable(this,R.drawable.next_arrow_icon,ContextCompat.getColor(this,R.color.white));
        ImageView buttonImageView=(ImageView)findViewById(R.id.intro_activity_button_image);
        buttonImageView.setImageDrawable(nextArrowIconDrawable);
    }
    private void setButtonText(){
        TextView buttonTextView=(TextView)findViewById(R.id.intro_activity_button_text);
        buttonTextView.setText(getString(R.string.first_launch_button_continue_text));
    }

    @Override
    public void showLocationFragmentAgain(){
        UsefulFunctions.setViewVisible(nextCardViewButton);
        setNestedConfigurationFragment(new IntroActivityLocationFragment(),"LocationFragment");
        step=3;
    }

    private void setNestedConfigurationFragment(android.support.v4.app.Fragment nestedFragment,String tag){
        configurationFragment.insertNestedFragment(nestedFragment,tag);
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
            if(step>3){
                int locationOption=configurationFragment.getSelectedDefeaultLocationOption();
                if(locationOption==1&&step==4) exitDialog.show();
            }
            else {
                exitDialog.show();
            }
        }
    }
}
