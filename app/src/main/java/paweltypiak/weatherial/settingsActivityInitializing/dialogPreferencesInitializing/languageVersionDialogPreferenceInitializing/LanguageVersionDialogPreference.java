package paweltypiak.weatherial.settingsActivityInitializing.dialogPreferencesInitializing.languageVersionDialogPreferenceInitializing;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import paweltypiak.weatherial.R;
import paweltypiak.weatherial.settingsActivityInitializing.SettingsActivity;
import paweltypiak.weatherial.settingsActivityInitializing.dialogPreferencesInitializing.CustomDialogPreference;
import paweltypiak.weatherial.usefulClasses.SharedPreferencesModifier;
import paweltypiak.weatherial.usefulClasses.UsefulFunctions;

public class LanguageVersionDialogPreference extends CustomDialogPreference {

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
    }

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
        recreateSettings();
    }

    private void recreateSettings(){
        OnLanguageVersionPreferenceChangeListener onLanguageVersionPreferenceChangeListener
                = ((SettingsActivity)getContext()).getRefreshSettingsFragmentListener();
        onLanguageVersionPreferenceChangeListener.recreateSettings();
    }

    protected void setPreferenceSummary(){
        int languageVersion=SharedPreferencesModifier.getLanguageVersion(getContext());
        if(languageVersion==0){
            setSummary(getContext().getString(R.string.language_version_english));
        }
        else if(languageVersion==1){
            setSummary(getContext().getString(R.string.language_version_polish));
        }
    }
}
