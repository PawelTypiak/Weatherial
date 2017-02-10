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
    private int step;
    private IntroActivityFragmentInsertionInitializer fragmentInsertionInitializer;

    public IntroActivityNextButtonInitializer(Activity activity,
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
        buttonTextView.setText(activity.getString(R.string.intro_activity_button_continue_text));
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

    public void setNextButtonVisible(){
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

    int getStep() {
        return step;
    }

    void setStep(int step) {
        this.step = step;
    }
}
