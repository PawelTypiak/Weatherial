package paweltypiak.matweather.settings;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import paweltypiak.matweather.R;
import paweltypiak.matweather.usefulClasses.SharedPreferencesModifier;

public class LanguageVersionDialogPreference extends CustomDialogPreference{

    private int choosenOption=-1;

    public LanguageVersionDialogPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setDialogTitle(getContext().getString(R.string.preferences_language_version_title));
        setPreferenceSummary();
    }

    protected void buildRadioGroup(RadioGroup radioGroup){
        String englishRadioButtonText=getContext().getString(R.string.language_version_english);
        final int englishRadioButtonId=R.id.language_version_dialog_english_radio_button_id;
        int englishRadioButtonBottomMargin=16;
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
            choosenOption=0;
        }
        else if(languageVersion==1) {
            polishRadioButton.setChecked(true);
            choosenOption=1;
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == englishRadioButtonId) {
                    choosenOption=0;
                }
                else if (i == polishRadioButtonId) {
                    choosenOption=1;
                }
            }
        });
    };

    protected void onPositiveResult(){
        if(choosenOption==0){
            SharedPreferencesModifier.setLanguage(getContext(),0);
            setSummary(getContext().getString(R.string.language_version_english));
        }
        else if(choosenOption==1){
            SharedPreferencesModifier.setLanguage(getContext(),1);
            setSummary(getContext().getString(R.string.language_version_polish));
        }
        Log.d("change preference",getTitle()+ " preference changed to: "+getSummary());
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
