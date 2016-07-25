package paweltypiak.matweather.firstLaunching;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import paweltypiak.matweather.DialogInitializer;
import paweltypiak.matweather.MainActivity;
import paweltypiak.matweather.R;
import paweltypiak.matweather.UsefulFunctions;

public class FirstLaunchActivity extends AppCompatActivity {

    AlertDialog exitDialog;
    FragmentTransaction fragmentTransaction;
    int step=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_launch);
        if (savedInstanceState == null) initializeFragment(new FirstLaunchStartFragment(),"StartFragment");
        initializeDialog();
        setMainActivityLayout();
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

    private void initializeFragment(Fragment fragment, String tag){

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.first_launch_main_fragment_placeholder, fragment, tag);
        fragmentTransaction.commit();
    }

    private void initializeDialog(){
        DialogInitializer dialogInitializer=new DialogInitializer(this);
        exitDialog=dialogInitializer.initializeExitDialog();
    }



    private void setMainActivityLayout() {
        setButtonIcon();
        final CardView startButtonCardView = (CardView) findViewById(R.id.first_launch_button_cardView);
        startButtonCardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setButtonText();
                if(step==0){
                    initializeFragment(new FirstLaunchConfigurationFragment(),"ConfigurationFragment");
                    step=1;
                }
                else if(step==1){
                    setNestedConfigurationFragment(new FirstLaunchUnitsFragment(),"UnitsFragment");
                    step=2;
                }
                else if(step==2){
                    setNestedConfigurationFragment(new FirstLaunchLocationFragment(),"LocationFragment");
                    step=3;
                }
                else if(step==3){
                    Intent intent = new Intent(FirstLaunchActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }


    private void setNestedConfigurationFragment(android.support.v4.app.Fragment nestedFragment,String tag){
        FirstLaunchConfigurationFragment configurationFragment = (FirstLaunchConfigurationFragment)
                getSupportFragmentManager().findFragmentByTag("ConfigurationFragment");
        configurationFragment.insertNestedFragment(nestedFragment,tag);
    }


    @Override
    public void onBackPressed() {
       exitDialog.show();
    }
}
