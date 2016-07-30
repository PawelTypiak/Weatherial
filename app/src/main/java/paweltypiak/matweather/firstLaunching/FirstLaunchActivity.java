package paweltypiak.matweather.firstLaunching;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import paweltypiak.matweather.DialogInitializer;
import paweltypiak.matweather.R;
import paweltypiak.matweather.UsefulFunctions;

public class FirstLaunchActivity extends AppCompatActivity  implements FirstLaunchLoadingFragment.ChooseLocationAgainListener{

    private CardView startButtonCardView;
    private AlertDialog exitDialog;
    private FragmentTransaction fragmentTransaction;
    private FirstLaunchConfigurationFragment configurationFragment;
    private boolean isFirstLaunch;
    int step=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_launch);
        isFirstLaunch =isFirstLaunch();
        if(isFirstLaunch ==true) initializeFirstLaunch();
        else initializeNextLaunch();
    }

    private void setButtonIcon(){
        ImageView buttonImageView;
        buttonImageView=(ImageView)findViewById(R.id.first_launch_button_image);
        Picasso.with(getApplicationContext()).load(R.drawable.next_arrow_icon).transform(new UsefulFunctions().new setDrawableColor(this.getResources().getColor(R.color.white))).fit().centerInside().into(buttonImageView);
    }
    private void setButtonText(){
        TextView buttonTextView=(TextView)findViewById(R.id.first_launch_button_text);
        buttonTextView.setText(getString(R.string.first_launch_layout_continue_button));
    }

    private boolean isFirstLaunch(){
        SharedPreferences sharedPreferences=UsefulFunctions.getSharedPreferences(this);
        String savedLocation=sharedPreferences.getString(getString(R.string.shared_preferences_first_location_key),"");
        Log.d("saved shared", "saved: "+savedLocation);
        if(savedLocation.equals("")) return true;
        else {
            return false;
        }
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
        UsefulFunctions.setViewVisible(startButtonCardView);
        setNestedConfigurationFragment(new FirstLaunchLocationFragment(),"LocationFragment");
    }

   /* private void getConfigurationFragment(){
        configurationFragment = (FirstLaunchConfigurationFragment)
                getSupportFragmentManager().findFragmentByTag("ConfigurationFragment");
    }*/

    private void initializeDialog(){
        DialogInitializer dialogInitializer=new DialogInitializer(this);
        exitDialog=dialogInitializer.initializeExitDialog(false,null);
    }

    private void initializeFirstLaunch(){
        initializeStartFragment();
        initializeDialog();
        setStartButton();

    }

    private void initializeNextLaunch(){
        initializeConfigurationFragment(isFirstLaunch);
        setStartButton();
    }

    private void setStartButton(){
        startButtonCardView = (CardView) findViewById(R.id.first_launch_button_cardView);
        if(isFirstLaunch ==true){
            setButtonIcon();
            setStartButtonOnClickListener(startButtonCardView);
        }
        else{
            UsefulFunctions.setViewInvisible(startButtonCardView);
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
                    configurationFragment.initializeLoadingLocation();
                    UsefulFunctions.setViewInvisible(startButtonCardView);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(isFirstLaunch ==true) exitDialog.show();
    }
}
