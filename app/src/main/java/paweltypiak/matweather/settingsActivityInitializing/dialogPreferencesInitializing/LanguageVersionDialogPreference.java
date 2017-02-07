package paweltypiak.matweather.settingsActivityInitializing.dialogPreferencesInitializing;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import paweltypiak.matweather.R;
import paweltypiak.matweather.settingsActivityInitializing.SettingsActivity;
import paweltypiak.matweather.usefulClasses.SharedPreferencesModifier;
import paweltypiak.matweather.usefulClasses.UsefulFunctions;

public class LanguageVersionDialogPreference extends CustomDialogPreference{

    private int selectedOption =-1;

    public LanguageVersionDialogPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setDialogTitle(getContext().getString(R.string.preferences_language_version_title));
        setPreferenceSummary();
    }

    protected void buildRadioGroup(RadioGroup radioGroup){
        String englishRadioButtonText=getContext().getString(R.string.language_version_english);
        final int englishRadioButtonId=R.id.language_version_dialog_english_radio_button_id;
        int englishRadioButtonBottomMargin=(int)getContext().getResources().getDimension(R.dimen.radio_button_bottom_margin);
        RadioButton englishRadioButton=setRadioButtonLayout(englishRadioButtonText,englishRadioButtonId,englishRadioButtonBottomMargin);
        radioGroup.addView(englishRadioButton);
        String polishRadioButtonText=getContext().getString(R.string.language_version_polish);
        final int polishRadioButtonId=R.id.language_version_dialog_polish_radio_button_id;
        int polishRadioButtonBottomMargin=0;
        RadioButton polishRadioButton=setRadioButtonLayout(polishRadioButtonText,polishRadioButtonId,polishRadioButtonBottomMargin);
        radioGroup.addView(polishRadioButton);
        int languageVersion= SharedPreferencesModifier.getLanguageVersion(getContext());
        if(languageVersion==0) {
            englishRadioButton.setChecked(true);
            selectedOption =0;
        }
        else if(languageVersion==1) {
            polishRadioButton.setChecked(true);
            selectedOption =1;
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == englishRadioButtonId) {
                    selectedOption =0;
                }
                else if (i == polishRadioButtonId) {
                    selectedOption =1;
                }
            }
        });
    };

    protected void onPositiveResult(){
        if(selectedOption ==0){
            SharedPreferencesModifier.setLanguage(getContext(),0);
            UsefulFunctions.setLocale(getContext(),0);
            setSummary(getContext().getString(R.string.language_version_english));
        }
        else if(selectedOption ==1){
            SharedPreferencesModifier.setLanguage(getContext(),1);
            UsefulFunctions.setLocale(getContext(),1);
            setSummary(getContext().getString(R.string.language_version_polish));
        }
        SettingsActivity.setLanguagePreferencesChanged(true);
        //callback for refreshing view
        RecreateSettingsListener recreateSettingsListener = SettingsActivity.getRefreshSettingsFragmentListener();
        recreateSettingsListener.recreateSettings();
        Log.d("changed_preference",getTitle()+ " preference changed to: "+getSummary());
    }

    protected void setPreferenceSummary(){
        int languageVersion=SharedPreferencesModifier.getLanguageVersion(getContext());
        Log.d("language_ver", ""+languageVersion);
        if(languageVersion==0){
            setSummary(getContext().getString(R.string.language_version_english));
        }
        else if(languageVersion==1){
            setSummary(getContext().getString(R.string.language_version_polish));
        }
    }

    public interface RecreateSettingsListener {
        //listener for recreating settings activity
        void recreateSettings();
    }
}
