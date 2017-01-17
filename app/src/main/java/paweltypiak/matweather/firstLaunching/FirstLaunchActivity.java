package paweltypiak.matweather.firstLaunching;

import android.app.AlertDialog;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import paweltypiak.matweather.dialogsInitializing.ExitDialogInitializer;
import paweltypiak.matweather.R;
import paweltypiak.matweather.usefulClasses.SharedPreferencesModifier;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

public class FirstLaunchActivity extends AppCompatActivity  implements FirstLaunchLoadingFragment.SelectLocationAgainListener {

    private CardView nextCardViewButton;
    private AlertDialog exitDialog;
    private FragmentTransaction fragmentTransaction;
    private FirstLaunchConfigurationFragment configurationFragment;
    private boolean isFirstLaunch;
    int step=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_launch);
        isFirstLaunch= SharedPreferencesModifier.getIsFirstLaunch(this);
        if(isFirstLaunch ==true) initializeFirstLaunch();
        else initializeNextLaunch();
    }

    private void setLanguageVersion(){
        //set saved language version
        int languageVersion=SharedPreferencesModifier.getLanguageVersion(this);
        if(languageVersion!=-1) UsefulFunctions.setLocale(this,languageVersion);
    }

    private void setButtonIcon(){
        ImageView buttonImageView;
        buttonImageView=(ImageView)findViewById(R.id.first_launch_button_image);
        Picasso.with(getApplicationContext()).load(R.drawable.next_arrow_icon).transform(new UsefulFunctions().new setDrawableColor(ContextCompat.getColor(this,R.color.white))).fit().centerInside().into(buttonImageView);
    }
    private void setButtonText(){
        TextView buttonTextView=(TextView)findViewById(R.id.first_launch_button_text);
        buttonTextView.setText(getString(R.string.first_launch_button_continue_text));
    }

    private void initializeConfigurationFragment(boolean isFirstLaunch){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        configurationFragment=FirstLaunchConfigurationFragment.newInstance(isFirstLaunch,this);
        fragmentTransaction.replace(R.id.first_launch_main_fragment_placeholder, configurationFragment, "ConfigurationFragment");
        fragmentTransaction.commit();
    }

    private void initializeStartFragment(){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        FirstLaunchStartFragment startFragment=new FirstLaunchStartFragment();
        fragmentTransaction.replace(R.id.first_launch_main_fragment_placeholder, startFragment, "StartFragment");
        fragmentTransaction.commit();
    }

    private void setNestedConfigurationFragment(android.support.v4.app.Fragment nestedFragment,String tag){
        configurationFragment.insertNestedFragment(nestedFragment,tag);
    }

    public void showLocationFragment(){
        UsefulFunctions.setViewVisible(nextCardViewButton);
        setNestedConfigurationFragment(new FirstLaunchLocationFragment(),"LocationFragment");
        step=3;
    }

    private void initializeDialogs(){
        /*DialogInitializer dialogInitializer=new DialogInitializer(this);
        exitDialog=dialogInitializer.initializeExitDialog(1,null);*/
        exitDialog= ExitDialogInitializer.initializeExitDialog(this,1,null);
    }

    private void initializeFirstLaunch(){
        //first launch of application
        Log.d("launch", "first launch");
        initializeStartFragment();
        initializeDialogs();
        setStartButton();
    }

    private void initializeNextLaunch(){
        //every next launch of application
        Log.d("launch", "next launch");
        setLanguageVersion();
        initializeConfigurationFragment(isFirstLaunch);
        setStartButton();
    }

    private void setStartButton(){
        nextCardViewButton = (CardView) findViewById(R.id.first_launch_button_cardView);
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
                    setNestedConfigurationFragment(new FirstLaunchUnitsFragment(), "UnitsFragment");
                    step = 2;
                }
                else if(step==2){
                    Log.d("step", ""+step);
                    setNestedConfigurationFragment(new FirstLaunchLocationFragment(),"LocationFragment");
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

    @Override
    public void onBackPressed() {
        //exit dialog availiblity depending on the current step
        if(isFirstLaunch ==true){
            if(step>3){
                int locationOption=configurationFragment.getSelectedDefeaultLocationOption();
                if(locationOption==1&&step==4) exitDialog.show();
            }
            else {
                initializeDialogs();
                exitDialog.show();
            }
        }
    }
}
