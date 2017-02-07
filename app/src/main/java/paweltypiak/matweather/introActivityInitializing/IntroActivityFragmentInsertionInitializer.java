package paweltypiak.matweather.introActivityInitializing;

import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import paweltypiak.matweather.R;
import paweltypiak.matweather.introActivityInitializing.introActivityFragmentInitializing.introActivityConfigurationFragmentInitializing.IntroActivityConfigurationFragment;
import paweltypiak.matweather.introActivityInitializing.introActivityFragmentInitializing.IntroActivityStartFragment;

class IntroActivityFragmentInsertionInitializer {

    private boolean isFirstLaunch;
    private IntroActivityConfigurationFragment configurationFragment;

    IntroActivityFragmentInsertionInitializer(boolean isFirstLaunch){
        this.isFirstLaunch=isFirstLaunch;
    }

    void initializeStartFragment(Activity activity){
        FragmentTransaction fragmentTransaction
                = ((FragmentActivity)activity).
                getSupportFragmentManager().
                beginTransaction();
        IntroActivityStartFragment startFragment=new IntroActivityStartFragment();
        fragmentTransaction.replace(
                R.id.intro_activity_main_fragment_placeholder,
                startFragment,
                activity.getString(R.string.intro_activity_start_fragment_tag));
        fragmentTransaction.commit();
    }

    void insertConfigurationFragment(Activity activity){
        FragmentTransaction fragmentTransaction
                = ((FragmentActivity)activity).
                getSupportFragmentManager().
                beginTransaction();
        configurationFragment= IntroActivityConfigurationFragment.newInstance(activity,isFirstLaunch);
        fragmentTransaction.replace(
                R.id.intro_activity_main_fragment_placeholder,
                configurationFragment,
                activity.getString(R.string.intro_activity_configuration_fragment_tag));
        fragmentTransaction.commit();
    }

    public void setNestedConfigurationFragment(android.support.v4.app.Fragment nestedFragment,String tag){
        configurationFragment.insertNestedFragment(nestedFragment,tag);
    }

    public IntroActivityConfigurationFragment getConfigurationFragment() {
        return configurationFragment;
    }
}
