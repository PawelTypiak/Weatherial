package paweltypiak.matweather.introActivityInitializing;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import paweltypiak.matweather.R;
import paweltypiak.matweather.introActivityInitializing.introActivityFragmentInitializing.IntroActivityUnitsFragment;
import paweltypiak.matweather.introActivityInitializing.introActivityFragmentInitializing.introActivityLocationFragmentInitializing.IntroActivityLocationFragment;

class IntroActivityNextButtonInitializer {

    private CardView nextButton;
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
            setStartButtonOnClickListener(activity);
        }
        else{
            //next button invisible when next launch
            nextButton.setVisibility(View.INVISIBLE);
        }
    }

    private void setStartButtonOnClickListener(final Activity activity){
        setButtonText(activity);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNextStep(activity);
            }
        });
    }

    private void setButtonText(Activity activity){
        TextView buttonTextView=(TextView)activity.findViewById(R.id.intro_activity_button_text);
        buttonTextView.setText(activity.getString(R.string.first_launch_button_continue_text));
    }

    private void setNextStep(Activity activity){
        if(step==0){
            Log.d("step", ""+step);
            initializeConfigurationFragment(activity);
            step=1;
        }
        else if (step == 1) {
            Log.d("step", "" + step);
            insertUnitsFragment(activity);
            step = 2;
        }
        else if(step==2){
            Log.d("step", ""+step);
            insertLocationFragment(activity);
            step=3;
        }
        else if(step==3){
            Log.d("step", ""+step);
            initializeLoadingLocation();
            step=4;
        }
        else if(step==4){
            Log.d("step", ""+step);
            initializeLoadingLocation();
            step=5;
        }
    }

    private void initializeConfigurationFragment(final Activity activity){
        final FrameLayout startFragmentLayout
                =(FrameLayout)activity.findViewById(R.id.intro_activity_main_fragment_placeholder);
        long transitionTime=200;
        startFragmentLayout.animate()
                .alpha(0f)
                .setDuration(transitionTime)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        fragmentInsertionInitializer.insertConfigurationFragment(activity);
                    }
                });
        setStartButtonDisabled();
    }

    private void insertUnitsFragment(Activity activity){
        fragmentInsertionInitializer.
                setNestedConfigurationFragment(
                        new IntroActivityUnitsFragment(),
                        activity.getString(R.string.intro_activity_units_fragment_tag)
                );
    }

    private void insertLocationFragment(Activity activity){
        fragmentInsertionInitializer.
                setNestedConfigurationFragment(
                        new IntroActivityLocationFragment(),
                        activity.getString(R.string.intro_activity_defeault_location_fragment_tag)
                );
    }

    private void initializeLoadingLocation(){
        fragmentInsertionInitializer.
                getConfigurationFragment().
                initializeLoadingLocation(nextButton);
    }

    void setNextButtonVisible(){
        nextButton.setVisibility(View.VISIBLE);
    }

    private void setStartButtonDisabled(){
        nextButton.setClickable(false);
    }

    void setStartButtonEnabled(){
        nextButton.setClickable(true);
    }

    int getStep() {
        return step;
    }

    void setStep(int step) {
        this.step = step;
    }
}
