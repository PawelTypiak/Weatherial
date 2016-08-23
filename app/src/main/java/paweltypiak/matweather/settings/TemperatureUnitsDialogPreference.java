package paweltypiak.matweather.settings;


import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import paweltypiak.matweather.R;
import paweltypiak.matweather.usefulClasses.SharedPreferencesModifier;

public class TemperatureUnitsDialogPreference extends UnitsDialogPreference{

    public TemperatureUnitsDialogPreference(Context context, AttributeSet attributeSet){
        super(context,attributeSet);
        setUnitId(0);
        setUnitDialogTitle();
        setPreferenceSummary();
    }
}
