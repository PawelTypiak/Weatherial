package paweltypiak.matweather.settings;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import paweltypiak.matweather.R;
import paweltypiak.matweather.usefulClasses.SharedPreferencesModifier;

public class UnitsDialogPreference extends CustomDialogPreference{

    private int selectedOption =-1;
    private int unitId=-1;
    private String[] unitsArray;

    public UnitsDialogPreference(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
    }

    protected void setUnitId(int unitId) {
        this.unitId = unitId;
    }

    protected void setUnitDialogTitle(){
        String unitsNames[]=getContext().getResources().getStringArray(R.array.preferences_units_names_array);
        setDialogTitle(unitsNames[unitId]);
    }

    private int[] getUnitsPreferences(){
        return SharedPreferencesModifier.getUnits(getContext());
    }

    protected void onPositiveResult(){
        int[] unitPreferences=getUnitsPreferences();
        if(selectedOption ==0){
            unitPreferences[unitId]=0;
            SharedPreferencesModifier.setUnits(getContext(),unitPreferences);
            setSummary(unitsArray[0]);
        }
        else if(selectedOption ==1){
            unitPreferences[unitId]=1;
            for(int i=0;i<4;i++){
            }
            SharedPreferencesModifier.setUnits(getContext(),unitPreferences);
            setSummary(unitsArray[1]);
        }
        Log.d("changed_preference",getTitle()+ " preference changed to: "+getSummary());
        Settings.setUnitsPreferencesChanged(true);
    }

    protected void setPreferenceSummary(){
        unitsArray=getContext().getResources().getStringArray(getContext().getResources().getIdentifier("units_array_"+unitId, "array", getContext().getPackageName()));
        if(getUnitsPreferences()[unitId]==0){
            setSummary(unitsArray[0]);
        }
        else if(getUnitsPreferences()[unitId]==1){
            setSummary(unitsArray[1]);
        }
    }

    private int[] getRadioButtonIds(){
        TypedArray typedArray = getContext().getResources().obtainTypedArray(getContext().getResources().getIdentifier("units_ids_"+unitId, "array", getContext().getPackageName()));
        int[] ids = new int[typedArray.length()];
        for(int ind=0; ind<typedArray.length(); ind++){
            ids[ind] = typedArray.getResourceId(ind,0);
        }
        typedArray.recycle();
        return ids;
    }

    protected void buildRadioGroup(RadioGroup radioGroup){
        final int unitRadioButtonIds[]=getRadioButtonIds();
        String unitRadioButtonTexts[]=unitsArray;
        int [] unitRadioButtonMargin=new int[2];
        unitRadioButtonMargin[0]=(int)getContext().getResources().getDimension(R.dimen.radio_button_bottom_margin);
        unitRadioButtonMargin[1]=0;
        for(int i=0;i<2;i++){
            RadioButton unitRadioButton=setRadioButtonLayout(unitRadioButtonTexts[i],unitRadioButtonIds[i],unitRadioButtonMargin[i]);
            radioGroup.addView(unitRadioButton);
            if(getUnitsPreferences()[unitId]==i) {
                unitRadioButton.setChecked(true);
                selectedOption =i;
            }
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == unitRadioButtonIds[0] ) {
                    selectedOption =0;
                }
                else if (i == unitRadioButtonIds[1]) {
                    selectedOption =1;
                }
            }
        });
    }
}
